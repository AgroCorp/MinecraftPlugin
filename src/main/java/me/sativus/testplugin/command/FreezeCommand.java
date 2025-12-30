package me.sativus.testplugin.command;

import org.bukkit.GameRules;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.sativus.testplugin.utils.Config.Daytimes;
import me.sativus.testplugin.utils.Config.Weathers;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class FreezeCommand extends BaseCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand(String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getSender() instanceof Player
                        && sender.getSender().hasPermission("testplugin.unfreeze"))
                .then(Commands.literal("time")
                        .then(Commands.argument("subtype", StringArgumentType.word()).suggests((ctx, builder) -> {
                            for (Daytimes dt : Daytimes.values()) {
                                builder.suggest(dt.name().toLowerCase());
                            }
                            return builder.buildFuture();
                        })
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String subtype = StringArgumentType.getString(ctx, "subtype");

                                    for (World world : plugin.getServer().getWorlds()) {
                                        world.setGameRule(GameRules.ADVANCE_TIME, false);
                                        world.setTime(Daytimes.valueOf(subtype.toUpperCase()).getTime());
                                    }
                                    player.sendMessage(
                                            MiniMessage.miniMessage()
                                                    .deserialize("Time is freezed to <aqua>" + subtype + "</aqua>."));

                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("weather")
                        .then(Commands.argument("subtype", StringArgumentType.word()).suggests((ctx, builder) -> {
                            for (Weathers wt : Weathers.values()) {
                                builder.suggest(wt.name().toLowerCase());
                            }
                            return builder.buildFuture();
                        })
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    String subtype = StringArgumentType.getString(ctx, "subtype");

                                    for (World world : plugin.getServer().getWorlds()) {
                                        world.setGameRule(GameRules.ADVANCE_WEATHER, false);
                                        switch (Weathers.valueOf(subtype.toUpperCase())) {
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
                                            MiniMessage.miniMessage().deserialize(
                                                    "Weather is freezed to <aqua>" + subtype + "</aqua>."));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .build();
    }
}
