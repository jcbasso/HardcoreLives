package me.jcbasso.hardcorelives.commands.subcommands;

import me.jcbasso.hardcorelives.i18n.Messages;
import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReviveCommand implements SubCommand {
    private final LivesManager livesManager;
    private final Messages messages;

    public ReviveCommand(LivesManager livesManager, Messages messages) {
        this.livesManager = livesManager;
        this.messages = messages;
    }

    @Override
    public String getKey() {
        return "revive";
    }

    @Override
    public String getDescription() {
        return messages.getString("cmd_revive_description");
    }

    @Override
    public String getSyntax() {
        return messages.getString("cmd_revive_syntax");
    }

    @Override
    public String getPermission() {
        return "hardcorelives.revive";
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        // Validate exactly 2 arguments
        if (args.length < 1 || args.length > 2 ) {
            sender.sendMessage(messages.getString("expected_either_params", 1, 2, args.length));
            return;
        }
        // Validate first argument is a player
        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(messages.getString("unknown_player", playerName));
            return;
        }

        // Validate lives number is integer and if it doesn't exist set it to starting lives
        int lives;
        if (args.length == 1) {
            lives = livesManager.getStartingLives();
        } else {
            try {
                lives = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                sender.sendMessage(messages.getString("lives_type", args[1]));
                return;
            }
        }

        // Set lives and revive
        this.livesManager.setLives(target, lives);
        target.setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(messages.getString("cmd_revive_message", playerName));
    }
}
