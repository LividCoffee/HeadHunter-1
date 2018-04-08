package com.neo.headhunter.command.sub.bounty;

import com.neo.headhunter.database.BountyRegister;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.message.Usage;
import org.bukkit.entity.Player;

public final class CmdBountyList {
	private static final String[] P = {"hunter.admin", "hunter.use", "hunter.list", "hunter.bounty.list"};
	
	public static boolean run(Player p, String[] args, BountyRegister bountyRegister) {
		if(PlayerUtils.hasAnyPermissions(p, P)) {
			int pageIndex = 0;
			switch(args.length) {
			case 2:
				break;
			case 3:
				if(!Utils.isInteger(args[2])) {
					return false;
				}
				break;
			default:
				p.sendMessage(Usage.CMD_BOUNTY_LIST.usage());
				return false;
			}
			//TODO
		} else p.sendMessage(Message.NO_PERMS.f());
		return false;
	}
}
