package me.jcbasso.hardcorelives.listeners;

import me.jcbasso.hardcorelives.i18n.Messages;
import me.jcbasso.hardcorelives.lives.LivesManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class UpdateDeathsListener implements Listener {
    private final Plugin plugin;
    private final LivesManager livesManager;
    private final Messages messages;

    public UpdateDeathsListener(Plugin plugin, LivesManager livesManager, Messages messages) {
        this.plugin = plugin;
        this.livesManager = livesManager;
        this.messages = messages;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        // Gets entities
        @NotNull Player player = event.getPlayer();
        World world = player.getWorld();

        Integer lives = livesManager.updateLives(player, -1);

        TextComponent message;
        if (lives > 0) {
            // Can revive
            message = Component.text(messages.getString("death_message", player.getName(), lives));
        } else {
            // Can't revive anymore
            message = Component.text(messages.getString("last_death_message", player.getName()));
            this.handleLastDeath(player);
        }

        // Strike lightning effect
        world.strikeLightningEffect(player.getLocation());

        // Death sound
        for (Player hearingPlayer : Bukkit.getOnlinePlayers()) {
            world.playSound(hearingPlayer.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F);
        }

        // Broadcast message
        Bukkit.broadcast(message);
    }

    private void handleLastDeath(Player player) {
        HandlerList.unregisterAll(this);

        // Show Title
        Component mainTitle = Component.text(messages.getString("last_death_title"));
        Component subtitle = Component.text(messages.getString("last_death_subtitle"));
        Title title = Title.title(mainTitle, subtitle);
        player.showTitle(title);

        // Respawn
        player.setGameMode(GameMode.SPECTATOR);

        Location playerLocation = player.getLocation();
        // Schedule task async to teleport so the death finishes
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
                plugin,
                () -> {
                    player.spigot().respawn();
                    player.teleport(playerLocation);
                },
                2L
        );
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
