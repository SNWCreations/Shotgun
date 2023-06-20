package snw.shotgun.impl.web;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class PlayerSlow extends BukkitRunnable implements Listener {
    private static final int VALID_TIME = 15; // in seconds
    private final Plugin plugin;
    private final Player player;
    private final BlockData data;
    private int ticks;

    public PlayerSlow(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.ticks = VALID_TIME * 20;
        this.data = plugin.getServer().createBlockData(Material.COBWEB);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer() == player) {
            e.setCancelled(true);
        }
    }

    @Override
    public void run() {
        if (ticks-- > 0) {
            Objects.requireNonNull(player.getLocation().getWorld())
                    .spawnParticle(Particle.BLOCK_DUST, player.getLocation(), 10, data);
        } else {
            cleanup();
        }
    }

    private void cleanup() {
        HandlerList.unregisterAll(this);
        cancel();
    }

    public void start() {
        runTaskTimer(plugin, 0L, 1L);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
