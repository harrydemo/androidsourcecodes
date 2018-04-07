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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * this class contains utility functions concerning parsing 
 */
public class Parsing {

    /**
     * convert a decoded fingerprint back into a stringc
     * 
     * @param fingerprint
     *            a bytearray containing the fingerprint data
     */
    public static String renderFingerprint(byte[] fingerprint, boolean withSpace) {
        String hex = "0123456789ABCDEF";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < fingerprint.length; ++i) {
            int x = fingerprint[i];
            if (x < 0)
                x = 256 + x; // why are there no unsigned bytes in java?
            sb.append(hex.substring(x >> 4, (x >> 4) + 1));
            sb.append(hex.substring(x % 16, (x % 16) + 1));
            if (((i + 1) % 2 == 0) && withSpace)
                sb.append(" ");
        }
        ;
        return sb.toString();
    }

    /**
     * parses a line by a regular expression and returns the first hit. If the
     * regular expression doesn't fit, it returns the default value
     * 
     * @param s
     *            the line to be parsed
     * @param regex
     *            the parsing regular expression
     * @param default_value
     *            the value to be returned, if teh regex doesn't apply
     * @return either the parsed result or the default_value
     */
    public static String parseStringByRE(String s, String regex, String default_value) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL + Pattern.MULTILINE
                + Pattern.CASE_INSENSITIVE + Pattern.UNIX_LINES);
        Matcher m = p.matcher(s);
        if (m.find())
            return m.group(1);
        return default_value;
    }

}
