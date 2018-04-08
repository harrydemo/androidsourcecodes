LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := Android_to_C

#共15个文件
LOCAL_SRC_FILES :=	Crinson_Blog_mainWnd.cpp


LOCAL_C_INCLUDES := $(LOCAL_PATH)/../C_to_Android				\
					$(LOCAL_PATH)/../Android_to_C				\
                    $(LOCAL_PATH)/.									

					                   
LOCAL_LDLIBS := -L$(call host-path, $(LOCAL_PATH)/../../libs/armeabi)	\
               -lC_to_Android
            
include $(BUILD_SHARED_LIBRARY)



