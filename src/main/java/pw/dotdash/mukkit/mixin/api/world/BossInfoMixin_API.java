package pw.dotdash.mukkit.mixin.api.world;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BossInfo.class)
public abstract class BossInfoMixin_API {

    // --- Shadowed Methods ---

    @Shadow
    public abstract float shadow$getPercent();

    @Shadow
    public abstract BossInfo.Overlay shadow$getOverlay();

    @Shadow
    public abstract BossInfo.Color shadow$getColor();

    @Shadow
    public abstract ITextComponent shadow$getName();
}