package com.neo.headhunter.util;

import com.neo.headhunter.event.head.HeadSellEvent;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.message.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Map;
import java.util.UUID;

public final class PlayerUtils {
	public static boolean sellHeads(Economy economy, Player p, boolean inventory, boolean fromSign) {
		HeadSellEvent hse = new HeadSellEvent(p, inventory, fromSign);
		Bukkit.getPluginManager().callEvent(hse);
		p = hse.getPlayer();
		inventory = hse.isInventory();
		fromSign = hse.isFromSign();
		Map<Integer, ItemStack> sellData = hse.remakeSellData();
		
		if(hse.isCancelled())
			return false;
		if(Settings.isSell_signOnly() && !fromSign) {
			p.sendMessage(Message.SELL_SIGN_ONLY.f());
			return false;
		}
		if(sellData.size() == 0) {
			p.sendMessage(Message.NO_HEADS.f());
			return false;
		}
		
		PlayerInventory inv = p.getInventory();
		double totalValueSold = 0;
		int totalHeadsSold = 0;
		String headDisplay = "";
		if(inventory) {
			for(Map.Entry<Integer, ItemStack> entry : sellData.entrySet()) {
				int sellSlot = entry.getKey();
				ItemStack sellStack = entry.getValue();
				
				totalValueSold += HeadUtils.getStackValue(sellStack);
				totalHeadsSold += sellStack.getAmount();
				
				inv.setItem(sellSlot, new ItemStack(Material.AIR));
			}
		}
		else {
			int sellSlot = hse.getHeldItemSlot();
			ItemStack sellStack = sellData.get(sellSlot);
			
			totalValueSold = HeadUtils.getStackValue(sellStack);
			totalHeadsSold = sellStack.getAmount();
			headDisplay = sellStack.getItemMeta().getDisplayName();
			
			inv.setItem(sellSlot, new ItemStack(Material.AIR));
		}
		
		economy.depositPlayer(p, totalValueSold);
		
		String msg;
		if (totalValueSold > 0) {
			if (inventory) msg = Message.SELL_INVENTORY.f(p.getName(), headDisplay, totalValueSold, totalHeadsSold);
			else msg = Message.SELL_NOTIFY.f(p.getName(), headDisplay, totalValueSold, totalHeadsSold);
		} else msg = Message.SELL_WORTHLESS.f(p.getName(), headDisplay, totalValueSold, totalHeadsSold);
		
		if (Settings.isSell_notify()) {
			if (Settings.isSell_broadcast() && totalValueSold > 0)
				PlayerUtils.sendBroadcast(msg);
			else p.sendMessage(msg);
		}
		if (Settings.isSell_console() && totalValueSold > 0)
			Bukkit.getConsoleSender().sendMessage(msg);
		return true;
	}
	
	public static OfflinePlayer getPlayer(UUID uuid) {
		if(uuid == null)
			return null;
		return Bukkit.getOfflinePlayer(uuid);
	}
	
	public static OfflinePlayer getPlayer(String name) {
		if(name == null || name.isEmpty())
			return null;
		OfflinePlayer result;
		if((result = Bukkit.getPlayerExact(name)) == null)
			return Bukkit.getPlayer(name);
		return result;
	}
	
	public static void sendBroadcast(String msg) {
		for(Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(msg);
	}
	
	public static long getCooldown(Permissible p) {
		for(PermissionAttachmentInfo pai : p.getEffectivePermissions()) {
			String perm = pai.getPermission();
			if (perm.startsWith("hunter.cooldown:")) {
				if(pai.getValue()) {
					String cooldown = perm.replace("hunter.cooldown:", "");
					if (Utils.isInteger(cooldown))
						return Long.valueOf(cooldown);
				}
			}
		}
		return Settings.getDrop_defaultCooldown();
	}
	
	public static boolean hasPlayedBefore(OfflinePlayer p) {
		for(OfflinePlayer offP : Bukkit.getOfflinePlayers())
			if(offP.getUniqueId().equals(p.getUniqueId())) return true;
		for(Player onP : Bukkit.getOnlinePlayers())
			if(onP.getUniqueId().equals(p.getUniqueId())) return true;
		return false;
	}
	
	public static boolean hasAnyPermissions(Permissible p, String... permissions) {
		for(String perm : permissions)
			if(p.hasPermission(perm))
				return true;
		return false;
	}
	
	public static boolean hasExplicitPermission(Permissible p, String permission) {
		if(p == null)
			return false;
		for(PermissionAttachmentInfo pai : p.getEffectivePermissions()) {
			if(pai.getPermission().equalsIgnoreCase(permission) && pai.getValue())
				return true;
		}
		return false;
	}
}
