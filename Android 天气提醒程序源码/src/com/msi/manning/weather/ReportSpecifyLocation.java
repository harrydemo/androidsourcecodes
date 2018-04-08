package com.msi.manning.weather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Allow user to specify new specific location by postal code.
 * 
 * @author charliecollins
 * 
 */
public class ReportSpecifyLocation extends Activity {

    private static final String CLASSTAG = ReportSpecifyLocation.class.getSimpleName();
    private static final int MENU_GET_REPORT = Menu.FIRST;
    private static final int MENU_VIEW_SAVED_LOCATIONS = Menu.FIRST + 1;

    private EditText location;
    private Button button;

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.report_specify_location);
        this.location = (EditText) findViewById(R.id.location);
        this.button = (Button) findViewById(R.id.specify_location_button);

        this.location.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                if (location.getText().toString().length() > 5) {
                    Toast
                        .makeText(ReportSpecifyLocation.this, "Please enter no more than 5 digits", Toast.LENGTH_SHORT)
                        .show();
                    location.setText(location.getText().toString().substring(0, 5));
                }
            }
            @Override
            public void afterTextChanged(final Editable e) {
            }
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }
        });

        this.button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoadReport();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(Constants.LOGTAG, " " + ReportSpecifyLocation.CLASSTAG + " onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, ReportSpecifyLocation.MENU_GET_REPORT, 0, getResources().getText(R.string.menu_view_report))
            .setIcon(android.R.drawable.ic_menu_more);
        menu.add(0, ReportSpecifyLocation.MENU_VIEW_SAVED_LOCATIONS, 1,
            getResources().getText(R.string.menu_goto_saved)).setIcon(android.R.drawable.ic_menu_myplaces);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        switch (item.getItemId()) {
            case MENU_VIEW_SAVED_LOCATIONS:
                Intent intent = new Intent(ReportSpecifyLocation.this, ReportViewSavedLocations.class);
                startActivity(intent);
                break;
            case MENU_GET_REPORT:
                handleLoadReport();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void handleLoadReport() {
        Intent intent = null;
        Uri uri = null;
        if (validate()) {
            uri = Uri.parse("weather://com.msi.manning/loc?zip=" + this.location.getText().toString());
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private boolean validate() {
        boolean valid = true;
        StringBuffer validationText = new StringBuffer();
        if ((this.location.getText() == null) || this.location.getText().toString().equals("")) {
            validationText.append(getResources().getString(R.string.message_no_location));
            valid = false;
        } else if (!isNumeric(this.location.getText().toString()) || (this.location.getText().toString().length() != 5)) {
            validationText.append(getResources().getString(R.string.message_invalid_location));
            valid = false;
        }
        if (!valid) {
            new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.alert_label)).setMessage(
                validationText.toString()).setPositiveButton("Continue",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int arg1) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }).show();
            validationText = null;
        }
        return valid;
    }

    private boolean isNumeric(final String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
