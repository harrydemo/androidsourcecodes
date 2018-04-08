LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE                  := DeflatingDecompressor
LOCAL_SRC_FILES               := DeflatingDecompressor/DeflatingDecompressor.cpp
LOCAL_LDLIBS                  := -lz

include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE                  := LineBreak
LOCAL_SRC_FILES               := LineBreak/LineBreaker.cpp LineBreak/liblinebreak-2.0/linebreak.c LineBreak/liblinebreak-2.0/linebreakdata.c LineBreak/liblinebreak-2.0/linebreakdef.c

include $(BUILD_SHARED_LIBRARY)


include $(CLEAR_VARS)

EXPAT_DIR := expat-2.0.1

LOCAL_MODULE                  := expat
LOCAL_SRC_FILES               := $(EXPAT_DIR)/lib/xmlparse.c $(EXPAT_DIR)/lib/xmlrole.c $(EXPAT_DIR)/lib/xmltok.c
LOCAL_CFLAGS                  := -DHAVE_EXPAT_CONFIG_H
LOCAL_C_INCLUDES              := $(LOCAL_PATH)/$(EXPAT_DIR)
LOCAL_EXPORT_C_INCLUDES       := $(LOCAL_PATH)/$(EXPAT_DIR)/lib

include $(BUILD_STATIC_LIBRARY)


include $(CLEAR_VARS)

SHAREDIR_MACRO := nativeShare

LOCAL_MODULE := NativeFormats
LOCAL_LDLIBS := -lz
LOCAL_CFLAGS := -Wall -DBASEDIR=\"$(SHAREDIR_MACRO)\"
LOCAL_STATIC_LIBRARIES := expat

