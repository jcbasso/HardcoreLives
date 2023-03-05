package me.jcbasso.hardcorelives.tasks;

import me.jcbasso.hardcorelives.scoreboard.LivesScoreboardManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateScoreboards extends BukkitRunnable {
    private final LivesScoreboardManager livesScoreboardManager;

    public UpdateScoreboards(LivesScoreboardManager livesScoreboardManager) {
        this.livesScoreboardManager = livesScoreboardManager;
    }

    @Override
    public void run() {
        livesScoreboardManager.updateScoreboards();
    }
}
