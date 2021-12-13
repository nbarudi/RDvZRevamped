package ca.bungo.modules.items.dragon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;

public class DragonBreath extends CustomItem {

	private int task = 0;
	int loop = 0;
	public DragonBreath(RDvZ pl, Material material) {
		super(pl, material, (short)1);
		this.name = "Dragon Breath";
		this.canDrop = false;
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Dragon Breath");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_RED + "Left-Click to rain fire on the dwarves!");
		lore.add(ChatColor.YELLOW + "Right-Click to shoot fireballs at the dwarves!");
		meta.setLore(lore);
		this.setItemMeta(meta);
	}

	@SuppressWarnings("deprecation")
	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		
		if(onCooldown(player, "DragonBreath") || onCooldown(player, "DragonFireball"))
			return;
		
		event.setCancelled(true);
		if(event.getAction().equals(Action.LEFT_CLICK_AIR)) {
			task = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, ()->{
				if(loop == 25) {
					endTask();
					giveCooldown(player, "DragonBreath", 3.5);
					loop = 0;
					return;
				}
				Location loc = player.getLocation();
				
				Vector add = loc.getDirection();
				add.setX(add.getX() * 2);
				add.setZ(add.getZ() * 2);
				
				Location fbloc = loc.add(add);
				
				
				FallingBlock fb = player.getWorld().spawnFallingBlock(fbloc, Material.FIRE, (byte) 0);
				fb.setDropItem(false);
				fb.setHurtEntities(true);
				
				
				Sound sound = Sound.BLOCK_PISTON_CONTRACT;
				loc.getWorld().playSound(loc, sound, 1, (float) 0.5);
				
				Vector vec = player.getLocation().getDirection();
				fb.setVelocity(vec);
				loop++;
			}, 0, 2);
		}else if(event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			giveCooldown(player, "DragonFireball", 0.2);
			
			Location loc = player.getLocation();
			
			Vector dir = loc.getDirection();
			dir.setX(dir.getX() * 3);
			dir.setZ(dir.getZ() * 3);

			loc.add(dir);
			
			loc.setY(loc.getY() + 2);
			
			
			Vector vec = player.getLocation().getDirection();

			
			
			Fireball ent = (Fireball) player.getWorld().spawnEntity(loc, EntityType.FIREBALL);
			ent.setVelocity(vec.multiply(1.5));
			ent.setBounce(false);
			ent.setYield(5);
			
			loc.getWorld().spawnParticle(Particle.FLAME, ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(), 100);
			loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, ent.getLocation().getX(), ent.getLocation().getY(), ent.getLocation().getZ(), 100);
			loc.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1, 1);
		}
		
	}
	
	private void endTask() {
		Bukkit.getScheduler().cancelTask(task);
	}

}
