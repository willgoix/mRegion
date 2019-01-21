package com.mathi.region.listener.flags;

import com.mathi.region.flags.Flags;
import com.mathi.region.manager.RegionManager;
import com.mathi.region.objects.GlobalRegion;
import com.mathi.region.objects.Region;
import com.mathi.region.utils.Permissions;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author zMathi
 */
public class MoveListener implements Listener {

    private final RegionManager manager;

    public MoveListener(RegionManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        Location from = event.getFrom();

        Player player = event.getPlayer();
        Region region = manager.getWorldManager(to.getWorld()).getRegion(to);

        if (!canCheck(player, region)){
            return;
        }
        if (!region.allows(Flags.ENTRY)) {
            event.setTo(new Location(from.getWorld(), from.getBlockX() + 0.5D, from.getBlockY(), from.getBlockZ() + 0.5D, to.getYaw(), to.getPitch()));
            player.sendMessage("§cVocê não pode entrar nessa área.");
            return;
        }

        Region regionFrom = manager.getWorldManager(from.getWorld()).getRegion(from);
        
        if (!canCheck(player, regionFrom)){
            return;
        }
        if (!regionFrom.allows(Flags.EXIT)) {
            if (!regionFrom.getName().equals(region.getName())) {
                event.setTo(new Location(from.getWorld(), from.getBlockX() + 0.5D, from.getBlockY(), from.getBlockZ() + 0.5D, to.getYaw(), to.getPitch()));
                player.sendMessage("§cVocê não pode sair dessa área.");
            }
        }
    }

    private boolean canCheck(Player player, Region region) {
        if (region instanceof GlobalRegion) {
            return false;
        }
        if ((region.getOwner() != null && region.getOwner().equalsIgnoreCase(player.getName())) || Permissions.isAdmin(player)) {
            return false;
        }
        return true;
    }
}
