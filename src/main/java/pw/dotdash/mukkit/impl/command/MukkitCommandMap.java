package pw.dotdash.mukkit.impl.command;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MukkitCommandMap extends SimpleCommandMap {

    public MukkitCommandMap(@NotNull Server server) {
        super(server);
    }

    public Map<String, Command> getKnownCommands() {
        return this.knownCommands;
    }
}