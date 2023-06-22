package snw.shotgun.impl.web;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import snw.shotgun.Shotgun;

import static snw.shotgun.CommonUtils.isRightClick;

public final class WebGunInteractListener implements Listener {
    private final Shotgun plugin;

    public WebGunInteractListener(Shotgun plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.hasItem()) {
            if (isRightClick(event)) {
                final ItemStack item = event.getItem();
                if (WebBullet.ITEM.isSimilar(item)) {
                    new WebBullet(event.getPlayer(), plugin).start(this.plugin);
                    item.setAmount(0);
                    event.getPlayer().playSound(
                            event.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.MASTER,
                            1, 1);
                }
            }
        }
    }
}
