package pw.dotdash.mukkit.mixin.api.advancements;

import net.minecraft.advancements.Criterion;
import net.minecraft.util.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pw.dotdash.mukkit.util.TypeConversions;

import java.util.Collection;
import java.util.Map;

@Mixin(net.minecraft.advancements.Advancement.class)
public abstract class AdvancementMixin_API implements Advancement {

    // --- Shadowed Methods ---

    @Shadow
    public abstract ResourceLocation shadow$getId();

    @Shadow
    public abstract Map<String, Criterion> shadow$getCriteria();

    // --- Advancement Implementation ---

    @Override
    public @NotNull Collection<String> getCriteria() {
        return this.shadow$getCriteria().keySet();
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return TypeConversions.fromMojang(this.shadow$getId());
    }
}