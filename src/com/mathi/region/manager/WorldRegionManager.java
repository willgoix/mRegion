package com.mathi.region.manager;

import com.mathi.region.flags.Flag;
import com.mathi.region.mRegion;
import com.mathi.region.manager.exception.RegionException;
import com.mathi.region.objects.GlobalRegion;
import com.mathi.region.objects.Region;
import com.mathi.region.storage.RegionStorage;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;
import java.util.Optional;

/**
 * @author zMathi
 */
public class WorldRegionManager {

    private final RegionStorage storage;

    private final Map<String, Region> regions;
    private final World world;

    public WorldRegionManager(mRegion core, World world) {
        this.storage = new RegionStorage(core.getConfig().getString("SQL.Host"), core.getConfig().getInt("SQL.Port"), core.getConfig().getString("SQL.Database"), core.getConfig().getString("SQL.User"), core.getConfig().getString("SQL.Password"));

        this.regions = storage.loadRegions(world.getName());
        this.regions.forEach((name, region) -> {
            region.getFlags().putAll(storage.loadFlags(name, world.getName()));
        });
        if (!this.regions.containsKey("global_"+ world.getName())){
            addRegion(new GlobalRegion(world.getName()));
        }
        this.world = world;
    }

    public void addRegion(Region region) {
        if (regions.containsKey(region.getName())) {
            throw new RegionException("Tentando adicionar regiao ja existente no mundo '"+ world.getName() +"'.");
        }
        regions.put(region.getName(), region);
        storage.addRegion(region);
    }

    public void removeRegion(String regionName) {
        removeRegion(getRegion(regionName));
    }

    public void removeRegion(Region region) {
        if (!regions.containsKey(region.getName())) {
            throw new RegionException("Tentando remover regiao que nao existe no mundo '"+ world.getName() +"'.");
        }
        regions.remove(region.getName());
        storage.removeRegion(region);
    }

    public <F extends Flag, V> void setFlag(String regionName, F flag, V value) {
        setFlag(getRegion(regionName), flag, value);
    }

    public <F extends Flag, V> void setFlag(Region region, F flag, V value) {
        if (value == null) {
            if (region.getFlag(flag) != null) {
                region.setFlag(flag, null);
                storage.removeFlag(region, flag);
            }
        } else {
            boolean storager = false;
            if (region.getFlag(flag) == null) {
                storager = true;
            }
            region.setFlag(flag, value);
            if (storager) {
                storage.addFlag(region, flag);
            }
        }
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public Region getRegion(Location location) {
        Optional<Region> regionLambda = regions.values().stream().filter(region -> region.containsLocation(location)).findFirst();
        return regionLambda.isPresent() ? regionLambda.get() : getRegion("global_" + location.getWorld().getName());
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public World getWorld() {
        return world;
    }
}
