package com.mathi.region.listener.flags;

import com.mathi.region.flags.Flags;
import com.mathi.region.manager.RegionManager;
import com.mathi.region.objects.GlobalRegion;
import com.mathi.region.objects.Region;
import com.mathi.region.utils.Permissions;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author zMathi
 */
public class CommandListener implements Listener {

    private final RegionManager manager;

    public CommandListener(RegionManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/")) {
            Player player = event.getPlayer();
            String command = event.getMessage().toLowerCase();

            Region region = manager.getWorldManager(player.getWorld()).getRegion(player.getLocation());
            if (region instanceof GlobalRegion || (region.getOwner() != null && region.getOwner().equalsIgnoreCase(player.getName())) || Permissions.isAdmin(player)) {
                return;
            }
            Set<String> blockeds = region.getFlag(Flags.BLOCKED_COMMANDS);
            Set<String> alloweds = region.getFlag(Flags.ALLOWED_COMMANDS);
            String result = null;

            if (blockeds != null) {
                for (String blocked : blockeds) {
                    if (blocked.equalsIgnoreCase(command)) {
                        result = blocked;
                    }
                }
            }
            if (result != null) {
                if (alloweds != null) {
                    for (String allowed : alloweds) {
                        if (allowed.equalsIgnoreCase(result)) {
                            result = null;
                        }
                    }
                }
                if (result != null) {
                    event.setCancelled(true);
                    player.sendMessage("§c'" + result + "' não é permitido nesta área.");
                }
            }
        }
    }
}
