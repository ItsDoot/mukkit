package pw.dotdash.mukkit.bridge;

import net.minecraft.world.GameType;
import org.bukkit.GameMode;

public interface GameModeBridge {

    GameMode bridge$toBukkit();

    GameType bridge$toMojang();
}