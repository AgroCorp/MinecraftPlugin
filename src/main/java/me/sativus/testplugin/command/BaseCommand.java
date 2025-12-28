package me.sativus.testplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;

import me.sativus.testplugin.Testplugin;

public abstract class BaseCommand implements CommandExecutor{
    Testplugin plugin;
    public BaseCommand() {
        this.plugin = (Testplugin) Bukkit.getPluginManager().getPlugin("Testplugin");
    }
}
