/*
 * Copyright (C) 2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

#include "JavaInputStream.h"

#include <AndroidUtil.h>


JavaInputStream::JavaInputStream(const std::string &name) : myName(name), myNeedRepositionToStart(false) {
	myJavaFile = 0;

	myJavaInputStream = 0;
	myOffset = 0;

	myJavaBuffer = 0;
	myJavaBufferSize = 0;
}

JavaInputStream::~JavaInputStream() {
	JNIEnv *env = AndroidUtil::getEnv();
	if (myJavaInputStream != 0) {
		closeStream(env);
	}
	env->DeleteGlobalRef(myJavaFile);
	env->DeleteGlobalRef(myJavaBuffer);
}


void JavaInputStream::initStream(JNIEnv *env) {
	if (myJavaFile == 0) {
		jobject javaFile = AndroidUtil::createZLFile(env, myName);
		myJavaFile = env->NewGlobalRef(javaFile);
		env->DeleteLocalRef(javaFile);
		if (myJavaFile == 0) {
			return;
		}
	}

	jobject stream = env->CallObjectMethod(myJavaFile, AndroidUtil::MID_ZLFile_getInputStream);
	if (env->ExceptionCheck()) {
		env->ExceptionClear();
	} else {
		myJavaInputStream = env->NewGlobalRef(stream);
		myOffset = 0;
	}
	env->DeleteLocalRef(stream);
}

void JavaInputStream::closeStream(JNIEnv *env) {
	env->CallVoidMethod(myJavaInputStream, AndroidUtil::MID_java_io_InputStream_close);
	if (env->ExceptionCheck()) {
		env->ExceptionClear();
	}
	env->DeleteGlobalRef(myJavaInputStream);
	myJavaInputStream = 0;
	myOffset = 0;
}

void JavaInputStream::rewind(JNIEnv *env) {
	if (myOffset > 0) {
		closeStream(env);
		initStream(env);
	}
}


void JavaInputStream::ensureBufferCapacity(JNIEnv *env, size_t maxSize) {
	if (myJavaBuffer != 0 && myJavaBufferSize >= maxSize) {
		return;
	}
	env->DeleteGlobalRef(myJavaBuffer);
	jbyteArray array = env->NewByteArray(maxSize);
	myJavaBuffer = (jbyteArray)env->NewGlobalRef(array);
	env->DeleteLocalRef(array);
	myJavaBufferSize = maxSize;
}

size_t JavaInputStream::readToBuffer(JNIEnv *env, char *buffer, size_t maxSize) {
	ensureBufferCapacity(env, maxSize);

	jint result = env->CallIntMethod(myJavaInputStream,
			AndroidUtil::MID_java_io_InputStream_read, myJavaBuffer, (jint)0, (jint)maxSize);
	if (env->ExceptionCheck()) {
		env->ExceptionClear();
		return 0;
	}
	if (result > 0) {
		size_t bytesRead = (size_t)result;
		myOffset += bytesRead;

		jbyte *data = env->GetByteArrayElements(myJavaBuffer, 0);
		memcpy(buffer, data, bytesRead);
		env->ReleaseByteArrayElements(myJavaBuffer, data, JNI_ABORT);

		return bytesRead;
	}
	return 0;
}

size_t JavaInputStream::skip(JNIEnv *env, size_t offset) {
	size_t result = (size_t) env->CallLongMethod(myJavaInputStream,
			AndroidUtil::MID_java_io_InputStream_skip, (jlong)offset);
	if (env->ExceptionCheck()) {
		env->ExceptionClear();
		return 0;
	}
	myOffset += result;
	return result;
}


bool JavaInputStream::open() {
	if (myJavaInputStream == 0) {
		JNIEnv *env = AndroidUtil::getEnv();
		initStream(env);
	} else {
		myNeedRepositionToStart = true;
	}
	return myJavaInputStream != 0;
}

size_t JavaInputStream::read(char *buffer, size_t maxSize) {
	JNIEnv *env = AndroidUtil::getEnv();
	if (myNeedRepositionToStart) {
		rewind(env);
		myNeedRepositionToStart = false;
	}
	if (buffer != 0) {
		return readToBuffer(env, buffer, maxSize);
	} else {
		return skip(env, maxSize);
	}
}

void JavaInputStream::close() {
}

size_t JavaInputStream::sizeOfOpened() {
	if (myJavaInputStream == 0 || myJavaFile == 0) {
		return 0;
	}
	JNIEnv *env = AndroidUtil::getEnv();
	return (size_t) env->CallLongMethod(myJavaFile, AndroidUtil::MID_ZLFile_size);
}

void JavaInputStream::seek(int offset, bool absoluteOffset) {
	if (offset < 0) {
		return;
	}
	JNIEnv *env = AndroidUtil::getEnv();
	if (myNeedRepositionToStart || absoluteOffset) {
		rewind(env);
		myNeedRepositionToStart = false;
	}
	if (offset > 0) {
		skip(env, offset);
	}
}

size_t JavaInputStream::offset() const {
	return myNeedRepositionToStart ? 0 : myOffset;
}
