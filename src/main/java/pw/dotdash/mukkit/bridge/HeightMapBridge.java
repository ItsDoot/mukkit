package pw.dotdash.mukkit.bridge;

import net.minecraft.world.gen.Heightmap;
import org.bukkit.HeightMap;

public interface HeightMapBridge {

    HeightMap bridge$toBukkit();

    Heightmap.Type bridge$toMojang();
}