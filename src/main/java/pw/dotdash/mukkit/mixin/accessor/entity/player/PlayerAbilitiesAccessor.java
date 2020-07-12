package pw.dotdash.mukkit.mixin.accessor.entity.player;

import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerAbilities.class)
public interface PlayerAbilitiesAccessor {

    @Accessor("flySpeed")
    void setFlySpeed(float flySpeed);

    @Accessor("walkSpeed")
    void setWalkSpeed(float walkSpeed);
}