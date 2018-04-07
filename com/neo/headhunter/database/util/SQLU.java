package com.neo.headhunter.database.util;

//SQLiteUtils
public final class SQLU {
    public static final String  TABLE =         "%t";
    public static final String  COLUMNS =       "%cs";
    public static final String  VALUES =        "%v";
    public static final String  ASSIGNMENTS =   "%a";
    public static final String  WHERE =         "%cond";
    public static final String  ORDER_BY =      "%ob";
    public static final String  LIMIT =         "%l";
    public static final String  OFFSET =        "%os";

    public static final String  LOCALTIME =     "DATETIME('now', 'localtime')";

    public static String concat(String[] arr) {
        boolean comma = false;
        StringBuilder result = new StringBuilder();
        for(String s : arr) {
            if(comma) result.append(",");
            result.append(s);
            comma = true;
        }
        return result.toString();
    }
}
