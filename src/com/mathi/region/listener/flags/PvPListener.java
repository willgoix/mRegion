package com.mathi.region.listener.flags;

import com.mathi.region.manager.RegionManager;
import com.mathi.region.flags.Flags;
import com.mathi.region.flags.StateFlag;
import com.mathi.region.objects.GlobalRegion;
import com.mathi.region.objects.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 *
 * @author zMathi
 */
public class PvPListener implements Listener {

    private final RegionManager manager;

    public PvPListener(RegionManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            Region region = manager.getWorldManager(damaged.getWorld()).getRegion(damaged.getLocation());

            if (region instanceof GlobalRegion) {
                return;
            }
            if (!region.allows(Flags.PVP)) {
                event.setCancelled(true);
                damager.sendMessage("§cO jogador está numa região com PvP desativado.");
            }
        }
    }
}
