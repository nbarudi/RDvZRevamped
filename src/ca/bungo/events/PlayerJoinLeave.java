package ca.bungo.events;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.bungo.main.RDvZ;
import ca.bungo.util.ChatManager;
import ca.bungo.util.backend.player.PlayerData;

public class PlayerJoinLeave implements Listener {
	
	RDvZ pl;
	
	public PlayerJoinLeave(RDvZ pl) {
		this.pl = pl;
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		
		HashMap<String, Object> punishments = data.getPunishments();
		if((Boolean)punishments.get("isBanned")) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatManager.formatColor("&7[&aRDvZ&7]\n &cYou have been banned from this server!\n"));
			sb.append(ChatManager.formatColor("&4A Host or Moderator has banned you from this server!\n"));
			sb.append(ChatManager.formatColor("&9If you believe this is a mistake...\n"));
			sb.append(ChatManager.formatColor("&9Or you believe you were wrongfully banned...\n"));
			sb.append(ChatManager.formatColor("&9Join the discord, and contact a staff member!\n"));
			sb.append(ChatManager.formatColor("&bhttps://discord.gg/YSV2z7V"));
			player.kickPlayer(sb.toString());
			return;
		}
		
		if((Integer)punishments.get("currentRoundBans") > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(ChatManager.formatColor("&7[&aRDvZ&7]\n&cYou have a Round Ban on this server!\n"));
			sb.append(ChatManager.formatColor("&4This means you will be unable to play this round of RDvZ!\n"));
			sb.append(ChatManager.formatColor("&9If you believe this is a mistake...\n"));
			sb.append(ChatManager.formatColor("&9Or you believe you were wrongfully banned...\n"));
			sb.append(ChatManager.formatColor("&9Join the discord, and contact a staff member!\n"));
			sb.append(ChatManager.formatColor("&bhttps://discord.gg/YSV2z7V"));
			player.sendMessage(sb.toString());
		}
		
		if(pl.currentRound.isGameStarted()) {
			if(!data.isDwarf)
				player.setHealth(0);
			return;
		}
		
		Bukkit.dispatchCommand(player, "warp " + data.getKey() + " spawn" );
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy : hh:mm:ss");
		String date = sdf.format(new Date());
		pl.fm.getConfig("punishments.yml").get().set("Players." + player.getUniqueId().toString() + ".lastJoined", date);
		pl.fm.getConfig("punishments.yml").save();
	}

}
