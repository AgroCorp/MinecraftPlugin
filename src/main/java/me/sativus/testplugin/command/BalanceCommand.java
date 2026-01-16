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
                .requires(
                        sender -> sender.getExecutor() instanceof Player)
                .then(Commands.literal("send")
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                        .executes(ctx -> {
                                            final Player sender =
                                                    (Player) ctx.getSource().getSender();
                                            final Player target = ctx
                                                    .getArgument("target",
                                                            PlayerSelectorArgumentResolver.class)
                                                    .resolve(ctx.getSource()).getFirst();
                                            final double amount =
                                                    DoubleArgumentType.getDouble(ctx, "amount");

                                            if (walletManager.withdraw(sender, amount)) {
                                                walletManager.deposit(target, amount);
                                                sender.sendRichMessage("You have sent <green>"
                                                        + amount + "</green> to <green>"
                                                        + target.getName() + "</green>.");
                                                target.sendRichMessage("You have received <green>"
                                                        + amount + "</green> from <green>"
                                                        + sender.getName() + "</green>.");
                                            } else {
                                                sender.sendRichMessage(
                                                        "You do not have enough balance to send <red>"
                                                                + amount + "</red>.");
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("set")
                        .requires(
                                sender -> sender.getSender()
                                        .hasPermission("testplugin.balance.set"))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                        .executes(ctx -> {
                                            final Player target = ctx
                                                    .getArgument("target",
                                                            PlayerSelectorArgumentResolver.class)
                                                    .resolve(ctx.getSource()).getFirst();
                                            double amount =
                                                    DoubleArgumentType.getDouble(ctx, "amount");
                                            walletManager.setBalance(target, amount);

                                            if (!target.getUniqueId()
                                                    .equals(((Player) ctx.getSource().getSender())
                                                            .getUniqueId())) {
                                                ctx.getSource().getSender().sendMessage(
                                                        "Set balance of " + target.getName()
                                                                + " to " + amount);
                                                target.sendRichMessage(
                                                        "Your balance has been set to: <green>"
                                                                + amount + "</green>");
                                            } else {
                                                target.sendRichMessage(
                                                        "Your balance has been set to: <green>"
                                                                + amount + "</green>");
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))
                .then(Commands.literal("get")
                        .then(Commands.argument("target", ArgumentTypes.player()).executes(ctx ->

                        {
                            final Player target =
                                    ctx.getArgument("target", PlayerSelectorArgumentResolver.class)
                                            .resolve(ctx.getSource()).getFirst();
                            final Player sender = (Player) ctx.getSource().getSender();

                            if (target.getUniqueId().equals(sender.getUniqueId())) {
                                double balance = walletManager.getBalance(sender);
                                sender.sendMessage("Your balance is: " + balance);
                                return Command.SINGLE_SUCCESS;
                            } else {
                                if (ctx.getSource().getSender()
                                        .hasPermission("testplugin.balance.get.other")) {
                                    double balance = walletManager.getBalance(target);
                                    ctx.getSource().getSender().sendMessage(
                                            target.getName() + "'s balance is: " + balance);
                                    return Command.SINGLE_SUCCESS;
                                } else {
                                    sender.sendMessage(
                                            "You do not have permission to view other players' balances.");
                                    return Command.SINGLE_SUCCESS;
                                }
                            }

                        })))
                .build();
    }
}
