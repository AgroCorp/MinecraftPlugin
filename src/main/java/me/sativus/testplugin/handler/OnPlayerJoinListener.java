package me.sativus.testplugin.handler;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.manager.FreezeManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnPlayerJoinListener implements Listener {
    private final UserRepository userRepository = new UserRepository();
    private final FreezeManager freezeManager = FreezeManager.getInstance();
    private final JavaPlugin plugin;

    public OnPlayerJoinListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = userRepository.getOrCreate(player.getUniqueId(), player.getName());

        if (!player.isOp()) {
            freezeManager.setFrozen(player.getUniqueId(), true);
            plugin.getLogger().info(String.format("Player %s has joined the server. Add to freezed players", player.getName()));
            player.setVisibleByDefault(false);
        }

        if (user.getPassword() == null) {
            player.sendMessage("Welcome to the server!");
            player.sendMessage("Please register using /register <email> <password> <confirmPassword>");
        } else {
            player.sendMessage("Welcome back!");
            player.sendMessage("Please login using /login <password>");
        }
    }

}
