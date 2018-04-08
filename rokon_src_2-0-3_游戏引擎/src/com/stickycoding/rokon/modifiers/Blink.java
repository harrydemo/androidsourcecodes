package com.stickycoding.rokon.modifiers;

import com.stickycoding.rokon.Modifier;
import com.stickycoding.rokon.Sprite;
import com.stickycoding.rokon.Time;

/**
 * Blink.java
 * A simple Modifier, mostly just an example of how to use this sytem
 * Blinks between 0 and 1 alpha, every 500ms.
 * After 10s, it stops
 * 
 * @author Richard
 */

public class Blink extends Modifier {
	
	private boolean show = true;
	private long nextFlash;
	private long end;
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Modifier#onStart()
	 */
	@Override
	public void onStart(Sprite sprite) {
		nextFlash = Time.getTicks() + 500;
		end = Time.getTicks() + 10000;
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Modifier#onUpdate(com.stickycoding.rokon.Sprite)
	 */
	@Override
	public void onUpdate(Sprite sprite) {
		if(Time.getTicks() > end) {
			sprite.setAlpha(1);
			end();
			return;
		}
		if(Time.getTicks() > nextFlash) {
			show = !show;
			if(show) {
				sprite.setAlpha(1);
			} else {
				sprite.setAlpha(0);
			}
			nextFlash = Time.getTicks() + 500;
		}
	}

	@Override
	public void onEnd(Sprite sprite) {
		// TODO Auto-generated method stub
		
	}

}
