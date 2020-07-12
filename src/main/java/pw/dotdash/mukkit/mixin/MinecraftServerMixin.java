package pw.dotdash.mukkit.mixin;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.bukkit.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    /**
     * @author doot
     * @reason Use mukkit server brand
     */
    @Overwrite
    public String getServerModName() {
        return ((Server) this).getName();
    }

    @Redirect(
            method = "convertMapIfNeeded",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/SaveFormat;isOldMapFormat(Ljava/lang/String;)Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;setUserMessage(Lnet/minecraft/util/text/ITextComponent;)V")
            ),
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", remap = false))
    private void logConvertingMapName(Logger logger, String message, String worldNameIn) {
        logger.info(message + " Map: " + worldNameIn);
    }
}