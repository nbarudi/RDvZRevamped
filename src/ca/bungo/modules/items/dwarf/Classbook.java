package ca.bungo.modules.items.dwarf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;

public class Classbook extends CustomItem {

	public Classbook(RDvZ pl, Material material) {
		super(pl, material);
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Run Class");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Click to run your classes ability!");
		meta.setLore(lore);
		setItemMeta(meta);
		this.canDrop = false;
		this.name = "Run Class";
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this.getItemMeta().getDisplayName()))
			return;
		if(onCooldown(player, "ClassBook")) {
			event.setCancelled(true);
			return;
		}
		
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			onRightclickItem(player);
			event.setCancelled(true);
			return;
		}
		
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		
		Random rnd = new Random();
		
		switch(data.getDwarfClass()) {
		case BUILDER:
			int b_stacks = rnd.nextInt(4) + 1;			
			int b_type = rnd.nextInt(4);
			
			ItemStack b_item = new ItemStack(Material.GLOWSTONE, 3);
			switch(b_type) {
			case 0:
				b_item = new ItemStack(Material.SMOOTH_BRICK, 64);
				break;
			case 1:
				b_item = new ItemStack(Material.SMOOTH_BRICK, 64, (short) 1);
				break;
			case 2:
				b_item = new ItemStack(Material.SMOOTH_BRICK, 64, (short) 2);
				break;
			case 3:
				b_item = new ItemStack(Material.SMOOTH_BRICK, 64, (short) 3);
				break;
			}
			
			Location loc = player.getLocation();
			loc.setY(loc.getY() + 1);
			Vector direction = player.getLocation().getDirection();
			loc.add(direction);
			for(int i = 0; i < b_stacks; i++) {
				player.getWorld().dropItemNaturally(loc, b_item);
			}
			player.getWorld().dropItemNaturally(loc, new ItemStack(Material.TORCH, rnd.nextInt(6)));
			
			giveCooldown(player, "ClassBook", this.getConfigurableCooldown("BuilderClassCooldown", 15));
			break;
		case BAKER:
			ItemStack b_cost = new ItemStack(Material.CLAY_BRICK);
			if(player.getInventory().containsAtLeast(b_cost, 4)) {
				ItemStack tcost = new ItemStack(Material.CLAY_BRICK, 4);
				player.getInventory().removeItem(tcost);
			}else {
				player.sendMessage("§3You are missing required items! (4 Clay Bricks)");
				return;
			}
			
			int cakeChance = rnd.nextInt(101);
			int breadChance = rnd.nextInt(101);
			
			Location b_loc = player.getLocation();
			b_loc.setY(b_loc.getY() + 1);
			Vector b_direction = player.getLocation().getDirection();
			b_loc.add(b_direction);
			
			if(cakeChance > 25) {
				ItemStack newItem = ItemManager.findCustomItem("Cake Spawner");
				newItem.setAmount(rnd.nextInt(2) + 1);
				player.getWorld().dropItemNaturally(b_loc, newItem);
			}
			
			if(breadChance > 50) {
				player.getWorld().dropItemNaturally(b_loc, new ItemStack(Material.BREAD, rnd.nextInt(5)));
			}
			
			
			player.getWorld().dropItemNaturally(b_loc, new ItemStack(Material.CLAY_BALL, rnd.nextInt(16) + 4));
			player.getWorld().dropItemNaturally(b_loc, new ItemStack(Material.COAL, rnd.nextInt(7)));
			giveCooldown(player, "ClassBook", this.getConfigurableCooldown("BakerClassCooldown", 10));
			break;
		case SMITH:
			ItemStack cost = new ItemStack(Material.WATCH);
			if(player.getInventory().containsAtLeast(cost, 4)) {
				ItemStack tcost = new ItemStack(Material.WATCH, 4);
				player.getInventory().removeItem(tcost);
			}else {
				player.sendMessage("§3You are missing required items! (4 Golden Clocks)");
				return;
			}
			
			int swordChance = rnd.nextInt(101);
			int pickChance = rnd.nextInt(101);
			int shovelChance = rnd.nextInt(101);
			int bowChance = rnd.nextInt(101);
			int randomArrows = rnd.nextInt(65) + 1;
			int randomGold = rnd.nextInt(5);
			int randomRedstone = rnd.nextInt(5) + 1;
			int randomCoal = rnd.nextInt(5) + 1;
			
			Location s_loc = player.getLocation();
			s_loc.setY(s_loc.getY() + 1);
			Vector s_direction = player.getLocation().getDirection();
			s_loc.add(s_direction);
			
			if(swordChance > 50) {
				player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.DIAMOND_SWORD));
			}
			
			if(bowChance > 50) {
				player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.BOW));
			}
			
			if(pickChance > 50) {
				player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.DIAMOND_PICKAXE));
			}
			
			if(shovelChance > 50) {
				player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.DIAMOND_SPADE));
			}
			
			player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.ARROW, randomArrows));
			player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.GOLD_ORE, randomGold));
			player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.REDSTONE_ORE, randomRedstone));
			player.getWorld().dropItemNaturally(s_loc, new ItemStack(Material.COAL, randomCoal));
			giveCooldown(player, "ClassBook", this.getConfigurableCooldown("BlacksmithClassCooldown", 15));
			break;
		case ALCHEMIST:
			ItemStack a_cost = new ItemStack(Material.POTION);
			PotionMeta pm = (PotionMeta) a_cost.getItemMeta();
			pm.setBasePotionData(new PotionData(PotionType.MUNDANE));
			a_cost.setItemMeta(pm);
			
			if(player.getInventory().containsAtLeast(a_cost, 3)) {
				ItemStack tcost = new ItemStack(Material.POTION, 3);
				PotionMeta tpm = (PotionMeta) a_cost.getItemMeta();
				pm.setBasePotionData(new PotionData(PotionType.MUNDANE));
				tcost.setItemMeta(tpm);
				player.getInventory().removeItem(tcost);
			}else {
				player.sendMessage("§3You are missing required items! (3 Mundane Potions [Created with 1 redstone in a brewing stand])");
				return;
			}
			
			int healpotChance = rnd.nextInt(101);
			int speedpotChance = rnd.nextInt(101);
			int fresistChance = rnd.nextInt(101);
			int strengthChance = rnd.nextInt(101);

			
			Location a_loc = player.getLocation();
			a_loc.setY(a_loc.getY() + 1);
			Vector a_direction = player.getLocation().getDirection();
			a_loc.add(a_direction);
			
			if(healpotChance > 25) {
				//player.getWorld().dropItemNaturally(loc, RDvZ.is.healPotion);
				player.getWorld().dropItemNaturally(a_loc, ItemManager.findCustomItem("Health Potion"));
			}
			
			if(speedpotChance > 50) {
				//player.getWorld().dropItemNaturally(loc, RDvZ.is.speedPotion);
				player.getWorld().dropItemNaturally(a_loc, ItemManager.findCustomItem("Speed Potion"));
			}
			
			if(fresistChance > 75) {
				//player.getWorld().dropItemNaturally(loc, RDvZ.is.fresistPotion);
				player.getWorld().dropItemNaturally(a_loc, ItemManager.findCustomItem("Fire Potion"));
			}
			
			if(strengthChance > 88) {
				//player.getWorld().dropItemNaturally(loc, RDvZ.is.strengthPotion);
				player.getWorld().dropItemNaturally(a_loc, ItemManager.findCustomItem("Strength Potion"));
			}
			
			
			player.getWorld().dropItemNaturally(a_loc, new ItemStack(Material.BONE, 10));
			player.getWorld().dropItemNaturally(a_loc, new ItemStack(Material.BLAZE_POWDER, rnd.nextInt(2)));
			player.getWorld().dropItemNaturally(a_loc, new ItemStack(Material.GLASS, rnd.nextInt(17)));
			giveCooldown(player, "ClassBook", this.getConfigurableCooldown("AlchemistClassCooldown", 15));
			break;
		case TAILOR:
			ItemStack t_cost = new ItemStack(Material.INK_SACK);
			t_cost.setDurability((short)14);
			
			if(player.getInventory().containsAtLeast(t_cost, 20)) {
				ItemStack tcost = new ItemStack(Material.INK_SACK, 20);
				tcost.setDurability((short)14);
				player.getInventory().removeItem(tcost);
			}else {
				player.sendMessage("§3You are missing required items! (20 Orange Dye)");
				return;
			}
			
			int helmChance = rnd.nextInt(101);
			int chestChance = rnd.nextInt(101);
			int legChance = rnd.nextInt(101);
			int bootsChance = rnd.nextInt(101);

			
			Location t_loc = player.getLocation();
			t_loc.setY(t_loc.getY() + 1);
			Vector t_direction = player.getLocation().getDirection();
			t_loc.add(t_direction);
			
			if(helmChance > 50) {
				player.getWorld().dropItemNaturally(t_loc, new ItemStack(Material.DIAMOND_HELMET));
			}
			
			if(chestChance > 50) {
				player.getWorld().dropItemNaturally(t_loc, new ItemStack(Material.DIAMOND_CHESTPLATE));
			}
			
			if(legChance > 50) {
				player.getWorld().dropItemNaturally(t_loc, new ItemStack(Material.DIAMOND_LEGGINGS));
			}
			
			if(bootsChance > 50) {
				player.getWorld().dropItemNaturally(t_loc, new ItemStack(Material.DIAMOND_BOOTS));
			}
			
			player.getWorld().dropItemNaturally(t_loc, new ItemStack(Material.GOLD_ORE, 10));
			player.getWorld().dropItemNaturally(t_loc, new ItemStack(Material.BONE, rnd.nextInt(5)));
			
			giveCooldown(player, "ClassBook", this.getConfigurableCooldown("TailorrClassCooldown", 15));
			break;
		default:
			break;
		}

	}

}
