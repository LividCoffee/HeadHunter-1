package com.neo.headhunter.event.bounty;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BountyAddEvent extends BountyEvent {
	private double amount;
	
	public BountyAddEvent(Player hunter, OfflinePlayer target, double amount) {
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
