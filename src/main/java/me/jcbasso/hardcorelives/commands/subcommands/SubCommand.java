package me.jcbasso.hardcorelives.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SubCommand {
    String getKey();

    String getDescription();

    String getSyntax();

    @Nullable
    default String getPermission() {
        return null;
    };

    void execute(@NotNull CommandSender sender, String[] args);
}
