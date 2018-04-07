package com.neo.headhunter.listener;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.HeadDB;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public final class ListenerHead implements Listener {
	private HeadDB headDB;
	
	public ListenerHead(HeadHunter plugin) {
		this.headDB = plugin.getHeadDB();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.isCancelled())
			return;
		ItemStack inHand = event.getItemInHand();
		if(inHand != null && inHand.getType() == Material.SKULL_ITEM)
			headDB.placeHead(event.getBlock().getLocation(), inHand);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled())
			return;
		Player p = event.getPlayer();
		Location loc = event.getBlock().getLocation();
		ItemStack head = headDB.getHead(loc);
		if(p.getGameMode() != GameMode.CREATIVE)
			loc.getWorld().dropItemNaturally(loc, head);
		headDB.breakHead(loc);
	}
}
