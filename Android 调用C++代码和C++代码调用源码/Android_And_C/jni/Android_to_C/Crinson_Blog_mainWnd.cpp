
#include "Crinson_Blog_mainWnd.h"
//下面这个头文件内容稍候解析
#include "crinson_jni.h"

JNIEXPORT void JNICALL Java_Crinson_Blog_mainWnd_setName(JNIEnv *_pEnv, jobject _obj, jstring _strName)
{
	const char* pName = _pEnv->GetStringUTFChars(_strName, 0);

	//这里反向调用了Android的setNameContent()函数
	showNameInAndroidJNI((char*)pName);

	_pEnv->ReleaseStringUTFChars(_strName, pName);
}//end of Java_Crinson_Blog_mainWnd_setName