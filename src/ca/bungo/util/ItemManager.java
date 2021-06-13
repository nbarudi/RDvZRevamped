package ca.bungo.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.main.RDvZ;
import ca.bungo.modules.CustomItem;

public class ItemManager {
	
	public static ItemStack createItem(Material type, String name) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material type, Short data, String name) {
		ItemStack item = new ItemStack(type, 1, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material type, String name, String ...args) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		
		List<String> lore = new ArrayList<String>();
		for(String s : args) {
			lore.add(s);
		}
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material type, Short data, String name, String ...args) {
		ItemStack item = new ItemStack(type, 1, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		
		List<String> lore = new ArrayList<String>();
		for(String s : args) {
			lore.add(s);
		}
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material type, String name, List<String> lore) {
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(Material type, Short data, String name, List<String> lore) {
		ItemStack item = new ItemStack(type, 1, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack findCustomItem(String name) {
		for(CustomItem item : RDvZ.customItems) {
			if(item.name.equalsIgnoreCase(name))
				return item;
		}
		return null;
	}

}
