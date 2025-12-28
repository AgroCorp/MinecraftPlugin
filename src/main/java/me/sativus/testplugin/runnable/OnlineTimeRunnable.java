package me.sativus.testplugin.runnable;

import me.sativus.testplugin.manager.WalletManager;
import me.sativus.testplugin.utils.Config;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OnlineTimeRunnable extends BukkitRunnable {
    private final Plugin plugin;
    private final WalletManager walletManager = WalletManager.getInstance();

    public OnlineTimeRunnable(Plugin plugin) {
        this.plugin = plugin;
        runTaskTimer(plugin, (long) Config.onlineTimeMinutes * (60 * 20), (long) Config.onlineTimeMinutes * (60 * 20));
        plugin.getLogger().info("OnlineTimeRunnable started.");
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            plugin.getLogger().info("Adding online time money to player: " + player.getName());
            walletManager.deposit(player, Config.onlineTimeMoney);
        }
    }
}
