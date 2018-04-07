#-------------------------------------------------
#
# Project created by gzshun
#
#-------------------------------------------------

QT       += core gui

TARGET = 360Preview
TEMPLATE = app


SOURCES += \
    main.cpp \
    preview360.cpp \
    clabel.cpp

HEADERS  += \
    preview360.h \
    clabel.h \
    common.h

FORMS    +=

RESOURCES += \
    images/images.qrc

RC_FILE = images/360preview.rc

DESTDIR = bin
