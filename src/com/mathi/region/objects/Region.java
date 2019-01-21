package com.mathi.region.objects;

import com.google.common.collect.Maps;
import com.mathi.region.flags.Flag;
import com.mathi.region.flags.StateFlag;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 *
 * @author zMathi
 */
public class Region {

    private String name = null;
    private String owner = null;
    private String world = null;
    private int minX = 0, minY = 0, minZ = 0;
    private int maxX = 0, maxY = 0, maxZ = 0;
    private final Map<Flag<?>, Object> flags = Maps.newHashMap();

    public Region(String name, String owner, String world, Location locationMin, Location locationMax) {
        this.name = name;
        this.owner = owner;
        this.world = world;
        if (locationMin != null){
            this.minX = Math.min(locationMin.getBlockX(), locationMax.getBlockX()); this.minY = Math.min(locationMin.getBlockY(), locationMax.getBlockY()); this.minZ = Math.min(locationMin.getBlockZ(), locationMax.getBlockZ());
        }
        if(locationMax != null){
            this.maxX = Math.max(locationMin.getBlockX(), locationMax.getBlockX()); this.maxY = Math.max(locationMin.getBlockY(), locationMax.getBlockY()); this.maxZ = Math.max(locationMin.getBlockZ(), locationMax.getBlockZ());
        }
    }

    public String getName() {
        return name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public String getWorld() {
        return world;
    }
    
    public Location getLocationMin(){
        return new Location(world == null ? null : Bukkit.getWorld(world), minX, minY, minZ);
    }
    
    public Location getLocationMax(){
        return new Location(world == null ? null : Bukkit.getWorld(world), maxX, maxY, maxZ);        
    }
    
    public boolean allows(StateFlag stateFlag){
        if (flags.containsKey(stateFlag)){
            return ((StateFlag.State) flags.get(stateFlag)).getBoolean();
        }else{
            return stateFlag.getDefaultState().getBoolean();
        }
    }
    
    public <F extends Flag<?>, V> void setFlag(F flag, V value){
        if (value == null){
            flags.remove(flag);
        }else{
            flags.put(flag, value);
        }
    }
    
    public <F extends Flag<?>, V> V getFlag(F flag){
        return (V) flags.get(flag);
    }

    public Map<Flag<?>, Object> getFlags() {
        return flags;
    }
    
    public boolean containsLocation(int x, int y, int z) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public boolean containsLocation(Location location) {
        return containsLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
