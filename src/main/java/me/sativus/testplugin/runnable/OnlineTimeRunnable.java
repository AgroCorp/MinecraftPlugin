package me.sativus.testplugin.runnable;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.manager.WalletManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OnlineTimeRunnable extends BukkitRunnable {
    private final Plugin plugin;
    private final double onlineMoney;
    private final UserRepository userRepository = new UserRepository();
    private final WalletManager walletManager = WalletManager.getInstance();

    public OnlineTimeRunnable(Plugin plugin, double onlineMoney, int onlineTimeMinutes) {
        this.plugin = plugin;
        this.onlineMoney = onlineMoney;
        runTaskTimer(plugin, 0L, (long) onlineTimeMinutes * 60 * 20);
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            User user = userRepository.findByUUIDWithWallet(player.getUniqueId());
            if (user != null && user.getWallet() != null) {
                walletManager.deposit(player, onlineMoney);
            }
        }
    }
}
