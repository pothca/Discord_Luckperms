package plugin.filled_sky24811.info.discord_luckperms.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;
import plugin.filled_sky24811.info.discord_luckperms.async;

import java.io.IOException;
import java.util.List;

public class discord_luckperms implements CommandExecutor {
    private final Discord_Luckperms plugin;
    private final JDA jda;
    private final async async;
    public discord_luckperms(Discord_Luckperms plugin, JDA jda, async async){
        this.plugin = plugin;
        this.jda = jda;
        this.async = async;
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
            case "reasync":
                List<Member> members= jda.getGuilds().get(0).getMembers();
                for(Member member : members){
                    List<Role> roles = member.getRoles();
                    for(Role role : roles){
                        String Smember = String.valueOf(member);
                        String Srole = String.valueOf(role);
                        try {
                            async.discord_minecraftRemove(Smember, Srole);
                            async.discord_minecraftAdd(Smember, Srole);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                break;
            default:
                commandSender.sendMessage(ChatColor.RED + "未知のサブコマンドです");
                break;
        }
        return true;
    }
}
