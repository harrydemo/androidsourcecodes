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
 * FIXME: insert at least a single line of documentation here!
 */
class IntroductionPoint {

	boolean loaded = false;
	private Server srv = null;
	private String nickname = null;
	private Directory dir = null;
	private String identityDigest = null;

	/***
	 * Construct an introduction point
	 * 
	 * @param s
	 *            the identifier of this introduction point. May be either an
	 *            identity digest (e.g. "$1a2b3c...") or a standard nickname
	 *            (e.g. "mytorrouter")
	 * @param dir
	 *            the directory object
	 */
	IntroductionPoint(String s, Directory dir) {

		if (s.startsWith("$")) {
			nickname = dir.getNicknameByIdentityDigest(s);
			identityDigest = s;
		} else {
			nickname = s;
			identityDigest = "$" + dir.getIdentityDigestByNickname(s);
		}

		this.dir = dir;
	}

	/***
	 * Get the standard nickname (e.g. "mytorrouter") for this introduction
	 * point
	 * 
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/***
	 * Get the server object corresponding to this introduction point. This may
	 * involve getting the server descriptor from a directory so this may take
	 * some time.
	 * 
	 * @return the corresponding server object
	 */
	public Server getSrv() {
		if (!loaded) {
			// find it in the directory
			srv = dir.getByName(nickname);
			loaded = true;
		}
		return srv;
	}
	
    /**
     * Get the identity digest of this introduction point (e.g. "a1b2c3...")
     * @return identity digest with leading $
     */
    public String getIdentityDigest() {
		return identityDigest;
	}
    
	// TODO constructor for into points as in 0.1.1.6-alpha
}
