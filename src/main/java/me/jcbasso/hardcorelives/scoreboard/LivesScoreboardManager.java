package me.jcbasso.hardcorelives.scoreboard;

import me.jcbasso.hardcorelives.lives.LivesManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

public class LivesScoreboardManager {
    private final Objective objective;
    private final Scoreboard scoreboard;
    private final LivesManager livesManager;

    public LivesScoreboardManager(ScoreboardManager scoreboardManager, LivesManager livesManager) {
        this.scoreboard = scoreboardManager.getMainScoreboard();
        this.livesManager = livesManager;
        String objectiveDisplayName = "Lives";
        String objectiveName = "lives";

        Objective objective1 = this.scoreboard.getObjective(objectiveName);
        if (objective1 == null) {
            Bukkit.getLogger().info("Building objective");
            TextComponent displayName = Component.text(objectiveDisplayName);
            objective1 = this.scoreboard.registerNewObjective(
                    objectiveName,
                    Criteria.DUMMY,
                    displayName,
                    RenderType.HEARTS
            );
            objective1.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }
        this.objective = objective1;
    }

    public void setScore(Player player, Integer value) {
        Score score = objective.getScore(player.getName());
        score.setScore(value * 2); // It is set to * 2 to use hearts
    }

    public void updateScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(this.scoreboard);
            @NotNull PersistentDataContainer playerDataContainer = player.getPersistentDataContainer();

            Integer lives = this.livesManager.getLives(player);
            setScore(player, lives);
        }
    }
}
