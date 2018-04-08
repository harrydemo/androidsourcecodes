package com.weibo.android.logic;

import java.util.HashMap;

import android.content.Context;

public class Task {
   
	
	public static final int LOGIN=1;
	public static final int MYCONTACT=5;
	public static final int MSGACTIVITY=4;
	public static final int NEWWEIBO=7;
	public static final int HOMEREFRESH=3;
	public static final int VIEWWEIBO=8;
	
	
	private int taskId;
	private HashMap taskParams;
	private Context ctx;
	
	public Task(int taskId, HashMap taskParams,Context ctx) {
		super();
		this.taskId = taskId;
		this.taskParams = taskParams;
		this.ctx = ctx;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public HashMap getTaskParams() {
		return taskParams;
	}
	public void setTaskParams(HashMap taskParams) {
		this.taskParams = taskParams;
	}
	public Context getCtx() {
		return ctx;
	}
	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	
	
}
