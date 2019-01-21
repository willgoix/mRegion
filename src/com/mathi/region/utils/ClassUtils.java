package com.mathi.region.utils;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author zMathi
 */
public class ClassUtils {

    public static Set<Class<?>> getClasses(JavaPlugin plugin, String packageName) {
        Set<Class<?>> classez = new HashSet<>();
        try {
            ImmutableSet<ClassPath.ClassInfo> classes = ClassPath.from(plugin.getClass().getClassLoader()).getAllClasses();
            classes.forEach(classe -> {
                if (classe.getPackageName().equalsIgnoreCase(packageName)) {
                    classez.add(classe.load());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classez;
    }
}
