LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := C_to_Android

LOCAL_SRC_FILES :=	crinson_jni.cpp

LOCAL_C_INCLUDES := $(LOCAL_PATH)/.

#LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)


