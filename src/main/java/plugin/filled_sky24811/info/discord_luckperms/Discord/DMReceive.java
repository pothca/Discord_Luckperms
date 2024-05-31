package plugin.filled_sky24811.info.discord_luckperms.Discord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DMReceive extends ListenerAdapter {
    private final Discord_Luckperms plugin;

    public DMReceive(Discord_Luckperms plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentRaw();

        // DMでのメッセージかどうかを確認
        // メッセージを送信したユーザーがBotの場合は処理を終了
        if (!event.isFromType(ChannelType.PRIVATE) || user.isBot()) {
            return;
        }

        File linkIDFolder = new File(plugin.getDataFolder(), "DiscordIDs");
        File[] files = linkIDFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode jsonObject = (ObjectNode) mapper.readTree(file);
                    String verificationCode = jsonObject.get("verificationCode").asText();
                    if (verificationCode.equals(content)) {
                        // 連携用コードが一致した場合の処理
                        File directory = new File(plugin.getDataFolder(),"DiscordIDs");
                        String searchText = jsonObject.get("MinecraftUUID").asText();
                        File return_File = searchFiles(directory,searchText);
                        if(return_File != null){
                            user.openPrivateChannel()
                                    .flatMap(channel -> channel.sendMessage("あなたはもう登録しています"));
                            return;
                        }
                        String discordID = user.getId();
                        String minecraftUUID = jsonObject.get("MinecraftUUID").asText();

                        // JSONファイルに書き込む
                        ObjectNode node = mapper.createObjectNode();
                        node.put("DiscordID", discordID);
                        node.put("MinecraftUUID", minecraftUUID);

                        File linkedFolder = new File(plugin.getDataFolder(), "LinkedMembers");
                        if (!linkedFolder.exists()) {
                            linkedFolder.mkdirs();
                        }
                        File linkedFile = new File(linkedFolder, discordID + ".json");
                        mapper.writeValue(linkedFile, node);

                        Bukkit.getLogger().info("連携用コードが一致しました。");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
