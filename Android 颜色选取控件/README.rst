=====================
ColorPickerPreference
=====================

Generally used classes by Daniel Nilsson.
ColorPickerPreference class by Sergey Margaritov.
Packed by Sergey Margaritov.

Features
========

* Color Area
* Hue Slider
* Alpha Slider (disabled by default)
* Old & New Color
* Color Preview in Preferences List

Requirements
============

Tested with APIv7, but maybe will work with early versions

Usage
=====

You can see some tests inside

::

    <com.att.preference.colorpicker.ColorPickerPreference
        android:key="color1"
        android:title="@string/color1_title"
        android:summary="@string/color1_summary"
        android:defaultValue="@integer/COLOR_BLACK"     <!-- HEX value also accepted (v1.1) -->
        alphaSlider="true"                              <!-- enable alpha slider via XML -->
    />

To enable Alpha Slider in your code use function:
::
    setAlphaSliderEnabled(boolean enable)

Screens
=======

* .. image:: https://github.com/attenzione/android-ColorPickerPreference/raw/master/screen_1.png

* .. image:: https://github.com/attenzione/android-ColorPickerPreference/raw/master/screen_2.png