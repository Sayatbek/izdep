package com.izdep.app.runner.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyHelper {

    public static Properties getProperties() {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("src/main/resources/application.properties");
            props.load(in);
            in.close();
            return props;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setPropertyRootUrl(Properties props, String url) throws IOException {
        props.setProperty(ApiConst.CRAWLER_ROOT, url);
        FileOutputStream out = new FileOutputStream("src/main/resources/application.properties");
        props.store(out, null);
    }

    public static void setCrawlerStatus(Properties props) throws IOException {
        props.setProperty(ApiConst.CRAWLER_RESET, "NO");
        FileOutputStream out = new FileOutputStream("src/main/resources/application.properties");
        props.store(out, null);
    }

    public static void setPropertyNextImageURL(Properties props, int NextImageURLID) throws IOException {
        props.setProperty(ApiConst.CRAWLER_NEXT_IMAGE_URL_ID, "" + NextImageURLID);
        FileOutputStream out = new FileOutputStream("src/main/resources/application.properties");
        props.store(out, null);
    }

    public static void setProperty(Properties props, String key, String value) throws IOException {
        props.setProperty(key, value);
        FileOutputStream out = new FileOutputStream("src/main/resources/application.properties");
        props.store(out, null);
    }

}
