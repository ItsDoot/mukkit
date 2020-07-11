package pw.dotdash.mukkit.mixin.api.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import pw.dotdash.mukkit.util.TypeConversions;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(net.minecraft.entity.LivingEntity.class)
@Implements(@Interface(iface = LivingEntity.class, prefix = "api$"))
public abstract class LivingEntityMixin_API extends EntityMixin_API implements LivingEntity {

    // --- Shadowed Fields ---

    @Shadow @Final public int maxHurtResistantTime;
    @Shadow protected float lastDamage;

    // --- Shadowed Methods ---

    @Shadow
    protected abstract float shadow$getEyeHeight(Pose poseIn, EntitySize sizeIn);

    @Shadow
    public abstract EntitySize shadow$getSize(Pose poseIn);

    @Shadow
    public abstract boolean shadow$addPotionEffect(EffectInstance effectInstanceIn);

    @Shadow
    public abstract boolean shadow$isPotionActive(Effect potionIn);

    @Shadow
    public abstract EffectInstance shadow$getActivePotionEffect(Effect potionIn);

    @Shadow
    public abstract boolean shadow$removePotionEffect(Effect effectIn);

    @Shadow
    public abstract Collection<EffectInstance> shadow$getActivePotionEffects();

    @Shadow
    public abstract boolean shadow$canEntityBeSeen(net.minecraft.entity.Entity entityIn);

    @Shadow
    public abstract boolean shadow$isElytraFlying();

    @Shadow
    public abstract boolean shadow$isActualySwimming();

    @Shadow
    public abstract boolean shadow$attackEntityAsMob(net.minecraft.entity.Entity entityIn);

    @Shadow
    public abstract void shadow$swing(Hand handIn, boolean p_226292_2_);

    @Shadow
    public abstract boolean shadow$canBeCollidedWith();

    @Shadow
    protected abstract void shadow$damageEntity(DamageSource damageSrc, float damageAmount);

    @Shadow
    public abstract float shadow$getHealth();

    @Shadow
    public abstract void shadow$setHealth(float health);

    @Shadow
    public abstract float shadow$getAbsorptionAmount();

    @Shadow
    public abstract void shadow$setAbsorptionAmount(float amount);

    @Shadow
    public abstract float shadow$getMaxHealth();

    @Shadow
    public abstract IAttributeInstance shadow$getAttribute(IAttribute attribute);

    @Shadow
    public abstract boolean shadow$isActiveItemStackBlocking();

    @Shadow
    public abstract boolean shadow$isHandActive();

    @Shadow
    public abstract boolean shadow$isSleeping();

    // --- LivingEntity Implementation ---

