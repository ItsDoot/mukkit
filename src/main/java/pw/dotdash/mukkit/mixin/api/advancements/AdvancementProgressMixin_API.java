package pw.dotdash.mukkit.mixin.api.advancements;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.minecraft.advancements.CriterionProgress;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Mixin(net.minecraft.advancements.AdvancementProgress.class)
@Implements(@Interface(iface = AdvancementProgress.class, prefix = "api$"))
public abstract class AdvancementProgressMixin_API implements AdvancementProgress {

    // --- Shadowed Methods ---

    @Shadow
    public abstract boolean shadow$isDone();

    @Shadow
    public abstract Iterable<String> shadow$getRemaningCriteria();

    @Shadow
    public abstract Iterable<String> shadow$getCompletedCriteria();

    @Shadow
    public abstract CriterionProgress shadow$getCriterionProgress(String criterionIn);

    // --- AdvancementProgress Implementation ---

    /**
     * TODO
     */
    @Override
    public @NotNull Advancement getAdvancement() {
        return null;
    }

    @Intrinsic
    public boolean api$isDone() {
        return this.shadow$isDone();
    }

    /**
     * TODO
     */
    @Override
    public boolean awardCriteria(@NotNull String criteria) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public boolean revokeCriteria(@NotNull String criteria) {
        return false;
    }

    @Override
    public @Nullable Date getDateAwarded(@NotNull String criteria) {
        Preconditions.checkNotNull(criteria, "criteria");

        CriterionProgress progress = this.shadow$getCriterionProgress(criteria);

        if (progress == null) {
            return null;
        }

        return progress.getObtained();
    }

    @Override
    public @NotNull Collection<String> getRemainingCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(this.shadow$getRemaningCriteria()));
    }

    @Override
    public @NotNull Collection<String> getAwardedCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(this.shadow$getCompletedCriteria()));
    }
}