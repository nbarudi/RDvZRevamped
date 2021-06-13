package ca.bungo.util.backend.player;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import ca.bungo.main.RDvZ;
import ca.bungo.util.ChatManager;
import ca.bungo.util.backend.cooldown.Cooldown;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerData {
	
	public static enum DwarfClass  {
		NONE, BUILDER, SMITH, TAILOR, ALCHEMIST, BAKER, ENCHANTER
	}
	
	public static enum MonsterClass  {
		NONE, ZOMBIE, SKELETON, CREEPER, SPIDER, WOLF, IRONGOLEM, BROODMOTHER, ENDERMAN
	}
	
	private RDvZ instance;
	
	private HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	
	private HashMap<String, Cooldown> cooldowns = new HashMap<String, Cooldown>();
	
	private String name = "";
	private String nick = "";
	
	public boolean isDwarf = false;
	public boolean isHero = false;
	public boolean claimedClasses = false;
	
	private boolean isOnline = true;
	
	private DeathData deathData;
	
	private DwarfClass dwarfClass = DwarfClass.NONE;
	private DwarfClass monsterClass = DwarfClass.NONE;
	
	private int mana;
	private int maxMana = 1000;
	
	public PlayerData(RDvZ instance, String name) {
		this.name = name;
		this.instance = instance;
		
		this.mana = maxMana;
		startTasks();
	}
	
	private void startTasks() {
		int temptask = 0;
		
		temptask = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () ->{
			if(!isOnline)
				return;
			Player player = Bukkit.getPlayer(name);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§bMana: §a§l" + mana));
			if(!instance.currentRound.isGameStarted())
				return;
			if(instance.currentRound.isShrineDestroyed())
				return;
			if(!isDwarf)
				return;
			else if(isHero) {
				mana += 5;
			}else {
				mana += 3;
			}
			if(mana > maxMana)
				mana = maxMana;
		}, 20, 10);
		tasks.put("Mana", temptask);
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public DwarfClass getDwarfClass() {
		return dwarfClass;
	}
	public void setDwarfClass(DwarfClass dwarfClass) {
		this.dwarfClass = dwarfClass;
	}
	
	public DwarfClass getMonsterClass() {
		return monsterClass;
	}
	
	public void setMonsterClass(DwarfClass monsterClass) {
		this.monsterClass = monsterClass;
	}
	
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public boolean useMana(int mana) {
		if(this.mana >= mana) {
			this.mana -= mana;
			return true;
		}
		return false;
	}
	
	public int getMaxMana() {
		return maxMana;
	}
	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}
	
	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
	public void addCooldown(String name, Cooldown cooldown) {
		cooldowns.put(name, cooldown);
	}
	
	public void removeCooldown(String name) {
		cooldowns.remove(name);
	}
	
	public Cooldown getCooldown(String name) {
		Cooldown cd = null;
		cd = cooldowns.get(name);
		return cd;
	}
	
	public HashMap<String, Object> getPunishments(){
		HashMap<String, Object> punishments = new HashMap<String, Object>();
		YamlConfiguration cfg = instance.fm.getConfig("punishments.yml").get();
		
		String uuid = Bukkit.getPlayer(name).getUniqueId().toString();
		
		ConfigurationSection data = cfg.getConfigurationSection("Players." + uuid);
		if(data == null) {
			cfg.set("Players." + uuid + ".Warnings", 0);
			cfg.set("Players." + uuid + ".totalRoundBans", 0);
			cfg.set("Players." + uuid + ".currentRoundBans", 0);
			cfg.set("Players." + uuid + ".isBanned", false);
			
			instance.fm.getConfig("punishments.yml").save();
			instance.fm.getConfig("punishments.yml").reload();
			data = cfg.getConfigurationSection("Players." + uuid);
		}
		for(String k : data.getKeys(false)) {
			punishments.put(k, cfg.get("Players." + uuid + "." + k));
		}
		
		return punishments;
	}
	
	public void rezPlayer() {
		Player player = Bukkit.getPlayer(name);
		if(deathData == null)
			return;
		if(!isOnline)
			return;
		player.getInventory().setContents(deathData.getDeathInv());
		player.teleport(deathData.getDeathLoc());
		mana = deathData.getMana();
		dwarfClass = deathData.getDclass();
		isDwarf = true;
		player.getWorld().strikeLightning(new Location(deathData.getDeathLoc().getWorld(), deathData.getDeathLoc().getX(), deathData.getDeathLoc().getY() + 10, deathData.getDeathLoc().getZ()));
		player.sendMessage(ChatManager.formatColor("&7[&aRDvZ&7] &eYou have been Resurrected by an admin!"));
	}

	public DeathData getDeathData() {
		return deathData;
	}

	public void setDeathData(DeathData deathData) {
		this.deathData = deathData;
	}

}
