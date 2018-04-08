package com.izdep.app.runner.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupHelper {

    public static Document parseURL(String url) throws IOException {
        InputStream in = null;
        Document doc = null;
        try {
            in = new URL(url).openStream();
            doc = Jsoup.parse(in, "UTF-8", url);
        } catch (Exception e) {
            System.out.println("Could Not Obtain Document...");
            System.out.println("Skipped.");
            System.out.println("-------------------------------------------");
            doc = null;
        } finally {
            if (in != null)
                in.close();
        }
        return doc;
    }
}
