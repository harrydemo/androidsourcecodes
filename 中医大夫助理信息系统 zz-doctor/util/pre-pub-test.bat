set APP_VERSION=0.5.0

REM start emulator -avd avd1.5
start emulator -avd gAPI1.5

start /b adb -s emulator-5554 install c:\ff\workspace\ZZ-publish\%APP_VERSION%\ZZ.apk
