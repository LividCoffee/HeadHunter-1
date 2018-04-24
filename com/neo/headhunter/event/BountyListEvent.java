package com.neo.headhunter.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BountyListEvent extends BountyEvent {
	private int pageRequested;
	
	public BountyListEvent(Player hunter, OfflinePlayer target, int pageRequested) {
		super(hunter, target);
		
		this.pageRequested = pageRequested;
	}
	
	public int getPageRequested() {
		return pageRequested;
	}
	
	public void setPageRequested(int pageRequested) {
		this.pageRequested = pageRequested;
	}
}
