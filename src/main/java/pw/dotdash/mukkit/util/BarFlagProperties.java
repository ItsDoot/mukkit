package pw.dotdash.mukkit.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarFlag;

public final class BarFlagProperties {

    private static final ImmutableMap<BarFlag, MutableProperty<BossInfo, Boolean>> FLAGS =
            ImmutableMap.<BarFlag, MutableProperty<BossInfo, Boolean>>builder()
                    .put(BarFlag.CREATE_FOG, new MutableProperty<>(BossInfo::shouldCreateFog, BossInfo::setCreateFog))
                    .put(BarFlag.DARKEN_SKY, new MutableProperty<>(BossInfo::shouldDarkenSky, BossInfo::setDarkenSky))
                    .put(BarFlag.PLAY_BOSS_MUSIC, new MutableProperty<>(BossInfo::shouldPlayEndBossMusic, BossInfo::setPlayEndBossMusic))
                    .build();

    public static MutableProperty<BossInfo, Boolean> get(BarFlag flag) {
        return Preconditions.checkNotNull(FLAGS.get(flag), "Unhandled enum value: " + flag);
    }

    private BarFlagProperties() {
    }
}