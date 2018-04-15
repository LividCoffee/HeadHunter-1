package com.neo.headhunter.command.sub.world;

import com.neo.headhunter.mgmt.LandManager;
import com.neo.headhunter.util.PlayerUtils;
import com.neo.headhunter.util.message.Control;
import com.neo.headhunter.util.message.Message;
import com.neo.headhunter.util.message.Usage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Usage: /hunter addworld [world]
 */
public final class CmdWorldAdd {
    private static final String[] P = {"hunter.admin", "hunter.world", "hunter.world.add"};

    public static boolean run(CommandSender sender, String[] args, LandManager landManager) {
        if(PlayerUtils.hasAnyPermissions(sender, P)) {
            World w = null;
            switch(args.length) {
                case 1:
                	if(sender instanceof Player)
		                w = ((Player) sender).getWorld();
	                else
                		sender.sendMessage(Control.PLAYERS_ONLY.error("/hunter addworld"));
                    break;
                case 2:
                    w = Bukkit.getWorld(args[1]);
                    if(w == null)
                        sender.sendMessage(Control.WORLD_NULL.error(args[1]));
                    break;
                default:
                    sender.sendMessage(Usage.CMD_ADD.usage());
                    break;
            }
            if(w != null) {
                if(landManager.addWorld(w)) {
	                sender.sendMessage(Control.WORLD_ADDED.success(w.getName()));
	                return true;
                }
                else
                    sender.sendMessage(Control.WORLD_EXISTS.error(w.getName()));
            }
        } else sender.sendMessage(Message.NO_PERMS.f());
        return false;
    }
}
