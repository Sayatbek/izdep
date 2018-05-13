package com.izdep.app.controller;

import com.izdep.app.model.ResultURLModel;
import com.izdep.app.runner.Crawler;
import com.izdep.app.runner.spell_checker.SpellChecker;
import com.izdep.app.runner.spell_checker.entity.SpellCheckerResult;
import com.izdep.app.runner.stemmer.IzdepStemmer;
import com.izdep.app.runner.utils.ApiConst;
import com.izdep.app.runner.utils.DBHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Servlet implementation class Search
 */
@Controller
@RequestMapping(value = "/web")
public class SearchController {
    @Value("${kaz.search}")
    String search;

    @Value("${kaz.images}")
    String images;

    @Value("${kaz.web}")
    String web;

    @Value("${kaz.request}")
    String searchRequest;

    @Value("${kaz.sitename}")
    String sitename;

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private Properties props;


    @RequestMapping(value = "/search")
    String doIt(ModelMap modelMap, HttpServletRequest request,
                  HttpServletResponse response) throws ServletException, IOException {
        modelMap.addAttribute("search", search);
        modelMap.addAttribute("images", images);
        modelMap.addAttribute("request", searchRequest);
        modelMap.addAttribute("web",web);
        modelMap.addAttribute("sitename", sitename);

        response.setContentType("text/html; UTF-8");

        int maxRank = 0;
        int pageNum = 0;
        List<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
        String search = new String();

        try {
            // Get the search query and page number from the request
            search = request.getParameter("search_field");
            pageNum = Integer.parseInt(request.getParameter("page"));

            // Open database connection
            openConnection();

            // Split the search query by words
            String[] words = search.toLowerCase().split("\\s+");
            List<Integer> urlIDs = new ArrayList<>();
            List<ResultURLModel> result = new ArrayList<ResultURLModel>();

            // Create a crawler with the opened connection
            openConnection();

            // Check search field with spell checker
            SpellCheckerResult spellCheckerResult = SpellChecker.handleSearch(connection, words);

            // Get the urlList of each word
            for (String word : words) {
                // Check to see if the search query word is in the DB
                word = IzdepStemmer.stem(word);
                if (DBHelper.checkWordInDB(connection, word, ApiConst.TABLE_WORDS)) {
                    // Get the urlList corresponding to the word
                    int word_id = DBHelper.getWordId(connection, word, ApiConst.TABLE_WORDS);
                    List<Integer> list = DBHelper.getLinksListForWordID(connection, word_id);
                    urlIDs.addAll(list);
                }
            }

            if (!urlIDs.isEmpty()) {
                // Get the intersection of the urlIDs
//                List<Integer> intersection = urlIDs.get(0);
//                for (List<Integer> list : urlIDs)
//                    intersection.retainAll(list);

                List<Integer> newListWithUniqueElements = new ArrayList<Integer>(new HashSet<Integer>(urlIDs));

                // Retrieve information from DB for each urlID
                for (int urlid : newListWithUniqueElements) {
                    PreparedStatement pstmt = connection
                            .prepareStatement("SELECT * FROM " + ApiConst.TABLE_LINKS + " WHERE id = ?");
                    pstmt.setInt(1, urlid);
                    ResultSet r = pstmt.executeQuery();
                    r.next();
                    int rank = r.getInt(3);
                    if (rank > maxRank)
                        maxRank = rank;

                    String url = r.getString(2);
                    if (url.contains("#")) // skip url containing fragment identifiers
                        continue;

                    // Add data to the result list
                    result.add(new ResultURLModel(urlid, url, rank, r
                            .getString(4), r.getString(5)));
                    pstmt.close();
                }

                /*
                 * Update rank if the word is found in the URL's title. For every
                 * word found in the title, increment the rank by one
                 *
                 */
                for (ResultURLModel obj : result) {
                    int count = 1;
                    for (String word : words) {
                        String title = obj.getTitle().toLowerCase();

                        if (title.contains(word.toLowerCase())) {
                            // Bold the word in the title
                            int start = title.indexOf(word);
                            int end = start + word.length();
                            String titleBold = obj.getTitle().substring(0, start) + "<b>"
                                    + obj.getTitle().substring(start, end) + "</b>"
                                    + obj.getTitle().substring(end, title.length());

                            /*
                             * Update page rank. Bring element to the top of the list
                             * by adding the maxRank then adding the found word counter
                             *
                             */
                            obj.setRank(obj.getRank() + maxRank + count);

                            // Update to new title
                            obj.setTitle(titleBold);
                            count++;
                        }
                    }
                }

                // Sort the result collection by rank from high to low
                Collections.sort(result);

                // Convert object to String representations to be used in the jsp
                for (ResultURLModel obj : result) {
                    ArrayList<String> temp = new ArrayList<String>();
                    temp.add(obj.getUrl());
                    temp.add(obj.getTitle());
                    temp.add(obj.getDescription());
                    temp.add(obj.getRank() + "");
                    temp.add(obj.getUrlid() + "");

                    resultList.add(temp);
                }
            }

            String spell = "";
            if(!spellCheckerResult.isCorrect()) {
                spell = spellCheckerResult.getResult();
            }
            /*
             * Add resultList, original search query, and the update page number
             * to the request
             */
            request.setAttribute("query", search);
            request.setAttribute("resultList", resultList);
            request.setAttribute("spell_checker", spell);
            request.setAttribute("pageNum", pageNum);

            connection.close();
            return "results";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "error";
    }

    /**
     * Create a connection to the DB from the properties file
     *
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public void openConnection() throws SQLException, IOException,
            ClassNotFoundException {
        String driver = "com.mysql.jdbc.Driver";
        String url = System.getProperty("JDBC_CONNECTION_STRING");
        if(url==null)
            url = "jdbc:mysql://aatms6gf0sjvoh.cycarrqqfvjn.eu-central-1.rds.amazonaws.com:3306/izdep?useUnicode=yes&characterEncoding=UTF-8&user=root&password=salemALEM*11";

        System.out.println("SearchController " + url);

        Class.forName(driver);
        connection = DriverManager.getConnection(url);
    }

}
