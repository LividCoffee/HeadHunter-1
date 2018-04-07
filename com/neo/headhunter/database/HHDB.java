package com.neo.headhunter.database;

import com.neo.headhunter.HeadHunter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class HHDB {
	private HeadHunter plugin;
	private File dbFile;
	private Connection c;
	
	private PreparedStatement getBounty;
	private PreparedStatement getTotalBounty;
	private PreparedStatement getGodfather;
	
	public HHDB(HeadHunter plugin) {
		this.plugin = plugin;
		this.dbFile = new File(plugin.getDataFolder() + File.separator + "headhunter.db");
		this.c = getSQLConnection();
		
		try {
			createPreparedStatements();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createPreparedStatements() throws SQLException {
		this.getBounty = c.prepareStatement("select amount from bounty where hunter = ? and target = ?");
		this.getTotalBounty = c.prepareStatement("select sum(amount) as amount from bounty where target = ?");
		this.getGodfather = c.prepareStatement("select hunter, max(amount) as amount from bounty where target = ? group by target");
	}
	
	private Connection getSQLConnection() {
		if (!dbFile.exists()){
			try {
				dbFile.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "File write error: " + dbFile);
			}
		}
		try {
			if(c != null && !c.isClosed())
				return c;
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			return c;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
		} catch (ClassNotFoundException ex) {
			plugin.getLogger().log(Level.SEVERE, "Missing SQLite JBDC library");
		}
		return null;
	}
}
