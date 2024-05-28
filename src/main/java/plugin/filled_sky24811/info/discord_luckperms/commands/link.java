package plugin.filled_sky24811.info.discord_luckperms.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class link implements CommandExecutor {
    private final Discord_Luckperms plugin;
    private final JDA jda;
    private final SecureRandom random = new SecureRandom();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public link(Discord_Luckperms plugin, JDA jda){
        this.plugin = plugin;
        this.jda = jda;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("このコマンドはプレイヤーからのみ実行可能です");
            return true;
        }
        if(strings.length < 1){
            commandSender.sendMessage("ユーザーIDを入力してください");
            return true;
        }
        String discordID = strings[0];
        Guild guild = jda.getGuildById(plugin.getGuildId());
        if (guild == null) {
            commandSender.sendMessage("ボットが参加しているサーバーが見つかりませんでした");
            return true;
        }

        // メンバーの存在チェックを修正
        Member member = guild.getMemberById(discordID);
        Bukkit.getLogger().info("1141724987628212314");
        Bukkit.getLogger().info(discordID);
        Bukkit.getLogger().info(String.valueOf(member));
        if (member == null) {
            commandSender.sendMessage("指定された Discord ID はこのサーバーに存在しません。正しい Discord ID を入力してください。");
            return true;
        }

        String verificationCode = generateVerificationCode();
        UUID player = ((Player) commandSender).getUniqueId();
        saveLinkData(discordID, verificationCode, player);
        commandSender.sendMessage("連携用コード: " + verificationCode + " をDiscordのDMに入力してください。");
        return true;
    }

    private String generateVerificationCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void saveLinkData(String discordID, String verificationCode, UUID player) {
        File linkIDFolder = new File(plugin.getDataFolder(), "DiscordIDs");
        if (!linkIDFolder.exists()) {
            linkIDFolder.mkdirs();
        }
        File file = new File(linkIDFolder, discordID + ".json");
        Map<String, String> data = new HashMap<>();
        data.put("verificationCode", verificationCode);
        data.put("MinecraftUUID", String.valueOf(player));

        try {
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
