#include "crinson_jni.h"
#include <jni.h>

extern "C"
{
	static	JavaVM*	g_pJavaVM		= 0;
	static	jclass	g_ClassTarget	= 0;
	JNIEnv*	g_pEnv					= 0;
	
	jint JNI_OnLoad(JavaVM *vm, void *reserved)
	{
		g_pJavaVM = vm;
		return JNI_VERSION_1_4;
	}

	static jmethodID getMethodID(const char *methodName, const char *paramCode)
	{
		//�ӿ�ID
		jmethodID funcID = 0;

		if (JNI_OK != g_pJavaVM->GetEnv((void**)&g_pEnv, JNI_VERSION_1_4))
		{
			return 0;
		}

		if (0 > g_pJavaVM->AttachCurrentThread(&g_pEnv, 0))
		{
			return 0;
		}

		//��Ҫ���õ��࣬����ͷ�ļ��ĺ���������
		g_ClassTarget = g_pEnv->FindClass(CLASS_WANT_TO_JNI);
		if (!g_ClassTarget)
		{
			return 0;
		}

		if (0 != g_pEnv && 0 != g_ClassTarget)
		{
			funcID = g_pEnv->GetStaticMethodID(g_ClassTarget, methodName, paramCode);
		}

		return funcID;
	}

	void showNameInAndroidJNI(char* _pstrName)
	{
		//����Android����������1Ϊ��Ҫ���õĺ���������ͷ�ļ������ˣ�
		//����2Ϊ���õĲ�����ʽ()V������ֵΪvoid��Ljava/lang/String����Ҫ����Ĳ���
		//������ϸ�Ľ���������JNI������
		jmethodID idshowName = getMethodID(FUNC_WANT_TO_JNI, "(Ljava/lang/String;)V");
		if (idshowName)
		{
			jstring StringArg = g_pEnv->NewStringUTF(_pstrName);
			g_pEnv->CallStaticVoidMethod(g_ClassTarget, idshowName, StringArg);
		}
	}
}