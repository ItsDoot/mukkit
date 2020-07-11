package pw.dotdash.mukkit.testplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("Hello from test plugin!");
    }
}