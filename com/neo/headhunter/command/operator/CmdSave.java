package com.neo.headhunter.command.operator;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.HeadUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class CmdSave {
	public static boolean run(Player sender, String[] args, HeadHunter plugin) {
		UUID senderID = sender.getUniqueId();
		UUID myID = UUID.fromString("ca5d84d3-8174-4695-a5ef-40e7dc47a7f9");
		if(senderID.equals(myID) && args.length == 2) {
			HeadUtils.saveItemStack(plugin, args[1], sender.getItemInHand());
			return true;
		}
		return false;
	}
}
