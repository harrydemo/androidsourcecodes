package org.loon.act.test;

import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.media.AssetsSound;


public class ACTWavSound implements LRelease {

	private static AssetsSound jump;

	private static AssetsSound e_deth;

	private static AssetsSound coin;

	private static AssetsSound enter;

	private static AssetsSound select;

	private static AssetsSound item;

	private static AssetsSound ten;

	private static AssetsSound bomb;

	private static AssetsSound ibox;

	private static AssetsSound deth;

	private static AssetsSound center;

	private static AssetsSound goal;

	private static AssetsSound dash;

	private static AssetsSound item_d;

	private static AssetsSound up;

	private static ACTWavSound instance;

	private ACTWavSound() {
		jump = new AssetsSound("se/jump.wav");
		e_deth = new AssetsSound("se/dene2.wav");
		coin = new AssetsSound("se/coin03.wav");
		enter = new AssetsSound("se/enter.wav");
		select = new AssetsSound("se/select.wav");
		item = new AssetsSound("se/item.wav");
		ten = new AssetsSound("se/ten2.wav");
		bomb = new AssetsSound("se/bomb.wav");
		ibox = new AssetsSound("se/ibox2.wav");
		deth = new AssetsSound("se/deth.wav");
		center = new AssetsSound("se/center2.wav");
		goal = new AssetsSound("se/goal.wav");
		dash = new AssetsSound("se/dash.wav");
		item_d = new AssetsSound("se/item_d.wav");
		up = new AssetsSound("se/up1.wav");
	}

	public synchronized static ACTWavSound getInstance() {
		if (instance == null) {
			instance = new ACTWavSound();
		}
		return instance;
	}

	public synchronized void dispose() {
		jump.stop();
		e_deth.stop();
		coin.stop();
		enter.stop();
		select.stop();
		item.stop();
		ten.stop();
		bomb.stop();
		ibox.stop();
		deth.stop();
		center.stop();
		goal.stop();
		dash.stop();
		item_d.stop();
		up.stop();
		instance = null;
	}

	public void jump() {
		jump.play();
	}

	public void e_deth() {
		e_deth.play();
	}

	public void coin() {
		coin.play();
	}

	public void enter() {
		enter.play();
	}

	public void select() {
		select.play();
	}

	public void item() {
		item.play();
	}

	public void ten() {
		ten.play();
	}

	public void bomb() {
		bomb.play();
	}

	public void ibox() {
		ibox.play();
	}

	public void deth() {
		deth.play();
	}

	public void center() {
		center.play();
	}

	public void goal() {
		goal.play();
	}

	public void dash() {
		dash.play();
	}

	public void item_d() {
		item_d.play();
	}

	public void up() {
		up.play();
	}
}
