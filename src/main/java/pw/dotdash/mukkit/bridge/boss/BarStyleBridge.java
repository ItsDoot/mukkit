package pw.dotdash.mukkit.bridge.boss;

import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarStyle;

public interface BarStyleBridge {

    BarStyle bridge$toBukkit();

    BossInfo.Overlay bridge$toMojang();
}