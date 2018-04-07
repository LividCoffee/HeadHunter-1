package com.neo.headhunter.database;

import com.neo.headhunter.HeadHunter;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class HHDB {
	private HeadHunter plugin;
	private File dbFile;
	private Connection c;
	
	private PreparedStatement getBounty;
	private PreparedStatement getTotalBounty;
	private PreparedStatement getGodfather;
	private PreparedStatement addBounty;
	private PreparedStatement removeBountyTarget;
	private PreparedStatement removeBountySingle;
	private PreparedStatement removeBountyAmount;
	private PreparedStatement setBounty;
	
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
		this.addBounty = c.prepareStatement("insert or replace into bounty(hunter, target, amount) values (?, ?, amount + ?)");
		this.removeBountyTarget = c.prepareStatement("delete from bounty where target = ?");
		this.removeBountySingle = c.prepareStatement("delete from bounty where hunter = ? and target = ?");
		this.removeBountyAmount = c.prepareStatement("update bounty set amount = amount - ? where hunter = ? and target = ?");
		this.setBounty = c.prepareStatement("insert or replace into bounty(hunter, target, amount) values (?, ?, ?)");
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
