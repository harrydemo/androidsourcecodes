
#include "Crinson_Blog_mainWnd.h"
//�������ͷ�ļ������Ժ����
#include "crinson_jni.h"

JNIEXPORT void JNICALL Java_Crinson_Blog_mainWnd_setName(JNIEnv *_pEnv, jobject _obj, jstring _strName)
{
	const char* pName = _pEnv->GetStringUTFChars(_strName, 0);

	//���ﷴ�������Android��setNameContent()����
	showNameInAndroidJNI((char*)pName);

	_pEnv->ReleaseStringUTFChars(_strName, pName);
}//end of Java_Crinson_Blog_mainWnd_setName