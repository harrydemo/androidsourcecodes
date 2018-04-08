// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Danmaku.java

package com.sph.player;

import android.app.Application;

public class Danmaku extends Application
{

	public Danmaku()
	{
	}

	public void onCreate()
	{
		super.onCreate();
		String s = System.setProperty("java.net.preferIPv6Addresses", "false");
	}

	public void onTerminate()
	{
		super.onTerminate();
	}
}
