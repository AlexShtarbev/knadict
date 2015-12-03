package main;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class Controller {
	private Hashtable<String, String> _dictionatyHashtable = new Hashtable<>();
	private static Controller _controller;
	
	private Controller() {};
	
	public static Controller instance() {
		if(_controller == null) {
			_controller = new Controller();
		}
		
		return _controller;
	}
	
	public void createDictionaryWord(String word, String translation) throws IOException {
		// FIXME create alert dialog
		if(word == null || translation == null) return;
		
		String filePathName = provideSubDictionaryPath(word.charAt(0));
		
		// Get the subdictionary where the word has to be saved.
		SubDictionary sd = new SubDictionary();
		sd.parseAll(readDictionaryFromFile(filePathName));
		
		// Cache all the words from the dictionary into the Global dictionary.
		cacheWords(sd);		
		
		if(_dictionatyHashtable != null) {
			if(_dictionatyHashtable.containsKey(Utils.getStringInUTF8(word))) {
				// FIXME create alert dialog
				System.out.println("Word already exists");
			} else {
				DictionaryWord dw = new DictionaryWord(Utils.getStringInUTF8(word), Utils.getStringInUTF8(translation));				
				sd.addWord(dw);
				sd.saveToFile(filePathName);
			}
		}
	}
	
	/**
	 * Provides the path of the file where the subdictionary is saved as a JSON string.
	 * @param firstLetter
	 * @return
	 */
	private String provideSubDictionaryPath(char firstLetter) {
		ensureValidPath();
		// TODO - Kolyo - create a string of the full file path and return it (you can delete the current return statement below).
		return "D:\\TU_3ti_Kurs\\PE\\Kursova\\kna_dictionary\\test_files\\test_dict.txt";
	}
	
	/**
	 * If the file does not exist - it is created.
	 */
	public void ensureValidPath() {
		// TODO - Kolyo
	}
	
	public String provideUserDesktopPath() {
		File desktop = new File(System.getProperty("user.home"), "Desktop");
		return desktop.getAbsolutePath();
	}

	private String readDictionaryFromFile(String filePathName) throws FileNotFoundException, IOException {
		DataInputStream in = null;
		String json = null;
		try {
			in = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(filePathName))));
			json = in.readUTF();
		} catch(EOFException e) {
			// file is empty - there is nothing to cache
		} finally {
			if(in != null) {
				in.close();
			}
		}
		
		return json;
	}

	private void cacheWords(SubDictionary subDict) throws UnsupportedEncodingException {
		if(subDict == null) return;
		for(DictionaryWord dw : subDict) {
			_dictionatyHashtable.put(dw.getWord(), dw.getTranslation());
		}
	}
}
