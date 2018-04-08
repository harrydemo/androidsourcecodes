package com.eassol.componenttest1;

import com.eassol.component.*;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

public class ComponentTest1 extends ActivityGroup {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main1);
        this.addContentView(new Component(this),new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        
    }
}