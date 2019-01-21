package com.mathi.region.manager;

import com.mathi.region.mRegion;
import com.mathi.region.objects.Region;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zMathi
 */
public class RegionManager {

    private final Map<String, WorldRegionManager> worldManagers;

    public RegionManager(mRegion core) {
        this.worldManagers = new HashMap<>();

        Bukkit.getScheduler().runTaskLater(core, () -> { //Esperando os mundos carregar...
            Bukkit.getWorlds().forEach((world) -> {
                WorldRegionManager worldManager = new WorldRegionManager(core, world);

                worldManagers.put(world.getName(), worldManager);
            });
        }, 80L);
    }

    public WorldRegionManager getWorldManager(World world){
        return getWorldManager(world.getName());
    }

    public WorldRegionManager getWorldManager(String worldName){
        return worldManagers.get(worldName);
    }
}
