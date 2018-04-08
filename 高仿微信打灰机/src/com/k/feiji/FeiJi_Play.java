package com.k.feiji;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteSheet;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FeiJi_Play extends CCColorLayer {

	private List<FeiJi_Sprite> _Foes = new CopyOnWriteArrayList<FeiJi_Sprite>();
	private List<FeiJi_Sprite> _BigFoes = new CopyOnWriteArrayList<FeiJi_Sprite>();
	private List<CCSprite> _Shots = new CopyOnWriteArrayList<CCSprite>();
	private List<CCSprite> _Red_Bombs = new CopyOnWriteArrayList<CCSprite>();
	private List<FeiJi_Sprite> _AllFoes = new CopyOnWriteArrayList<FeiJi_Sprite>();
	private CCLabel _ScoreLabel;
	private CCLabel _Red_Bomb_Num;
	private CCSprite _Red_Bomb;
	private CCSprite _FeiJi_Play;
	private CCSprite _FeiJi_Pause;
	private CGPoint _Touch_Location;
	private String _Font_Path = "Cookies.ttf";
	private String _FeiJi_Back_Path = "images/feiji_background.png";
	private String _MiddleFoe_Path_2 = "images/middlefoe_2.png";
	private String _SmallFoe_Path = "images/smallfoe.png";
	private String _MiddleFoe_Path = "images/middlefoe.png";
	private String _BigFoe_Path = "images/bigfoe.png";
	private String _BigFoe_Path2 = "images/bigfoe2.png";
	private String _BigFoe_Path_2 = "images/bigfoe_2.png";
	private String _Play_Path = "images/play.png";
	private String _Play_Path2 = "images/play2.png";
	private String _Shot_Path = "images/shot.png";
	private String _Pause_Path = "images/pause.png";
	private String _Red_Bomb_Down_Path = "images/red_bomb_down.png";
	private String _Red_Bomb_Path = "images/red_bomb.png";
	private String _Blue_Shot_Down_Path = "images/blue_shot_down.png";
	private String _Blue_Shot = "images/blue_shot.png";
	private int _Get_Score = 0;// 获得的分数
	private float _Shot_Du = 0.5f;// 子弹速度
	private boolean _Can_Move = false;// 是否在对象上移动
	private CGSize _WinSize;
	private int _Play_Image_Chage = 1;// 飞机图片判断
	private int _BigFoe_Image_Chage = 1;// 大型敌机图片判断
	private String _SmallFoe_Sequence_Path = "images/smallfoe_seq.png";
	private String _MiddleFoe_Sequence_Path = "images/middlefoe_seq.png";
	private String _BigFoe_Sequence_Path = "images/bigfoe_seq.png";
	private String _Play_Sequence_Path = "images/play_seq.png";
	private int _Big_Life = 16, _Middle_Life = 8, _Small_life = 2;// 敌机生命
	private int _ChangeImage_Delay = -1;// 图片改变延时
	private int _Pause_OR = -1;// 暂停点击判断
	private int Red_Bomb_Num = 0;// 红炸弹数量
	private boolean Blue_Shot_Change = false;// 是否蓝子弹
	private long Blue_Shot_Last_Time = 0;// 蓝子弹持续时间
	private int Blue_Red_Down_Time = 30;// 蓝子弹和红炸弹的随机数
	private int FoeDown_Time = 8;// 敌机的下落速度
	private SharedPreferences _Share;
	private String ScoreList = "0;0;0;0;0;0;0;0;0;0";
	private Dialog _Dialog;
	private boolean _Invincible = false;

	protected FeiJi_Play(ccColor4B color) {
		super(color);
		// TODO Auto-generated constructor stub
		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		_Share = CCDirector.sharedDirector().getActivity()
				.getSharedPreferences("Share", Context.MODE_PRIVATE);
		_WinSize = CCDirector.sharedDirector().displaySize();// 获取屏幕大小
		setIsTouchEnabled(true);

		CCSprite _FeiJi_Back = CCSprite.sprite(_FeiJi_Back_Path);
		// 背景按屏幕比例放大
		_FeiJi_Back.setScaleX(_WinSize.width
				/ _FeiJi_Back.getTexture().getWidth());
		_FeiJi_Back.setScaleY(_WinSize.height
				/ _FeiJi_Back.getTexture().getHeight());
		_FeiJi_Back.setPosition(CGPoint.make(_WinSize.width / 2,
				_WinSize.height / 2));// 默认中心点为左下角，将背景中心点移到屏幕中间
		addChild(_FeiJi_Back);// 将背景添加到场景

		_FeiJi_Pause = CCSprite.sprite(_Pause_Path);
		_FeiJi_Pause.setPosition(CGPoint.make(
				_FeiJi_Pause.getContentSize().width / 2 + 1, _WinSize.height
						- _FeiJi_Pause.getContentSize().height / 2 - 1));
		addChild(_FeiJi_Pause);// 添加暂停

		AddScore();

		this.schedule("GameFoes", 0.5f);// 一秒执行一次
		this.schedule("GameShot", 0.2f);
		GamePlay();
		this.schedule("AddPlay", 0.2f);
		this.schedule("Detection", 0f);
		this.schedule("AddRedBlueDown", 2.0f);
		this.schedule("AddBigFoe", 0.2f);
		
		AddRedBomb();
	}

	public void GameFoes(float t) {

		AddFoes();

	}

	public void GamePlay() {

		AddPlay();

	}

	public void GameShot(float t) {
		AddShot();
	}

	/**
	 * 添加红色炸弹蓝色子弹落下
	 */
	public void AddRedBlueDown(float t) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int randomValue = rand.nextInt(Blue_Red_Down_Time);
		CCSprite _Red_Blue_Down = null;

		if (randomValue != 0 && randomValue != 1) {
			return;
		}
		if (_Red_Bombs.size() >= 1) {
			return;
		}

		if (randomValue == 0 && _Red_Bombs.size() < 1) {
			_Red_Blue_Down = CCSprite.sprite(_Red_Bomb_Down_Path);
			_Red_Blue_Down.setTag(0);
		} else if (randomValue == 1 && _Red_Bombs.size() < 1) {
			_Red_Blue_Down = CCSprite.sprite(_Blue_Shot_Down_Path);
			_Red_Blue_Down.setTag(1);
		}
		int minX = (int) (_Red_Blue_Down.getContentSize().width / 2.0f);
		int maxX = (int) (_WinSize.width - _Red_Blue_Down.getContentSize().width / 2.0f);
		int rangeX = maxX - minX;
		int actualX = rand.nextInt(rangeX) + minX;
		_Red_Blue_Down
				.setPosition(actualX, _Red_Blue_Down.getContentSize().height
						/ 2.0f + _WinSize.height);
		addChild(_Red_Blue_Down);
		_Red_Bombs.add(_Red_Blue_Down);
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(1,
				CGPoint.ccp(actualX, _WinSize.height / 3 * 2));// 时间内移动

		CCCallFuncN fs_back = CCCallFuncN.action(this, "Red_Bomb_Back");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_back);
		_Red_Blue_Down.runAction(fs_actions);

	}

	/**
	 * 添加红色炸弹
	 */
	private void AddRedBomb() {
		// TODO Auto-generated method stub
		if (_Red_Bomb_Num != null)
			_Red_Bomb_Num.removeSelf();
		if (_Red_Bomb != null)
			_Red_Bomb.removeSelf();
		if (Red_Bomb_Num > 0) {
			_Red_Bomb = CCSprite.sprite(_Red_Bomb_Path);
			_Red_Bomb.setPosition(CGPoint.make(
					_Red_Bomb.getContentSize().width / 2 + 1,
					_Red_Bomb.getContentSize().height / 2 + 1));
			addChild(_Red_Bomb);

			_Red_Bomb_Num = CCLabel.makeLabel("x" + Red_Bomb_Num, _Font_Path,
					30);
			_Red_Bomb_Num.setColor(ccColor3B.ccBLACK);
			_Red_Bomb_Num.setPosition(CGPoint.ccp(
					_Red_Bomb.getContentSize().width + 5
							+ _Red_Bomb_Num.getContentSize().width / 2,
					_Red_Bomb.getContentSize().height / 2));
			addChild(_Red_Bomb_Num);// 将分数添加到场景
		}

	}

	/**
	 * 添加红色炸弹落下返回
	 */
	public void Red_Bomb_Back(Object sender) {
		CCSprite Red_Bomb = (CCSprite) sender;
		Red_Bomb.setPosition(Red_Bomb.getPosition().x, _WinSize.height / 3 * 2);
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(
				0.5f,
				CGPoint.ccp(Red_Bomb.getPosition().x, _WinSize.height / 3 * 2
						+ _WinSize.height / 6));// 时间内移动
		CCCallFuncN fs_back = CCCallFuncN.action(this, "Red_Bomb_Back2");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_back);
		Red_Bomb.runAction(fs_actions);
	}

	/**
	 * 添加红色炸弹落下返回后下降
	 */
	public void Red_Bomb_Back2(Object sender) {
		CCSprite Red_Bomb = (CCSprite) sender;
		Red_Bomb.setPosition(Red_Bomb.getPosition().x,
				_WinSize.height / 3 * 2 + 50);
		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(
				1.0f,
				CGPoint.ccp(Red_Bomb.getPosition().x,
						-(Red_Bomb.getContentSize().height / 2)));// 时间内移动
		CCCallFuncN fs_Over = CCCallFuncN.action(this, "Red_Bomb_Over");
		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
		Red_Bomb.runAction(fs_actions);
	}

	/**
	 * 添加敌机
	 */
	private void AddFoes() {

		Random rand = new Random();
		int randomValue = rand.nextInt(30);
		FeiJi_Sprite _FeiJi_Foe = new FeiJi_Sprite();
		_FeiJi_Foe.setClicked_Or(false);
		if (randomValue == 0 && _BigFoes.size() < 1) {
			_FeiJi_Foe.setLift(_Big_Life);
			_FeiJi_Foe.setMax_Life(_Big_Life);
			_FeiJi_Foe.setCCSprite(_BigFoe_Path);
			FoeDown(_FeiJi_Foe, 0);
		} else if (randomValue == 1 || randomValue == 2) {
			_FeiJi_Foe.setLift(_Middle_Life);
			_FeiJi_Foe.setMax_Life(_Middle_Life);
			_FeiJi_Foe.setCCSprite(_MiddleFoe_Path);
			FoeDown(_FeiJi_Foe, 1);
		} else {
			_FeiJi_Foe.setLift(_Small_life);
			_FeiJi_Foe.setMax_Life(_Small_life);
			_FeiJi_Foe.setCCSprite(_SmallFoe_Path);
			FoeDown(_FeiJi_Foe, 2);
		}
		_AllFoes.add(_FeiJi_Foe);

	}

	/**
	 * 改变玩家控制飞机图片
	 */
	public void AddPlay(float t) {
		_Play_Image_Chage = -_Play_Image_Chage;
		if (_FeiJi_Play != null) {
			_FeiJi_Play.removeSelf();
		}
		if (_Play_Image_Chage == 1) {
			_FeiJi_Play = CCSprite.sprite(_Play_Path);
		} else {
			_FeiJi_Play = CCSprite.sprite(_Play_Path2);
		}
		if (_Touch_Location == null) {
			_Touch_Location = CCDirector.sharedDirector().convertToGL(
					CGPoint.ccp(_WinSize.width / 2, _WinSize.height
							- _FeiJi_Play.getContentSize().height / 2));
		}
		_FeiJi_Play.setPosition(_Touch_Location.x, _Touch_Location.y);
		addChild(_FeiJi_Play);
	}

	/**
	 * 添加玩家控制飞机
	 */
	private void AddPlay() {
		_FeiJi_Play = CCSprite.sprite(_Play_Path);
		_FeiJi_Play.setPosition(_WinSize.width / 2,
				_FeiJi_Play.getContentSize().height / 2);
		addChild(_FeiJi_Play);
	}

	/**
	 * 改变大型敌机图片
	 */
	public void AddBigFoe(float t) {
		if (_BigFoes.size() > 0) {
			_BigFoe_Image_Chage = -_BigFoe_Image_Chage;
			FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoes.get(0);
			if (_Play_Image_Chage == 1)
				ChageSpriteBack(BigFoe, false, _BigFoe_Path, 0);
			else
				ChageSpriteBack(BigFoe, false, _BigFoe_Path2, 0);
			BigFoe.getCCSprite().removeSelf();
			_BigFoes.remove(0);
		}

	}

	/**
	 * 添加飞机子弹
	 */
	private void AddShot() {
		CCSprite _FeiJi_Shot = null;

		if (Blue_Shot_Change) {
			_FeiJi_Shot = CCSprite.sprite(_Blue_Shot);
			_FeiJi_Shot.setTag(1);
			if (System.currentTimeMillis() - Blue_Shot_Last_Time > 10000) {
				Blue_Shot_Change = false;
			}
		} else {
			_FeiJi_Shot = CCSprite.sprite(_Shot_Path);
			_FeiJi_Shot.setTag(0);
		}

		float localX = 0, localY = 0;

		if (_Touch_Location == null) {
			localX = _WinSize.width / 2;
			localY = _FeiJi_Play.getContentSize().height
					+ _FeiJi_Shot.getContentSize().height / 2;
		} else {
			localX = _Touch_Location.x;
			localY = _Touch_Location.y + _FeiJi_Play.getContentSize().height
					/ 2 + _FeiJi_Shot.getContentSize().height / 2;
		}

		_FeiJi_Shot.setPosition(localX, localY);
		addChild(_FeiJi_Shot);

		_Shots.add(_FeiJi_Shot);

		// 在时间内移动到目的地
		CCFiniteTimeAction fs_timeAction = CCMoveBy.action(_Shot_Du, CGPoint
				.ccp(localX - localX, _FeiJi_Shot.getContentSize().height / 2
						+ _WinSize.height));// CCMoveBy是向量，相当于从当前点开始加上你的点的大小就是移动过后的位置
		CCCallFuncN fs_Over = null;
		fs_Over = CCCallFuncN.action(this, "Shot_Over");

		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);// 循环运行
		_FeiJi_Shot.runAction(fs_actions);
	}

	/**
	 * 添加敌机落下
	 */
	private void FoeDown(FeiJi_Sprite _FeiJi_Foe2, int i) {
		Random rand = new Random();
		int minX = (int) (_FeiJi_Foe2.getCCSprite().getContentSize().width / 2.0f);
		int maxX = (int) (_WinSize.width - _FeiJi_Foe2.getCCSprite()
				.getContentSize().width / 2.0f);
		int rangeX = maxX - minX;
		int actualX = rand.nextInt(rangeX) + minX;
		int minDuration = FoeDown_Time - 4;
		int maxDuration = FoeDown_Time;
		if (_Get_Score > 1000000) {
			maxDuration = FoeDown_Time - 2;
			minDuration = FoeDown_Time - 5;
		}
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		if (actualDuration < 0) {
			actualDuration = rand.nextInt(2) + 2;
		}

		_FeiJi_Foe2.setInitX(actualX);
		_FeiJi_Foe2.setInitDuration(actualDuration);
		_FeiJi_Foe2.setInitY(_FeiJi_Foe2.getCCSprite().getContentSize().height
				/ 2.0f + _WinSize.height);

		_FeiJi_Foe2.getCCSprite().setPosition(
				actualX,
				_FeiJi_Foe2.getCCSprite().getContentSize().height / 2.0f
						+ _WinSize.height);
		addChild(_FeiJi_Foe2.getCCSprite());

		if (i == 0) {
			_FeiJi_Foe2.getCCSprite().setTag(0);
			_BigFoes.add(_FeiJi_Foe2);
		} else {
			if (i == 1) {
				_FeiJi_Foe2.getCCSprite().setTag(1);
			} else {
				_FeiJi_Foe2.getCCSprite().setTag(2);
			}
			_Foes.add(_FeiJi_Foe2);
		}

		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(actualDuration,
				CGPoint.ccp(actualX, -(_FeiJi_Foe2.getCCSprite()
						.getContentSize().height / 2)));// 时间内移动
		CCCallFuncN fs_Over = null;
		if (i == 0)
			fs_Over = CCCallFuncN.action(this, "BigFoe_Over");
		else
			fs_Over = CCCallFuncN.action(this, "Foe_Over");

		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
		_FeiJi_Foe2.getCCSprite().runAction(fs_actions);
	}

	/**
	 * 给予时间和位置，添加敌机落下
	 */
	private void Down(FeiJi_Sprite _FeiJi_Foe, int i, float y) {

		_FeiJi_Foe.getCCSprite().setPosition(_FeiJi_Foe.getInitX(), y);
		addChild(_FeiJi_Foe.getCCSprite());

		if (i == 0) {
			_FeiJi_Foe.getCCSprite().setTag(0);
			_BigFoes.add(_FeiJi_Foe);
		} else {
			if (i == 1) {
				_FeiJi_Foe.getCCSprite().setTag(1);
			} else {
				_FeiJi_Foe.getCCSprite().setTag(2);
			}
			_Foes.add(_FeiJi_Foe);
		}

		CCFiniteTimeAction fs_timeAction = CCMoveTo.action(_FeiJi_Foe
				.getInitDuration(), CGPoint.ccp(_FeiJi_Foe.getInitX(),
				-(_FeiJi_Foe.getCCSprite().getContentSize().height / 2)));// 时间内移动
		CCCallFuncN fs_Over = null;
		if (i == 0)
			fs_Over = CCCallFuncN.action(this, "BigFoe_Over");
		else
			fs_Over = CCCallFuncN.action(this, "Foe_Over");

		CCSequence fs_actions = CCSequence.actions(fs_timeAction, fs_Over);
		_FeiJi_Foe.getCCSprite().runAction(fs_actions);
	}

	/**
	 * 添加结束清除敌机
	 */
	public void Foe_Over(Object sender) {
		CCSprite foe_over = (CCSprite) sender;
		foe_over.removeSelf();

		for (int i = 0; i < _Foes.size(); i++) {
			FeiJi_Sprite foe = _Foes.get(i);
			if (foe.getCCSprite() == foe_over) {
				_Foes.remove(i);
				break;
			}
		}
	}

	/**
	 * 添加结束清除敌机
	 */
	public void BigFoe_Over(Object sender) {
		CCSprite bigfoe_over = (CCSprite) sender;
		bigfoe_over.removeSelf();

		for (int i = 0; i < _BigFoes.size(); i++) {
			FeiJi_Sprite foe = _BigFoes.get(i);
			if (foe.getCCSprite() == bigfoe_over) {
				_BigFoes.remove(i);
				break;
			}
		}

	}

	/**
	 * 添加结束清除子弹
	 */
	public void Shot_Over(Object sender) {
		CCSprite shot_over = (CCSprite) sender;
		_Shots.remove(shot_over);
		shot_over.removeSelf();
	}

	/**
	 * 添加结束清除红色炸弹
	 */
	public void Red_Bomb_Over(Object sender) {
		CCSprite bomb_over = (CCSprite) sender;
		bomb_over.removeSelf();
		_Red_Bombs.remove(bomb_over);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		// TODO Auto-generated method stub
		CGPoint Location = CCDirector.sharedDirector().convertToGL(
				CGPoint.ccp(event.getX(), event.getY()));// 获取点击位置
		CGRect Rect = _FeiJi_Play.getBoundingBox();
		CGRect Rect2 = _FeiJi_Pause.getBoundingBox();
		CGRect Rect3 = null;
		if (_Red_Bomb != null)
			Rect3 = _Red_Bomb.getBoundingBox();
		if (CGRect.containsPoint(Rect, Location) && _Pause_OR != 1) {// 判断是否点击到控制飞机
			_Can_Move = true;
		} else {
			_Can_Move = false;
			if (CGRect.containsPoint(Rect2, Location)) {
				_Pause_OR = -_Pause_OR;
				if (_Pause_OR == 1) {
					CCDirector.sharedDirector().pause();
				} else {
					CCDirector.sharedDirector().resume();
				}
			}
			if (_Pause_OR != 1) {
				if (_Red_Bomb != null && CGRect.containsPoint(Rect3, Location)
						&& Red_Bomb_Num > 0) {
					Red_Bomb_Num--;
					AddRedBomb();
					ReMoveAll();
				}
			}
		}

		return super.ccTouchesBegan(event);
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.ccTouchesEnded(event);
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		// TODO Auto-generated method stub
		if (_Can_Move) {
			_Touch_Location = CCDirector.sharedDirector().convertToGL(
					CGPoint.ccp(event.getX(), event.getY()));
			_FeiJi_Play.setPosition(_Touch_Location.x, _Touch_Location.y);
		}
		return super.ccTouchesMoved(event);
	}

	/**
	 * 控制飞机的碰撞
	 */
	private void PlayOver() {
		CGRect Rect = _FeiJi_Play.getBoundingBox();
		CGRect Rect3 = CGRect.make(Rect.origin.x + (Rect.size.width / 3.2f),
				Rect.origin.y, (Rect.size.width / 3.2f), Rect.size.height);
		for (int j = 0; j < _BigFoes.size(); j++) {
			FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoes.get(j);
			CGRect Rect2 = BigFoe.getCCSprite().getBoundingBox();
			if (CGRect.intersects(Rect2, Rect3)) {
				StopSchedule();
				AddSpriteAnimal(BigFoe.getCCSprite().getPosition(),
						_BigFoe_Sequence_Path, 164, 245, 6);
				BigFoe.getCCSprite().removeSelf();
				_BigFoes.remove(j);
				AddPlaySpriteAnimal(_FeiJi_Play.getPosition(),
						_Play_Sequence_Path, 99, 123, 4);
				_FeiJi_Play.removeSelf();
			}
		}
		for (int j = 0; j < _Foes.size(); j++) {
			FeiJi_Sprite Foe = _Foes.get(j);
			CGRect Rect2 = Foe.getCCSprite().getBoundingBox();
			if (CGRect.intersects(Rect2, Rect3)) {
				StopSchedule();
				if (Foe.getCCSprite().getTag() == 2) {
					AddSpriteAnimal(Foe.getCCSprite().getPosition(),
							_SmallFoe_Sequence_Path, 52, 52, 3);
				} else {
					AddSpriteAnimal(Foe.getCCSprite().getPosition(),
							_MiddleFoe_Sequence_Path, 69, 87, 4);
				}
				Foe.getCCSprite().removeSelf();
				_Foes.remove(j);
				AddPlaySpriteAnimal(_FeiJi_Play.getPosition(),
						_Play_Sequence_Path, 99, 123, 4);
				_FeiJi_Play.removeSelf();
			}
		}
	}

	/**
	 * 停止持续的方法
	 */
	private void StopSchedule() {
		this.unschedule("GameFoes");
		this.unschedule("GameShot");
		this.unschedule("AddPlay");
		this.unschedule("Detection");
		this.unschedule("AddRedBlueDown");
	}

	/**
	 * 碰撞监听
	 * 
	 * @param t
	 */
	public void Detection(float t) {

		for (int i = 0; i < _Shots.size(); i++) {
			CCSprite Shot = _Shots.get(i);
			CGRect Rect = Shot.getBoundingBox();
			for (int j = 0; j < _BigFoes.size(); j++) {
				FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoes.get(j);
				CGRect Rect2 = BigFoe.getCCSprite().getBoundingBox();
				if (CGRect.intersects(Rect2, Rect)) {// 判断碰撞
					_ChangeImage_Delay = 0;
					if (Shot.getTag() == 1)
						BigFoe.Life -= 2;
					else
						BigFoe.Life--;
					_Shots.remove(Shot);
					Shot.removeSelf();
					if (BigFoe.Life <= 0) {// 生命结束 飞机消失
						BigFoe.getCCSprite().removeSelf();
						_BigFoes.remove(j);
						ChageScore(30000);
						AddSpriteAnimal(BigFoe.getCCSprite().getPosition(),
								_BigFoe_Sequence_Path, 164, 245, 6);
					} else {
						ChageSpriteBack(BigFoe, true, _BigFoe_Path_2, 0);
						BigFoe.getCCSprite().removeSelf();
						_BigFoes.remove(j);
					}
				} else {
					_ChangeImage_Delay++;
					if (BigFoe.getClicked_Or() && _ChangeImage_Delay >= 10) {
						ChageSpriteBack(BigFoe, false, _BigFoe_Path, 0);
						BigFoe.getCCSprite().removeSelf();
						_BigFoes.remove(j);
					}
				}
			}
			for (int j = 0; j < _Foes.size(); j++) {
				FeiJi_Sprite Foe = _Foes.get(j);
				CGRect Rect2 = Foe.getCCSprite().getBoundingBox();
				if (CGRect.intersects(Rect2, Rect)) {
					_ChangeImage_Delay = 0;
					if (Shot.getTag() == 1)
						Foe.Life -= 2;
					else
						Foe.Life--;
					_Shots.remove(Shot);
					Shot.removeSelf();
					if (Foe.Life <= 0) {
						if (Foe.getCCSprite().getTag() == 2) {
							ChageScore(1000);
							AddSpriteAnimal(Foe.getCCSprite().getPosition(),
									_SmallFoe_Sequence_Path, 52, 52, 3);
						} else {
							ChageScore(6000);
							AddSpriteAnimal(Foe.getCCSprite().getPosition(),
									_MiddleFoe_Sequence_Path, 69, 87, 4);
						}
						Foe.getCCSprite().removeSelf();
						_Foes.remove(j);
					} else {
						if (Foe.getCCSprite().getTag() == 2) {

						} else {
							ChageSpriteBack(Foe, true, _MiddleFoe_Path_2, 1);
							Foe.getCCSprite().removeSelf();
							_Foes.remove(j);
						}
					}
				} else {
					_ChangeImage_Delay++;
					if (Foe.getClicked_Or() && _ChangeImage_Delay >= 10) {
						if (Foe.getCCSprite().getTag() == 2) {

						} else {
							ChageSpriteBack(Foe, false, _MiddleFoe_Path, 1);
							Foe.getCCSprite().removeSelf();
							_Foes.remove(j);
						}
					}
				}

			}
		}

		for (int i = 0; i < _Red_Bombs.size(); i++) {
			CCSprite bomb = _Red_Bombs.get(i);
			CGRect Rect = bomb.getBoundingBox();
			CGRect Rect2 = _FeiJi_Play.getBoundingBox();
			if (CGRect.intersects(Rect2, Rect)) {
				bomb.removeSelf();
				_Red_Bombs.remove(bomb);
				if (bomb.getTag() == 0) {
					Red_Bomb_Num++;
					AddRedBomb();
				} else {
					Blue_Shot_Change = true;
					Blue_Shot_Last_Time = System.currentTimeMillis();
				}
			}
		}
		if (!_Invincible) {
			PlayOver();
		}
	}

	/**
	 * 清除当前所有敌机
	 */
	private void ReMoveAll() {
		List<FeiJi_Sprite> _FoesAll = _Foes;
		List<FeiJi_Sprite> _BigFoesAll = _BigFoes;
		for (int j = 0; j < _FoesAll.size(); j++) {
			FeiJi_Sprite Foe = _FoesAll.get(j);
			Foe.Life = 0;
			if (Foe.Life <= 0) {
				if (Foe.getCCSprite().getTag() == 2) {
					ChageScore(1000);
					AddSpriteAnimal(Foe.getCCSprite().getPosition(),
							_SmallFoe_Sequence_Path, 52, 52, 3);
				} else {
					ChageScore(6000);
					AddSpriteAnimal(Foe.getCCSprite().getPosition(),
							_MiddleFoe_Sequence_Path, 69, 87, 4);
				}
				Foe.getCCSprite().removeSelf();
			}
		}
		_Foes.removeAll(_FoesAll);
		for (int j = 0; j < _BigFoesAll.size(); j++) {
			FeiJi_Sprite BigFoe = (FeiJi_Sprite) _BigFoesAll.get(j);
			BigFoe.Life = 0;
			if (BigFoe.Life <= 0) {
				BigFoe.getCCSprite().removeSelf();
				ChageScore(30000);
				AddSpriteAnimal(BigFoe.getCCSprite().getPosition(),
						_BigFoe_Sequence_Path, 164, 245, 6);
			}
		}
		_BigFoes.removeAll(_BigFoesAll);
	}

	/**
	 * 分数改变
	 * 
	 * @param score
	 */
	private void ChageScore(int score) {
		_Get_Score += score;
		AddScore();

	}

	/**
	 * 添加分数
	 */
	private void AddScore() {
		if (_ScoreLabel != null)
			_ScoreLabel.removeSelf();
		_ScoreLabel = CCLabel.makeLabel("SCORE:" + _Get_Score, _Font_Path, 30);
		_ScoreLabel.setColor(ccColor3B.ccWHITE);
		_ScoreLabel.setString("SCORE:" + _Get_Score);
		_ScoreLabel.setPosition(CGPoint.ccp(_FeiJi_Pause.getContentSize().width
				+ 5 + _ScoreLabel.getTexture().getWidth() / 2, _WinSize.height
				- _FeiJi_Pause.getContentSize().height / 2));
		addChild(_ScoreLabel);// 将分数添加到场景
	}

	/**
	 * 添加敌机消失动画
	 */
	private void AddSpriteAnimal(CGPoint touchRect, String Path, int CutW,
			int CutH, int Cut) {

		CCSpriteSheet boomSheet = CCSpriteSheet.spriteSheet(Path);

		this.addChild(boomSheet);

		CCSprite Sprite = CCSprite.sprite(boomSheet.getTexture(),
				CGRect.make(0, 0, CutW, CutH));
		boomSheet.addChild(Sprite);
		Sprite.setPosition(touchRect.x, touchRect.y);
		int frameCount = 0;

		ArrayList<CCSpriteFrame> boomAnimFrames = new ArrayList<CCSpriteFrame>();

		for (int y = 0; y < 1; y++) {
			for (int x = 0; x < Cut; x++) {
				CCSpriteFrame frame = CCSpriteFrame.frame(
						boomSheet.getTexture(),
						CGRect.make(x * CutW, y * CutH, CutW, CutH),
						CGPoint.ccp(0, 0));
				boomAnimFrames.add(frame);
				frameCount++;
				if (frameCount == Cut)
					break;
			}
		}
		CCAnimation boomAnimation = CCAnimation.animation("", (float) 0.1,
				boomAnimFrames);

		CCAnimate boomAction = CCAnimate.action(boomAnimation);

		CCCallFuncN actionAnimateDone = CCCallFuncN.action(this,
				"SpriteAnimationFinished");
		CCSequence actions = CCSequence.actions(boomAction, actionAnimateDone);

		Sprite.runAction(actions);
	}

	/**
	 * 添加主机消失动画
	 */
	private void AddPlaySpriteAnimal(CGPoint touchRect, String Path, int CutW,
			int CutH, int Cut) {

		CCSpriteSheet boomSheet = CCSpriteSheet.spriteSheet(Path);

		this.addChild(boomSheet);

		CCSprite Sprite = CCSprite.sprite(boomSheet.getTexture(),
				CGRect.make(0, 0, CutW, CutH));
		boomSheet.addChild(Sprite);
		Sprite.setPosition(touchRect.x, touchRect.y);
		int frameCount = 0;

		ArrayList<CCSpriteFrame> boomAnimFrames = new ArrayList<CCSpriteFrame>();

		for (int y = 0; y < 1; y++) {
			for (int x = 0; x < Cut; x++) {
				CCSpriteFrame frame = CCSpriteFrame.frame(
						boomSheet.getTexture(),
						CGRect.make(x * CutW, y * CutH, CutW, CutH),
						CGPoint.ccp(0, 0));
				boomAnimFrames.add(frame);
				frameCount++;
				if (frameCount == Cut)
					break;
			}
		}
		CCAnimation boomAnimation = CCAnimation.animation("", (float) 0.2,
				boomAnimFrames);

		CCAnimate boomAction = CCAnimate.action(boomAnimation);

		CCCallFuncN actionAnimateDone = CCCallFuncN.action(this,
				"PlaySpriteAnimationFinished");
		CCSequence actions = CCSequence.actions(boomAction, actionAnimateDone);

		Sprite.runAction(actions);
	}

	/**
	 * 结束敌机消失动画
	 */
	public void SpriteAnimationFinished(Object sender) {
		CCSprite SpriteFinished = (CCSprite) sender;
		SpriteFinished.removeSelf();
	}

	/**
	 * 结束主机消失动画
	 */
	public void PlaySpriteAnimationFinished(Object sender) {
		if (_Share != null) {
			if (_Share != null)
				ScoreList = _Share.getString("SCORE", "0;0;0;0;0;0;0;0;0;0");
			String[] Scores = ScoreList.split(";");
			String[] Scores2 = ScoreList.split(";");
			String _Score_Value = "";
			boolean or = false;
			for (int i = 0; i < Scores.length; i++) {
				if (_Get_Score > Integer.valueOf(Scores[i]) && !or) {
					Scores[i] = _Get_Score + "";
					or = true;
					int j = i;
					while (j < Scores.length - 1) {
						Scores[j + 1] = Scores2[j];
						j++;
					}
				}
				if (i >= Scores.length - 1)
					_Score_Value += Scores[i];
				else
					_Score_Value += Scores[i] + ";";
			}
			Editor e = _Share.edit();
			e.putString("SCORE", _Score_Value);
			e.commit();
		}
		CCSprite SpriteFinished = (CCSprite) sender;
		SpriteFinished.removeSelf();
		ShowDialog();
	}

	/**
	 * 击中更换敌机图片
	 * 
	 * @param Foe
	 * @param Click
	 * @param Path
	 * @param i
	 */
	private void ChageSpriteBack(FeiJi_Sprite Foe, boolean Click, String Path,
			int i) {
		FeiJi_Sprite _FeiJi_Foe = new FeiJi_Sprite();
		_FeiJi_Foe.setClicked_Or(Click);
		_FeiJi_Foe.setLift(Foe.Life);
		_FeiJi_Foe.setCCSprite(Path);
		_FeiJi_Foe.setInitX(Foe.getInitX());
		_FeiJi_Foe.setInitY(Foe.getCCSprite().getPosition().y);
		float _sudu = (Foe.getInitY() + _FeiJi_Foe.getCCSprite()
				.getContentSize().height / 2.0f)
				/ (float) Foe.getInitDuration();
		float time = ((Foe.getCCSprite().getPosition().y + _FeiJi_Foe
				.getCCSprite().getContentSize().height / 2.0f) / _sudu);
		_FeiJi_Foe.setInitDuration(time);
		Down(_FeiJi_Foe, i, Foe.getCCSprite().getPosition().y);
		Foe.getCCSprite().removeSelf();
	}

	private void ShowDialog() {
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater.from(CCDirector
						.sharedDirector().getActivity());
				View v = inflater.inflate(R.layout.feiji_dialog, null);// 得到加载view
				_Dialog = new Dialog(CCDirector.sharedDirector().getActivity(),
						R.style.Dialog_Style);
				_Dialog.setCancelable(false);
				_Dialog.setCanceledOnTouchOutside(false);
				_Dialog.setContentView(v);// 设置布局
				_Dialog.show();
				TextView _Score_TV = (TextView) v
						.findViewById(R.id.feiji_dialog_score);
				_Score_TV.setText(_Get_Score + "");
				Button _Return = (Button) v
						.findViewById(R.id.feiji_dialog_return);
				_Return.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						IntentToBack();
						_Dialog.dismiss();
					}
				});
			}
		});

	}

	private void IntentToBack() {
		Intent mainScore = new Intent(
				CCDirector.sharedDirector().getActivity(), FeiJi_Menu.class);
		CCDirector.sharedDirector().getActivity().startActivity(mainScore);
		CCDirector.sharedDirector().getActivity().finish();
	}
}
