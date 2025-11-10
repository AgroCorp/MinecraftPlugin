package me.sativus.testplugin.command;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.utils.BCryptUtil;
import me.sativus.testplugin.utils.EmailUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class Register implements CommandExecutor {
    private final UserRepository userRepository = new UserRepository();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length != 3) {
            return false;
        }
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            // validate email address
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            String email = strings[0];
            if (!Pattern.matches(emailRegex, email)) {
                player.sendMessage("Invalid email address.");
                return false;
            }

            // validate password and repassword same
            String password = strings[1];
            String repassword = strings[2];
            if (!password.equals(repassword)) {
                player.sendMessage("Passwords do not match.");
                return false;
            }

            // validate user does not already has a password
            User user = userRepository.findByUUID(player.getUniqueId());
            if (user != null && user.getPassword() != null) {
                player.sendMessage("You have already registered.");
                return true;
            }

            if (user != null) {
                user.setPassword(BCryptUtil.hashPassword(password));
                userRepository.save(user);
                player.sendMessage("Registration successful.");

                EmailUtil.sendEmail(email, "Register Complete", "Your registration has been completed. Now you can login using /login <password>.");

                return true;
            }

        } else {
            commandSender.sendMessage("Only players can use this command.");
        }

        return true;
    }
}
