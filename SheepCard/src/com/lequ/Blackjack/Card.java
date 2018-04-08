package com.lequ.Blackjack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lequ.Base.ToDoDB;
import com.lequ.Base.Tools;

/**
 * @author ed
 *
 */
public class Card extends Activity
{
//	private AdView mAdView;
	private Button mAgainButton;
	private SheepCardView mSheepCardView;
	private TextView mClipInText;
	private long mCurrentScore;
	private ToDoDB mDB;
	private long mLoseCount;
	private TextView mLoseCountText;
	private long mMaxScore;
	private TextView mMaxScoreText;
	private long mMoney;
	private TextView mMoneyText;
	private Button mNoButton;
	private Button doubleButton;
	private TextView mPlayerPointText;
	private long mStartTime;
	private TableLayout mTableLayout;
	
	private long mWinCount;
	private TextView mWinCountText;

	private int mWhoBurst = CardConstant.BURST_NO;
	
	/**根据winstate 计算分值
	 * @param state
	 */
	private void doData(int state){
		switch(state){
		case CardConstant.WIN_BLACKJACK_BLACKJACK:
		case CardConstant.WIN_BLACKJACK:
			mLoseCount++;
			mCurrentScore = 0;
			break;
		case CardConstant.WIN_PLAYER_BLACKJACK:
		case CardConstant.WIN_PLAYER:
			mWinCount++;
			mMoney+=mCurrentScore*2;
			if(mMaxScore<mMoney) mMaxScore=mMoney;
			mCurrentScore = 0;
			break;
		case CardConstant.WIN_NO:
			mMoney+=mCurrentScore;
			mCurrentScore = 0;
			break;
		}
		if(state!=CardConstant.WIN_NO){
			isReset();
			mDB.update(1, mMaxScore, mMoney, mWinCount, mLoseCount);
		}
	}

	private void doResult(){
		Resources r = getResources();
		doScore(r);
		int winner = mSheepCardView.getWiner();
		switch (winner) {
		case CardConstant.WIN_PLAYER_BLACKJACK:
			mSheepCardView.openBottomCard();
			mTableLayout.setVisibility(View.INVISIBLE);
			doData(CardConstant.WIN_PLAYER_BLACKJACK);
			updateData(CardConstant.WIN_PLAYER);
			popAlertDialog(R.string.alertdialog_title, r.getString(R.string.player)+r.getString(R.string.max_card),winner);
			break;
		case CardConstant.WIN_PLAYER:	
			mSheepCardView.openBottomCard();
			mTableLayout.setVisibility(View.INVISIBLE);
			doData(CardConstant.WIN_PLAYER);
			updateData(CardConstant.WIN_PLAYER);
			if(mWhoBurst==CardConstant.BURST_BANKER){
				popAlertDialog(R.string.alertdialog_title, r.getString(R.string.banker)+r.getString(R.string.burst),winner);
			}
			else{
				popAlertDialog(R.string.alertdialog_title, r.getString(R.string.banker)+mSheepCardView.getBankerScore(true)+r.getString(R.string.point)+","+r.getString(R.string.player)+mSheepCardView.getPlayerScore()+r.getString(R.string.point)+","+r.getString(R.string.player)+r.getString(R.string.who_win),winner);
			}
			break;
		case CardConstant.WIN_BLACKJACK_BLACKJACK:
			mSheepCardView.openBottomCard();
			mTableLayout.setVisibility(View.INVISIBLE);
			doData(CardConstant.WIN_BLACKJACK_BLACKJACK);
			updateData(CardConstant.WIN_BLACKJACK_BLACKJACK);
			popAlertDialog(R.string.alertdialog_title, r.getString(R.string.banker)+r.getString(R.string.max_card),winner);
			break;
		case CardConstant.WIN_BLACKJACK:
			mSheepCardView.openBottomCard();
			mTableLayout.setVisibility(View.INVISIBLE);
			doData(CardConstant.WIN_BLACKJACK);
			updateData(CardConstant.WIN_BLACKJACK);
			if(mWhoBurst==CardConstant.BURST_PLAYER){
				popAlertDialog(R.string.alertdialog_title, r.getString(R.string.player)+r.getString(R.string.burst),winner);
			}
			else{
				popAlertDialog(R.string.alertdialog_title, r.getString(R.string.banker)+mSheepCardView.getBankerScore(true)+r.getString(R.string.point)+","+r.getString(R.string.player)+mSheepCardView.getPlayerScore()+r.getString(R.string.point)+","+r.getString(R.string.banker)+r.getString(R.string.who_win),winner);
			}
			break;
		case CardConstant.WIN_NO:
			doData(CardConstant.WIN_NO);
			updateData(CardConstant.WIN_NO);
			mTableLayout.setVisibility(View.INVISIBLE);
			popAlertDialog(R.string.alertdialog_title, r.getString(R.string.equal),winner);
			break;
		}
	}

