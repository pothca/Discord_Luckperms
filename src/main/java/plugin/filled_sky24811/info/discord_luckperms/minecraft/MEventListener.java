package plugin.filled_sky24811.info.discord_luckperms.minecraft;

import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import plugin.filled_sky24811.info.discord_luckperms.Discord_Luckperms;
import plugin.filled_sky24811.info.discord_luckperms.async;

import java.util.UUID;

public class MEventListener {
    private final Discord_Luckperms plugin;
    private final async async;
    public MEventListener(Discord_Luckperms plugin, async async){
        this.plugin = plugin;
        this.async = async;
    }

    public void onGroupAdd(NodeAddEvent event){
        User user = (User) event.getTarget();
        UUID uuid = user.getUniqueId();
        String node = event.getNode().getKey();
        async.minecraft_discordAdd(uuid, node);
    }

    public void onGroupRemove(NodeRemoveEvent event){
        User user = (User) event.getTarget();
        UUID uuid = user.getUniqueId();
        String node = event.getNode().getKey();
        async.minecraft_discordRemove(uuid, node);
    }
}
