package main;

import java.io.IOException;
import java.util.Scanner;

import main.Controller.FailedToCreateDictionaryDirectoryExceptions;
/**
 * Created by Alex on 11/27/2015.
 */
public class Test {
	
	public static void main(String[] args) {
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			System.out.println("Please, input a word and translation in the format \"<cmd>,<word>,<translation>\"");
			while(sc.hasNext()) {
				System.out.println("Please, input a word and translation in the format \"<cmd>,<word>,<translation>\"");
				String str = sc.nextLine();
				String[] input = str.split(",");
				if(input[0].equals("p")) {
					try {
						Controller.instance().createDictionaryWord(input[1], input[2]);
					} catch (FailedToCreateDictionaryDirectoryExceptions e) {
						e.printStackTrace();
					}
				} else if(input[0].equals("s")) {
					try {
						System.out.println(Controller.instance().search(input[1]));
					} catch (FailedToCreateDictionaryDirectoryExceptions e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(sc != null)
				sc.close();
		}
	}
}
