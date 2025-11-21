package me.sativus.testplugin.manager;

import me.sativus.testplugin.DAO.Wallet;
import me.sativus.testplugin.Repository.WalletRepository;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class WalletManager {
    private static WalletManager instance;
    private final WalletRepository walletRepository = new WalletRepository();
    private final HashMap<UUID, Wallet> wallets = new HashMap<>();

    private WalletManager() {
    }

    public static WalletManager getInstance() {
        if (instance == null) {
            instance = new WalletManager();
        }
        return instance;
    }

    public void deposit(Player player, double amount) {
        if (wallets.get(player.getUniqueId()) == null) {
            double current = wallets.get(player.getUniqueId()).getBalance();
            wallets.get(player.getUniqueId()).setBalance(current + amount);
        }
    }

    public void withdraw(Player player, double amount) {
        if (wallets.get(player.getUniqueId()) == null) {
            double current = wallets.get(player.getUniqueId()).getBalance();
            wallets.get(player.getUniqueId()).setBalance(current - amount);
        }
    }

    public void addWallet(Player player, Wallet wallet) {
        wallets.put(player.getUniqueId(), wallet);
    }

    public void removeWallet(Player player) {
        walletRepository.save(wallets.get(player.getUniqueId()));
        wallets.remove(player.getUniqueId());
    }
}
