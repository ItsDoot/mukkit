package pw.dotdash.mukkit.mixin.bridge;

import org.bukkit.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.DifficultyBridge;

@Mixin(net.minecraft.world.Difficulty.class)
public abstract class DifficultyBridgeMixin_Mojang implements DifficultyBridge {

    @SuppressWarnings("ConstantConditions")
    @Override
    public Difficulty bridge$toBukkit() {
        switch ((net.minecraft.world.Difficulty) (Object) this) {
            case PEACEFUL:
                return Difficulty.PEACEFUL;
            case EASY:
                return Difficulty.EASY;
            case NORMAL:
                return Difficulty.NORMAL;
            case HARD:
                return Difficulty.HARD;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }

    @Override
    public net.minecraft.world.Difficulty bridge$toMojang() {
        return (net.minecraft.world.Difficulty) (Object) this;
    }
}