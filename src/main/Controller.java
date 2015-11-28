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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
		
		String filePathName = getSubDictionaryPath(word.charAt(0));
		cacheWords(readDictionaryFromFile(filePathName));		
		
		if(_dictionatyHashtable != null) {
			if(_dictionatyHashtable.containsKey(Utils.getStringInUTF8(word))) {
				// FIXME create alert dialog
				System.out.println("Word already exists");
			} else {
				DictionaryWord dw = new DictionaryWord(Utils.getStringInUTF8(word), Utils.getStringInUTF8(translation));	
				SubDictionary sd = new SubDictionary();
				sd.parseAll(readDictionaryFromFile(filePathName));
				sd.addWord(dw);
				sd.saveDictionaryToFile(filePathName);
			}
		}
	}

	// TODO - Kolyo
	private String getSubDictionaryPath(char firstLetter) {
		return "D:\\TU_3ti_Kurs\\PE\\Kursova\\kna_dictionary\\test_files\\test_dict.txt";
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

	private void cacheWords(String json) throws UnsupportedEncodingException {
		if(json == null) return;
		
		JsonParser parser = new JsonParser();
		// The JsonElement is the root node. It can be an object, array, null or
		// java primitive.
		JsonElement element = parser.parse(json);
		
		if (element.isJsonObject()) {
		    JsonObject jDict = element.getAsJsonObject();
		    // get all sets of <word>:<translation> 
		    JsonArray datasets = jDict.getAsJsonArray(SubDictionary.DICTIONARY_LIST_JSON_KEY);
		    // traverse the sting of sets and cache each set into the hashtbale
		    for (int i = 0; i < datasets.size(); i++) {
		        JsonObject jDataset = datasets.get(i).getAsJsonObject();
		        
		        String word = Utils.getStringInUTF8(jDataset.get(DictionaryWord.WORD_JSON_KEY).getAsString());
		        String translation =  Utils.getStringInUTF8(jDataset.get(DictionaryWord.TRANSLATION_JSON_KEY).getAsString());
		        
		        _dictionatyHashtable.put(word, translation);
		    }
		}
	}
}
