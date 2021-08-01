package net.snofox.minecraft.advertiseafk;

import net.lapismc.afkplus.api.AFKStartEvent;
import net.lapismc.afkplus.api.AFKStopEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AdvertiseAfk extends JavaPlugin implements Listener {
    final HashMap<UUID, String> names = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        for(final Map.Entry<UUID, String> entry : names.entrySet()) {
            final Player p = getServer().getPlayer(entry.getKey());
            if(p != null) p.setPlayerListName(entry.getValue());
        }
        names.clear();
    }

    @EventHandler(ignoreCancelled = true)
    void onAfkStart(final AFKStartEvent ev) {
        final UUID playerId = ev.getPlayer().getUUID();
        final Player p = getServer().getPlayer(playerId);
        if(!names.containsKey(playerId)) names.put(playerId, p.getPlayerListName());
        p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', "&7&o[AFK] " + names.get(playerId)));
    }

    @EventHandler(ignoreCancelled = true)
    void onAfkStop(final AFKStopEvent ev) {
        final UUID playerId = ev.getPlayer().getUUID();
        final Player p = getServer().getPlayer(playerId);
        p.setPlayerListName(names.remove(playerId));
    }
}
