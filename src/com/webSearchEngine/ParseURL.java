package com.webSearchEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ParseURL {

	public static void saveDoc(Document document,String url) {
		try {
			BufferedWriter html = new BufferedWriter(
					new FileWriter(Path.htmlDirectoryPath + document.title().replace('/', '_') + ".html"));
			html.write(document.toString());
			html.close();
			htmlToText(Path.htmlDirectoryPath + document.title().replace('/', '_') + ".html",url, document.title().replace('/', '_') + ".txt");
			

		} catch (Exception e) {

		}
	}
	

	public static void htmlToText(String htmlfile,String url,String filename) throws Exception {
		File file = new File(htmlfile);
		Document document = Jsoup.parse(file, "UTF-8");
		String data = document.text();
		data=url+"::"+data;
		PrintWriter writer = new PrintWriter(Path.txtDirectoryPath + filename);
		writer.println(data);
		writer.close();
	}
}
