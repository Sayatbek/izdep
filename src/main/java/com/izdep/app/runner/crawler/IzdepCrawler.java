package com.izdep.app.runner.crawler;

import com.izdep.app.runner.entities.Images;
import com.izdep.app.runner.utils.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.*;

public class IzdepCrawler {

    private static Queue links;
    private static HashMap<String, Integer> mostPopularLinks = new HashMap<>();

    public static void main(String[] args) throws IOException, SQLException {

        File propertiesFile = new File("src/main/resources/application.properties");
        if(propertiesFile.exists()) {
            Properties props = PropertyHelper.getProperties();
            if(checkReset(props)) {
                startNewCrawler(props);
            }else {
                continueOldCrawler(props);
            }
        }
    }

    private static void startNewCrawler(Properties props) throws SQLException, IOException {
        links = RootUrls.getRootList();
        PropertyHelper.setCrawlerStatus(props);
        DBHelper.createDB(props);

        crawl(props);
    }

    private static void continueOldCrawler(Properties props) throws IOException, SQLException {
        links = new LinkedList();
        links.add(props.getProperty(ApiConst.CRAWLER_ROOT));
        DBHelper.openConnection(props);

        crawl(props);
    }

    private static String reverse(String s) {
        String res = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            res += s.charAt(i);
        }
        return res;
    }

    private static String getLink(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.' && (s.charAt(i + 1) == 'k' ||
            s.charAt(i + 1) == 'c')) {
                while (s.charAt(i - 1) != '.' && s.charAt(i - 1) != '/') {
                    res += s.charAt(i - 1);
                    i--;
                }
                res = reverse(res);
                return res;
            }
        }
        return res;
    }

    private static void crawl(Properties props) throws IOException, SQLException {

        while (!links.isEmpty()) {
            String curLink = links.remove().toString();
            String mainPart = getLink(curLink);
//            System.out.println(mainPart + " mainPart to add HashMap for checking existance");
            if (mostPopularLinks.containsKey(mainPart) && mostPopularLinks.get(mainPart) == 10) continue;
            if (!mostPopularLinks.containsKey(mainPart)) mostPopularLinks.put(mainPart, 0);
            else mostPopularLinks.put(mainPart, mostPopularLinks.get(mainPart) + 1);
            boolean urlExists = DBHelper.checkUrlInDB(curLink, ApiConst.TABLE_LINKS);
            if(ContentChecker.checkContent(curLink) && !urlExists) {
//            if( !urlExists) {
                int id = Integer.parseInt(props.getProperty(ApiConst.CRAWLER_NEXT_URL_ID));
                DBHelper.insertURLInDB(id ,curLink, JsoupHelper.getTitleOfUrl(curLink), JsoupHelper.getDescriptionOfUrl(curLink));
                Document document = JsoupHelper.parseURL(curLink);
                List<Images> imagesList = getImageFromLink(props, document, curLink);

                getWordsFromUrl(props, document, imagesList, curLink);
                id++;
                PropertyHelper.setProperty(props, ApiConst.CRAWLER_NEXT_URL_ID, id + "");
                getLinksFromURL(document, curLink);

                if (!links.isEmpty())
                    PropertyHelper.setPropertyRootUrl(props, links.peek().toString());
            }
        }
    }

    private static boolean checkReset(Properties props) {
        if(props.getProperty(ApiConst.CRAWLER_RESET).equalsIgnoreCase("yes")) {
            return true;
        }else {
            return false;
        }
    }

    private static List<Images> getImageFromLink(Properties props, Document document, String url) throws SQLException, IOException {
        System.out.println("Extracting images from: " + url);
        List<Images> imagesList = new ArrayList<>();
        Elements images = document.select("img");
        int nextImageURLID = Integer.valueOf(props.getProperty(ApiConst.CRAWLER_NEXT_IMAGE_URL_ID));
        for(Element e: images) {
            String imageFound = e.attr("abs:src").trim();
            boolean imageExists = DBHelper.checkUrlInDB(imageFound, ApiConst.TABLE_IMAGES);
            Images img = new Images(nextImageURLID, imageFound);
            imagesList.add(img);
            if(imageExists) {
                DBHelper.updateRankOfUrl(imageFound, ApiConst.TABLE_IMAGES);
            }

            if(!imageExists && JsoupHelper.imageExists(imageFound)) {
                DBHelper.insertImageInDB(nextImageURLID, imageFound);
                nextImageURLID++;
            }
        }
        PropertyHelper.setPropertyNextImageURL(props, nextImageURLID);
        return imagesList;
    }

    private static void getWordsFromUrl(Properties props, Document document, List<Images> imagesList, String url) throws SQLException, IOException {
        System.out.println("Extracting words from: " + url);
        String[] words;
        String text, urllist;
        int NextWordID = Integer.valueOf(props.getProperty(ApiConst.CRAWLER_NEXT_WORD_ID));
        // Remove html white spaces
        document.select(":containsOwn(\u00a0)").remove();
        text = document.text();
        // Remove remaining HTML code
        text = Jsoup.clean(text, Whitelist.relaxed());
        words = text.split("\\s+"); // split the string by white spaces to get individual words
        for (String word:words) {
            word = word.toLowerCase();
            word = word.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", ""); // Remove punctuation
            if (word.matches("[\\p{IsAlphabetic}\\p{IsDigit}]+")) { // If the word is letters and numbers only
                boolean wordExists = DBHelper.checkWordInDB(word, ApiConst.TABLE_WORDS);
                if(!wordExists) {
                    // If the word is not in the table, create a new entry
                    DBHelper.insertWordInDB(NextWordID, word, ApiConst.TABLE_WORDS);
                    DBHelper.insertWordVsLinks(NextWordID, DBHelper.getUrlIDFromDB(url, ApiConst.TABLE_LINKS), ApiConst.TABLE_WORDS_VS_LINKS);
                } else {
                    int id = DBHelper.getWordId(word, ApiConst.TABLE_WORDS);
                    DBHelper.insertWordVsLinks(id, DBHelper.getUrlIDFromDB(url, ApiConst.TABLE_LINKS), ApiConst.TABLE_WORDS_VS_LINKS);
                }

                int word_id = DBHelper.getWordId(word, ApiConst.TABLE_WORDS);
                boolean wordExitstInImgWord = DBHelper.checkWordInWordsVsImages(word_id, ApiConst.TABLE_WORDS_VS_IMAGES);
                if(!wordExitstInImgWord) {
                    if(!imagesList.isEmpty()) {
                        for (int i = 0; i < imagesList.size(); i++) {
                            DBHelper.insertWordToImageLinks(NextWordID, imagesList.get(i).getId(), ApiConst.TABLE_WORDS_VS_IMAGES);
                        }
                    }
                }else {
                    if(!imagesList.isEmpty()) {
                        for(Images image: imagesList) {
                            DBHelper.insertWordToImageLinks(word_id, image.getId(), ApiConst.TABLE_WORDS_VS_IMAGES);
                        }
                    }
                }
            }
            NextWordID++;
            PropertyHelper.setProperty(props, ApiConst.CRAWLER_NEXT_WORD_ID, NextWordID + "");
        }
    }

    private static void getLinksFromURL(Document mDocument, String url) throws SQLException, IOException {
        System.out.println("Extracting links from: " + url);
        Elements localLinks = mDocument.select("a");

        int amount = 5;

        for(Element e: localLinks) {
            if (amount-- == 0) break;
            String urlFound = e.attr("abs:href");
            urlFound = urlFound.trim();

            boolean urlExists = DBHelper.checkUrlInDB(urlFound, ApiConst.TABLE_LINKS);
            if(urlExists) {
                System.out.println("Url exists " + urlFound);
                DBHelper.updateRankOfUrl(urlFound, ApiConst.TABLE_LINKS);
            }

            if (!urlExists && (urlFound.contains("http://") || urlFound.contains("https://"))
                    && !urlFound.contains("#")
                    && JsoupHelper.isHTML(urlFound)) {
                if(ContentChecker.checkContent(urlFound)) {
                    System.out.println("New url is added " + urlFound);
                    links.add(urlFound);
                }
            }
        }
    }

 }


