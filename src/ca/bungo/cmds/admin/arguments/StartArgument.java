package ca.bungo.cmds.admin.arguments;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;
import ca.bungo.util.ChatManager;
import ca.bungo.util.ItemManager;
import ca.bungo.util.backend.player.PlayerData;

public class StartArgument extends AdminArgument{

	public StartArgument(RDvZ pl, String name) {
		super(pl, name);
		this.requiresPlayer = true;
		this.desc = "Start the game";
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.getInventory().clear();
			PlayerData d = pl.currentRound.getPlayerData(p.getName());
			d.isDwarf = true;
		}
		
		ItemStack magma = ItemManager.findCustomItem("Dwarf Claim Classes").clone();
		magma.setAmount(64);
		player.getInventory().addItem(magma);
		player.getInventory().addItem(magma);
		
		pl.currentRound.setGameStarted(true);
		Bukkit.broadcastMessage(ChatManager.formatColor("&7[&aRDvZ&7] &dIt's time to play.... &bDwarves &7Vs &2Zombies&d!"));
		
		player.sendMessage("Game has started!");
	}

}
