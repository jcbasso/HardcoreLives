package me.jcbasso.hardcorelives.commands;

import me.jcbasso.hardcorelives.commands.subcommands.GetLivesCommand;
import me.jcbasso.hardcorelives.commands.subcommands.ReviveCommand;
import me.jcbasso.hardcorelives.commands.subcommands.SetLivesCommand;
import me.jcbasso.hardcorelives.commands.subcommands.SubCommand;
import me.jcbasso.hardcorelives.lives.LivesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {
    public final String rootCommand;
    private final Map<String, SubCommand> subCommands;

    public CommandManager(LivesManager livesManager, String rootCommand) {
        this.rootCommand = rootCommand;

        subCommands = new HashMap<>();
        this.register(new SetLivesCommand(livesManager));
        this.register(new GetLivesCommand(livesManager));
        this.register(new ReviveCommand(livesManager));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("&" + ChatColor.GOLD.getChar() + "Options:");
            for (String key : subCommands.keySet()) {
                sender.sendMessage("&" + ChatColor.RED.getChar() + "/" + command.getName() + " " + subCommands.get(key).getSyntax() + "&" + ChatColor.GOLD.getChar() + " - " + subCommands.get(key).getDescription());
            }
            return true;
        }
        SubCommand subcommand = subCommands.get(args[0]);
        if (subcommand == null) return false;

        return executeSubCommand(sender, command, subcommand, Arrays.copyOfRange(args, 1, args.length));
    }

    private void register(SubCommand command) {
        subCommands.put(command.getKey(), command);
    }

    private boolean executeSubCommand(@NotNull CommandSender sender, @NotNull Command command, SubCommand subcommand, @NotNull String[] args) {
        if (subcommand.getPermission() != null && !sender.hasPermission(subcommand.getPermission())) {
            sender.sendMessage("&" + ChatColor.GOLD.getChar() + "You don't have permission to execute " + "&" + ChatColor.RED.getChar() + "/" + command.getName() +  " " + subcommand.getKey());
            return false;
        }

        subcommand.execute(sender, args);
        return true;
    }
}
