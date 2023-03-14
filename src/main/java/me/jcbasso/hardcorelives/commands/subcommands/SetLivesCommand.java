package me.jcbasso.hardcorelives.commands.subcommands;

import me.jcbasso.hardcorelives.i18n.Messages;
import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetLivesCommand implements SubCommand {
    private final LivesManager livesManager;
    private final Messages messages;

    public SetLivesCommand(LivesManager livesManager, Messages messages) {
        this.livesManager = livesManager;
        this.messages = messages;
    }

    @Override
    public String getKey() {
        return "set";
    }

    @Override
    public String getDescription() {
        return messages.getString("cmd_set_description");
    }

    @Override
    public String getSyntax() {
        return messages.getString("cmd_set_syntax");
    }

    @Override
    public String getPermission() {
        return "hardcorelives.set";
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        // Validate exactly 2 arguments
        if (args.length != 2) {
            sender.sendMessage(messages.getString("expected_params", 2, args.length));
            return;
        }
        // Validate first argument is a player
        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            sender.sendMessage(messages.getString("unknown_player"), playerName);
            return;
        }

        // Validate lives number is integer
        int lives;
        try {
            lives = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(messages.getString("lives_type"), args[1]);
            return;
        }

        // Set lives
        this.livesManager.setLives(target, lives);
    }
}
