package org.loon.framework.android.game.srpg.view;


import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGradation;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.srpg.SRPGScreen;
import org.loon.framework.android.game.srpg.ability.SRPGAbilityFactory;
import org.loon.framework.android.game.srpg.actor.SRPGStatus;

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
public class SRPGActorStatusView extends SRPGDrawView {

	private final static LColor colorAbility = new LColor(220, 220, 220),
			color0 = new LColor(255, 128, 96),
			color1 = new LColor(192, 220, 255),
			color2 = new LColor(192, 192, 192),
			color3 = new LColor(255, 192, 192);

	private static final LFont statusFont = LFont.getFont("Monospaced", 0, 12);

	private static final LFont bigFont = LFont.getFont("Monospaced", 0, 14);

	private String[][] cstatus;

	private LColor[] acolor;

	private SRPGStatus s;

	private String[][] status;

	private String[][] bstatus;

	private int[] swidth;

	private int[][] widths;

	public SRPGActorStatusView(SRPGStatus status) {
		this(status, 320, 280);
	}

	public SRPGActorStatusView(SRPGStatus status, int w, int h) {
		this.setExist(true);
		this.setLock(true);
		super.width = w;
		super.height = h;
		this.s = status;
		String res[][] = { { "STR", String.valueOf(status.strength) },
				{ "DEX", String.valueOf(status.dexterity) },
				{ "VIT", String.valueOf(status.vitality) },
				{ "AGI", String.valueOf(status.agility) },
				{ "MAG", String.valueOf(status.magic) },
				{ "MOV", String.valueOf(status.move) } };
		this.cstatus = new String[11][4];
		this.cstatus[0][0] = "Ability";
		this.cstatus[0][1] = "Length";
		this.cstatus[0][2] = "";
		this.cstatus[0][3] = "";
		this.acolor = new LColor[11];
		this.swidth = new int[res.length];
		for (int i = 0; i < swidth.length; i++) {
			swidth[i] = statusFont.stringWidth(res[i][1]);
		}
		String[][] res1 = {
				{ "Lv", String.valueOf(status.level), "Exp",
						String.valueOf(status.exp) },
				{ "HP", String.valueOf(status.hp), "/",
						String.valueOf(status.max_hp) },
				{ "MP", String.valueOf(status.mp), "/",
						String.valueOf(status.max_mp) } };
		this.widths = new int[res1.length][2];
		for (int j = 0; j < widths.length; j++) {
			for (int i = 0; i < 2; i++) {
				widths[j][i] = bigFont.stringWidth(res1[j][i * 2 + 1]);
			}
		}
		this.status = res;
		this.bstatus = res1;
	}

