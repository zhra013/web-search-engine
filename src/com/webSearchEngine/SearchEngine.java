package com.webSearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchEngine {
	static Scanner sc = new Scanner(System.in);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SearchEngine engine = new SearchEngine();
		String choose = "n";
		System.out.println("\n---------------------------------------------------\n");
		System.out.println("              Welcome to Web Search Engine             ");
		System.out.println("\n---------------------------------------------------\n");
		do {
			System.out.println(" select the option mentioned below");
			System.out.println("---------------------------------------------------");
			System.out.println(" 1) Enter 1 for the Web search from the URL you will pass");
			System.out.println(" 2) Enter 2 for the Web search from static URL (http://geeksforgeeks.org/)");
			System.out.println(" 3) Enter 3 for Exit ");
		
			int option = sc.nextInt();

			switch (option) {
			case 1:
				System.out.println("\n Please enter valid URL for example 'https://www.xyz.com/'");
				String url = sc.next();
				choose = engine.searchWord(url);
				break;

			case 2:
				choose = engine.searchWord("https://www.geeksforgeeks.org/");
				break;

			case 3:
				System.out.println("Exit...");
				choose = "n";
				break;

			default:
				System.out.println("Please enter correct option");
				choose = "y";

			}
		} while (choose.equals("y"));

		System.out.println("\n---------------------------------------------------\n");
		System.out.println("	:) THANK YOU FOR USING SEARCH ENGINE :)        ");
		System.out.println("\n---------------------------------------------------\n");
	}

	private String searchWord(String url) {
		
		if(!isValid(url)) {
			 System.out.println("The url " + url + " isn't valid");
			 System.out.println("Please try again\n");
			 return "y";
		}
		System.out.println("The url " + url + " is valid\n");
		System.out.println("Crawling Started...");
		Crawler.startCrawler(url, 0);
		System.out.println("Crawling Compelted...");

		// Hash table is used instead of Hash Map as it don't allow null value in insertion
		Hashtable<String, Integer> listOfFiles = new Hashtable<String, Integer>();
		listOfFiles.clear();
		
		String choice = "y";
		do {
			System.out.println("---------------------------------------------------");
			System.out.println("\n Enter the word to search ");
			String wordToSearch = sc.next();
			System.out.println("---------------------------------------------------");
			int frequency = 0;
			int noOfFiles = 0;
			
			try {
				System.out.println("\nSearching...");
				File files = new File(Path.txtDirectoryPath);

				File[] fileArray = files.listFiles();

				for (int i = 0; i < fileArray.length; i++) {

					In data = new In(fileArray[i].getAbsolutePath());

					String txt = data.readAll();
					Pattern p = Pattern.compile("::");
					String[] file_name = p.split(txt);
					frequency = SearchWord.wordSearch(txt, wordToSearch, file_name[0]);

					if (frequency != 0) {
						listOfFiles.put(file_name[0], frequency);
						noOfFiles++;
					}

				}

				if(noOfFiles>0) {
				System.out.println("\nTotal Number of Files containing word : " + wordToSearch + " is : " + noOfFiles);
				}else {
					System.out.println("\n File not found! containing word : "+ wordToSearch);
					SearchWord.suggestAltWord(wordToSearch);

				}

				SearchWord.rankFiles(listOfFiles, noOfFiles);

			} catch (Exception e) {
				System.out.println("Exception:" + e);
			}
			System.out.println("\n Do you want return to search another word(y/n)?");
			choice = sc.next();
		} while (choice.equals("y"));
		System.out.println("\n Do you want return to main menu(y/n)?");
		return sc.next();
	}
	
	public boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
        	System.out.println("Validating URL...");
        	URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            //Sending the request
            conn.setRequestMethod("GET");
            int response = conn.getResponseCode();
            if(response==200) {
            	 return true;
            }else {
            	return false;
            }
           
        }
          
        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

}
