package org.metalslug;

import org.redengine.systems.graphsystem.opengl.RTexture;
import org.redengine.systems.graphsystem.opengl.RTextureManager;
import org.redengine.systems.graphsystem.sprite.spriteex.RFont;

public class GameFont extends RFont {
	
	public void init(){
		final RTexture t=RTextureManager.getTextureManager().getTexture("gamefont");
		imgs=t.clipTexture(5, 8);
	}
	
	public int getCharImageIndex(char cs) {
		final char c=Character.toLowerCase(cs);
		int temp=Integer.MAX_VALUE;
		switch(c){
		case ' ':
			break;
		case '?':
			temp=27;
			break;
		case '!':
			temp=26;
			break;
		case ',':
			temp=28;
			break;
		case '.':
			temp=29;
			break;
		default:
			if(Character.isDigit(c)){
				temp=c-'0'+30;
			}else{
				temp=c-'a';
			}
		}
		return temp;
	}
}
