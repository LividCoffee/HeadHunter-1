package com.neo.headhunter.database.util;

public enum DBClause {
    /**
     * SELECT %columns% FROM %table% [WHERE %condition%] [ORDER BY %column%] [LIMIT [%offset%, ]%limit%];
     */
    SELECT("SELECT " + SQLU.COLUMNS + " FROM " + SQLU.TABLE),

	/**
	 * INSERT INTO %table% (%columns%) VALUES (%values%);
	 */
	INSERT("INSERT INTO " + SQLU.TABLE + " (" + SQLU.COLUMNS + ") VALUES (" + SQLU.VALUES + ")"),
	
	/**
	 * REPLACE INTO %table% (%columns%) VALUES (%values%);
	 */
	INSERT_REPLACE("REPLACE INTO " + SQLU.TABLE + " (" + SQLU.COLUMNS + ") VALUES (" + SQLU.VALUES + ")"),

	/**
	 * DELETE FROM %table% [WHERE %condition%] [ORDER BY %column%] [LIMIT [%offset%, ]%limit%];
	 */
	DELETE("DELETE FROM " + SQLU.TABLE),

	/**
	 * UPDATE %table% SET %assignments% [WHERE %condition%] [ORDER BY %column%] [LIMIT [%offset%, ]%limit%];
	 */
	UPDATE("UPDATE " + SQLU.TABLE + " SET " + SQLU.ASSIGNMENTS);

    public static final String WHERE = " WHERE " + SQLU.WHERE;
    public static final String ORDER_BY = " ORDER BY " + SQLU.ORDER_BY;
    public static final String LIMIT = " LIMIT " + SQLU.LIMIT;
    public static final String LIMIT_OFFSET = " LIMIT " + SQLU.OFFSET + ", " + SQLU.LIMIT;

    private String format;

    DBClause(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
