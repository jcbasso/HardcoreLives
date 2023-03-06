package me.jcbasso.hardcorelives.listeners;

import me.jcbasso.hardcorelives.lives.LivesManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class UpdateDeathsListener implements Listener {
    private final LivesManager livesManager;
    private final Plugin plugin;

    public UpdateDeathsListener(Plugin plugin, LivesManager livesManager) {
        this.plugin = plugin;
        this.livesManager = livesManager;
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
            message = this.getRevivingDeathMessage(player, lives);
        } else {
            // Can't revive anymore
            message = this.getLastDeathMessage(player);
            this.handleLastDeath(player);
        }

        // Strike lightning effect
        world.strikeLightningEffect(player.getLocation());

        // Death sound
        for (Player hearingPlayer : Bukkit.getOnlinePlayers()) {
            world.playSound(hearingPlayer.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F);
        }

        // Broadcast message
        event.deathMessage(message);
    }

    private TextComponent getRevivingDeathMessage(Player player, Integer lives) {
        return Component.text()
                .append(Component.text(player.getName(), NamedTextColor.RED))
                .append(Component.text(" ha muerto. Le quedan ", NamedTextColor.GOLD))
                .append(Component.text(lives, NamedTextColor.RED))
                .append(Component.text(" vidas.", NamedTextColor.GOLD))
                .build()
                ;
    }

    private TextComponent getLastDeathMessage(Player player) {
        return Component.text()
                .append(Component.text(player.getName(), NamedTextColor.RED))
                .append(Component.text(" ha muerto por ultima vez!", NamedTextColor.RED))
                .build()
                ;
    }

    private void handleLastDeath(Player player) {
        // Respawn
        Location respawnLocation = player.getLocation();
        player.setGameMode(GameMode.SPECTATOR);
        Bukkit.getScheduler().runTask(this.plugin,
                () ->
                {
                    player.spigot().respawn();
                    player.teleport(respawnLocation); // Using player.setBedSpawnLocation forced sometimes has obstruction problem
                }
        );

        // Show Title
        Component mainTitle = Component.text("Has muerto!", NamedTextColor.RED);
        Component subtitle = Component.text("ahora eres un espectador", NamedTextColor.GRAY);
        Title title = Title.title(mainTitle, subtitle);
        player.showTitle(title);
    }
}
