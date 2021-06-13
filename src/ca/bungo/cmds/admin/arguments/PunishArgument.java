package ca.bungo.cmds.admin.arguments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;

@SuppressWarnings("deprecation")
public class PunishArgument extends AdminArgument {

	private HashMap<String, Inventory> storage = new HashMap<String, Inventory>();
	
	private int warnBeforeKick = 3;
	private int warnBeforeRBan = 5;
	
	public PunishArgument(RDvZ pl, String name) {
		super(pl, name);
		this.requiresPlayer = true;
		this.desc = "Open the management GUI for a given player";
		this.usage = "/admin " + this.name + " <player>";
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		if(storage.get(player.getName()) != null)
			storage.remove(player.getName());
		
		if(args.length == 1) {
			player.sendMessage(ChatColor.RED + "Invalid usage: " + this.usage);
			return;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
		if(target == null) {
			player.sendMessage(ChatColor.RED + "Couldn't find player: " + args[1]);
			return;
		}
		
		player.sendMessage(ChatColor.YELLOW + "Opening Management Menu For " + target.getName());
		
		Inventory inv = buildInventory(target.getName());
		storage.put(player.getName(), inv);
		player.openInventory(inv);
		return;
	}
	
	public Inventory buildInventory(String target) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Player: " + target);
		
		//Extras
		ItemStack close = ItemManager.createItem(Material.BARRIER, ChatColor.RED + "Close the Inventory");

		ItemStack pInfo = ItemManager.createItem(Material.PAPER, ChatColor.BLUE + "Player Info", getPlayerInfo(target));
		
		//Punishments
		ItemStack warn = ItemManager.createItem(Material.GLOWSTONE, ChatColor.YELLOW + "Warn Player", ChatColor.BLUE + "Gives the player a warning!");
		
		ItemStack roundBan = ItemManager.createItem(Material.INK_SACK, (short) 1, ChatColor.RED + "Round Ban", ChatColor.BLUE + "Ban " + target + " for 1 round!");
		ItemStack roundBan2 = ItemManager.createItem(Material.REDSTONE, ChatColor.RED + "2 Round Bans", ChatColor.BLUE + "Ban " + target + " for 2 round!");
		ItemStack roundBan3 = ItemManager.createItem(Material.CONCRETE, (short) 14, ChatColor.RED + "3 Round Ban", ChatColor.BLUE + "Ban " + target + " for 3 round!");
		ItemStack permaBan = ItemManager.createItem(Material.REDSTONE_BLOCK, ChatColor.RED + "Permanent Ban", ChatColor.BLUE + "Ban " + target + " from the Server!");
		
		ItemStack roundUnBan = ItemManager.createItem(Material.INK_SACK, (short) 10, ChatColor.GREEN + "Remove Round Ban", ChatColor.BLUE + "Remove 1 Round Ban from" + target + "!");
		ItemStack roundUnBan2 = ItemManager.createItem(Material.SLIME_BALL, ChatColor.GREEN + "Remove 2 Round Bans", ChatColor.BLUE + "Remove 2 Round Bans from" + target + "!");
		ItemStack roundUnBanAll = ItemManager.createItem(Material.SLIME_BLOCK, (short) 14, ChatColor.GREEN + "Remove All Round Ban", ChatColor.BLUE + "Remove All Round Bans from" + target + "!");
		ItemStack UnBan = ItemManager.createItem(Material.EMERALD_BLOCK, ChatColor.GREEN + "UnBan", ChatColor.BLUE + "UnBan " + target + " from the Server!");
		
		//Management
		ItemStack Kill = ItemManager.createItem(Material.NETHER_STAR, ChatColor.DARK_RED + "Kill the Player");
		ItemStack Rez = ItemManager.createItem(Material.TOTEM, ChatColor.AQUA + "Resurrect the Player");
		ItemStack GoTo = ItemManager.createItem(Material.ENDER_PEARL, ChatColor.YELLOW + "GoTo the Player");
		ItemStack Bring = ItemManager.createItem(Material.EYE_OF_ENDER, ChatColor.YELLOW + "Bring the Player");
		ItemStack InvSee = ItemManager.createItem(Material.CHEST, ChatColor.YELLOW + "Inventory", ChatColor.BLUE + "Open " + target + "'s inventory!");
		
		//Stuff I'll need:
		/*
		 * Warn a player
		 * Give a player a round ban
		 * Give a player 2 round bans
		 * Give a player 3 round bans
		 * Ban a player from a server
		 * Same as above but opposite
		 * Kill a player
		 * Resurrect a player
		 * Bring a player
		 * GoTo a player
		 * */
		
		
		inv.setItem(4, pInfo); //Top Middle
		
		inv.setItem(10, roundBan);
		inv.setItem(19, roundBan2);
		inv.setItem(28, roundBan3);
		inv.setItem(37, permaBan);
		
		inv.setItem(11, roundUnBan);
		inv.setItem(20, roundUnBan2);
		inv.setItem(29, roundUnBanAll);
		inv.setItem(38, UnBan);
		
		inv.setItem(13, warn);
		inv.setItem(22, Kill);
		inv.setItem(31, Rez);
		inv.setItem(40, InvSee);
		
		inv.setItem(25, GoTo);
		inv.setItem(34, Bring);
	
		inv.setItem(53, close); //Bottom Right
		
		return inv;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Inventory inv = event.getClickedInventory();
		if(inv == null)
			return;
		if(inv.getName().contains("Player:")) {
			
			if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null)
				return;
			
			String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
			String target = inv.getName().substring(10);

			if(itemName.equalsIgnoreCase(ChatColor.RED + "Close the Inventory")) {
				event.getWhoClicked().closeInventory();
				return;
			}
			else if(itemName.equalsIgnoreCase(ChatColor.YELLOW + "Warn Player")) {
				warnPlayer(target);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Warnned " + target + "!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.RED + "Round Ban")) {
				roundBan(target, 1);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Added 1 Round ban to: " + target + "!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.RED + "2 Round Bans")) {
				roundBan(target, 2);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Added 2 Round ban to: " + target + "!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.RED + "3 Round Ban")) {
				roundBan(target, 3);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Added 3 Round bans to: " + target + "!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.RED + "Permanent Ban")) {
				banPlayer(target);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Banned " + target + " from the server!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.GREEN + "Remove Round Ban")) {
				roundBan(target, -1);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Removed 1 round ban from " + target + "!");	
			}
			else if(itemName.equalsIgnoreCase(ChatColor.GREEN + "Remove 2 Round Bans")) {
				roundBan(target, -2);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Removed 2 round bans from " + target + "!");	
			}
			else if(itemName.equalsIgnoreCase(ChatColor.GREEN + "Remove All Round Ban")) {
				roundBan(target, 0);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Removed all round bans from " + target + "!");	
			}
			else if(itemName.equalsIgnoreCase(ChatColor.GREEN + "UnBan")) {
				unbanPlayer(target);
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "UnBanned " + target + " from the server!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.DARK_RED + "Kill the Player")) {
				Player player = Bukkit.getPlayer(target);
				if(player == null) {
					event.getWhoClicked().sendMessage(ChatColor.RED + "Could not find player: " + target);
					return;
				}
				player.setHealth(0);
			}
			else if(itemName.equalsIgnoreCase(ChatColor.AQUA + "Resurrect the Player")) {
				Player player = Bukkit.getPlayer(target);
				if(player == null) {
					event.getWhoClicked().sendMessage(ChatColor.RED + "Could not find player: " + target);
					return;
				}
				PlayerData data = pl.currentRound.getPlayerData(player.getName());
				data.rezPlayer();
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Attempted to Resurrect " + target + "!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.YELLOW + "GoTo the Player")) {
				Player player = Bukkit.getPlayer(target);
				if(player == null) {
					event.getWhoClicked().sendMessage(ChatColor.RED + "Could not find player: " + target);
					return;
				}
				event.getWhoClicked().teleport(player.getLocation());
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Teleporting to " + target + "!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.YELLOW + "Bring the Player")) {
				Player player = Bukkit.getPlayer(target);
				if(player == null) {
					event.getWhoClicked().sendMessage(ChatColor.RED + "Could not find player: " + target);
					return;
				}
				player.teleport(event.getWhoClicked().getLocation());
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Bringing " + target + "!");
			}
			else if(itemName.equalsIgnoreCase(ChatColor.YELLOW + "Inventory")) {
				Player player = Bukkit.getPlayer(target);
				if(player == null) {
					event.getWhoClicked().sendMessage(ChatColor.RED + "Could not find player: " + target);
					return;
				}
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().sendMessage(ChatColor.YELLOW + "Opening the inventory of " + target + "!");
				event.getWhoClicked().openInventory(player.getInventory());
			}
		
			event.setCancelled(true);
		}
		return;
	}
	
	private void warnPlayer(String target) {
		YamlConfiguration cfg = pl.fm.getConfig("punishments.yml").get();
		int cWarnings = cfg.getInt("Players." + getOfflineUUID(target) + ".Warnings");
		String uuid = getOfflineUUID(target);
		cfg.set("Players." + uuid + ".Warnings", cWarnings + 1);
		
		if(Bukkit.getPlayer(target) != null) {
			Bukkit.getPlayer(target).sendMessage(ChatColor.YELLOW + "You have been warned by an Admin!\n" + ChatColor.BLUE + "3 Warnings: Kick \n5 Warnings: Ban\n" 
												+ ChatColor.RED + "You currently have: " + (cWarnings+1) + " warnings!");
		}
		
		if((cWarnings+1) >= warnBeforeRBan) {
			cfg.set("Players." + uuid + ".isBanned", true);
			if(Bukkit.getPlayer(target) != null)
				Bukkit.getPlayer(target).kickPlayer(ChatColor.YELLOW + "You have been warned by an Admin!\n" + ChatColor.BLUE + "5 Warnings: Ban" 
						+ ChatColor.RED + "You currently have: " + (cWarnings+1) + " warnings!");
			return;
		}
		else if((cWarnings+1) >= warnBeforeKick) {
			if(Bukkit.getPlayer(target) != null)
				Bukkit.getPlayer(target).kickPlayer(ChatColor.YELLOW + "You have been warned by an Admin!\n" + ChatColor.BLUE + "3 Warnings: Kick\n" 
						+ ChatColor.RED + "You currently have: " + (cWarnings+1) + " warnings!");
			return;
		}
		pl.fm.getConfig("punishments.yml").save();
		pl.fm.getConfig("punishments.yml").reload();
		return;
	}
	
	private void roundBan(String target, int numBans) {
		YamlConfiguration cfg = pl.fm.getConfig("punishments.yml").get();
		int cWarnings = cfg.getInt("Players." + getOfflineUUID(target) + ".currentRoundBans");
		String uuid = getOfflineUUID(target);
		cfg.set("Players." + uuid + ".currentRoundBans", cWarnings + numBans);
		cfg.set("Players." + uuid + ".totalRoundBans", cfg.getInt("Players." + uuid + ".totalRoundBans") + numBans);
		
		if(numBans < 0) {
			if(cWarnings <= 0)
				return;
			if(Bukkit.getPlayer(target) != null) {
				Bukkit.getPlayer(target).sendMessage(ChatColor.YELLOW + "An admin has removed some of your Round Bans!\n" + ChatColor.RED + "You currently have: " + (cWarnings+numBans) + " round bans!");
			}
			pl.fm.getConfig("punishments.yml").save();
			pl.fm.getConfig("punishments.yml").reload();
			return;
		}else if(numBans == 0) {
			cfg.set("Players." + uuid + ".currentRoundBans", 0);
			cfg.set("Players." + uuid + ".totalRoundBans", 0);
			if(Bukkit.getPlayer(target) != null) {
				Bukkit.getPlayer(target).sendMessage(ChatColor.YELLOW + "An admin has removed all of your Round Bans!");
			}
		}else {
			if(Bukkit.getPlayer(target) != null) {
				Bukkit.getPlayer(target).sendMessage(ChatColor.YELLOW + "You have been round banned by an Admin!\n" + ChatColor.RED + "You currently have: " + (cWarnings+numBans) + " round bans!");
			}
		}
		
		
		pl.fm.getConfig("punishments.yml").save();
		pl.fm.getConfig("punishments.yml").reload();
		return;
	}
	
	private void banPlayer(String target) {
		YamlConfiguration cfg = pl.fm.getConfig("punishments.yml").get();
		String uuid = getOfflineUUID(target);
		cfg.set("Players." + uuid + ".isBanned", true);
		if(Bukkit.getPlayer(target) != null)
			Bukkit.getPlayer(target).kickPlayer(ChatColor.RED + "You have been banned from the server!");
	}
	
	private void unbanPlayer(String target) {
		YamlConfiguration cfg = pl.fm.getConfig("punishments.yml").get();
		String uuid = getOfflineUUID(target);
		cfg.set("Players." + uuid + ".isBanned", false);
	}
	
	//Inventory Events
	private List<String> getPlayerInfo(String target){
		List<String> playerInfo = new ArrayList<String>();
		HashMap<String, Object> punishments;// = pl.currentRound.getPlayerData(target).getPunishments();
		
		if(Bukkit.getPlayer(target) == null) {
			punishments = getOfflinePlayerPunishments(target);
		}else {
			punishments = pl.currentRound.getPlayerData(target).getPunishments();
		}
		
		playerInfo.add(ChatColor.BLUE + "Player Name: " + ChatColor.YELLOW + target);
		playerInfo.add(ChatColor.BLUE + "Player UUID: " + ChatColor.YELLOW +  getOfflineUUID(target));
		playerInfo.add(ChatColor.BLUE + "Total Warnings: " + ChatColor.YELLOW + (Integer)punishments.get("Warnings"));
		playerInfo.add(ChatColor.BLUE + "Total Round Bans: " + ChatColor.YELLOW + (Integer)punishments.get("totalRoundBans"));
		playerInfo.add(ChatColor.BLUE + "Current Round Bans: " + ChatColor.YELLOW + (Integer)punishments.get("currentRoundBans"));
		playerInfo.add(ChatColor.BLUE + "Is Server Banned: " + ChatColor.YELLOW + (!(Boolean)punishments.get("isBanned") ? ChatColor.GREEN + "No" : ChatColor.RED + "Yes"));
		return playerInfo;
	}
	
	private String getOfflineUUID(String name) {
		YamlConfiguration cfg = pl.fm.getConfig("punishments.yml").get();
		
		for(String k : cfg.getConfigurationSection("Players").getKeys(false)) {
			UUID uuid = UUID.fromString(k);
			if(Bukkit.getOfflinePlayer(uuid).getName().equals(name)) {
				return k;
			}
		}
		return null;
	}
	
	private HashMap<String, Object> getOfflinePlayerPunishments(String target){
		HashMap<String, Object> punishments = new HashMap<String, Object>();
		YamlConfiguration cfg = pl.fm.getConfig("punishments.yml").get();
		
		String uuid = getOfflineUUID(target);
		
		if(uuid == null)
			return null;
		
		ConfigurationSection data = cfg.getConfigurationSection("Players." + uuid);
		if(data == null) {
			cfg.set("Players." + uuid + ".Warnings", 0);
			cfg.set("Players." + uuid + ".totalRoundBans", 0);
			cfg.set("Players." + uuid + ".currentRoundBans", 0);
			cfg.set("Players." + uuid + ".isBanned", false);
			
			pl.fm.getConfig("punishments.yml").save();
			pl.fm.getConfig("punishments.yml").reload();
			data = cfg.getConfigurationSection("Players." + uuid);
		}
		for(String k : data.getKeys(false)) {
			punishments.put(k, cfg.get("Players." + uuid + "." + k));
		}
		return punishments;
	}

}
