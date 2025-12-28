package me.sativus.testplugin.command;

import java.util.List;

import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class Unfreeze extends BaseCommand implements TabCompleter {
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label,
            String[] args) {
        if (args.length != 1) {
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
                    world.setGameRule(GameRules.ADVANCE_WEATHER, false);
                }
                player.sendMessage(MiniMessage.miniMessage().deserialize("Time is <red>unfreezed</red>."));
            } else if (args[0].equalsIgnoreCase("weather")) {
                for (World world : plugin.getServer().getWorlds()) {
                    world.setGameRule(GameRules.ADVANCE_WEATHER, false);
                }
                player.sendMessage(MiniMessage.miniMessage().deserialize("Weather is <red>unfreezed</red>."));
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
        }

        return completions;
    }
}
