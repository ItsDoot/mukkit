package pw.dotdash.mukkit.modlauncher.launch;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MukkitMixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfigurations(
                "core.mixins.json",
                "bridge.mixins.json"
        );
    }
}