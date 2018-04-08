LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := render
LOCAL_SRC_FILES := render.c render_alloc.c

LOCAL_LDLIBS := -lGLESv1_CM -llog


include $(BUILD_SHARED_LIBRARY)
