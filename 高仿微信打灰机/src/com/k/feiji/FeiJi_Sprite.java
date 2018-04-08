package com.k.feiji;

import org.cocos2d.nodes.CCSprite;

public class FeiJi_Sprite {

	public int Life = 0, Max_Life = 0;
	public CCSprite _FeiJi_Foe;
	public float Init_X = 0, Init_Y = 0, Init_Duration = 0;
	public boolean Clicked_Or = false;

	public void setMax_Life(int _Max_Life) {
		Max_Life = _Max_Life;
	}

	public int getMax_Life() {
		return Max_Life;
	}

	public void setLift(int _Life) {
		Life = _Life;
	}

	public int getLift() {
		return Life;
	}

	public void setInitX(float _Init_X) {
		Init_X = _Init_X;
	}

	public float getInitX() {
		return Init_X;
	}

	public void setInitY(float _Init_Y) {
		Init_Y = _Init_Y;
	}

	public float getInitY() {
		return Init_Y;
	}

	public void setInitDuration(float _Init_Duration) {
		Init_Duration = _Init_Duration;
	}

	public float getInitDuration() {
		return Init_Duration;
	}

	public void setCCSprite(String Path) {
		_FeiJi_Foe = CCSprite.sprite(Path);
	}

	public CCSprite getCCSprite() {
		return _FeiJi_Foe;
	}

	public void setClicked_Or(boolean _Clicked_Or) {
		Clicked_Or = _Clicked_Or;
	}

	public boolean getClicked_Or() {
		return Clicked_Or;
	}
}
