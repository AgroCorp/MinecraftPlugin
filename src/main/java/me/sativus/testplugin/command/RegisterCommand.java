package me.sativus.testplugin.command;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.utils.BCryptUtil;
import me.sativus.testplugin.utils.EmailUtil;
import org.bukkit.entity.Player;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.Command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.regex.Pattern;

public class RegisterCommand extends BaseCommand {
    private final UserRepository userRepository = new UserRepository();

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player)
                .then(Commands.argument("email", StringArgumentType.string())
                        .then(Commands.argument("password", StringArgumentType.string())
                                .then(Commands.argument("re-password", StringArgumentType.string())
                                        .executes(this::executeRegister))))
                .build();
    }

    private int executeRegister(CommandContext<CommandSourceStack> ctx) {

        Player player = (Player) ctx.getSource().getSender();

        // validate email address
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String email = StringArgumentType.getString(ctx, "email");
        if (!Pattern.matches(emailRegex, email)) {
            player.sendMessage("Invalid email address.");
            return Command.SINGLE_SUCCESS;
        }

        // validate password and repassword same
        String password = StringArgumentType.getString(ctx, "password");
        String repassword = StringArgumentType.getString(ctx, "re-password");
        if (!password.equals(repassword)) {
            player.sendMessage("Passwords do not match.");
            return Command.SINGLE_SUCCESS;
        }

        // validate user does not already has a password
        User user = userRepository.findByUUID(player.getUniqueId());
        if (user != null && user.getPassword() != null) {
            player.sendMessage("You have already registered.");
            return Command.SINGLE_SUCCESS;
        }

        if (user != null) {
            user.setPassword(BCryptUtil.hashPassword(password));
            userRepository.save(user);
            player.sendMessage("Registration successful.");

            EmailUtil.sendEmail(email, "Register Complete",
                    "Your registration has been completed. Now you can login using /login <password>.");

            return Command.SINGLE_SUCCESS;
        }
        return Command.SINGLE_SUCCESS;
    }
}
