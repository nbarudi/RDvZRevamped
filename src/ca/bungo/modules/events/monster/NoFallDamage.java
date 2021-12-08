package ca.bungo.modules.events.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class NoFallDamage implements Listener {
	
	RDvZ pl;
	
	public NoFallDamage(RDvZ pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(!event.getEntityType().equals(EntityType.PLAYER))
			return;
		Player player = (Player) event.getEntity();
		PlayerData pData = pl.currentRound.getPlayerData(player.getName());
		if(event.getCause().equals(DamageCause.FALL) && !pData.isDwarf && pl.currentRound.isGameStarted())
			event.setCancelled(true);
	}

}
