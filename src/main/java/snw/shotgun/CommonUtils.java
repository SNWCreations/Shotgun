package snw.shotgun;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class CommonUtils {

    private CommonUtils() {}

    public static boolean isRightClick(PlayerInteractEvent event) {
        final Action action = event.getAction();
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }
}
