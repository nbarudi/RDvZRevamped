package ca.bungo.util.backend.player.heros.dragonWarrior.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.backend.player.PlayerData;

public class DragonFireball extends CustomItem {

	public DragonFireball(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "DWarriorFireball";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Fireball");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Left-Click to throw a fireball at the enemy!");
		meta.setLore(lore);
		this.setItemMeta(meta);
		
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(!verifyItem(this, player.getInventory().getItemInMainHand()))
			return;
			
		if(onCooldown(player, "DWarriorFireball"))
			return;
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		
		if(!data.isHero)
			return;
		
		if(!data.useMana(500)) {
			giveCooldown(player, "DWarriorFireball", 2);
			player.sendMessage(ChatColor.RED + "You do not have enough mana to use this ability!");
			return;
		}
		giveCooldown(player, "DWarriorFireball", 15);
		
		Location loc = player.getLocation();
		
		Vector vec = loc.getDirection();
		vec.setX(vec.getX() * 2);
		vec.setZ(vec.getZ() * 2);
		
		loc.setY(loc.getY() + 1);
		
		loc.add(vec);
		
		Fireball fb = (Fireball) player.getLocation().getWorld().spawnEntity(loc, EntityType.FIREBALL);
		
		fb.setYield(6);
		fb.setIsIncendiary(false);
		fb.setBounce(false);
		
		fb.setVelocity(loc.getDirection());
		player.playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1, 1);
		player.sendMessage(ChatColor.GOLD + "You have shot a fireball!");
		
		
	}

}
