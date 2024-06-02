package plugin.filled_sky24811.info.discord_luckperms.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;

public class discord_luckperms implements CommandExecutor {
    private final Discord_Luckperms plugin;
    public discord_luckperms(Discord_Luckperms plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0){
            commandSender.sendMessage
                    (ChatColor.AQUA + "-----CommandList-----\n" + ChatColor.RESET +
                            "/dl reload\n - configをreloadします。");
            return false;
        }
        switch (strings[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfigFile();
                commandSender.sendMessage("configをreloadしました！");
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "未知のサブコマンドです");
                break;
        }
        return true;
    }
}
