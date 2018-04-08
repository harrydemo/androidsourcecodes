NDK_ROOT=D:/android-ndk-r8
TOOLCHAINS_ROOT=$(NDK_ROOT)/toolchains/arm-linux-androideabi-4.4.3/prebuilt/windows
TOOLCHAINS_PREFIX=$(TOOLCHAINS_ROOT)/bin/arm-linux-androideabi
TOOLCHAINS_INCLUDE=$(TOOLCHAINS_ROOT)/lib/gcc/arm-linux-androideabi/4.4.3/include-fixed

PLATFORM_ROOT=$(NDK_ROOT)/platforms/android-14/arch-arm
PLATFORM_INCLUDE=$(PLATFORM_ROOT)/usr/include
PLATFORM_LIB=$(PLATFORM_ROOT)/usr/lib
PATH_LIBGCC=$(TOOLCHAINS_ROOT)/lib/gcc/arm-linux-androideabi/4.4.3

MODULE_NAME=getaddr
RM=del

FLAGS=-I$(TOOLCHAINS_INCLUDE) \
	-I$(PLATFORM_INCLUDE) \
	-L$(PLATFORM_LIB) \
	-nostdlib \
	-lgcc \
	-ldl \
	-Bdynamic \
	-lc \

OBJS=$(MODULE_NAME).o \
	$(PLATFORM_LIB)/crtbegin_dynamic.o \
	$(PLATFORM_LIB)/crtend_android.o \
	$(PATH_LIBGCC)/libgcc.a
all:
	$(TOOLCHAINS_PREFIX)-gcc $(FLAGS) -c $(MODULE_NAME).c -o $(MODULE_NAME).o
	$(TOOLCHAINS_PREFIX)-gcc $(FLAGS) $(OBJS) -o $(MODULE_NAME)
clean:
	$(RM) *.o
install:
	adb push $(MODULE_NAME) /data/local/
	adb shell chmod 755 /data/local/$(MODULE_NAME)
