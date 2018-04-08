package org.abrantix.rockon.rockonnggl;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.ads.AdSenseSpec;
import com.google.ads.GoogleAdView;
import com.google.ads.AdSenseSpec.AdType;

public class AdsenseStuff {

    /**
     * Initialize the ads
     */
    static void initAdSense(Activity act)
    {
    	TextView tv = (TextView)act.findViewById(R.id.donate_button);
    	tv.setVisibility(View.GONE);
    	
    	GoogleAdView googleAdView = (GoogleAdView) act.findViewById(R.id.adview);
    	
    	AdSenseSpec adSenseSpec =
    	    new AdSenseSpec("ca-mb-app-pub-4668630037566688")
    	    .setCompanyName("Filipe Abrantes")
    	    .setAppName("3 (Cubed)")
    	    .setChannel("4530840367")
    	    .setAdType(AdType.TEXT_IMAGE)
//    	    .setAdType(AdType.IMAGE)
    	    .setAdTestEnabled(false);
    	
    	/*
    	 * Play with the keywords
    	 */
    	double r = Math.random();
    	if(r <= 0.2) {
    		adSenseSpec.setKeywords("ringtones");
    	} else if(r <= 0.4) {
    		adSenseSpec.setKeywords("dating");
    	} else if (r <= 0.6) {
    		adSenseSpec.setKeywords("work from home");
    	} else if (r <= 0.8) {
    		adSenseSpec.setKeywords("lose weight");
    	} else if (r <= 1) {
    		adSenseSpec.setKeywords("android");
    	}
    	
    	/*
    	 * Show the ads
    	 */
    	googleAdView.showAds(adSenseSpec);    
    	googleAdView.setVisibility(View.VISIBLE);
    }    
    
    /**
     * Hide the Adsense view 
     */
    static void hideAdsAndDonation(Activity act)
    {
    	GoogleAdView googleAdView = (GoogleAdView) act.findViewById(R.id.adview);
    	googleAdView.setVisibility(View.GONE);
    	TextView tv = (TextView)act.findViewById(R.id.donate_button);
    	tv.setVisibility(View.GONE);
    }
}
