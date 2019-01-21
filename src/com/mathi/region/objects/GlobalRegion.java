package com.mathi.region.objects;

/**
 *
 * @author zMathi
 */
public class GlobalRegion extends Region {

    public GlobalRegion(String world) {
        super("global_" + world, null, world, null, null);
    }
}
