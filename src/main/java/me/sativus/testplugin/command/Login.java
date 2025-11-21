package me.sativus.testplugin.command;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.Repository.WalletRepository;
import me.sativus.testplugin.manager.FreezeManager;
import me.sativus.testplugin.manager.WalletManager;
import me.sativus.testplugin.utils.BCryptUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Login implements CommandExecutor {
    private final UserRepository userRepository = new UserRepository();
    private final WalletManager walletManager = WalletManager.getInstance();
    private final FreezeManager freezeManager = FreezeManager.getInstance();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length != 1) {
            return false;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            User user = userRepository.findByUUIDWithWallet(player.getUniqueId());

            System.out.println(user);
            if (user != null && user.getPassword() != null) {
                System.out.println(strings[0]);

                if (BCryptUtil.checkPassword(strings[0], user.getPassword())) {
                    System.out.println("Password matches.");
                    player.sendMessage("Login successful.");
                    freezeManager.setFrozen(player.getUniqueId(), false);

                    user.setLoggedIn(true);
                    user.setLastLogin(LocalDateTime.now(ZoneId.of("Europe/Budapest")));
                    userRepository.save(user);

                    walletManager.addWallet(player, user.getWallet());

                    return true;
                }
            } else {
                player.sendMessage("You must register before logging in.");
                return true;
            }

        } else {
            commandSender.sendMessage("Only players can use this command.");
            return false;
        }

        return true;
    }
}
