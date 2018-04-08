package com.veally.timesale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ActivityTimeSale extends Activity {

	private ListView listView;
	private AdapterSaleItem adapterSaleItem;
	 Random random = new Random();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_sale);
        
        listView = (ListView) findViewById(R.timeSale.listView);
        
       
        List<ProductItem> items = new ArrayList<ProductItem>();
        ProductItem item = null;
        for(int i = 0 ;i < 10; i++) {
        	 item = new ProductItem();
        	 item.setId(System.currentTimeMillis());
        	 item.setImageUrl("images/"+getImages()[random.nextInt(7)]);
        	 item.setRemainTime(getRandomTime());
        	items.add(item);
        }
        
        adapterSaleItem = new AdapterSaleItem(this, items);
        listView.setAdapter(adapterSaleItem);
        
    }
    private long getRandomTime() {
    	long curTime = System.currentTimeMillis();
    	long [] t = new long[] {500,200,640,120,300,450,100,1000,1540,2500};
    	return curTime + t[random.nextInt(9)]*1000;
    }
    private String [] getImages() {
    	String [] images = null;
    	try {
			images = this.getApplicationContext().getAssets().list("images");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return images;
    }


   

    
}
