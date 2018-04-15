package com.neo.headhunter.command.sub.bounty;

import com.neo.headhunter.database.BountyRegister;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.message.Usage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CmdBountyCheck {
	private static final String[] P = {"hunter.admin", "hunter.use", "hunter.bounty", "hunter.bounty.check"};
	
	public static boolean run(CommandSender sender, String[] args, BountyRegister bountyRegister) {
		if(PlayerUtils.hasAnyPermissions(sender, P)) {
			OfflinePlayer target;
			switch(args.length) {
			case 3:
				//no argument assignments necessary
				break;
			default:
				sender.sendMessage(Usage.CMD_BOUNTY_CHECK.usage());
				return false;
			}
			target = PlayerUtils.getPlayer(args[2]);
			if(target == null || !PlayerUtils.hasPlayedBefore(target)) {
				sender.sendMessage(Message.PLAYER_INVALID.f(sender.getName(), args[2], 0, 1));
				return false;
			}
			String pName = sender.getName(), tName = target.getName();
			sender.sendMessage(Message.BOUNTY_TOTAL.f(pName, tName, bountyRegister.getTotalBounty(target), 1));
			if(sender instanceof Player)
				sender.sendMessage(Message.BOUNTY_PERSONAL.f(pName, tName, bountyRegister.getBounty((Player) sender, target), 1));
			return true;
		} else sender.sendMessage(Message.NO_PERMS.f());
		return false;
	}
}
