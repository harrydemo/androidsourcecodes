package com.sgf.game.tankwar;

import java.util.Stack;

public class GameFactory {
	public static Stack<Shells> shellsFactory=new Stack<Shells>();
	public static final int SHELLS_FACTORY_SIZE=10;
	
	public synchronized static Shells createShells(){
		if(shellsFactory.size()==0)
			return new Shells();
		//Log.i("System.out", "shells factory:"+shellsFactory.size());
		return shellsFactory.pop();
	}
}
