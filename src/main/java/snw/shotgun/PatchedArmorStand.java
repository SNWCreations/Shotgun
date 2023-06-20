package snw.shotgun;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EnumMoveType;
import net.minecraft.server.v1_16_R3.Vec3D;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import java.util.Objects;

// Code from https://www.spigotmc.org/threads/set-velocity-of-armor-stand-without-gravity.486827/
// The reply #12
// This modified ArmorStand allow the velocity without gravity!
public final class PatchedArmorStand extends EntityArmorStand {
    public PatchedArmorStand(Location location) {
        super(((CraftWorld) Objects.requireNonNull(location.getWorld())).getHandle(),
                location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void g(Vec3D vec3d) {
        if (!isNoGravity()) {
            super.g(vec3d);
        } else {
            move(EnumMoveType.SELF, super.getMot());
        }
    }
}
