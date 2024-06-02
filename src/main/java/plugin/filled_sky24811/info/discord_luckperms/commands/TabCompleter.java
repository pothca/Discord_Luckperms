package plugin.filled_sky24811.info.discord_luckperms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private final List<String> subCommands = Arrays.asList("reload");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            for (String subCommand : subCommands) {
                if (subCommand.startsWith(args[0])) {
                    suggestions.add(subCommand);
                }
            }
            return suggestions;
        }
        return null;
    }
}