package com.mathi.region;

import com.mathi.region.listener.RegionListener;
import com.mathi.region.manager.RegionManager;
import com.mathi.region.utils.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author zMathi
 */
public class mRegion extends JavaPlugin {

    private RegionManager regionManager;
    private static mRegion CORE = null;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§a[mRegion] §7Plugin iniciando...");

        saveDefaultConfig();
        CORE = this;
        regionManager = new RegionManager(this);
        new CommandManager(this).registerCommands(this, "com.mathi.region.commands");
        new RegionListener(this);

        Bukkit.getConsoleSender().sendMessage("§a[mRegion] §7Plugin iniciado.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§a[mRegion] §7Plugin finalizando...");
        
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        
        Bukkit.getConsoleSender().sendMessage("§a[mRegion] §7Plugin finazalido.");
    }

    public static mRegion getCore() {
        return CORE;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
