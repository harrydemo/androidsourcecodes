package com.lequ.Blackjack;


import java.util.Date;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lequ.Base.ToDoDB;
import com.lequ.Base.Tools;

/**
 * @author ed
 *
 */
public class SheepCard extends Activity implements OnClickListener{
	
	/**
	 * score constants
	 */
	public final static int FIVE = 5;
	public final static int TEN = 10;
	public final static int TWENTY = 20;
	public final static int FIFTY = 50;
	public final static int ZERO = 0;
	public final static int INIT_MONEY = 5000;
		
	public static String ICICLE_KEY="Blackjack";
	private ImageButton mStartButton;
	private TextView mClipInText;
	private TextView mMessageText;
	private TextView mMoneyText;
	private TextView mMaxScoreText;
	private TextView mWinCountText;
	private TextView mLoseCountText;
	/**
	 * 当前总下注
	 */
	private long mCurrentScore;
	/**
	 * 账户金额
	 */
	private long mMoney;
	private long mMaxScore;
	private long mWinCount;
	private long mLoseCount;
	private long mStartTime;
	
	private ToDoDB mDB;
	private Cursor mCursor;
	static
	{
		AdManager.init("63c29e6e6ca9990f", "0c232d24937d3973", 31, false);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return result;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.restart:
				mDB.update(1,SheepCard.INIT_MONEY, SheepCard.INIT_MONEY, SheepCard.ZERO, SheepCard.ZERO);
				mDB.updateMenu(R.string.zc, 0);
				Intent intent = new Intent();
				intent.setClass(this, SheepCard.class);
				startActivity(intent);
				finish();
				break;
			case R.id.prop:
				item.getSubMenu().clear();
				Cursor cursor = mDB.selectMenu();
				int count = cursor.getCount();
				if(count>0){
					cursor.moveToFirst();
					for(int i=0;i<count;i++) {
						item.getSubMenu().add(getResources().getString(cursor.getInt(1))
								+"  "+cursor.getInt(2)
								+getResources().getString(R.string.count_unit));
					}
				}
				break;
			case R.id.help:
				new AlertDialog.Builder(SheepCard.this)
					.setTitle(R.string.help_title)
					.setMessage(R.string.help_message)
					.setPositiveButton(R.string.play_game, new DialogInterface.OnClickListener() {					
						public void onClick(DialogInterface dialog, int which) {	
						}
					}) .show();
				break;
			case R.id.exit:
				finish();
				break;
			default:
				mMoney+=CardConstant.ZC_MONEY;
				if(mMaxScore<mMoney+mCurrentScore)
					mMaxScore = mMoney+mCurrentScore;
				isReset();
				mDB.update(1, mMaxScore, mMoney, mWinCount, mLoseCount);
				Cursor cur = mDB.selectMenuByName(R.string.zc);
				cur.moveToFirst();
				int cou = cur.getInt(2)-1;
				mDB.updateMenu(R.string.zc, cou);
				doData();
				Tools.popToast(this,getResources().getString(R.string.use_zc), Toast.LENGTH_SHORT);
			}
		return false;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,  
        		WindowManager.LayoutParams. FLAG_FULLSCREEN); 
        setContentView(R.layout.main);
        mStartButton = (ImageButton)findViewById(R.id.start);
        mClipInText = (TextView)findViewById(R.id.clip_in);
        mMessageText =(TextView)findViewById(R.id.message);
        mMoneyText = (TextView)findViewById(R.id.money);
        mMaxScoreText = (TextView)findViewById(R.id.max_score);
        mWinCountText = (TextView)findViewById(R.id.win_count);
        mLoseCountText = (TextView)findViewById(R.id.lose_count);
        mCurrentScore = SheepCard.ZERO;
        
        mDB = new ToDoDB(this);
        if(savedInstanceState==null){
        	mStartTime = new Date().getTime();
        	mCursor = mDB.select();
        	mCurrentScore = 0;
        	if(mCursor.getCount()>0){
        		mCursor.moveToFirst();
        		mMaxScore = mCursor.getLong(1);
        		mMoney = mCursor.getLong(2);
        		mWinCount = mCursor.getLong(3);
        		mLoseCount = mCursor.getLong(4);
        	}
        	else{
        		mMaxScore = SheepCard.INIT_MONEY;
        		mMoney = SheepCard.INIT_MONEY;
        		mWinCount = SheepCard.ZERO;
        		mLoseCount = SheepCard.ZERO;
        		mDB.insert(SheepCard.INIT_MONEY, SheepCard.INIT_MONEY, SheepCard.ZERO, SheepCard.ZERO);
        		mDB.insertMenu(R.string.zc, 0);
        	}
        }
        else{
        	Bundle bundle = savedInstanceState.getBundle(SheepCard.ICICLE_KEY);
        	mMaxScore = bundle.getLong("mMaxScore");
        	mMoney = bundle.getLong("mMoney");
        	mWinCount = bundle.getLong("mWinCount");
        	mLoseCount = bundle.getLong("mLoseCount");
        	mCurrentScore = bundle.getLong("mCurrentScore");
        	mStartTime = bundle.getLong("mStartTime");
        }
       doData();
        //assign onClickerLister to ImageButton
        ((ImageButton)findViewById(R.id.five)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.ten)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.twenty)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.fifty)).setOnClickListener(this);
        ((ImageButton)findViewById(R.id.reset)).setOnClickListener(this);
        
        ((ImageButton)findViewById(R.id.start)).setOnClickListener(this);
    }

	public void onClick(View v) {
		int score = SheepCard.ZERO;
		switch(v.getId()){
			case R.id.five:
				score = SheepCard.FIVE;
				break;
			case R.id.ten:
				score = SheepCard.TEN;
				break;
			case R.id.twenty:
				score = SheepCard.TWENTY;
				break;
			case R.id.fifty:
				score = SheepCard.FIFTY;
				break;
			case R.id.reset:
				ClearWaper();
				break;
			case R.id.start:
				Intent intent = new Intent();
				intent.setClass(this, Card.class);
				intent.putExtras(saveToBundle());
				startActivityForResult(intent, 1);
				break;
		}
		if(judgeMoney(score)){
			mCurrentScore += score;
			mMoney -= score;
			onWaper(mCurrentScore);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		int score = SheepCard.ZERO;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			score = SheepCard.FIFTY;
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			score = SheepCard.TWENTY;
			break;
			
		case KeyEvent.KEYCODE_DPAD_LEFT:
			score = SheepCard.TEN;
			break;
			
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			score = SheepCard.FIVE;
			break;
		
		case KeyEvent.KEYCODE_CALL:
			ClearWaper();
			break;
			
		case KeyEvent.KEYCODE_ENTER:
			if(mCurrentScore>0){
				Intent intent = new Intent();
				intent.setClass(this, Card.class);
				intent.putExtras(saveToBundle());
				startActivityForResult(intent, 1);
			}
			break;
		}
		if(judgeMoney(score)){
			mCurrentScore += score;
			mMoney -= score;
			onWaper(mCurrentScore);
		}
		return super.onKeyDown(keyCode, event);
	}


	/**设置开始按钮是否可见,已下住等信息
	 * @param score 为当前下注金额
	 */
	private void onWaper(long score){
		String message = "";
		Resources r = getResources();
		if(score<=0){
			message = r.getString(R.string.start);
			mStartButton.setVisibility(View.INVISIBLE);
		}
		else{
			message = r.getString(R.string.wager)+" "+score+" "+r.getString(R.string.unit);
			mStartButton.setVisibility(View.VISIBLE);
		}
		mMessageText.setText(message);
		mClipInText.setText(new Long(score).toString());
		mMoneyText.setText(new Long(mMoney).toString());
	}
	
	/**
	 * 重设下注信息
	 */
	private void ClearWaper(){
		Resources r = getResources();
		mMessageText.setText(r.getString(R.string.start));
		mMoney += mCurrentScore;
		mCurrentScore = SheepCard.ZERO;
		
	}
	
	/**判断账户余额是否足够
	 * called when judge money
	 * @param money : a money of you choice
	 * @return boolean
	 */
	private boolean judgeMoney(int money){
		if(mMoney-money<=0){
			Tools.popToast(this, getToastMessage(money), Toast.LENGTH_SHORT);
			if(mMoney-money==0)
				return true;
			return false;
		}
		if(mCurrentScore+money>CardConstant.MAX_CLIP_IN){
			Tools.popToast(this, getResources().getString(R.string.max_clip_in), Toast.LENGTH_SHORT);
			return false;
		}
		return true;
	}
	
	/**
	 * called when get toast message
	 * @param money : a money of you choice
	 * @return CharSequence
	 */
	private CharSequence getToastMessage(int money){
		CharSequence str = "";
		Resources r = getResources();
		if(mMoney<=0){
			if(mCurrentScore==SheepCard.ZERO){// no money
				str = r.getString(R.string.no_money);
			}
			else{
				str = r.getString(R.string.lack_money);
			}
		}
		else{
			if(mMoney-money==0){// a little money
				str = r.getString(R.string.lack_money);
			}
			else{// lack money
				str = r.getString(R.string.little_money);
			}
		}
		return str;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
		outState.putBundle(ICICLE_KEY, saveToBundle());
	}
	
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		switch(resultCode){
			case RESULT_OK:
				Bundle bundle = data.getBundleExtra(SheepCard.ICICLE_KEY);
				mMaxScore = bundle.getLong("mMaxScore");
				mMoney = bundle.getLong("mMoney");
				mWinCount = bundle.getLong("mWinCount");
				mLoseCount = bundle.getLong("mLoseCount");
				mCurrentScore = bundle.getLong("mCurrentScore");
				mStartTime = bundle.getLong("mStartTime");
				mStartButton.setVisibility(View.INVISIBLE);
				mMessageText.setText(getResources().getString(R.string.start));
				doData();
				break;
			default:
				break;
		}
	}
	/**
	 * 分数设置
	 */
	private void doData(){
		mMaxScoreText.setText(Long.toString(mMaxScore));
        mMoneyText.setText(Long.toString(mMoney));
        mWinCountText.setText(Long.toString(mWinCount));
        mLoseCountText.setText(Long.toString(mLoseCount));
        mClipInText.setText(Long.toString(mCurrentScore));
	}
	
	/**
	 * 判断是否你牛逼，条件
	 * 1，总分超过最大
	 * 2，赢的次数或者输的次数超过最大
	 */
	private void isReset(){
		boolean flag = false;
		if(mMaxScore>=CardConstant.MAX_MONEY ){
			mMaxScore = mMoney = SheepCard.INIT_MONEY;
			mCurrentScore = 0;
			flag = true;
		}
		if(mWinCount>=CardConstant.MAX_COUNT || mLoseCount>=CardConstant.MAX_COUNT){
			mWinCount = 0;
			mLoseCount = 0;
			flag = true;
		}
		if(flag){
			Tools.popToast(this, getResources().getString(R.string.you_nb), Toast.LENGTH_LONG);
		}
	}
	
	private Bundle saveToBundle(){
		Bundle map = new Bundle();
		map.putLong("mMaxScore", mMaxScore);
		map.putLong("mMoney", mMoney);
		map.putLong("mWinCount", mWinCount);
		map.putLong("mCurrentScore", mCurrentScore);
		map.putLong("mLoseCount", mLoseCount);
		map.putLong("mStartTime",mStartTime);
		return map;
	}
	
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			AdView adView = (AdView) findViewById(R.id.adView);
			if (adView != null) {
				adView.onDestroy();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
