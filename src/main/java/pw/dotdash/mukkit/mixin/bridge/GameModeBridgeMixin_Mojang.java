package pw.dotdash.mukkit.mixin.bridge;

import net.minecraft.world.GameType;
import org.bukkit.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import pw.dotdash.mukkit.bridge.GameModeBridge;

@Mixin(GameType.class)
public abstract class GameModeBridgeMixin_Mojang implements GameModeBridge {

    @SuppressWarnings("ConstantConditions")
    @Override
    public GameMode bridge$toBukkit() {
        switch ((GameType) (Object) this) {
            case NOT_SET:
            case SURVIVAL:
                return GameMode.SURVIVAL;
            case CREATIVE:
                return GameMode.CREATIVE;
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SPECTATOR:
                return GameMode.SPECTATOR;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + this);
        }
    }

    @Override
    public GameType bridge$toMojang() {
        return (GameType) (Object) this;
    }
}