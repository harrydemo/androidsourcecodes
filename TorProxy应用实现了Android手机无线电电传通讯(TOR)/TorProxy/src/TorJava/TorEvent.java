/**
 * OnionCoffee - Anonymous Communication through TOR Network
 * Copyright (C) 2005-2007 RWTH Aachen University, Informatik IV
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package TorJava;

/**
 *  used
 */
public class TorEvent {
	private String description;
	private int type;
	private Object cause;

	public final static int GENERAL = 0;
	public final static int CIRCUIT_BUILD = 10;  
	public final static int CIRCUIT_CLOSED = 11;
	public final static int STREAM_BUILD = 20;
	public final static int STREAM_CLOSED = 21;
		
	TorEvent(int type,Object o,String description) {
		this.description = description;
		this.cause = o;
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public int getType() {
		return this.type;
	}

	public Object getObject() {
		return this.cause;
	}
}
