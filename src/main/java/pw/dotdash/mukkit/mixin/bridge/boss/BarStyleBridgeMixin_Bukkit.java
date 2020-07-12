package pw.dotdash.mukkit.mixin.bridge.boss;

import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarStyle;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.boss.BarStyleBridge;

@Mixin(BarStyle.class)
public abstract class BarStyleBridgeMixin_Bukkit implements BarStyleBridge {

    @Override
    public BarStyle bridge$toBukkit() {
        return (BarStyle) (Object) this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public BossInfo.Overlay bridge$toMojang() {
        switch ((BarStyle) (Object) this) {
            case SOLID:
                return BossInfo.Overlay.PROGRESS;
            case SEGMENTED_6:
                return BossInfo.Overlay.NOTCHED_6;
            case SEGMENTED_10:
                return BossInfo.Overlay.NOTCHED_10;
            case SEGMENTED_12:
                return BossInfo.Overlay.NOTCHED_12;
            case SEGMENTED_20:
                return BossInfo.Overlay.NOTCHED_20;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }
}