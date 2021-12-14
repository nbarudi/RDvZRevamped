package ca.bungo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class DamageHandler implements Listener {
	
	RDvZ pl;
	
	public DamageHandler(RDvZ pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.getEntity();
		
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		
		
		//Godemode Handler
		if(data.hasGodMode)
			event.setCancelled(true);
		
		//Dragon Damage Negation
		if(pl.currentRound.getDragonUUID() == "")
			return;
		//Dragon warrior should have the same immunities the Dragon does.. AKA: People die to their own fireballs 90% of the time and this will stop that from happening
		else if(player.getUniqueId().toString().equals(pl.currentRound.getDragonUUID()) || data.getCurrentHero().getName().equals("Dragon Warrior")) {
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
				event.setDamage(0);
				event.setCancelled(true);
			}
			
			if(event.getCause().equals(DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				event.setDamage(0);
				event.setCancelled(true);
			}
			
			if(event.getCause().equals(DamageCause.LAVA) || event.getCause().equals(DamageCause.SUFFOCATION)) {
				event.setDamage(0);
				event.setCancelled(true);
			}
		}
	}

}
