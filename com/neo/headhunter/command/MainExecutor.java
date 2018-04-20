package com.neo.headhunter.command;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.command.operator.CmdReload;
import com.neo.headhunter.command.operator.CmdSave;
import com.neo.headhunter.command.sub.CmdSellhead;
import com.neo.headhunter.command.sub.bounty.CmdBountyAdd;
import com.neo.headhunter.command.sub.bounty.CmdBountyCheck;
import com.neo.headhunter.command.sub.bounty.CmdBountyList;
import com.neo.headhunter.command.sub.bounty.CmdBountyRemove;
import com.neo.headhunter.command.sub.world.CmdWorldAdd;
import com.neo.headhunter.command.sub.world.CmdWorldRemove;
import com.neo.headhunter.util.message.Control;
import com.neo.headhunter.util.message.Usage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MainExecutor implements CommandExecutor {
	private HeadHunter plugin;
	
	public MainExecutor(HeadHunter plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch(label) {
		case "hunter":
			if(args.length == 0) {
				sendHelpMessages(sender);
				return false;
			}
			else {
				switch(args[0]) {
				case "sellhead":
					if(!(sender instanceof Player)) {
						sender.sendMessage(Control.PLAYERS_ONLY.error("sellhead"));
						return false;
					}
					return CmdSellhead.run((Player) sender, args, plugin.getEconomy());
				case "addworld":
					return CmdWorldAdd.run(sender, args, plugin.getHHDB().getWorldRegister());
				case "removeworld":
					return CmdWorldRemove.run(sender, args, plugin.getHHDB().getWorldRegister());
				case "bounty":
					if(args.length >= 2) {
						switch(args[1]) {
						case "add":
							if(!(sender instanceof Player)) {
								sender.sendMessage(Control.PLAYERS_ONLY.error("bounty.add"));
								return false;
							}
							return CmdBountyAdd.run((Player) sender, args, plugin.getEconomy(), plugin.getHHDB().getBountyRegister());
						case "remove":
							if(!(sender instanceof Player)) {
								sender.sendMessage(Control.PLAYERS_ONLY.error("bounty.remove"));
								return false;
							}
							return CmdBountyRemove.run((Player) sender, args, plugin.getEconomy(), plugin.getHHDB().getBountyRegister());
						case "check":
							return CmdBountyCheck.run(sender, args, plugin.getHHDB().getBountyRegister());
						case "list":
							return CmdBountyList.run(sender, args, plugin.getHHDB().getBountyRegister());
						default:
							sender.sendMessage(Usage.CMD_BOUNTY.usage());
							break;
						}
					}
					else {
						sender.sendMessage(Usage.CMD_BOUNTY.usage());
					}
					break;
				case "reload":
					return CmdReload.run(sender, plugin);
				case "save":
					if(!(sender instanceof Player)) {
						sender.sendMessage(Control.PLAYERS_ONLY.error("save"));
						return false;
					}
					return CmdSave.run((Player) sender, args, plugin);
				default:
					sendHelpMessages(sender);
					break;
				}
			}
			break;
		case "sellhead":
		case "bounty":
			StringBuilder newCmd = new StringBuilder("hunter " + label);
			for(String arg : args) {
				newCmd.append(" ");
				newCmd.append(arg);
			}
			((Player) sender).performCommand(newCmd.toString());
			break;
		}
		return false;
	}
	
	private static void sendHelpMessages(CommandSender sender) {
		sender.sendMessage(Usage.CMD_ADD.usage());
		sender.sendMessage(Usage.HELP_ADD.subtext());
		sender.sendMessage(Usage.CMD_REMOVE.usage());
		sender.sendMessage(Usage.HELP_REMOVE.subtext());
		if(sender instanceof Player) {
			sender.sendMessage(Usage.CMD_SELLHEAD.usage());
			sender.sendMessage(Usage.HELP_SELLHEAD.subtext());
			sender.sendMessage(Usage.CMD_BOUNTY_ADD.usage());
			sender.sendMessage(Usage.CMD_BOUNTY_REMOVE.usage());
		}
		sender.sendMessage(Usage.CMD_BOUNTY_CHECK.usage());
		sender.sendMessage(Usage.CMD_BOUNTY_LIST.usage());
		sender.sendMessage(Usage.HELP_BOUNTY.subtext());
	}
}
