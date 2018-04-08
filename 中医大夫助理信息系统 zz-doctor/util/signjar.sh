echo "jarsigner <jar-file> [key-alias]"

ALIAS="zz@efan"
if $# >= 2 then
	ALIAS=$2
fi

jarsigner $1 $ALIAS

jarsigner -verify -verbose -certs ZZ.apk
