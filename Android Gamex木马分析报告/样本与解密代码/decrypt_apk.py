# -*- coding:utf-8 -*-
import sys

def main(filename):
    infile = file(filename,"rb")
    outfile = file(filename[:-4]+".apk","wb")
    while 1:
        c = infile.read(1)
        if not c:
            break
        c = chr(ord(c) ^ 18)
        outfile.write(c)
    outfile.close()
    infile.close()
if __name__ == '__main__':    
    main(sys.argv[1])

