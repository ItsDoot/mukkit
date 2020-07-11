package pw.dotdash.mukkit.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPropertiesProvider;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.world.chunk.listener.IChunkStatusListenerFactory;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.dotdash.mukkit.extra.ServerExtra;

import java.io.File;
import java.util.logging.Level;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private void helloWorld(CallbackInfoReturnable<Boolean> cir) {
        LogManager.getLogger("Mukkit").info("Hello world!");
    }

    @Inject(method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/dedicated/DedicatedServer;isServerInOnlineMode()Z",
            ordinal = 0
    ))
    private void loadPlugins(CallbackInfoReturnable<Boolean> cir) {
        ServerExtra server = (ServerExtra) this;

        server.getLogger().info("Loading plugins...");
        try {
            server.loadPlugins();
            server.enablePlugins(PluginLoadOrder.STARTUP);
        } catch (Throwable e) {
            server.getLogger().log(Level.SEVERE, "Failed to load plugins.", e);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setupBukkitStatic(File p_i50720_1_, ServerPropertiesProvider p_i50720_2_, DataFixer dataFixerIn,
                                   YggdrasilAuthenticationService p_i50720_4_, MinecraftSessionService p_i50720_5_, GameProfileRepository p_i50720_6_,
                                   PlayerProfileCache p_i50720_7_, IChunkStatusListenerFactory p_i50720_8_, String p_i50720_9_, CallbackInfo ci) {
        Bukkit.setServer(((Server) this));
    }
}