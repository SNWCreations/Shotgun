package snw.shotgun.impl.web;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;
import snw.shotgun.AbstractBullet;
import snw.shotgun.Shotgun;

import java.util.Objects;

public final class WebBullet extends AbstractBullet {
    private final Shotgun plugin;
    public static final ItemStack ITEM;

    static {
        final ItemStack itemStack = new ItemStack(Material.IRON_SHOVEL);

        final ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.setDisplayName(ChatColor.RED + "网枪");
        itemStack.setItemMeta(meta);

        ITEM = itemStack;
    }

    public WebBullet(Player player, Shotgun plugin) {
        super(player, -1, 20, 1.1);
        this.plugin = plugin;
    }

    @Override
    protected void initializeEntity(ArmorStand entity) {
        super.initializeEntity(entity);
        Objects.requireNonNull(entity.getEquipment()).setHelmet(new ItemStack(Material.COBWEB));
    }

    @Override
    protected void tickEntity(ArmorStand entity) {
    }

    @Override
    protected void hitEntity(Entity entity) {
        if (entity instanceof Player) {
            final Team team =
                    Objects.requireNonNull(plugin.getServer().getScoreboardManager())
                            .getMainScoreboard()
                            .getEntryTeam(entity.getName());
            if (team != null) {
                if (team.getName().equals("rfm_hunter")) {
                    new PlayerSlow(plugin, ((Player) entity)).start();
                }
            }
        }
    }

    @Override
    protected void tryToCallHitEntity(double radius) {
        super.tryToCallHitEntity(2);
    }
}
