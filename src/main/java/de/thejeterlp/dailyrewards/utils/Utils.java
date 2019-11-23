package de.thejeterlp.dailyrewards.utils;

public class Utils {

    public static String replaceColors(String message) {
        return message.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
    }

}
