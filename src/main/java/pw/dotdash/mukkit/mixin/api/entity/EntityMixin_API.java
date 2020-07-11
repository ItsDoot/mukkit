package pw.dotdash.mukkit.mixin.api.entity;

import com.google.common.base.Preconditions;
import net.minecraft.block.material.PushReaction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.*;
import pw.dotdash.mukkit.util.TypeConversions;
import pw.dotdash.mukkit.util.PermissibleUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(net.minecraft.entity.Entity.class)
@Implements(@Interface(iface = Entity.class, prefix = "api$"))
public abstract class EntityMixin_API implements Entity {

    // --- Shadowed Fields ---

    @Shadow public boolean onGround;
    @Shadow public float rotationYaw;
    @Shadow public float rotationPitch;
    @Shadow public float fallDistance;
    @Shadow public int ticksExisted;
    @Shadow public int hurtResistantTime;

    // --- Shadowed Methods ---

    @Shadow
    public abstract MinecraftServer shadow$getServer();

    @Shadow
    public abstract float shadow$getWidth();

    @Shadow
    public abstract float shadow$getHeight();

    @Shadow
    public abstract double shadow$getPosX();

    @Shadow
    public abstract double shadow$getPosY();

    @Shadow
    public abstract double shadow$getPosZ();

    @Shadow
    public abstract Vec3d shadow$getMotion();

    @Shadow
    public abstract void shadow$setMotion(Vec3d motionIn);

    @Shadow
    public abstract AxisAlignedBB shadow$getBoundingBox();

    @Shadow
    public abstract net.minecraft.world.World shadow$getEntityWorld();

    @Shadow
    public abstract void shadow$setLocationAndAngles(double x, double y, double z, float yaw, float pitch);

    @Shadow
    public abstract void shadow$setRotationYawHead(float rotation);

    @Shadow
    public abstract net.minecraft.entity.Entity shadow$getControllingPassenger();

    @Shadow
    public abstract List<net.minecraft.entity.Entity> shadow$getPassengers();

    @Shadow
    public abstract void shadow$removePassengers();

    @Shadow
    public abstract UUID shadow$getUniqueID();

    @Shadow
    public abstract boolean shadow$isPassenger();

    @Shadow
    public abstract void shadow$stopRiding();

    @Shadow
    public abstract net.minecraft.entity.Entity shadow$getRidingEntity();

    @Shadow
    public abstract PushReaction shadow$getPushReaction();

    @Shadow
    public abstract Direction shadow$getAdjustedHorizontalFacing();

    @Shadow
    public abstract net.minecraft.entity.EntityType<?> shadow$getType();

    @Shadow
    public abstract int shadow$getEntityId();

    @Shadow
    public abstract Set<String> shadow$getTags();

    @Shadow
    public abstract boolean shadow$addTag(String tag);

    @Shadow
    public abstract boolean shadow$removeTag(String tag);

    @Shadow
    public abstract boolean shadow$isBeingRidden();

    @Shadow
    public abstract boolean shadow$isAlive();

    @Shadow
    public abstract ITextComponent shadow$getCustomName();

    @Shadow
    public abstract void shadow$setCustomName(ITextComponent name);

    @Shadow
    public abstract ITextComponent shadow$getDisplayName();

    @Shadow
    public abstract boolean shadow$isCustomNameVisible();

    @Shadow
    public abstract void shadow$setCustomNameVisible(boolean alwaysRenderNameTag);

    @Shadow
    public abstract boolean shadow$isGlowing();

    @Shadow
    public abstract void shadow$setGlowing(boolean glowingIn);

    @Shadow
    public abstract boolean shadow$isInvulnerable();

    @Shadow
    public abstract void shadow$setInvulnerable(boolean isInvulnerable);

    @Shadow
    public abstract boolean shadow$isSilent();

    @Shadow
    public abstract void shadow$setSilent(boolean isSilent);

    @Shadow
    public abstract boolean shadow$hasNoGravity();

    @Shadow
    public abstract void shadow$setNoGravity(boolean noGravity);

    @Shadow
    public abstract int shadow$getPortalCooldown();

    @Shadow
    public abstract int shadow$getFireTimer();

    @Shadow
    public abstract void shadow$setFireTimer(int p_223308_1_);

    @Shadow
    public abstract void shadow$remove();

    @Shadow
    public abstract net.minecraft.entity.Pose shadow$getPose();

    @Shadow
    public abstract float shadow$getEyeHeight();

    @Shadow
    public abstract int shadow$getAir();

    @Shadow
    public abstract int shadow$getMaxAir();

    @Shadow
    public abstract void shadow$setAir(int air);

