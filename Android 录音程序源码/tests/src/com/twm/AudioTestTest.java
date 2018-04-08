package com.twm;

import android.test.ActivityInstrumentationTestCase;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.twm.AudioTestTest \
 * com.twm.tests/android.test.InstrumentationTestRunner
 */
public class AudioTestTest extends ActivityInstrumentationTestCase<AudioTest> {

    public AudioTestTest() {
        super("com.twm", AudioTest.class);
    }

}
