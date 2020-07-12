package pw.dotdash.mukkit.modlauncher.launch;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;

public class ServerLauncher {

    public static void launch(String[] args) {
        LogManager.getLogger("MukkitMain").info("Loading Minecraft server...");
        MinecraftServer.main(args);
    }
}