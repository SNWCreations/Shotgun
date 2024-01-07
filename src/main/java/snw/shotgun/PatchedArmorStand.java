package snw.shotgun;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;

import java.util.Objects;

// Code from https://www.spigotmc.org/threads/set-velocity-of-armor-stand-without-gravity.486827/
// The reply #12
// This modified ArmorStand allow the velocity without gravity!
public final class PatchedArmorStand extends ArmorStand {
    public PatchedArmorStand(Location location) {
        super(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(),
                location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (!isNoGravity()) {
            super.travel(vec3d);
        } else {
            move(MoverType.SELF, super.getDeltaMovement());
        }
    }
}
