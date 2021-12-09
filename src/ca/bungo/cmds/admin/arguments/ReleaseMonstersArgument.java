package ca.bungo.cmds.admin.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;
import ca.bungo.util.ChatManager;
import ca.bungo.util.backend.player.PlayerData;

public class ReleaseMonstersArgument extends AdminArgument{

	public ReleaseMonstersArgument(RDvZ pl, String name) {
		super(pl, name);
		this.requiresPlayer = true;
		this.desc = "Release Monsters";
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.getInventory().clear();
			PlayerData d = pl.currentRound.getPlayerData(p.getName());
			d.isDwarf = true;
		}

		
		pl.currentRound.setMonstersReleased(true);
		Bukkit.broadcastMessage(ChatManager.formatColor("&7[&aRDvZ&7] &4The monsters have been released!"));
		Bukkit.broadcastMessage(ChatManager.formatColor("&7[&aRDvZ&7] &4The monsters have been released!"));
		Bukkit.broadcastMessage(ChatManager.formatColor("&7[&aRDvZ&7] &4The monsters have been released!"));
		Bukkit.broadcastMessage(ChatManager.formatColor("&7[&aRDvZ&7] &4The monsters have been released!"));
		
		player.getLocation().getWorld().strikeLightning(player.getLocation().add(0,100,0));
		
		player.sendMessage("Released Monsters!");
	}

}