    @Override
    public double getEyeHeight() {
        return this.shadow$getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) {
            return this.getEyeHeight();
        } else {
            Pose pose = this.shadow$getPose();
            return this.shadow$getEyeHeight(pose, this.shadow$getSize(pose));
        }
    }

    @Override
    public Location getEyeLocation() {
        Location location = this.getLocation();
        location.setY(location.getY() + this.getEyeHeight());
        return location;
    }

    /**
     * TODO
     */
    @Override
    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Block getTargetBlockExact(int maxDistance) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    @Override
    public int getRemainingAir() {
        return this.shadow$getAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        this.shadow$setAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return this.shadow$getMaxAir();
    }

    /**
     * TODO - currently hardcoded
     *
     * @see net.minecraft.entity.Entity#getMaxAir()
     */
    @Override
    public void setMaximumAir(int ticks) {

    }

    @Override
    public int getMaximumNoDamageTicks() {
        return this.maxHurtResistantTime;
    }

    /**
     * TODO - currently hardcoded
     *
     * @see net.minecraft.entity.LivingEntity#maxHurtResistantTime
     */
    @Override
    public void setMaximumNoDamageTicks(int ticks) {

    }

    @Override
    public double getLastDamage() {
        return this.lastDamage;
    }

    @Override
    public void setLastDamage(double damage) {
        this.lastDamage = (float) damage;
    }

    @Override
    public int getNoDamageTicks() {
        return this.hurtResistantTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        this.hurtResistantTime = ticks;
    }

    /**
     * TODO
     */
    @Override
    public Player getKiller() {
        return null;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        return this.addPotionEffect(effect, false);
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        return this.shadow$addPotionEffect(TypeConversions.fromBukkit(effect));
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= this.addPotionEffect(effect);
        }
        return success;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        return this.shadow$isPotionActive(TypeConversions.fromBukkit(type));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        return TypeConversions.fromMojang(this.shadow$getActivePotionEffect(TypeConversions.fromBukkit(type)));
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        this.shadow$removePotionEffect(TypeConversions.fromBukkit(type));
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return this.shadow$getActivePotionEffects().stream()
                .map(TypeConversions::fromMojang)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        return this.shadow$canEntityBeSeen((net.minecraft.entity.Entity) other);
    }

    /**
     * TODO
     */
    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void setRemoveWhenFarAway(boolean remove) {

    }

    /**
     * TODO
     */
    @Override
    public EntityEquipment getEquipment() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public boolean getCanPickupItems() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void setCanPickupItems(boolean pickup) {
    }

    /**
     * TODO
     */
    @Override
    public boolean isLeashed() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public boolean setLeashHolder(Entity holder) {
        return false;
    }

    @Override
    public boolean isGliding() {
        return this.shadow$isElytraFlying();
    }

    /**
     * TODO
     */
    @Override
    public void setGliding(boolean gliding) {

    }

    @Override
    public boolean isSwimming() {
        return this.shadow$isActualySwimming();
    }

    /**
     * TODO
     */
    @Override
    public void setSwimming(boolean swimming) {

    }

    /**
     * TODO
     */
    @Override
    public boolean isRiptiding() {
        return false;
    }

    @Intrinsic
    public boolean api$isSleeping() {
        return this.shadow$isSleeping();
    }

    /**
     * TODO
     */
    @Override
    public void setAI(boolean ai) {
    }

    /**
     * TODO
     */
    @Override
    public boolean hasAI() {
        return false;
    }

    @Override
    public void attack(Entity target) {
        Preconditions.checkNotNull(target, "target");

        this.shadow$attackEntityAsMob((net.minecraft.entity.Entity) target);
    }

    @Override
    public void swingMainHand() {
        this.shadow$swing(Hand.MAIN_HAND, true);
    }

    @Override
    public void swingOffHand() {
        this.shadow$swing(Hand.OFF_HAND, true);
    }

    /**
     * TODO
     */
    @Override
    public void setCollidable(boolean collidable) {

    }

    @Override
    public boolean isCollidable() {
        return this.shadow$canBeCollidedWith();
    }

    /**
     * TODO
     */
    @Override
    public <T> T getMemory(MemoryKey<T> memoryKey) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public <T> void setMemory(MemoryKey<T> memoryKey, T memoryValue) {

    }

    // --- Attributable Implementation ---

    /**
     * TODO
     */
    @Override
    public @Nullable AttributeInstance getAttribute(@NotNull Attribute attribute) {
        return null;
    }

    // --- Damageable Implementation ---


    @Override
    public void damage(double amount) {
        this.damage(amount, null);
    }

    @Override
    public void damage(double amount, Entity source) {
        DamageSource reason = DamageSource.GENERIC;

        if (source instanceof HumanEntity) {
            reason = DamageSource.causePlayerDamage((PlayerEntity) source);
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.causeMobDamage((net.minecraft.entity.LivingEntity) source);
        }

        this.shadow$damageEntity(reason, (float) amount);
    }

    @Override
    public double getHealth() {
        return this.shadow$getHealth();
    }

    @Override
    public void setHealth(double health) {
        this.shadow$setHealth((float) health);
    }

    @Override
    public double getAbsorptionAmount() {
        return this.shadow$getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        this.shadow$setAbsorptionAmount((float) amount);
    }

    @Override
    public double getMaxHealth() {
        return this.shadow$getMaxHealth();
    }

    @Override
    public void setMaxHealth(double health) {
        Preconditions.checkArgument(health > 0, "Max health must be greater than zero");

        this.shadow$getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);

        if (this.getHealth() > health) {
            this.setHealth(health);
        }
    }

    @Override
    public void resetMaxHealth() {
        this.setMaxHealth(this.shadow$getAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttribute().getDefaultValue());
    }

    // --- ProjectileSource Implementation ---


    @Override
    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile) {
        return this.launchProjectile(projectile, null);
    }

    /**
     * TODO - lots of work!
     */
    @Override
    public <T extends Projectile> @NotNull T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity) {
        return null;
    }
}