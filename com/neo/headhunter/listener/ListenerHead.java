package com.neo.headhunter.listener;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.database.BlockRegister;
import com.neo.headhunter.database.HeadRegister;
import com.neo.headhunter.util.general.Triplet;
import com.neo.headhunter.util.item.BlockType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public final class ListenerHead implements Listener {
	private BlockRegister blockRegister;
	private HeadRegister headRegister;
	
	public ListenerHead(HeadHunter plugin) {
		this.blockRegister = plugin.getHHDB().getBlockRegister();
		this.headRegister = plugin.getHHDB().getHeadRegister();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.isCancelled())
			return;
		ItemStack inHand = event.getItemInHand();
		if(inHand != null && inHand.getType() == Material.SKULL_ITEM) {
			blockRegister.placeBlock(event.getBlock().getLocation(), event.getPlayer(), BlockType.HEAD);
			headRegister.placeHead(event.getBlock().getLocation(), inHand);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled())
			return;
		Player p = event.getPlayer();
		Location loc = event.getBlock().getLocation();
		Triplet<Location, OfflinePlayer, BlockType> block = blockRegister.getBlock(loc);
		if(block != null && block.getV() == BlockType.HEAD) {
			ItemStack head = headRegister.getHead(loc);
			if (p.getGameMode() != GameMode.CREATIVE)
				loc.getWorld().dropItemNaturally(loc, head);
			blockRegister.breakBlock(loc);
		}
	}
}
