package ca.bungo.cmds.admin.arguments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class DataArgument extends AdminArgument {

	public DataArgument(RDvZ pl, String name) {
		super(pl, name);
		this.requiresPlayer = false;
		this.usage = "/admin " + name + " <player>";
		this.desc = "Shows player information that the server is using";
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		
		if (args.length != 2) {
			sender.sendMessage(ChatColor.RED + "Invalid Usage: " + this.usage);
			return;
		}
		
		Player target = Bukkit.getPlayer(args[1]);
		PlayerData data;
		if (target == null) {
			String name = args[1];
			data = pl.currentRound.getPlayerData(name);
		}else {
			data = pl.currentRound.getPlayerData(target.getName());
		}
		
		if(data == null) {
			sender.sendMessage(ChatColor.RED + "Could not get players information!");
			return;
		}
		
		sender.sendMessage(ChatColor.AQUA + "Showing data for: " + data.getName());
		
		sender.sendMessage(data.toString());
		return;

	}

}
