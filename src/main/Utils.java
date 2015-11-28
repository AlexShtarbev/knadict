package main;

import java.io.UnsupportedEncodingException;

public class Utils {
	
	public static String getStringInUTF8(String word) throws UnsupportedEncodingException {
		String myString = word;
		byte bytes[] = myString.getBytes("UTF-8"); 
		String value = new String(bytes, "UTF-8");
		
		return value;
	}
}
