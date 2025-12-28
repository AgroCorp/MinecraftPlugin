package me.sativus.testplugin.command;

import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.sativus.testplugin.manager.WalletManager;

public class Balance implements CommandExecutor {
    private final WalletManager walletManager = WalletManager.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            double balance = walletManager.getBalance(player);
            player.sendMessage("Your balance is: " + Color.GREEN + balance);
        } else {
            commandSender.sendMessage("Only players can use this command.");
        }

        return true;
    }
}
