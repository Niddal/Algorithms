import sys
import pprint
import math
import pprint
import cProfile


'''Analysis of complexity
Space:

G possible dotted rules, N possible start positions in column
yields space complexity of GN for one column and since there are N columns, 
O(Gn^2) space total. 

Runtime:

It takes O(Gn^2 * E) where E = the time to process an entry. How long does it
take to process an entry? There are three ways to process entry: if it's 
terminal then it's constant time because moving the dot over is constant time 
for a (scan). The second way is a predict, which is bounded above by G. 
Finally, an attach takes O(GN) time. 

Hence, runtime is upper bounded by

((1 + G + Gn)*Gn^2) = (G^2N^3) This is a problem for a naive implementation
if the grammar is particularly large, like in the WSJ data set...



OPTIMIZATIONS IMPROVEMENTS: (running on wallstreet-abridged)
								abridged	full
baseline (no optimizations): 	49.723s		~3hrs
Optimization A:					22.726s				(no duplicate predictions)
Optimization B:					7.04s		11 min	(filter out rules for terminals that arent in the sentence)
Optimization C:					1.4s		2m 21s	(customer table)
Optimization D:					1.32		2m 24s  (left corner speedup: prefix table, left ancestor pair table)
'''

###############################################################################
#							global data structures and classes
###############################################################################

rules = []	#list of rules, cannot change

masterRulesTable = {} #maps left hand side nonterminal to list of possible 
					  #right hand sides as tuples
rulesTable = {} # optimization B: filter terminals not in sentence

probTable = {}	# maps grammar rules as tuples to probability of that rule
				# to see if rule exists, check probTable
chart = []

NumWords = 0

prefixTable = {} # Optimization D

leftParentTable = {} # Optimization D

initialRootRules = []


class Column:
	def __init__(self, index):
		self.col = {} #maps tuples to Entry objects
		self.index = index

		# optimization A
		self.predictedLefts = set()
		# optimization C : keep a customer table in each column:
		self.customer = {} 	# maps terminal/nonterminal after the dot in an
						 	#entry's rule to that entire entry itself
						 	# maps entry's rule[dotIndex + 1] to the entry
		# optimization D
		self.leftAncestorPairTable = {}


	def __str__(self):
		s =  'COLUMN: ' + str(self.index) + ' {\n'
		for item in self.col:
			s += str(item) + '\n'
		s += '}\n'
		return s
	# part 3 optimizations:
	# customer = {} 


class Entry:
	def __init__(self, start, rule, dotIndex, weight):
		# column is implicit bc entry resides in a column table
		self.startCol = start #column of where this possible rule started
		self.rule = rule 	#index of rule in the rules list
		self.dotIndex = dotIndex
		self.weight = weight

		# list of packpointer pairs [(p1, p2), (p1, p2)...]
		# backpointer invariant: the left backpointer is the
		# customer and must come from a previous column, while
		# the right backpointer comes from a completed constituent
		# at the top of the current column, which answer the question
		# of how the left parent was able to advance its pointer
		self.backpointer  = [None, None]

	def isComplete(self):
		return len(self.rule) == self.dotIndex + 1

	def __cmp__(self, other):
		return self.startCol == other.startCol and \
		self.dotIndex == other.dotIndex and \
		self.rule == other.rule

	def __eq__(self, other):
		return self.startCol == other.startCol and \
		self.dotIndex == other.dotIndex and \
		self.rule == other.rule

	def __hash__(self):
		return hash((self.startCol, self.dotIndex, self.rule))
		#part 3 consideration: compare backpointers???

	def __str__(self):
		return str(self.startCol) + ' ' + str(self.rule) + ' ' + str(self.dotIndex)

	def nextCategory(self):
		if self.dotIndex == len(self.rule) - 1:
			raise RuntimeException("already completed")
		else:
			return self.rule[self.dotIndex + 1]

