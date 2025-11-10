package me.sativus.testplugin;

import me.sativus.testplugin.command.Login;
import me.sativus.testplugin.command.Register;
import me.sativus.testplugin.utils.EmailUtil;
import me.sativus.testplugin.utils.HibernateUtil;
import me.sativus.testplugin.handler.OnFreezeListener;
import me.sativus.testplugin.handler.OnPlayerJoinListener;
import me.sativus.testplugin.handler.OnPlayerLeaveListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Testplugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic

        // save default config
        getLogger().info("Saving default config...");
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        saveConfig();

        // Create database connection
        getLogger().info("Initializing database connection...");
        String host = getConfig().getString("database.host");
        int port = getConfig().getInt("database.port");
        String database = getConfig().getString("database.database");
        String username = getConfig().getString("database.username");
        String password = getConfig().getString("database.password");

        HibernateUtil.initialize(host, port, database, username, password);

        // Initialize emailService
        getLogger().info("Initializing emailService...");
        String emailHost = getConfig().getString("email.host");
        int emailPort = getConfig().getInt("email.port");
        String emailUsername = getConfig().getString("email.username");
        String emailPassword = getConfig().getString("email.password");
        String emailFrom = getConfig().getString("email.from");
        EmailUtil.initializeEmailUtil(emailHost, emailPort, emailUsername, emailPassword, emailFrom);

        // Register listeners
        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(new OnPlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new OnPlayerLeaveListener(this), this);
        getServer().getPluginManager().registerEvents(new OnFreezeListener(this), this);

        // Register commands
        getLogger().info("Registering commands...");
        getCommand("register").setExecutor(new Register());
        getCommand("login").setExecutor(new Login());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        HibernateUtil.shutdown();
    }
}
