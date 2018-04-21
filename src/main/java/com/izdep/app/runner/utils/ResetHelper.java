package com.izdep.app.runner.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ResetHelper {

    public static void main(String[] args) throws SQLException, IOException {
        Properties props = PropertyHelper.getProperties();

        resetApplicationProperties(props);
        dropTables(props);
    }

    private static void resetApplicationProperties(Properties props) throws IOException {
        props.setProperty(ApiConst.CRAWLER_RESET, "YES");
        props.setProperty(ApiConst.CRAWLER_NEXT_WORD_ID, "" + 0);
        props.setProperty(ApiConst.CRAWLER_NEXT_URL_ID, "" + 0);
        props.setProperty(ApiConst.CRAWLER_NEXT_IMAGE_URL_ID, "" + 0);
        props.setProperty(ApiConst.CRAWLER_NEXT_URL_ID_SCANNED, "" + 0);
        FileOutputStream out = new FileOutputStream("src/main/resources/application.properties");
        props.store(out, null);
    }

    private static void dropTables(Properties props) throws SQLException {
        Connection connection = DBHelper.openConnection(props);
        Statement mStatement = connection.createStatement();

        try {
            System.out.println("Removing Previously Created Tables...");
            mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_LINKS);
            mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_IMAGES);
            mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_WORDS);
            mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_WORDS_VS_LINKS);
            mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_WORDS_VS_IMAGES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
