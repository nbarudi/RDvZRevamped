package ca.bungo.cmds.admin.arguments;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;

public class RestartArgument extends AdminArgument{
	

	public RestartArgument(RDvZ pl, String name) {
		super(pl, name);
		this.desc = "Restart the world, and round data.";
		this.requiresPlayer = false;
		
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		
		
		World lobby = Bukkit.getWorld("RDvZ_Lobby");
		if(lobby == null) {
			WorldCreator wc = new WorldCreator("RDvZ_Lobby");
			wc.type(WorldType.FLAT);
			wc.generateStructures(false);
			lobby = wc.createWorld();
		}
		
		lobby.setDifficulty(Difficulty.PEACEFUL);
		
		Bukkit.broadcastMessage(ChatColor.YELLOW + "Sending players to lobby world!");
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(lobby.getSpawnLocation());
		}
		
		Bukkit.broadcastMessage(ChatColor.RED + "Please wait while we reset the map...");
		
		Bukkit.unloadWorld(pl.currentRound.worldName, false);
		
		Bukkit.broadcastMessage(ChatColor.RED + "World unloaded...");
		Bukkit.broadcastMessage(ChatColor.RED + "Reloading server...");
		Bukkit.reload();
		Bukkit.broadcastMessage(ChatColor.GREEN + "Reload completed...");
		Bukkit.broadcastMessage(ChatColor.GREEN + "Reset completed!");
		lobby.setDifficulty(Difficulty.PEACEFUL);
		
	}

}
