package me.jcbasso.hardcorelives.tasks;

import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SendActionBars extends BukkitRunnable {
    private final LivesManager livesManager;

    public SendActionBars(LivesManager livesManager) {
        this.livesManager = livesManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            livesManager.sendActionBar(player);
        }

    }
}
