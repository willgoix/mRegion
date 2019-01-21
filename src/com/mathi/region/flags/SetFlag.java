package com.mathi.region.flags;

import com.mathi.region.flags.exception.InvalidFlagException;
import com.mathi.region.manager.exception.RegionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.command.CommandSender;

/**
 *
 * @author zMathi
 */
public class SetFlag<V> extends Flag<Set<V>> {

    private final Flag<V> subFlag;

    public SetFlag(String name, Flag<V> subFlag) {
        super(name);
        this.subFlag = subFlag;
    }

    @Override
    public Set<V> parseInput(CommandSender sender, String input) {
        Set<V> items = new HashSet();
        for (String str : input.split(",")) {
            items.add(subFlag.parseInput(sender, str.trim()));
        }
        return new HashSet(items);
    }

    @Override
    public Object toObject(Set<V> value) {
        List<Object> list = new ArrayList();
        for (V item : value) {
            list.add(subFlag.toObject(item));
        }
        return list;
    }

    @Override
    public Set<V> fromObject(Object object) {
        if (object instanceof Collection) {
            Collection<?> collection = (Collection) object;
            Set<V> items = new HashSet();
            for (Object sub : collection) {
                V item = subFlag.fromObject(sub);
                if (item != null) {
                    items.add(item);
                }
            }
            return items;
        }
        throw new InvalidFlagException("Object não é uma Collection.");
    }
}
