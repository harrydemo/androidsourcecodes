
/**
 * org.hermit.utils: useful Android utility classes.
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License version 2
 *   as published by the Free Software Foundation (see COPYING).
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 */


package org.hermit.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * This class implements a popup info box (a subclass of AlertDialog)
 * which can be used to display help text, about info, license info, etc.
 */
public class InfoBox
	extends Dialog
{

	// ******************************************************************** //
	// Public Classes.
	// ******************************************************************** //

	/**
	 * Version info detail level.
	 */
	public enum Detail {
		NONE,				// Do not display.
		SIMPLE,				// Show basic name and version.
		DEBUG;				// Show debug-level detail.
	}
	
	
	// ******************************************************************** //
	// Constructor.
	// ******************************************************************** //

	/**
	 * Create an info box.
	 * 
	 * @param parent		Parent application context.
	 * @param button		Resource ID of the text for the OK button.
	 */
	public InfoBox(Activity parent, int button) {
		super(parent);
		parentApp = parent;
		resources = parent.getResources();
		buttonLabel = button;
		
		createDialog();
	}

	
    /**
     * Create the popup dialog UI.
     */
    private void createDialog() {
    	final int WCON = LinearLayout.LayoutParams.WRAP_CONTENT;
    	final int FPAR = LinearLayout.LayoutParams.FILL_PARENT;

        // Create a layout to hold the view elements.
    	LinearLayout layout = new LinearLayout(parentApp);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);
        
        // Construct the text view, and add it to the layout.
        // Weird bug -- turning off auto-link seems to kill the scrollbar.
        subtitleView = new TextView(parentApp);
        subtitleView.setTextSize(18f);
    	subtitleView.setVisibility(View.GONE);
        layout.addView(subtitleView, new LinearLayout.LayoutParams(FPAR, WCON));
        
        subtitleBar = new ImageView(parentApp);
        subtitleBar.setImageResource(android.R.drawable.divider_horizontal_dim_dark);
    	subtitleBar.setVisibility(View.GONE);
        layout.addView(subtitleBar, new LinearLayout.LayoutParams(FPAR, WCON));

        // Construct the text view, and add it to the layout.
        // Weird bug -- turning off auto-link seems to kill the scrollbar.
        textView = new TextView(parentApp);
 	    textView.setAutoLinkMask(Linkify.ALL);
        textView.setVerticalScrollBarEnabled(true);
        layout.addView(textView, new LinearLayout.LayoutParams(FPAR, WCON, 1));

        // Add a layout to hold the buttons.
    	buttonHolder = new LinearLayout(parentApp);
    	buttonHolder.setBackgroundColor(0xf08080);
    	buttonHolder.setOrientation(LinearLayout.HORIZONTAL);
    	buttonHolder.setPadding(6, 3, 3, 3);
        layout.addView(buttonHolder,
				 	   new LinearLayout.LayoutParams(FPAR, WCON));
        
        // Add the OK button.
		Button but = new Button(parentApp);
		but.setText(buttonLabel);
		but.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				okButtonPressed();
			}
		});
		buttonHolder.addView(but, new LinearLayout.LayoutParams(WCON, WCON, 1));

    	// Build a dialog around that view.
    	setContentView(layout);
    }


	// ******************************************************************** //
	// Configuration.
	// ******************************************************************** //

    /**
     * Set some link buttons on this dialog.  These are buttons that the
     * user can click to open a URL, e.g. the project page, license,
     * etc.
     * 
     * @param labels			List of button labels as resource IDs.
     * @param links				Resource IDs of the URLs for each button.
     */
    public void setLinkButtons(int[] labels, int[] links) {
    	final int WCON = LinearLayout.LayoutParams.WRAP_CONTENT;

    	// Kill all existing buttons.
    	int count = buttonHolder.getChildCount();
    	if (count > 1)
        	buttonHolder.removeViews(1, count - 1);
    	
    	// Add the requested buttons.
    	LinearLayout.LayoutParams lp;
    	for (int i = 0; i < labels.length && i < links.length; ++i) {
    		Button but = new Button(parentApp);
    		but.setText(labels[i]);
    		but.setId(i + 1);
    		but.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View b) {
    				linkButtonPressed(((Button) b).getId() - 1);
    			}
    		});
    		
    		lp = new LinearLayout.LayoutParams(WCON, WCON);
    		lp.gravity = Gravity.RIGHT;
    		buttonHolder.addView(but, lp);
    	}
    	
    	buttonLinks = links;
    }
     
    
    /**
     * Set the subtitle for the about box.
     * 
     * @param text			Subtitle to display; if null, don't show one.
     */
    public void setSubtitle(String text) {
    	if (text == null) {
        	subtitleView.setVisibility(View.GONE);
        	subtitleBar.setVisibility(View.GONE);
    	} else {
    		subtitleView.setText(text);
    		subtitleView.setVisibility(View.VISIBLE);
    		subtitleBar.setVisibility(View.VISIBLE);
    	}
    }


    // ******************************************************************** //
    // Dialog control.
    // ******************************************************************** //

    /**
     * Start the dialog and display it on screen.  The window is placed in
     * the application layer and opaque.
     * 
     * @param	title			Title for the dialog.
     * @param	text			Text to display in the dialog.
     */
    public void show(int title, int text) {
    	setTitle(title);
        textView.setText(text);
        show();
    }
    

    /**
     * Start the dialog and display it on screen.  The window is placed in
     * the application layer and opaque.
     * 
     * @param	text			Text to display in the dialog.
     */
    public void show(int text) {
        textView.setText(text);
        show();
    }
    

    // ******************************************************************** //
    // Input Handling.
    // ******************************************************************** //

    /**
     * Called when the OK button is clicked.
     */
    private void okButtonPressed() {
    	dismiss();
    }
    

    /**
     * Called when a link button is clicked.
     */
    private void linkButtonPressed(int which) {
    	if (which < 0 || which >= buttonLinks.length)
    		return;
    	
    	int URLId = buttonLinks[which];
    	String URLText = (String) resources.getText(URLId);
    	Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, 
                Uri.parse(URLText));
        parentApp.startActivity(myIntent); 
    }
    
   
	// ******************************************************************** //
	// Private Data.
	// ******************************************************************** //
 
	// Parent application context.
	private Activity parentApp;
	
	// App's resources.
	private Resources resources;

	// Text view we use to show the subtitle, and divider bar.
	private TextView subtitleView;
	private ImageView subtitleBar;
	
	// Text view we use to show the text.
	private TextView textView;
	
	// Layout to hold the link buttons.
	private LinearLayout buttonHolder;

	// OK button label.
	private int buttonLabel;

	// The URLs associated with the user-specified link buttons.
	private int[] buttonLinks;
	
}

