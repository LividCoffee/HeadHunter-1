package com.neo.headhunter.listener.operator;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;
import java.util.logging.Level;

public final class ListenerEntity implements Listener {
	private HeadHunter plugin;
	
	public ListenerEntity(HeadHunter plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent event) {
		UUID myID = UUID.fromString(Utils.MY_UUID);
		Player p = event.getPlayer();
		if(p.getUniqueId().equals(myID))
			plugin.getLogger().log(Level.INFO, event.getRightClicked().getClass().getName());
	}
}
