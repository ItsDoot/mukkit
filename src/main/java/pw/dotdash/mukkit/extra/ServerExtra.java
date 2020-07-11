package pw.dotdash.mukkit.extra;

import org.bukkit.Server;
import org.bukkit.plugin.PluginLoadOrder;

public interface ServerExtra extends Server {

    void loadPlugins();

    void enablePlugins(PluginLoadOrder order);

    void disablePlugins();
}