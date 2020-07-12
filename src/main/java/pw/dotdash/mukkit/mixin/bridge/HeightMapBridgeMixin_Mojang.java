package pw.dotdash.mukkit.mixin.bridge;

import net.minecraft.world.gen.Heightmap;
import org.bukkit.HeightMap;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.HeightMapBridge;

@Mixin(Heightmap.Type.class)
public abstract class HeightMapBridgeMixin_Mojang implements HeightMapBridge {

    @SuppressWarnings("ConstantConditions")
    @Override
    public HeightMap bridge$toBukkit() {
        switch ((Heightmap.Type) (Object) this) {
            case MOTION_BLOCKING:
                return HeightMap.MOTION_BLOCKING;
            case MOTION_BLOCKING_NO_LEAVES:
                return HeightMap.MOTION_BLOCKING_NO_LEAVES;
            case OCEAN_FLOOR:
                return HeightMap.OCEAN_FLOOR;
            case OCEAN_FLOOR_WG:
                return HeightMap.OCEAN_FLOOR_WG;
            case WORLD_SURFACE:
                return HeightMap.WORLD_SURFACE;
            case WORLD_SURFACE_WG:
                return HeightMap.WORLD_SURFACE_WG;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }

    @Override
    public Heightmap.Type bridge$toMojang() {
        return (Heightmap.Type) (Object) this;
    }
}