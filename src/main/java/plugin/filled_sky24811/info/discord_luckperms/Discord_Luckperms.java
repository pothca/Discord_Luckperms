package plugin.filled_sky24811.info.discord_luckperms;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.filled_sky24811.info.discord_luckperms.Discord.BotReady;
import plugin.filled_sky24811.info.discord_luckperms.Discord.DMReceive;
import plugin.filled_sky24811.info.discord_luckperms.commands.link;
import plugin.filled_sky24811.info.discord_luckperms.minecraft.MEventListener;

import java.util.List;

public final class Discord_Luckperms extends JavaPlugin {
    private MEventListener MListener;
    private BotReady botReady;
    private JDA jda;
    private String guildId;

    public Discord_Luckperms() {}

    // onEnableで初期化するために必要なコンストラクタ
    public Discord_Luckperms(MEventListener mListener) {
        this.MListener = mListener;
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("プラグインが有効化されました！");
        saveDefaultConfig();
        getDataFolder();

        // Discord Botの起動
        String token = getConfig().getString("Discord.token");
        if (token == null || token.isEmpty()) {
            Bukkit.getLogger().severe("Discordトークンが設定ファイルに含まれていません。プラグインを無効化します。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            botReady = new BotReady(this);
            this.jda = botReady.startBot(token);
            if (jda != null) {
                jda.awaitReady();
                // 最初のギルドIDを取得
                List<Guild> guilds = jda.getGuilds();
                if (!guilds.isEmpty()) {
                    this.guildId = guilds.get(0).getId();
                } else {
                    Bukkit.getLogger().severe("JDAに接続されているギルドがありません。プラグインを無効化します。");
                    getServer().getPluginManager().disablePlugin(this);
                    return;
                }
                // MEventListenerの初期化
                this.MListener = new MEventListener(this, new async(this, jda));
            } else {
                Bukkit.getLogger().severe("JDAの初期化に失敗しました。");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        } catch (Exception e) {
            Bukkit.getLogger().severe("Discordボットの起動中にエラーが発生しました。");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // コマンドの登録
        link link = new link(this, jda);
        getCommand("link").setExecutor(link);

        // LuckPermsイベントの登録
        DMReceive DMReceive = new DMReceive(this);
        LuckPerms luckPerms = getServer().getServicesManager().load(LuckPerms.class);
        if (luckPerms != null) {
            EventBus eventBus = luckPerms.getEventBus();
            eventBus.subscribe(this, NodeAddEvent.class, MListener::onGroupAdd);
            eventBus.subscribe(this, NodeRemoveEvent.class, MListener::onGroupRemove);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("プラグインが無効化されました！");
    }

    public JDA getJDA() {
        return jda;
    }

    public String getGuildId() {
        return guildId;
    }

    public List<String> getMinecraftGroups() {
        return getConfig().getStringList("Minecraft.groups");
    }

    public List<String> getDiscordRoles() {
        return getConfig().getStringList("Discord.roles");
    }
}
