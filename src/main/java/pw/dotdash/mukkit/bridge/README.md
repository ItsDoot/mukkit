# `bridge`

This package contains "Bridge" interfaces. These interfaces exist to provide conversions between Bukkit and Mojang types when it isn't feasible to make the Mojang type extend the Bukkit type.

## Format

```java
public interface BukkitTypeBridge {
    
    BukkitType bridge$toBukkit();

    MojangType bridge$toMojang();
}
```

The mixin implementations then go under `pw.dotdash.mukkit.mixin.bridge`:

```java
@Mixin(BukkitType.class)
public abstract class BukkitTypeBridge_Bukkit implements BukkitTypeBridge {

    @Override
    public BukkitType bridge$toBukkit() {
        return this;
    }

    @Override
    public MojangType bridge$toMojang() {
        // convert...
    }
}

@Mixin(MojangType.class)
public abstract class BukkitTypeBridge_Mojang implements BukkitTypeBridge {

    @Override
    public BukkitType bridge$toBukkit() {
        // convert...
    }

    @Override
    public MojangType bridge$toMojang() {
        return this;
    }
}
```