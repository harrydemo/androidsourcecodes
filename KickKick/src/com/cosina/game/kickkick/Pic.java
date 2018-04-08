package com.cosina.game.kickkick;

public class Pic {
	public static final int NOTHING = 0;
	
	public static final int UP_ONE = 13;
	
	public static final int DOWN_HIT = -9;
	
	int currentType = NOTHING;
	
	public void toNext(){
		if(currentType > 0){
			currentType --;
			if(currentType == NOTHING){
				KickView.self.hp --;
			}
		}
		else if(currentType < 0){
			currentType ++;
		}
	}

	public void toShow() {
		currentType = UP_ONE;
	}
	
	public void click(){
		if(currentType > NOTHING){
			currentType = DOWN_HIT;
		}
	}
}
