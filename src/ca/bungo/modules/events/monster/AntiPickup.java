package ca.bungo.modules.events.monster;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class AntiPickup implements Listener {
	
	RDvZ pl;
	
	public AntiPickup(RDvZ pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if(!(e.getEntity() instanceof Player)) 
			return;
		
		Player player = (Player) e.getEntity();
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		
		if(!data.isDwarf && pl.currentRound.isGameStarted())
			e.setCancelled(true);
	}

}
