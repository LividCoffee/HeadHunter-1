package com.neo.headhunter.listener.support;

import au.com.mineauz.minigames.events.JoinMinigameEvent;
import au.com.mineauz.minigames.events.QuitMinigameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;

public final class ListenerMinigames implements Listener {
	private Set<Player> inGame;
	
	public ListenerMinigames() {
		this.inGame = new HashSet<>();
	}
	
	@EventHandler
	public void onMinigameJoin(JoinMinigameEvent event) {
		if(event.isCancelled())
			return;
		inGame.add(event.getPlayer());
	}
	
	@EventHandler
	public void onMinigameLeave(QuitMinigameEvent event) {
		if(event.isCancelled())
			return;
		inGame.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		inGame.remove(event.getPlayer());
	}
	
	public boolean isInMinigame(Player p) {
		return inGame.contains(p);
	}
}
