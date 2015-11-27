package main;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
	public static final String DICTIONARY_LIST_JSON_KEY = "dictionaryList";
	
	public List<DictionaryWord> dictionaryList;
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