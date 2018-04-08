/**
 * Copyright (C) 2011, Karsten Priegnitz
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * @author: Karsten Priegnitz
 * @see: http://code.google.com/p/android-change-log/
 */
package com.noshufou.android.su.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;

import com.noshufou.android.su.R;

public class ChangeLog {
    
    private final Context context;
    private int lastVersion, thisVersion;

    // this is the key for storing the version name in SharedPreferences
    private static final String VERSION_KEY = "pref_version_key";
    
    /**
     * Constructor
     *
     * Retrieves the version names and stores the new version name in
     * SharedPreferences
     */
    public ChangeLog(Context context) {
        this.context = context;

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);

        // get version numbers
        try {
            this.lastVersion = sp.getInt(VERSION_KEY, 0);
        } catch (ClassCastException e) {
            // Coming from the old version of tracking by version name
            this.lastVersion = 0;
        }
        Log.d(TAG, "lastVersion: " + lastVersion);
        try {
            this.thisVersion = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            this.thisVersion = 0;
            Log.e(TAG, "could not get version name from manifest!");
            e.printStackTrace();
        }
        Log.d(TAG, "appVersion: " + this.thisVersion);
        
        // save new version number to preferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(VERSION_KEY, this.thisVersion);
        editor.commit();
    }
    
    /**
     * @return  The version name of the last installation of this app (as
     *          described in the former manifest). This will be the same as
     *          returned by <code>getThisVersion()</code> from the second time
     *          this version of the app is launched (more precisely: the
     *          second time ChangeLog is instantiated).
     * @see AndroidManifest.xml#android:versionName
     */
    public int getLastVersion() {
        return  this.lastVersion;
    }

    /**
     * manually set the last version name - for testing purposes only
     * @param lastVersion
     */
    void setLastVersion(int lastVersion) {
        this.lastVersion = lastVersion;
    }
    
    /**
     * @return  The version name of this app as described in the manifest.
     * @see AndroidManifest.xml#android:versionName
     */
    public int getThisVersion() {
        return  this.thisVersion;
    }

    /**
     * @return  <code>true</code> if this version of your app is started the
     *          first time
     */
    public boolean firstRun() {
        return  this.lastVersion < this.thisVersion;
    }

    /**
     * @return  <code>true</code> if your app is started the first time ever.
     *          Also <code>true</code> if your app was deinstalled and 
     *          installed again.
     */
    public boolean firstRunEver() {
        return  "".equals(this.lastVersion);
    }

    /**
     * @return  an AlertDialog displaying the changes since the previous
     *          installed version of your app (what's new).
     */
    public AlertDialog getLogDialog() {
        return  this.getDialog(false);
    }

    /**
     * @return  an AlertDialog with a full change log displayed
     */
    public AlertDialog getFullLogDialog() {
        return  this.getDialog(true);
    }
    
    private AlertDialog getDialog(boolean full) {
        
        WebView wv = new WebView(this.context);
        wv.setBackgroundColor(0); // transparent
        wv.loadData(this.getLog(full), "text/html", "UTF-8");

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(context.getResources().getString(
                full 
                    ? R.string.changelog_full_title
                    : R.string.changelog_title))
                .setView(wv)
                .setCancelable(true) // Set to true, personal preference (chainsdd)
                .setPositiveButton(
                        context.getResources().getString(
                                R.string.ok),
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return  builder.create();   
    }
    
    /**
     * @return  HTML displaying the changes since the previous
     *          installed version of your app (what's new)
     */
    public String getLog() {
        return  this.getLog(false);
    }

    /**
     * @return  HTML which displays full change log
     */
    public String getFullLog() {
        return  this.getLog(true);
    }

    /** modes for HTML-Lists (bullet, numbered) */
    private enum Listmode {
        NONE,
        ORDERED,
        UNORDERED,
    };
    private Listmode listMode = Listmode.NONE;
    private StringBuffer sb = null;
    private static final int EOCL = -1;

    private String getLog(boolean full) {
        // read changelog.txt file
        sb = new StringBuffer();
        try {
            InputStream ins = context.getResources().openRawResource(
                    R.raw.changelog);
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            String line = null;
            boolean advanceToEOVS = false; // true = ignore further version sections
            while (( line = br.readLine()) != null){
                line = line.trim();
                if (line.startsWith("$")) {
                    // begin of a version section
                    this.closeList();
                    int version = Integer.parseInt(line.substring(1).trim());
                    // stop output?
                    if (! full) {
                        if (version < this.lastVersion + 1)
                            advanceToEOVS = true;
                        else if (version == EOCL)
                            advanceToEOVS = false;
                     }
                } else if (! advanceToEOVS) {
                    if (line.startsWith("%")) {
                        // line contains version title
                        this.closeList();
                        sb.append("<div class='title'>" 
                                + line.substring(1).trim() + "</div>\n");
                    } else if (line.startsWith("_")) {
                        // line contains version title
                        this.closeList();
                        sb.append("<div class='subtitle'>" 
                                + line.substring(1).trim() + "</div>\n");
                    } else if (line.startsWith("!")) {
                        // line contains free text
                        this.closeList();
                        sb.append("<div class='freetext'>" 
                                + line.substring(1).trim() + "</div>\n");
                    } else if (line.startsWith("#")) {
                        // line contains numbered list item
                        this.openList(Listmode.ORDERED);
                        sb.append("<li>" 
                                + line.substring(1).trim() + "</li>\n");
                    } else if (line.startsWith("*")) {
                        // line contains bullet list item
                        this.openList(Listmode.UNORDERED);
                        sb.append("<li>" 
                                + line.substring(1).trim() + "</li>\n");
                    } else {
                        // no special character: just use line as is
                        this.closeList();
                        sb.append(line + "\n");
                    }
                }
            }
            this.closeList();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  sb.toString();
    }
    
    private void openList(Listmode listMode) {
        if (this.listMode != listMode) {
            closeList();
            if (listMode == Listmode.ORDERED) {
                sb.append("<div class='list'><ol>\n");
            } else if (listMode == Listmode.UNORDERED) {
                sb.append("<div class='list'><ul>\n");
            }
            this.listMode = listMode;
        }
    }
    private void closeList() {
        if (this.listMode == Listmode.ORDERED) {
            sb.append("</ol></div>\n");
        } else if (this.listMode == Listmode.UNORDERED) {
            sb.append("</ul></div>\n");
        }
        this.listMode = Listmode.NONE;
    }

    private static final String TAG = "ChangeLog";
}