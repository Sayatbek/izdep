package com.izdep.app.runner.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class DBHelper {

    private static String INIT_TABLE_WORDS = "CREATE TABLE " + ApiConst.TABLE_WORDS +
            "(id INT, " +
            "word VARCHAR(20000) CHARACTER SET utf8 COLLATE utf8_general_ci)";

    private static String INIT_TABLE_LINKS = "CREATE TABLE " + ApiConst.TABLE_LINKS +
            " (id INT, " +
            "url VARCHAR(20000) CHARACTER SET utf8 COLLATE utf8_general_ci, " +
            "rank INT, " + "title VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci, " +
            "description VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci)";

    private static String INIT_TABLE_IMAGES = "CREATE TABLE " + ApiConst.TABLE_IMAGES +
            " (id INT, " +
            "url VARCHAR(20000) CHARACTER SET utf8 COLLATE utf8_general_ci, rank INT)";

    private static String INIT_TABLE_WORDS_VS_LINKS = "CREATE TABLE " + ApiConst.TABLE_WORDS_VS_LINKS +
            " (word_id INT, link_id INT)";

    private static String INIT_TABLE_WORDS_VS_IMAGES = "CREATE TABLE " + ApiConst.TABLE_WORDS_VS_IMAGES +
            " (word_id INT, image_id INT)";

    private static Connection mConnection;
    private static PreparedStatement preparedStatement;

    public static Connection openConnection(Properties props) throws SQLException {
        String drivers = "com.mysql.jdbc.Driver";
        if(drivers!=null)
            System.setProperty(ApiConst.JDBC_DRIVERS, drivers);

        String url = System.getProperty("JDBC_CONNECTION_STRING");
        if(url==null)
            url = "jdbc:mysql://aatms6gf0sjvoh.cycarrqqfvjn.eu-central-1.rds.amazonaws.com:3306/izdep?useUnicode=yes&characterEncoding=UTF-8&user=root&password=salemALEM*11";

        mConnection = DriverManager.getConnection(url);

        return mConnection;
    }

    public static void createDB(Properties props) throws SQLException {
        System.out.println("Connecting to database");
        Connection mConnection = openConnection(props);

        Statement mStatement = mConnection.createStatement();

        System.out.println("Creating Tables...");
        mStatement.executeUpdate(INIT_TABLE_WORDS);
        mStatement.executeUpdate(INIT_TABLE_IMAGES);
        mStatement.executeUpdate(INIT_TABLE_LINKS);
        mStatement.executeUpdate(INIT_TABLE_WORDS_VS_LINKS);
        mStatement.executeUpdate(INIT_TABLE_WORDS_VS_IMAGES);
        mStatement.close();
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

    public static HashMap getAllWords(Connection connection, String table) {
        HashMap<String, Integer> nWords = new HashMap<String, Integer>();
        try {
            String query = "SELECT * FROM " + table;
            preparedStatement = connection.prepareStatement(query);
            ResultSet mResultSet = preparedStatement.executeQuery(query);
            while(mResultSet.next()) {
                nWords.put(mResultSet.getString("name"), 0);
            }
            return nWords;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Integer> getLinksListForWordID(Connection connection, int word_id) throws SQLException {
        List<Integer> wordsList = new ArrayList<>();
        if(connection!=null) {
            String query = "SELECT * FROM " + ApiConst.TABLE_WORDS_VS_LINKS + " WHERE word_id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, word_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                wordsList.add(resultSet.getInt("link_id"));
            }
            return wordsList;
        }else {
            return null;
        }
    }

    public static boolean checkUrlInDB(String url, String table) throws SQLException {
        preparedStatement = mConnection
                .prepareStatement("SELECT * FROM " + table + " WHERE url LIKE ?");
        preparedStatement.setString(1, url);
        ResultSet result = preparedStatement.executeQuery();
        if(result.next()) {
            preparedStatement.close();
            return true;
        }else {
            preparedStatement.close();
            return false;
        }
    }

    public static boolean checkWordInDB(String word, String table) throws SQLException {
        preparedStatement = mConnection.prepareStatement("SELECT * FROM " + table + " WHERE word LIKE ?");
        preparedStatement.setString(1, word);
        ResultSet result = preparedStatement.executeQuery();
        if (result.next()) {
            preparedStatement.close();
            return true;
        }
        preparedStatement.close();
        return false;
    }

    public static boolean checkWordInDB(Connection connection, String word, String table) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE word LIKE ?");
        preparedStatement.setString(1, word);
        ResultSet result = preparedStatement.executeQuery();
        if (result.next()) {
            preparedStatement.close();
            return true;
        }
        preparedStatement.close();
        return false;
    }

    public static boolean checkWordInWordsVsImages(int word_id, String table) throws SQLException {
        preparedStatement = mConnection.prepareStatement("SELECT * FROM " + table + " WHERE word_id=?");
        preparedStatement.setInt(1, word_id);
        ResultSet result = preparedStatement.executeQuery();
        if (result.next()) {
            preparedStatement.close();
            return true;
        }
        preparedStatement.close();
        return false;
    }


    public static void updateRankOfUrl(String url, String table) throws SQLException {
        int currentRank = getUrlRankFromDB(url, table);
        currentRank++;
        preparedStatement = mConnection.prepareStatement("UPDATE " + table + " SET rank = ? WHERE url = ?");
        preparedStatement.setInt(1, currentRank);
        preparedStatement.setString(2, url);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public static int getUrlRankFromDB(String url, String table) throws SQLException {
        preparedStatement = mConnection.prepareStatement("SELECT * FROM " + table + " WHERE url LIKE ?");
        preparedStatement.setString(1, url);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        int rank = result.getInt(3);
        preparedStatement.close();
        return rank;
    }

    public static int getWordId(String word, String table) throws SQLException {
        preparedStatement = mConnection.prepareStatement("SELECT * FROM " + table + " WHERE word=?");
        preparedStatement.setString(1, word);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        int id = result.getInt("id");
        preparedStatement.close();
        return id;
    }

    public static int getWordId(Connection connection, String word, String table) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM " + table + " WHERE word=?");
        preparedStatement.setString(1, word);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        int id = result.getInt("id");
        preparedStatement.close();
        return id;
    }

    public static void insertURLInDB(int id, String url, String title, String description) throws SQLException {
        if (mConnection!=null) {
            preparedStatement = mConnection.prepareStatement("INSERT INTO " + ApiConst.TABLE_LINKS
                    + " (id, url, rank, title, description)"
                    + " VALUES (?, ?, 1, ?, ?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, url);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, description);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
    }

    public static void insertImageInDB(int id, String url) throws SQLException {
        if(mConnection!=null) {
            preparedStatement = mConnection.prepareStatement("INSERT INTO " + ApiConst.TABLE_IMAGES
                    + " (id, url, rank)"
                    + " VALUES (? , ?, 1)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, url);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
    }

    public static void insertWordInDB(int id, String word, String table) throws SQLException {
        if(mConnection!=null) {
            preparedStatement = mConnection
                    .prepareStatement("INSERT INTO " + table + " VALUES (?, ?)");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, word);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
    }

    public static void insertWordToImageLinks(int word_id, int image_id, String table) throws SQLException {
        if(mConnection!=null) {
            preparedStatement = mConnection
                    .prepareStatement("INSERT INTO " + table + " VALUES (?, ?)");
            preparedStatement.setInt(1, word_id);
            preparedStatement.setInt(2, image_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
    }

    public static void insertWordVsLinks(int word_id, int url_id, String table) throws SQLException {
        if(mConnection!=null) {
            preparedStatement = mConnection
                    .prepareStatement("INSERT INTO " + table + " VALUES (?, ?)");
            preparedStatement.setInt(1, word_id);
            preparedStatement.setInt(2, url_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
    }

    public static List<Integer> getURLListFromDB(Connection connection, int word_id, String table) throws SQLException {
        List<Integer> list = new ArrayList<>();
        preparedStatement = connection
                .prepareStatement("SELECT * FROM " + table + " WHERE word_id = ?");
        preparedStatement.setInt(1, word_id);
        ResultSet result = preparedStatement.executeQuery();
        while(result.next()) {
            list.add(result.getInt("image_id"));
        }
        preparedStatement.close();
        return list;
    }

    public static int getUrlIDFromDB(String url, String table) throws SQLException {
        preparedStatement = mConnection.prepareStatement("SELECT * FROM " + table + " WHERE url LIKE ?");
        preparedStatement.setString(1, url);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        int id = result.getInt("id");
        preparedStatement.close();
        return id;
    }

}
