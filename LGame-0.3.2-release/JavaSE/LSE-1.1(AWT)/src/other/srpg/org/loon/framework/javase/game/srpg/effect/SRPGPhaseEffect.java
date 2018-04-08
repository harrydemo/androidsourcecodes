package org.loon.framework.javase.game.srpg.effect;

import java.awt.Color;
import java.awt.Font;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.LGradation;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;
import org.loon.framework.javase.game.srpg.SRPGType;

public class SRPGPhaseEffect extends SRPGEffect {

	private LTimer timer;

	private int count, index;

	private boolean isEnd;

	private int twidth;

	private int fontLenght;

	private StringBuffer sbr;

	private char[] mes;

	public SRPGPhaseEffect(String mes) {
		super.isExist = true;
		super.frame = 0;
		this.timer = new LTimer(10);
		this.sbr = new StringBuffer();
		this.mes = mes.toCharArray();
		this.fontLenght = mes.length() - 1;
		this.twidth = SRPGType.DEFAULT_BIG_FONT.stringWidth(mes);
		this.index = LSystem.random.nextInt(4);
	}

	public void draw(LGraphics g, int tx, int ty) {
		next();
		if (!isEnd) {
			if (timer.action(1)) {
				sbr.append(mes[count]);
				count++;
			}
			if (count > fontLenght) {
				this.count = 0;
				this.isEnd = true;
			}
		} else {
			count++;
			if (count > 30) {
				this.isExist = false;
			}
		}
		Color color = null;
		switch (index) {
		case 0:
			color = Color.red;
			break;
		case 1:
			color = Color.orange;
			break;
		case 2:
			color = Color.blue;
			break;
		case 3:
			color = Color.yellow;
			break;
		default:
			color = Color.orange;
			break;
		}
		LGradation
				.getInstance(color, Color.black, LSystem.screenRect.width, 80)
				.drawHeight(g, 0, (LSystem.screenRect.height - 80) / 2);
		Font old = g.getFont();
		g.setFont(SRPGType.DEFAULT_BIG_FONT);
		g.drawStyleString(sbr.toString(),
				(LSystem.screenRect.width - twidth) / 2,
				(LSystem.screenRect.height ) / 2 + 10, Color.black, Color.white);
		g.setFont(old);
	}

}
