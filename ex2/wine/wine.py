

import csv
import sys
import numpy
import random

def column(rows, i):
    for row in rows:
        yield float(row[i])

def zmv(mean,deviation,c):
    #mean = numpy.mean(col)
    #deviation = numpy.std(col)
    return (c-mean)/deviation

def m_v_col(col):
    mean = numpy.mean(col)
    deviation = numpy.std(col)
    return mean,deviation

def minmax(_min, _max,c):
    #_min = min(col)
    #_max = max(col)
    return (c-_min)/(_max-_min)

def min_max_col(col):
    return min(col),max(col)

def bin_quality_type(quality, wtype):
    quality = int(quality)
    wtype = int(wtype)
    if quality <= 5:
        if wtype == 0:
            return 0
        else:
            return 1
    elif quality <= 6:
        if wtype == 0:
            return 2
        else:
            return 3
    elif quality <= 9:
        if wtype == 0:
            return 4
        else:
            return 5
    else:
        print("failed", quality, wtype)
        sys.exit(0)

if __name__ == "__main__":
    reader = csv.reader(sys.stdin)
    lines = list(reader)
    header = lines[0]
    rows = lines[1:]
    writer = csv.writer(sys.stdout)

    col_start = 0
    cols = col_end = len(header)-2
    funcs = [ (zmv,m_v_col) for _ in range(0,cols) ]

    subsample = -1
    if len(sys.argv) >= 2:
        subsample = int(sys.argv[1])

    if subsample > 0:
        samples = []
        random.seed(0)
        rows_copy = rows[:]
        for _ in range(0,subsample):
            i = random.randint(0,len(rows_copy)-1)
            r = rows_copy[i]
            del rows_copy[i]
            samples.append(r)

        rows = samples

    filterfuncs = [ lambda x: False for _ in range(0,cols) ]
    filterfuncs[2] = lambda x: float(x) >= 1.0
    filterfuncs[3] = lambda x: float(x) >= 30.0
    filterfuncs[4] = lambda x: float(x) >= 0.3 
    filterfuncs[5] = lambda x: float(x) >= 150.0
    filterfuncs[7] = lambda x: float(x) >= 1.01 
    filterfuncs[9] = lambda x: float(x) >= 1.3 

    frows = []
    for row in rows:
        dofilter = False
        for i in range(0,cols):
            ff = filterfuncs[i]
            v = float(row[i])
            if ff(v):
                dofilter = True
                break
        if not dofilter:
            frows.append(row)

    funcs[8] = (minmax,min_max_col)
    funcs[10] = (minmax,min_max_col)

    values = []
    for c in range(0,cols):
        _cols = list(column(frows,c))
        _, f = funcs[c]
        values.append(f(_cols))

    name = 'wq-n'
    csv_fd = open(name+'.csv','wb')
    clazz_fd = open(name+'.cls','wb')
    vec_fd = open(name+'.vec','wb')
    vt_fd = open(name+'.vt','wb')

    csv_fd.write(",".join(header[col_start:col_end])+"\n")

    clazz_fd.write("$TYPE class_information\n")
    clazz_fd.write("$NUM_CLASSES 2\n")
    clazz_fd.write("$CLASS_NAMES white_wine red_wine\n")
    clazz_fd.write("$XDIM 2\n")

    vt_fd.write("$TYPE template\n")
    vt_fd.write("$XDIM 2\n")
    vt_fd.write("$YDIM "+str(len(frows))+"\n")
    vt_fd.write("$VEC_DIM 12\n")
    for i,h in enumerate(header[col_start:col_end]):
        h = h.replace(" ","_")
        vt_fd.write(str(i) + " " + str(h) + "\n")
    vt_fd.close()

    vec_fd.write("$TYPE vec\n")
    vec_fd.write("$XDIM " + str(len(frows)) + "\n")
    vec_fd.write("$YDIM 1\n")
    vec_fd.write("$VEC_DIM "+str(col_end-col_start)+"\n")

    csv_orig_fd = open(name+'-o.csv','wb')
    csv_orig_fd.write(",".join(header[col_start:col_end])+"\n")
    for row in rows:
        csv_orig_fd.write(','.join([i for i in row[col_start:col_end]]) + "\n")
    csv_orig_fd.close()

    j = 0
    clazzes = []
    for row in frows:
        out = row[:]
        for i in range(0,cols):
            f,_ = funcs[i]
            v = float(row[i])
            vals = values[i]
            if f is None:
                out[i] = v
            else:
                out[i] = f(vals[0],vals[1],v)

        newrow = out[col_start:col_end]
        j+=1

        csv_fd.write(','.join([str(i) for i in newrow]) + "\n")

        cla = bin_quality_type(out[11],out[12])
        clazzes.append([str(j),str(cla)])

        newrow.append(j)
        vec_fd.write(' '.join([str(i) for i in newrow]) + "\n")

    clazz_fd.write("$YDIM "+str(len(clazzes))+"\n")
    for l in clazzes:
        clazz_fd.write('\t'.join(l) + "\n")

    vec_fd.close()
    csv_fd.close()
    clazz_fd.close()
