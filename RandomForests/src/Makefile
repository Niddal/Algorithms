# Using variables lets us switch tools easily
JAVAC = javac
JAVA = java
JAVADOC = javadoc
SHELL = bash

# Where we want the things we build to get put
BIN_DIR = ../bin
DOC_DIR = ../doc

# Make does not offer a recursive wildcard function, so here's one:
rwildcard=$(wildcard $1$2) $(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2))

# Try to compile all .java files anywhere under ./edu/jhu/
# Note that this is a quick and dirty approach, and won't scale
# well for larger projects.
SOURCES := $(call rwildcard,code/,*.java)
OBJECTS := $(patsubst %.java, $(BIN_DIR)/%.class, $(SOURCES))

# Package that the driver class resides in
PACKAGE = code

#########
# flags for tools
#########

JAVAC_FLAGS = -d $(BIN_DIR) -Xlint:all
JAVADOC_FLAGS = -d $(DOC_DIR) 
JAVA_FLAGS = -ea -cp $(BIN_DIR)


########
# targets and build rules
########

MAIN_CLASS_TRADITIONAL = DecisionTreeLearnerTraditionalDriver
MAIN_CLASS_EVOLUTIONARY = DecisionTreeLearnerEvolutionaryDriver
MAIN_CLASS_FOREST = RandomForestDriver

# compile traditional binaries
default: $(MAIN_CLASS_TRADITIONAL) 
$(MAIN_CLASS_TRADITIONAL): $(SOURCES) | $(BIN_DIR)
	@echo Building $@...
	$(JAVAC) $(JAVAC_FLAGS) $^ 

# compile evolutionary binaries
default: $(MAIN_CLASS_EVOLUTIONARY) 
$(MAIN_CLASS_EVOLUTIONARY): $(SOURCES) | $(BIN_DIR)
	@echo Building $@...
	$(JAVAC) $(JAVAC_FLAGS) $^ 

# compile forest binaries
default: $(MAIN_CLASS_FOREST) 
$(MAIN_CLASS_FOREST): $(SOURCES) | $(BIN_DIR)
	@echo Building $@...
	$(JAVAC) $(JAVAC_FLAGS) $^ 

# compile javadocs
docs: $(SOURCES) | $(DOC_DIR)
	@echo Building JavaDocs...
	$(JAVADOC) $(JAVADOC_FLAGS) $^

# pass driver class object to JRE
run_traditional:
	$(JAVA) $(JAVA_FLAGS) $(PACKAGE).$(MAIN_CLASS_TRADITIONAL) $(JAVA_ARGS)
run_evolutionary:
	$(JAVA) $(JAVA_FLAGS) $(PACKAGE).$(MAIN_CLASS_EVOLUTIONARY) $(JAVA_ARGS)
run_forest:
	$(JAVA) $(JAVA_FLAGS) $(PACKAGE).$(MAIN_CLASS_FOREST) $(JAVA_ARGS)

# make sure binary output directory exists
$(BIN_DIR):
	mkdir $(BIN_DIR);

# make sure document output directory exists
$(DOC_DIR):
	mkdir $(DOC_DIR);

# clean up object files
clean:
	@echo Deleting object files...
	@rm -f $(OBJECTS) 

