import math

w = 1 #math.sqrt(3)/2.0
w2 = w/2.0
h = 2/math.sqrt(3) * 3.0/4.0

print("width", w)
print("height", h)

def calc(x1,y1,x2,y2):
    x1s = 0
    if y1 % 2 == 1:
        x1s = w2
    x2s = 0
    if y2 % 2 == 1:
        x2s = w2

    a = (w * x2 + x2s) - (w * x1 + x1s)
    b = (h * y2) - (h * y1)

    return math.sqrt(a*a + b*b)


import sys

print("circle 1:")
l = [(1,1,1,0),(1,1,2,0),(1,1,2,1),(1,1,2,2),(1,1,1,2),(1,1,0,1)]
for x1,y1,x2,y2 in l:
    c = calc(x1,y1,x2,y2)
    print("-> point %d/%d -> (%d/%d):  %.2f" % (x1,y1,x2,y2,c))

print("circle 2:")
l = [(1,1,0,0),(1,1,3,0),(1,1,3,2),(1,1,1,3),(1,1,0,2)]
for x1,y1,x2,y2 in l:
    c = calc(x1,y1,x2,y2)
    print("-> point %d/%d -> (%d/%d):  %.2f" % (x1,y1,x2,y2,c))

print("circle 2.5:")
l = [(1,1,0,3),(1,1,2,3),(1,1,3,1)]
for x1,y1,x2,y2 in l:
    c = calc(x1,y1,x2,y2)
    print("-> point %d/%d -> (%d/%d):  %.2f" % (x1,y1,x2,y2,c))

print("circle 3:")
l = [(1,1,3,3)]
for x1,y1,x2,y2 in l:
    c = calc(x1,y1,x2,y2)
    print("-> point %d/%d -> (%d/%d):  %.2f" % (x1,y1,x2,y2,c))
