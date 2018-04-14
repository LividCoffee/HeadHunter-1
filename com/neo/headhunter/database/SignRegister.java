package com.neo.headhunter.database;

import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.item.sign.SellingSign;
import com.neo.headhunter.util.item.sign.WantedSign;
import org.bukkit.Location;

import java.sql.*;
import java.util.UUID;

public class SignRegister {
	private Connection c;
	
	private PreparedStatement placeSellingSign, getSellingSign;
	private PreparedStatement placeWantedSign, getWantedSign, setWantedSignHead;
	
	public SignRegister(Connection c) {
		this.c = c;
		
		createTables();
		prepareStatements();
	}
	
	public void placeSellingSign(Location loc) {
		try {
			placeSellingSign.setString(1, Utils.parseLocation(loc));
			placeSellingSign.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public SellingSign getSellingSign(Location loc, UUID placer) {
		try {
			getSellingSign.setString(1, Utils.parseLocation(loc));
			ResultSet rs = getSellingSign.executeQuery();
			if(rs.next())
				return new SellingSign(placer);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void placeWantedSign(Location loc, int index) {
		try {
			placeWantedSign.setString(1, Utils.parseLocation(loc));
			placeWantedSign.setInt(2, index);
			placeWantedSign.setString(3, null);
			placeWantedSign.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public WantedSign getWantedSign(Location loc, UUID placer) {
		try {
			getWantedSign.setString(1, Utils.parseLocation(loc));
			ResultSet rs = getWantedSign.executeQuery();
			if(rs.next()) {
				WantedSign result = new WantedSign(placer);
				result.setBountyIndex(rs.getInt(2));
				result.setHeadLocation(Utils.readLocation(rs.getString(3)));
				return result;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setWantedSignHead(Location sign, Location head) {
		try {
			setWantedSignHead.setString(1, Utils.parseLocation(head));
			setWantedSignHead.setString(2, Utils.parseLocation(sign));
			setWantedSignHead.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createTables() {
		try {
			Statement s = c.createStatement();
			s.execute("create table if not exists selling_sign (" +
					          "location text," +
					          "primary key (location)," +
					          "foreign key (location) references block(location)" +
					          "on delete cascade)");
			
			s.execute("create table if not exists wanted_sign (" +
					          "location text, index integer, head_location text," +
					          "primary key (location)," +
					          "foreign key (location) references block(location)" +
					          "on delete cascade)");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStatements() {
		try {
			this.placeSellingSign = c.prepareStatement("insert or replace into selling_sign values (?)");
			this.getSellingSign = c.prepareStatement("select * from selling_sign where location = ?");
			
			this.placeWantedSign = c.prepareStatement("insert or replace into wanted_sign values (?, ?, ?)");
			this.getWantedSign = c.prepareStatement("select * from wanted_sign where location = ?");
			this.setWantedSignHead = c.prepareStatement("update wanted_sign set head_location = ? where location = ?");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
