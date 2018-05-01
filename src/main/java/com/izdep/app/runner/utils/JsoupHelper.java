package com.izdep.app.runner.utils;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class JsoupHelper {

    public static Document parseURL(String url) throws IOException {
        if(isActiveUrl(url)) {
            Document doc = null;
            doc = Jsoup.connect(url)
                    .ignoreHttpErrors(true)
                    .timeout(0)
                    .get();
            return doc;
        }else {
            return null;
        }
    }

    public static boolean isActiveUrl(String url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");

            con.setConnectTimeout(7000);
            con.setReadTimeout(10000);

            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (java.net.SocketTimeoutException e) {
            return false;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    public static boolean imageExists(String url) {

        HttpURLConnection con = null;

        try {
            HttpURLConnection.setFollowRedirects(false);

            // Only get the head of the document to save space and time
            con =
                    (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    public static String getTitleOfUrl(String url) throws IOException {
        Document mDocument = parseURL(url);
        int maxLength = 200;
        if(mDocument!=null && mDocument.title()!=null) {
            if(mDocument.title().length()>200)
                return mDocument.title().substring(0, maxLength);
            else
                return mDocument.title();
        }else {
            return "";
        }
    }

    public static String getDescriptionOfUrl(String url) throws IOException {
        Document mDocument = parseURL(url);
        String description = getMetaTag(mDocument, "description");
        if (description == null && mDocument.body() != null)
            description = mDocument.body().text();
        else if (description == null)
            description = mDocument.text();
        if (description.length() > 100)
            description = description.substring(0, 100);

        return description;
    }

    public static String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null)
                return s;
        }
        return null;
    }

    public static boolean isHTML(String link) {
        URL url;
        HttpURLConnection urlc = null;
        try {
            url = new URL(link);
            urlc = (HttpURLConnection) url.openConnection();
            urlc.setAllowUserInteraction(false);
            urlc.setDoInput(true);
            urlc.setDoOutput(false);
            urlc.setUseCaches(true);

            // Only get the head of the document to save space and time
            urlc.setRequestMethod("HEAD");
            urlc.connect();

            // Check the content type to make sure the document is HTML
            String mime = urlc.getContentType();
            if (mime.contains("text/html")) {
                return true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (urlc != null)
                urlc.disconnect();
        }

        return false;
    }
}
