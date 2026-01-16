package me.sativus.testplugin.handler;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.sativus.testplugin.DAO.Salary;
import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.manager.WalletManager;

public class OnBreakBlock implements Listener {
    private final UserRepository userRepository = new UserRepository();
    private final WalletManager walletManager = WalletManager.getInstance();

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        User user = userRepository.findByUUIDWithFetch(player.getUniqueId(), "job,");

        if (user != null && user.getJob() != null) {
            for (Salary salary : user.getJob().getSalaries()) {
                if (salary.getName().equals(block.getType().name())) {
                    walletManager.deposit(player, salary.getSalary());
                }
            }
        }
    }

}
