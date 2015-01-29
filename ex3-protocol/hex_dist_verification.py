import math

w = 1 #math.sqrt(3)/2.0
w2 = w/2.0
h = 2/math.sqrt(3) * 3.0/4.0

def calc(x1,y1,x2,y2):
    x1s = 0
    if y1 % 2 == 1:
        x1s = w2
    x2s = 0
    if y2 % 2 == 1:
        x2s = w2

    a = (w * x2 + x2s) - (w * x1 + x1s)
    b = (h * y2) - (h * y1)

    #return math.sqrt(a*a + b*b)
    return a*a + b*b

def calc_rect(x1,y1,x2,y2):
    x = x2-x1
    y = y2-y1
    #return math.sqrt(x*x + y*y)
    return x*x + y*y


import sys

print(calc(5,5,6,4))
print(calc_rect(5,5,7,4))

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

#print("circle 3:")
#l = [(1,1,3,3)]
#for x1,y1,x2,y2 in l:
#    c = calc(x1,y1,x2,y2)
#    print("-> point %d/%d -> (%d/%d):  %.2f" % (x1,y1,x2,y2,c))


for radius in [1,2,3,4]:
    x_size = 100
    y_size = 100
    x_c = 50
    y_c = 51
    print()
    print("the following positions (center (%d/%d)) are in radius"%(x_c,y_c), radius)
    ch = 0
    lh = []
    cr = 0
    lr = []
    for x in range(0,x_size):
        for y in range(0,y_size):
            if x != x_c or y != y_c:
                if round(calc(x_c,y_c,x,y),2) <= radius:
                    #print(" +++++> h", round(calc(x_c,y_c,x,y),2), (x,y))
                    lh.append((x,y))
                    ch += 1
                if round(calc_rect(x_c,y_c,x,y),2) <= radius:
                    #print(" +++++> r", calc_rect(x_c,y_c,x,y), (x,y))
                    lr.append((x,y))
                    cr += 1

    print(" => sum hex:", ch) #, str(lh))
    print(" => sum rec:", cr) #, str(lr))

