package com.neo.headhunter.database.mysqlite;

import com.neo.headhunter.HeadHunter;

import java.util.logging.Level;

public class Error {
    public static void execute(HeadHunter plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }

    public static void close(HeadHunter plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
