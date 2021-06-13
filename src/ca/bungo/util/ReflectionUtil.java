package ca.bungo.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtil {
	public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
    }
	
	//This one I threw together myself
	public static Class<?> getCBClass(String cbClassPath) throws ClassNotFoundException {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",") [3] + ".";
		String name = "org.bukkit.craftbukkit." + version + cbClassPath;
		Class<?> cbClass = Class.forName(name);
		return cbClass;
	}
	//This one I threw together myself
	public static Class<?> getNMSClass0(String nmsClassString) {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        Class<?> nmsClass;
		try {
			nmsClass = Class.forName(name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        return nmsClass;
    }	
}
