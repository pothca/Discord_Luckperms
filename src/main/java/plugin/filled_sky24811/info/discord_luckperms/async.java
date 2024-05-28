package plugin.filled_sky24811.info.discord_luckperms;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class async {
    private final Discord_Luckperms plugin;
    private final JDA jda;

    public async(Discord_Luckperms plugin, JDA jda) {
        this.plugin = plugin;
        this.jda = jda;
    }

    public void minecraft_discordAdd(UUID targetUUID, String targetNode){
        int checker = rolesCheck(targetNode);
        if(checker == 32767) return;

        List<String> settingRoles = plugin.getDiscordRoles();
        File directory = plugin.getDataFolder();
        String searchText = targetUUID.toString();
        File targetFile = searchFiles(directory, searchText);
        if(targetFile == null) return;

        String discordID = targetFile.getName().replace(".json", "");
        Guild guild = jda.getGuildById(plugin.getGuildId());
        if (guild == null) return;

        Member member = guild.getMemberById(discordID);
        if (member == null) return;

        Role role = guild.getRoleById(settingRoles.get(checker));
        if (role == null) return;

        guild.addRoleToMember(member, role).queue();
    }

    public void minecraft_discordRemove(UUID targetUUID, String targetNode){
        int checker = rolesCheck(targetNode);
        if(checker == 32767) return;

        List<String> settingRoles = plugin.getDiscordRoles();
        File directory = plugin.getDataFolder();
        String searchText = targetUUID.toString();
        File targetFile = searchFiles(directory, searchText);
        if(targetFile == null) return;

        String discordID = targetFile.getName().replace(".json", "");
        Guild guild = jda.getGuildById(plugin.getGuildId());
        if (guild == null) return;

        Member member = guild.getMemberById(discordID);
        if (member == null) return;

        Role role = guild.getRoleById(settingRoles.get(checker));
        if (role == null) return;

        guild.removeRoleFromMember(member, role).queue();
    }

    private int rolesCheck(String targetNode){
        List<String> settingGroups = plugin.getMinecraftGroups();
        int count = settingGroups.size();
        for(int i = 0; i < count; i++){
            if(targetNode.equals(settingGroups.get(i))){
                return i;
            }
        }
        return 32767;
    }

    private File searchFiles(File directory, String searchText) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // ディレクトリの場合は再帰的に探索
                    File result = searchFiles(file, searchText);
                    if (result != null) {
                        return result;
                    }
                } else {
                    // ファイルの場合はテキストを検索
                    if (file.getName().endsWith(".json")) {
                        if (searchTextInFile(file, searchText)) {
                            return file;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean searchTextInFile(File file, String searchText) {
        try {
            // ファイルの中身を文字列として読み込む
            String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            Bukkit.getLogger().info("ファイルの中身確認までは動いてるのよん");
            return content.contains(searchText);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
