package com.neo.headhunter.command.operator;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.util.HeadUtils;
import com.neo.headhunter.util.Utils;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class CmdSave {
	public static boolean run(Player sender, String[] args, HeadHunter plugin) {
		UUID senderID = sender.getUniqueId();
		UUID myID = UUID.fromString(Utils.MY_UUID);
		if(senderID.equals(myID) && args.length == 2) {
			HeadUtils.saveItemStack(plugin, args[1], sender.getItemInHand());
			return true;
		}
		return false;
	}
}
