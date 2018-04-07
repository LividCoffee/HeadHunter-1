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
 * Usage: /hunter removeworld [world]
 */
public final class CmdWorldRemove {
    private static final String[] P = {"hunter.admin", "hunter.world", "hunter.world.remove"};

    public static boolean run(CommandSender sender, String[] args, LandManager landManager) {
        if(PlayerUtils.hasAnyPermissions(sender, P)) {
            World w = null;
            switch(args.length) {
                case 1:
                    if(sender instanceof Player)
                        w = ((Player) sender).getWorld();
                    else
                        sender.sendMessage(Control.PLAYERS_ONLY.error("/hunter removeworld"));
                    break;
                case 2:
                    w = Bukkit.getWorld(args[1]);
                    if(w == null)
                        sender.sendMessage(Control.WORLD_NULL.error(args[1]));
                    break;
                default:
                    sender.sendMessage(Usage.CMD_REMOVE.usage());
                    sender.sendMessage(Usage.HELP_REMOVE.subtext());
                    break;
            }
            if(w != null) {
                if(landManager.removeWorld(w)) {
	                sender.sendMessage(Control.WORLD_REMOVED.success(w.getName()));
	                return true;
                }
                else
                    sender.sendMessage(Control.WORLD_INVALID.error(w.getName()));
            }
        } else sender.sendMessage(Message.NO_PERMS.f());
        return false;
    }
}
