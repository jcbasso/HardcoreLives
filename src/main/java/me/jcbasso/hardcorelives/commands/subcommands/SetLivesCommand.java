package me.jcbasso.hardcorelives.commands.subcommands;

import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetLivesCommand implements SubCommand {
    private final LivesManager livesManager;

    public SetLivesCommand(LivesManager livesManager) {
        this.livesManager = livesManager;
    }

    @Override
    public String getKey() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "Set player number of lives";
    }

    @Override
    public String getSyntax() {
        return "set <player name> <lives number>";
    }

    @Override
    public String getPermission() {
        return "hardcorelives.set";
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        // Validate exactly 2 arguments
        if (args.length != 2) {
            sender.sendMessage("Expected 2 params. Got " + args.length);
            return;
        }
        // Validate first argument is a player
        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("Player " + playerName + " does not exist");
            return;
        }

        // Validate lives number is integer
        int lives;
        try {
            lives = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            sender.sendMessage("Expected integer number of lives. Got " + args[1]);
            return;
        }

        // Set lives
        this.livesManager.setLives(target, lives);
    }
}
