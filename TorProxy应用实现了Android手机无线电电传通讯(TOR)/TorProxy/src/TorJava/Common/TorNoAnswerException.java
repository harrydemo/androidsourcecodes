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
package TorJava.Common;

/**
 * this exception is used to handle timeout-related problems.
 * 
 * @author Michael Koellejan
 * @version unstable
 */

public class TorNoAnswerException extends TorException {
    // Exception is in principle a serializable class in tough needs a VersionUID
    private static final long serialVersionUID = 5311921437743700439L;

    //private int waitedFor = 0;

    TorNoAnswerException() {
        super();
    }

    TorNoAnswerException(String s, int waitedFor) {
        super(s);
        //this.waitedFor = waitedFor;
    }
}
