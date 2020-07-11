package pw.dotdash.mukkit.mixin.api.entity.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.ITextComponent;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.*;
import pw.dotdash.mukkit.mixin.api.entity.LivingEntityMixin_API;

@Mixin(PlayerEntity.class)
@Implements(@Interface(iface = Player.class, prefix = "api$"))
public abstract class PlayerEntityMixin_API extends LivingEntityMixin_API {

    // --- Shadowed Fields ---

    @Shadow public float experience;
    @Shadow public int experienceLevel;
    @Shadow public int experienceTotal;
    @Shadow @Final public PlayerAbilities abilities;

    // --- Shadowed Methods ---

    @Shadow
    public abstract ITextComponent shadow$getName();

    @Shadow
    public abstract FoodStats shadow$getFoodStats();

    @Shadow
    public abstract GameProfile shadow$getGameProfile();

    @Shadow
    public abstract CooldownTracker shadow$getCooldownTracker();

    @Shadow
    public abstract int shadow$getSleepTimer();

    @Shadow
    public abstract float shadow$getCooldownPeriod();

    @Shadow
    public abstract CompoundNBT shadow$getLeftShoulderEntity();

    @Shadow
    protected abstract void shadow$setLeftShoulderEntity(CompoundNBT tag);

    @Shadow
    public abstract CompoundNBT shadow$getRightShoulderEntity();

    @Shadow
    protected abstract void shadow$setRightShoulderEntity(CompoundNBT tag);

    // --- Player Implementation ---

    @Intrinsic
    public String api$getName() {
        return this.shadow$getName().getString();
    }
}