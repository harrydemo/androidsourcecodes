/* 
 	Copyright 2010 Cesar Valiente Gordo
 
 	This file is part of QuiteSleep.

    QuiteSleep is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    QuiteSleep is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with QuiteSleep.  If not, see <http://www.gnu.org/licenses/>.
*/

package es.cesar.quitesleep.ddbb;


import android.util.Log;

import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;

import es.cesar.quitesleep.interfaces.IDDBB;
import es.cesar.quitesleep.utils.ExceptionUtils;
import es.cesar.quitesleep.utils.QSLog;

/**
 * 
 * @author Cesar Valiente Gordo
 * @mail cesar.valiente@gmail.com
 *
 */
public class ClientDDBB implements IDDBB{		
	
	final String CLASS_NAME = getClass().getName();
	
	protected ObjectContainer clientDDBB;
	
	
	protected Selects selects;
	protected Inserts inserts;
	protected Updates updates;
	protected Deletes deletes;
	
	
	/**
	 * This function return the ObjectContainer ised like as clientDDBB for
	 * the application.
	 * 
	 * @return		The ObjectContainer used like as clientDDBB
	 * @see			ObjectContainer
	 */
	public ObjectContainer getObjectContainer () {
		return clientDDBB;
	}
	
	
			
	/**
	 * Otro cliente para conectarse al servidor de forma embebida, pero
	 * a diferencia del anterior, usamos una clase estática que lo lanza.
	 */	
	public ClientDDBB () {
		
		try
		{												
			synchronized (SEMAPHORE) {
							
				clientDDBB = ServerDDBB.getServer().openClient();								
				
				
				selects = new Selects(clientDDBB);
				inserts = new Inserts(clientDDBB);
				updates = new Updates(clientDDBB);
				deletes = new Deletes(clientDDBB);
				
			}						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}
	
	/**
	 * 
	 * @param cliente
	 */
	public ClientDDBB (ObjectContainer clientDDBB) {

		try {
			synchronized (SEMAPHORE) {
									
				
				selects = new Selects(clientDDBB);
				inserts = new Inserts(clientDDBB);
				updates = new Updates(clientDDBB);
				deletes = new Deletes(clientDDBB);
				
			}
			
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}

	/**
	 * Constructor al que le pasamos un servidor para que lo utilice en vez
	 * de utilizar el que pone en memoria la clase ServerBBDDEmbebido.
	 * 
	 * Este constructor esta creado para ser utilizado en la Migración de BBDD.
	 * 
	 * @param server
	 */
	public ClientDDBB (ObjectServer serverDDBB) {
		
		try
		{												
			synchronized (SEMAPHORE) {
			
				clientDDBB = serverDDBB.openClient();
			
				
				selects = new Selects(clientDDBB);
				inserts = new Inserts(clientDDBB);
				updates = new Updates(clientDDBB);
				deletes = new Deletes(clientDDBB);
				
			}						
		}catch (Exception e) {
			if (QSLog.DEBUG_E)QSLog.e(CLASS_NAME, ExceptionUtils.exceptionTraceToString(
					e.toString(),
					e.getStackTrace()));
		}
	}
	
	
	/**
	 * Function that close the ClientDDBB if it's opened previously
	 */
	public void close () {
		if (!isClosed())
			// close the ObjectContainer 
			clientDDBB.close();
	}
	
	
	/**
	 * Function that said if the client is closed
	 * @return		true or false if the clientDDBB is closed or not
	 * @see			boolean
	 */
	public boolean isClosed () {
		if (clientDDBB != null)
			return clientDDBB.ext().isClosed();
		else
			return true;
	}
	
	public void commit () {
		clientDDBB.commit();
	}


	public ObjectContainer getClientDDBB() {
		return clientDDBB;
	}
	public void setClientDDBB(ObjectContainer clientDDBB) {
		this.clientDDBB = clientDDBB;
	}

	public Selects getSelects() {
		return selects;
	}
	public void setSelects(Selects selects) {
		this.selects = selects;
	}

	public Inserts getInserts() {
		return inserts;
	}
	public void setInserts(Inserts inserts) {
		this.inserts = inserts;
	}
	
	public Updates getUpdates() {
		return updates;
	}
	public void setUpdates(Updates updates) {
		this.updates = updates;
	}

	public Deletes getDeletes() {
		return deletes;
	}
	public void setDeletes(Deletes deletes) {
		this.deletes = deletes;
	}						
	
	

}
