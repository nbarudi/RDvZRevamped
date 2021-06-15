package ca.bungo.util.backend.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.bungo.main.RDvZ;

public class PlayerManager implements Listener {
	
	RDvZ instance;
	public PlayerManager(RDvZ instance) {
		this.instance = instance;
	}
	
	//Creating player data
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		
		if(instance.currentRound.getPlayerData(player.getName()) != null) {
			instance.currentRound.getPlayerData(player.getName()).setOnline(true);
			return;
		}
		//Create player data
		PlayerData data = new PlayerData(instance, player.getName());
		instance.currentRound.addPlayerData(data);
		event.setJoinMessage(ChatColor.LIGHT_PURPLE + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.LIGHT_PURPLE + " to the server!");
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		PlayerData data = instance.currentRound.getPlayerData(player.getName());
		data.setOnline(false);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(instance.currentRound.getPlayerData(player.getName()) != null) {
			instance.currentRound.getPlayerData(player.getName()).setOnline(false);
			event.setQuitMessage(ChatColor.YELLOW + player.getName() + " has left the server!");
			return;
		}
	}
	
	//Handling Player Deaths
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerData data = instance.currentRound.getPlayerData(player.getName());
		
		if(data.isDwarf) {
			data.isDwarf = false;
			
			//Setting up Death Information
			DeathData dData = new DeathData();
			dData.setDclass(data.getDwarfClass());
			dData.setDeathInv(player.getInventory().getContents());
			dData.setDeathLoc(player.getLocation());
			dData.setMana(data.getMana());
			data.setDeathData(dData);
		}
		data.claimedClasses = false;
	}

}
