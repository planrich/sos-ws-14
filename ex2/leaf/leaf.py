

import csv
import sys

if __name__ == "__main__":
    reader = csv.reader(sys.stdin)
    lines = list(reader)
    writer = csv.writer(sys.stdout)
    for line in lines:
        writer.writerow(line[2:])



