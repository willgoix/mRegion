package com.mathi.region.utils;

import org.bukkit.command.CommandSender;

/**
 *
 * @author zMathi
 */
public class Permissions {
    
    public static boolean isAdmin(CommandSender sender){
        return sender.hasPermission("regionprotecion.admin");
    }
}
