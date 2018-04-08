package com.ty.weather;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.AbsoluteLayout.LayoutParams;

import com.ty.weather.ForecastProvider.WeatherWidgets;
import com.ty.weather.util.ForecastEntity;
import com.ty.weather.util.ForecastUtil;
import com.ty.weather.util.WidgetEntity;

public class DetailForecastActivity extends ListActivity {
	private static final String TAG = "DetailForecastActivity";

	private Uri mData;
	private ListAdapter listAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		// Use provided data to figure out which widget was selected
		final Intent intent = getIntent();
		mData = intent.getData();
		if (mData == null) {
			finish();
			return;
		} else {
			Log.d(TAG, "Showing details for data=" + mData);
		}

		// Pull widget title and desired units
		WidgetEntity we = WebServiceHelper.getWidgetEntity(this, mData, false);

		// 处理天气预报信息
		if (we != null) {
			ImageView forecastImage = (ImageView) findViewById(R.id.dForecastImage);
			TextView cityText = (TextView) findViewById(R.id.dCityText);
			TextView conditionText = (TextView) findViewById(R.id.dConditionText);
			TextView humidityText = (TextView) findViewById(R.id.dHumidityText);
			TextView windText = (TextView) findViewById(R.id.dWindText);
			TextView tempcText = (TextView) findViewById(R.id.dTempCText);

			forecastImage.setImageResource(ForecastUtil.getForecastImage(we
					.getIcon(), ForecastUtil.isDaytime()));
			cityText.setText(we.getCity());
			conditionText.setText(we.getCondition());
			humidityText.setText(we.getHumidity());
			windText.setText(we.getWindCondition());
			tempcText.setText(we.getTempC() + "°");
			
			// 设置动画
			updateAnimtation(we.getIcon());
		}

		// Query for any matching forecast data and create adapter
		Uri forecastUri = Uri.withAppendedPath(mData,
				WeatherWidgets.FORECAST_END);
		Cursor forecastCursor = managedQuery(forecastUri,
				ForecastEntity.forecastProjection, null, null, null);

		listAdapter = new ForecastAdapter(this, forecastCursor);

