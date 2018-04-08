# -*- coding:utf-8 -*-
import sys

def decrypt2url(decryptedfile):
    f = file(decryptedfile,"r")
    buf = f.read()
    bs = map(ord, buf) #将字节流存储为10进制的list
    sizz = len(bs)
    for i in range(0, sizz, 2):  #后面的字与前面的字交换存储
        if i >= sizz / 2 : break
        d = bs[i]
        bs[i] = bs[sizz - 1 - i]
        bs[sizz - 1 - i] = d
    ss = ''.join(map(chr,bs)) #将字节流转成字符串
    bs2 = ss.split(',') #用逗号分隔开
    bss = list(bs2)
    sout = ''
    for i in range(0, len(bss), 2):
        sout = sout + chr(int(bss[i]))
    print sout
    
def main(filename):
    PASS = ''.join(chr(x) for x in [9, 5, 9, 8, 5]) #这个是解密的原子
    infile = file(filename,"rb")
    outfile = file(filename[:-4]+".txt","wb")
    i = 0
    while 1:
        c = infile.read(1)
        if not c:
            break
        j = i % 5
        d = PASS[j]
        c = chr(ord(c) ^ ord(d))
        i = i + 1
        outfile.write(c)
    outfile.close()
    infile.close()
    decrypt2url(filename[:-4]+".txt")
    
if __name__ == '__main__':    
    main(sys.argv[1])
    