package com.neo.headhunter.database;

import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.item.head.HeadData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.*;
import java.util.Arrays;

public final class HeadRegister {
	private Connection c;
	
	private PreparedStatement placeHead, getHead;
	
	HeadRegister(Connection c) {
		this.c = c;
		
		createTables();
		prepareStatements();
	}
	
	public void placeHead(Location loc, ItemStack head) {
		try {
			if(head == null || head.getType() != Material.SKULL_ITEM)
				return;
			HeadData headData = new HeadData(head);
			placeHead.setString(1, Utils.parseLocation(loc));
			placeHead.setString(2, headData.getOwner());
			placeHead.setShort(3, headData.getDurability());
			placeHead.setString(4, headData.getDisplayName());
			placeHead.setString(5, headData.getLore());
			placeHead.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ItemStack getHead(Location loc) {
		try {
			getHead.setString(1, Utils.parseLocation(loc));
			ResultSet rs = getHead.executeQuery();
			if(rs.next()) {
				ItemStack result = new ItemStack(Material.SKULL_ITEM, 1, rs.getShort(3));
				SkullMeta meta = (SkullMeta) result.getItemMeta();
				meta.setOwner(rs.getString(2));
				meta.setDisplayName(rs.getString(4));
				meta.setLore(Arrays.asList(rs.getString(5).split("\\Q\n\\E")));
				result.setItemMeta(meta);
				return result;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void createTables() {
		try {
			Statement s = c.createStatement();
			s.execute("create table if not exists head (" +
					          "location text, owner text, durability integer, displayname text, lore text," +
					          "primary key (location)," +
					          "foreign key (location) references block(location)" +
					          "on delete cascade);");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStatements() {
		try {
			this.placeHead = c.prepareStatement("insert or replace into head values (?, ?, ?, ?, ?)");
			this.getHead = c.prepareStatement("select * from head where location = ?");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
