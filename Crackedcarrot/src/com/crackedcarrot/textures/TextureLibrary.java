package com.crackedcarrot.textures;

import java.util.HashMap;
import java.util.Iterator;

public class TextureLibrary {
	private HashMap<Integer, Integer> frameData;

	// More metadata containers here

	public TextureLibrary() {
		frameData = new HashMap<Integer, Integer>();
	}

	public void setFrameData(Integer resourceId, Integer frames) {
		frameData.put(resourceId, frames);
	}

	public Integer getFrameData(Integer resourceId) {
		return frameData.get(resourceId);
	}

	public Iterator<Integer> textureResourceIdIterator() {
		return frameData.keySet().iterator();
	}

	public int size() {
		return frameData.size();
	}
}
