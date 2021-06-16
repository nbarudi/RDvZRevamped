package ca.bungo.util;

import org.bukkit.entity.Player;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.PlayerData;

public class CommandUtils {
	
	private RDvZ pl;
	
	public CommandUtils(RDvZ pl) {
		this.pl = pl;
	}

	public boolean verifyPlayerCommand(Player player, String key) {
		PlayerData data = pl.currentRound.getPlayerData(player.getName());
		if(data.getKey().equals(key)) {
			data.updateKey();
			return true;
		}
		data.updateKey();
		return false;
	}
	
}
