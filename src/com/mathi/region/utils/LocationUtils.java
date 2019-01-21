package com.mathi.region.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author zMathi
 */
public class LocationUtils {

    public static String serialize(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x + "&" + y + "&" + z;
    }

    public static Location deserialize(String locationSerialized) {
        String[] argumentos = locationSerialized.split("&");
        double x = Double.valueOf(argumentos[0]);
        double y = Double.valueOf(argumentos[1]);
        double z = Double.valueOf(argumentos[2]);

        return new Location(null, x, y, z);
    }
}
