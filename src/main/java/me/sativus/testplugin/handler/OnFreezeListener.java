package me.sativus.testplugin.handler;

import me.sativus.testplugin.manager.FreezeManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class OnFreezeListener implements Listener {
    private final JavaPlugin plugin;
    private final FreezeManager freezeManager = FreezeManager.getInstance();

    public OnFreezeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (freezeManager.contains(event.getPlayer().getUniqueId())) {
            // Cancel movement by teleporting back to original location
            event.getPlayer().teleport(event.getFrom());
            event.setCancelled(true);
            plugin.getLogger().finest(String.format("Player %s has been frozen. Movement cancelled.", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (freezeManager.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            plugin.getLogger().finest(String.format("Player %s has been frozen. Block breaking cancelled.", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (freezeManager.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            plugin.getLogger().finest(String.format("Player %s has been frozen. Block placing cancelled.", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (freezeManager.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            plugin.getLogger().finest(String.format("Player %s has been frozen. Interacting cancelled.", event.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Set<String> allowedCommands = Set.of("/register", "/login");

        if (freezeManager.contains(event.getPlayer().getUniqueId())) {
            String command = event.getMessage();
            System.out.println(command);
            if (!allowedCommands.contains(command.split(" ")[0])) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("You cannot use commands while frozen.");
                plugin.getLogger().finest(String.format("Player %s has been frozen. Command %s cancelled.", event.getPlayer().getName(), command));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (freezeManager.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot chat while frozen.");
            plugin.getLogger().finest(String.format("Player %s has been frozen. Chat cancelled.", event.getPlayer().getName()));
        }
    }
}
