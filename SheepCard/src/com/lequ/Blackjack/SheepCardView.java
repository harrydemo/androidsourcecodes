package com.lequ.Blackjack;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;


public class SheepCardView extends CardView {
	
	private  Context mContext;
	private HandleCard mHandleCard;
	private int mWhoWin = CardConstant.WIN_INIT_STATE;
	private boolean isDeal = true;
	
	public SheepCardView(Context context,AttributeSet attrs){
		super(context,attrs);
		mContext = context;
		mHandleCard =  new HandleCard();
		initSheepjackView();
	}
	
	public SheepCardView(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		mContext = context;
		mHandleCard =  new HandleCard();
		initSheepjackView();
	}
		
	private void initSheepjackView(){
		resetCards();
		Resources r = mContext.getResources();
		int[] id = {R.drawable.back,R.drawable.h14,R.drawable.h2,R.drawable.h3,R.drawable.h4,R.drawable.h5,
					R.drawable.h6,R.drawable.h7,R.drawable.h8,R.drawable.h9,R.drawable.h10,R.drawable.h11,
					R.drawable.h12,R.drawable.h13,R.drawable.r14,R.drawable.r2,R.drawable.r3,R.drawable.r4,
					R.drawable.r5,R.drawable.r6,R.drawable.r7,R.drawable.r8,R.drawable.r9,R.drawable.r10,
					R.drawable.r11,R.drawable.r12,R.drawable.r13,R.drawable.m14,R.drawable.m2,R.drawable.m3,
					R.drawable.m4,R.drawable.m5,R.drawable.m6,R.drawable.m7,R.drawable.m8,R.drawable.m9,
					R.drawable.m10,R.drawable.m11,R.drawable.m12,R.drawable.m13,R.drawable.f14,R.drawable.f2,
					R.drawable.f3,R.drawable.f4,R.drawable.f5,R.drawable.f6,R.drawable.f7,R.drawable.f8,R.drawable.f9,
					R.drawable.f10,R.drawable.f11,R.drawable.f12,R.drawable.f13};
		loadCards(r, id);
		deal();
	}
	
	/**
	 * deal card
	 */
	public void deal(){
		mHandleCard.shuffle();
		mHandleCard.cut();
		
		setPlayerCard(mHandleCard.getCard());
		setBottomCard(mHandleCard.getCard());
		setPlayerCard(mHandleCard.getCard());
		setBlackjackCard(mHandleCard.getCard());
		
		setIsDeal();
	}
	
	/**
	 * deal to player
	 */
	private void dealToPlayer(){
		setPlayerCard(mHandleCard.getCard());
	}
	
	/**
	 * deal to banker
	 */
	public void dealToBanker(){
		if(isDeal){
			setBlackjackCard(mHandleCard.getCard());
			setIsDeal();
		}
	}
	
	/**
	 * 设置庄家是否继续叫牌
	 */
	private void setIsDeal(){
		int score = getBankerScore(true);
		if(score>=16){
			if(score>16)
				isDeal=false;
			else{
				int[] cards = getBlackjackCards();
				int j=0;
				for(int i=0;i<cards.length;i++){
					if(cards[i]!=-1)
						j++;
					else
						break;
						
				}
				if(j==2) isDeal=false;
			}
		}
	}
	
	public void doDeal(){
		dealToPlayer();
		dealToBanker();
		update();
	}
	
	/**
	 * 加倍，发最后一张牌
	 */
	public void doDoubleDeal() {
		dealToPlayer();
		update();
	}
	
	public void update(){
		SheepCardView.this.invalidate();
	}
	
	public void whoWin(int who){
		mWhoWin = who;
	}
	
	public void isBlackjack(){
		if(mHandleCard.isBlackjack(getBlackjackCards())==CardConstant.BLACKJACK){
			mWhoWin = CardConstant.WIN_BLACKJACK_BLACKJACK;
		}
		if(mHandleCard.isBlackjack(getPlayerCards())==CardConstant.BLACKJACK){
			mWhoWin = CardConstant.WIN_PLAYER_BLACKJACK;
		}
	}
	
	public void openBottomCard(){
		open();
		update();
	}
	
	public boolean getIsDeal(){
		return isDeal;
	}
	
	public int getWiner(){
		return mWhoWin;
	}
	
	/**庄家分数
	 * @return
	 */
	public int getBankerScore(boolean isCulateBottom){
		return mHandleCard.getScore(getBlackjackCards(),isCulateBottom);
	}
	
	public int getPlayerScore(){
		return mHandleCard.getScore(getPlayerCards(),true);
	}
	
	private class HandleCard{
			
		private int mPosition;   // current position
		private int[] cards={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,
							26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,
							48,49,50,51,52};
				
		/**
		 * 洗牌
		 */
		public void shuffle(){
			int temp = 0;
			for(int j=0;j<3;j++){
				for(int i=0;i<52;i++){
					int rand1 = (int)Math.floor(Math.random()*52);	
					int rand2 = (int)Math.floor(Math.random()*52);
					temp = cards[rand1];
					cards[rand1] = cards[rand2];
					cards[rand2] = temp;
				}
			}
		}	
		
		public void cut(){
			mPosition = (int)Math.floor(Math.random()*52);	
		}
		
		public int getCard(){
			int temp = mPosition;
			mPosition = (mPosition+1)%52;
			return cards[temp];
		}
		
		/**判断是否21点
		 * @param card
		 * @return
		 */
		public int isBlackjack(int[] card){
			int score = getScore(card,true);
			if(score<21){
				return CardConstant.NORMAL;
			}
			else{
				if(score==21)
					return CardConstant.BLACKJACK;
				else
					return CardConstant.BURST;
			}
		}
		
		/**计算牌面分数
		 * @param card
		 * @param isCulateBottom 是否计算底牌
		 * @return
		 */
		public int getScore(int[] card,boolean isCulateBottom){
			int score = 0;
			int ANum=0;
			for(int i=0;i<card.length && card[i]!=-1;i++){
				int temp = card[i]%13;
				if(i==0&& card[i]==0&&isCulateBottom) {
						temp = getBottomCard()%13;//庄家底牌
				}
				if(temp==1){
					ANum++;
				}else{
					if(i==0&&  card[i]==0 && !isCulateBottom) {
						continue;
					}
					if(temp==0||temp>10)
						score+=10;
					else
						score+=temp;
				}
			}
			if(ANum!=0){// deal A TODO when you have two A's,you may want one of them to be 11
				int temp = score+10+ANum;
				if(temp>21){
					score+=ANum;
				}
				else{
					score = temp;
				}
			}
			return score;
		}
	}
}
