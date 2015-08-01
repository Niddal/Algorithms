import numpy as np
import matplotlib.pyplot as plt
from scipy import interpolate
from numpy.random import randn

#initialize variables
start = 0 
stop = 6 #2*np.pi+np.pi/4
smoothness = 0.2
num_points = 50
dis_loc = 25 #between 0 and num_points
num_knots = 5

#initialize complicated, noisy function
x = np.linspace(start, stop, num=num_points)
y_original = np.sin(x) #+ discontinuity
y = y_original + randn(len(x))/10

#find B-spline representation of 1-D curve
std = 1/np.std(y); #weights should be inverse of std of y (w parameter)
#k=order of spline, s=how smooth (higher is smoother)
#try values of s between 0.1 and 1
knots = np.arange(x[1],x[-1],(x[-1]-x[1])/num_knots) #equally spaced knots
tck = interpolate.splrep(x, y, k=3, t=knots, s=0.2)
#or choose not to specify the knots and use smoothness+weights instead...
#tck = interpolate.splrep(x, y, t=knots, w=std*np.ones(len(x)), k=3, s=smoothness)
#tck[0] is location of knots, tck[1] B-spline coeff, tck[2] is degree

print tck[0]
print 'red lines are knot positions'

ynew = interpolate.splev(x, tck)

#plot
plt.figure()
plt.plot(x, y_original, 'b', x, y, 'x', x, ynew)
plt.hold()
for xc in tck[0]:
	#red vertical lines are locations of knots
    plt.axvline(x=xc, color='r')
plt.hold()
plt.legend(['Original', 'Original with Noise', 'Cubic Spline'], loc=2)
plt.axis([start-0.5, stop+0.5, min(y) - 0.5, max(y) + 1])
plt.title('Cubic-spline interpolation')
plt.show()
