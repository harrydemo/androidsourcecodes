/**
 * Copyright (c) 2009 Muh Hon Cheng
 * 
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject 
 * to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT 
 * WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT 
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author 		Muh Hon Cheng <honcheng@gmail.com>
 * @copyright	2009	Muh Hon Cheng
 * @version
 * 
 */

package com.mobyfactory.uiwidgets;

import java.util.ArrayList;
import java.util.List;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class ScrollableTabActivity extends ActivityGroup  implements RadioGroup.OnCheckedChangeListener{
	
	private LocalActivityManager activityManager;
	private LinearLayout contentViewLayout;
	private LinearLayout.LayoutParams contentViewLayoutParams;
	private HorizontalScrollView bottomBar;
	private RadioGroup.LayoutParams buttonLayoutParams;
	private RadioGroup bottomRadioGroup;
	private Context context;
	private List intentList;
	private List titleList;
	private List states;
	private SliderBarActivityDelegate delegate;
	private int defaultOffShade;
	private int defaultOnShade;
	
	private IntentFilter changeTabIntentFilter;
	private ChangeTabBroadcastReceiver changeTabBroadcastReceiver;
	public static String CURRENT_TAB_INDEX;
	public static String ACTION_CHANGE_TAB = "com.mobyfactory.changeTab";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        
        activityManager = getLocalActivityManager();
        setContentView(R.layout.customslidingtabhost);
        contentViewLayout = (LinearLayout)findViewById(R.id.contentViewLayout);
        bottomBar = (HorizontalScrollView)findViewById(R.id.bottomBar);
        bottomRadioGroup = (RadioGroup)findViewById(R.id.bottomMenu);
        contentViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT); 
        
        defaultOffShade = RadioStateDrawable.SHADE_GRAY;
        defaultOnShade = RadioStateDrawable.SHADE_YELLOW;
        /*
         *  alternative method to using XML for layout, not used
         */
        /*
        if (inflateXMLForContentView())
        {
        	contentViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT); 
        }
        else
        {
        	RelativeLayout mainLayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams mainLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            mainLayout.setLayoutParams(mainLayoutParams);
            contentViewLayout = new LinearLayout(this);
            contentViewLayout.setOrientation(LinearLayout.VERTICAL);
            contentViewLayout.setBackgroundColor(Color.WHITE);
            contentViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            contentViewLayoutParams.bottomMargin = 55;
            mainLayout.addView(contentViewLayout, contentViewLayoutParams);
            bottomBar = new HorizontalScrollView(this);
            //bottomBar.setHorizontalFadingEdgeEnabled(false);
            RelativeLayout.LayoutParams bottomBarLayout = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 55);
            bottomBarLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mainLayout.addView(bottomBar, bottomBarLayout);
            bottomRadioGroup = new RadioGroup(this);
            bottomRadioGroup.setOrientation(RadioGroup.HORIZONTAL);
            LayoutParams bottomRadioGroupLayoutParam = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            bottomRadioGroup.setLayoutParams(bottomRadioGroupLayoutParam);
            bottomBar.addView(bottomRadioGroup);
            if (bottomBar()!=-1) bottomBar.setBackgroundResource(bottomBar());
            //bottomBar.setBackgroundResource(R.drawable.bottom_bar);
            setContentView(mainLayout);
        }*/
         
        bottomRadioGroup.setOnCheckedChangeListener(this);
        intentList  = new ArrayList();
        titleList	= new ArrayList();
        states 		= new ArrayList();
        
        buttonLayoutParams = new RadioGroup.LayoutParams(320/5, RadioGroup.LayoutParams.WRAP_CONTENT);
    }
    
    public void onResume()
    {
    	changeTabIntentFilter = new IntentFilter(ACTION_CHANGE_TAB);
    	changeTabBroadcastReceiver = new ChangeTabBroadcastReceiver();
    	registerReceiver(changeTabBroadcastReceiver, changeTabIntentFilter);
    	super.onResume();
    }
    
    public void onPause()
    {
    	unregisterReceiver(changeTabBroadcastReceiver);
    	super.onPause();
    }

    public void commit()
    {
    	bottomRadioGroup.removeAllViews();
    	
    	int optimum_visible_items_in_portrait_mode = 5;
    	try
    	{
    		WindowManager window = getWindowManager();
        	Display display = window.getDefaultDisplay();
        	int window_width = display.getWidth();
        	
        	optimum_visible_items_in_portrait_mode = (int) (window_width/64.0);
    	}
    	catch (Exception e)
    	{
    		optimum_visible_items_in_portrait_mode = 5;
    	}
    	
    	int screen_width = getWindowManager().getDefaultDisplay().getWidth();
    	int width;
    	if (intentList.size()<=optimum_visible_items_in_portrait_mode)
    	{
    		width = screen_width/intentList.size();
    	}
    	else
    	{
    		//width = screen_width/5;
    		width = 320/5; 
    	}
    	RadioStateDrawable.width = width;
		RadioStateDrawable.screen_width = screen_width;
		buttonLayoutParams = new RadioGroup.LayoutParams(width, RadioGroup.LayoutParams.WRAP_CONTENT);
    	
    	for (int i=0; i<intentList.size(); i++)
    	{
    		TabBarButton tabButton = new TabBarButton(this);
    		int[] iconStates = (int[]) states.get(i);
    		if (iconStates.length==1) tabButton.setState( titleList.get(i).toString(),iconStates[0]);
    		else if (iconStates.length==2) tabButton.setState(titleList.get(i).toString(), iconStates[0], iconStates[1]);
    		else if (iconStates.length==3) tabButton.setState(titleList.get(i).toString(), iconStates[0], iconStates[1], iconStates[2]);
        	tabButton.setId(i);
        	tabButton.setGravity(Gravity.CENTER);
        	bottomRadioGroup.addView(tabButton, i, buttonLayoutParams);
    	}
    	
    	setCurrentTab(0);
    }
    
    /**
     * <b><i>protected void addTab(String title, int offIconStateId, int onIconStateId, Intent intent)</i></b> <p><p>
     * Add a tab to the navigation bar by specifying the title, 1 image for button on-state, and 1 image for button off-state<p>
     * @param title				a String that specifies that title of the tab button
     * @param onIconStateId		id of the on-state image
     * @param offIconStateId	id of the off-state image
     * @param intent			intent to start when button is tapped
     */
    protected void addTab(String title, int offIconStateId, int onIconStateId, Intent intent)
    {
    	int[] iconStates = {onIconStateId, offIconStateId};
    	states.add(iconStates);
    	intentList.add(intent);
    	titleList.add(title);
    	//commit();
    }
    
    /**
     * <b><i>protected void addTab(String title, int iconStateId, Intent intent)</i></b> <p><p>
     * Add a tab to the navigation bar by specifying the title, and 1 image for the button. Default yellow/gray shade is used for button on/off state<p>
     * @param title				a String that specifies that title of the tab button
     * @param iconStateId		id of the image used for both on/off state
     * @param intent			intent to start when button is tapped
     */
    protected void addTab(String title, int iconStateId, Intent intent)
    {
    	//int[] iconStates = {iconStateId};
    	int[] iconStates = {iconStateId, defaultOffShade, defaultOnShade};
    	states.add(iconStates);
    	intentList.add(intent);
    	titleList.add(title);
    	//commit();
    }
    
    /**
     * <b><i>protected void addTab(String title, int iconStateId, int offShade, int onShade, Intent intent)</i></b> <p><p>
     * Add a tab to the navigation bar by specifying the title, and 1 image for the button. Default yellow/gray shade is used for button on/off state<p>
     * @param title				a String that specifies that title of the tab button
     * @param iconStateId		id of the image used for both on/off state
     * @param offShade  		id for off-state color shades (e.g. RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN etc)
     * @param onShade			id for on-state color shades (e.g. RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN etc)
     * @param intent			intent to start when button is tapped
     */
    protected void addTab(String title, int iconStateId, int offShade, int onShade, Intent intent)
    {
    	int[] iconStates = {iconStateId, offShade, onShade};
    	states.add(iconStates);
    	intentList.add(intent);
    	titleList.add(title);
    	//commit();
    }

    /**
     * <b><i>protected void setDefaultShde(int offShade, int onShade)</i></b><p><p>
     * Sets the default on and off color shades of the bottom bar buttons<p>
     * If not specified, the default off shade is gray, and the default on shade is yellow
     * @param offShade			id for off-state color shades (e.g. RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN etc)
     * @param onShade			id for on-state color shades (e.g. RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN etc)
     */
    protected void setDefaultShade(int offShade, int onShade)
    {
    	defaultOffShade = offShade;
    	defaultOnShade = onShade;
    }
    
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    	try
    	{
    		if (delegate!=null) delegate.onTabChanged(checkedId);
    	}
    	catch (Exception e){}
    	
    	startGroupActivity( titleList.get(checkedId).toString(), (Intent) intentList.get(checkedId));
    }
    
    public void startGroupActivity(String id, Intent intent)
    {
    	contentViewLayout.removeAllViews();
    	
    	View view = activityManager.startActivity(id, intent).getDecorView();
        contentViewLayout.addView(view, contentViewLayoutParams);
    }
    
    public void setCurrentTab(int index)
    {
    	bottomRadioGroup.check(index);
		startGroupActivity(titleList.get(index).toString(), (Intent)intentList.get(index));
    }
    
    public int getCurrentTab()
    {
    	return bottomRadioGroup.getCheckedRadioButtonId();
    }
    
    /*
     * gets required R, not used
     */
    public boolean inflateXMLForContentView()
    {
    	/*
    	setContentView(R.layout.customslidingtabhost);
        contentViewLayout = (LinearLayout)findViewById(R.id.contentViewLayout);
        bottomBar = (HorizontalScrollView)findViewById(R.id.bottomBar);
        bottomRadioGroup = (RadioGroup)findViewById(R.id.bottomMenu);
    	*/
    	return false;
    }
    
    public int bottomBar()
    {
    	return -1;
    }
    
    /*
     * delegates
     */
    
    public void setDelegate(SliderBarActivityDelegate delegate_)
    {
    	delegate = delegate_;
    }
    
    public static abstract class SliderBarActivityDelegate {

        /*
         * Called when tab changed
         */
        protected void onTabChanged(int tabIndex) {}
    }
    
    /*
     * Broadcast receiver to set current tab
     */
    
    public class ChangeTabBroadcastReceiver extends BroadcastReceiver
    {
    	@Override
    	public void onReceive(Context context, Intent intent)
    	{
    		int index = intent.getExtras().getInt(CURRENT_TAB_INDEX);
    		setCurrentTab(index);
    	}
    }
}
