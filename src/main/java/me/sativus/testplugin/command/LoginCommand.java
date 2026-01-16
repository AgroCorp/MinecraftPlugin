package me.sativus.testplugin.command;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.manager.FreezeManager;
import me.sativus.testplugin.manager.WalletManager;
import me.sativus.testplugin.utils.BCryptUtil;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.time.LocalDateTime;
import java.time.ZoneId;

@NullMarked
public class LoginCommand extends BaseCommand {
    private final UserRepository userRepository = new UserRepository();
    private final WalletManager walletManager = WalletManager.getInstance();
    private final FreezeManager freezeManager = FreezeManager.getInstance();

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand(final String commandName) {
        return Commands.literal(commandName)
                .requires(sender -> sender.getExecutor() instanceof Player)
                .then(Commands.argument("password", StringArgumentType.greedyString())
                        .executes(this::executeLogin))
                .build();
    }

    private int executeLogin(CommandContext<CommandSourceStack> ctx) {

        Player player = (Player) ctx.getSource().getSender();
        User user = userRepository.findByUUIDWithFetch(player.getUniqueId(), "wallet,");

        String password = StringArgumentType.getString(ctx, "password");

        if (user != null && user.getPassword() != null) {
            if (user.getLoggedIn() != null && user.getLoggedIn()) {
                player.sendMessage("You are already logged in.");
                return Command.SINGLE_SUCCESS;
            }

            if (BCryptUtil.checkPassword(password, user.getPassword())) {
                this.plugin.getLogger().info(String.format("Player %s has logged in.", player.getName()));
                player.sendMessage("Login successful.");
                freezeManager.setFrozen(player.getUniqueId(), false);

                user.setLoggedIn(true);
                user.setLastLogin(LocalDateTime.now(ZoneId.of("Europe/Budapest")));
                userRepository.save(user);

                walletManager.addWallet(player, user.getWallet());

                return Command.SINGLE_SUCCESS;
            } else {
                player.sendMessage("Incorrect password. Please try again.");
                return Command.SINGLE_SUCCESS;
            }
        } else {
            player.sendMessage("No account found. Please register first.");
            return Command.SINGLE_SUCCESS;
        }
    }
}
