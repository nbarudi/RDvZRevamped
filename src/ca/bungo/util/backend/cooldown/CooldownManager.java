package ca.bungo.util.backend.cooldown;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import ca.bungo.main.RDvZ;
import ca.bungo.util.ReflectionUtil;

public class CooldownManager {
	
	private RDvZ pl;
	
	private boolean reflectionValid = true;
	
	//Reflection Stuff
	private Method getHandle, sendPacket;
	private Class<?> EntityPlayer, CraftPlayer, PacketPlayOutSetCooldown, Item;
	private Constructor<?> PPOSCConstructor;
	
	public CooldownManager(RDvZ pl) {
		this.pl = pl;
		
		//Reflection stuff
		EntityPlayer = ReflectionUtil.getNMSClass0("EntityPlayer");
		try {
			CraftPlayer = ReflectionUtil.getCBClass("entity.CraftPlayer");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			reflectionValid = false;
			return;
		}
		PacketPlayOutSetCooldown = ReflectionUtil.getNMSClass0("PacketPlayOutSetCooldown");
		Item = ReflectionUtil.getNMSClass0("Item");
		
		try {
			getHandle = CraftPlayer.getDeclaredMethod("getHandle");
			PPOSCConstructor = PacketPlayOutSetCooldown.getConstructor(Item, int.class);
			sendPacket = ReflectionUtil.getNMSClass0("PlayerConnection").getMethod("sendPacket", ReflectionUtil.getNMSClass0("Packet"));
		}catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			reflectionValid = false;
			return;
		}
	}
	
	//1 Tick = 50 milliseconds
	public Cooldown giveCooldown(Player player, int ticks) {
		
		Cooldown cooldown = new Cooldown(pl, ticks);
		if(reflectionValid)
			giveEffect(player, ticks);
		return cooldown;
	}
	
	private void giveEffect(Player player, int ticks) {
		Object plr;
		try {
			plr = EntityPlayer.cast(getHandle.invoke(CraftPlayer.cast(player)));
		}catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return;
		}
		
		Object itemstack;
		try {
			Object inv = EntityPlayer.cast(plr).getClass().getField("inventory").get(EntityPlayer.cast(plr));
			
			itemstack = inv.getClass().getMethod("getItemInHand").invoke(inv);
		}catch(NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
			return;
		}
		
		Object packet;
		try {
			Object item = itemstack.getClass().getMethod("getItem").invoke(itemstack);
			packet = PPOSCConstructor.newInstance(Item.cast(item), ticks);
			
		}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sendPacket.invoke(ReflectionUtil.getConnection(player), packet);
		}catch(IllegalArgumentException | SecurityException | ReflectiveOperationException e) {
			e.printStackTrace();
			return;
		}
		
	}

}
