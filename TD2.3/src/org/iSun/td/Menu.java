package org.iSun.td;

import org.iSun.td.model.Turret;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.LPaper;
import org.loon.framework.android.game.core.graphics.window.actor.Layer;

/**
 * 按钮
 * 
 * @author iSun
 * 
 */
public class Menu extends Layer {
	private final TurretDefense td;
	public LPaper update;
	public LPaper sell;

	public Menu(final TurretDefense td) {
		super(105, 380, true);
		this.td = td;
		// 设置menu层高于MapLayer
		setLayer(101);
		// 不锁定menu拖动
		setLocked(true);
		setLimitMove(false);
		// 锁定Actor拖动
		setActorDrag(false);
		setDelay(200);
		// 设置Menu背景

		LImage image = LImage.createImage(this.getWidth(), this.getHeight(),
				false);
		LGraphics g = image.getLGraphics();
		g.setColor(LColor.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(LColor.white);
		g.setFont(16);

		g.dispose();
		setBackground(image);

		LPaper bulletTurret = new LPaper("assets/button_0.png") {

			@Override
			public void paint(LGraphics g) {
				// TODO Auto-generated method stub
				if (Menu.this.td.selectTurret == 0) {
					g.setAntiAlias(true);
					LColor color = g.getColor();
					float alph = g.getAlpha();

					g.setColor(LColor.red);
					g.setAlpha(0.3F);

					g.fillRect(0, 0, this.getWidth(), this.getWidth());
					g.setColor(color);
					g.setAlpha(alph);
				}
			}

			@Override
			public void downClick() {
				// TODO Auto-generated method stub
				Menu.this.td.selectTurret = 0;
			}
		};

		bulletTurret.setLocation(60, 40);

		LPaper poisonTurret = new LPaper("assets/button_1.png") {

			@Override
			public void downClick() {
				// TODO Auto-generated method stub
				Menu.this.td.selectTurret = 2;
			}

			@Override
			public void paint(LGraphics g) {
				// TODO Auto-generated method stub
				if (Menu.this.td.selectTurret == 2) {
					g.setAntiAlias(true);
					LColor color = g.getColor();
					float alph = g.getAlpha();

					g.setColor(LColor.red);
					g.setAlpha(0.3F);

					g.fillRect(0, 0, this.getWidth(), this.getWidth());
					g.setColor(color);
					g.setAlpha(alph);
				}
			}
		};
		poisonTurret.setLocation(60, 105);

		LPaper bombTurret = new LPaper("assets/button_2.png") {

			@Override
			public void downClick() {
				// TODO Auto-generated method stub
				Menu.this.td.selectTurret = 1;
			}

			@Override
			public void paint(LGraphics g) {
				// TODO Auto-generated method stub
				if (Menu.this.td.selectTurret == 1) {
					g.setAntiAlias(true);
					LColor color = g.getColor();
					float alph = g.getAlpha();

					g.setColor(LColor.red);
					g.setAlpha(0.3F);

					g.fillRect(0, 0, this.getWidth(), this.getWidth());
					g.setColor(color);
					g.setAlpha(alph);
				}
			}

		};
		bombTurret.setLocation(60, 170);

		LPaper laserTurret = new LPaper("assets/button_3.png") {

			@Override
			public void downClick() {
				// TODO Auto-generated method stub
				Menu.this.td.selectTurret = 3;
			}

			@Override
			public void paint(LGraphics g) {
				// TODO Auto-generated method stub
				if (Menu.this.td.selectTurret == 3) {
					g.setAntiAlias(true);
					LColor color = g.getColor();
					float alph = g.getAlpha();

					g.setColor(LColor.red);
					g.setAlpha(0.3F);

					g.fillRect(0, 0, this.getWidth(), this.getWidth());
					g.setColor(color);
					g.setAlpha(alph);
				}
			}

		};
		laserTurret.setLocation(60, 235);

		LPaper updateTurret = new LPaper("assets/tower_upgrade.png") {

			@Override
			public void downClick() {
				// TODO Auto-generated method stub
				td.playtAssetsMusic("audio/button_press.mp3", false);
				if (td.beSelectedTurret != null) {
					int cost = td.beSelectedTurret.update();
					td.setMoney(td.getMoney() - cost);
					// 如果当前的金币数大于被选中炮台下一次升级所需要的金钱，那么升级按钮继续显示
					if (td.beSelectedTurret.currentLevel < Turret.maxLevel - 1
							&& (td.getMoney() > td.beSelectedTurret.values[td.beSelectedTurret.currentLevel + 1] / 2)) {
						update.setVisible(true);
					} else {
						update.setVisible(false);
					}
				}
			}
		};
		updateTurret.setLocation(2, 5);
		this.update = updateTurret;

		LPaper sellTurret = new LPaper("assets/tower_sell.png") {

			@Override
			public void downClick() {
				// TODO Auto-generated method stub
				if (td.beSelectedTurret != null) {
					td.playtAssetsMusic("audio/button_press.mp3", false);
					int value = td.beSelectedTurret.sell();
					td.setMoney(td.getMoney() + value);
					td.beSelectedTurret = null;
					sell.setVisible(false);
					update.setVisible(false);
				}
			}

		};
		sellTurret.setLocation(2, 250);
		this.sell = sellTurret;
		add(bulletTurret);
		add(bombTurret);
		add(poisonTurret);
		add(laserTurret);
		add(updateTurret);
		add(sellTurret);
		this.sell.setVisible(false);
		this.update.setVisible(false);

		setAlpha(0.2F);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void downClick(int x, int y) {
		// TODO Auto-generated method stub
		this.td.selectTurret = -1;
	}

}
