package com.neo.headhunter.command.sub;

import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.message.Usage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

/**
 * Usage: /hunter sellhead
 * Alias: /sellhead
 */
public final class CmdSellhead {
    public static final String[] P = {"hunter.admin", "hunter.use", "hunter.sellhead", "hunter.sell"};

    public static boolean run(Player sender, String[] args, Economy economy) {
        if(PlayerUtils.hasAnyPermissions(sender, P)) {
            switch(args.length) {
                case 1:
                	//no argument assignments necessary
                    break;
                default:
                    sender.sendMessage(Usage.CMD_SELLHEAD.usage());
                    return false;
            }
	        return PlayerUtils.sellHeads(economy, sender, sender.isSneaking(), false);
        } else sender.sendMessage(Message.NO_PERMS.f());
        return false;
    }
}
