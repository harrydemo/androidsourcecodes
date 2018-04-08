/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.cam.cl.dtg.android.tor.Shadow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Provides a preference for setting the browser's home page
 * @author Connell Gauld
 *
 */
public class BrowserHomepagePreference extends EditTextPreference implements
        TextWatcher {

    public BrowserHomepagePreference(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        getEditText().addTextChangedListener(this);
    }

    public BrowserHomepagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        getEditText().addTextChangedListener(this);
    }

    public BrowserHomepagePreference(Context context) {
        super(context);
        getEditText().addTextChangedListener(this);
    }

    public void afterTextChanged(Editable s) {
    	// TODO clean up this method
    	
        AlertDialog dialog = (AlertDialog) getDialog();
        // This callback is called before the dialog has been fully constructed
        if (dialog != null) {
            //String url = s.toString();
            //dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
            //        url.length() == 0 || url.equals("about:blank") ||
            //        Regex.WEB_URL_PATTERN.matcher(url).matches());
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
