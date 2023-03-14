package me.jcbasso.hardcorelives.actionbar;

import me.jcbasso.hardcorelives.lives.LivesManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ActionbarManager {
    private final LivesManager livesManager;
    private final HeartColor heartColor;
    private final Boolean showEmptyHearts;

    public ActionbarManager(FileConfiguration config, LivesManager livesManager) {
        this.livesManager = livesManager;
        this.heartColor = HeartColor.fromString(config.getString("actionbar.hearts_color", HeartColor.RED.toString()));
        this.showEmptyHearts = config.getObject("actionbar.show_empty", Boolean.class, false);
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
        int codePoint = ZERO_VALUE + (spaceSize * 2) - 0x10000; // Multiplying by 2 since it apparently counts as half pixel
        char[] surrogatePair = {(char) ((codePoint >>> 10) + 0xD800), (char) ((codePoint & 0x3FF) + 0xDC00)};
        return new String(surrogatePair);
    }

    public void sendActionBar(Player player) {
        // For lives to be shown you need to have the resource pack added into it if not you will see a square box
        final String charsSpaceOffset = "\uDAFF\uDFFF"; // -1 space offset to be closer
        final String noShadowColor = "<color:#4e5c24>"; // Hides actionbar shadow when using resource pack

        Integer playerLives = this.livesManager.getLives(player);
        int totalLivesSlots = Math.max(this.livesManager.getStartingLives(), playerLives);
        if (!this.showEmptyHearts) totalLivesSlots = playerLives;

        String[] heartsChars = new String[totalLivesSlots];
        Arrays.fill(heartsChars, HeartColor.EMPTY.getUnicode());

        for (int i = 0; i < playerLives; i++) {
            heartsChars[(heartsChars.length - 1) - i] = charsSpaceOffset + this.heartColor.getUnicode();
        }

        int difference;
        if (heartsChars.length % 2 == 0) {
            difference = 4 + (9 * ((heartsChars.length / 2) - 1));
        } else {
            difference = 9 * (((heartsChars.length + 1) / 2) - 1);
        }

        final String spaces = negativeSpaceToUnicode(87 - difference);
        Component hearts = MiniMessage.miniMessage().deserialize(noShadowColor + spaces + String.join("", heartsChars));
        player.sendActionBar(hearts);
    }
}
