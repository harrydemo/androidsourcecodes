package com.galleryTab;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

 public class Gallery_tabhostActivity extends Activity {

 private Gallery gallery;
 private TabAdapter textAdapter;

 private static final String[] TAB_NAMES = {

 "第四个",
 "第一个",
 "第二个",
 "第三个",
 };

 private LinearLayout mTabLayout_One;
 private LinearLayout mTabLayout_Two;
 private LinearLayout mTabLayout_Three;
 private LinearLayout mTabLayout_Four;

 @Override
 public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.main);

 gallery = (Gallery) findViewById(R.id.gallery);
 textAdapter = new TabAdapter(this, Arrays.asList(TAB_NAMES));
 gallery.setAdapter(textAdapter);
 gallery.setSelection(34);//这里根据你的Tab数自己算一下，让左边的稍微多一点，不要一滑就滑到头

 mTabLayout_One = (LinearLayout) this.findViewById( R.id.TabLayout_One );
 mTabLayout_Two = (LinearLayout) this.findViewById( R.id.TabLayout_Two );
 mTabLayout_Three = (LinearLayout) this.findViewById( R.id.TabLayout_Three );
 mTabLayout_Four = (LinearLayout) this.findViewById( R.id.TabLayout_Four );

 mTabLayout_One.setVisibility( View.GONE );
 mTabLayout_Two.setVisibility( View.VISIBLE );
 mTabLayout_Three.setVisibility( View.GONE );
 mTabLayout_Four.setVisibility( View.GONE );

 gallery.setOnItemClickListener(new OnItemClickListener() {

 @Override
 public void onItemClick(AdapterView<?> parent, View view, int position,
 long id) {
 TabAdapter adapter = (TabAdapter)parent.getAdapter();
 adapter.setSelectedTab(position);
 switch(position %TAB_NAMES.length ){
 case 0:
 mTabLayout_One.setVisibility( View.VISIBLE );
 mTabLayout_Two.setVisibility( View.GONE );
 mTabLayout_Three.setVisibility( View.GONE );
 mTabLayout_Four.setVisibility( View.GONE );
 break;
 case 1:
 mTabLayout_One.setVisibility( View.GONE );
 mTabLayout_Two.setVisibility( View.VISIBLE );
 mTabLayout_Three.setVisibility( View.GONE );
 mTabLayout_Four.setVisibility( View.GONE );
 break;
 case 2:
 mTabLayout_One.setVisibility( View.GONE );
 mTabLayout_Two.setVisibility( View.GONE );
 mTabLayout_Three.setVisibility( View.VISIBLE );
 mTabLayout_Four.setVisibility( View.GONE );
 break;
 case 3:
 mTabLayout_One.setVisibility( View.GONE );
 mTabLayout_Two.setVisibility( View.GONE );
 mTabLayout_Three.setVisibility( View.GONE );
 mTabLayout_Four.setVisibility( View.VISIBLE );
 }

 }

 });

 }
 }