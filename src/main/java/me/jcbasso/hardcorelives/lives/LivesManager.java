package me.jcbasso.hardcorelives.lives;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LivesManager {
    private final PersistentDataType<Integer, Integer> livesType = PersistentDataType.INTEGER;
    private final NamespacedKey livesKey;
    private final Integer startingLives;

    public LivesManager(Plugin plugin, FileConfiguration config) {
        this.livesKey = new NamespacedKey(plugin, "lives");
        this.startingLives = config.getInt("lives", 3);
    }

    public Integer getLives(Player player) {
        @NotNull PersistentDataContainer playerDataContainer = player.getPersistentDataContainer();
        Integer lives = playerDataContainer.get(livesKey, livesType);
        if (lives == null) return this.startLives(player);
        return lives;
    }

    public void setLives(Player player, Integer lives) {
        @NotNull PersistentDataContainer playerDataContainer = player.getPersistentDataContainer();
        playerDataContainer.set(livesKey, livesType, lives);
    }

    private Integer startLives(Player player) {
        this.setLives(player, startingLives);
        return startingLives;
    }

    public Integer updateLives(Player player, Integer amount) {
        Integer lives = this.getLives(player);
        lives = lives + amount;
        if (lives < 0) lives = 0;
        this.setLives(player, lives);

        return lives;
    }

    public Integer getStartingLives() {
        return startingLives;
    }
}
