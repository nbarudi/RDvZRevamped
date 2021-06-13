package ca.bungo.cmds.admin.arguments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;

public class WorldArgument extends AdminArgument {

	public WorldArgument(RDvZ pl, String name) {
		super(pl, name);
		this.desc = "Teleport to or List active worlds";
		this.usage = this.usage + " <WorldName>";
		this.requiresPlayer = true;
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		if(args.length == 1 || args[1].equalsIgnoreCase("list")) {
			player.sendMessage(ChatColor.AQUA + "World List: ");
			for(World world : Bukkit.getWorlds()) {
				player.sendMessage(ChatColor.GREEN + "- " + world.getName());
			}
			return;
		}
		
		if(args.length > 2) {
			player.sendMessage(ChatColor.RED + "Invalid usage: " + this.usage);
			return;
		}
		
		World world = Bukkit.getWorld(args[1]);
		if(world == null) {
			player.sendMessage(ChatColor.RED + "Could not find world " + args[1] + "!");
			return;
		}
		
		player.teleport(world.getSpawnLocation());
		player.sendMessage(ChatColor.YELLOW + "Teleporting to world " + world.getName());
		
	}

}
