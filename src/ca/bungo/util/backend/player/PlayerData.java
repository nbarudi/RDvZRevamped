package ca.bungo.util.backend.player;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.main.RDvZ;
import ca.bungo.util.ChatManager;
import ca.bungo.util.backend.cooldown.Cooldown;
import ca.bungo.util.backend.player.heros.Hero;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
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
	
	private MobDisguise cDisg;
	
	private String name = "";
	private String nick = "";
	
	private String key = "";
	
	public boolean isDwarf = false;
	public boolean isHero = false;
	public boolean hasGodMode = false;
	public boolean claimedClasses = false;
	
	private boolean isOnline = true;
	
	private DeathData deathData;
	
	private DwarfClass dwarfClass = DwarfClass.NONE;
	private MonsterClass monsterClass = MonsterClass.NONE;
	private Hero currentHero = null;
	
	private int mana;
	private int maxMana = 1000;
	
	public PlayerData(RDvZ instance, String name) {
		this.name = name;
		this.nick = name;
		this.instance = instance;
		
		this.key = new PlayerManager.RandomString().nextString();
		
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
		
		
		temptask = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
				if (!isOnline || !instance.currentRound.isGameStarted())
					return;
				Player player = Bukkit.getPlayer(name);
				Location loc = player.getLocation();

				byte light = loc.getBlock().getLightLevel();
				if (light <= 6) {
					// If the player has a torch in their main or off hand don't blind them!
					if (player.getInventory().getItemInMainHand().getType().equals(Material.TORCH)
							|| player.getInventory().getItemInOffHand().getType().equals(Material.TORCH)
							|| player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
						player.removePotionEffect(PotionEffectType.BLINDNESS);
						return;
					}
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 0));
				} else {
					player.removePotionEffect(PotionEffectType.BLINDNESS);
				}
		}, 20, 20);

		tasks.put("Blindness", temptask);
		
		if(!tasks.containsKey("TabName")) {
			temptask = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, ()->{
				
				Player player = Bukkit.getPlayer(name);
				if(!instance.currentRound.isGameStarted()) {
					this.nick = name;
				}
				else if(isDwarf && dwarfClass != DwarfClass.NONE && !isHero) {
					switch(dwarfClass) {
					case ALCHEMIST:
						this.nick = name + ChatColor.BLUE + " the Alchemist";
						break;
					case BAKER:
						this.nick = name + ChatColor.BLUE + " the Baker";
						break;
					case BUILDER:
						this.nick = name + ChatColor.BLUE + " the Builder";
						break;
					case ENCHANTER:
						this.nick = name + ChatColor.BLUE + " the Enchanter";
						break;
					case SMITH:
						this.nick = name + ChatColor.BLUE + " the BlackSmith";
						break;
					case TAILOR:
						this.nick = name + ChatColor.BLUE + " the Tailor";
						break;
					default:
						break;
					}
				}
				else if(isHero) {
					this.nick = name + getCurrentHero().getColor() + getCurrentHero().getSuffix();
				}else if(!monsterClass.equals(MonsterClass.NONE)) {
					this.nick = name + ChatColor.DARK_RED + " the Monster";
				}else {
					this.nick = name;
				}
				player.setPlayerListName(nick);
			}, 10, 10);
			tasks.put("TabName", temptask);
		}
		
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
	
	public MonsterClass getMonsterClass() {
		return monsterClass;
	}
	
	public void setMonsterClass(MonsterClass monsterClass) {
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
	
	public String getKey() {
		return this.key;
	}
	
	public void updateKey() {
		this.key = new PlayerManager.RandomString().nextString();
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
		startTasks();
	}

	public DeathData getDeathData() {
		return deathData;
	}

	public void setDeathData(DeathData deathData) {
		this.deathData = deathData;
	}
	
	public void addTask(String name, int task) {
		tasks.put(name, task);
	}
	
	public void removeTask(String name) {
		if(tasks.containsKey(name)) {
			Bukkit.getScheduler().cancelTask(tasks.get(name));
			tasks.remove(name);
		}
	}
	
	public void disguisePlayer(MobDisguise disg) {
		disg.setEntity(Bukkit.getPlayer(name));
		disg.startDisguise();
		cDisg = disg;
	}
	
	public void unDisguisePlayer() {
		if(cDisg == null)
			return;
		cDisg.stopDisguise();
		cDisg = null;
	}
	
	public Hero getCurrentHero() {
		return currentHero;
	}


	public void setCurrentHero(Hero currentHero) {
		this.currentHero = currentHero;
	}


	@Override
	public String toString() {
		String toRet = "" + ChatColor.BLUE;
		
		toRet += ChatColor.BLUE + "Name: " + this.nick + "\n";
		toRet += ChatColor.BLUE + "RealName: " + this.name + "\n";
		toRet += ChatColor.BLUE + "IsDwarf: " + (this.isDwarf ? ChatColor.GREEN + "True" : ChatColor.RED + "False") + "\n";
		toRet += ChatColor.BLUE + "IsHero: " + (this.isHero ? ChatColor.GREEN + "True" : ChatColor.RED + "False") + "\n";
		toRet += ChatColor.BLUE + "Claimed Classes: " + (this.claimedClasses ? ChatColor.GREEN + "True" : ChatColor.RED + "False") + "\n";
		toRet += ChatColor.BLUE + "IsOnline: " + (this.isOnline ? ChatColor.GREEN + "True" : ChatColor.RED + "False") + "\n";
		toRet += ChatColor.BLUE + "DwarfClass: " + this.dwarfClass.toString() + "\n";
		toRet += ChatColor.BLUE + "MonsterClass: " + this.monsterClass.toString() + "\n";
		toRet += ChatColor.BLUE + "Mana: " + this.mana + "\n";
		toRet += ChatColor.BLUE + "Current Key: " + ChatColor.YELLOW + this.key;
		
		return toRet;
	}

}
