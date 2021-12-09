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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;
import ca.bungo.util.backend.player.PlayerData.DwarfClass;
import ca.bungo.util.backend.player.PlayerData.MonsterClass;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

public class BecomeZombie extends CustomItem {

	public BecomeZombie(RDvZ pl) {
		super(pl, Material.MONSTER_EGG);
		SpawnEggMeta meta = (SpawnEggMeta) this.getItemMeta();
		meta.setSpawnedType(EntityType.ZOMBIE);
		meta.setDisplayName(ChatColor.DARK_GREEN + "Become Zombie");
		this.setItemMeta(meta);
		this.canDrop = false;
		this.name = "Become Zombie";
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
		data.setMonsterClass(MonsterClass.ZOMBIE);
		player.sendMessage(ChatColor.DARK_GREEN + "You have become a Zombie!");
		player.getInventory().clear();
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		
		MobDisguise md = new MobDisguise(DisguiseType.ZOMBIE);
		md.setEntity(player);
		md.startDisguise();
		
		//Creating Items
		ItemStack ihelm = new ItemStack(Material.IRON_HELMET);
		ItemStack ichest = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack ilegg = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack iboot = new ItemStack(Material.IRON_BOOTS);
		ItemStack isword = new ItemStack(Material.IRON_SWORD);
		ItemStack hpot = new ItemStack(Material.SPLASH_POTION, 2);
		ItemStack steak = new ItemStack(Material.COOKED_BEEF, 16);
		
		ihelm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ichest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ilegg.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		iboot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		isword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
		
		PotionMeta pm = (PotionMeta)hpot.getItemMeta();
		pm.setDisplayName(ChatColor.LIGHT_PURPLE + "Health Potion");
		pm.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1), true);
		hpot.setItemMeta(pm);
		
		//Adding to Inventory
		final PlayerInventory inv = player.getInventory();
		
		inv.setHelmet(ihelm);
		inv.setChestplate(ichest);
		inv.setLeggings(ilegg);
		inv.setBoots(iboot);
		
		inv.addItem(isword);
		
		inv.addItem(hpot);
		
		inv.addItem(steak);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () ->{
			if(data.claimedClasses)
				player.getInventory().setItem(35, ItemManager.findCustomItem("Seppuku"));
		}, 20);
		
		Bukkit.dispatchCommand(player, "warp " + data.getKey() + " monster" );
	}
	
	

}
