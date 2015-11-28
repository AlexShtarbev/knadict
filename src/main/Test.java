package main;

import java.io.IOException;
import java.util.Scanner;
/**
 * Created by Alex on 11/27/2015.
 */
public class Test {
	
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			System.out.println("Please, input a word and translation in the format \"<word>,<translation>\"");
			while(sc.hasNext()) {
				System.out.println("Please, input a word and translation in the format \"<word>,<translation>\"");
				String str = sc.nextLine();
				String[] wordAndTranslation = str.split(",");
				Controller.instance().createDictionaryWord(wordAndTranslation[0], wordAndTranslation[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(sc != null)
				sc.close();
		}
	}
}
