package org.loon.framework.javase.game.srpg;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;

/**
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class SRPGDelta {

	private int height;

	private double[][] delta;

	private double[] pos;

	private double[] move;

	private double[] avg;

	private double vector;

	private double v_speed;

	public SRPGDelta(double[][] res, double ad1[], double x, double y,
			double speed) {
		this(res, x, y, speed);
		this.setAverage(ad1);
	}

	public SRPGDelta(double[][] res, double x, double y, double speed) {
		this.setDelta(res);
		this.v_speed = speed;
		this.pos = new double[2];
		this.move = new double[2];
		this.move[0] = x;
		this.move[1] = y;
		this.resetAverage();
		this.height = LSystem.screenRect.height;
	}

	public void setDelta(double[][] res) {
		this.delta = res;
	}

	public double[][] getDelta() {
		return delta;
	}

	public void setAverage(double[] res) {
		this.avg = res;
	}

	public void resetAverage() {
		this.avg = new double[2];
		for (int j = 0; j < delta.length; j++) {
			for (int i = 0; i < avg.length; i++) {
				avg[i] += delta[j][i];
			}
		}
		for (int i = 0; i < avg.length; i++) {
			avg[i] /= 3D;
		}

	}

	public void setPosX(double x) {
		this.pos[0] = x;
	}

	public void setPosY(double y) {
		this.pos[1] = y;
	}

	public void setPos(double x, double y) {
		this.setPosX(x);
		this.setPosY(y);
	}

	public double getPosX() {
		return pos[0];
	}

	public double getPosY() {
		return pos[1];
	}

	public void setVector(double v) {
		this.vector = v;
	}

	public void setVectorSpeed(double v) {
		this.v_speed = v;
	}

	public void setMoveX(double x) {
		this.move[0] = x;
	}

	public void setMoveY(double y) {
		this.move[1] = y;
	}

	public void setMove(int x, int y) {
		setMoveX(x);
		setMoveY(y);
	}

	public void next() {
		pos[0] += move[0];
		pos[1] += move[1];
		vector += v_speed;
		vector %= 360D;
		if (vector < 0.0D) {
			vector += 360D;
		}
	}

	public int[][] drawing(int x, int y) {
		int[][] res = new int[3][2];
		for (int i = 0; i < delta.length; i++) {
			double d = getLine(delta[i][0] - avg[0], delta[i][1] - avg[1]);
			double d1 = getDegrees(delta[i][0] - avg[0], delta[i][1] - avg[1]);
			double d2 = Math.cos(Math.toRadians(vector + d1)) * d + avg[0]
					+ pos[0] + (double) x;
			double d3 = Math.sin(Math.toRadians(vector + d1)) * d + avg[1]
					+ pos[1] + (double) y;
			res[i][0] = (int) (d2 + 0.5D);
			res[i][1] = (int) (d3 + 0.5D);
		}
		return res;
	}

	public synchronized void draw(LGraphics g) {
		draw(g, 0, 0);
	}

	public synchronized void draw(LGraphics g, int x, int y) {
		next();
		int[][] res = drawing(x, y);
		for (int i = 0; i < res.length; i++) {
			int index = (i + 1) % 3;
			g.drawLine(res[i][0], height - res[i][1], res[index][0], height
					- res[index][1]);
		}

	}

	public synchronized void drawPaint(LGraphics g, int x, int y) {
		next();
		int[][] res = drawing(x, y);
		int xs[] = new int[3];
		int ys[] = new int[3];
		for (int i = 0; i < res.length; i++) {
			xs[i] = res[i][0];
			ys[i] = height - res[i][1];
		}
		g.fillPolygon(xs, ys, 3);
	}

	private double getLine(double d, double d1) {
		return Math
				.sqrt(Math.pow(Math.abs(d), 2D) + Math.pow(Math.abs(d1), 2D));
	}

	public static double getDegrees(double d, double d1) {
		if (d == 0.0D && d1 == 0.0D) {
			return 0.0D;
		}
		double d2 = Math.sqrt(Math.pow(d, 2D) + Math.pow(d1, 2D));
		double d3 = Math.toDegrees(Math.acos(d / d2));
		if (Math.asin(d1 / d2) < 0.0D) {
			return 360D - d3;
		} else {
			return d3;
		}
	}

}