	private void doScore(Resources r){		
		int winState = mSheepCardView.getWiner();
		int bankerScore = mSheepCardView.getBankerScore(true);
		int playerScore = mSheepCardView.getPlayerScore();
		if(winState==CardConstant.WIN_INIT_STATE){	
			if(playerScore>=21){
				if(playerScore>21){
					mWhoBurst = CardConstant.BURST_PLAYER;
					mSheepCardView.whoWin(CardConstant.WIN_BLACKJACK);
				}
				else{
					mSheepCardView.whoWin(CardConstant.WIN_STOP_STATE);
					winState = CardConstant.WIN_STOP_STATE;
					while(mSheepCardView.getIsDeal()){
						mSheepCardView.dealToBanker();
					}
				}
				mTableLayout.setVisibility(View.INVISIBLE);
			}
			else{
				mTableLayout.setVisibility(View.VISIBLE);
				mPlayerPointText.setText(r.getString(R.string.player_point)+mSheepCardView.getPlayerScore()+",\n"+r.getString(R.string.banker_desk_point)+mSheepCardView.getBankerScore(false));
			}
		}
		winState = mSheepCardView.getWiner();
		bankerScore = mSheepCardView.getBankerScore(true);
		if(winState==CardConstant.WIN_STOP_STATE){
			mTableLayout.setVisibility(View.INVISIBLE);
			mSheepCardView.openBottomCard();
			if(bankerScore>21){
				mWhoBurst = CardConstant.BURST_BANKER;
				mSheepCardView.whoWin(CardConstant.WIN_PLAYER);
			}
			else{
				if(bankerScore>playerScore){
					mSheepCardView.whoWin(CardConstant.WIN_BLACKJACK);
				}
				else{
					if(bankerScore<playerScore){
						mSheepCardView.whoWin(CardConstant.WIN_PLAYER);
					}
					else{
						mSheepCardView.whoWin(CardConstant.WIN_NO);
					}
				}
			}
		}
	}


	/**从bundle中取出余额信息并设置TextView分数
	 * @param paramBundle
	 */
	private void init(Bundle paramBundle)
	{
		this.mLoseCount = paramBundle.getLong("mLoseCount");
		this.mWinCount = paramBundle.getLong("mWinCount");
		this.mMaxScore = paramBundle.getLong("mMaxScore");
		this.mMoney = paramBundle.getLong("mMoney");;
		this.mCurrentScore = paramBundle.getLong("mCurrentScore");;
		this.mStartTime =  paramBundle.getLong("mStartTime");;
		mLoseCountText.setText(Long.toString(this.mLoseCount));
		mWinCountText.setText(Long.toString(this.mWinCount));
		mMaxScoreText.setText(Long.toString(this.mMaxScore));
		mMoneyText.setText(Long.toString(this.mMoney));
		mClipInText.setText(Long.toString(this.mCurrentScore));
		this.mSheepCardView.isBlackjack();
		doResult();
	}

