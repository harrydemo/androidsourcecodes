package org.loon.framework.javase.game.core.task;
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
public abstract class Process {

	private ThreadRun[] threads;

	private boolean isRunning = true;

	private ThreadGroup group;

	public Process(final String threadName, final int threadCount) {
		group = new ThreadGroup(threadName);
		threads = new ThreadRun[threadCount];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new ThreadRun(group);
		}
	}

	public Process(final int threadCount) {
		this("Task", threadCount);
	}

	public final int getThreadCount() {
		return threads.length;
	}

	public final void start() {
		isRunning = true;
		for (final ThreadRun t : threads) {
			t.start();
		}
	}

	public final void stop() {
		isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public final void join() {
		for (final ThreadRun t : threads) {
			try {
				t.join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	protected abstract void update();

	private class ThreadRun extends Thread {

		ThreadRun(final ThreadGroup group) {
			super(group, group.getName());
		}

		public void run() {
			while (isRunning) {
				update();
			}
		}
	}
}
