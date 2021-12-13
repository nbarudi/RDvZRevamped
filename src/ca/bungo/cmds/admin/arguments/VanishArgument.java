package ca.bungo.cmds.admin.arguments;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;

public class VanishArgument extends AdminArgument {
	
	private List<String> vanished = new ArrayList<String>();

	public VanishArgument(RDvZ pl, String name) {
		super(pl, name);
		this.desc = "Hide/UnHide yourself from online players";
		this.requiresPlayer = true;
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if(vanished.contains(player.getUniqueId().toString())) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.showPlayer(pl, player);
			}
			vanished.remove(player.getUniqueId().toString());
			player.sendMessage(ChatColor.GRAY + "You are now Visible! Poof!");
		}else {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.hidePlayer(pl, player);
			}
			vanished.add(player.getUniqueId().toString());
			player.sendMessage(ChatColor.GRAY + "You have Vanished! Poof!");
		}
		return;
	}


}
