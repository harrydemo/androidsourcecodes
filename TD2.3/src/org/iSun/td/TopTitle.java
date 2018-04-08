package org.iSun.td;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.window.actor.Layer;

/**
 * 顶部记录条
 * 
 * @author iSun
 * 
 */
public class TopTitle extends Layer {
	private TurretDefense td;

	public TopTitle(TurretDefense td) {
		super(370, 20, true);
		this.td = td;
		setLayer(102);
		setLocked(true);
		setLimitMove(false);
		setActorDrag(false);
		setDelay(200);

	}

	@Override
	public void paint(LGraphics g) {
		// TODO Auto-generated method stub
		LColor color = g.getColor();
		int size = g.getFont().getSize();
		g.setColor(LColor.white);
		g.setFont(18);
		g.drawString("score:" + td.getScore(), 5, 15);
		g.drawString("wave:" + (td.getWave() + 1), 165, 15);
		g.drawString("$:" + td.getMoney(), 300, 15);
		g.setColor(color);
		g.setFont(size);
	}

}
