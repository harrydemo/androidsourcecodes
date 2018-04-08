#!/bin/bash
if [ -z $1 ]
then
	printf "no trace name\n"
	exit 1
fi
trace=$1
DIR=tmp
if [ -d $DIR ]
then
	printf "temp dir already exists\n"
else
	mkdir "$DIR"
	printf "create dir %s" "$DIR"
fi
adb pull /sdcard/$trace.trace $DIR/
traceview $DIR/$trace.trace
