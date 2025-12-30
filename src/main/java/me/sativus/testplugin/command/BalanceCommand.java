package me.sativus.testplugin.command;

import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.sativus.testplugin.manager.WalletManager;

public class BalanceCommand extends BaseCommand {
    private final WalletManager walletManager = WalletManager.getInstance();

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand(String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player)
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    double balance = walletManager.getBalance(player);
                    player.sendRichMessage("Your balance is: <green>" + balance + "</green>");
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("set")
                        .requires(sender -> sender.getSender().hasPermission("testplugin.balance.set"))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                        .executes(ctx -> {
                                            final Player target = ctx.getArgument("target", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                                            double amount = DoubleArgumentType.getDouble(ctx, "amount");
                                            walletManager.setBalance(target, amount);
                                            target.sendRichMessage("Your balance has been set to: <green>" + amount + "</green>");
                                            ctx.getSource().getSender().sendMessage("Set balance of " + target.getName() + " to " + amount);
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                .executes(ctx -> {
                                    Player player = (Player) ctx.getSource().getSender();
                                    double amount = DoubleArgumentType.getDouble(ctx, "amount");
                                    walletManager.setBalance(player, amount);
                                    player.sendRichMessage("Your balance has been set to: <green>" + amount + "</green>");
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("get")
                        .requires(sender -> sender.getSender().hasPermission("testplugin.balance.get"))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(ctx -> {
                                    final Player target = ctx.getArgument("target", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst();
                                    double balance = walletManager.getBalance(target);
                                    ctx.getSource().getSender().sendMessage(target.getName() + "'s balance is: " + balance);
                                    return Command.SINGLE_SUCCESS;
                                })))
                .build();
    }
}
