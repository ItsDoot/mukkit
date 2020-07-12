package pw.dotdash.mukkit.mixin.accessor.world;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(GameRules.class)
public interface GameRulesAccessor {

    @Accessor("rules") Map<GameRules.RuleKey<?>, GameRules.RuleValue<?>> accessor$getRules();
}