package com.mathi.region.flags;

import com.mathi.region.flags.exception.InvalidFlagException;
import org.bukkit.command.CommandSender;

/**
 *
 * @author zMathi
 */
public abstract class Flag<V> {

    private final String name;

    public Flag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract V parseInput(CommandSender sender, String input) throws InvalidFlagException;

    public abstract Object toObject(V value);

    public abstract V fromObject(Object object);
}
