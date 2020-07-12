package pw.dotdash.mukkit.bridge;

import org.bukkit.Difficulty;

public interface DifficultyBridge {

    Difficulty bridge$toBukkit();

    net.minecraft.world.Difficulty bridge$toMojang();
}