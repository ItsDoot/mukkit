package pw.dotdash.mukkit.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.BossInfo;
import org.bukkit.boss.BarFlag;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class BarFlagMapper {

    private static final ImmutableMap<BarFlag, BarFlagMapper> FLAGS = ImmutableMap.<BarFlag, BarFlagMapper>builder()
            .put(BarFlag.CREATE_FOG, new BarFlagMapper(BossInfo::shouldCreateFog, BossInfo::setCreateFog))
            .put(BarFlag.DARKEN_SKY, new BarFlagMapper(BossInfo::shouldDarkenSky, BossInfo::setDarkenSky))
            .put(BarFlag.PLAY_BOSS_MUSIC, new BarFlagMapper(BossInfo::shouldPlayEndBossMusic, BossInfo::setPlayEndBossMusic))
            .build();

    public static BarFlagMapper get(BarFlag flag) {
        return Preconditions.checkNotNull(FLAGS.get(flag), "Unhandled enum value: " + flag);
    }

    private final Function<BossInfo, Boolean> getter;
    private final BiConsumer<BossInfo, Boolean> setter;

    private BarFlagMapper(Function<BossInfo, Boolean> getter, BiConsumer<BossInfo, Boolean> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public boolean get(BossInfo info) {
        return this.getter.apply(info);
    }

    public void set(BossInfo info, boolean value) {
        this.setter.accept(info, value);
    }
}