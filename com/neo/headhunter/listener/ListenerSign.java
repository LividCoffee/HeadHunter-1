package com.neo.headhunter.listener;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.command.sub.CmdSellhead;
import com.neo.headhunter.database.BountyRegister;
import com.neo.headhunter.mgmt.SignManager;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.item.sign.WantedSign;
import com.neo.headhunter.util.message.Control;
import com.neo.headhunter.util.message.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public final class ListenerSign implements Listener {
	private Economy economy;
	private BountyRegister bountyRegister;
	private SignManager signManager;
	
	public ListenerSign(HeadHunter plugin) {
		this.economy = plugin.getEconomy();
		this.bountyRegister = plugin.getHHDB().getBountyRegister();
		this.signManager = plugin.getSignManager();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Block b = event.getClickedBlock();
		if(b == null)
			return;
		BlockState state = b.getState();
		Player p = event.getPlayer();
		Location signLoc;
		if(state instanceof Sign) {
			signLoc = b.getLocation();
			if(signManager.isSellingSign(signLoc)) {
				if(!PlayerUtils.hasAnyPermissions(p, CmdSellhead.P))
					p.sendMessage(Message.NO_PERMS.f());
				else
					PlayerUtils.sellHeads(economy, p, p.isSneaking(), true);
			}
			else if(signManager.isWantedSign(signLoc)) {
				if(!PlayerUtils.hasAnyPermissions(p, "hunter.admin", "hunter.sign", "hunter.sign.wanted"))
					p.sendMessage(Message.NO_PERMS.f());
				else {
					signManager.putSignLink(p, signLoc);
					p.sendMessage(Control.WANTED_OPENED.info());
				}
			}
		}
		else if(state instanceof Skull) {
			signLoc = signManager.getSignLink(p);
			if(signLoc == null)
				return;
			WantedSign wantedSign = signManager.getWantedSign(signLoc);
			if(wantedSign != null) {
				if(!PlayerUtils.hasAnyPermissions(p, "hunter.admin", "hunter.sign", "hunter.sign.wanted"))
					p.sendMessage(Message.NO_PERMS.f());
				else {
					wantedSign.setHeadLocation(b.getLocation());
					signManager.saveWantedSigns();
					p.sendMessage(Control.WANTED_LINKED.success());
					bountyRegister.setHeadUpdateRequired(true);
				}
			}
			signManager.removeSignLink(p);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignChange(SignChangeEvent event) {
		if(event.isCancelled())
			return;
		Player p = event.getPlayer();
		if(event.getLine(0).equalsIgnoreCase("[SellHead]")) {
			if(!PlayerUtils.hasAnyPermissions(p, "hunter.admin", "hunter.sign", "hunter.sign.selling")) {
				p.sendMessage(Message.NO_PERMS.f());
				event.setCancelled(true);
				return;
			}
			signManager.registerSellingSign(p, event.getBlock().getLocation());
			p.sendMessage(Control.SIGN_CREATED.success());
		}
		else if(event.getLine(0).equalsIgnoreCase("[Wanted]") && Utils.isInteger(event.getLine(1))) {
			if(!PlayerUtils.hasAnyPermissions(p, "hunter.admin", "hunter.sign", "hunter.sign.wanted")) {
				p.sendMessage(Message.NO_PERMS.f());
				event.setCancelled(true);
				return;
			}
			signManager.registerWantedSign(p, event.getBlock().getLocation(), Integer.parseInt(event.getLine(1)) - 1);
			p.sendMessage(Control.WANTED_CREATED.success());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled())
			return;
		Block b = event.getBlock();
		if(b == null || !(b.getState() instanceof Sign))
			return;
		Player p = event.getPlayer();
		if(signManager.isSellingSign(b.getLocation())) {
			if(!PlayerUtils.hasAnyPermissions(p, "hunter.admin", "hunter.sign", "hunter.sign.selling")) {
				p.sendMessage(Message.NO_PERMS.f());
				event.setCancelled(true);
				return;
			}
			signManager.unregisterSellingSign(b.getLocation());
			p.sendMessage(Control.SIGN_REMOVED.success());
		}
		else if(signManager.isWantedSign(b.getLocation())) {
			if(!PlayerUtils.hasAnyPermissions(p, "hunter.admin", "hunter.sign", "hunter.sign.wanted")) {
				p.sendMessage(Message.NO_PERMS.f());
				event.setCancelled(true);
				return;
			}
			signManager.unregisterWantedSign(b.getLocation());
			p.sendMessage(Control.WANTED_REMOVED.success());
		}
	}
}
