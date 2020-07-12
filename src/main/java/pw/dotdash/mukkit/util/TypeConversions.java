package pw.dotdash.mukkit.util;

import com.google.common.base.Preconditions;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.dimension.DimensionType;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import java.util.Locale;

public final class TypeConversions {

    public static World.Environment fromMojang(DimensionType type) {
        ResourceLocation location = Preconditions.checkNotNull(net.minecraft.util.registry.Registry.DIMENSION_TYPE.getKey(type));
        return World.Environment.valueOf(location.getPath().toUpperCase());
    }

    public static Item fromBukkit(Material material) {
        ResourceLocation location = TypeConversions.fromBukkit(material.getKey());
        return net.minecraft.util.registry.Registry.ITEM.getValue(location).get();
    }

    public static PistonMoveReaction fromMojang(final PushReaction reaction) {
        switch (reaction) {
            case NORMAL:
                return PistonMoveReaction.MOVE;
            case DESTROY:
                return PistonMoveReaction.BREAK;
            case BLOCK:
                return PistonMoveReaction.BLOCK;
            case IGNORE:
                return PistonMoveReaction.IGNORE;
            case PUSH_ONLY:
                return PistonMoveReaction.PUSH_ONLY;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + reaction);
        }
    }

    public static BlockFace fromMojang(final Direction direction) {
        switch (direction) {
            case DOWN:
                return BlockFace.DOWN;
            case UP:
                return BlockFace.UP;
            case NORTH:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.EAST;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + direction);
        }
    }

    public static Pose fromMojang(final net.minecraft.entity.Pose pose) {
        switch (pose) {
            case STANDING:
                return Pose.STANDING;
            case FALL_FLYING:
                return Pose.FALL_FLYING;
            case SLEEPING:
                return Pose.SLEEPING;
            case SWIMMING:
                return Pose.SWIMMING;
            case SPIN_ATTACK:
                return Pose.SPIN_ATTACK;
            case CROUCHING:
                return Pose.SNEAKING;
            case DYING:
                return Pose.DYING;
            default:
                throw new IllegalArgumentException("Unhandled enum value: " + pose);
        }
    }

    @SuppressWarnings("deprecation")
    public static NamespacedKey fromMojang(final ResourceLocation location) {
        return new NamespacedKey(location.getNamespace(), location.getPath());
    }

    public static ResourceLocation fromBukkit(final NamespacedKey key) {
        return new ResourceLocation(key.getNamespace(), key.getKey());
    }

    public static EntityType fromMojang(final net.minecraft.entity.EntityType<?> type) {
        ResourceLocation location = net.minecraft.util.registry.Registry.ENTITY_TYPE.getKey(type);
        NamespacedKey key = fromMojang(location);
        return Registry.ENTITY_TYPE.get(key);
    }

    public static PotionEffectType fromMojang(final Effect effect) {
        return PotionEffectType.getByName(effect.getName());
    }

    public static Effect fromBukkit(final PotionEffectType type) {
        ResourceLocation location = new ResourceLocation("minecraft", type.getName().toLowerCase(Locale.ENGLISH));
        return net.minecraft.util.registry.Registry.EFFECTS.getValue(location).get();
    }

    public static PotionEffect fromMojang(final EffectInstance effect) {
        return new PotionEffect(
                fromMojang(effect.getPotion()),
                effect.getDuration(),
                effect.getAmplifier(),
                effect.isAmbient(),
                effect.doesShowParticles(),
                effect.isShowIcon()
        );
    }

    public static EffectInstance fromBukkit(final PotionEffect effect) {
        return new EffectInstance(
                fromBukkit(effect.getType()),
                effect.getDuration(),
                effect.getAmplifier(),
                effect.isAmbient(),
                effect.hasParticles(),
                effect.hasIcon()
        );
    }

    public static SoundEvent fromBukkit(Sound sound) {
        return new SoundEvent(new ResourceLocation("minecraft", sound.name().toLowerCase().replace('_', '.')));
    }

    public static SoundCategory fromBukkit(org.bukkit.SoundCategory category) {
        return SoundCategory.valueOf(category.name());
    }

    public static AxisAlignedBB fromBukkit(BoundingBox box) {
        return new AxisAlignedBB(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ());
    }

    private TypeConversions() {
    }
}