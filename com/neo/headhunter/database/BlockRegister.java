package com.neo.headhunter.database;

import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.general.Triplet;
import com.neo.headhunter.util.item.BlockType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.UUID;

public final class BlockRegister {
	private Connection c;
	
	private PreparedStatement placeBlock, getBlock, breakBlock;
	
	BlockRegister(Connection c) {
		this.c = c;
		
		createTables();
		prepareStatements();
	}
	
	public void placeBlock(Location loc, OfflinePlayer placer, BlockType type) {
		try {
			placeBlock.setString(1, Utils.parseLocation(loc));
			placeBlock.setString(2, placer == null ? null : placer.getUniqueId().toString());
			placeBlock.setString(3, type.toString());
			placeBlock.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Triplet<Location, OfflinePlayer, BlockType> getBlock(Location loc) {
		try {
			getBlock.setString(1, Utils.parseLocation(loc));
			ResultSet rs = getBlock.executeQuery();
			if(rs.next()) {
				Location block = Utils.readLocation(rs.getString(1));
				OfflinePlayer placer = PlayerUtils.getPlayer(UUID.fromString(rs.getString(2)));
				BlockType type = BlockType.valueOf(rs.getString(3));
				return new Triplet<>(block, placer, type);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void breakBlock(Location loc) {
		try {
			breakBlock.setString(1, Utils.parseLocation(loc));
			breakBlock.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isHead(Location loc) {
		try {
			PreparedStatement s = c.prepareStatement("select type from block where location = ?");
			s.setString(1, Utils.parseLocation(loc));
			ResultSet rs = s.executeQuery();
			if(rs.next())
				return rs.getString(1).equalsIgnoreCase(BlockType.HEAD.toString());
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isSellingSign(Location loc) {
		try {
			PreparedStatement s = c.prepareStatement("select type from block where location = ?");
			s.setString(1, Utils.parseLocation(loc));
			ResultSet rs = s.executeQuery();
			if(rs.next())
				return rs.getString(1).equalsIgnoreCase(BlockType.SELLING_SIGN.toString());
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isWantedSign(Location loc) {
		try {
			PreparedStatement s = c.prepareStatement("select type from block where location = ?");
			s.setString(1, Utils.parseLocation(loc));
			ResultSet rs = s.executeQuery();
			if(rs.next())
				return rs.getString(1).equalsIgnoreCase(BlockType.WANTED_SIGN.toString());
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void createTables() {
		try {
			Statement s = c.createStatement();
			s.execute("create table if not exists block (" +
					          "location text, placer text, type text," +
					          "primary key (location))");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStatements() {
		try {
			this.placeBlock = c.prepareStatement("insert or replace into block values (?, ?, ?)");
			this.getBlock = c.prepareStatement("select * from block where location = ?");
			this.breakBlock = c.prepareStatement("delete from block where location = ?");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
