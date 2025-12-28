package me.sativus.testplugin.command;

import org.bukkit.entity.Player;

import me.sativus.testplugin.runnable.OnlineTimeRunnable;
import me.sativus.testplugin.utils.Config;
import me.sativus.testplugin.utils.EmailUtil;
import me.sativus.testplugin.utils.HibernateUtil;

public class Reload extends BaseCommand {
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("You do not have permission to execute this command.");
                return true;
            }

            plugin.getServer().getScheduler().cancelTasks(plugin);

            plugin.reloadConfig();
            plugin.reloadConfigValues();

            HibernateUtil.shutdown();
            HibernateUtil.initialize(Config.host, Config.port, Config.database, Config.username, Config.password);
            EmailUtil.initializeEmailUtil(Config.emailHost, Config.emailPort, Config.emailUsername, Config.emailPassword, Config.emailFrom);

            new OnlineTimeRunnable(plugin);

            player.sendMessage("Configuration reloaded successfully.");
        }
        
        
        return true;
    }
    
}
