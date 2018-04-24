package com.neo.headhunter.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BountyCheckEvent extends BountyEvent {
	public BountyCheckEvent(Player hunter, OfflinePlayer target) {
		super(hunter, target);
	}
}
