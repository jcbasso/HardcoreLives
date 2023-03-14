package me.jcbasso.hardcorelives;

import me.jcbasso.hardcorelives.actionbar.ActionbarManager;
import me.jcbasso.hardcorelives.actionbar.SendActionBars;
import me.jcbasso.hardcorelives.commands.CommandManager;
import me.jcbasso.hardcorelives.i18n.Messages;
import me.jcbasso.hardcorelives.listeners.UpdateDeathsListener;
import me.jcbasso.hardcorelives.lives.LivesManager;
import me.jcbasso.hardcorelives.placeholderapi.LivesPlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HardcoreLives extends JavaPlugin {

    @Override
    public void onEnable() {
        // Resources
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        saveMessages();
        getMessages();
        Messages messages = new Messages(config, this);
        // Lives Manager
        LivesManager livesManager = new LivesManager(this, config);
        // Commands
        setupCommands(livesManager, messages);
        // Listeners
        setupListeners(livesManager, messages);
        // PlacerHolderAPI
        setupPlaceholderAPI(livesManager);
        // Action Bar
        setupActionBar(livesManager, config);
    }

    private void setupCommands(LivesManager livesManager, Messages messages) {
        CommandManager commandManager = new CommandManager("hcl", livesManager, messages);
        Objects.requireNonNull(getCommand(commandManager.getRootCommand())).setExecutor(commandManager);
    }

    private void setupListeners(LivesManager livesManager, Messages messages) {
        getServer().getPluginManager().registerEvents(new UpdateDeathsListener(this, livesManager, messages), this);
    }

    private void setupPlaceholderAPI(LivesManager livesManager) {
        Plugin placeholderapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderapi != null) {
            new LivesPlaceholderAPI(livesManager).register();
        }
    }

    private void setupActionBar(LivesManager livesManager, FileConfiguration config) {
        ActionbarManager actionbarManager = new ActionbarManager(config, livesManager);

        if (config.getBoolean("actionbar.enabled", false)) {
            new SendActionBars(actionbarManager).runTaskTimer(this, 0L, 10L);
        }
    }

    private void saveMessages() {
        String[] messagesFiles = {"messages.properties", "messages_es.properties"};
        for (String messagesFile : messagesFiles) {
            saveResource(messagesFile, false);
        }
    }

    private void getMessages() {
        String[] messagesFiles = {"messages.properties", "messages_es.properties"};
        for (String messagesFile : messagesFiles) {
            getResource(messagesFile);
        }
    }
}
