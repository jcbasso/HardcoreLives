package me.jcbasso.hardcorelives.commands.subcommands;

import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetLivesCommand implements SubCommand {
    private final LivesManager livesManager;

    public GetLivesCommand(LivesManager livesManager) {
        this.livesManager = livesManager;
    }

    @Override
    public String getKey() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Get player number of lives";
    }

    @Override
    public String getSyntax() {
        return "get <player name>";
    }

    @Override
    public String getPermission() {
        return "hardcorelives.get";
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        // Validate exactly 1 arguments
        if (args.length != 1) {
            sender.sendMessage("Expected 1 params. Got " + args.length);
            return;
        }
        // Validate first argument is a player
        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("Player " + playerName + " does not exist");
            return;
        }

        // Get lives
        Integer lives = this.livesManager.getLives(target);
        String msg = playerName + " has " + lives + " live";
        if (lives != 1) msg += "s";
        sender.sendMessage(msg);
    }
}
