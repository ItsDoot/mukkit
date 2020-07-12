package pw.dotdash.mukkit.mixin.bridge.boss;

import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarStyle;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.boss.BarStyleBridge;

@Mixin(BossInfo.Overlay.class)
public abstract class BarStyleBridgeMixin_Mojang implements BarStyleBridge {

    @SuppressWarnings("ConstantConditions")
    @Override
    public BarStyle bridge$toBukkit() {
        switch ((BossInfo.Overlay) (Object) this) {
            case PROGRESS:
                return BarStyle.SOLID;
            case NOTCHED_6:
                return BarStyle.SEGMENTED_6;
            case NOTCHED_10:
                return BarStyle.SEGMENTED_10;
            case NOTCHED_12:
                return BarStyle.SEGMENTED_12;
            case NOTCHED_20:
                return BarStyle.SEGMENTED_20;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }

    @Override
    public BossInfo.Overlay bridge$toMojang() {
        return (BossInfo.Overlay) (Object) this;
    }
}