package com.renzh.earthtest;
/**
 * IReleaseable interface which defines the
 * release function for the object which need
 * releasing resources after it finishes it's
 * responsibility
 */
public interface IReleaseable {
	public boolean release();
}
