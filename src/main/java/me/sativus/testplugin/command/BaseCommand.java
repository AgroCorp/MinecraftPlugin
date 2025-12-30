package me.sativus.testplugin.command;

import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.sativus.testplugin.Testplugin;

@NullMarked
public abstract class BaseCommand {
    Testplugin plugin;

    public BaseCommand() {
        this.plugin = (Testplugin) Bukkit.getPluginManager().getPlugin("testplugin");
    }

    public abstract LiteralCommandNode<CommandSourceStack> createCommand(final String commandName);
}
