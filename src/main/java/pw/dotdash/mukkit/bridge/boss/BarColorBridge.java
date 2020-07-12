package pw.dotdash.mukkit.bridge.boss;

import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarColor;

public interface BarColorBridge {

    BarColor bridge$toBukkit();

    BossInfo.Color bridge$toMojang();
}