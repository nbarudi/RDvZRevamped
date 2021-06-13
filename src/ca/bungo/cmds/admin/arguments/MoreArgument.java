package ca.bungo.cmds.admin.arguments;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.bungo.cmds.admin.AdminArgument;
import ca.bungo.main.RDvZ;

public class MoreArgument extends AdminArgument {

	public MoreArgument(RDvZ pl, String name) {
		super(pl, name);
		this.desc = "Get 64 of the item you have in hand";
		this.requiresPlayer = true;
	}

	@Override
	public void runArgument(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if(item != null) {
			player.getInventory().getItemInMainHand().setAmount(64);
			sender.sendMessage(ChatColor.YELLOW + "Given more of your item!");
			return;
		}
		sender.sendMessage(ChatColor.RED + "Invalid item!");
		return;
	}


}