LOCAL_SRC_FILES := \
	NativeFormats/extensions.cpp \
	NativeFormats/fbreader/src/bookmodel/BookModel.cpp \
	NativeFormats/fbreader/src/bookmodel/BookReader.cpp \
	NativeFormats/fbreader/src/formats/css/StyleSheetParser.cpp \
	NativeFormats/fbreader/src/formats/css/StyleSheetTable.cpp \
	NativeFormats/fbreader/src/formats/EncodedTextReader.cpp \
	NativeFormats/fbreader/src/formats/fb2/FB2BookReader.cpp \
	NativeFormats/fbreader/src/formats/fb2/FB2CoverReader.cpp \
	NativeFormats/fbreader/src/formats/fb2/FB2MetaInfoReader.cpp \
	NativeFormats/fbreader/src/formats/fb2/FB2Plugin.cpp \
	NativeFormats/fbreader/src/formats/fb2/FB2Reader.cpp \
	NativeFormats/fbreader/src/formats/fb2/FB2TagManager.cpp \
	NativeFormats/fbreader/src/formats/FormatPlugin.cpp \
	NativeFormats/fbreader/src/formats/html/HtmlBookReader.cpp \
	NativeFormats/fbreader/src/formats/html/HtmlDescriptionReader.cpp \
	NativeFormats/fbreader/src/formats/html/HtmlEntityCollection.cpp \
	NativeFormats/fbreader/src/formats/html/HtmlPlugin.cpp \
	NativeFormats/fbreader/src/formats/html/HtmlReader.cpp \
	NativeFormats/fbreader/src/formats/html/HtmlReaderStream.cpp \
	NativeFormats/fbreader/src/formats/oeb/NCXReader.cpp \
	NativeFormats/fbreader/src/formats/oeb/OEBBookReader.cpp \
	NativeFormats/fbreader/src/formats/oeb/OEBCoverReader.cpp \
	NativeFormats/fbreader/src/formats/oeb/OEBMetaInfoReader.cpp \
	NativeFormats/fbreader/src/formats/oeb/OEBPlugin.cpp \
	NativeFormats/fbreader/src/formats/oeb/OEBTextStream.cpp \
	NativeFormats/fbreader/src/formats/PluginCollection.cpp \
	NativeFormats/fbreader/src/formats/txt/PlainTextFormat.cpp \
	NativeFormats/fbreader/src/formats/txt/TxtBookReader.cpp \
	NativeFormats/fbreader/src/formats/txt/TxtPlugin.cpp \
	NativeFormats/fbreader/src/formats/txt/TxtReader.cpp \
	NativeFormats/fbreader/src/formats/util/EntityFilesCollector.cpp \
	NativeFormats/fbreader/src/formats/util/MergedStream.cpp \
	NativeFormats/fbreader/src/formats/util/MiscUtil.cpp \
	NativeFormats/fbreader/src/formats/util/TextFormatDetector.cpp \
	NativeFormats/fbreader/src/formats/util/XMLTextStream.cpp \
	NativeFormats/fbreader/src/formats/xhtml/XHTMLReader.cpp \
	NativeFormats/fbreader/src/library/Author.cpp \
	NativeFormats/fbreader/src/library/Book.cpp \
	NativeFormats/fbreader/src/library/Comparators.cpp \
	NativeFormats/fbreader/src/library/Library.cpp \
	NativeFormats/fbreader/src/library/Tag.cpp \
	NativeFormats/JavaNativeFormatPlugin.cpp \
	NativeFormats/JavaPluginCollection.cpp \
	NativeFormats/main.cpp \
	NativeFormats/util/AndroidUtil.cpp \
	NativeFormats/util/CoversWriter.cpp \
	NativeFormats/zlibrary/core/src/constants/ZLMimeType.cpp \
	NativeFormats/zlibrary/core/src/constants/ZLXMLNamespace.cpp \
	NativeFormats/zlibrary/core/src/encoding/DummyEncodingConverter.cpp \
	NativeFormats/zlibrary/core/src/encoding/EncodingCollectionReader.cpp \
	NativeFormats/zlibrary/core/src/encoding/MyEncodingConverter.cpp \
	NativeFormats/zlibrary/core/src/encoding/ZLEncodingCollection.cpp \
	NativeFormats/zlibrary/core/src/encoding/ZLEncodingConverter.cpp \
	NativeFormats/zlibrary/core/src/encoding/ZLEncodingSet.cpp \
	NativeFormats/zlibrary/core/src/filesystem/zip/ZLGzipInputStream.cpp \
	NativeFormats/zlibrary/core/src/filesystem/zip/ZLZDecompressor.cpp \
	NativeFormats/zlibrary/core/src/filesystem/zip/ZLZipDir.cpp \
	NativeFormats/zlibrary/core/src/filesystem/zip/ZLZipEntryCache.cpp \
	NativeFormats/zlibrary/core/src/filesystem/zip/ZLZipHeader.cpp \
	NativeFormats/zlibrary/core/src/filesystem/zip/ZLZipInputStream.cpp \
	NativeFormats/zlibrary/core/src/filesystem/ZLDir.cpp \
	NativeFormats/zlibrary/core/src/filesystem/ZLFile.cpp \
	NativeFormats/zlibrary/core/src/filesystem/ZLFSManager.cpp \
	NativeFormats/zlibrary/core/src/filesystem/ZLInputStreamDecorator.cpp \
	NativeFormats/zlibrary/core/src/image/ZLBase64EncodedImage.cpp \
	NativeFormats/zlibrary/core/src/image/ZLFileImage.cpp \
	NativeFormats/zlibrary/core/src/image/ZLImage.cpp \
	NativeFormats/zlibrary/core/src/image/ZLImageManager.cpp \
	NativeFormats/zlibrary/core/src/image/ZLStreamImage.cpp \
	NativeFormats/zlibrary/core/src/language/ZLCharSequence.cpp \
	NativeFormats/zlibrary/core/src/language/ZLLanguageDetector.cpp \
	NativeFormats/zlibrary/core/src/language/ZLLanguageList.cpp \
	NativeFormats/zlibrary/core/src/language/ZLLanguageMatcher.cpp \
	NativeFormats/zlibrary/core/src/language/ZLStatistics.cpp \
	NativeFormats/zlibrary/core/src/language/ZLStatisticsGenerator.cpp \
	NativeFormats/zlibrary/core/src/language/ZLStatisticsItem.cpp \
	NativeFormats/zlibrary/core/src/language/ZLStatisticsXMLReader.cpp \
	NativeFormats/zlibrary/core/src/library/ZLibrary.cpp \
	NativeFormats/zlibrary/core/src/logger/ZLLogger.cpp \
	NativeFormats/zlibrary/core/src/runnable/ZLExecutionData.cpp \
	NativeFormats/zlibrary/core/src/runnable/ZLRunnable.cpp \
	NativeFormats/zlibrary/core/src/typeId/ZLTypeId.cpp \
	NativeFormats/zlibrary/core/src/unix/filesystem/ZLUnixFileInputStream.cpp \
	NativeFormats/zlibrary/core/src/unix/filesystem/ZLUnixFileOutputStream.cpp \
	NativeFormats/zlibrary/core/src/unix/filesystem/ZLUnixFSDir.cpp \
	NativeFormats/zlibrary/core/src/unix/filesystem/ZLUnixFSManager.cpp \
	NativeFormats/zlibrary/core/src/unix/library/ZLUnixLibrary.cpp \
	NativeFormats/zlibrary/core/src/util/ZLFileUtil.cpp \
	NativeFormats/zlibrary/core/src/util/ZLLanguageUtil.cpp \
	NativeFormats/zlibrary/core/src/util/ZLSearchUtil.cpp \
	NativeFormats/zlibrary/core/src/util/ZLStringUtil.cpp \
	NativeFormats/zlibrary/core/src/util/ZLUnicodeUtil.cpp \
	NativeFormats/zlibrary/core/src/util/ZLUserData.cpp \
	NativeFormats/zlibrary/core/src/xml/expat/ZLXMLReaderInternal.cpp \
	NativeFormats/zlibrary/core/src/xml/ZLAsynchronousInputStream.cpp \
	NativeFormats/zlibrary/core/src/xml/ZLPlainAsynchronousInputStream.cpp \
	NativeFormats/zlibrary/core/src/xml/ZLXMLReader.cpp \
	NativeFormats/zlibrary/text/src/model/ZLCachedMemoryAllocator.cpp \
	NativeFormats/zlibrary/text/src/model/ZLImageMapWriter.cpp \
	NativeFormats/zlibrary/text/src/model/ZLTextModel.cpp \
	NativeFormats/zlibrary/text/src/model/ZLTextParagraph.cpp \
	NativeFormats/zlibrary/ui/src/android/filesystem/JavaFSDir.cpp \
	NativeFormats/zlibrary/ui/src/android/filesystem/JavaInputStream.cpp \
	NativeFormats/zlibrary/ui/src/android/filesystem/ZLAndroidFSManager.cpp \
	NativeFormats/zlibrary/ui/src/android/library/ZLAndroidLibraryImplementation.cpp \


LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/NativeFormats/util \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/application \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/blockTreeView \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/constants \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/dialogs \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/encoding \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/filesystem \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/filesystem/zip \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/image \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/language \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/library \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/logger \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/message \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/network \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/optionEntries \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/options \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/resources \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/runnable \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/time \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/typeId \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/util \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/view \
	$(LOCAL_PATH)/NativeFormats/zlibrary/core/src/xml \
	\
	$(LOCAL_PATH)/NativeFormats/zlibrary/text/src/model \
	$(LOCAL_PATH)/NativeFormats/zlibrary/text/src/area \
	$(LOCAL_PATH)/NativeFormats/zlibrary/text/src/view \
	$(LOCAL_PATH)/NativeFormats/zlibrary/text/src/style \
	$(LOCAL_PATH)/NativeFormats/zlibrary/text/src/styleOptions \
	$(LOCAL_PATH)/NativeFormats/zlibrary/text/src/hyphenation \


include $(BUILD_SHARED_LIBRARY)
