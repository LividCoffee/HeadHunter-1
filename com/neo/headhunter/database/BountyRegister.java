package com.neo.headhunter.database;

import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.general.MappedList;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.UUID;

public final class BountyRegister {
	private Connection c;
	
	private PreparedStatement getBounty, getTotalBounty, getGodfather;
	private PreparedStatement addBounty;
	private PreparedStatement removeBountyTarget, removeBountySingle, removeBountyAmount;
	private PreparedStatement setBounty;
	private PreparedStatement getSortedBounties;
	
	private boolean headUpdateRequired;
	
	BountyRegister(Connection c) {
		this.c = c;
		
		createTables();
		prepareStatements();
		
		this.headUpdateRequired = true;
	}
	
	/**
	 * Gets the hunter's monetary bounty on the target.
	 * @param hunter The hunter associated with this bounty
	 * @param target The target associated with this bounty
	 * @return The double value of the target's bounty
	 */
	public double getBounty(OfflinePlayer hunter, OfflinePlayer target) {
		try {
			getBounty.setString(1, hunter.getUniqueId().toString());
			getBounty.setString(2, target.getUniqueId().toString());
			ResultSet rs = getBounty.executeQuery();
			if(rs.next())
				return rs.getDouble(1);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Gets the sum of all bounties which have this target.
	 * @param target The target associated with these bounties
	 * @return The double sum total of all target's bounties
	 */
	public double getTotalBounty(OfflinePlayer target) {
		try {
			getTotalBounty.setString(1, target.getUniqueId().toString());
			ResultSet rs = getTotalBounty.executeQuery();
			if(rs.next())
				return rs.getDouble(1);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Gets the hunter who has placed the highest bounty total on this target.
	 * @param target The target associated with this bounty
	 * @return The hunter who has placed the highest bounty total on the specified target
	 */
	public OfflinePlayer getGodfather(OfflinePlayer target) {
		try {
			getGodfather.setString(1, target.getUniqueId().toString());
			ResultSet rs = getGodfather.executeQuery();
			if(rs.next())
				return PlayerUtils.getPlayer(UUID.fromString(rs.getString(1)));
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Adds the specified amount to the hunter's bounty on the target, creating it if necessary.
	 * @param hunter The player adding the bounty
	 * @param target The player being targeted by the hunter
	 * @param amount The amount of money the bounty addition is worth
	 */
	public void addBounty(OfflinePlayer hunter, OfflinePlayer target, double amount) {
		try {
			addBounty.setString(1, hunter.getUniqueId().toString());
			addBounty.setString(2, target.getUniqueId().toString());
			addBounty.setDouble(3, getBounty(hunter, target) + amount);
			addBounty.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes all bounties which target the specified player.
	 * @param target The target associated with these bounties
	 * @return The total amount these bounties were worth before removal
	 */
	public double removeBounty(OfflinePlayer target) {
		try {
			double previous = getTotalBounty(target);
			removeBountyTarget.setString(1, target.getUniqueId().toString());
			removeBountyTarget.executeUpdate();
			return previous;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Removes a hunter's entire bounty from a target.
	 * @param hunter The hunter associated with this bounty
	 * @param target The target associated with this bounty
	 * @return The amount this bounty was worth before removal
	 */
	public double removeBounty(OfflinePlayer hunter, OfflinePlayer target) {
		try {
			double previous = getBounty(hunter, target);
			removeBountySingle.setString(1, hunter.getUniqueId().toString());
			removeBountySingle.setString(2, target.getUniqueId().toString());
			removeBountySingle.executeUpdate();
			return previous;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Removes a certain amount from a hunter's bounty on a target
	 * @param hunter The hunter associated with this bounty
	 * @param target The target associated with this bounty
	 * @param amount The amount to be removed from this bounty
	 * @return The amount remaining on the bounty after removal
	 */
	public double removeBounty(OfflinePlayer hunter, OfflinePlayer target, double amount) {
		try {
			if(amount < 0)
				removeBounty(hunter, target);
			else {
				double previous = getBounty(hunter, target);
				if(previous == 0)
					return ERR_EXIST;
				if(amount > previous)
					return ERR_REMOVE_AMOUNT;
				removeBountyAmount.setString(1, hunter.getUniqueId().toString());
				removeBountyAmount.setString(2, target.getUniqueId().toString());
				removeBountyAmount.setDouble(3, amount);
				removeBountyAmount.executeUpdate();
				return previous - amount;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setBounty(OfflinePlayer hunter, OfflinePlayer target, double amount) {
		try {
			setBounty.setString(1, hunter.getUniqueId().toString());
			setBounty.setString(2, target.getUniqueId().toString());
			setBounty.setDouble(3, amount);
			setBounty.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public MappedList<UUID, Double> getSortedBounties() {
		MappedList<UUID, Double> result = new MappedList<>(true);
		try {
			ResultSet rs = getSortedBounties.executeQuery();
			while(rs.next()) {
				UUID target = UUID.fromString(rs.getString(1));
				double amount = rs.getDouble(2);
				result.put(target, amount, false);
			}
			result.sort();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void createTables() {
		try {
			Statement s = c.createStatement();
			s.execute(
					"create table if not exists bounty (" +
							"hunter text, target text, amount real," +
							"primary key (hunter, target))"
			);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStatements() {
		try {
			this.getBounty = c.prepareStatement("select amount from bounty where hunter = ? and target = ?");
			this.getTotalBounty = c.prepareStatement("select sum(amount) as amount from bounty where target = ?");
			this.getGodfather = c.prepareStatement("select hunter, max(amount) as amount from bounty where target = ? group by target");
			this.addBounty = c.prepareStatement("insert or replace into bounty values (?, ?, ?)");
			this.removeBountyTarget = c.prepareStatement("delete from bounty where target = ?");
			this.removeBountySingle = c.prepareStatement("delete from bounty where hunter = ? and target = ?");
			this.removeBountyAmount = c.prepareStatement("update bounty set amount = amount - ? where hunter = ? and target = ?");
			this.setBounty = c.prepareStatement("insert or replace into bounty values (?, ?, ?)");
			this.getSortedBounties = c.prepareStatement("select target, sum(amount) as amount from bounty group by target");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isHeadUpdateRequired() {
		return headUpdateRequired;
	}
	
	public void setHeadUpdateRequired(boolean headUpdateRequired) {
		this.headUpdateRequired = headUpdateRequired;
	}
	
	private static final int ERR_EXIST = -1;
	private static final int ERR_REMOVE_AMOUNT = -2;
}
