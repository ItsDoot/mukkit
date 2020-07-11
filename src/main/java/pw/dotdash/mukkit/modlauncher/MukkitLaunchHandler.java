package pw.dotdash.mukkit.modlauncher;

import cpw.mods.gross.Java9ClassLoaderUtil;
import cpw.mods.modlauncher.api.ILaunchHandlerService;
import cpw.mods.modlauncher.api.ITransformingClassLoader;
import cpw.mods.modlauncher.api.ITransformingClassLoaderBuilder;
import org.apache.logging.log4j.LogManager;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class MukkitLaunchHandler implements ILaunchHandlerService {

    @Override
    public String name() {
        return "mukkit";
    }

    @Override
    public void configureTransformationClassLoader(ITransformingClassLoaderBuilder builder) {
        // Allow the entire classpath to be transformed...for now
        for (final URL url : Java9ClassLoaderUtil.getSystemClassPathURLs()) {
            if (url.toString().contains("mixin") && url.toString().endsWith(".jar")) {
                continue;
            }

            try {
                builder.addTransformationPath(Paths.get(url.toURI()));
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Callable<Void> launchService(String[] arguments, ITransformingClassLoader launchClassLoader) {
        LogManager.getLogger("MukkitLauncher").info("Transitioning to ServerLauncher...");
        return () -> {
            Class.forName("pw.dotdash.mukkit.launch.ServerLauncher", true, launchClassLoader.getInstance())
                    .getMethod("launch", String[].class)
                    .invoke(null, (Object) arguments);
            return null;
        };
    }
}