###############################################################################
###############################################################################
def earleyParse(sentence):
	global chart
	global initialRootRules
	queue = [] 
	queueIndex = 0

	# initially add all root entries
	for root in rulesTable['ROOT']:
		rule = ('ROOT',) + tuple(root)
		assert rule in probTable
		rootKey = (0, rule, 0)
		rootEntry = Entry(start=0, rule=rule, \
			dotIndex=0, weight= -1*math.log(probTable[rule], 2))
		chart[0].predictedLefts.add(rule[0]) # optimization A
		# Optimization D:
		addOrAppend(leftParentTable, rule[1], 'ROOT')
		# Optimization C:
		insertOrUpdate(chart[0].customer, rootEntry.nextCategory(), rootEntry)

		chart[0].col[rootKey] = rootEntry
	# process sentence

	for i in range(len(sentence) + 1):
		assert len(queue) == queueIndex
		queueIndex = 0
		queue = []
		column = chart[i]
		for entryKey in column.col: # initialize the queue with what scan added
			queue.append(column.col[entryKey])

		# Optimization D:
		if i < len(sentence):
			createLeftAncestorPairTable(column, sentence[i], set())
		# end Optimization D
		for state in queue:
			queueIndex += 1
			if complete(state) == False:
				if state.nextCategory() in rulesTable:
					# optimization A
					if state.nextCategory() not in column.predictedLefts:
						predict(state, queue, column)         # non-terminal
				else:
					scan(state.nextCategory(), state, column, sentence) 
					# terminal
			else:
				attach(state, column, queue)
		# print len(column.col)

	# check if any root rule exists in the final column for recognizer
	found = False
	for root in rulesTable['ROOT']:
		rule = ('ROOT',) + tuple(root)
		assert rule in probTable
		rootKey = (0, rule, len(rule)-1)
		if rootKey in chart[-1].col:
			# print '\n***************\n* PARSE FOUND *\n***************\n'
			print (print_entry(chart[-1].col[rootKey]))
			print chart[-1].col[rootKey].weight
			found = True
	if not found:
		print 'NONE'
	# 	print'\n\n@@@@@@@@@@@@@@@@@@\n@ NO parse found @\n@@@@@@@@@@@@@@@@@@\n'
	return


def complete(entry):
	'''if state's dot is not at the end'''
	return entry.isComplete()

def predict(entry, queue, column):
	'''nextCat is nonterminal, add all rules that begin with
	nextCat on the left side of the rule to the queue'''
	column.predictedLefts.add(entry.nextCategory()) # optimization A

	# Optimization D
	left = entry.nextCategory()
	# for left in column.leftAncestorPairTable:
	if left not in column.leftAncestorPairTable:
		return 
	for firstChild in column.leftAncestorPairTable[left]:
		for fullRule in prefixTable[(left, firstChild)]:
		# end Optimization D
			key = (column.index, fullRule, 0)
			if key not in column.col: #check duplicates here
				# print '# predicting: ' + str(left) + ' with rule: ' + str(fullRule)
				# print 'predict; enqueueing ' + str(newEntry)
				rulesWeight = -1*math.log(probTable[fullRule], 2)
				newEntry = Entry(start=column.index, rule=fullRule, \
				dotIndex=0, weight=rulesWeight)
				queue.append(newEntry)
				column.col[key] = newEntry #check for duplicates!

				# Optimization C ##############################################
				insertOrUpdate(column.customer, newEntry.nextCategory(), newEntry)
				###############################################
	column.leftAncestorPairTable.pop(left, None) #Optimization D


def scan(hypothesisWord, entry, column, sentence):
	'''If the currColumn-th word in the sentence
		matches the hypothesisWord:
		add to the next column this entry with the
		dot advanced (with same startCol)
	'''
	global chart
	if column.index >= len(sentence):
		# print 'scan FAILURE'
		return
	if sentence[column.index] == hypothesisWord:
		weight = entry.weight
		key = (entry.startCol, entry.rule, (entry.dotIndex + 1))
		newEntry = Entry(start=entry.startCol, rule=entry.rule, \
			dotIndex=(entry.dotIndex + 1), weight=weight)
		# make fake entry for right parent for parse tree backpointers
		newEntry.backpointer[1] = Entry(0, [str(hypothesisWord)], 0, weight)
		newEntry.backpointer[0] = entry
		nextColumn = chart[column.index + 1].col

		# Optimization C:
		if not newEntry.isComplete():
			insertOrUpdate(chart[column.index + 1].customer, newEntry.nextCategory(), newEntry)
		# end optimization C
		if key not in nextColumn: #check duplicates
			nextColumn[key] = newEntry
		else:
			raise RuntimeError("duplicate scan found? not possible")
		return

def attach(completedEntry, currColumn, queue):
	'''for all entries in completedEntry.startCol:
		if entry has completedEntry.leftSide after the dot:
			advance its dot and add that entry to current column
	'''
	# Optimization C ##########################################################
	if completedEntry.rule[0] not in chart[completedEntry.startCol].customer:
		return
	customers = chart[completedEntry.startCol].customer[completedEntry.rule[0]]
	for entry in customers: #chart[completedEntry.startCol].col:
		# if an entry in some previous column has a category after its
		# dot that matches the left hand category of our completedEntry:
		
		key = (entry.startCol, entry.rule, (entry.dotIndex + 1))

		if key not in currColumn.col:
			# print 'attaching new entry ' + str(newEntry)
			newWeight = entry.weight + completedEntry.weight
			newEntry = Entry(start=entry.startCol, rule=entry.rule, \
				dotIndex=(entry.dotIndex + 1), weight=newWeight)
			newEntry.backpointer = [entry, completedEntry]

			currColumn.col[key] = newEntry
			queue.append(newEntry)
			######### optimization C:
			if (entry.dotIndex + 1) != len(entry.rule) - 1: 
				insertOrUpdate(currColumn.customer, newEntry.nextCategory(), newEntry)
			########
		else:
			# print '# got a duplicate attach'
			newWeight = entry.weight + completedEntry.weight
			if newWeight < currColumn.col[key].weight:
				currColumn.col[key].weight = newWeight
				currColumn.col[key].backpointer = [entry, completedEntry]

