package com.mathi.region.flags;

import com.mathi.region.flags.exception.InvalidFlagException;
import com.mathi.region.manager.exception.RegionException;
import org.bukkit.command.CommandSender;

/**
 *
 * @author zMathi
 */
public class CommandFlag extends Flag<String> {

    public CommandFlag(String name) {
        super(name);
    }

    @Override
    public String parseInput(CommandSender sender, String input){
        input = input.trim();
        if (!input.startsWith("/")) {
            input = "/" + input;
        }
        return input.toLowerCase();
    }
    

    @Override
    public Object toObject(String value) {
        return value;
    }

    @Override
    public String fromObject(Object object) {
        if (object instanceof String) {
            return (String) object;
        }
        throw new InvalidFlagException("Object não é uma String.");
    }
}
