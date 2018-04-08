 package com.sordid.particly;

import android.util.Log;

public final class Slog
{
	private Slog() {}
	
	public static void i(String g, String m)
	{
		if(ParticlyActivity.DEBUG) {
			Log.i(g,m);
		}
	}
	
	public static void i(String g, int m)
	{
		i(g,""+m);
	}	
	
	public static void i(String g, float m)
	{
		i(g,""+m);
	}	
	
	public static void d(String g, String m)
	{
		if(ParticlyActivity.DEBUG) {
			Log.d(g,m);
		}
	}
	
	public static void d(String g, int m)
	{
		d(g,""+m);
	}	
}