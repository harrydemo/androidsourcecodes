LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

subdirs += $(LOCAL_PATH)/C_to_Android/Android.mk

subdirs += $(LOCAL_PATH)/Android_to_C/Android.mk

include $(subdirs)
