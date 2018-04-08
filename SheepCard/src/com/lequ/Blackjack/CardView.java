package com.lequ.Blackjack;

import java.util.Arrays;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lequ.Base.Tools;

public class CardView extends View {

	private int mTopSpace;
	private int mLeftSpace;
	private int mPlayerXOffset;
	private int mPlayerYOffset;
	private int mBlackjackXOffset;
	private int mBlackjackYOffset;
	private static Bitmap[] mCards;  		//save all cards
	private int[] mPlayerCards;     //save player's cards
	private int[] mSheepCards;  //save Blackjack's cards
	private int mBottomCard;
	private static boolean  hasLoadPicture=false;

	private final Paint mPaint = new Paint();
	public final static int CARD_COUNT = 53;

	public CardView(Context context,AttributeSet attrs){
		super(context,attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,Tools.styleable.CardView);
		mTopSpace = a.getInt(Tools.styleable.CardView_topSpace, 3);
		mLeftSpace = a.getInt(Tools.styleable.CardView_leftSpace,15);
	}

	public CardView(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,Tools.styleable.CardView);
		mTopSpace = a.getInt(Tools.styleable.CardView_topSpace, 3);
		mLeftSpace = a.getInt(Tools.styleable.CardView_leftSpace,15);
	}

	/**
	 * reset the internal array of Bitmaps used for drawing cards
	 * reset the internal array of int used for player cards
	 * reset the internal array of int useed for Blackjack cards
	 */
	public void resetCards(){
		if(!hasLoadPicture ) {
			mCards = new Bitmap[CardView.CARD_COUNT];//
		}
		clearCards();
	}

	/**
	 * @param yOffset
	 */
	public void initOffset(int yOffset){
		mBlackjackXOffset = 5;
		mBlackjackYOffset = 10;
		mPlayerXOffset = 5;
		mPlayerYOffset = yOffset;
	}
	/**
	 * clear cards
	 */
	public void clearCards(){
		mPlayerCards = new int[9];
		Arrays.fill(mPlayerCards, -1);
		mSheepCards = new int[9];
		Arrays.fill(mSheepCards, -1);
		mSheepCards[0] = 0;              //0 as a hide card
	}

	/**set bottom card
	 * 
	 * @param bottomCard
	 */
	public void setBottomCard(int bottomCard){
		mBottomCard = bottomCard;
	}

	/**
	 * 开
	 */
	protected void open(){
		mSheepCards[0] = mBottomCard;
	}

	/**庄家底牌
	 * @return
	 */
	protected int getBottomCard(){
		return mBottomCard;
	}
	/**
	 * set player card
	 * @param card
	 */
	public void setPlayerCard(int card){
		mPlayerCards[fillPostion(0)] = card;
	}

	/**set blackjack card
	 * 
	 * @param card
	 */
	public void setBlackjackCard(int card){
		mSheepCards[fillPostion(1)] = card;
	}
	/**最多9张牌
	 * @return
	 */
	protected int[] getBlackjackCards(){
		return mSheepCards;
	}

	/**最多9张牌
	 * @return
	 */
	protected int[] getPlayerCards(){
		return mPlayerCards;
	}

	/**找到发牌时候当前牌应该在的位置
	 * @param type :0 playerCards 1 blackjackCards
	 */
	private int fillPostion(int type){
		if(type==0){
			for(int i=0;i<9;i++){
				if(mPlayerCards[i]==-1)
					return i;
			}
		}
		else{
			for(int i=0;i<9;i++){
				if(mSheepCards[i]==-1)
					return i;
			}
		}
		return 0;
	}

	 /**加载图片到mCards
	   * @param r
	   * @param id
	   */
	public void loadCards(Resources r,int[] id){
		if(hasLoadPicture) {
			return;
		}
		for(int i=0;i<id.length;i++){
			mCards[i] = BitmapFactory.decodeResource(r, id[i]);
		}
		hasLoadPicture = true;
	}

	/* draw puke 
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(int i=0;i<9;i++){
			if(mPlayerCards[i]!=-1){
				canvas.drawBitmap(mCards[mPlayerCards[i]], 
						mPlayerXOffset + i * mLeftSpace,
						mPlayerYOffset + i * mTopSpace,
						mPaint);
			}

			if(mSheepCards[i]!=-1){
				canvas.drawBitmap(mCards[mSheepCards[i]], 
						mBlackjackXOffset + i * mLeftSpace,
						mBlackjackYOffset + i * mTopSpace,
						mPaint);
			}
		}
	}
}
