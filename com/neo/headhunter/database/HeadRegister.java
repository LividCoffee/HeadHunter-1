package com.neo.headhunter.database;

import com.neo.headhunter.util.HeadUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.item.head.HeadData;
import com.neo.headhunter.util.mob.MobLibrary;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.*;
import java.util.Arrays;

public final class HeadRegister {
	private Connection c;
	private MobLibrary mobLibrary;
	
	private PreparedStatement placeHead, getHead;
	
	HeadRegister(Connection c, MobLibrary mobLibrary) {
		this.c = c;
		this.mobLibrary = mobLibrary;
		
		createTables();
		prepareStatements();
	}
	
	public void placeHead(Location loc, ItemStack head) {
		try {
			if(head == null || head.getType() != Material.SKULL_ITEM)
				return;
			HeadData headData = HeadUtils.getData(head);
			placeHead.setString(1, Utils.parseLocation(loc));
			placeHead.setString(2, mobLibrary.getEntityTag(head));
			placeHead.setString(3, headData.getDisplayName());
			placeHead.setString(4, headData.getLore());
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
				String entityTag = rs.getString(2);
				ItemStack result;
				if(entityTag == null)
					result = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				else
					result = mobLibrary.getBaseHead(entityTag);
				SkullMeta meta = (SkullMeta) result.getItemMeta();
				if(entityTag != null)
					meta.setOwner(entityTag);
				String displayName = rs.getString(3);
				if(displayName != null)
					meta.setDisplayName(displayName);
				String lore = rs.getString(4);
				if(lore != null)
					meta.setLore(Arrays.asList(lore.split("\\Q\n\\E")));
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
					          "location text, entity_tag text, display_name text, lore text," +
					          "primary key (location)," +
					          "foreign key (location) references block(location)" +
					          "on delete cascade)");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStatements() {
		try {
			this.placeHead = c.prepareStatement("insert or replace into head values (?, ?, ?, ?)");
			this.getHead = c.prepareStatement("select * from head where location = ?");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
