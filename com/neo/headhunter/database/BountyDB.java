package com.neo.headhunter.database;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.mysqlite.DBStatement;
import com.neo.headhunter.database.mysqlite.Database;
import com.neo.headhunter.database.util.DBClause;
import com.neo.headhunter.util.general.Duplet;
import com.neo.headhunter.util.general.MappedList;
import com.neo.headhunter.util.PlayerUtils;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BountyDB extends Database {
	private boolean headUpdateRequired;
	
    public BountyDB(HeadHunter plugin, File dbfile) {
        super(plugin, dbfile);
        this.headUpdateRequired = true;
        createTable(
                T_Individual.NAME,
                T_Individual.C_HUNTER + ", " + T_Individual.C_TARGET,
                T_Individual.C_HUNTER + " TEXT NOT NULL",
		        T_Individual.C_TARGET + " TEXT NOT NULL",
		        T_Individual.C_AMOUNT + " REAL NOT NULL DEFAULT 0"
        );
    }
	
	/**
	 * Gets the hunter's monetary bounty on the target.
	 * @param hunter The hunter associated with this bounty
	 * @param target The target associated with this bounty
	 * @return The double value of the target's bounty
	 */
	public double getBounty(OfflinePlayer hunter, OfflinePlayer target) {
    	DBStatement dbs = new DBStatement(DBClause.SELECT, T_Individual.NAME);
    	dbs.setColumns(T_Individual.C_AMOUNT);
    	dbs.setWhere(
    			T_Individual.C_HUNTER + " = " + hunter.getUniqueId().toString() +
				" AND " +
				T_Individual.C_TARGET + " = " + target.getUniqueId().toString()
	    );
    	double result = 0;
	    ResultSet rs = query(dbs.toString());
	    
	    try {
		    if (rs.next())
			    result = rs.getDouble(T_Individual.C_AMOUNT);
	    } catch(SQLException ex) {
	    	ex.printStackTrace();
	    }
    	return result;
    }
	
	/**
	 * Gets the sum of all bounties which have this target.
	 * @param target The target associated with these bounties
	 * @return The double sum total of all target's bounties
	 */
	public double getTotalBounty(OfflinePlayer target) {
	    DBStatement dbs = new DBStatement(DBClause.SELECT, T_Individual.NAME);
	    dbs.setColumns(T_Individual.C_AMOUNT);
	    dbs.setWhere(
			    T_Individual.C_TARGET + " = " + target.getUniqueId().toString()
	    );
	    ResultSet rs = query(dbs.toString());
	    
	    double result = 0;
	    try {
		    if (rs.next())
			    result = rs.getDouble(T_Individual.C_AMOUNT);
	    } catch(SQLException ex) {
		    ex.printStackTrace();
	    }
	    return result;
    }
	
	/**
	 * Gets the hunter who has placed the highest bounty total on this target.
	 * @param target The target associated with this bounty
	 * @return The hunter who has placed the highest bounty total on the specified target
	 */
	public OfflinePlayer getGodfather(OfflinePlayer target) {
    	DBStatement dbs = new DBStatement(DBClause.SELECT, T_Individual.NAME);
    	dbs.setColumns(T_Individual.C_HUNTER);
	    dbs.setWhere(
			    T_Individual.C_TARGET + " = " + target.getUniqueId().toString()
	    );
	    dbs.setOrderBy(T_Individual.C_AMOUNT + " DESC");
	    dbs.setLimit(1);
	    ResultSet rs = query(dbs.toString());
	    
	    try {
	    	if(rs.next())
			    return PlayerUtils.getPlayer(UUID.fromString(rs.getString(T_Individual.C_HUNTER)));
	    } catch(SQLException ex) {
	    	ex.printStackTrace();
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
        DBStatement dbs = new DBStatement(DBClause.INSERT_REPLACE, T_Individual.NAME);
        dbs.setColumns(
        		T_Individual.C_HUNTER,
		        T_Individual.C_TARGET,
		        T_Individual.C_AMOUNT
        );
        dbs.setValues(
		        hunter.getUniqueId().toString(),
		        target.getUniqueId().toString(),
		        T_Individual.C_AMOUNT + " + " + String.valueOf(amount)
        );
        update(dbs.toString());
    }
	
	/**
	 * Removes all bounties which target the specified player.
	 * @param target The target associated with these bounties
	 * @return A list of duplets, each containing the UUID of a hunter and the bounty set by that hunter
	 */
	public List<Duplet<UUID, Double>> removeBounty(OfflinePlayer target) {
    	DBStatement dbs = new DBStatement(DBClause.SELECT, T_Individual.NAME);
    	dbs.setColumns(
    			T_Individual.C_HUNTER,
    			T_Individual.C_AMOUNT
	    );
    	dbs.setWhere(
			    T_Individual.C_TARGET + " = " + target.getUniqueId().toString()
	    );
    	ResultSet rs = query(dbs.toString());
    	dbs.setOperation(DBClause.DELETE);
    	update(dbs.toString());
    	
	    UUID hunter;
	    double amount;
    	List<Duplet<UUID, Double>> result = new ArrayList<>();
    	try {
		    while (rs.next()) {
			    hunter = UUID.fromString(rs.getString(T_Individual.C_HUNTER));
			    amount = rs.getDouble(T_Individual.C_AMOUNT);
			    result.add(new Duplet<>(hunter, amount));
		    }
	    } catch(SQLException ex) {
    		ex.printStackTrace();
	    }
	    return result;
    }
	
	/**
	 * Removes a hunter's entire bounty from a target.
	 * @param hunter The hunter associated with this bounty
	 * @param target The target associated with this bounty
	 * @return The amount this bounty was worth before removal
	 */
	public double removeBounty(OfflinePlayer hunter, OfflinePlayer target) {
		DBStatement dbs = new DBStatement(DBClause.SELECT, T_Individual.NAME);
		dbs.setColumns(T_Individual.C_AMOUNT);
		dbs.setWhere(
				T_Individual.C_HUNTER + " = " + hunter.getUniqueId().toString() +
				" AND " +
				T_Individual.C_TARGET + " = " + target.getUniqueId().toString()
		);
		ResultSet rs = query(dbs.toString());
		dbs.setOperation(DBClause.DELETE);
		update(dbs.toString());
	
	    double result = 0;
	    try {
		    if (rs.next())
			    result = rs.getDouble(T_Individual.C_AMOUNT);
	    } catch(SQLException ex) {
		    ex.printStackTrace();
	    }
	    return result;
    }
	
	/**
	 * Removes a certain amount from a hunter's bounty on a target
	 * @param hunter The hunter associated with this bounty
	 * @param target The target associated with this bounty
	 * @param amount The amount to be removed from this bounty
	 * @return The amount remaining on the bounty after removal
	 */
	public double removeBounty(OfflinePlayer hunter, OfflinePlayer target, double amount) {
		if(amount < 0) {
			removeBounty(hunter, target);
			return 0;
		}
		double currentBounty = getBounty(hunter, target);
		if(currentBounty == 0)
			return ERR_EXIST;
		if(amount > currentBounty)
			return ERR_REMOVE_AMOUNT;
		double newBounty = currentBounty - amount;
		setBounty(hunter, target, newBounty);
		return newBounty;
    }
	
	/**
	 * A list of target-bounty pairs, sorted from highest to lowest total bounties
	 * @param fromPosition The starting position on the list (the first element is 1)
	 * @param toPosition The ending position on the list
	 * @return An ordered list of target-bounty pairs, from greatest to least
	 */
	public List<Duplet<UUID, Double>> getTopBounties(int fromPosition, int toPosition) {
		assert(fromPosition >= 1);
		assert(toPosition > fromPosition);
		return getSortedBounties().getListRegion(fromPosition - 1, toPosition - 1);
    }
	
	public MappedList<UUID, Double> getSortedBounties() {
		DBStatement dbs = new DBStatement(DBClause.SELECT, T_Individual.NAME);
		dbs.setColumns("*");
		ResultSet rs = query(dbs.toString());
		
		MappedList<UUID, Double> targetTotals = new MappedList<>(true);
		UUID target;
		double amount;
		try {
			if(rs.next()) {
				target = UUID.fromString(rs.getString(T_Individual.C_TARGET));
				amount = rs.getDouble(T_Individual.C_AMOUNT);
				if(targetTotals.contains(target))
					targetTotals.put(target, (targetTotals.get(target) + amount), false);
				else
					targetTotals.put(target, amount, false);
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
		targetTotals.sort();
		return targetTotals;
	}
	
	private void setBounty(OfflinePlayer hunter, OfflinePlayer target, double amount) {
		DBStatement dbs = new DBStatement(DBClause.UPDATE, T_Individual.NAME);
		dbs.setAssignments(T_Individual.C_AMOUNT + " = " + amount);
		dbs.setWhere(
				T_Individual.C_HUNTER + " = " + hunter.getUniqueId().toString() +
				" AND " +
				T_Individual.C_TARGET + " = " + target.getUniqueId().toString()
		);
		update(dbs.toString());
	}
	
	public boolean isHeadUpdateRequired() {
		return headUpdateRequired;
	}
	
	public void setHeadUpdateRequired(boolean headUpdateRequired) {
		this.headUpdateRequired = headUpdateRequired;
	}
	
	//Table layout for consistency
    private static final class T_Individual {
    	private static final String NAME = "individual";
    	
	    private static final String C_HUNTER = "hunter";
	    private static final String C_TARGET = "target";
	    private static final String C_AMOUNT = "amount";
    }
    
    public static final int ERR_EXIST = -1;
	public static final int ERR_REMOVE_AMOUNT = -2;
}
