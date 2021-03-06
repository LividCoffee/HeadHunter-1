package com.neo.headhunter.event.bounty;

import org.bukkit.OfflinePlayer;

public class BountyRemoveEvent extends BountyEvent {
	private double amount;
	
	public BountyRemoveEvent(OfflinePlayer hunter, OfflinePlayer target, double amount) {
		super(hunter.getPlayer(), target);
		
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
