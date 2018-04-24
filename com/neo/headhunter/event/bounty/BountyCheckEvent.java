package com.neo.headhunter.event.bounty;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BountyCheckEvent extends BountyEvent {
	public BountyCheckEvent(Player hunter, OfflinePlayer target) {
		super(hunter, target);
	}
}
