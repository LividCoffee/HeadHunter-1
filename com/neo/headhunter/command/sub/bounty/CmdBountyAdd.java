package com.neo.headhunter.command.sub.bounty;

import com.neo.headhunter.database.BountyDB;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.config.Settings;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.message.Usage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class CmdBountyAdd {
	private static final String[] P = {"hunter.admin", "hunter.use", "hunter.bounty", "hunter.bounty.add"};
	
	public static boolean run(Player p, String[] args, Economy economy, BountyDB bountyDB) {
		if(PlayerUtils.hasAnyPermissions(p, P)) {
			if(Settings.isHoardMode()) {
				p.sendMessage(Message.HOARD_MODE.f());
				return false;
			}
			OfflinePlayer target;
			double amount;
			switch(args.length) {
			case 4:
				if(!Utils.isNumeric(args[3])) {
					p.sendMessage(Message.AMOUNT_INVALID.f());
					return false;
				}
				amount = Double.parseDouble(args[3]);
				if(amount <= 0) {
					p.sendMessage(Message.AMOUNT_INVALID.f());
					return false;
				}
				target = PlayerUtils.getPlayer(args[2]);
				if(target == null || !PlayerUtils.hasPlayedBefore(target)) {
					p.sendMessage(Message.PLAYER_INVALID.f(p.getName(), args[2], amount, 1));
					return false;
				}
				if(target.getName().equals(p.getName())) {
					p.sendMessage(Message.SELF_TARGET.f());
					return false;
				}
				break;
			default:
				p.sendMessage(Usage.CMD_BOUNTY_ADD.usage());
				return false;
			}
			if(economy.getBalance(p) < amount) {
				p.sendMessage(Message.BOUNTY_ADD_ERR.f());
				return false;
			}
			if(amount < Settings.getBounty_minimum()) {
				p.sendMessage(Message.BOUNTY_LOW.f());
				return false;
			}
			economy.withdrawPlayer(p, amount);
			bountyDB.addBounty(p, target, amount);
			
			String msg = Message.BOUNTY_ADDED.f(p.getName(), target.getName(), amount, 1);
			if(Settings.isBounty_notify()) {
				if(Settings.isBounty_broadcast())
					PlayerUtils.sendBroadcast(msg);
				else
					p.sendMessage(msg);
			}
			if(Settings.isBounty_console())
				Bukkit.getConsoleSender().sendMessage(msg);
		} else p.sendMessage(Message.NO_PERMS.f());
		return false;
	}
}
