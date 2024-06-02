package plugin.filled_sky24811.info.discord_luckperms.minecraft;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;
import plugin.filled_sky24811.info.discord_luckperms.async;

import java.util.UUID;

public class MEventListener extends ListenerAdapter {
    private final Discord_Luckperms plugin;
    private final async async;

    public MEventListener(Discord_Luckperms plugin, async async) {
        this.plugin = plugin;
        this.async = async;
    }

    public void onGroupAdd(NodeAddEvent event) {
        if (event.getTarget() instanceof User) {
            User user = (User) event.getTarget();
            UUID uuid = user.getUniqueId();
            String node = event.getNode().getKey();
            async.minecraft_discordAdd(uuid, node);
        }
    }

    public void onGroupRemove(NodeRemoveEvent event) {
        if (event.getTarget() instanceof User) {
            User user = (User) event.getTarget();
            UUID uuid = user.getUniqueId();
            String node = event.getNode().getKey();
            async.minecraft_discordRemove(uuid, node);
        }
    }
}