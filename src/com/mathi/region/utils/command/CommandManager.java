package com.mathi.region.utils.command;

import com.mathi.region.utils.ClassUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author zMathi
 */
public class CommandManager {

    private CommandMap commandMap = null;
    private JavaPlugin plugin = null;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerCommands(JavaPlugin javaPlugin, String packageName) {
        Set<Class<?>> classesSet = ClassUtils.getClasses(plugin, packageName);

        for (Class<?> classes : classesSet) {
            for (Method method : classes.getMethods()) {
                if (method.isAnnotationPresent(CommandHandler.class)) {
                    CommandHandler commandHandler = method.getAnnotation(CommandHandler.class);
                    if (commandMap.getCommand(commandHandler.command()) != null) {
                        continue;
                    }
                    register(commandHandler, method, classes);
                }
            }
        }
    }

    private void register(CommandHandler commandHandler, Method method, Class<?> classe) {
        commandMap.register(commandHandler.command().toLowerCase(), new Command(commandHandler.command(), commandHandler.description(), "/" + commandHandler.command(), Arrays.asList(commandHandler.aliases())) {
            @Override
            public boolean execute(CommandSender sender, String command, String[] args) {
                if (commandHandler.onlyPlayers() && (!(sender instanceof Player))) {
                    sender.sendMessage("Esse comando so pode ser usado in-game por jogadores.");
                    return false;
                }
                try {
                    method.invoke(classe.newInstance(), sender, command, args);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
}
