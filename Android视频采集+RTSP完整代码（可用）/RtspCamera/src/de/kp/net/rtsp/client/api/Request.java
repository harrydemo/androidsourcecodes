package de.kp.net.rtsp.client.api;
/**
 *	Copyright 2010 Voice Technology Ind. e Com. Ltda.
 *
 *	RTSPClientLib is free software: you can redistribute it and/or 
 *	modify it under the terms of the GNU Lesser General Public License 
 *	as published by the Free Software Foundation, either version 3 of 
 *	the License, or (at your option) any later version.
 *
 *	RTSPClientLib is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details. 
 *
 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this software. If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 *	This class has been adapted to the needs of the RtspCamera project
 *	@author Stefan Krusche (krusche@dr-kruscheundpartner.de)
 *
 */

import java.net.URISyntaxException;

import de.kp.net.rtsp.client.RtspClient;

public interface Request extends Message {
	
	enum Method {
		OPTIONS, DESCRIBE, SETUP, PLAY, PAUSE, RECORD, TEARDOWN
	};

	public void setLine(Method method, String uri) throws URISyntaxException;
	
	public Method getMethod();
	
	public String getURI();
	
	public void handleResponse(RtspClient client, Response response);
	
}
