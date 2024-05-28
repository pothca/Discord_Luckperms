package plugin.filled_sky24811.info.discord_luckperms.Discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;

import java.util.List;

public class DEventListener extends ListenerAdapter {
    private final Discord_Luckperms plugin;

    public DEventListener(Discord_Luckperms plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        Member member = event.getMember();
        List<Role> roles = event.getRoles();

    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        Member member = event.getMember();
        List<Role> roles = event.getRoles();

    }
}