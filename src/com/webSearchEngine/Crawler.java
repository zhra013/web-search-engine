package com.webSearchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	private static Set<String> crawledList = new HashSet<String>();
	private static int maxDepth = 2;
	private static String regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

	/**
	 * This method search for links in the web page and stores html in list
	 * 
	 * @param url   [web page url to be searched for urls]
	 * @param depth [depth of the urls to be crawled]
	 * @throws IOException
	 */
	public static void startCrawler(String url, int depth) {
		Pattern patternObject = Pattern.compile(regex);
		if (depth <= maxDepth) {
			try {
				Document document = Jsoup.connect(url).get();
				ParseURL.saveDoc(document, url);
				depth++;
				if (depth < maxDepth) {
					Elements links = document.select("a[href]");
					for (Element page : links) {

						if (verifyUrl(page.attr("abs:href")) && patternObject.matcher(page.attr("href")).find()) {
							System.out.println(page.attr("abs:href"));
							startCrawler(page.attr("abs:href"), depth);
							crawledList.add(page.attr("abs:href"));
						}
					}
				}
			} catch (Exception e) {

			}
		}
	}

	private static boolean verifyUrl(String nextUrl) {
		if (crawledList.contains(nextUrl)) {
			return false;
		}
		if (nextUrl.endsWith(".jpeg") || nextUrl.endsWith(".jpg") || nextUrl.endsWith(".png")
				|| nextUrl.endsWith(".pdf") || nextUrl.contains("#") || nextUrl.contains("?")
				|| nextUrl.contains("mailto:") || nextUrl.startsWith("javascript:") || nextUrl.endsWith(".gif")) {
			return false;
		}
		return true;
	}
}
