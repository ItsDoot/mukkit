package pw.dotdash.mukkit.mixin.bridge.boss;

import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarColor;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.boss.BarColorBridge;

@Mixin(BossInfo.Color.class)
public abstract class BarColorBridgeMixin_Mojang implements BarColorBridge {

    @SuppressWarnings("ConstantConditions")
    @Override
    public BarColor bridge$toBukkit() {
        switch ((BossInfo.Color) (Object) this) {
            case PINK:
                return BarColor.PINK;
            case BLUE:
                return BarColor.BLUE;
            case RED:
                return BarColor.RED;
            case GREEN:
                return BarColor.GREEN;
            case YELLOW:
                return BarColor.YELLOW;
            case PURPLE:
                return BarColor.PURPLE;
            case WHITE:
                return BarColor.WHITE;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }

    @Override
    public BossInfo.Color bridge$toMojang() {
        return (BossInfo.Color) (Object) this;
    }
}