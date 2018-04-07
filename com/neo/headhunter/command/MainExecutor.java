package com.neo.headhunter.command;

import com.neo.headhunter.HeadHunter;
import com.neo.headhunter.command.operator.CmdSave;
import com.neo.headhunter.command.sub.CmdSellhead;
import com.neo.headhunter.command.sub.world.CmdWorldAdd;
import com.neo.headhunter.command.sub.world.CmdWorldRemove;
import com.neo.headhunter.util.message.Control;
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
						sender.sendMessage(Control.PLAYERS_ONLY.error());
						return false;
					}
					return CmdSellhead.run((Player) sender, args, plugin.getEconomy());
				case "addworld":
					return CmdWorldAdd.run(sender, args, plugin.getLandManager());
				case "removeworld":
					return CmdWorldRemove.run(sender, args, plugin.getLandManager());
					/*
				case "bounty":
					if(args.length >= 2) {
						switch(args[1]) {
						case "add":
							CmdBountyAdd.run(
									p, args,
									plugin.getEconomy(),
									plugin.getBountyManager()
							);
							break;
						case "remove":
							CmdBountyRemove.run(
									p, args,
									plugin.getEconomy(),
									plugin.getBountyManager()
							);
							break;
						case "check":
							CmdBountyCheck.run(
									p, args,
									plugin.getBountyManager()
							);
							break;
						case "list":
							CmdBountyList.run(
									p, args,
									plugin.getBountyManager(),
									plugin.getWaitress()
							);
							break;
						default:
							p.sendMessage(Message.CMD.x() + Message.CMD_BOUNTY.x());
							break;
						}
					}
					else
						p.sendMessage(Message.CMD.x() + Message.CMD_BOUNTY.x());
					break;
				case "reload":
					CmdReload.run(p, plugin);
					break;
					*/
				case "save":
					if(!(sender instanceof Player)) {
						sender.sendMessage(Control.PLAYERS_ONLY.error());
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
			if(!(sender instanceof Player)) {
				sender.sendMessage(Control.PLAYERS_ONLY.error());
				return false;
			}
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
		if(sender instanceof Player) {
			//TODO send player-only commands
		}
		//TODO
	}
}
