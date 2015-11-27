package main;

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
            JsonArray datasets = jDict.getAsJsonArray(Dictionary.DICTIONARY_LIST_JSON_KEY);
            for (int i = 0; i < datasets.size(); i++) {
                JsonObject jDataset = datasets.get(i).getAsJsonObject();
                System.out.println(jDataset.get(DictionaryWord.WORD_JSON_KEY).getAsString() + ":" + jDataset.get(DictionaryWord.TRANSLATION_JSON_KEY).getAsString());
            }
        }
	}
}
