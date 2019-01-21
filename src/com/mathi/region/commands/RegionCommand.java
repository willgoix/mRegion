package com.mathi.region.commands;

import com.mathi.region.mRegion;
import com.mathi.region.flags.Flag;
import com.mathi.region.flags.Flags;
import com.mathi.region.flags.exception.InvalidFlagException;
import com.mathi.region.manager.RegionManager;
import com.mathi.region.objects.Region;
import com.mathi.region.utils.Permissions;
import com.mathi.region.utils.command.CommandHandler;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author zMathi
 */
public class RegionCommand {

    private final RegionManager manager = mRegion.getCore().getRegionManager();
    private static final String METADATA_MIN = "mregion-min";
    private static final String METADATA_MAX = "mregion-max";

    @CommandHandler(command = "regiao", aliases = {"rg", "region"}, description = "Comandos de administradores para controle de regiões.", onlyPlayers = true)
    public void execute(CommandSender sender, String command, String[] args) {
        Player player = (Player) sender;

        if (!Permissions.isAdmin(sender)) {
            player.sendMessage("§cVocê não tem permissão para executar esse comando.");
            return;
        }
        if (args.length == 0) {
            help(player);
            return;
        }
        if (args[0].equalsIgnoreCase("flags")) {
            flags(player);
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                Region region = manager.getWorldManager(player.getWorld()).getRegion(player.getLocation());

                if (region == null) {
                    player.sendMessage("§cNão há nenhuma região na sua posição.");
                    return;
                }

                info(player, region);
            } else if (args.length == 2) {
                String name = args[1];
                Region region = manager.getWorldManager(player.getWorld()).getRegion(name);

                if (region == null) {
                    player.sendMessage("§cA região '" + name + "' não existe nesse mundo.");
                    return;
                }
                info(player, region);
            }
        } else if (args[0].equalsIgnoreCase("max")) {
            player.setMetadata(METADATA_MAX, new FixedMetadataValue(mRegion.getCore(), player.getLocation()));
            player.sendMessage(String.format("§aPosição máxima definida! [Max: x%d, y%d, z%d]", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
        } else if (args[0].equalsIgnoreCase("min")) {
            player.setMetadata(METADATA_MIN, new FixedMetadataValue(mRegion.getCore(), player.getLocation()));
            player.sendMessage("§aPosição mínima definida! [Min: x" + player.getLocation().getBlockX() + ", y" + player.getLocation().getBlockY() + ", z" + player.getLocation().getBlockZ() + "]");
        } else if (args[0].equalsIgnoreCase("lista")) {
            player.sendMessage("");
            player.sendMessage(" §aLista de regiões:");
            int i = 1;
            for (Region region : manager.getWorldManager(player.getWorld()).getRegions().values()) {
                player.sendMessage(" §f" + i++ + ". §7" + region.getName() + " - Mundo: " + region.getWorld() + (region.getOwner() == null ? "" : ", Dono: " + region.getOwner()) + " §e(Min: x" + region.getLocationMin().getBlockX() + ", y" + region.getLocationMin().getBlockY() + " z" + region.getLocationMin().getBlockZ() + ") §e(Max: x" + region.getLocationMax().getBlockX() + ", y" + region.getLocationMax().getBlockY() + " z" + region.getLocationMax().getBlockZ() + ")");
            }
            player.sendMessage("");
        } else if (args[0].equalsIgnoreCase("criar")) {
            if (args.length != 2) {
                player.sendMessage("§cUtilize: /" + command + " criar <nome>");
                return;
            }
            if (!player.hasMetadata(METADATA_MIN) || !player.hasMetadata(METADATA_MAX)) {
                player.sendMessage("§cVocê precisa definir as posições primeiro. (/region min/max)");
                return;
            }
            String name = args[1];

            if (manager.getWorldManager(player.getWorld()).getRegion(name) != null) {
                player.sendMessage("§cJá existe uma região com o nome '" + name + "'.");
                return;
            }

            Location min = (Location) player.getMetadata(METADATA_MIN).get(0).value();
            Location max = (Location) player.getMetadata(METADATA_MAX).get(0).value();

            if (!min.getWorld().getName().equals(max.getWorld().getName())) {
                player.sendMessage("§aAs posições não podem ser em mundos diferentes.");
                return;
            }

            Region region = new Region(name, null, max.getWorld().getName(), min, max);
            manager.getWorldManager(player.getWorld()).addRegion(region);

            player.sendMessage("§aRegião " + name + " criada! [Máx: x" + max.getBlockX() + ", y" + max.getBlockY() + ", z" + max.getBlockZ() + "] [Min: x" + min.getBlockX() + ", y" + min.getBlockY() + ", z" + min.getBlockZ() + "]");
        } else if (args[0].equalsIgnoreCase("remover")) {
            if (args.length != 2) {
                player.sendMessage("§cUtilize: /" + command + " remover <região>");
                return;
            }
            String name = args[1];
            Region region = manager.getWorldManager(player.getWorld()).getRegion(name);

            if (region == null) {
                player.sendMessage("§cA região '" + name + "' não existe.");
                return;
            }
            manager.getWorldManager(player.getWorld()).removeRegion(region);
            player.sendMessage("§aRegião " + name + " removida!");
        } else if (args[0].equalsIgnoreCase("flag")) {
            if (args.length == 3 || args.length == 4) {
                String regionName = args[1];
                String flagName = args[2];
                String flagValue = args.length == 4 ? args[3] : null;

                Region region = manager.getWorldManager(player.getWorld()).getRegion(regionName);
                if (region == null) {
                    player.sendMessage("§cA região '" + regionName + "' não existe.");
                    return;
                }

                Flag flag = Flags.getFlag(flagName);
                if (flag == null) {
                    player.sendMessage("§cA flag '" + flagName + "' não existe.");
                    flags(player);
                    return;
                }
                Object value = null;
                if (flagValue != null) {
                    try {
                        value = flag.parseInput(sender, flagValue);
                    } catch (InvalidFlagException e) {
                        player.sendMessage("§cOcorreu um erro: " + e.getMessage());
                        flags(player);
                        return;
                    }
                }
                region.setFlag(flag, value);
                player.sendMessage("§aFlag " + flagName + " "+ (flagValue == null ? "removida" : "definida") +" na região " + regionName + "!" + (flagValue == null ? "" : " §7("+ flagValue + ")"));
            } else {
                player.sendMessage("§cUtilize: /" + command + " flag <região> <flag> [valor]");
            }
        }
    }

    private void info(Player player, Region region) {
        StringBuilder builder = new StringBuilder();
        if (region.getFlags().isEmpty()) {
            builder.append("§c").append("(Nenhuma)");
        } else {
            boolean first = true;
            for (Flag flags : region.getFlags().keySet()) {
                if (!first) {
                    builder.append("§8, ");
                }
                first = false;
                builder.append("§7").append(flags.getName()).append(" ").append("§e(").append(String.valueOf(region.getFlags().get(flags))).append(")");
            }
        }
        player.sendMessage("");
        player.sendMessage(" §aRegião: §e" + region.getName());
        if (region.getOwner() != null) {
            player.sendMessage(" §aDono: §7"+ region.getOwner());
        }
        player.sendMessage(" §aMundo: §7" + region.getWorld());
        player.sendMessage(" §aPosição máxima: §7x" + region.getLocationMax().getBlockX() + ", y" + region.getLocationMax().getBlockY() + ", z" + region.getLocationMax().getBlockZ() + "");
        player.sendMessage(" §aPosição mínima: §7x" + region.getLocationMin().getBlockX() + ", y" + region.getLocationMin().getBlockY() + ", z" + region.getLocationMin().getBlockZ() + "");
        player.sendMessage(" §aFlags: " + builder);
        player.sendMessage("");
    }

    private void help(Player player) {
        player.sendMessage("");
        player.sendMessage(" §a/region criar <nome> §7- Criar uma região.");
        player.sendMessage(" §a/region remover <região> §7- Remover uma região.");
        player.sendMessage(" §a/region info [região] §7- Ver informações de uma região.");
        player.sendMessage(" §a/region lista §7- Listar todas regiões.");
        player.sendMessage(" §a/region min §7- Definir a posição mínima.");
        player.sendMessage(" §a/region max §7- Definir a posição máxima.");
        player.sendMessage(" §a/region flags §7- Ver todas flags.");
        player.sendMessage(" §a/region flag <região> <flag> [valor] §7- Definir o valor de uma flag. (vazio para remover)");
        player.sendMessage("");
    }

    private void flags(Player player) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Flag flags : Flags.FLAGS) {
            if (!first) {
                builder.append("§8, ");
            }
            first = false;
            builder.append("§7").append(flags.getName());
        }
        player.sendMessage("");
        player.sendMessage(" §aFlags: " + builder);
        player.sendMessage("");
    }
}
