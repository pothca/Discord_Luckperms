package plugin.filled_sky24811.info.discord_luckperms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class async {
    private final Discord_Luckperms plugin;
    private JDA jda;
    private final LuckPerms luckPerms;


    public async(Discord_Luckperms plugin, JDA jda, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.jda = jda;
        this.luckPerms = luckPerms;
    }
    public void setJDA(JDA jda) {
        this.jda = jda;
    }

    public void discord_minecraftAdd(String targetID, String targetRole) throws IOException {
        int checker = rolesCheck(targetRole);
        if(checker == 32767) return;

        List<String> settingGroups = plugin.getMinecraftGroups();
        File directory = new File(plugin.getDataFolder(),"LinkedMembers");
        String searchText =  targetID.toString();
        File targetFile = searchFiles(directory, searchText);
        if(targetFile == null) return;

        String content = new String(Files.readAllBytes(Paths.get(targetFile.getAbsolutePath())));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(content);
        String minecraftUUID = rootNode.path("MinecraftUUID").asText();
        if(minecraftUUID == null) return;
        UUID uuid = UUID.fromString(minecraftUUID);

        CompletableFuture<User> userFuture = luckPerms.getUserManager().loadUser(uuid);
        userFuture.thenAccept(user -> {
            if (user != null) {
                String parent = settingGroups.get(checker).replace("group.", "");
                InheritanceNode node = InheritanceNode.builder(parent).build();
                user.data().add(node);
                luckPerms.getUserManager().saveUser(user);
            }
        });
    }

    public void discord_minecraftRemove(String targetID, String targetRole) throws IOException {
        int checker = rolesCheck(targetRole);
        if(checker == 32767) return;

        List<String> settingGroups = plugin.getMinecraftGroups();
        File directory = new File(plugin.getDataFolder(),"LinkedMembers");
        String searchText =  targetID.toString();
        File targetFile = searchFiles(directory, searchText);
        if(targetFile == null) return;

        String content = new String(Files.readAllBytes(Paths.get(targetFile.getAbsolutePath())));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(content);
        String minecraftUUID = rootNode.path("MinecraftUUID").asText();
        if(minecraftUUID == null) return;
        UUID uuid = UUID.fromString(minecraftUUID);

        CompletableFuture<User> userFuture = luckPerms.getUserManager().loadUser(uuid);
        userFuture.thenAccept(user -> {
            if (user != null) {
                String parent = settingGroups.get(checker).replace("group.", "");
                InheritanceNode node = InheritanceNode.builder(parent).build();
                user.data().remove(node);
                luckPerms.getUserManager().saveUser(user);
            }
        });
    }

    public void minecraft_discordAdd(UUID targetUUID, String targetNode){
        int checker = groupsCheck(targetNode);
        if(checker == 32767) return;

        List<String> settingRoles = plugin.getDiscordRoles();
        File directory = new File(plugin.getDataFolder(),"LinkedMembers");
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
        int checker = groupsCheck(targetNode);
        if(checker == 32767) return;

        List<String> settingRoles = plugin.getDiscordRoles();
        File directory = new File(plugin.getDataFolder(),"LinkedMembers");
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

    private int rolesCheck(String targetRole){
        List<String> settingRoles = plugin.getDiscordRoles();
        int count = settingRoles.size();
        for(int i = 0; i < count; i++){
            if(targetRole.equals(settingRoles.get(i))){
                return i;
            }
        }
        return 32767;
    }
    private int groupsCheck(String targetNode){
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
            return content.contains(searchText);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
