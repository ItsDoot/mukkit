package pw.dotdash.mukkit.mixin.accessor.net.minecraft.util;

import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoodStats.class)
public interface FoodStatsAccessor {

    @Accessor("foodExhaustionLevel")
    float getFoodExhaustionLevel();

    @Accessor("foodExhaustionLevel")
    void setFoodExhaustionLevel(float foodExhaustionLevel);

    @Accessor("foodSaturationLevel")
    void setFoodSaturationLevel(float foodSaturationLevel);
}