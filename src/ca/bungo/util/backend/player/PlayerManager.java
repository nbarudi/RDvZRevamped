package ca.bungo.util.backend.player;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.warps.Warp;

public class PlayerManager implements Listener {
	
	RDvZ instance;
	public PlayerManager(RDvZ instance) {
		this.instance = instance;
	}
	
	//Creating player data
	@EventHandler(priority = EventPriority.LOW)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		
		if(instance.currentRound.getPlayerData(player.getName()) != null) {
			instance.currentRound.getPlayerData(player.getName()).setOnline(true);
			return;
		}
		//Create player data
		PlayerData data = new PlayerData(instance, player.getName());
		instance.currentRound.addPlayerData(data);
		event.setJoinMessage(ChatColor.LIGHT_PURPLE + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.LIGHT_PURPLE + " to the server!");
		player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100.0D);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		PlayerData data = instance.currentRound.getPlayerData(player.getName());
		data.setOnline(false);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		if(instance.currentRound.getPlayerData(player.getName()) != null) {
			instance.currentRound.getPlayerData(player.getName()).setOnline(false);
			event.setQuitMessage(ChatColor.YELLOW + player.getName() + " has left the server!");
			return;
		}
	}
	
	//Handling Player Deaths
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerData data = instance.currentRound.getPlayerData(player.getName());
		
		if(data.isDwarf) {
			data.isDwarf = false;
			
			//Removing tasks that are not needed
			data.removeTask("Blindness");
			data.removeTask("Mana");
			
			//Setting up Death Information
			DeathData dData = new DeathData();
			dData.setDclass(data.getDwarfClass());
			dData.setDeathInv(player.getInventory().getContents());
			dData.setDeathLoc(player.getLocation());
			dData.setMana(data.getMana());
			data.setDeathData(dData);
		}
		data.claimedClasses = false;
		data.unDisguisePlayer();
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		//Player player = event.getPlayer();
		
		Warp w = instance.wm.getWarp("spawn");
		event.setRespawnLocation(w.getLocation());
		
	}
	
	public static class RandomString {

	    /**
	     * Generate a random string.
	     */
	    public String nextString() {
	        for (int idx = 0; idx < buf.length; ++idx)
	            buf[idx] = symbols[random.nextInt(symbols.length)];
	        return new String(buf);
	    }

	    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	    public static final String lower = upper.toLowerCase(Locale.ROOT);

	    public static final String digits = "0123456789";

	    public static final String alphanum = upper + lower + digits;

	    private final Random random;

	    private final char[] symbols;

	    private final char[] buf;

	    public RandomString(int length, Random random, String symbols) {
	        if (length < 1) throw new IllegalArgumentException();
	        if (symbols.length() < 2) throw new IllegalArgumentException();
	        this.random = Objects.requireNonNull(random);
	        this.symbols = symbols.toCharArray();
	        this.buf = new char[length];
	    }

	    /**
	     * Create an alphanumeric string generator.
	     */
	    public RandomString(int length, Random random) {
	        this(length, random, alphanum);
	    }

	    /**
	     * Create an alphanumeric strings from a secure generator.
	     */
	    public RandomString(int length) {
	        this(length, new SecureRandom());
	    }

	    /**
	     * Create session identifiers.
	     */
	    public RandomString() {
	        this(21);
	    }

	}

}
