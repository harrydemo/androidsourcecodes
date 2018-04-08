package com.crackedcarrot.textures;

public class TextureData {
	public final int mTextureName;
	public final int nFrames;
	public final int texIndex;

	public TextureData(int index, int texName, int frames) {
		this.texIndex = index;
		this.mTextureName = texName;
		this.nFrames = frames;
	}
}