    // --- Entity Implementation ---

    @Override
    public Location getLocation() {
        return new Location((World) this.shadow$getEntityWorld(), this.shadow$getPosX(), this.shadow$getPosY(), this.shadow$getPosZ());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc == null) {
            return null;
        }

        loc.setWorld((World) this.shadow$getEntityWorld());
        loc.setX(this.shadow$getPosX());
        loc.setY(this.shadow$getPosY());
        loc.setZ(this.shadow$getPosZ());
        return loc;
    }

    @Override
    public void setVelocity(Vector velocity) {
        this.shadow$setMotion(new Vec3d(velocity.getX(), velocity.getY(), velocity.getZ()));
    }

    @Override
    public Vector getVelocity() {
        Vec3d motion = this.shadow$getMotion();
        return new Vector(motion.getX(), motion.getY(), motion.getZ());
    }

    @Intrinsic
    public double api$getHeight() {
        return this.shadow$getHeight();
    }

    @Intrinsic
    public double api$getWidth() {
        return this.shadow$getWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        AxisAlignedBB aabb = this.shadow$getBoundingBox();
        return new BoundingBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    @Override
    public boolean isOnGround() {
        return this.onGround;
    }

    @Override
    public World getWorld() {
        return (World) this.shadow$getEntityWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(yaw, "yaw is not finite");
        NumberConversions.checkFinite(pitch, "pitch is not finite");

        float yawNormal = Location.normalizeYaw(yaw);
        float pitchNormal = Location.normalizePitch(pitch);

        this.rotationYaw = yawNormal;
        this.rotationPitch = pitchNormal;
        this.shadow$setRotationYawHead(yawNormal);
    }

    @Override
    public boolean teleport(Location location) {
        return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkNotNull(location, "location");
        location.checkFinite();

        if (this.shadow$isBeingRidden() || this.isDead()) {
            return false;
        }

        // Dismount before teleporting.
        this.shadow$stopRiding();

        if (location.getWorld() != this.getWorld()) {
            // TODO: hand off to vanilla
            return true;
        }

        // TODO Note: no event is thrown here; cannot be cancelled
        this.shadow$setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.shadow$setRotationYawHead(location.getYaw());
        return true;
    }

    @Override
    public boolean teleport(Entity destination) {
        return this.teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(destination.getLocation(), cause);
    }

    @Override
    public List<Entity> getNearbyEntities(double x, double y, double z) {
        AxisAlignedBB aabb = this.shadow$getBoundingBox().grow(x, y, z);
        List<net.minecraft.entity.Entity> entities = this.shadow$getEntityWorld().getEntitiesWithinAABB(net.minecraft.entity.Entity.class, aabb);

        return entities.stream()
                .map(entity -> (Entity) entity)
                .collect(Collectors.toList());
    }

    @Intrinsic
    public int api$getEntityId() {
        return this.shadow$getEntityId();
    }

    @Override
    public int getFireTicks() {
        return this.shadow$getFireTimer();
    }

    /**
     * TODO
     */
    @Override
    public int getMaxFireTicks() {
        return 0;
    }

    @Override
    public void setFireTicks(int ticks) {
        this.shadow$setFireTimer(ticks);
    }

    @Intrinsic
    public void api$remove() {
        this.shadow$remove();
    }

    @Override
    public boolean isDead() {
        return !this.shadow$isAlive();
    }

    @Override
    public boolean isValid() {
        // TODO: isChunkLoaded
        return this.shadow$isAlive();
    }

    @Override
    public Server getServer() {
        return (Server) Preconditions.checkNotNull(this.shadow$getServer(), "server was null");
    }

    /**
     * TODO
     */
    @Override
    public boolean isPersistent() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void setPersistent(boolean persistent) {

    }

    @Override
    public Entity getPassenger() {
        return (Entity) this.shadow$getControllingPassenger();
    }

    /**
     * TODO
     */
    @Override
    public boolean setPassenger(Entity passenger) {
        return false;
    }

    @Intrinsic
    public List<Entity> api$getPassengers() {
        return this.shadow$getPassengers().stream()
                .map(entity -> (Entity) entity)
                .collect(Collectors.toList());
    }

    /**
     * TODO
     */
    @Override
    public boolean addPassenger(Entity passenger) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public boolean removePassenger(Entity passenger) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.shadow$getPassengers().isEmpty();
    }

    @Override
    public boolean eject() {
        if (this.isEmpty()) {
            return false;
        }

        this.shadow$removePassengers();
        return true;
    }

    @Override
    public float getFallDistance() {
        return this.fallDistance;
    }

    @Override
    public void setFallDistance(float distance) {
        this.fallDistance = distance;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {

    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return this.shadow$getUniqueID();
    }

    @Override
    public int getTicksLived() {
        return this.ticksExisted;
    }

    @Override
    public void setTicksLived(int value) {
        this.ticksExisted = value;
    }

    @Override
    public void playEffect(EntityEffect type) {
        Preconditions.checkNotNull(type, "type");

        if (type.getApplicable().isInstance(this)) {
            // TODO
        }
    }

    @Override
    public EntityType getType() {
        return TypeConversions.fromMojang(this.shadow$getType());
    }

    @Override
    public boolean isInsideVehicle() {
        return this.shadow$isPassenger();
    }

    @Override
    public boolean leaveVehicle() {
        if (!this.isInsideVehicle()) {
            return false;
        }

        this.shadow$stopRiding();
        return true;
    }

    @Override
    public Entity getVehicle() {
        return (Entity) this.shadow$getRidingEntity();
    }

    @Intrinsic
    public void api$setCustomNameVisible(boolean flag) {
        this.shadow$setCustomNameVisible(flag);
    }

    @Intrinsic
    public boolean api$isCustomNameVisible() {
        return this.shadow$isCustomNameVisible();
    }

    @Intrinsic
    public void api$setGlowing(boolean flag) {
        this.shadow$setGlowing(flag);
    }

    @Intrinsic
    public boolean api$isGlowing() {
        return this.shadow$isGlowing();
    }

    @Intrinsic
    public void api$setInvulnerable(boolean flag) {
        this.shadow$setInvulnerable(flag);
    }

    @Intrinsic
    public boolean api$isInvulnerable() {
        return this.shadow$isInvulnerable();
    }

    @Intrinsic
    public boolean api$isSilent() {
        return this.shadow$isSilent();
    }

    @Intrinsic
    public void api$setSilent(boolean flag) {
        this.shadow$setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return !this.shadow$hasNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        this.shadow$setNoGravity(!gravity);
    }

    @Intrinsic
    public int api$getPortalCooldown() {
        return this.shadow$getPortalCooldown();
    }

    /**
     * TODO - currently portal cooldown is hardcoded
     *
     * @see net.minecraft.entity.Entity#getPortalCooldown()
     */
    @Override
    public void setPortalCooldown(int cooldown) {
    }

    @Override
    public Set<String> getScoreboardTags() {
        return this.shadow$getTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return this.shadow$addTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return this.shadow$removeTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return TypeConversions.fromMojang(this.shadow$getPushReaction());
    }

    @Override
    public BlockFace getFacing() {
        return TypeConversions.fromMojang(this.shadow$getAdjustedHorizontalFacing());
    }

    @Override
    public Pose getPose() {
        return TypeConversions.fromMojang(this.shadow$getPose());
    }

    // --- Nameable Implementation ---

    @Override
    public String getCustomName() {
        return this.shadow$getCustomName().getString();
    }

    @Override
    public void setCustomName(String name) {
        this.shadow$setCustomName(new StringTextComponent(name));
    }

    // --- CommandSender Implementation ---

    @Override
    public void sendMessage(String message) {
        // Can't message regular ole entities!
    }

    @Override
    public void sendMessage(String[] messages) {
        // Can't message regular ole entities!
    }

    @Override
    public String getName() {
        return this.shadow$getDisplayName().getString();
    }

    // --- Metadatable Implementation ---

    /**
     * TODO
     */
    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
    }

    /**
     * TODO
     */
    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
    }

    // --- Permissible Implementation ---

    @Override
    public boolean isPermissionSet(String name) {
        return PermissibleUtil.getDefaultEntity().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return PermissibleUtil.getDefaultEntity().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return PermissibleUtil.getDefaultEntity().hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return PermissibleUtil.getDefaultEntity().hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return PermissibleUtil.getDefaultEntity().addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return PermissibleUtil.getDefaultEntity().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
         return PermissibleUtil.getDefaultEntity().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return PermissibleUtil.getDefaultEntity().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        PermissibleUtil.getDefaultEntity().removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        PermissibleUtil.getDefaultEntity().recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return PermissibleUtil.getDefaultEntity().getEffectivePermissions();
    }

    // --- ServerOperator Implementation ---

    @Override
    public boolean isOp() {
        return PermissibleUtil.getDefaultEntity().isOp();
    }

    @Override
    public void setOp(boolean value) {
        PermissibleUtil.getDefaultEntity().setOp(value);
    }

    // --- PersistentDataHolder Implementation ---

    /**
     * TODO
     */
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null;
    }
}