		setListAdapter(listAdapter);
	}

	private void updateAnimtation(String url) {
		// TODO Auto-generated method stub
		int icon = ForecastUtil.getCurrentForecastIcon(url);
		
		ImageView currentIcon = (ImageView)findViewById(R.id.dForecastImage);
		Animation curIconAnim = AnimationUtils.loadAnimation(this, R.anim.rotatecurrentweather);
		currentIcon.setAnimation(curIconAnim);
		
		// 如果有云则执行如下动画
		if (icon == R.drawable.weather_cloudy){
			AbsoluteLayout absLayout = (AbsoluteLayout)findViewById(R.id.imagesLayout);
			ImageView cloud01 = new ImageView(this);
			ImageView cloud02 = new ImageView(this);
			cloud01.setAdjustViewBounds(true);
			cloud02.setAdjustViewBounds(true);
			cloud01.setImageResource(R.drawable.layer_cloud1);
			cloud02.setImageResource(R.drawable.layer_cloud2);
			cloud01.setMaxHeight(48);
			cloud01.setMinimumHeight(48);
			cloud01.setMaxWidth(100);
			cloud01.setMinimumWidth(100);
			
			cloud02.setMaxHeight(58);
			cloud02.setMinimumHeight(58);
			cloud02.setMaxWidth(83);
			cloud02.setMinimumWidth(83);
			
			Animation leftAnim = AnimationUtils.loadAnimation(this, R.anim.translatecloudleft);
			Animation rightAnim = AnimationUtils.loadAnimation(this, R.anim.translatecloudright);
			
			//leftAnim.setRepeatCount(Animation.INFINITE);
			//rightAnim.setRepeatCount(Animation.INFINITE);
			
			cloud01.setAnimation(leftAnim);
			cloud02.setAnimation(rightAnim);
			
			absLayout.addView(cloud01);
			absLayout.addView(cloud02);			
			
		}
		
		// 如果有雨则执行如下动画
		if (icon == R.drawable.weather_rain){
			AbsoluteLayout absLayout = (AbsoluteLayout)findViewById(R.id.imagesLayout);
			ImageView rain01 = new ImageView(this);
			ImageView rain02 = new ImageView(this);
			ImageView rain03 = new ImageView(this);
			ImageView rain04 = new ImageView(this);
			ImageView rain05 = new ImageView(this);
			ImageView drop01 = new ImageView(this);
			ImageView drop02 = new ImageView(this);
			ImageView drop03 = new ImageView(this);
			rain01.setAdjustViewBounds(true);
			rain02.setAdjustViewBounds(true);
			rain03.setAdjustViewBounds(true);
			rain04.setAdjustViewBounds(true);
			rain05.setAdjustViewBounds(true);
			drop01.setAdjustViewBounds(true);
			drop02.setAdjustViewBounds(true);
			drop03.setAdjustViewBounds(true);
			rain01.setImageResource(R.drawable.rain1);
			rain02.setImageResource(R.drawable.rain1);
			rain03.setImageResource(R.drawable.rain2);
			rain04.setImageResource(R.drawable.rain3);
			rain05.setImageResource(R.drawable.rain2);
			drop01.setImageResource(R.drawable.layer_drop1);
			drop02.setImageResource(R.drawable.layer_drop5);
			drop03.setImageResource(R.drawable.layer_drop7);
			
			LayoutParams lp01 = new LayoutParams(18, 30, 100, 150);
			LayoutParams lp02 = new LayoutParams(16, 33, 150, 140);
			LayoutParams lp03 = new LayoutParams(19, 30, 200, 150);
			
			Animation rain01Anim = AnimationUtils.loadAnimation(this, R.anim.translaterain01);
			Animation rain02Anim = AnimationUtils.loadAnimation(this, R.anim.translaterain02);
			Animation rain03Anim = AnimationUtils.loadAnimation(this, R.anim.translaterain03);
			Animation rain04Anim = AnimationUtils.loadAnimation(this, R.anim.translaterain04);
			Animation rain05Anim = AnimationUtils.loadAnimation(this, R.anim.translaterain05);
						
			rain01.setAnimation(rain01Anim);
			rain02.setAnimation(rain02Anim);
			rain03.setAnimation(rain03Anim);
			rain04.setAnimation(rain04Anim);
			rain05.setAnimation(rain05Anim);
			
			absLayout.addView(rain01);
			absLayout.addView(rain02);
			absLayout.addView(rain03);
			absLayout.addView(rain04);
			absLayout.addView(rain05);
			absLayout.addView(drop01, lp01);
			absLayout.addView(drop02, lp02);
			absLayout.addView(drop03, lp03);		
		}
	}

	/**
	 * 列表适配器
	 * @author tangyong
	 *
	 */
	private class ForecastAdapter extends ResourceCursorAdapter {

		public ForecastAdapter(Context context, Cursor c) {
			super(context, R.layout.detailitems, c);
			// TODO Auto-generated constructor stub
			Log.d(TAG, "constructor ForecastAdapter");
		}

		@Override
		public void bindView(View view, Context context, Cursor c) {
			// TODO Auto-generated method stub
			Log.d(TAG, "bind View ForecastAdapter");
			ImageView icon = (ImageView) view.findViewById(R.id.dDetailImage);
			TextView day = (TextView) view.findViewById(R.id.ddDayText);
			TextView condition = (TextView) view.findViewById(R.id.ddConditionText);
			TextView temp = (TextView) view.findViewById(R.id.ddTempCText);

			icon.setImageResource(ForecastUtil.getDetailForecastIcon(c.getString(4)));
			day.setText(c.getString(1));
			condition.setText(c.getString(5));
			temp.setText(c.getInt(2) + "°/" + c.getInt(3) + "°");
		}
	}
}