package main;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by Alex on 11/27/2015.
 */
public class Test {
	public static class Dictionary {
		private List<DictionaryWord> dictionaryList;
		private static Dictionary _dictionary;
		
		private Dictionary() {
			dictionaryList = new ArrayList<DictionaryWord>();
		}
		
		public static Dictionary instance() {
			if(_dictionary == null) {
				_dictionary = new Dictionary();
			}
			
			return _dictionary;
		}
		
		public void addWord(DictionaryWord dw) throws IllegalArgumentException {
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
	}
	
	public static class DictionaryWord {
		private String word;
		private String translation;
		
		public DictionaryWord(String word, String translation) {
			this.word = word;
			this.translation = translation;
		}
		
		public void setWord(String word) {
			this.word = word;
		}
		
		public String getWord() {
			return word;
		}
		
		public void setTranslation(String translation) {
			 this.translation = translation;
		}
		
		public String getTranslation() {
			return translation;
		}
	}
	
	public static void main(String[] args) {
		Dictionary dict = Dictionary.instance();
		
		dict.addWord(new DictionaryWord("книга", "book"));
		dict.addWord(new DictionaryWord("обяд", "dinner"));
		dict.addWord(new DictionaryWord("речник", "dictionary"));
		
		// ----- From Object to JSON
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		Gson gson = builder.create();
		String json = gson.toJson(dict);
		System.out.println(json);
		
		// ----- From String to JSON
		JsonParser parser = new JsonParser();
        // The JsonElement is the root node. It can be an object, array, null or
        // java primitive.
        JsonElement element = parser.parse(json);
        // use the isxxx methods to find out the type of jsonelement. In our
        // example we know that the root object is the Albums object and
        // contains an array of dataset objects
        if (element.isJsonObject()) {
            JsonObject jDict = element.getAsJsonObject();
            JsonArray datasets = jDict.getAsJsonArray("dictionaryList");
            for (int i = 0; i < datasets.size(); i++) {
                JsonObject jDataset = datasets.get(i).getAsJsonObject();
                System.out.println(jDataset.get("word").getAsString() + ":" + jDataset.get("translation").getAsString());
            }
        }
	}
}
