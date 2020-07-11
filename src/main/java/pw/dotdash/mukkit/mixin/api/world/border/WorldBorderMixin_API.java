package pw.dotdash.mukkit.mixin.api.world.border;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.spongepowered.asm.mixin.*;
import pw.dotdash.mukkit.util.MathUtil;

@Mixin(net.minecraft.world.border.WorldBorder.class)
@Implements(@Interface(iface = WorldBorder.class, prefix = "api$"))
public abstract class WorldBorderMixin_API implements WorldBorder {

    // --- Shadowed Methods ---

    @Shadow
    public abstract int shadow$getSize();

    @Shadow
    public abstract void shadow$setSize(int size);

    @Shadow
    public abstract void shadow$setTransition(double oldSize, double newSize, long time);

    @Shadow
    public abstract double shadow$getCenterX();

    @Shadow
    public abstract double shadow$getCenterZ();

    @Shadow
    public abstract void shadow$setCenter(double x, double z);

    @Shadow
    public abstract double shadow$getDamageBuffer();

    @Shadow
    public abstract void shadow$setDamageBuffer(double bufferSize);

    @Shadow
    public abstract double shadow$getDamagePerBlock();

    @Shadow
    public abstract void shadow$setDamagePerBlock(double newAmount);

    @Shadow
    public abstract int shadow$getWarningTime();

    @Shadow
    public abstract void shadow$setWarningTime(int warningTime);

    @Shadow
    public abstract int shadow$getWarningDistance();

    @Shadow
    public abstract void shadow$setWarningDistance(int warningDistance);

    @Shadow
    public abstract boolean shadow$contains(BlockPos pos);

    // --- WorldBorder Implementation ---

    @Override
    public void reset() {
        this.setSize(6.0E7D);
        this.setDamageAmount(0.2D);
        this.setDamageBuffer(5.0D);
        this.setWarningDistance(5);
        this.setWarningTime(15);
        this.setCenter(0.0D, 0.0D);
    }

    @Override
    public double getSize() {
        return this.shadow$getSize();
    }

    @Override
    public void setSize(double newSize) {
        this.setSize(newSize, 0L);
    }

    @Override
    public void setSize(double newSize, long seconds) {
        double boundedSize = MathUtil.clamp(newSize, 1.0D, 6.0E7D);
        long boundedSeconds = MathUtil.clamp(seconds, 0L, 9223372036854775L);

        if (boundedSeconds > 0L) {
            this.shadow$setTransition(this.getSize(), boundedSize, boundedSeconds * 1000L);
        } else {
            this.shadow$setSize((int) boundedSize);
        }
    }

    @Override
    public Location getCenter() {
        return new Location(null, this.shadow$getCenterX(), 0.0D, this.shadow$getCenterZ());
    }

    @Intrinsic
    public void api$setCenter(double x, double z) {
        this.shadow$setCenter(
                MathUtil.clamp(x, -3.0E7D, 3.0E7D),
                MathUtil.clamp(z, -3.0E7D, 3.0E7D)
        );
    }

    @Override
    public void setCenter(Location location) {
        Preconditions.checkNotNull(location, "location");

        this.setCenter(location.getX(), location.getZ());
    }

    @Intrinsic
    public double api$getDamageBuffer() {
        return this.shadow$getDamageBuffer();
    }

    @Intrinsic
    public void api$setDamageBuffer(double blocks) {
        this.shadow$setDamageBuffer(blocks);
    }

    @Override
    public double getDamageAmount() {
        return this.shadow$getDamagePerBlock();
    }

    @Override
    public void setDamageAmount(double damage) {
        this.shadow$setDamagePerBlock(damage);
    }

    @Intrinsic
    public int api$getWarningTime() {
        return this.shadow$getWarningTime();
    }

    @Intrinsic
    public void api$setWarningTime(int seconds) {
        this.shadow$setWarningTime(seconds);
    }

    @Intrinsic
    public int api$getWarningDistance() {
        return this.shadow$getWarningDistance();
    }

    @Intrinsic
    public void api$setWarningDistance(int distance) {
        this.shadow$setWarningDistance(distance);
    }

    @Override
    public boolean isInside(Location location) {
        Preconditions.checkNotNull(location, "location");

        return this.shadow$contains(new BlockPos(location.getX(), location.getY(), location.getZ()));
    }
}