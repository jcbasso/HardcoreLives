package me.jcbasso.hardcorelives.lives;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LivesManager {
    private final PersistentDataType<Integer, Integer> livesType = PersistentDataType.INTEGER;
    private final NamespacedKey livesKey;
    private final Integer startingLives;
    private final String heartsColor;

    public LivesManager(Plugin plugin, FileConfiguration config) {
        this.livesKey = new NamespacedKey(plugin, "lives");

        Integer startingLives_tmp;
        if (config.get("lives") == null) {
            Bukkit.getLogger().severe("Missing 'lives' definition on config.yml");
            startingLives_tmp = 3;
        } else {
            startingLives_tmp = (Integer) config.get("lives");
        }
        this.startingLives = startingLives_tmp;

        String heartsColor;
        if (config.get("hearts_color") == null) {
            Bukkit.getLogger().severe("Missing 'hearts_color' definition on config.yml");
            heartsColor = "red";
        } else {
            heartsColor = (String) config.get("hearts_color");
        }
        this.heartsColor = heartsColor;
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

    public void sendActionBar(Player player) {
        // For lives to be shown you need to have the resource pack added into it if not you will see a square box
        final String charsSeparation = "\uDAFF\uDFFF";
        final String emptyHeartChar = charsSeparation + "\uE000";
        final String redHeartChar = charsSeparation + "\uE001";
        final String goldHeartChar = charsSeparation + "\uE002";

        final String heartChar;
        if (this.heartsColor.equals("gold")) {
            heartChar = goldHeartChar;
        } else {
            heartChar = redHeartChar;
        }

        String[] heartsChars = new String[Math.max(this.startingLives, this.getLives(player))];
        Arrays.fill(heartsChars, emptyHeartChar);

        for (int i = 0; i < this.getLives(player); i++) {
            heartsChars[(heartsChars.length - 1) - i] = heartChar;
        }

        int difference;
        if (heartsChars.length % 2 == 0) {
            difference = 4 + (9 * ((heartsChars.length / 2) - 1));
        } else {
            difference = 9 * (((heartsChars.length + 1) / 2) - 1);
        }

        final String spaces = negativeSpaceToUnicode(87 - difference);
        Component hearts = MiniMessage.miniMessage().deserialize(
                "<color:#4e5c24>" + spaces + String.join("", heartsChars)
        );
        player.sendActionBar(hearts);
    }

    /**
     * Created by <a href="https://github.com/jcbasso">jcbasso</a> on 2023-03-12.
     * <p>
     * Converts number of negative spaces to unicode.
     * This uses <a href="https://github.com/AmberWat/NegativeSpaceFont">AmberWat/NegativeSpaceFont</a> resource pack
     * spaces unicode definition.
     *
     * @param spaceSize number of spaces to offset.
     * @return the string unicode value of the spaces.
     */
    private static String negativeSpaceToUnicode(int spaceSize) {
        final int ZERO_VALUE = 851968; // The starting 0 spaces value defined in AmberWat/NegativeSpaceFont.
        int codePoint = ZERO_VALUE + (spaceSize * 2) - 0x10000; // Multiplying by since it counts as half pixel
        char[] surrogatePair = {(char) ((codePoint >>> 10) + 0xD800), (char) ((codePoint & 0x3FF) + 0xDC00)};
        return new String(surrogatePair);
    }
}
