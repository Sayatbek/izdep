package com.izdep.app.runner.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBHelper {

    private static String INIT_TABLE_WORDS = "CREATE TABLE " + ApiConst.TABLE_WORDS +
            "(id INT not null auto_increment primary key, " +
            "word VARCHAR(20000) CHARACTER SET utf8 COLLATE utf8_general_ci)";

    private static String INIT_TABLE_LINKS = "CREATE TABLE " + ApiConst.TABLE_LINKS +
            " (id INT not null auto_increment primary key, " +
            "url VARCHAR(20000) CHARACTER SET utf8 COLLATE utf8_general_ci, " +
            "rank INT, " + "title VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci, " +
            "description VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci)";

    private static String INIT_TABLE_IMAGES = "CREATE TABLE " + ApiConst.TABLE_IMAGES +
            " (id INT not null auto_increment primary key, " +
            "url VARCHAR(20000) CHARACTER SET utf8 COLLATE utf8_general_ci, rank INT)";

    private static String INIT_TABLE_WORDS_VS_LINKS = "CREATE TABLE " + ApiConst.TABLE_WORDS_VS_LINKS +
            " (word_id INT, link_id INT)";

    private static String INIT_TABLE_WORDS_VS_IMAGES = "CREATE TABLE " + ApiConst.TABLE_WORDS_VS_IMAGES +
            " (word_id INT, image_id INT)";

    private static Connection mConnection;
    private static PreparedStatement preparedStatement;

    public static Connection openConnection(Properties props) throws SQLException {
        String drivers = props.getProperty(ApiConst.JDBC_DRIVERS);
        if(drivers!=null)
            System.setProperty(ApiConst.JDBC_DRIVERS, drivers);

        String url = props.getProperty(ApiConst.JDBC_URL);
        String password = props.getProperty(ApiConst.JDBC_PASSWORD);
        String username = props.getProperty(ApiConst.JDBC_USER);

        mConnection = DriverManager.getConnection(url, username, password);

        return mConnection;
    }

    public static void createDB(Properties props) throws SQLException {
        System.out.println("Connecting to database");
        Connection mConnection = openConnection(props);

        Statement mStatement = mConnection.createStatement();
        boolean reset = false;
        if(props.getProperty(ApiConst.CRAWLER_RESET).trim().equalsIgnoreCase("YES")) {
            reset = true;
        }

        if(reset) {
            try {
                System.out.println("Removing Previously Created Tables...");
                mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_LINKS);
                mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_IMAGES);
                mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_WORDS);
                mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_WORDS_VS_IMAGES);
                mStatement.executeUpdate("DROP TABLE " + ApiConst.TABLE_WORDS_VS_LINKS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Creating Tables...");
            mStatement.executeUpdate(INIT_TABLE_WORDS);
            mStatement.executeUpdate(INIT_TABLE_IMAGES);
            mStatement.executeUpdate(INIT_TABLE_LINKS);
            mStatement.executeUpdate(INIT_TABLE_WORDS_VS_LINKS);
            mStatement.executeUpdate(INIT_TABLE_WORDS_VS_IMAGES);
            mStatement.close();
        }
    }

    public static List<String> fetchWordFromDB(String word) throws SQLException {
        List<String> wordsList = new ArrayList<>();
        word = word.toLowerCase();
        if(mConnection!=null) {
            String query = "SELECT * FROM " + ApiConst.TABLE_ALL_WORDS + " WHERE name LIKE '%" + word + "%'";
            preparedStatement = mConnection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                wordsList.add(resultSet.getString("name"));
            }
            return wordsList;
        }else {
            return null;
        }
    }

    public static List<String> getAllWords() throws SQLException {
        List<String> wordsList = new ArrayList<>();
        if(mConnection!=null) {
            String query = "SELECT * FROM " + ApiConst.TABLE_ALL_WORDS;
            preparedStatement = mConnection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                wordsList.add(resultSet.getString("name"));
            }
            return wordsList;
        }else {
            return null;
        }
    }
}
