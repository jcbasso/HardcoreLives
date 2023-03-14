package me.jcbasso.hardcorelives.commands.subcommands;

import me.jcbasso.hardcorelives.i18n.Messages;
import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetLivesCommand implements SubCommand {
    private final LivesManager livesManager;
    private final Messages messages;

    public GetLivesCommand(LivesManager livesManager, Messages messages) {
        this.livesManager = livesManager;
        this.messages = messages;
    }

    @Override
    public String getKey() {
        return "get";
    }

    @Override
    public String getDescription() {
        return messages.getString("cmd_get_description");
    }

    @Override
    public String getSyntax() {
        return messages.getString("cmd_get_syntax");
    }

    @Override
    public String getPermission() {
        return "hardcorelives.get";
    }

    @Override
    public void execute(@NotNull CommandSender sender, String[] args) {
        // Validate exactly 1 arguments
        if (args.length != 1) {
            sender.sendMessage(messages.getString("expected_params", 1, args.length));
            return;
        }
        // Validate first argument is a player
        String playerName = args[0];
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(messages.getString("unknown_player", playerName));
            return;
        }

        // Get lives
        Integer lives = this.livesManager.getLives(target);
        sender.sendMessage(messages.getString("cmd_get_message", playerName, lives));
    }
}
