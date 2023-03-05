package me.jcbasso.hardcorelives.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LivesPlaceholderAPI extends PlaceholderExpansion {


    private final LivesManager livesManager;

    public LivesPlaceholderAPI(LivesManager livesManager) {
        this.livesManager = livesManager;
    }

    @Override
    public @NotNull String getAuthor() {
        return "jcbasso";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hardcorelives";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        // Only working with only players
        Player onlinePlayer = player.getPlayer();
        if (onlinePlayer == null) return "Player offline";

        // %hardcorelives_lives%
        if (params.equals("lives")) {
            return livesManager.getLives(onlinePlayer).toString();
        }

        // %hardcorelives_starting_lives%
        if (params.equals("starting_lives")) {
            return livesManager.getStartingLives().toString();
        }

        return null;
    }
}
