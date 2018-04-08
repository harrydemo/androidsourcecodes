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

import java.util.zip.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class GeoIP {
	Vector<TableEntry> list;

	// CONSTRUCTORZ
	public GeoIP(String pathToZip) 
		throws IOException
	{
		readFromFile(pathToZip);
	}

	public static void main(String[] argv) {
		// used for testing this class (stand-alone)
		try{
			GeoIP gi = new GeoIP(argv[0]);
			if (argv.length>1) 
				System.out.println(argv[1]+" is in "+gi.getCountryLong(argv[1]));
		}
		catch(Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	// *** init
	void readFromFile(String filename) 
		throws IOException
	{
		list = new Vector<TableEntry>(10000,1000);
		try{
			ZipFile file = new ZipFile(filename);
			Enumeration<? extends ZipEntry> e = file.entries();
			while(e.hasMoreElements()) {
				ZipEntry ze = (ZipEntry)e.nextElement();
				if (ze.isDirectory()) continue;
				try{ parseEntry( file.getInputStream(ze)); }
				catch(Exception e3) { /* ignore exceptions */ }
			}
			file.close();
		}
		catch(Exception ex) {
			// could not read from REAL FILE, try fetching file from inside this jar
			try{
				ZipInputStream zipIn = new ZipInputStream(ClassLoader.getSystemResourceAsStream(filename));
				ZipEntry zEntry = zipIn.getNextEntry();
				while(zEntry != null) {
					if (zEntry.isDirectory()) continue;
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					while(zipIn.available()!=0) {
						byte[] smallBuffer = new byte[1024];
						int len = zipIn.read(smallBuffer,0,smallBuffer.length);
						if (len>0) out.write(smallBuffer,0,len);
					}
					byte[] data = out.toByteArray();
					try{ parseEntry(new ByteArrayInputStream(data)); }
					catch(Exception e3) { /* ignore exceptions */ }
					zEntry = zipIn.getNextEntry();
				}
			}
			catch(Exception ex2) { /*bla*/ }
		}
	}

	private void parseEntry(InputStream compIn) 
		throws IOException
	{
		BufferedReader bin = new BufferedReader( new InputStreamReader( compIn ) );
		String line = bin.readLine().trim();
		while( line.length() > 0) {
			line = bin.readLine().trim();
			try{
				String[] parts = line.split(",");
				insertSorted(new TableEntry(parts[2],parts[3],parts[4],parts[5]));
			}
			catch(Exception e2) { /* ignore exceptions */ }
		}
	}

	// **** main functionality

	// (yes I know that the list provided by maxmind.com IS sorted, but better
	// safe then sorry!)
	private void insertSorted(TableEntry fresh) {
		// list is filled
		int first = 0;
		int last = list.size();
		// empty list
		if (last==0) {
			list.add(fresh);
			return;
		}
		// check if entry is sorted after the last element (if input is sorted!)
		TableEntry here = (TableEntry) list.elementAt(last-1);
		if (fresh.from > here.from) {
			list.add(fresh);
			return;
		}
		// iterate
		//System.out.println("");
		while (last>first) {
			int mid = (last+first)/2;
			//System.out.println(first+" "+mid+" "+last);
			here = (TableEntry) list.elementAt(mid);
			if (fresh.from < here.from) {
				last = mid-1;
			} else {
				first = mid+1;
			}
		}
		//System.out.println("insert at "+first);
		// insert
		list.insertElementAt(fresh,first);
	}

	private TableEntry search(long ip) {
		// use binary search to find record
		//System.out.println("searching for "+ip);
		int first = 0;
		int last = list.size()-1;
		while (last>first) {
			int mid = (last+first)/2;
			TableEntry here = (TableEntry) list.elementAt(mid);
			//System.out.println(first+" "+mid+" ("+here.from+") "+last);
			if (ip < here.from) {
				last = mid-1;
			} else {
				if (first == mid) {
					first=mid+1;
				} else {
					first=mid;
				}
			}
		}
		//System.out.println("found at "+first);
		return (TableEntry) list.elementAt(first);
	}

	// for unit32-notation
	public String getCountryShort(long ip) {  return search(ip).country_short; }
	public String getCountryLong(long ip) {   return search(ip).country_long;  }
	// for char-array notation (convert to unit32 and call those functions)
	private long CharArrayToUint32(char[] ip) {
		long result = 0;
		for(int i=0;i<ip.length;++i) result = (result << 8) + ip[i];
		return result;
	}
	public String getCountryShort(char[] ip) { return getCountryShort(CharArrayToUint32(ip)); }
	public String getCountryLong(char[] ip) {  return getCountryLong(CharArrayToUint32(ip));  }
	// for byte arrays
	private long ByteArrayToUint32(byte[] ip) {
		long result = 0;
		for(int i=0;i<ip.length;++i) result = (result << 8) + ((int)(256+ip[i]) & 0xff);
		return result;
	}
	public String getCountryShort(byte[] ip) { return getCountryShort(ByteArrayToUint32(ip)); }
	public String getCountryLong(byte[] ip) {  return getCountryLong(ByteArrayToUint32(ip));  }
	// for dotted-quad notation (convert to uint32 and call those functions)
	private char[] StringToCharArray(String ip) {
		String[] a = ip.split("\\.");
		char[] x = new char[a.length];
		for(int i=0;i<a.length;++i) 
			x[i] = (char)Integer.parseInt(a[i]);
		return x;
	}
	public String getCountryShort(String ip) { return getCountryShort(CharArrayToUint32(StringToCharArray(ip))); }
	public String getCountryLong(String ip) {  return getCountryLong(CharArrayToUint32(StringToCharArray(ip)));  }
	// for InetAdress
	public String getCountryShort(InetAddress ip) { return getCountryShort(ip.getAddress()); }
	public String getCountryLong(InetAddress ip) { return getCountryLong(ip.getAddress()); }
	
	// ***** struct to store data in list
	private class TableEntry {
		long from;
		long to;
		String country_short;
		String country_long;

		TableEntry(String from,String to,String c_short,String c_long) 
			throws NumberFormatException
		{
			this.from = Long.parseLong(from.replace('"',' ').trim());
			this.to   = Long.parseLong(to.replace('"',' ').trim());
			country_short = c_short.replace('"',' ').trim();
			country_long  = c_long.replace('"',' ').trim();
		}
	}
}
