package com.neo.headhunter.command.sub.bounty;

import com.neo.headhunter.database.BountyRegister;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.message.Usage;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public final class CmdBountyCheck {
	private static final String[] P = {"hunter.admin", "hunter.use", "hunter.bounty", "hunter.bounty.check"};
	
	public static boolean run(Player p, String[] args, BountyRegister bountyRegister) {
		if(PlayerUtils.hasAnyPermissions(p, P)) {
			OfflinePlayer target;
			switch(args.length) {
			case 3:
				break;
			default:
				p.sendMessage(Usage.CMD_BOUNTY_CHECK.usage());
				return false;
			}
			target = PlayerUtils.getPlayer(args[2]);
			if(target == null || !PlayerUtils.hasPlayedBefore(target)) {
				p.sendMessage(Message.PLAYER_INVALID.f(p.getName(), args[2], 0, 1));
				return false;
			}
			String pName = p.getName(), tName = target.getName();
			p.sendMessage(Message.BOUNTY_TOTAL.f(pName, tName, bountyRegister.getTotalBounty(target), 1));
			p.sendMessage(Message.BOUNTY_PERSONAL.f(pName, tName, bountyRegister.getBounty(p, target), 1));
			return true;
		} else p.sendMessage(Message.NO_PERMS.f());
		return false;
	}
}
