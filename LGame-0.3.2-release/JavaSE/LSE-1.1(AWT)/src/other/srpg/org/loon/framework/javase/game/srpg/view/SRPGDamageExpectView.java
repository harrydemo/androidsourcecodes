package org.loon.framework.javase.game.srpg.view;

import java.awt.Color;

import org.loon.framework.javase.game.core.graphics.LGradation;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.srpg.SRPGType;
import org.loon.framework.javase.game.srpg.ability.SRPGAbilityFactory;
import org.loon.framework.javase.game.srpg.ability.SRPGDamageData;
import org.loon.framework.javase.game.srpg.actor.SRPGActor;
import org.loon.framework.javase.game.srpg.actor.SRPGActors;
import org.loon.framework.javase.game.srpg.actor.SRPGStatus;
import org.loon.framework.javase.game.srpg.field.SRPGField;

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
// 默认的角色伤害预期试图
public class SRPGDamageExpectView extends SRPGDrawView {

	private SRPGDamageData dd;

	private SRPGAbilityFactory ab;

	private SRPGActor attacker;

	private SRPGActor defender;

	private final static Color attackColor = new Color(255, 220, 220),
			recoveryColornew = new Color(220, 220, 255);

	public SRPGDamageExpectView(SRPGAbilityFactory ability, SRPGField field,
			SRPGActors actors, int atk, int def) {
		this(ability, field, actors, atk, def, 330, 100);
	}

	public SRPGDamageExpectView(SRPGAbilityFactory ability, SRPGField field,
			SRPGActors actors, int atk, int def, int w, int h) {
		setExist(true);
		setLock(false);
		super.width = w;
		super.height = h;
		this.attacker = actors.find(atk);
		if (def != -1) {
			this.defender = actors.find(def);
			this.dd = ability.getDamageExpect(field, actors, atk, def);
		} else {
			this.defender = null;
			this.dd = new SRPGDamageData();
			this.dd.setGenre(ability.getGenre());
		}
		this.ab = ability;
	}

	public void draw(LGraphics g) {
		LGradation.getInstance(Color.blue, Color.black, super.width,
				super.height).drawHeight(g, super.left, super.top);
		SRPGStatus status = attacker.getActorStatus();
		g.setColor(Color.black);
		g.fillRect(5 + super.left, 2 + super.top, 80, 3);
		if (status.max_hp > 0 && status.hp > 0) {
			int i = (status.hp * 80) / status.max_hp;
			g.setColor(new Color(96, 128, 255));
			g.fillRect(5 + super.left, 2 + super.top, i, 3);
		}
		g.setColor(Color.white);
		g.drawString("Attack", 5 + super.left, 15 + super.top);
		g.drawImage(attacker.getImage(), 10 + super.left, 20 + super.top);
		g.drawString(status.name, 5 + super.left, 75 + super.top);
		g
				.drawString(String.valueOf(status.hp) + " / "
						+ String.valueOf(status.max_hp), 5 + super.left,
						90 + super.top);
		g.setColor(Color.white);
		g.drawString("Defence", 115 + super.left, 15 + super.top);
		if (defender != null) {
			SRPGStatus status1 = defender.getActorStatus();
			g.setColor(Color.black);
			g.fillRect(115 + super.left, 2 + super.top, 80, 3);
			if (status1.max_hp > 0 && status1.hp > 0) {
				int hp = (status1.hp * 80) / status1.max_hp;
				g.setColor(new Color(96, 128, 255));
				g.fillRect(115 + super.left, 2 + super.top, hp, 3);
			}
			g.setColor(Color.white);
			g.drawImage(defender.getImage(), 120 + super.left, 20 + super.top);
			g.drawString(status1.name, 115 + super.left, 75 + super.top);
			g.drawString(String.valueOf(status1.hp) + " / "
					+ String.valueOf(status1.max_hp), 115 + super.left,
					90 + super.top);
		} else {
			g.drawString("- Nothing -", 115 + super.left, 75 + super.top);
		}
		Color color = Color.white;
		String s = "";
		// 判定使用的技能类型
		switch (dd.getGenre()) {
		// 普通攻击
		case SRPGType.GENRE_ATTACK:
			// 魔法伤害
		case SRPGType.GENRE_MPDAMAGE:
			// 全局伤害
		case SRPGType.GENRE_ALLDAMAGE:
			s = "Attack";
			color = attackColor;
			break;
		// 普通恢复
		case SRPGType.GENRE_RECOVERY:
			// 魔法恢复
		case SRPGType.GENRE_MPRECOVERY:
			// 全局恢复
		case SRPGType.GENRE_ALLRECOVERY:
			s = "Recovery";
			color = recoveryColornew;
			break;
		// 辅助技能
		case SRPGType.GENRE_HELPER:
			// 治疗
		case SRPGType.GENRE_CURE:
			s = "Helper";
			break;
		// 不可用的空技能
		case -1:
			s = "---";
			break;
		}
		g.drawString("Ability", 230 + super.left, 15 + super.top);
		g.drawString(ab.getAbilityName(), 230 + super.left, 35 + super.top);
		g.drawString(s, 230 + super.left, 60 + super.top);
		g.drawString("STR", 230 + super.left, 75 + super.top);
		g.drawString("HIT", 230 + super.left, 90 + super.top);
		String s1 = dd.getHitrateExpectString();
		String s2 = dd.getDamageExpectString() + dd.getHelperString();
		if (defender == null) {
			s1 = "---";
			s2 = "---";
		}
		g.drawString(s1, 260 + super.left, 90 + super.top);
		g.setColor(color);
		g.drawString(s2, 260 + super.left, 75 + super.top);
	}
}
