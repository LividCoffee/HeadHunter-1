package com.neo.headhunter.event.bounty;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BountyRemoveEvent extends BountyEvent {
	private double amount;
	
	public BountyRemoveEvent(Player hunter, OfflinePlayer target, double amount) {
		super(hunter, target);
		
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
