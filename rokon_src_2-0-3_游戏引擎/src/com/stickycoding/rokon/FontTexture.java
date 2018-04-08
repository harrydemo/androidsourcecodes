package com.stickycoding.rokon;

/**
 * TextTexture.java
 * A texture specifically tailored for bitmap fonts
 * 
 * @author Richard
 */
public class FontTexture extends Texture {
	
	protected char[] chars;

	/**
	 * Creates a FontTexture from an asset, the character string must be passed
	 *  
	 * @param filename path to a valid image file in assets
	 * @param characters the string of characters
	 * @param columns number of columns
	 * @param rows number of rows
	 */
	public FontTexture(String filename, String characters, int columns, int rows) {
		super(filename, columns, rows);
		chars = new char[characters.length()];
		for(int i = 0; i < characters.length(); i++) {
			chars[i] = characters.charAt(i);
		}
	}
	
	/**
	 * Creates a FontTexture from an asset, assuming there is only 1 row
	 * @param filename path to a valid image file in assets
	 * @param characters the string of characters
	 */
	public FontTexture(String filename, String characters) {
		this(filename, characters, characters.length(), 1);
	}
	
	/**
	 * Returns the column position of a given character
	 * 
	 * @param ch character to find
	 * @return column index, -1 if not found
	 */
	public int getCol(char ch) {
		return charPos(ch) % columns;
	}
	
	/**
	 * Returns the row position of a given character
	 * 
	 * @param ch character to find
	 * @return row index, -1 if not found
	 */
	public int getRow(char ch) {
		int charPos = charPos(ch);
		return charPos - (charPos % columns) / columns;
	}
	
	/**
	 * Returns the index of a given character
	 * 
	 * @param ch character to find
	 * @return tile index, -1 if not found
	 */
	public int charPos(char ch) {
		for(int i = 0; i < chars.length; i++) {
			if(chars[i] == ch) {
				return i;
			}
		}
		Debug.error("Character not found! " + ch);
		return -1;
	}

}
