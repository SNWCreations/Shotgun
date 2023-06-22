package snw.shotgun;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.WeakHashMap;

public abstract class AbstractBullet extends BukkitRunnable {
    private static final PotionEffect INVISIBLE =
            new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
    private static final Collection<AbstractBullet> INSTANCES = Collections.newSetFromMap(new WeakHashMap<>());
    protected final Player shooter;
    protected final Location origin;
    protected final ArmorStand entity;
    protected final Vector direction;
    protected long maxLivingTime;
    protected final double maxDistanceTravelled;
    protected final double multiplyAmplifier;
    protected double distanceTravelled = 0;

    protected AbstractBullet(
            Player player,
            int maxLivingTime, // in seconds
            double maxDistanceTravelled,
            double multiplyAmplifier
    ) {
        this.shooter = player;
        this.origin = player.getLocation();
        this.direction = player.getEyeLocation().getDirection().normalize();
        this.maxLivingTime = maxLivingTime * 20L;
        this.maxDistanceTravelled = maxDistanceTravelled;
        this.multiplyAmplifier = multiplyAmplifier;
        final PatchedArmorStand theEntity = new PatchedArmorStand(this.origin);
        this.entity = (ArmorStand) theEntity.getBukkitEntity();
        initializeEntity(this.entity);
        ((CraftWorld) Objects.requireNonNull(this.origin.getWorld())).getHandle().addEntity(theEntity);
        INSTANCES.add(this);
    }

    protected void initializeEntity(ArmorStand entity) {
        entity.setVisible(false);
        entity.setMarker(true);
        entity.setGravity(false);
        entity.setSmall(true);
        entity.setInvulnerable(true);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            entity.addEquipmentLock(slot, ArmorStand.LockType.REMOVING_OR_CHANGING);
        }
    }

    @Override
    public final void run() {
        if (!this.entity.isValid()) {
            terminate(RemovalReason.ENTITY_NOT_VALID_ANYMORE);
            return;
        }
        if (maxLivingTime != -1) { // filter out the magic number -1
            if (maxLivingTime-- == 0) {
                terminate(RemovalReason.LIVING_TIME_IS_UP);
                return;
            }
        }

        double distance = this.entity.getLocation().distance(this.origin);
        if (distance > this.maxDistanceTravelled) {
            terminate(RemovalReason.OVER_DISTANCE);
            return;
        }

        //this.distanceTravelled += distance;
        //if (this.distanceTravelled > this.maxDistanceTravelled) {
        //    terminate(RemovalReason.OVER_DISTANCE);
        //    return;
        //}

        //this.entity.teleport(this.entity.getLocation().add(this.direction.multiply(this.multiplyAmplifier)));
        this.entity.setVelocity(this.direction.multiply(this.multiplyAmplifier));
        this.tickEntity(this.entity);
        tryToCallHitEntity(0.5);
    }

    protected boolean tryToCallHitEntity(double radius) {
        boolean hit = false;
        if (this.entity.isValid()) {
            for (Entity nearbyEntity : this.entity.getNearbyEntities(radius, 0.5, radius)) {
                if (!(nearbyEntity instanceof LivingEntity) || nearbyEntity.equals(this.shooter)) {
                    continue;
                }

                hit = hitEntity(nearbyEntity);
            }
        }
        return hit;
    }

    protected abstract void tickEntity(ArmorStand entity);

    protected abstract boolean hitEntity(Entity entity);

    protected void terminate(RemovalReason reason) {
        if (this.entity.isValid()) {
            this.entity.remove();
        }
        if (!isCancelled()) {
            cancel();
        }
    }

    public BukkitTask start(Plugin plugin) {
        return this.runTaskTimer(plugin, 1L, 1L);
    }

    public enum RemovalReason {
        LIVING_TIME_IS_UP,
        OVER_DISTANCE,
        ENTITY_NOT_VALID_ANYMORE,
        HIT_ENTITY,
        OTHER
    }

    public static void killAll() {
        for (AbstractBullet instance : INSTANCES) {
            if (instance == null) {
                continue;
            }
            instance.terminate(RemovalReason.OTHER);
        }
    }
}
