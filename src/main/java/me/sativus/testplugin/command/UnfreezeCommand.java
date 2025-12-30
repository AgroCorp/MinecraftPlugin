package me.sativus.testplugin.command;

import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.Command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class UnfreezeCommand extends BaseCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand(String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender() instanceof Player
                        && sender.getSender().hasPermission("testplugin.unfreeze"))
                .then(Commands.literal("time")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();

                            for (World world : plugin.getServer().getWorlds()) {
                                world.setGameRule(GameRules.ADVANCE_TIME, true);
                            }
                            player.sendMessage(
                                    MiniMessage.miniMessage().deserialize("Time is <green>unfreezed</green>."));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("weather")
                        .executes(ctx -> {
                            Player player = (Player) ctx.getSource().getSender();
                            for (World world : plugin.getServer().getWorlds()) {
                                world.setGameRule(GameRules.ADVANCE_WEATHER, true);
                            }
                            player.sendMessage(
                                    MiniMessage.miniMessage().deserialize("Weather is <green>unfreezed</green>."));
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }
}
