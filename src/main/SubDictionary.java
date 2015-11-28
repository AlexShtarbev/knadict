package main;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SubDictionary {
	public static final String DICTIONARY_LIST_JSON_KEY = "dictionaryList";
	
	public List<DictionaryWord> dictionaryList;
	
	public SubDictionary() {
		dictionaryList = new ArrayList<DictionaryWord>();
	}
	
	public void addWord(DictionaryWord dw) throws IllegalArgumentException {
		// FIXME create alert dialog
		assertValidWord(dw);
		dictionaryList.add(dw);
	}
	
	public void assertValidWord(DictionaryWord dw) throws IllegalArgumentException{
		if(dw.getWord() == null || dw.getWord().trim().isEmpty())
			throw new IllegalArgumentException("No meaningful word was entered.");
		
		if(dw.getTranslation() == null || dw.getTranslation().trim().isEmpty())
			throw new IllegalArgumentException("No translation was entered for the word.");
	}
	
	public List<DictionaryWord> getDictionary() {
		return dictionaryList;
	}
	
	/**
	 * Converts the entire sub dictionary to a JSON formatted string.
	 * @return sub directory in JSON string.
	 */
	public String toJSON() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		
		String json = gson.toJson(this);
		
		return json;
	}
	
	/**
	 * Save the current dictionary to its appropriate file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveDictionaryToFile(String filePathName) throws FileNotFoundException, IOException {
		DataOutputStream out = null;
		try{
		out = new DataOutputStream(new BufferedOutputStream(
		          new FileOutputStream(new File(filePathName))));
		
		out.writeUTF(toJSON());
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * Replaces the the current contents of the dictionary with the ones in the JSON string.
	 * @param json
	 * @throws UnsupportedEncodingException
	 */
	public void parseAll(String json) throws UnsupportedEncodingException {
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
		        
		        addWord(new DictionaryWord(word, translation));
		    }
		}
	}
}