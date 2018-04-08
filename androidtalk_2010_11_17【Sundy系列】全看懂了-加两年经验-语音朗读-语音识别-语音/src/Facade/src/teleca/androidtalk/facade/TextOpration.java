package teleca.androidtalk.facade;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/*
 * @author: daniel
 * this is a class which will be used to read any kinds of encode of .txt file to String
 * */

public final class TextOpration {
	// some encoding format variables of .txt file
	private final static String ENC_UTF16BE = "UTF-16BE";
	private final static String ENC_UTF16LE = "UTF-16LE";
	private final static String ENC_UTF8 = "UTF-8";
	private final static String ENC_GB2312 = "GB2312";
	private File _file;
	
	//some variables of file operation
	private String bookName;  // full path of file
	private long bookSize; // size of the book
    private InputStream fis;	
    private String bookFormat; // format of encoding
	
    /* @function: intial the vearibles;
     * @para: file naame with full path;
     * @return:a instance of TextOpration will be returned;
     * */
    
    public TextOpration(String fileName) {
		byte[] headOfBook = new byte[3];
		try {
	        _file = new File(fileName);
	        bookSize = _file.length();
	        fis = new FileInputStream(_file);	
			fis.read(headOfBook); //read first three bytes of the fileinputStream
		    fis.close();
		    fis = null;
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		// conform the bookFormat through first three bytes;
		if ((headOfBook[0] == -2) && (headOfBook[1] == -1)) {
			bookFormat =  ENC_UTF16BE;
		}
		else if ((headOfBook[0] == -1) && (headOfBook[1] == -2)) {
			bookFormat =  ENC_UTF16LE;
		}
		else if ((headOfBook[0] == -17) && (headOfBook[1] == -69) &&(headOfBook[2] == -65))
		{
			bookFormat = ENC_UTF8;  // UTF8 with BOM
		}
		else {
			bookFormat = ENC_GB2312;
		}
		
	}
	
    /* @function: get content from  the .txt file and switch to String  ;
     * @para: void;
     * @return:content of file  ;
     * */
    
    
    
	public String getStringFromFile()
	{
		try {
			StringBuffer sBuffer = new StringBuffer();
			fis = new FileInputStream(_file);
			InputStreamReader inputStreamReader = new InputStreamReader(fis, bookFormat);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if(!_file.exists())
			{
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			fis.close();
			fis = null;
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
    
      
    /* @function: get the size of file  ;
     * @para: void;
     * @return: size of file  ;
     * */
	
	public long getBookSize() {
		return bookSize;
	}
    
	/* @function: get the name of file  ;
     * @para: void;
     * @return: name of file  ;
     * */
	
    public String getBookName() {
		return bookName;
	}
    
    public String getFileEncoding(){
    	return bookFormat;
    }
    
}
