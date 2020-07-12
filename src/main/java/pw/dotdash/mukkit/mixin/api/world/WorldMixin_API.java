package pw.dotdash.mukkit.mixin.api.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldType;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldInfo;
import org.bukkit.World;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Mixin(net.minecraft.world.World.class)
@Implements(@Interface(iface = World.class, prefix = "api$"))
public abstract class WorldMixin_API {

    // --- Shadowed Methods ---

    @Shadow
    public abstract WorldBorder shadow$getWorldBorder();

    @Shadow
    public abstract GameRules shadow$getGameRules();

    @Shadow
    public abstract WorldType shadow$getWorldType();

    @Shadow
    public abstract int shadow$getSeaLevel();

    @Shadow
    public abstract int shadow$getActualHeight();

    @Shadow
    public abstract long shadow$getSeed();

    @Shadow
    public abstract Dimension shadow$getDimension();

    @Shadow
    public abstract Explosion shadow$createExplosion(@Nullable Entity entityIn, double xIn, double yIn, double zIn, float explosionRadius, boolean causesFire, Explosion.Mode modeIn);

    @Shadow
    public abstract long shadow$getGameTime();

    @Shadow
    public abstract void shadow$setDayTime(long time);

    @Shadow
    public abstract long shadow$getDayTime();

    @Shadow
    public abstract BlockPos shadow$getSpawnPoint();

    @Shadow
    public abstract WorldInfo shadow$getWorldInfo();

    @Shadow
    public abstract List<Entity> shadow$getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate);

    @Shadow
    public abstract net.minecraft.world.chunk.Chunk shadow$getChunk(int chunkX, int chunkZ);

    @Shadow
    public abstract AbstractChunkProvider shadow$getChunkProvider();

    @Shadow
    public abstract void shadow$notifyBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags);

    @Shadow
    public abstract Random shadow$getRandom();

    // --- World Implementation ---

    @Intrinsic
    public long api$getSeed() {
        return this.shadow$getSeed();
    }
}