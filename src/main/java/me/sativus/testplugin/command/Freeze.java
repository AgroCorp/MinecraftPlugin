package me.sativus.testplugin.command;

import java.util.List;

import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.sativus.testplugin.utils.Config.Daytimes;
import me.sativus.testplugin.utils.Config.Weathers;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Freeze extends BaseCommand implements TabCompleter {
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label,
            String[] args) {
        if (args.length != 2) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("You do not have permission to execute this command.");
                return true;
            }

            if (args[0].equalsIgnoreCase("time")) {
                for (World world : plugin.getServer().getWorlds()) {
                    world.setGameRule(GameRules.ADVANCE_TIME, false);
                    world.setTime(Daytimes.valueOf(args[1].toUpperCase()).getTime());
                }
                player.sendMessage(
                        MiniMessage.miniMessage().deserialize("Time is freazet to <aqua>" + args[1] + "</aqua>."));
            } else if (args[0].equalsIgnoreCase("weather")) {
                for (World world : plugin.getServer().getWorlds()) {
                    world.setGameRule(GameRules.ADVANCE_WEATHER, false);
                    switch (Weathers.valueOf(args[1].toUpperCase())) {
                        case CLEAR:
                            world.setStorm(false);
                            world.setThundering(false);
                            break;
                        case RAIN:
                            world.setStorm(true);
                            world.setThundering(false);
                            break;
                        case THUNDER:
                            world.setStorm(true);
                            world.setThundering(true);
                            break;
                    }
                }
                player.sendMessage(
                        MiniMessage.miniMessage().deserialize("Weather is freezed to <aqua>" + args[1] + "</aqua>."));
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new java.util.ArrayList<>();
        if (args.length == 1) {
            completions.add("time");
            completions.add("weather");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("time")) {
                completions.add("day");
                completions.add("night");
                completions.add("midnight");
                completions.add("noon");
            } else if (args[0].equalsIgnoreCase("weather")) {
                completions.add("clear");
                completions.add("thunder");
                completions.add("rain");
            }
        }

        return completions;
    }

}
