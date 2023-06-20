package snw.shotgun;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import snw.shotgun.impl.web.WebGunInteractListener;
import snw.shotgun.impl.web.WebBullet;

public final class Shotgun extends JavaPlugin {

    @Override
    public void onEnable() {
        // region WebGun init
        getServer().getPluginManager().registerEvents(new WebGunInteractListener(this), this);
        getCommand("webgun").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                ((Player) sender).getInventory().addItem(WebBullet.ITEM);
            }
            return true;
        });
        // endregion WebGun init
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        AbstractBullet.killAll();
    }
}
