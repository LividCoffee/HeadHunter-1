package com.neo.headhunter.database;

import com.neo.headhunter.HeadHunter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public final class HHDB {
	private HeadHunter plugin;
	private File dbFile;
	private Connection c;
	
	private WorldRegister worldRegister;
	private BountyRegister bountyRegister;
	private BlockRegister blockRegister;
	private HeadRegister headRegister;
	private SignRegister signRegister;
	
	public HHDB(HeadHunter plugin) {
		this.plugin = plugin;
		this.dbFile = new File(plugin.getDataFolder() + File.separator + "headhunter.db");
		this.c = getSQLConnection();
		
		if(c == null)
			throw new IllegalArgumentException("connection is null");
		
		setupSQLite();
		
		this.worldRegister = new WorldRegister(c);
		this.bountyRegister = new BountyRegister(c);
		this.blockRegister = new BlockRegister(c);
		this.headRegister = new HeadRegister(c, plugin.getMobLibrary());
		this.signRegister = new SignRegister(c);
	}
	
	public WorldRegister getWorldRegister() {
		return worldRegister;
	}
	
	public BountyRegister getBountyRegister() {
		return bountyRegister;
	}
	
	public BlockRegister getBlockRegister() {
		return blockRegister;
	}
	
	public HeadRegister getHeadRegister() {
		return headRegister;
	}
	
	public SignRegister getSignRegister() {
		return signRegister;
	}
	
	private Connection getSQLConnection() {
		if (!dbFile.exists()){
			try {
				if(dbFile.createNewFile())
					plugin.getLogger().log(Level.INFO, "HeadHunter successfully created new database file");
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
	
	private void setupSQLite() {
		try {
			Statement s = c.createStatement();
			s.execute("pragma foreign_keys = on");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
