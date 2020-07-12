package pw.dotdash.mukkit.mixin.api;

import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin_API {

    // --- Shadowed Fields ---

    @Shadow public abstract int getMaxPlayerIdleMinutes();

    @Shadow
    @Final
    private Map<DimensionType, ServerWorld> worlds;

    // --- Shadowed Methods ---

    @Shadow
    public abstract Iterable<ServerWorld> shadow$getWorlds();

    @Shadow
    public abstract RecipeManager shadow$getRecipeManager();

    @Shadow
    public abstract String shadow$getMOTD();

    @Shadow
    public abstract boolean shadow$isServerStopped();
}