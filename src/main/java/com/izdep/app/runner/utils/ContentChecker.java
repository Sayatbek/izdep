package com.izdep.app.runner.utils;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ContentChecker {

    public static void main(String[] args) throws IOException, SQLException {
        String url = "http://www.qamshy.kz/";

        if(checkContent(url)) {
            System.out.println("This is kazakh web page");
        }else {
            System.out.println("This is not kazakh web page");
        }
    }

    public static boolean checkContent(String url) throws SQLException, IOException {
        DBHelper.openConnection(PropertyHelper.getProperties());
        List<String> allWords = DBHelper.getAllWords();

        System.out.println("AllWords table last element " + allWords.size());

        String text;
        String[] words;
        Document mDocument = JsoupHelper.parseURL(url);
        System.out.println("Extracting words from: " + url);
        // Remove html white spaces
        mDocument.select(":containsOwn(\u00a0)").remove();
        text = mDocument.text();
        // Remove remaining HTML code
        text = Jsoup.clean(text, Whitelist.relaxed());
        // Get each word of the document
        words = text.split("\\s+"); // split the string by white spaces to get individual words

        int numOfWords = 0, numOfKazakhWords = 0;
        for (String word:words) {
            word = word.toLowerCase();
            word = word.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", ""); // Remove punctuation
            if (word.matches("[\\p{IsAlphabetic}\\p{IsDigit}]+")) { // If the word is letters and numbers only
                numOfWords++;
                if(allWords.contains(word)) {
                    numOfKazakhWords++;
                }
            }
        }

        System.out.println(numOfWords + "/" + numOfKazakhWords);
        int onePercent = numOfWords/100;
        int percentageOfKazakhWords = numOfKazakhWords/onePercent;
        if(percentageOfKazakhWords>=65) {
            return true;
        }else {
            return false;
        }

    }

}
