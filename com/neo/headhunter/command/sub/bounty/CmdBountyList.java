package com.neo.headhunter.command.sub.bounty;

import com.neo.headhunter.database.BountyRegister;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.Utils;
import com.neo.headhunter.util.general.Duplet;
import com.neo.headhunter.util.general.MappedList;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.message.Usage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class CmdBountyList {
	private static final String[] P = {"hunter.admin", "hunter.use", "hunter.list", "hunter.bounty.list"};
	
	public static boolean run(CommandSender sender, String[] args, BountyRegister bountyRegister) {
		if(PlayerUtils.hasAnyPermissions(sender, P)) {
			int pageIndex = 0;
			switch(args.length) {
			case 2:
				break;
			case 3:
				if(!Utils.isInteger(args[2])) {
					sender.sendMessage(Message.BOUNTIES_NO_PAGE.f());
					return false;
				}
				pageIndex = Integer.valueOf(args[2]) - 1;
				break;
			default:
				sender.sendMessage(Usage.CMD_BOUNTY_LIST.usage());
				return false;
			}
			MappedList<UUID, Double> sortedBounties = bountyRegister.getSortedBounties();
			if(sortedBounties.size() == 0) {
				sender.sendMessage(Message.NO_BOUNTIES.f());
				return true;
			}
			if(pageIndex < 0 || (pageIndex * 9) >= sortedBounties.size()) {
				sender.sendMessage(Message.BOUNTIES_NO_PAGE.f());
				return false;
			}
			int index = pageIndex * 9;
			int endIndex = index + 9;
			sender.sendMessage(Message.BOUNTIES_HEADER.f(pageIndex + 1, ((sortedBounties.size() - 1) / 9) + 1));
			while(index < endIndex) {
				Duplet<UUID, Double> entry = sortedBounties.get(index);
				if(entry != null) {
					String hunterName = (sender instanceof Player) ? sender.getName() : "Console";
					String targetName = PlayerUtils.getPlayer(entry.getT()).getName();
					sender.sendMessage(Message.BOUNTY_TOTAL.f(hunterName, targetName, entry.getU(), 1));
				}
				index++;
			}
			return true;
		} else sender.sendMessage(Message.NO_PERMS.f());
		return false;
	}
}
