package ca.bungo.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class ChatHandler implements Listener {
	
	//Just a refrence to the Main Plugin Class incase needed
	RDvZ pl;
	
	public ChatHandler(RDvZ pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		
		event.setCancelled(true);
		
		Bukkit.broadcastMessage(data.getNick() + ChatColor.GRAY + ": " + event.getMessage());
		
	}

}
