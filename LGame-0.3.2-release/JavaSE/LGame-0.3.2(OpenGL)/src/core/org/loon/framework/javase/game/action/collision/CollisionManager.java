package org.loon.framework.javase.game.action.collision;

import java.util.ArrayList;

import org.loon.framework.javase.game.core.geom.Shape;
import org.loon.framework.javase.game.core.graphics.Screen;

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
public class CollisionManager {

	private ArrayList<CollisionEvent> actionListener;

	private CollisionListener listener;

	private Screen screen;

	public CollisionManager(final Screen s, final CollisionListener l) {
		this.screen = s;
		this.listener = l;
	}

	public synchronized void addCollisionListener(final CollisionEvent e) {
		if (this.actionListener == null) {
			this.actionListener = new ArrayList<CollisionEvent>();
		}
		this.actionListener.add(e);
	}

	public synchronized void removeCollisionListener(final CollisionEvent e) {
		if ((this.actionListener != null)
				&& this.actionListener.contains(actionListener)) {
			this.actionListener.remove(e);
		}
	}

	private void processClickEvent(boolean on) {
		if (on) {
			if (!screen.isTouchClick()) {
				return;
			}
		} else {
			if (!screen.isTouchClickUp()) {
				return;
			}
		}
		ArrayList<CollisionEvent> actionEventList = copyAction();
		if (actionEventList == null) {
			return;
		}
		for (final CollisionEvent actionEvent : actionEventList) {
			Collidable collidable = actionEvent.getCollidable();
			if (collidable == null) {
				continue;
			}
			Shape shape = collidable.getShape();
			if (shape != null
					&& shape.contains(screen.getTouchX(), screen.getTouchY())) {
				listener.onClick(actionEvent);
			}
		}
	}

	public void processOnClickEvent() {
		processClickEvent(true);
	}

	public void processUpClickEvent() {
		processClickEvent(false);
	}

	public void processCollidesWithEvent(Collidable o) {
		if (o == null) {
			return;
		}
		ArrayList<CollisionEvent> actionEventList = copyAction();
		if (actionEventList == null) {
			return;
		}
		for (final CollisionEvent actionEvent : actionEventList) {
			Collidable collidable = actionEvent.getCollidable();
			if (collidable == null) {
				continue;
			}
			Shape shape = collidable.getShape();
			if (shape != null && shape.intersects(o.getShape())) {
				listener.onCollidesWith(actionEvent);
			}
		}
	}

	private final ArrayList<CollisionEvent> copyAction() {
		ArrayList<CollisionEvent> actionEventList;
		synchronized (this) {
			if (this.actionListener == null) {
				return null;
			}
			actionEventList = new ArrayList<CollisionEvent>(this.actionListener);
		}
		return actionEventList;
	}
}
