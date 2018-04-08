package org.mingjiang.ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class TrainTypes extends Activity {
	CheckBox train_DCType;
	CheckBox train_TType;
	CheckBox train_ZType;
	CheckBox train_KType;
	CheckBox train_PKType;
	CheckBox train_PKEType;
	CheckBox train_LKType;
	CheckBox train_AllType;
	
	Button submitButton;
	Button cancelButton;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.traintypes);

		this.train_AllType = (CheckBox) this.findViewById(R.id.train_AllType);
		this.train_DCType = (CheckBox) this.findViewById(R.id.train_DCType);
		this.train_ZType = (CheckBox) this.findViewById(R.id.train_ZType);
		this.train_TType = (CheckBox) this.findViewById(R.id.train_TType);
		this.train_KType = (CheckBox) this.findViewById(R.id.train_KType);
		this.train_PKType = (CheckBox) this.findViewById(R.id.train_PKType);
		this.train_PKEType = (CheckBox) this.findViewById(R.id.train_PKEType);
		this.train_LKType = (CheckBox) this.findViewById(R.id.train_LKType);
		
		this.submitButton = (Button) this.findViewById(R.id.submitButton);
		this.cancelButton = (Button) this.findViewById(R.id.cancelButton);

		this.train_AllType.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (train_AllType.isChecked()) {
					train_DCType.setChecked(true);
					train_ZType.setChecked(true);
					train_KType.setChecked(true);
					train_PKType.setChecked(true);
					train_PKEType.setChecked(true);
					train_LKType.setChecked(true);
					train_TType.setChecked(true);
				} else {
					train_DCType.setChecked(false);
					train_ZType.setChecked(false);
					train_KType.setChecked(false);
					train_PKType.setChecked(false);
					train_PKEType.setChecked(false);
					train_LKType.setChecked(false);
					train_TType.setChecked(false);
				}
			}
			
		});
				/*.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (train_AllType.isChecked()) {
							train_DCType.setChecked(true);
							train_ZType.setChecked(true);
							train_KType.setChecked(true);
							train_PKType.setChecked(true);
							train_PKEType.setChecked(true);
							train_LKType.setChecked(true);
							train_TType.setChecked(true);
						} else {
							train_DCType.setChecked(false);
							train_ZType.setChecked(false);
							train_KType.setChecked(false);
							train_PKType.setChecked(false);
							train_PKEType.setChecked(false);
							train_LKType.setChecked(false);
							train_TType.setChecked(false);
						}
					}

				});*/

		this.train_DCType
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if(train_DCType.isChecked()){
							checkAll();
						} else {
							if(train_AllType.isChecked())
								train_AllType.setChecked(false);
						}
					}
				});
		this.train_ZType
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if(train_ZType.isChecked()){
							checkAll();
						} else {
							if(train_AllType.isChecked())
								train_AllType.setChecked(false);
						}
					}
				});
		this.train_TType
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if(train_TType.isChecked()){
							checkAll();
						} else {
							if(train_AllType.isChecked())
								train_AllType.setChecked(false);
						}
					}
				});
		this.train_KType
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if(train_KType.isChecked()){
							checkAll();
						} else {
							if(train_AllType.isChecked())
								train_AllType.setChecked(false);
						}
					}
				});
		this.train_PKType
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if(train_PKType.isChecked()){
							checkAll();
						} else {
							if(train_AllType.isChecked())
								train_AllType.setChecked(false);
						}
					}
				});
		this.train_PKEType
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if(train_PKEType.isChecked()){
							checkAll();
						} else {
							if(train_AllType.isChecked())
								train_AllType.setChecked(false);
						}
					}
				});
		this.train_LKType
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if(train_LKType.isChecked()){
							checkAll();
						} else {
							if(train_AllType.isChecked())
								train_AllType.setChecked(false);
						}
					}
				});
		
		
		this.submitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String trainTypes = "";
				if(train_AllType.isChecked()){
					trainTypes = TrainHelper.Train_AllType;
				} else {
					if(train_DCType.isChecked()){
						trainTypes += TrainHelper.Train_DCType_Text + ";";
					}
					if(train_ZType.isChecked()){
						trainTypes += TrainHelper.Train_ZType_Text + ";";
					}
					if(train_TType.isChecked()){
						trainTypes += TrainHelper.Train_TType_Text + ";";
					}
					if(train_KType.isChecked()){
						trainTypes += TrainHelper.Train_KType_Text + ";";
					}
					if(train_PKType.isChecked()){
						trainTypes += TrainHelper.Train_PKType_Text + ";";
					}
					if(train_PKEType.isChecked()){
						trainTypes += TrainHelper.Train_PKEType_Text + ";";
					}
					if(train_LKType.isChecked()){
						trainTypes += TrainHelper.Train_LKType_Text + ";";
					}
					
				}
					
				Intent intent = new Intent();
		        Bundle bundle = new Bundle();
		        bundle.putString("trainTypes", trainTypes);
		        intent.putExtras(bundle);
		        setResult(RESULT_OK, intent);
				finish();
			}
			
		});
		
		this.cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
	
	void checkAll(){
		if(train_DCType.isChecked()
				&& train_ZType.isChecked()
				&& train_TType.isChecked()
				&& train_KType.isChecked()
				&& train_PKType.isChecked()
				&& train_PKEType.isChecked()
				&& train_LKType.isChecked()){
			train_AllType.setChecked(true);
		}
	}

}
