package org.loon.framework.javase.game.srpg.effect;

import java.awt.Color;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;

//令角色身体冒出火花，可用于升级，治疗等特效
public class SRPGSparkEffect extends SRPGEffect {

	private int left;

	private int top;

	private int width;

	private int height;

	private int value;

	private Color color;

	private int[][] effpos;

	private int[] effframe;

	private int max;

	private int wait;

	public SRPGSparkEffect(int x, int y, int w, int h, int v, int wait) {
		this(x, y, w, h, v, wait, new Color(220, 220, 0));
	}

	public SRPGSparkEffect(int x, int y, int w, int h, int v, int wait, Color color) {
		this.setExist(true);
		this.left = x;
		this.top = y;
		this.width = w;
		this.height = h;
		this.value = v;
		this.wait = wait;
		this.color = color;
		this.effpos = new int[v][2];
		this.effframe = new int[v];
		for (int i = 0; i < v; i++) {
			effframe[i] = -1;
		}
		this.max = 0;
	}

	public void draw(LGraphics g, int x, int y) {
		next();
		g.setColor(color);
		if (value > max && super.frame % wait == 0) {
			effpos[max][0] = left + LSystem.random.nextInt(width);
			effpos[max][1] = top + LSystem.random.nextInt(height);
			effframe[max] = 0;
			max++;
		}
		for (int i = 0; i < max; i++) {
			if (effframe[i] != -1 && effframe[i] < 10) {
				effframe[i]++;
				int nx = effpos[i][0] - x;
				int ny = effpos[i][1] - y;
				int f = effframe[i];
				g.drawLine(nx, ny - f, nx, ny + f);
				g.drawLine(nx - f, ny, nx + f, ny);
			}
		}
		if (super.frame > 10 + value * wait) {
			setExist(false);
		}
	}

}
