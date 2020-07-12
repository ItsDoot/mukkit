package pw.dotdash.mukkit.mixin.bridge.boss;

import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarColor;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.boss.BarColorBridge;

@Mixin(BarColor.class)
public abstract class BarColorBridgeMixin_Bukkit implements BarColorBridge {

    @Override
    public BarColor bridge$toBukkit() {
        return (BarColor) (Object) this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public BossInfo.Color bridge$toMojang() {
        switch ((BarColor) (Object) this) {
            case PINK:
                return BossInfo.Color.PINK;
            case BLUE:
                return BossInfo.Color.BLUE;
            case RED:
                return BossInfo.Color.RED;
            case GREEN:
                return BossInfo.Color.GREEN;
            case YELLOW:
                return BossInfo.Color.YELLOW;
            case PURPLE:
                return BossInfo.Color.PURPLE;
            case WHITE:
                return BossInfo.Color.WHITE;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }
}