package main;

public class DictionaryWord {
	public static final String WORD_JSON_KEY = "word";
	public static final String TRANSLATION_JSON_KEY = "translation";
	
	public String word;
	public String translation;

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