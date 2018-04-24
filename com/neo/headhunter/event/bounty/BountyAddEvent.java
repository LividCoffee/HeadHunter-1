package com.neo.headhunter.event.bounty;

import org.bukkit.OfflinePlayer;

public class BountyAddEvent extends BountyEvent {
	private double amount;
	
	public BountyAddEvent(OfflinePlayer hunter, OfflinePlayer target, double amount) {
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
