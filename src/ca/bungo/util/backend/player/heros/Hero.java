package ca.bungo.util.backend.player.heros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class Hero {
	
	public RDvZ pl;
	
	private int maxHeros = 1;
	private int maxMana = 1500;
	private String suffix = "";
	private String name = "";
	private ChatColor color = ChatColor.GOLD;
	
	public Hero(RDvZ pl, String name, String suffix) {
		this.pl = pl;
		this.suffix = suffix;
		this.name = name;
	}
	
	public void setPlayer(Player toHero, boolean isForced) {
		PlayerData data = pl.currentRound.getPlayerData(toHero.getName());
		data.isHero = true;
		data.setCurrentHero(this);
		data.setMaxMana(maxMana);
		data.setMana(maxMana);
		
		data.setNick(toHero.getName() + color + " " + suffix );
		if(!isForced)
			toHero.getInventory().clear();
		
		Bukkit.broadcastMessage(ChatColor.GRAY + toHero.getName() + ChatColor.GOLD + " has become the " + getColor() + getName());
	}

	public int getMaxHeros() {
		return maxHeros;
	}

	public void setMaxHeros(int maxHeros) {
		this.maxHeros = maxHeros;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	
	
}
