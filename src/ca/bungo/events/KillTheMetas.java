package ca.bungo.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;
import ca.bungo.util.backend.player.PlayerData.DwarfClass;

public class KillTheMetas implements Listener {
	
	RDvZ pl;
	
	public KillTheMetas(RDvZ pl) {
		this.pl = pl;
	}
	
	static double MAXDISTANCE = 100;
	
	ArrayList<String> messageCooldown = new ArrayList<String>();
	
	@EventHandler
	public void antiMiningAct(PlayerMoveEvent event) {
		
		Player plr = event.getPlayer();
		PlayerData pd = pl.currentRound.getPlayerData(plr.getName());
		
		if(!pd.isDwarf || pd.getDwarfClass().equals(DwarfClass.NONE))
			return;
		
		if(!pl.currentRound.isGameStarted())
			return;
		
		if(plr.isOp())
			return;
		
		
		Location loc = pl.wm.getWarp("dwarf").getLocation();
		
		Location ploc = event.getTo();
		
		double dist = loc.distance(ploc);
		dist = Math.abs(dist);
		if(dist >= MAXDISTANCE) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 99999, 2));
			if(messageCooldown.contains(event.getPlayer().getName()))
				return;
			event.getPlayer().sendMessage("§eThe shrines protection is too week this far way.. The monster aura begins to corrupt you");
			messageCooldown.add(event.getPlayer().getName());
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
				public void run() {
					messageCooldown.remove(event.getPlayer().getName());
				}
			}, 40);
		}else {
			event.getPlayer().removePotionEffect(PotionEffectType.WITHER);
		}
	}
	
	@EventHandler
	public void AntiPlaceBoio(BlockPlaceEvent event) {
	}
	
	
	@EventHandler
	public void AntiAxePvP(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		if(!(event.getDamager() instanceof Player))
			return;
		
		Player damager = (Player)event.getDamager();
		
		//if(damager.isOp())
		//	event.setDamage(99999);
		
		if(damager.getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("axe")) {
			event.setDamage(0);
			damager.sendMessage("§cSorry! Axes are 'a little' too OP for combat! Good Luck!");
			damager.sendMessage("§6You've dealt 0 damage with your axe!");
		}
	}

	
	@EventHandler
	public void screwLava(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(player.isOp())
			return;
		
		if(player.getInventory().getItemInMainHand().getType().equals(Material.LAVA_BUCKET) || player.getInventory().getItemInOffHand().getType().equals(Material.LAVA_BUCKET)) {
			event.setCancelled(true);
			player.sendMessage("§aIf I keep Lava being a Meta... This game will go to *hell*! Get it? Hell... Lava... HaHa I'm funny!");
		}
	}

}
