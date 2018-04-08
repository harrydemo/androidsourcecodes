# set params
ANDROID_NDK_ROOT=/cygdrive/e/Android/android-ndk-r5
CRINSON_ROOT=/cygdrive/c/Users/Crinson/Android_And_C

# build
pushd $ANDROID_NDK_ROOT
./ndk-build -C $CRINSON_ROOT
popd

