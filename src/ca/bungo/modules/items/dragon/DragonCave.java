package ca.bungo.modules.items.dragon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.backend.player.PlayerData;
import me.libraryaddict.disguise.DisguiseAPI;

public class DragonCave extends CustomItem {

	public DragonCave(RDvZ pl, Material material) {
		super(pl, material);
		this.name = "Dragon Cave";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_RED + "Dragon Cave");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_RED + "Left-Click to create your cave.");
		lore.add(ChatColor.YELLOW + "Right-Click to 'kill' the dragon.");
		lore.add(ChatColor.YELLOW + "Note: Closest player to the dragon when it dies becomes the Dragon Warrior!");
		meta.setLore(lore);
		this.setItemMeta(meta);
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		
		if(onCooldown(player, "Cave"))
			return;
		giveCooldown(player, "Cave", 1);
		
		event.setCancelled(true);
		if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			
			//This ability requires WorldEdit to function because changing large sections of blocks is harder then I thought
			Bukkit.dispatchCommand(player, "/sphere air 15");
	        Bukkit.dispatchCommand(player, "/cyl netherrack 15");
	        Bukkit.dispatchCommand(player, "/hcyl 10%lava,90%air 15 3");
	        Bukkit.dispatchCommand(player, "minecraft:tp @p ~ ~2 ~");
	        Bukkit.dispatchCommand(player, "minecraft:setblock ~ ~-2 ~ minecraft:netherrack");
		}else if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			//Spawning dead dragon
			Bukkit.dispatchCommand(player, "summon ender_dragon ~ ~ ~ {DragonPhase:9}");
			player.setGameMode(GameMode.CREATIVE);
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.hidePlayer(pl, player);
			}
			
			Player dwarrior = null;
			double curDist = 9999999;
			for(Player d : Bukkit.getOnlinePlayers()) {
				if(d.getName().equals(player.getName()))
					continue;
				if(curDist > d.getLocation().distance(player.getLocation())) {
					if(d.isDead())
						continue;
					curDist = d.getLocation().distance(player.getLocation());
					dwarrior = d;
				}
			}
			
			final Player dw = dwarrior;
			DisguiseAPI.undisguiseToAll(player); //UD the dragon
			dwarrior.sendMessage("§6You have become the Dragon Warrior!");
			PlayerData dwData = pl.currentRound.getPlayerData(dw.getName());
			dwData.hasGodMode = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(pl, ()->{
				//Remove GodMode!
				dwData.hasGodMode = false;
				dw.sendMessage("§cYour invincibility has run out!");
			}, 1200);
			dwarrior.sendMessage("§aYou have been granted 60 seconds of invincibility!");
			pl.hm.getHeroByName("Dragon Warrior").setPlayer(dw, false);
			
		}
		
		

	}

}
