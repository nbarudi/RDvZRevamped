package ca.bungo.cmds.admin;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import ca.bungo.main.RDvZ;

public abstract class AdminArgument implements Listener {

    public String name = "";
    public String desc = "";
    public String usage = "";
    public boolean requiresPlayer = false;
    public RDvZ pl;

    public AdminArgument(RDvZ pl, String name){
    	this.pl = pl;
        this.name = name;
        this.usage = "/admin " + this.name;
    }

    
    public AdminArgument(RDvZ pl, String name, boolean requiresPlayer){
    	this.pl = pl;
        this.name = name;
        this.usage = "/admin " + this.name;
        this.requiresPlayer = requiresPlayer;
    }

    public abstract void runArgument(CommandSender sender, String[] args);

}
