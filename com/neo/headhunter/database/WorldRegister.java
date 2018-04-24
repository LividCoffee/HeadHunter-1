package com.neo.headhunter.database;

import com.neo.headhunter.util.config.Settings;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public final class WorldRegister {
	private Connection c;
	
	private PreparedStatement addWorld, removeWorld, getWorld;
	
	public WorldRegister(Connection c) {
		this.c = c;
		
		createTables();
		prepareStatements();
	}
	
	public boolean isValidDropLocation(LivingEntity target) {
		return Settings.isIgnoreWorlds() || isHunterWorld(target.getWorld());
	}
	
	public boolean addWorld(World world) {
		if(world == null)
			return false;
		if(isHunterWorld(world))
			return false;
		try {
			addWorld.setString(1, world.getName());
			addWorld.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean removeWorld(World world) {
		if(world == null)
			return false;
		if(!isHunterWorld(world))
			return false;
		try {
			removeWorld.setString(1, world.getName());
			removeWorld.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private boolean isHunterWorld(World world) {
		if(world == null)
			return false;
		try {
			getWorld.setString(1, world.getName());
			return getWorld.executeQuery().next();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void createTables() {
		try {
			Statement s = c.createStatement();
			s.execute("create table if not exists hunter_world (" +
					          "world_name text," +
					          "primary key (world_name))");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStatements() {
		try {
			this.addWorld = c.prepareStatement("insert or replace into hunter_world values (?)");
			this.removeWorld = c.prepareStatement("delete from hunter_world where world_name = ?");
			this.getWorld = c.prepareStatement("select world_name from hunter_world where world_name = ?");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