	/**
	 * 判断是否你牛逼，条件
	 * 1，总分超过最大
	 * 2，赢的次数或者输的次数超过最大
	 */
	private void isReset()
	{
		int i =0;
		if (this.mMaxScore >= CardConstant.MAX_MONEY)
		{
			this.mMoney = SheepCard.INIT_MONEY;
			this.mMaxScore = SheepCard.INIT_MONEY;
			this.mCurrentScore = 0;
			i = 1;
		}
		if ((mWinCount >= CardConstant.MAX_COUNT) || (mLoseCount >= CardConstant.MAX_COUNT))
		{
			this.mWinCount = 0;
			this.mLoseCount = 0;
			i = 1;
		}
		if (i == 0)
			return;
		String str = getResources().getString(R.string.you_nb);
		Tools.popToast(this, str, Toast.LENGTH_LONG);
	}


	/**再来一次
	 * @param title
	 * @param message
	 * @param winner
	 */
	private void popAlertDialog(int title,String message,int winner){
		final Intent data= new Intent();		
		new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(
				R.string.again,
				new DialogInterface.OnClickListener() {				
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_OK, data);
						finish();
					}
				}
		).show();
//		int[] messages = new HandleMessage().getMessage();
//		if(messages!=null){
//			int id = messages[0];
//			if(id!=R.string.zc){
//				mMoney+=messages[1];
//				if(mMaxScore<mMoney)
//					mMaxScore = mMoney;
//				isReset();
//				mDB.update(1, mMaxScore, mMoney, mWinCount,mLoseCount);
//				Tools.popToast(this, getResources().getString(id)+Math.abs(messages[1])+"Y!", Toast.LENGTH_LONG);
//			}
//			else{
//				Tools.popToast(this, getResources().getString(R.string.gain_zc), Toast.LENGTH_LONG);
//				Cursor cursor = mDB.selectMenuByName(id);
//				if(cursor.getCount()>0){
//					cursor.moveToFirst();
//					int t = cursor.getInt(2)+1;
//					mDB.updateMenu(cursor.getInt(1),t);
//				}
//				else{
//					mDB.insertMenu(R.string.zc, 1);
//				}
//			}
//			updateData(winner);
//		}
		data.putExtra(SheepCard.ICICLE_KEY, saveToBundle());
	}

	/**
	 * @return
	 */
	private Bundle saveToBundle()
	{
		Bundle localBundle = new Bundle();
		localBundle.putLong("mMaxScore", mMaxScore);
		localBundle.putLong("mMoney", mMoney);
		localBundle.putLong("mWinCount", mWinCount);
		localBundle.putLong("mCurrentScore", mCurrentScore);
		localBundle.putLong("mLoseCount", mLoseCount);
		localBundle.putLong("mStartTime", mStartTime);
		return localBundle;
	}
	

	/**更新text控件分数
	 * @param paramInt
	 */
	private void updateData(int state)
	{
		
		if (state == CardConstant.WIN_INIT_STATE)
			return;
		String unit = getResources().getString(R.string.unit);
		mLoseCountText.setText(mLoseCount+ unit);
		mWinCountText.setText(mWinCount+ unit);
		
		mMaxScoreText.setText(mMaxScore+ unit);
		mMoneyText.setText(mMoney+ unit);
		mClipInText.setText(mCurrentScore+ unit);
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,  
		WindowManager.LayoutParams. FLAG_FULLSCREEN); 
		setContentView(R.layout.blackjack);
		
		SheepCardView bv = (SheepCardView)findViewById(R.id.card);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		bv.initOffset(dm.heightPixels-125-40-20-9*3);
		
		mClipInText = (TextView)findViewById(R.id.clip_in);
        mMoneyText = (TextView)findViewById(R.id.money);
        mMaxScoreText = (TextView)findViewById(R.id.max_score);
        mWinCountText = (TextView)findViewById(R.id.win_count);
        mLoseCountText = (TextView)findViewById(R.id.lose_count);
        
		mSheepCardView = (SheepCardView)findViewById(R.id.card);
		mTableLayout = (TableLayout)findViewById(R.id.blackjack_tableLayout);
		mNoButton = (Button)findViewById(R.id.no_card);
		doubleButton = (Button)findViewById(R.id.double_score_button);
		mAgainButton = (Button)findViewById(R.id.again_card);
		mPlayerPointText = (TextView)findViewById(R.id.my_point);
		mDB = new ToDoDB(this);
		init(getIntent().getExtras());
		
		mNoButton.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View v) {
				mSheepCardView.whoWin(CardConstant.WIN_STOP_STATE);
				while(mSheepCardView.getIsDeal()){
					mSheepCardView.dealToBanker();
				}
				doResult();			
			}
		});
		
		mAgainButton.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View v) {
				doubleButton.setVisibility(View.INVISIBLE);
				mSheepCardView.doDeal();
				doResult();
			}
		});
		
		doubleButton.setOnClickListener(new View.OnClickListener() {		
			public void onClick(View v) {
				mMoney-=mCurrentScore;
				mCurrentScore = mCurrentScore*2;
				mSheepCardView.doDoubleDeal();//发给用户一张牌后停牌
				int playerScore = mSheepCardView.getPlayerScore();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(playerScore>21) {
					doResult();
				} else {
					mSheepCardView.whoWin(CardConstant.WIN_STOP_STATE);
					while(mSheepCardView.getIsDeal()){
						mSheepCardView.dealToBanker();
					}
					doResult();	
				}
							
			}
		});
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public boolean onCreateOptionsMenu(Menu paramMenu)
	{
		boolean bool = super.onCreateOptionsMenu(paramMenu);
		getMenuInflater().inflate(R.menu.menu, paramMenu);
		paramMenu.removeItem(R.id.restart);
		paramMenu.removeItem(R.id.exit);
		return bool;
	}


	/* (non-Javadoc) 菜单选择项，先不处理
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.prop:
				item.getSubMenu().clear();
				Cursor cursor = mDB.selectMenu();
				int count = cursor.getCount();
				if(count>0){
					cursor.moveToFirst();
					for(int i=0;i<count;i++)
						item.getSubMenu().add(getResources().getString(cursor.getInt(1))+"  "+cursor.getInt(2)+getResources().getString(R.string.count_unit));
				}
				break;
			case R.id.help:
				new AlertDialog.Builder(Card.this)
				.setTitle(R.string.help_title)
				.setMessage(R.string.help_message)
				.setPositiveButton(R.string.play_game, new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {	
					}
				})
				.show();
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
				mDB.updateMenu(cur.getInt(1),cou);
				mMaxScoreText.setText(Long.toString(mMaxScore));
		        mMoneyText.setText(Long.toString(mMoney));
		        mWinCountText.setText(Long.toString(mWinCount));
		        mLoseCountText.setText(Long.toString(mLoseCount));
		        mClipInText.setText(Long.toString(mCurrentScore));
		        Tools.popToast(this,getResources().getString(R.string.use_zc), Toast.LENGTH_SHORT);
			}
		return false;
	}


	/**道具
	 *
	 */
	class HandleMessage{
//		private int[][] goodMessages = {{R.string.good1,100},{R.string.good2,200},{R.string.good3,100},{R.string.good4,300},{R.string.good5,50}};
//		private int[][] badMessages = {{R.string.bad1,-50},{R.string.bad2,-20},{R.string.bad3,-100},{R.string.bad4,-300},{R.string.bad5,-200},{R.string.bad6,-1000},{R.string.bad7,-1500},{R.string.bad8,-800}};
//		private int[][] bankMessages = {{R.string.bank,0}};
		private int[] functions = {R.string.zc};
		public int[] getMessage(){
//			int random = (int)Math.floor(Math.random()*100);
//			if(random>=0 && random<10){
//				return goodMessages[random%(goodMessages.length)];
//			}
//			if(random>=20 && random<50){
//				return badMessages[(random-20)%(badMessages.length)];
//			}
//			if(random>=65 && random<76){
//				long now = new Date().getTime();
//				int distance = (int)(now - mStartTime)/(1000*60);
//				bankMessages[0][1]=distance<1?90:90*distance>900?900:90*distance;
//				return bankMessages[0];
//			}
//			if(random>85 && random<100){
//				return functions;
//			}
			return null;
		}
	}

}