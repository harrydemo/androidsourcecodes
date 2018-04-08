package org.loon.framework.android.game.core.task;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public final class Interrupt extends Task {

	private CyclicBarrier barrier;

	boolean init(TaskExecuter process) {
		if (super.init(process)) {
			barrier = new CyclicBarrier(process.getThreadCount());
			return true;
		}
		return false;
	}

	void drop() {
		barrier = null;
	}

	protected void run() {
		try {
			barrier.await();
		} catch (InterruptedException ex) {
			Logger.getLogger(Interrupt.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (BrokenBarrierException ex) {
			Logger.getLogger(Interrupt.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
