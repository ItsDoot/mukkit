package pw.dotdash.mukkit.mixin.bridge;

import org.bukkit.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.DifficultyBridge;

@Mixin(Difficulty.class)
public abstract class DifficultyBridgeMixin_Bukkit implements DifficultyBridge {

    @Override
    public Difficulty bridge$toBukkit() {
        return (Difficulty) (Object) this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public net.minecraft.world.Difficulty bridge$toMojang() {
        switch ((Difficulty) (Object) this) {
            case PEACEFUL:
                return net.minecraft.world.Difficulty.PEACEFUL;
            case EASY:
                return net.minecraft.world.Difficulty.EASY;
            case NORMAL:
                return net.minecraft.world.Difficulty.NORMAL;
            case HARD:
                return net.minecraft.world.Difficulty.HARD;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }
}