package com.vietanhs0817.faceidentify.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by YukiNoHara on 11/1/2017.
 */

public class AppUtils {
    public static String validatePersonName(String name){
        String valName;

        name = name.trim();
        name = removeAccent(name);
        valName = name.replaceAll(" ", "_");

        return valName;
    }

    public static String removeAccent(String s){
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
