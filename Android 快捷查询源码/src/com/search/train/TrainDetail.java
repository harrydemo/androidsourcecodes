package com.search.train;

import com.search.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class TrainDetail extends Activity {
	
	private TextView cls, type,fromStation,toStation,leaveTime,reachTime,passduration,passdistant,yingzuo,yingwo,startStation,endStation;
	

	
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.train_info);	

        cls = (TextView)this.findViewById(R.id.train_cls);
        type = (TextView)this.findViewById(R.id.train_type);
        fromStation = (TextView)this.findViewById(R.id.train_fromstation);
        toStation = (TextView)this.findViewById(R.id.train_tostation);
        leaveTime = (TextView)this.findViewById(R.id.train_leavetime);
        reachTime = (TextView)this.findViewById(R.id.train_reachtime);
        passduration = (TextView)this.findViewById(R.id.train_passduration);
        passdistant = (TextView)this.findViewById(R.id.train_passdistant);
        yingzuo = (TextView)this.findViewById(R.id.train_yingzuo);
        yingwo = (TextView)this.findViewById(R.id.train_yingwo);
        startStation = (TextView)this.findViewById(R.id.train_startstation);
        endStation = (TextView)this.findViewById(R.id.train_endstation);

        this.showResult();
    }

	private void showResult() {
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Train train = (Train)bundle.getSerializable("trainDetail");


		//ActivityUtils.showDialog(this, res.getString(R.string.ok), res.getString(R.string.tip), train.getCls());
		cls.setText(train.getCls());
		type.setText(train.getType());
		fromStation.setText(train.getFromStation());
		toStation.setText(train.getToStation());
		leaveTime.setText(train.getLeaveTime());
		reachTime.setText(train.getReachTime());
		passduration.setText(train.getPassDuration());
		passdistant.setText(train.getPassDistant());
		yingzuo.setText(train.getPriceYingZuo()+"元");
		yingwo.setText(train.getPriceYingWo()+"元");
		startStation.setText(train.getStartStation());
		endStation.setText(train.getEndStation());


	}
}