	public void draw(LGraphics g) {
		LFont old = g.getFont();
		LColor color = LColor.blue;
		if (s.team != 0) {
			color = LColor.red;
		}
		LGradation.getInstance(color, LColor.black, super.width, super.height)
				.drawHeight(g, super.left, super.top);
		g.setColor(LColor.black);
		g.fillRect(130 + super.left, 77 + super.top, 100, 3);
		g.fillRect(130 + super.left, 97 + super.top, 100, 3);
		g.setColor(96, 128, 255);
		int chp = 0;
		if (s.hp > 0) {
			chp = (s.hp * 100) / s.max_hp;
		}
		g.fillRect(130 + super.left, 77 + super.top, chp, 3);
		g.setColor(color0);
		int cmp = 0;
		if (s.mp > 0) {
			cmp = (s.mp * 100) / (s.max_mp > 0 ? s.max_mp : 1);
		}
		g.fillRect(130 + super.left, 97 + super.top, cmp, 3);
		g.setColor(LColor.white);
		g.setFont(bigFont);
		int index = 0;
		for (int i = 9; i < SRPGStatus.STATUS_MAX; i++) {
			if (s.status[i] == 0) {
				continue;
			}
			if (index > 5) {
				break;
			}
			String mes = SRPGStatus.STATUS_NAME[i];
			int size = (SRPGScreen.TILE_WIDTH - mes.length() * 6) / 2;
			g.drawStyleString(mes, 260 + size + super.left, (75 - index * 12)
					+ super.top, LColor.black, LColor.white);
			index++;
		}

		g.setColor(LColor.white);
		g.drawString(s.name, 130 + super.left, 20 + super.top);
		g.drawString(s.jobname, 130 + super.left, 40 + super.top);
		for (int i = 0; i < bstatus.length; i++) {
			g.drawString(bstatus[i][0], 130 + super.left, 60 + 20 * i
					+ super.top);
			g.drawString(bstatus[i][1], (190 - widths[i][0]) + super.left, 60
					+ 20 * i + super.top);
			g.drawString(bstatus[i][2], 205 + super.left, 60 + 20 * i
					+ super.top);
			g.drawString(bstatus[i][3], (255 - widths[i][1]) + super.left, 60
					+ 20 * i + super.top);
		}

		g.setFont(statusFont);
		int size = 0;
		int max = 0;
		do {
			if (max >= s.status.length) {
				break;
			}
			if (s.status[max] != 0) {
				g.setColor(LColor.white);
				if (max >= 9) {
					g.setColor(192, 192, 192);
				}
				String statusName = SRPGStatus.STATUS_NAME[max];
				if (statusName.length() > 6) {
					statusName = statusName.substring(0, 6);
				}
				g.drawString(statusName, 130 + super.left + 45 * size,
						117 + super.top);
				size++;
			}
			if (max >= 4) {
				break;
			}
			max++;
		} while (true);
		g.setColor(LColor.white);
		for (int i = 0; i < status.length; i++) {
			g.drawString(status[i][0], 220 + super.left, 153 + i * 18
					+ super.top);
			g.drawString(status[i][1], (300 - swidth[i]) + super.left, 153 + i
					* 18 + super.top);
		}

		int[] abilitys = s.ability;

		int[] abilityFilted = SRPGAbilityFactory.filtedAbility(abilitys, s, true);

		if (s.leader != 0) {
			String leader = "";
			if (s.leader == 1) {
				leader = "Leader";
			} else {
				if (s.leader == 2) {
					leader = "Main Leader";
				}
			}
			g.setColor(colorAbility);
			g.drawString(leader, 220 + super.left, 135 + super.top);
			g.setColor(LColor.white);
		}
		acolor[0] = colorAbility;
		index = 0;
		for (int i = 0; i < abilitys.length; i++) {
			for (index = 0; index < cstatus[i + 1].length; index++) {
				cstatus[i + 1][index] = "";
			}
			if (abilitys[i] == -1) {
				continue;
			}
			SRPGAbilityFactory ability = SRPGAbilityFactory.getInstance(abilitys[i]);
			cstatus[i + 1][0] = ability.getAbilityName();
			cstatus[i + 1][1] = String.valueOf(ability.getMinLength()) + "-"
					+ String.valueOf(ability.getMaxLength());
			cstatus[i + 1][2] = String.valueOf(ability.getRange());
			if (ability.getMP(s) > 0) {
				cstatus[i + 1][3] = String.valueOf(ability.getMP(s));
			} else {
				cstatus[i + 1][3] = "";
			}
			acolor[i + 1] = LColor.white;
			if (ability.getMP(s) > s.mp) {
				acolor[i + 1] = color2;
				continue;
			}
			if (ability.getCounter() == 2) {
				acolor[i + 1] = color1;
				continue;
			}
			boolean flag = false;
			if (abilityFilted != null) {
				index = 0;
				do {
					if (index >= abilityFilted.length) {
						break;
					}
					if (abilityFilted[index] == abilitys[i]) {
						flag = true;
						break;
					}
					index++;
				} while (true);
			}
			if (!flag) {
				acolor[i + 1] = color3;
			}
		}
		for (index = 0; index < cstatus.length; index++) {
			if(cstatus[index][0]!=null){
			g.setColor(acolor[index]);
			g.drawString(cstatus[index][0], 5 + super.left, 135 + index * 18
					+ super.top);
			g.drawString(cstatus[index][1], 130 + super.left, 135 + index * 18
					+ super.top);
			g.drawString(cstatus[index][2], 165 + super.left, 135 + index * 18
					+ super.top);
			g.drawString(cstatus[index][3], 190 + super.left, 135 + index * 18
					+ super.top);
			}
		}
		if (s.face != null) {
			g.drawImage(s.face, 5 + super.left, 6 + super.top);
			g.setColor(LColor.black);
			g.drawRect(5 + super.left, 6 + super.top, s.face.getWidth(),
					s.face.getWidth());
			g.setColor(LColor.white);
		}
		g.setFont(old);
	}

}
