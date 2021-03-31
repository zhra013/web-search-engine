package com.webSearchEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchWord {
	static ArrayList<String> key = new ArrayList<String>();
	static HashMap<String, Integer> numbers = new HashMap<String, Integer>();
	// Positions and Occurrences of the words
		public static int wordSearch(String data, String word, String fileName) {
			int counter = 0;

			int offset = 0;
			com.webSearchEngine.BoyerMoore boyerMoore = new com.webSearchEngine.BoyerMoore(word);

			for (int loc = 0; loc <= data.length(); loc += offset + word.length()) {
				offset = boyerMoore.search(word, data.substring(loc));
				if ((offset + loc) < data.length()) {
					counter++;
//					System.out.println( word + " at position " + (offset + loc)+"in txt file: "+txtFile ); // printing position of word in .txt file
				}
			}
			if (counter != 0) {
				System.out.println("Found in HTML file --> " + fileName+" --> "+counter+" times"); // Founded from which HTML file..
				System.out.println("-------------------------------------------------");
			}
			return counter;
		}

		// Merge-sort for ranking of the pages

		public static void rankFiles(Hashtable<?, Integer> files, int occur) {

			// Transfer as List and sort it
			ArrayList<Map.Entry<?, Integer>> fileList = new ArrayList<Map.Entry<?, Integer>>(files.entrySet());

			Collections.sort(fileList, new Comparator<Map.Entry<?, Integer>>() {

				public int compare(Map.Entry<?, Integer> obj1, Map.Entry<?, Integer> obj2) {
					return obj1.getValue().compareTo(obj2.getValue());
				}
			});

			Collections.reverse(fileList);

			if (occur != 0) {
				
				System.out.println("------Top 5 search results -----");

				int noOfFetch = 5;
				int j = 0;
				int i=1;
				while (fileList.size() > j && noOfFetch > 0) {
					
					
					if(fileList.get(j).getKey()!=null) {
					System.out.println("(" + i + ") " + fileList.get(j).getKey());
					j++;
					i++;
					}
					noOfFetch--;
					
				}
			} 

		}

		public static void suggestAltWord(String wordToSearch) {
			String line = " ";
			String regex = "[a-zA-Z0-9]+";

			// Create a Pattern object
			Pattern pattern = Pattern.compile(regex);
			// Now create matcher object.
			Matcher matcher = pattern.matcher(line);
			int fileNumber = 0;

			File dir = new File(Path.txtDirectoryPath);
			File[] fileArray = dir.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				try {
					findWord(fileArray[i], fileNumber, matcher, wordToSearch);
					fileNumber++;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

			Integer allowedDistance = 1; // Edit distance allowed
			boolean matchFound = false; // set to true if word found with edit distance equal to allowedDistance

			
			int i = 0;
			for (Map.Entry entry : numbers.entrySet()) {
				if (allowedDistance == entry.getValue()) {
					
					i++;
					
					if(i==1)
					System.out.println("Did you mean? ");
					
					System.out.print("(" + i + ") " + entry.getKey() + "\n");
					matchFound = true;
				}
			}
			if (!matchFound)
				System.out.println("Entered word cannot be resolved....");
		}

		// finds strings with similar pattern and calls edit distance() on those strings
		public static void findWord(File sourceFile, int fileNumber, Matcher match, String str)
				throws FileNotFoundException, ArrayIndexOutOfBoundsException {
			try {
				BufferedReader my_rederObject = new BufferedReader(new FileReader(sourceFile));
				String line = null;

				while ((line = my_rederObject.readLine()) != null) {
					match.reset(line);
					while (match.find()) {
						key.add(match.group());
					}
				}

				my_rederObject.close();
				for (int p = 0; p < key.size(); p++) {
					numbers.put(key.get(p), editDistance(str.toLowerCase(), key.get(p).toLowerCase()));
				}
			} catch (Exception e) {
				System.out.println("Exception:" + e);
			}

		}

		public static int editDistance(String str1, String str2) {
			int len1 = str1.length();
			int len2 = str2.length();

			int[][] my_array = new int[len1 + 1][len2 + 1];

			for (int i = 0; i <= len1; i++) {
				my_array[i][0] = i;
			}

			for (int j = 0; j <= len2; j++) {
				my_array[0][j] = j;
			}

			// iterate though, and check last char
			for (int i = 0; i < len1; i++) {
				char c1 = str1.charAt(i);
				for (int j = 0; j < len2; j++) {
					char c2 = str2.charAt(j);

					if (c1 == c2) {
						my_array[i + 1][j + 1] = my_array[i][j];
					} else {
						int replace = my_array[i][j] + 1;
						int insert = my_array[i][j + 1] + 1;
						int delete = my_array[i + 1][j] + 1;

						int min = replace > insert ? insert : replace;
						min = delete > min ? min : delete;
						my_array[i + 1][j + 1] = min;
					}
				}
			}

			return my_array[len1][len2];
		}

}
