/*
 * Copyright (C) 2004-2011 Geometer Plus <contact@geometerplus.com>
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

#include <AndroidUtil.h>

#include <ZLibrary.h>
#include <ZLFile.h>

#include "FormatPlugin.h"

#include "../library/Book.h"

#include "fb2/FB2Plugin.h"
////#include "docbook/DocBookPlugin.h"
#include "html/HtmlPlugin.h"
#include "txt/TxtPlugin.h"
//#include "pdb/PdbPlugin.h"
//#include "tcr/TcrPlugin.h"
#include "oeb/OEBPlugin.h"
//#include "chm/CHMPlugin.h"
//#include "rtf/RtfPlugin.h"
//#include "openreader/OpenReaderPlugin.h"
////#include "pdf/PdfPlugin.h"

PluginCollection *PluginCollection::ourInstance = 0;

PluginCollection &PluginCollection::Instance() {
	if (ourInstance == 0) {
		ourInstance = new PluginCollection();
		ourInstance->myPlugins.push_back(new FB2Plugin());
//		//ourInstance->myPlugins.push_back(new DocBookPlugin());
		ourInstance->myPlugins.push_back(new HtmlPlugin());
		ourInstance->myPlugins.push_back(new TxtPlugin());
//		ourInstance->myPlugins.push_back(new PluckerPlugin());
//		ourInstance->myPlugins.push_back(new PalmDocPlugin());
//		ourInstance->myPlugins.push_back(new MobipocketPlugin());
//		ourInstance->myPlugins.push_back(new EReaderPlugin());
//		ourInstance->myPlugins.push_back(new ZTXTPlugin());
//		ourInstance->myPlugins.push_back(new TcrPlugin());
//		ourInstance->myPlugins.push_back(new CHMPlugin());
		ourInstance->myPlugins.push_back(new OEBPlugin());
//		ourInstance->myPlugins.push_back(new RtfPlugin());
//		ourInstance->myPlugins.push_back(new OpenReaderPlugin());
//		//ourInstance->myPlugins.push_back(new PdfPlugin());
	}
	return *ourInstance;
}

void PluginCollection::deleteInstance() {
	if (ourInstance != 0) {
		delete ourInstance;
		ourInstance = 0;
	}
}

PluginCollection::PluginCollection() {
	JNIEnv *env = AndroidUtil::getEnv();
	jclass cls = env->FindClass(AndroidUtil::Class_PluginCollection);
	jobject instance = env->CallStaticObjectMethod(cls, AndroidUtil::SMID_PluginCollection_Instance);
	myJavaInstance = env->NewGlobalRef(instance);
	env->DeleteLocalRef(instance);
	env->DeleteLocalRef(cls);
}

PluginCollection::~PluginCollection() {
	JNIEnv *env = AndroidUtil::getEnv();
	env->DeleteGlobalRef(myJavaInstance);
}

shared_ptr<FormatPlugin> PluginCollection::plugin(const Book &book) {
	return plugin(book.file(), false);
}

shared_ptr<FormatPlugin> PluginCollection::plugin(const ZLFile &file, bool strong) {
	for (std::vector<shared_ptr<FormatPlugin> >::const_iterator it = myPlugins.begin(); it != myPlugins.end(); ++it) {
		if ((!strong || (*it)->providesMetaInfo()) && (*it)->acceptsFile(file)) {
			return *it;
		}
	}
	return 0;
}

bool PluginCollection::isLanguageAutoDetectEnabled() {
	JNIEnv *env = AndroidUtil::getEnv();
	jboolean res = env->CallBooleanMethod(myJavaInstance, AndroidUtil::MID_PluginCollection_isLanguageAutoDetectEnabled);
	return res != 0;
}

std::string PluginCollection::defaultLanguage() {
	JNIEnv *env = AndroidUtil::getEnv();
	jstring res = (jstring)env->CallObjectMethod(myJavaInstance, AndroidUtil::MID_PluginCollection_getDefaultLanguage);
	const char *data = env->GetStringUTFChars(res, 0);
	std::string str(data);
	env->ReleaseStringUTFChars(res, data);
	env->DeleteLocalRef(res);
	return str;
}

std::string PluginCollection::defaultEncoding() {
	JNIEnv *env = AndroidUtil::getEnv();
	jstring res = (jstring)env->CallObjectMethod(myJavaInstance, AndroidUtil::MID_PluginCollection_getDefaultEncoding);
	const char *data = env->GetStringUTFChars(res, 0);
	std::string str(data);
	env->ReleaseStringUTFChars(res, data);
	env->DeleteLocalRef(res);
	return str;
}
