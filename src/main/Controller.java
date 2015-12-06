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
	private static final String DIRECTORY_NAME = "KNA Dictionary";
	private static final String FILE_NAME_KEY = "kna_";
	private static final String EXTENSION = ".txt";
	
	private Controller() {};
	
	public static Controller instance() {
		if(_controller == null) {
			_controller = new Controller();
		}
		
		return _controller;
	}
	
	public void createDictionaryWord(String word, String translation) throws IOException, FailedToCreateDictionaryDirectoryExceptions {
		// FIXME create alert dialog
		if(word == null || translation == null) return;
		
		String filePathName = provideSubDictionaryPath(word.toLowerCase().charAt(0));
		
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
	 * @throws FailedToCreateDictionaryDirectoryExceptions 
	 * @throws IOException 
	 */
	private String provideSubDictionaryPath(char firstLetter) throws FailedToCreateDictionaryDirectoryExceptions, IOException {
		ensureSubDictionaryPathExists(firstLetter);
		return createSubDictionaryPathFromFirsLetterOfWord(firstLetter);
	}
	
	/**
	 * If the file does not exist - it is created.
	 * @throws FailedToCreateDictionaryDirectoryExceptions 
	 * @throws IOException 
	 */
	public void ensureSubDictionaryPathExists(char firstLetter) throws FailedToCreateDictionaryDirectoryExceptions, IOException {
		ensureDirectoryExists();
		File path = new File(createSubDictionaryPathFromFirsLetterOfWord(firstLetter));
		if(!path.exists()) {
			path.createNewFile();
		}
	}
	
	private String createSubDictionaryPathFromFirsLetterOfWord(char firstLetter) throws FailedToCreateDictionaryDirectoryExceptions {
		return provideDictionaryDirectoryPath() + "\\" + FILE_NAME_KEY + (int)firstLetter + EXTENSION;
	}
	
	private void ensureDirectoryExists() throws FailedToCreateDictionaryDirectoryExceptions {
		File directory = new File(provideDictionaryDirectoryPath());
		if(!directory.exists()) {
			if(!directory.mkdirs()) {
				throw new FailedToCreateDictionaryDirectoryExceptions();
			}
		}
	}
	
	public String provideDictionaryDirectoryPath() {
		String directory = provideUserDesktopPath() + "\\" + DIRECTORY_NAME;
		return directory;
	}
	
	private String provideUserDesktopPath() {
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
	
	public String search(String word) throws FailedToCreateDictionaryDirectoryExceptions, IOException {
		String filePathName = provideSubDictionaryPath(word.toLowerCase().charAt(0));
		
		// Get the subdictionary where the word has to be saved.
		SubDictionary sd = new SubDictionary();
		sd.parseAll(readDictionaryFromFile(filePathName));
		
		// Cache all the words from the dictionary into the Global dictionary.
		cacheWords(sd);		
		if(_dictionatyHashtable.containsKey(word)) {
			return _dictionatyHashtable.get(word);
		}
		
		return null;
	}
	
	public static class FailedToCreateDictionaryDirectoryExceptions extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FailedToCreateDictionaryDirectoryExceptions() {
			super();
		}
		
		public FailedToCreateDictionaryDirectoryExceptions(String message) {
			super(message);
		}
	}
}
