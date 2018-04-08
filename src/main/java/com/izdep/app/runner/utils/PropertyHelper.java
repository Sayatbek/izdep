package com.izdep.app.runner.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
}
