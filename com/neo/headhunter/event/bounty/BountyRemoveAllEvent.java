package com.neo.headhunter.event.bounty;

import org.bukkit.OfflinePlayer;

public class BountyRemoveAllEvent extends BountyEvent {
	private final double amount;
	
	public BountyRemoveAllEvent(OfflinePlayer hunter, OfflinePlayer target, double amount) {
		super(hunter.getPlayer(), target);
		
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}
}
