package com.mathi.region.listener.flags;

import com.mathi.region.flags.Flags;
import com.mathi.region.flags.StateFlag;
import com.mathi.region.manager.RegionManager;
import com.mathi.region.objects.GlobalRegion;
import com.mathi.region.objects.Region;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 *
 * @author zMathi
 */
public class MobsListener implements Listener {

    private final RegionManager manager;

    public MobsListener(RegionManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            return;
        }
        Region region = manager.getWorldManager(event.getLocation().getWorld()).getRegion(event.getLocation());

        if (region instanceof GlobalRegion) {
            return;
        }
        if (!region.allows(Flags.MOBS)) {
            event.setCancelled(true);
        }
    }
}
