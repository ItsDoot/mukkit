package pw.dotdash.mukkit.launch;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MukkitMixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        Mixins.addConfiguration("core.mixins.json");
    }
}