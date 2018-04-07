package com.neo.headhunter.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HeadSellEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean cancelled;
	
	private final Player hunter;
	private boolean inventory;
	private boolean fromSign;
	
	public HeadSellEvent(Player hunter, boolean inventory, boolean fromSign) {
		this.cancelled = false;
		
		this.hunter = hunter;
		this.inventory = inventory;
		this.fromSign = fromSign;
	}
	
	public Player getHunter() {
		return hunter;
	}
	
	public boolean isInventory() {
		return inventory;
	}
	
	public void setInventory(boolean inventory) {
		this.inventory = inventory;
	}
	
	public boolean isFromSign() {
		return fromSign;
	}
	
	public void setFromSign(boolean fromSign) {
		this.fromSign = fromSign;
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
