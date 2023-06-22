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

import java.util.Arrays;
import java.util.Objects;

public final class WebBullet extends AbstractBullet {
    private final Shotgun plugin;
    public static final ItemStack ITEM;
    public static final int MAX_DISTANCE = 20;
    public static final int RADIUS = 2;

    static {
        final ItemStack itemStack = new ItemStack(Material.IRON_SHOVEL);

        final ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "网枪");
        meta.setLore(Arrays.asList(
                ChatColor.RED + "一次性物品",
                "按右键发射蜘蛛网，可以让距离子弹 " + RADIUS + " 格及以内的猎人暂时失去行动能力。",
                "有射程限制，最远 " + MAX_DISTANCE + " 格方块。"
        ));
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
