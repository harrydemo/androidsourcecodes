package org.loon.framework.javase.game.core.task;

import java.util.Vector;

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
// PS:这是一个0.3.1版中新增的任务执行器，用于更加有效的进行线程管理
public final class TaskExecuter extends Process {

	private final TaskList list = new TaskList();

	private final Interrupt barrier = new Interrupt();

	final class TaskList extends Task {

		private boolean tasksManaged = false;

		private final Vector<Task> tasksToAdd = new Vector<Task>();

		private final Vector<Task> tasksToRemove = new Vector<Task>();

		private final Vector<Task> tasks = new Vector<Task>();

		public final int size() {
			return tasks.size() + tasksToAdd.size();
		}

		public final void add(final Task task) {
			tasksToAdd.add(task);
		}

		public final Task get(final int index) {
			try {
				return tasks.get(index);
			} catch (final ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}

		public final Task remove(final int index) {
			try {
				final Task t = tasks.get(index);
				tasksToRemove.add(t);
				return t;
			} catch (final ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}

		public final void remove(final Task task) {
			tasksToRemove.add(task);
		}

		public final void clear() {
			tasksToRemove.addAll(tasks);
			tasksToRemove.addAll(tasksToAdd);
		}

		protected void run() {
			tasksManaged = false;
			for (int i = 0; i < tasks.size(); i++) {
				if (!tasks.get(i).moveOn()) {
					tasks.get(i).enter();
				}
			}
		}

		final void prepare() {
			super.prepare();
			if (!tasksManaged()) {
				manageTasks();
			}

			for (int i = 0; i < tasks.size(); i++) {
				if (!tasks.get(i).prepared()) {
					tasks.get(i).prepare();
				}
			}
		}

		private final synchronized boolean tasksManaged() {
			if (tasksManaged) {
				return true;
			} else {
				tasksManaged = true;
				return false;
			}
		}

		private final void manageTasks() {
			for (int i = tasksToRemove.size() - 1; i > -1; i--) {
				final Task t = tasksToRemove.remove(i);
				t.drop();
				if (!tasks.remove(t)) {
					tasksToAdd.remove(t);
				}
			}
			while (!tasksToAdd.isEmpty()) {
				final Task t = tasksToAdd.remove(0);
				t.init(taskProcessor);
				tasks.add(t);
			}
		}
	}

	public TaskExecuter(final int threads) {
		this("TaskExecuter", threads);
	}

	public TaskExecuter() {
		this(1);
	}
	
	public TaskExecuter(final String name, final int threads) {
		super(name, threads);
		list.init(this);
		barrier.init(this);
	}

	public final void addTask(final Task task) {
		list.add(task);
	}

	public final void removeTask(final Task task) {
		list.remove(task);
	}

	public final Task get(final int index) {
		return list.get(index);
	}

	public final Task remove(final int index) {
		return list.remove(index);
	}

	protected final void update() {
		synchronized (this) {
			if (!list.prepared()) {
				list.prepare();
			}
		}
		list.enter();
	}
}
