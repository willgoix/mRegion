package com.mathi.region.listener.flags;

import com.mathi.region.flags.Flags;
import com.mathi.region.manager.RegionManager;
import com.mathi.region.objects.GlobalRegion;
import com.mathi.region.objects.Region;
import com.mathi.region.utils.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author zMathi
 */
public class BuildListener implements Listener {

    private final RegionManager manager;

    public BuildListener(RegionManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Region region = manager.getWorldManager(player.getWorld()).getRegion(event.getBlock().getLocation());

        if (region instanceof GlobalRegion){
            return;
        }
        if ((region.getOwner() != null && region.getOwner().equalsIgnoreCase(player.getName())) || Permissions.isAdmin(player)) {
            return;
        }
        if (!region.allows(Flags.BUILD)) {
            event.setCancelled(true);
            player.sendMessage("§cVocê não pode colocar blocos neste lugar.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Region region = manager.getWorldManager(player.getWorld()).getRegion(event.getBlock().getLocation());

        if (region instanceof GlobalRegion){
            return;
        }
        if ((region.getOwner() != null && region.getOwner().equalsIgnoreCase(player.getName())) || Permissions.isAdmin(player)) {
            return;
        }
        if (!region.allows(Flags.BUILD)) {
            event.setCancelled(true);
            player.sendMessage("§cVocê não pode quebrar blocos neste lugar.");
        }
    }
}
