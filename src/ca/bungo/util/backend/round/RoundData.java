package ca.bungo.util.backend.round;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class RoundData {
	
	private boolean gameStarted = false;
	private boolean monstersReleased = false;
	private boolean shrineDestroyed = false;
	
	//private RDvZ pl;
	
	public String worldName = "RDvZ_World";
	
	private List<PlayerData> playerData = new ArrayList<>();
	
	public RoundData(RDvZ pl) {
		//this.pl = pl;
		for(Player player : Bukkit.getOnlinePlayers()) {
			System.out.println("Added a player!");
			PlayerData data = new PlayerData(pl, player.getName());
			this.addPlayerData(data);
		}
		
		YamlConfiguration cfg = pl.fm.getConfig("punishments.yml").get();
		for(String k : cfg.getConfigurationSection("Players").getKeys(false)) {
			int cBans = cfg.getInt("Players." + k + ".currentRoundBans");
			if(cBans <= 0)
				continue;
			cfg.set("Players." + k + ".currentRoundBans", cBans - 1);
		}
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean value) {
		gameStarted = value;
	}
	
	public boolean isMonstersReleased() {
		return monstersReleased;
	}

	public void setMonstersReleased(boolean monstersReleased) {
		this.monstersReleased = monstersReleased;
	}

	public boolean isShrineDestroyed() {
		return shrineDestroyed;
	}

	public void setShrineDestroyed(boolean shrineDestroyed) {
		this.shrineDestroyed = shrineDestroyed;
	}

	public List<PlayerData> getPlayerData() {
		return playerData;
	}

	public void addPlayerData(PlayerData data) {
		playerData.add(data);
	}
	
	public void removePlayerData(String name) {
		playerData.remove(getPlayerData(name));
	}
	
	public PlayerData getPlayerData(String name) {
		PlayerData toReturn = null;
		for(PlayerData data : playerData) {
			if(data.getName().equalsIgnoreCase(name))
				toReturn = data;
		}
		return toReturn;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}
}
	