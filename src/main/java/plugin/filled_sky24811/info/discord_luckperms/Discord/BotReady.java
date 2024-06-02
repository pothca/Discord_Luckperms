package plugin.filled_sky24811.info.discord_luckperms.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;

import javax.security.auth.login.LoginException;
import java.util.concurrent.CompletableFuture;
import java.util.List;

public class BotReady extends ListenerAdapter {
    private final Discord_Luckperms plugin;
    private JDA jda;

    public BotReady(Discord_Luckperms plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<List<Member>> loadMembersAsync(Guild guild) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return guild.loadMembers().get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public JDA startBot(String token) throws InterruptedException, LoginException {
        if (token == null || token.isEmpty()) {
            Bukkit.getLogger().severe("Discordトークンが無効です。ボットを起動できません。");
            return null;
        }

        JDABuilder jdaBuilder = JDABuilder.createDefault(token);
        jdaBuilder.addEventListeners(this);
        jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);
        jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);

        jda = jdaBuilder.build();
        jda.awaitReady();

        Guild guild = jda.getGuilds().get(0);

        loadMembersAsync(guild).thenAccept(members -> {
            Bukkit.getLogger().info("メンバー情報の取得が完了しました。");
        });

        Bukkit.getLogger().info("メンバー情報取得中...");
        return jda;
    }

    @Override
    public void onReady(ReadyEvent event) {
        Bukkit.getLogger().info("Discord bot is ready!");
    }
}
