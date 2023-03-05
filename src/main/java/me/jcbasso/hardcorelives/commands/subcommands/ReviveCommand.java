package me.jcbasso.hardcorelives.commands.subcommands;

import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReviveCommand implements SubCommand {
    private final LivesManager livesManager;

    public ReviveCommand(LivesManager livesManager) {
        this.livesManager = livesManager;
    }

    @Override
    public String getKey() {
        return "revive";
    }

    @Override
    public String getDescription() {
        return "Revives player with a given amount of lives";
    }

    @Override
    public String getSyntax() {
        return "revive <player name> [<lives number>]";
    }

    @Override
    public String getPermission() {
        return "hardcorelives.revive";
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        // Validate exactly 2 arguments
        if (args.length < 1 || args.length > 2 ) {
            sender.sendMessage("Expected either 1 or 2 params. Got " + args.length);
            return;
        }
        // Validate first argument is a player
        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("Player " + playerName + " does not exist");
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
                sender.sendMessage("Expected integer number of lives. Got " + args[1]);
                return;
            }
        }

        // Set lives and revive
        this.livesManager.setLives(target, lives);
        target.setGameMode(GameMode.SURVIVAL);
    }
}