def print_entry(entry):
	'''print in pre-order traversal, that is root then left child then right 
		child. Only print anything when a node is complete. In a root, print
		only the first term (lhs) of a rule. One caveat is printing terminals,
		do so by wrapping the terminal itself in an entry (but dont include
		that entry in the column/parse chart). Make this fake entry in
		the scan step, attach it only to the right parent (not left). '''
	s = ""
	if entry is None:
		return s
	isLeaf = entry.backpointer[0] is None and entry.backpointer[1]is None
	if entry.isComplete() and not isLeaf:
		s += '(' + str(entry.rule[0]) + ' '
	elif entry.isComplete() and isLeaf:
		s += str(entry.rule[0]) #dummy leaf found by 
	s += print_entry(entry.backpointer[0])
	s += print_entry(entry.backpointer[1])
	if entry.isComplete() and not isLeaf:
		s += ')'
	return s


###############################################################################
#							helper functions
###############################################################################

# Method belongs to Optimization D
def createLeftAncestorPairTable(column, category, processed):
	'''
		create S_j, the left ancestor pair table for column j, stored in column
		.leftAncestorPairTable, by depth first search by processing some 
		category Y by adding Y to S_j(X) for each X in leftParentTable[Y], and 
		then recursively call this function with X as the category. 

		category initially starts as the word at jth index in the sentence.

	'''
	processed.add(category)
	S = column.leftAncestorPairTable
	if category not in leftParentTable:
		return
	for x in leftParentTable[category]:
		if (insertOrAdd(S, x, category)):
			assert x not in processed
			createLeftAncestorPairTable(column, x, processed)

	
def addOrAppend(table, key, value):
	# returns whether this was the first addition of value to table[key]
	# values are a list
	if key not in table:
		table[key] = [value]
		return True
	else:
		table[key].append(value)
		return False

def insertOrUpdate(table, key, value):
	# returns whether this was the first addition of value to table[key]
	# values are in a set on right hand side
	if key in table:
		if value in table[key]:
			table[key].remove(value)
		table[key].add(value)
		return False
	else:
		table[key] = set()
		table[key].add(value)
		return True

def insertOrAdd(table, key, value):
	# returns whether this was the first addition of value to table[key]
	# values are in a set on right hand side
	if key in table:
		table[key].add(value)
		return False
	else:
		table[key] = set()
		table[key].add(value)
		return True


def checkCustomers(customers, category):
	for customer in customers:
		if customer.nextCategory() != category:
			print category
			print customer
			return False
	return True

###############################################################################
#							main driver
###############################################################################
if len(sys.argv) != 3:
	raise RuntimeError("not enough input files")
grammarFile = sys.argv[1]
if grammarFile.split('.')[1] != 'gr':
	raise RuntimeError("not a valid grammar file")
sentenceFile = sys.argv[2]
if sentenceFile.split('.')[1] != 'sen':
	raise RuntimeError("not a valid sentence file")
parsedSentences = sentenceFile.split('.')[0] + '.par'

with open(grammarFile) as grammar:
	for line in grammar:
		rule = line.strip().split('\t')
		prob = float(rule[0])
		left = rule[1]
		right = tuple(rule[2].split(' '))
		r = (left,) + right
		rules.append(r)
		addOrAppend(masterRulesTable, left, right)
		assert r not in probTable
		probTable[r] = prob

		# Optimization D:
		if (left, right[0]) not in prefixTable:
			addOrAppend(leftParentTable, right[0], left)
		addOrAppend(prefixTable, (left, right[0]), tuple(r))
		# End Optimization D

with open(sentenceFile) as sentences:
	for sentence in sentences:
		rulesTable.clear()
		chart = []
		if sentence.strip() == '':
			continue
		sentence = sentence.strip()
		print '# ' + str(sentence.strip())
		NumWords = len(sentence.split(' '))

		# optimization B
		sentenceTerminals = set(sentence.split(' '))
		for key, value in masterRulesTable.iteritems():
			for rhs in value:
				inSentence = True
				for elem in rhs:
					if elem not in masterRulesTable:
						if elem not in sentenceTerminals:
							inSentence = False
							break
				if inSentence == True:
					addOrAppend(rulesTable, key, rhs)
		# end optimization B

		for i in range(NumWords + 1):
			chart.append(Column(i))
		# cProfile.run('earleyParse(' + str(sentence.split(' ')) + ')')
		earleyParse(sentence.split(' '))
		# print '###############################################################################'






