#
# Copyright (C) 2008 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This makefile supplies the rules for building a library of JNI code for
# use by our example platform shared library.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

# This is the target being built.
LOCAL_MODULE:= libH263Encoder

# All of the source files that we will compile.
LOCAL_SRC_FILES:= \
	src/com_orangelabs_rcs_core_ims_protocol_rtp_codec_video_h263_encoder_NativeH263Encoder.cpp \
	src/bitstream_io.cpp \
	src/combined_encode.cpp \
	src/datapart_encode.cpp \
	src/dct.cpp \
	src/fastcodemb.cpp \
	src/fastidct.cpp \
	src/fastquant.cpp \
	src/findhalfpel.cpp \
	src/me_utils.cpp \
	src/motion_comp.cpp \
	src/motion_est.cpp \
	src/mp4enc_api.cpp \
	src/rate_control.cpp \
	src/sad.cpp \
	src/sad_halfpel.cpp \
	src/vlc_encode.cpp \
	src/vop.cpp

# All of the shared libraries we link against.
LOCAL_SHARED_LIBRARIES := 

# No static libraries.
LOCAL_STATIC_LIBRARIES :=

# Also need the JNI headers.
LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE) \
	$(LOCAL_PATH)/src \
 	$(LOCAL_PATH)/include \
	$(LOCAL_PATH)/oscl

# No specia compiler flags.
LOCAL_CFLAGS +=

# Don't prelink this library.  For more efficient code, you may want
# to add this library to the prelink map and set this to true.
LOCAL_PRELINK_MODULE := false

include $(BUILD_SHARED_LIBRARY)
