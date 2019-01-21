package com.mathi.region.listener.flags;

import com.mathi.region.manager.RegionManager;
import com.mathi.region.flags.Flags;
import com.mathi.region.flags.StateFlag;
import com.mathi.region.objects.GlobalRegion;
import com.mathi.region.objects.Region;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 *
 * @author zMathi
 */
public class DamageListener implements Listener {

    private final RegionManager manager;

    public DamageListener(RegionManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Region region = manager.getWorldManager(event.getEntity().getWorld()).getRegion(event.getEntity().getLocation());

        if (region instanceof GlobalRegion) {
            return;
        }
        if (!region.allows(Flags.DAMAGE)) {
            event.setCancelled(true);
        }
    }
}
