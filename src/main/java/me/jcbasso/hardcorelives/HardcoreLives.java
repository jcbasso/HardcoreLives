package me.jcbasso.hardcorelives;

import me.jcbasso.hardcorelives.commands.CommandManager;
import me.jcbasso.hardcorelives.listeners.UpdateDeathsListener;
import me.jcbasso.hardcorelives.lives.LivesManager;
import me.jcbasso.hardcorelives.placeholderapi.LivesPlaceholderAPI;
import me.jcbasso.hardcorelives.scoreboard.LivesScoreboardManager;
import me.jcbasso.hardcorelives.tasks.SendActionBars;
import me.jcbasso.hardcorelives.tasks.UpdateScoreboards;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Objects;

public final class HardcoreLives extends JavaPlugin {

    @Override
    public void onEnable() {
        // Config
        FileConfiguration config = setupConfigFile();
        // Lives Manager
        LivesManager livesManager = new LivesManager(this, config);
        // Commands
        setupCommands(livesManager);
        // Listeners
        setupListeners(livesManager);
        // PlacerHolderAPI
        setupPlaceholderAPI(livesManager);
        // Scoreboard
        LivesScoreboardManager livesScoreboardManager = setupLivesScorer(livesManager);
        // Tasks
        setupTasks(livesScoreboardManager, livesManager, config);
    }

    private FileConfiguration setupConfigFile() {
        saveDefaultConfig();
        return getConfig();
    }

    private void setupCommands(LivesManager livesManager) {
        CommandManager commandManager = new CommandManager(livesManager, "hcl");
        Objects.requireNonNull(getCommand(commandManager.rootCommand)).setExecutor(commandManager);
    }

    private LivesScoreboardManager setupLivesScorer(LivesManager livesManager) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        return new LivesScoreboardManager(scoreboardManager, livesManager);
    }

    private void setupTasks(LivesScoreboardManager livesScoreboardManager, LivesManager livesManager, FileConfiguration config) {
        new UpdateScoreboards(livesScoreboardManager).runTaskTimer(this, 0L, 40L);

        if (config.get("actionbar") == null) Bukkit.getLogger().severe("Missing 'actionbar' definition on config.yml");
        else if ((boolean) config.get("actionbar")) new SendActionBars(livesManager).runTaskTimer(this, 0L, 10L);
    }

    private void setupListeners(LivesManager livesManager) {
        getServer().getPluginManager().registerEvents(new UpdateDeathsListener(livesManager), this);
    }

    private void setupPlaceholderAPI(LivesManager livesManager) {
        Plugin placeholderapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderapi != null) {
            new LivesPlaceholderAPI(livesManager).register();
        }
    }
}
