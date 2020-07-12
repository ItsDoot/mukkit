package pw.dotdash.mukkit.mixin.bridge;

import net.minecraft.world.GameType;
import org.bukkit.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.GameModeBridge;

@Mixin(GameMode.class)
public abstract class GameModeBridgeMixin_Bukkit implements GameModeBridge {

    @Override
    public GameMode bridge$toBukkit() {
        return (GameMode) (Object) this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public GameType bridge$toMojang() {
        switch ((GameMode) (Object) this) {
            case CREATIVE:
                return GameType.CREATIVE;
            case SURVIVAL:
                return GameType.SURVIVAL;
            case ADVENTURE:
                return GameType.ADVENTURE;
            case SPECTATOR:
                return GameType.SPECTATOR;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }
}