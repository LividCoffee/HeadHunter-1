package com.neo.headhunter.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public abstract class BountyEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;
	
	private OfflinePlayer target;
	
	public BountyEvent(Player hunter, OfflinePlayer target) {
		super(hunter);
		
		this.target = target;
	}
	
	public OfflinePlayer getTarget() {
		return target;
	}
	
	public void setTarget(OfflinePlayer target) {
		this.target = target;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
