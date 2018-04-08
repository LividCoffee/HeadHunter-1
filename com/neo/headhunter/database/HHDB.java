package com.neo.headhunter.database;

import com.neo.headhunter.HeadHunter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public final class HHDB {
	private HeadHunter plugin;
	private File dbFile;
	private Connection c;
	
	private BountyRegister bountyRegister;
	
	public HHDB(HeadHunter plugin) {
		this.plugin = plugin;
		this.dbFile = new File(plugin.getDataFolder() + File.separator + "headhunter.db");
		this.c = getSQLConnection();
		
		if(c == null)
			throw new IllegalArgumentException("connection is null");
		
		this.bountyRegister = new BountyRegister(c);
	}
	
	public BountyRegister getBountyRegister() {
		return bountyRegister;
	}
	
	private Connection getSQLConnection() {
		if (!dbFile.exists()){
			try {
				if(dbFile.createNewFile())
					plugin.getLogger().log(Level.INFO, "HeadHunter successfully created database file");
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
