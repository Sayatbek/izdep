package com.izdep.app.runner.spell_checker;

import com.izdep.app.runner.spell_checker.entity.SpellCheckerResult;
import com.izdep.app.runner.utils.ApiConst;
import com.izdep.app.runner.utils.DBHelper;
import com.izdep.app.runner.utils.PropertyHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SpellChecker {

    private static HashMap<String, Integer> nWords;

    public static SpellCheckerResult handleSearch(Connection connection, String[] words) throws SQLException {
        nWords = DBHelper.getAllWords(connection, ApiConst.TABLE_ALL_WORDS);
        String result = "";
        int correctWordsCounter = 0;
        int wordsLength = words.length;
        SpellCheckerResult spellCheckerResult = new SpellCheckerResult();
        for(String word: words) {
            if(!word.trim().equals("")) {
                String correctWord = checkWord(word);
                if (correctWord.trim().equals(word)) {
                    correctWordsCounter++;
                }
                result = result + (" " + correctWord);
            }else {
                wordsLength--;
            }
        }

        if(correctWordsCounter==wordsLength)
            spellCheckerResult.setCorrect(true);
        else
            spellCheckerResult.setCorrect(false);
        spellCheckerResult.setResult(result);
        return spellCheckerResult;
    }

    public static String checkWord(String word) throws SQLException {
        if(nWords.containsKey(word)) {
            return word;
        }else {
            ArrayList<String> list = getVariants(word);
            HashMap<Integer, String> candidates = new HashMap<Integer, String>();
            for(String s : list) if(nWords.containsKey(s)) candidates.put(nWords.get(s),s);
            if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
            for(String s : list) for(String w : getVariants(s)) if(nWords.containsKey(w)) candidates.put(nWords.get(w),w);
            return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
        }
    }

    public static ArrayList<String> getVariants(String word) {
        char[] alphabet = "аәбвгғдеёжзийкқлмнңоөпрстуұүфхһцчшщъыіьэюя".toCharArray();
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0; i < word.length(); ++i) {
            result.add(word.substring(0, i) + word.substring(i+1));
        }
        for(int i=0; i < word.length()-1; ++i) {
            result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
        }
        for(int i=0; i < word.length(); ++i) {
            for(int j=0; j < alphabet.length; ++j) {
                result.add(word.substring(0, i) + alphabet[j] + word.substring(i+1));
            }
        }
        for(int i=0; i <= word.length(); ++i) {
            for(int j=0; j < alphabet.length; ++j){
                result.add(word.substring(0, i) + alphabet[j] + word.substring(i));
            }
        }
        return result;
    }
}
