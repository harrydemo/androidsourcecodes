package org.loon.game.test.avg;

import org.loon.framework.android.game.action.avg.AVGDialog;
import org.loon.framework.android.game.action.avg.AVGScreen;
import org.loon.framework.android.game.action.avg.command.Command;
import org.loon.framework.android.game.core.graphics.component.LButton;
import org.loon.framework.android.game.core.graphics.component.LMessage;
import org.loon.framework.android.game.core.graphics.component.LPaper;
import org.loon.framework.android.game.core.graphics.component.LSelect;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;


/**
 * Copyright 2008 - 2010
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
public class MyAVGScreen extends AVGScreen {

	LPaper roleName;
	
	// 自定义命令（有些自定义命令为了突出写成了中文，实际不推荐）
	String flag = "自定义命令.";

	String[] selects = { "鹏凌三千帅不帅？" };

	int type;

	public MyAVGScreen() {
		super("assets/script/s1.txt", AVGDialog.getRMXPDialog("assets/w6.png",
				460, 150));
	}

	public void onLoading() {
		roleName = new LPaper("assets/name0.png", 25, 25);
		leftOn(roleName);
		roleName.setLocation(5, 15);
		add(roleName);
	}

	public void drawScreen(GLEx g) {
		switch (type) {
		case 1:
			g.setAntiAlias(true);
			g.drawSixStart(GLColor.yellow, 130, 100, 100);
			g.setAntiAlias(false);
			break;
		}
		g.resetColor();
	}


	public void initCommandConfig(Command command) {
		// 初始化时预设变量
		command.setVariable("p", "assets/p.png");
		command.setVariable("sel0", selects[0]);
	}

	public void initMessageConfig(LMessage message) {

	}

	public void initSelectConfig(LSelect select) {
	}

	public boolean nextScript(String mes) {

		// 自定义命令（有些自定义命令为了突出写成了中文，实际不推荐）
		if (roleName != null) {
			if ("noname".equalsIgnoreCase(mes)) {
				roleName.setVisible(false);
			} else if ("name0".equalsIgnoreCase(mes)) {
				roleName.setVisible(true);
				roleName.setBackground("assets/name0.png");
				roleName.setLocation(5, 15);
			} else if ("name1".equalsIgnoreCase(mes)) {
				roleName.setVisible(true);
				roleName.setBackground("assets/name1.png");
				roleName.setLocation(getWidth() - roleName.getWidth() - 5, 15);
			}
		}
		if ((flag + "星星").equalsIgnoreCase(mes)) {
			// 添加脚本事件标记（需要点击后执行）
			setScrFlag(true);
			type = 1;
			return false;
		} else if ((flag + "去死吧，星星").equalsIgnoreCase(mes)) {
			type = 0;
		} else if ((flag + "关于天才").equalsIgnoreCase(mes)) {
			message.setVisible(false);
			setScrFlag(true);
			// 强行锁定脚本
			setLocked(true);
			LButton yes = new LButton("assets/dialog_yes.png", 112, 33) {
				public void doClick() {
					// 解除锁定
					setLocked(false);
					// 触发事件
					// click();
					// 删除当前按钮
					remove(this);
				}
			};
			centerOn(yes);
			add(yes);
			return false;
		}
		return true;
	}

	public void onExit() {
		// 重新返回标题画面
		setScreen(new TitleScreen());
	}

	public void onSelect(String message, int type) {
		if (selects[0].equalsIgnoreCase(message)) {
			command.setVariable("sel0", String.valueOf(type));
		}
	}

}
