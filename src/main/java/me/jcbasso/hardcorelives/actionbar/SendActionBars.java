package me.jcbasso.hardcorelives.actionbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SendActionBars extends BukkitRunnable {
    private final ActionbarManager actionbarManager;

    public SendActionBars(ActionbarManager actionbarManager) {
        this.actionbarManager = actionbarManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            actionbarManager.sendActionBar(player);
        }

    }
}
