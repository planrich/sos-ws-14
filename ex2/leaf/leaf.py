

import csv
import sys
import numpy

def column(rows, i):
    for row in rows:
        yield float(row[i])

def zmv(col,c):
    mean = numpy.mean(col)
    deviation = numpy.std(col)
    return (c-mean)/deviation

def minmax(col,c):
    _min = min(col)
    _max = max(col)
    return (c-_min)/(_max-_min)


if __name__ == "__main__":
    reader = csv.reader(sys.stdin)
    lines = list(reader)
    header = lines[0]
    rows = lines[1:]
    writer = csv.writer(sys.stdout)
    if sys.argv[1].startswith("norm"):
        col_start = 2
        cols = col_end = len(header)
        funcs = [ lambda x,y: y for _ in range(0,cols) ]
        funcs[3] = minmax # aspect ratio

        csv == len(sys.argv) > 3 and sys.argv[2] == "csv"

        for i in range(0,cols):
            col = list(column(rows,i))
            if len(filter(lambda x: x >= 0 and x <= 1, col)) == len(col):
                funcs[i] = lambda x,y: y
        funcs[2] = minmax

        # write header
        if csv:
            sys.stdout.write(",".join(header[col_start:col_end])+"\n")
        else:
            sys.stdout.write("$TYPE vec\n")
            sys.stdout.write("$XDIM " + str(len(lines)-1) + "\n")
            sys.stdout.write("$YDIM 1\n")
            sys.stdout.write("$VEC_DIM "+str(col_end-col_start)+"\n")
        for line in lines[1:]:
            out = line[:]
            for i in range(0,cols):
                col = list(column(lines[1:],i))[1:]
                f = funcs[i]
                v = float(line[i])
                out[i] = f(col,v)
            if csv:
                sys.stdout.write(','.join([str(i) for i in out[col_start:col_end]]) + "\n")
            else:
                sys.stdout.write(' '.join([str(i) for i in out[col_start:col_end]]) + "\n")



