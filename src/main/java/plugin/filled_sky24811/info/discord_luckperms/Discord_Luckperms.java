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
import plugin.filled_sky24811.info.discord_luckperms.Discord.DEventListener;
import plugin.filled_sky24811.info.discord_luckperms.Discord.DMReceive;
import plugin.filled_sky24811.info.discord_luckperms.commands.discord_luckperms;
import plugin.filled_sky24811.info.discord_luckperms.commands.dl_link;
import plugin.filled_sky24811.info.discord_luckperms.minecraft.MEventListener;

import java.util.List;

public final class Discord_Luckperms extends JavaPlugin {
    private MEventListener MListener;
    private DEventListener DListener;
    private BotReady botReady;
    private JDA jda;
    private String guildId;
    private LuckPerms luckPerms;
    private async async;
    private DMReceive DMReceive;

    @Override
    public void onEnable() {
        try {
            Class.forName("net.dv8tion.jda.api.JDA");
            Bukkit.getLogger().info("JDAクラスが正常にロードされました");
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe("JDAクラスのロードに失敗しました");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // 既存のonEnable処理
        Bukkit.getLogger().info("プラグインが有効化されました！");
        saveDefaultConfig();
        this.reloadConfigFile();

        // Discord Botの起動
        String token = getConfig().getString("Discord.token");
        if (token == null || token.isEmpty()) {
            Bukkit.getLogger().severe("Discordトークンが設定ファイルに含まれていません。プラグインを無効化します。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        try {
            // LuckPermsの初期化
            luckPerms = getServer().getServicesManager().load(LuckPerms.class);
            if (luckPerms == null) {
                Bukkit.getLogger().severe("LuckPermsがロードされませんでした。プラグインを無効化します。");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            this.async = new async(this, null, luckPerms);
            botReady = new BotReady(this);
            this.jda = botReady.startBot(token);
            if (jda != null) {
                jda.awaitReady();
                this.async.setJDA(jda);

                // 最初のギルドIDを取得
                List<Guild> guilds = jda.getGuilds();
                if (!guilds.isEmpty()) {
                    this.guildId = guilds.get(0).getId();
                } else {
                    Bukkit.getLogger().severe("JDAに接続されているギルドがありません。プラグインを無効化します。");
                    getServer().getPluginManager().disablePlugin(this);
                    return;
                }

                // MEventListenerとDEventListenerの初期化
                this.MListener = new MEventListener(this, async);
                this.DListener = new DEventListener(async);
                this.DMReceive = new DMReceive(this);

                // JDAイベントリスナーの追加
                jda.addEventListener(DListener,DMReceive);

                // LuckPermsのイベントリスナー登録
                EventBus eventBus = luckPerms.getEventBus();
                eventBus.subscribe(this, NodeAddEvent.class, MListener::onGroupAdd);
                eventBus.subscribe(this, NodeRemoveEvent.class, MListener::onGroupRemove);
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
        dl_link dl_link = new dl_link(this, jda);
        discord_luckperms discord_luckperms = new discord_luckperms(this);
        getCommand("dl_link").setExecutor(dl_link);
        getCommand("discord_luckperms").setExecutor(discord_luckperms);

        DMReceive DMReceive = new DMReceive(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("プラグインが無効化されました！");
    }

    public void reloadConfigFile(){
        getDataFolder();
        reloadConfig();
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
