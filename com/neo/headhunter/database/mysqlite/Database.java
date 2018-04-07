package com.neo.headhunter.database.mysqlite;

import com.neo.headhunter.HeadHunter;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public abstract class Database {
    private HeadHunter plugin;
    private Connection connection;

    private File dbFile;

    public Database(HeadHunter plugin, File dbFile) {
        this.plugin = plugin;
        this.dbFile = dbFile;
        this.connection = getSQLConnection();
    }

    protected void createTable(String name, String primary, String... columns) {
        StringBuilder statement = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        statement.append(name);
        statement.append(" (");
        for(String column : columns) {
            statement.append(column);
            statement.append(", ");
        }
        statement.append("PRIMARY KEY (");
        statement.append(primary);
        statement.append("));");
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(statement.toString());
            s.close();
        } catch(SQLException ex) {
        	System.out.println("Error statement: " + statement.toString());
            ex.printStackTrace();
        }
    }

    protected ResultSet query(String query) {
    	Statement s = null;
        try {
            s = connection.createStatement();
            return s.executeQuery(query);
        } catch(SQLException ex) {
	        System.out.println("Error statement: " + ((s == null) ? "null" : s.toString()));
            ex.printStackTrace();
            return null;
        }
    }
    
    protected ResultSet query(PreparedStatement query) {
        try {
            return query.executeQuery();
        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    protected boolean update(String update) {
    	Statement s = null;
        try {
            s = connection.createStatement();
            s.executeUpdate(update);
            s.close();
            return true;
        } catch(SQLException ex) {
	        System.out.println("Error statement: " + ((s == null) ? "null" : s.toString()));
            ex.printStackTrace();
            return false;
        }
    }

    public boolean update(PreparedStatement update) {
        try {
            update.executeUpdate();
            update.close();
            return true;
        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Connection getSQLConnection() {
        if (!dbFile.exists()){
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                //plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if(connection != null && !connection.isClosed())
                return connection;
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            return connection;
        } catch (SQLException ex) {
            //TODO fix logging
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "Missing SQLite JBDC library");
        }
        return null;
    }
}
