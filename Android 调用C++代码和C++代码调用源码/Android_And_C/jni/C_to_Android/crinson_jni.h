
#ifndef _CRINSON_JNI_2011_04_08
#define _CRINSON_JNI_2011_04_08

#define CLASS_WANT_TO_JNI	"Crinson/Blog/mainWnd"
#define FUNC_WANT_TO_JNI	"showNameInAndroid"

extern "C"
{
	extern void showNameInAndroidJNI(char* _pstrName);
}

#endif