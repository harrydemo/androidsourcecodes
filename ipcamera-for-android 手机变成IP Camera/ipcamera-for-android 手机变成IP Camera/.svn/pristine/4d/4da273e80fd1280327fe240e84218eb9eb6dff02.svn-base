#####################################################################
# the build script for NDK for droidipcam project
#

LOCAL_PATH:= $(call my-dir)

###########################################################
# the third prebuild library for linking
#
include $(CLEAR_VARS) 
LOCAL_MODULE := libffmpeg-prebuild	
LOCAL_SRC_FILES := libffmpeg.so
LOCAL_MODULE_TAGS := prebuild
include $(PREBUILT_SHARED_LIBRARY)

###########################################################
# build android libteaony 
# libteaonly: clone from libjingle, for generat NDK appplicaton
#      threads, socket, signal/slot and messages, etc.
#
include $(CLEAR_VARS)
LOCAL_MODULE := libteaonly
LOCAL_CPP_EXTENSION := .cc
LOCAL_CPPFLAGS := -O2 -Werror -Wall -DHAMMER_TIME=1 -DLOGGING=1 -DHASHNAMESPACE=__gnu_cxx -DHASH_NAMESPACE=__gnu_cxx -DPOSIX -DDISABLE_DYNAMIC_CAST -D_REENTRANT -DOS_LINUX=OS_LINUX -DLINUX -D_DEBUG  -DANDROID

#including source files
include $(LOCAL_PATH)/lib_build.mk

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)

###########################################################
# the native jni interface library
#
include $(CLEAR_VARS)
LOCAL_MODULE    := ipcamera
LOCAL_CPPFLAGS := -O2 -Werror -Wall -DHAMMER_TIME=1 -DLOGGING=1 -DHASHNAMESPACE=__gnu_cxx -DHASH_NAMESPACE=__gnu_cxx -DPOSIX -DDISABLE_DYNAMIC_CAST -D_REENTRANT -DOS_LINUX=OS_LINUX -DLINUX -D_DEBUG  -DANDROID
LOCAL_C_INCLUDES :=  ./ $(LOCAL_PATH)/ffmpeg
LOCAL_SHARED_LIBRARIES := libffmpeg-prebuild libteaonly
LOCAL_LDLIBS := -llog

include $(LOCAL_PATH)/build.mk

include $(BUILD_SHARED_LIBRARY)
