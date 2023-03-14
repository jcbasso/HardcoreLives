package me.jcbasso.hardcorelives.commands;

import me.jcbasso.hardcorelives.commands.subcommands.GetLivesCommand;
import me.jcbasso.hardcorelives.commands.subcommands.ReviveCommand;
import me.jcbasso.hardcorelives.commands.subcommands.SetLivesCommand;
import me.jcbasso.hardcorelives.commands.subcommands.SubCommand;
import me.jcbasso.hardcorelives.i18n.Messages;
import me.jcbasso.hardcorelives.lives.LivesManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {
    private final String rootCommand;
    private final Map<String, SubCommand> subCommands;
    private final Messages messages;

    public CommandManager(String rootCommand, LivesManager livesManager, Messages messages) {
        this.rootCommand = rootCommand;
        this.messages = messages;

        subCommands = new HashMap<>();
        this.register(new SetLivesCommand(livesManager, messages));
        this.register(new GetLivesCommand(livesManager, messages));
        this.register(new ReviveCommand(livesManager, messages));
    }

    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            Component options = Component.text(messages.getString("options") + ":", NamedTextColor.GOLD);
            sender.sendMessage(options);
            for (String key : subCommands.keySet()) {
                Component subcommand_options = Component.text()
                        .append(Component.text("/" + command.getName() + " " + subCommands.get(key).getKey() + " " + subCommands.get(key).getSyntax(), NamedTextColor.RED))
                        .append(Component.text(" - " + subCommands.get(key).getDescription(), NamedTextColor.GOLD))
                        .build();
                sender.sendMessage(subcommand_options);
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
            Component permission_message = Component.text()
                    .append(Component.text(messages.getString("missing_cmd_permission") + " ", NamedTextColor.GOLD))
                    .append(Component.text("/" + command.getName() + " " + subcommand.getKey(), NamedTextColor.RED))
                    .build();
            sender.sendMessage(permission_message);
            return false;
        }

        subcommand.execute(sender, args);
        return true;
    }
}
