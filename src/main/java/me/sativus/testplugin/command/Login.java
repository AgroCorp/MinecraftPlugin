package me.sativus.testplugin.command;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.handler.FreezeManager;
import me.sativus.testplugin.utils.BCryptUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Login implements CommandExecutor {
    private final UserRepository userRepository = new UserRepository();
    private final FreezeManager freezeManager = FreezeManager.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length != 1) {
            return false;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            User user = userRepository.findByUUID(player.getUniqueId());

            System.out.println(user);
            if (user != null && user.getPassword() != null) {
                System.out.println(strings[0]);

                if (BCryptUtil.checkPassword(strings[0], user.getPassword())) {
                    System.out.println("Password matches.");
                    player.sendMessage("Login successful.");
                    freezeManager.setFrozen(player.getUniqueId(), false);

                    user.setLoggedIn(true);
                    userRepository.save(user);
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
