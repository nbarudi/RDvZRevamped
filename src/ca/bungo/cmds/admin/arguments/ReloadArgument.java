package ca.bungo.cmds.admin.arguments;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;

public class ReloadArgument extends AdminArgument{

	public ReloadArgument(RDvZ pl, String name) {
		super(pl, name);
		this.desc = "Reload the RDvZ config!";
		this.requiresPlayer = false;
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		pl.fm.getConfig("config.yml").reload();
		pl.fm.getConfig("punishments.yml").reload();
		sender.sendMessage(ChatColor.GREEN + "Reloaded config!");
	}
	

}
