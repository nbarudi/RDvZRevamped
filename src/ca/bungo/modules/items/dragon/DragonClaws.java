package ca.bungo.modules.items.dragon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.TeleportUtil;
import ca.bungo.util.backend.player.PlayerData;

public class DragonClaws extends CustomItem {

	public DragonClaws(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "Dragon Claws";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Claws");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Left-Click to let out a loud roar slowing and weakening nearby dwarves!");
		lore.add(ChatColor.YELLOW + "Right-Click a dwarf to catch them in your claws!");
		meta.setLore(lore);
		this.setItemMeta(meta);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, ()->{
			
			if(!pl.currentRound.isGameStarted())
				return;
			
			if(pl.currentRound.getDragonUUID() == "" || pl.currentRound.getGrappledUUID() == "")
				return;
			
			
			Player player = Bukkit.getPlayer(UUID.fromString(pl.currentRound.getDragonUUID()));
			Player grappled = Bukkit.getPlayer(UUID.fromString(pl.currentRound.getGrappledUUID()));
			
			if(player == null || grappled == null)
				return;
			
			Location loc = player.getLocation();
			Vector direc = player.getLocation().getDirection();
			loc.add(direc);
			
			TeleportUtil.teleport(grappled, loc);
		}, 20, 2);
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		
		if(this.onCooldown(player, "DragonTimer"))
			return;
		
		event.setCancelled(true);
		if(event.getAction().equals(Action.LEFT_CLICK_AIR)) {
			
			this.giveCooldown(player, "DragonTimer", 2);
			
			Location loc = player.getLocation();
			
			loc.getWorld().playSound(loc, Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
			
			for(Player o : Bukkit.getOnlinePlayers()) {
				PlayerData data = pl.currentRound.getPlayerData(o.getName());
				if(!data.isDwarf)
					return;
				
				//I know that i could just do if(o == player) but I would still rather compare the UUID strings instead
				if(o.getUniqueId().toString().equals(player.getUniqueId().toString()))
					return;
				
				double distance = o.getLocation().distance(loc);
				
				//Are they within 100 blocks of the dragon?
				if(distance <= 100) {
					o.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2));
					o.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
					o.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1));
					o.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 0));
				}
			}
			
		}
	}
	
	@EventHandler
	public void onEntityClick(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		
		if(this.onCooldown(player, "DragonGrab"))
			return;
		
		if(!(event.getRightClicked() instanceof Player))
			return;
		
		event.setCancelled(true);
		//Changing the dragon and grappled user.
		
		if(!pl.currentRound.getGrappledUUID().equals(""))
			return;
		
		pl.currentRound.setGrappledUUID(event.getRightClicked().getUniqueId().toString());
		pl.currentRound.setDragonUUID(player.getUniqueId().toString());
		
		player.sendMessage(ChatColor.AQUA + "You have grappled " + event.getRightClicked().getName());
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, (float) 0.5);
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, ()->{
			player.sendMessage(ChatColor.AQUA + "You have dropped " + event.getRightClicked().getName());
			pl.currentRound.setGrappledUUID("");
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1, (float) 0.3); 
			giveCooldown(player, "DragonGrab", 3);
		}, 100);
		
	}

}
