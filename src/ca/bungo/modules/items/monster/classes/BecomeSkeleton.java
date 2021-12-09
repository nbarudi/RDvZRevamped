package ca.bungo.modules.items.monster.classes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SpawnEggMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;
import ca.bungo.util.backend.player.PlayerData.DwarfClass;
import ca.bungo.util.backend.player.PlayerData.MonsterClass;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

public class BecomeSkeleton extends CustomItem {

	public BecomeSkeleton(RDvZ pl) {
		super(pl, Material.MONSTER_EGG);
		SpawnEggMeta meta = (SpawnEggMeta) this.getItemMeta();
		meta.setSpawnedType(EntityType.SKELETON);
		meta.setDisplayName(ChatColor.GRAY + "Become Skeleton");
		this.setItemMeta(meta);
		this.canDrop = false;
		this.name = "Become Skeleton";
	}

	@Override
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if(!verifyItem(item, this))
			return;
		if(onCooldown(player, "BecomeMonster"))
			return;
		giveCooldown(player, "BecomeMonster", 1);
		
		event.setCancelled(true);
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			this.onRightclickItem(player);
			return;
		}
		
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		data.setDwarfClass(DwarfClass.NONE);
		data.isDwarf = false;
		data.setMonsterClass(MonsterClass.SKELETON);
		player.sendMessage(ChatColor.GRAY + "You have become a Skeleton!");
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		
		//Disugise
		MobDisguise md = new MobDisguise(DisguiseType.SKELETON);
		md.setEntity(player);
		md.startDisguise();
				
		//Creating Items
				
		ItemStack helm = new ItemStack(Material.CHAINMAIL_HELMET);
		ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack legg = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemStack boot = new ItemStack(Material.CHAINMAIL_BOOTS);
				
		ItemStack bow = new ItemStack(Material.BOW);
		ItemStack arrow = new ItemStack(Material.ARROW);
				
		ItemStack sword = new ItemStack(Material.WOOD_SWORD);
				
		ItemStack vines = new ItemStack(Material.VINE, 32);
				
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 16);
				
		bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		bow.getItemMeta().setUnbreakable(true);
				
		sword.getItemMeta().setUnbreakable(true);
				
		//Adding to Inventory
		final PlayerInventory inv = player.getInventory();
				
		inv.setHelmet(helm);
		inv.setChestplate(chest);
		inv.setLeggings(legg);
		inv.setBoots(boot);
				
		inv.addItem(sword);
		inv.addItem(bow);
		inv.addItem(arrow);
		inv.addItem(vines);
				
		inv.addItem(steak);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () ->{
			if(data.claimedClasses)
				player.getInventory().setItem(35, ItemManager.findCustomItem("Seppuku"));
		}, 20);
		
		Bukkit.dispatchCommand(player, "warp " + data.getKey() + " monster");
	}
	
	

}
