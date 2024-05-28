package plugin.filled_sky24811.info.discord_luckperms.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;

import javax.security.auth.login.LoginException;

public class BotReady extends ListenerAdapter {
    private final Discord_Luckperms plugin;

    public BotReady(Discord_Luckperms plugin) {
        this.plugin = plugin;
    }

    public JDA startBot(String token) throws InterruptedException, LoginException {
        if (token == null || token.isEmpty()) {
            Bukkit.getLogger().severe("Discordトークンが無効です。ボットを起動できません。");
            return null;
        }

        JDABuilder jda = JDABuilder.createDefault(token);
        jda.addEventListeners(this, new DEventListener(plugin), new DMReceive(plugin));
        jda.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);
        jda.setMemberCachePolicy(MemberCachePolicy.ALL);
        jda.build();
        return jda.build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        plugin.getLogger().info("Discord bot is ready!");
    }
}
