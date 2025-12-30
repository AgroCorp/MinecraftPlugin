package me.sativus.testplugin;

import me.sativus.testplugin.command.LoginCommand;
import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.UserRepository;
import me.sativus.testplugin.command.BalanceCommand;
import me.sativus.testplugin.command.FreezeCommand;
import me.sativus.testplugin.command.RegisterCommand;
import me.sativus.testplugin.command.ReloadCommand;
import me.sativus.testplugin.command.UnfreezeCommand;
import me.sativus.testplugin.runnable.OnlineTimeRunnable;
import me.sativus.testplugin.utils.EmailUtil;
import me.sativus.testplugin.utils.HibernateUtil;
import me.sativus.testplugin.utils.Config;
import me.sativus.testplugin.handler.OnFreezeListener;
import me.sativus.testplugin.handler.OnPlayerJoinListener;
import me.sativus.testplugin.handler.OnPlayerLeaveListener;
import me.sativus.testplugin.manager.WalletManager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

public final class Testplugin extends JavaPlugin {
    WalletManager walletManager = WalletManager.getInstance();
    UserRepository userRepository = new UserRepository();

    @Override
    public void onEnable() {
        // Plugin startup logic

        // save default config
        getLogger().info("Saving default config...");
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfigValues();

        // Create database connection
        getLogger().info("Initializing database connection...");
        HibernateUtil.initialize(Config.host, Config.port, Config.database, Config.username, Config.password);

        // Initialize emailService
        getLogger().info("Initializing emailService...");
        EmailUtil.initializeEmailUtil(Config.emailHost, Config.emailPort, Config.emailUsername, Config.emailPassword,
                Config.emailFrom);

        // Register listeners
        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(new OnPlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new OnFreezeListener(this), this);

        // Register commands
        getLogger().info("Registering commands...");
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(new LoginCommand().createCommand("login"));
            commands.registrar().register(new RegisterCommand().createCommand("register"));
            commands.registrar().register(new BalanceCommand().createCommand("balance"));
            commands.registrar().register(new FreezeCommand().createCommand("freeze"));
            commands.registrar().register(new UnfreezeCommand().createCommand("unfreeze"));
            commands.registrar().register(new ReloadCommand().createCommand("reload"));
        });

        // Start runnables
        getLogger().info("Starting runnables...");
        new OnlineTimeRunnable(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Stop runnables
        getLogger().info("Stopping runnables...");
        getServer().getScheduler().cancelTasks(this);

        // Save config
        getLogger().info("Saving config...");
        saveConfig();

        // Save all wallets and remove them from WalletManager
        getLogger().info("Saving wallets...");
        walletManager.removeAllWallets();

        // Log out all users
        getLogger().info("Logging out users...");
        for (Player player : getServer().getOnlinePlayers()) {
            User user = userRepository.findByUUID(player.getUniqueId());
            if (user != null && user.getLoggedIn()) {
                user.setLoggedIn(false);
                userRepository.save(user);
            }
        }

        // Shutdown database connection
        getLogger().info("Shutting down database connection...");
        HibernateUtil.shutdown();
    }

    public void reloadConfigValues() {
        Config.host = getConfig().getString("database.host");
        Config.port = getConfig().getInt("database.port");
        Config.database = getConfig().getString("database.database");
        Config.username = getConfig().getString("database.username");
        Config.password = getConfig().getString("database.password");

        Config.emailHost = getConfig().getString("email.host");
        Config.emailPort = getConfig().getInt("email.port");
        Config.emailUsername = getConfig().getString("email.username");
        Config.emailPassword = getConfig().getString("email.password");
        Config.emailFrom = getConfig().getString("email.from");

        Config.onlineTimeMoney = getConfig().getDouble("online-time-money");
        Config.onlineTimeMinutes = getConfig().getInt("online-time-minutes");
    }
}
