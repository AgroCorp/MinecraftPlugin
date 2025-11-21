package me.sativus.testplugin.handler;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.manager.FreezeManager;
import me.sativus.testplugin.manager.WalletManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnPlayerLeaveListener implements Listener {
    private final FreezeManager freezeManager = FreezeManager.getInstance();
    private final WalletManager walletManager = WalletManager.getInstance();
    private final JavaPlugin plugin;
    private final UserRepository userRepository = new UserRepository();

    public OnPlayerLeaveListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = userRepository.findByUUID(player.getUniqueId());

        if (user != null) {
            user.setLoggedIn(false);
            userRepository.save(user);
        }

        freezeManager.setFrozen(player.getUniqueId(), false);
        walletManager.removeWallet(player);

        plugin.getLogger().info(String.format("Player %s has left the server. Remove from freezed players", player.getName()));
    }
}
