package ca.bungo.util.backend.player.heros.dragonWarrior;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.heros.Hero;

public class DragonWarrior extends Hero {

	public DragonWarrior(RDvZ pl) {
		super(pl, "Dragon Warrior", "the Dragon Warrior");
		this.setColor(ChatColor.GOLD);
		this.setMaxMana(1500);
	}
	
	@Override
	public void setPlayer(Player toHero, boolean isForced) {
		super.setPlayer(toHero, isForced);
		
		PlayerInventory inv = toHero.getInventory();
		
		//This was just taken from the old code since items are the same.
		ItemStack dhelm = new ItemStack(Material.GOLD_HELMET);
		ItemMeta dhim = dhelm.getItemMeta();
		dhim.setDisplayName("§6Warriors Helmet");
		dhim.setUnbreakable(true);
		dhim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		dhelm.setItemMeta(dhim);
		dhelm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		
		inv.setHelmet(dhelm);
		
		ItemStack dchest = new ItemStack(Material.GOLD_CHESTPLATE);
		ItemMeta dcim = dhelm.getItemMeta();
		dcim.setDisplayName("§6Warriors Chestplate");
		dcim.setUnbreakable(true);
		dcim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		dchest.setItemMeta(dcim);
		dchest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		
		inv.setChestplate(dchest);
		
		ItemStack dlegg = new ItemStack(Material.GOLD_LEGGINGS);
		ItemMeta dlim = dhelm.getItemMeta();
		dlim.setDisplayName("§6Warriors Leggings");
		dlim.setUnbreakable(true);
		dhim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		dlegg.setItemMeta(dlim);
		dlegg.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		
		inv.setLeggings(dlegg);
		
		ItemStack dboot = new ItemStack(Material.GOLD_BOOTS);
		ItemMeta dbim = dhelm.getItemMeta();
		dbim.setDisplayName("§6Warriors Boots");
		dbim.setUnbreakable(true);
		dbim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		dboot.setItemMeta(dbim);
		dboot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		
		inv.setBoots(dboot);
		
		ItemStack dsword = new ItemStack(Material.GOLD_SWORD);
		ItemMeta dsim = dsword.getItemMeta();
		dsim.setDisplayName("§6Warriors Sword");
		dsim.setUnbreakable(true);
		dsim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		dsword.setItemMeta(dsim);
		dsword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		dsword.addEnchantment(Enchantment.FIRE_ASPECT, 1);
		
		ItemStack dbow = new ItemStack(Material.BOW);
		ItemMeta dboim = dbow.getItemMeta();
		dboim.setDisplayName("§6Dragon Skin Bow");
		dboim.setUnbreakable(true);
		dboim.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		dbow.setItemMeta(dboim);
		dbow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		dbow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
		dbow.addEnchantment(Enchantment.ARROW_FIRE, 1);
		
		inv.addItem(dsword);
		inv.addItem(dbow);
		inv.addItem(new ItemStack(Material.ARROW, 1));
		
		inv.addItem(ItemManager.findCustomItem("DWarriorFireball"));
		inv.addItem(ItemManager.findCustomItem("Health Potion"));
		inv.addItem(ItemManager.findCustomItem("Speed Potion"));
		inv.addItem(ItemManager.findCustomItem("Fire Potion"));
		inv.addItem(ItemManager.findCustomItem("Strength Potion"));
		
	}

}
