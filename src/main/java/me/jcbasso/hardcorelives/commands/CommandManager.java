package me.jcbasso.hardcorelives.commands;

import me.jcbasso.hardcorelives.commands.subcommands.GetLivesCommand;
import me.jcbasso.hardcorelives.commands.subcommands.ReviveCommand;
import me.jcbasso.hardcorelives.commands.subcommands.SetLivesCommand;
import me.jcbasso.hardcorelives.commands.subcommands.SubCommand;
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
            Component options = Component.text("Opciones:", NamedTextColor.GOLD);
            sender.sendMessage(options);
            for (String key : subCommands.keySet()) {
                Component subcommand_options = Component.text()
                        .append(Component.text("/" + command.getName() + " " + subCommands.get(key).getSyntax(), NamedTextColor.RED))
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
                    .append(Component.text("No tienes permisos para ejecutar ", NamedTextColor.GOLD))
                    .append(Component.text("/" + command.getName() + " " + subcommand.getKey(), NamedTextColor.RED))
                    .build();
            sender.sendMessage(permission_message);
            return false;
        }

        subcommand.execute(sender, args);
        return true;
    }
}
