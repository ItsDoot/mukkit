package pw.dotdash.mukkit.mixin.bridge;

import net.minecraft.world.gen.Heightmap;
import org.bukkit.HeightMap;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.HeightMapBridge;

@Mixin(HeightMap.class)
public abstract class HeightMapBridgeMixin_Bukkit implements HeightMapBridge {

    @Override
    public HeightMap bridge$toBukkit() {
        return (HeightMap) (Object) this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Heightmap.Type bridge$toMojang() {
        switch ((HeightMap) (Object) this) {
            case MOTION_BLOCKING:
                return Heightmap.Type.MOTION_BLOCKING;
            case MOTION_BLOCKING_NO_LEAVES:
                return Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;
            case OCEAN_FLOOR:
                return Heightmap.Type.OCEAN_FLOOR;
            case OCEAN_FLOOR_WG:
                return Heightmap.Type.OCEAN_FLOOR_WG;
            case WORLD_SURFACE:
                return Heightmap.Type.WORLD_SURFACE;
            case WORLD_SURFACE_WG:
                return Heightmap.Type.WORLD_SURFACE_WG;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }
}