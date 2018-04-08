package org.loon.framework.android.game.core.task;

/**
 * 
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
public abstract class Task {

	private boolean prepared = false;
	
	boolean moveOn = false;

	TaskExecuter taskProcessor;

	public final void stop() {
		taskProcessor.stop();
	}

	final void setMoveOn() {
		this.moveOn = true;
		this.prepared = false;
	}

	synchronized boolean init(final TaskExecuter taskProcessor) {
		if (this.taskProcessor == null) {
			this.taskProcessor = taskProcessor;
			return true;
		}
		return false;
	}

	void drop() {
		taskProcessor = null;
	}

	boolean moveOn() {
		return moveOn;
	}

	final void enter() {
		run();
		setMoveOn();
	}

	void prepare() {
		moveOn = false;
		prepared = true;
	}

	final boolean prepared() {
		return prepared;
	}

	protected abstract void run();
}
