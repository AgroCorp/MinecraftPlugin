package me.sativus.testplugin.command;

import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.sativus.testplugin.runnable.OnlineTimeRunnable;
import me.sativus.testplugin.utils.Config;
import me.sativus.testplugin.utils.EmailUtil;
import me.sativus.testplugin.utils.HibernateUtil;

public class ReloadCommand extends BaseCommand {

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand(String commandName) {
        return Commands.literal(commandName)
                .requires(Commands.restricted(source -> source.getSender() instanceof Player
                        && source.getSender().hasPermission("testplugin.reload")))
                .executes(this::executeReload)
                .build();
    }

    private int executeReload(CommandContext<CommandSourceStack> ctx) {
        Player player = (Player) ctx.getSource().getSender();

        plugin.getServer().getScheduler().cancelTasks(plugin);

        plugin.reloadConfig();
        plugin.reloadConfigValues();

        HibernateUtil.shutdown();
        HibernateUtil.initialize(Config.host, Config.port, Config.database, Config.username, Config.password);
        EmailUtil.initializeEmailUtil(Config.emailHost, Config.emailPort, Config.emailUsername, Config.emailPassword,
                Config.emailFrom);

        new OnlineTimeRunnable(plugin);

        player.sendMessage("Configuration reloaded successfully.");
        return Command.SINGLE_SUCCESS;
    }

}
