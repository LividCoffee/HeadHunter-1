package com.neo.headhunter.database;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.mysqlite.DBStatement;
import com.neo.headhunter.database.mysqlite.Database;
import com.neo.headhunter.database.util.DBClause;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.item.head.HeadData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public final class HeadDB extends Database {
	public HeadDB(HeadHunter plugin, File dbFile) {
		super(plugin, dbFile);
		createTable(
				T_Heads.NAME,
				T_Heads.C_LOCATION,
				T_Heads.C_LOCATION + " TEXT NOT NULL",
				T_Heads.C_OWNER + " TEXT",
				T_Heads.C_DURABILITY + " INTEGER NOT NULL DEFAULT 3",
				T_Heads.C_DISPLAYNAME + " TEXT",
				T_Heads.C_LORE + " TEXT"
		);
	}
	
	public void placeHead(Location loc, ItemStack head) {
		String locString = Utils.parseLocation(loc);
		if(locString == null || head == null || head.getType() != Material.SKULL_ITEM)
			return;
		DBStatement dbs = new DBStatement(DBClause.INSERT, T_Heads.NAME);
		dbs.setColumns(
				T_Heads.C_LOCATION,
				T_Heads.C_OWNER,
				T_Heads.C_DURABILITY,
				T_Heads.C_DISPLAYNAME,
				T_Heads.C_LORE
		);
		HeadData data = new HeadData(head);
		dbs.setValues(
				locString,
				data.getOwner(),
				String.valueOf(data.getDurability()),
				data.getDisplayName(),
				data.getLore()
		);
		update(dbs.toString());
	}
	
	public ItemStack getHead(Location loc) {
		String locString = Utils.parseLocation(loc);
		if(locString == null)
			return null;
		DBStatement dbs = new DBStatement(DBClause.SELECT, T_Heads.NAME);
		dbs.setColumns(
				T_Heads.C_OWNER,
				T_Heads.C_DURABILITY,
				T_Heads.C_DISPLAYNAME,
				T_Heads.C_LORE
		);
		ResultSet rs = query(dbs.toString());
		
		ItemStack result = null;
		try {
			if (rs.next()) {
				String owner = rs.getString(T_Heads.C_OWNER);
				short durability = rs.getShort(T_Heads.C_DURABILITY);
				String displayName = rs.getString(T_Heads.C_DISPLAYNAME);
				List<String> lore = Arrays.asList(rs.getString(T_Heads.C_LORE).split("\\Q\n\\E"));
				
				result = new ItemStack(Material.SKULL_ITEM, 1, durability);
				SkullMeta meta = (SkullMeta) result.getItemMeta();
				meta.setOwner(owner);
				meta.setDisplayName(displayName);
				meta.setLore(lore);
				result.setItemMeta(meta);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public void breakHead(Location loc) {
		String locString = Utils.parseLocation(loc);
		if(locString == null)
			return;
		DBStatement dbs = new DBStatement(DBClause.DELETE, T_Heads.NAME);
		dbs.setWhere(T_Heads.C_LOCATION + " = " + locString);
		update(dbs.toString());
	}
	
	//Table layout for consistency
	private static final class T_Heads {
		private static final String NAME = "heads";
		private static final String C_LOCATION = "location";
		private static final String C_OWNER = "owner";
		private static final String C_DURABILITY = "durability";
		private static final String C_DISPLAYNAME = "displayname";
		private static final String C_LORE = "lore";
	}
}
