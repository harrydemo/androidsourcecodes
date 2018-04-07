package com.kerence.mine.exceptions;

/**
 * 这个异常用来表示LED数字类出现的异常
 * 
 * @author Administrator
 * 
 */
public class LEDException extends RuntimeException
{
	public LEDException(String str)
	{
		super(str);
	}
}
