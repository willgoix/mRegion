package com.mathi.region.listener;

import com.mathi.region.mRegion;
import com.mathi.region.listener.flags.BuildListener;
import com.mathi.region.listener.flags.CommandListener;
import com.mathi.region.listener.flags.DamageListener;
import com.mathi.region.listener.flags.MobsListener;
import com.mathi.region.listener.flags.MoveListener;
import com.mathi.region.listener.flags.PvPListener;
import org.bukkit.Bukkit;

/**
 *
 * @author zMathi
 */
public class RegionListener {

    public RegionListener(mRegion core) {
        Bukkit.getPluginManager().registerEvents(new PvPListener(core.getRegionManager()), core);
        Bukkit.getPluginManager().registerEvents(new BuildListener(core.getRegionManager()), core);
        Bukkit.getPluginManager().registerEvents(new MobsListener(core.getRegionManager()), core);
        Bukkit.getPluginManager().registerEvents(new MoveListener(core.getRegionManager()), core);
        Bukkit.getPluginManager().registerEvents(new CommandListener(core.getRegionManager()), core);
        Bukkit.getPluginManager().registerEvents(new DamageListener(core.getRegionManager()), core);
    }
}
