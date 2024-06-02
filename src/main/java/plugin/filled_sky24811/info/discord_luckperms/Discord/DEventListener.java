package plugin.filled_sky24811.info.discord_luckperms.Discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import plugin.filled_sky24811.info.discord_luckperms.async;

import java.io.IOException;
import java.util.List;

public class DEventListener extends ListenerAdapter {
    private final async async;

    public DEventListener(async asyncInstance) {
        this.async = asyncInstance;
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        Member member = event.getMember();
        String user = member.getId();
        List<Role> roles = event.getRoles();
        for (Role role : roles) {
            String roleName = role.getId();
            try {
                async.discord_minecraftAdd(user, roleName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        Member member = event.getMember();
        String user = member.getId();
        List<Role> roles = event.getRoles();
        for (Role role : roles) {
            String roleName = role.getId();
            try {
                async.discord_minecraftRemove(user, roleName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
