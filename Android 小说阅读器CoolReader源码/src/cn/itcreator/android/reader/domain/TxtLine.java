/**
 * <Txt line for content in text view.>
 *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package cn.itcreator.android.reader.domain;

/**
 * Txt line for content in text view
 * @author Wang XinFeng
 * @version 1.0
 *
 */
public class TxtLine {
	
	/**当前行最后一个字符在文件中的偏移量*/
    public int offset = 0;
    
    /**当前行的长度*/
    public int lineLength = 0;
    
    /**the data length before this line and contain current line*/
    public int beforeLineLength=0;
    public TxtLine() {
        this(0, 0,0);
    }

    public TxtLine(int offset, int lenght,int beforeLineLength) {
        this.offset = offset;
        this.lineLength = lenght;
        this.beforeLineLength=beforeLineLength;
    }

}
