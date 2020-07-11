package pw.dotdash.mukkit.mixin.api;

import com.google.common.base.Preconditions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPropertiesProvider;
import net.minecraft.server.dedicated.DedicatedPlayerList;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.ServerProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pw.dotdash.mukkit.extra.ServerExtra;
import pw.dotdash.mukkit.impl.command.MukkitCommandMap;
import pw.dotdash.mukkit.util.UnsafeValuesImpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implements {@link org.bukkit.Server} on {@link net.minecraft.server.dedicated.DedicatedServer}.
 */
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin_API implements ServerExtra {

    @Shadow @Final private ServerPropertiesProvider settings;

    @Shadow public abstract DedicatedPlayerList shadow$getPlayerList();

    @Shadow public abstract ServerProperties shadow$getServerProperties();

    @Shadow public abstract String shadow$getHostname();

    @Shadow public abstract void shadow$stopServer();

    private final Logger api$logger = Logger.getLogger("Mukkit");

    private final MukkitCommandMap api$commandMap = new MukkitCommandMap(this);
    private final PluginManager api$pluginManager = new SimplePluginManager(this, this.api$commandMap);
    private final ServicesManager api$servicesManager = new SimpleServicesManager();

    @Override
    public void loadPlugins() {
        this.api$pluginManager.registerInterface(JavaPluginLoader.class);

        Path pluginsFolder = Paths.get("plugins");

        if (Files.exists(pluginsFolder)) {
            for (Plugin plugin : this.api$pluginManager.loadPlugins(pluginsFolder.toFile())) {
                try {
                    plugin.getLogger().info("Loading " + plugin.getDescription().getFullName());
                    plugin.onLoad();
                } catch (Throwable e) {
                    this.api$logger.log(Level.SEVERE, "Failed to load plugin " + plugin.getDescription().getFullName(), e);
                }
            }
        } else {
            try {
                Files.createDirectories(pluginsFolder);
            } catch (IOException e) {
                this.api$logger.severe("Failed to create plugins directory.");
            }
        }
    }

    @Override
    public void enablePlugins(PluginLoadOrder order) {
        if (order == PluginLoadOrder.STARTUP) {
            // TODO - clear helpMap
        }

        for (Plugin plugin : this.api$pluginManager.getPlugins()) {
            if (!plugin.isEnabled() && (plugin.getDescription().getLoad() == order)) {
                try {
                    this.api$pluginManager.enablePlugin(plugin);
                } catch (Throwable e) {
                    this.api$logger.log(Level.SEVERE, "Failed to enable plugin " + plugin.getDescription().getFullName(), e);
                }
            }
        }

        if (order == PluginLoadOrder.POSTWORLD) {
            // TODO - resync commands
        }
    }

    @Override
    public void disablePlugins() {

    }

    @Override
    @NotNull
    public String getName() {
        return "Mukkit";
    }

    @Override
    public String getVersion() {
        return "1.0.0-SNAPSHOT";
    }

    @Override
    public String getBukkitVersion() {
        return "1.15.2-R0.1-SNAPSHOT";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return this.shadow$getPlayerList().getPlayers().stream()
                .map(player -> (Player) player)
                .collect(Collectors.toList());
    }

    @Override
    public int getMaxPlayers() {
        return this.shadow$getPlayerList().getMaxPlayers();
    }

    @Override
    public int getPort() {
        return ((DedicatedServer) (Object) this).getPort();
    }

    @Override
    public int getViewDistance() {
        return this.shadow$getServerProperties().viewDistance;
    }

    @Override
    public String getIp() {
        return this.shadow$getHostname();
    }

    @Override
    public String getWorldType() {
        return this.shadow$getServerProperties().worldType.getName();
    }

    @Override
    public boolean getGenerateStructures() {
        return this.shadow$getServerProperties().generateStructures;
    }

    @Override
    public boolean getAllowEnd() {
        return this.shadow$getServerProperties().allowNether;
    }

    @Override
    public boolean getAllowNether() {
        return this.shadow$getServerProperties().allowNether;
    }

    @Override
    public boolean hasWhitelist() {
        return this.shadow$getServerProperties().whitelistEnabled.get();
    }

    @Override
    public void setWhitelist(boolean value) {
        this.shadow$getPlayerList().setWhiteListEnabled(value);
        ((MinecraftServer) (Object) this).setWhitelistEnabled(true);
    }

    /**
     * TODO
     */
    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        return null;
    }

    @Override
    public void reloadWhitelist() {
        this.shadow$getPlayerList().reloadWhitelist();
    }

    @Override
    public int broadcastMessage(String message) {
        return this.broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    @Override
    public String getUpdateFolder() {
        return "update";
    }

    @Override
    public File getUpdateFolderFile() {
        return Paths.get("plugins", "update").toFile();
    }

    /**
     * TODO
     */
    @Override
    public long getConnectionThrottle() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public int getTicksPerAnimalSpawns() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public int getTicksPerMonsterSpawns() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public int getTicksPerWaterSpawns() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public int getTicksPerAmbientSpawns() {
        return 0;
    }

    @Override
    public Player getPlayer(String name) {
        Preconditions.checkNotNull(name, "name");

        Player found = this.getPlayerExact(name);
        if (found != null) {
            return found;
        }

        String lowerName = name.toLowerCase(Locale.ENGLISH);
        int delta = Integer.MAX_VALUE;

        for (Player player : this.getOnlinePlayers()) {
            if (player.getName().toLowerCase(Locale.ENGLISH).startsWith(lowerName)) {
                int curDelta = Math.abs(player.getName().length() - lowerName.length());

                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }

                if (curDelta == 0) {
                    break;
                }
            }
        }

        return found;
    }

    @Override
    public Player getPlayerExact(String name) {
        Preconditions.checkNotNull(name, "name");

        return (Player) this.shadow$getPlayerList().getPlayerByUsername(name);
    }

    @Override
    public List<Player> matchPlayer(String name) {
        Preconditions.checkNotNull(name, "name");

        List<Player> matchedPlayers = new ArrayList<>();

        for (Player player : this.getOnlinePlayers()) {
            String playerName = player.getName();

            if (name.equalsIgnoreCase(playerName)) {
                // Found an exact match.
                matchedPlayers.clear();
                matchedPlayers.add(player);
                break;
            }

            if (playerName.toLowerCase(Locale.ENGLISH).contains(name.toLowerCase(Locale.ENGLISH))) {
                // Found a partial match.
                matchedPlayers.add(player);
            }
        }

        return matchedPlayers;
    }

    @Override
    public Player getPlayer(UUID id) {
        Preconditions.checkNotNull(id, "id");

        return (Player) this.shadow$getPlayerList().getPlayerByUUID(id);
    }

    @Override
    public PluginManager getPluginManager() {
        return this.api$pluginManager;
    }

    /**
     * TODO
     */
    @Override
    public BukkitScheduler getScheduler() {
        // TODO
        throw new AbstractMethodError();
    }

    @Override
    public ServicesManager getServicesManager() {
        return this.api$servicesManager;
    }

    /**
     * TODO
     */
    @Override
    public List<World> getWorlds() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public World createWorld(WorldCreator creator) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public boolean unloadWorld(String name, boolean save) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public boolean unloadWorld(World world, boolean save) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public World getWorld(String name) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public World getWorld(UUID uid) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public MapView getMap(int id) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public MapView createMap(World world) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType) {
        return null;

    }

    /**
     * TODO
     */
    @Override
    public ItemStack createExplorerMap(World world, Location location, StructureType structureType, int radius, boolean findUnexplored) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void reload() {
    }

    /**
     * TODO
     */
    @Override
    public void reloadData() {
    }

    @Override
    public Logger getLogger() {
        return this.api$logger;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        Command command = this.api$commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        } else {
            return null;
        }
    }

    @Override
    public void savePlayers() {
        this.shadow$getPlayerList().saveAllPlayerData();
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        Preconditions.checkNotNull(sender, "sender");
        Preconditions.checkNotNull(commandLine, "commandLine");

        if (this.api$commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        if (sender instanceof Player) {
            sender.sendMessage("Unknown command. Type \"/help\" for help!");
        } else {
            sender.sendMessage("Unknown command. Type \"help\" for help!");
        }

        return false;
    }

    /**
     * TODO
     */
    @Override
    public boolean addRecipe(Recipe recipe) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Iterator<Recipe> recipeIterator() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void clearRecipes() {

    }

    /**
     * TODO
     */
    @Override
    public void resetRecipes() {

    }

    /**
     * TODO
     */
    @Override
    public boolean removeRecipe(NamespacedKey key) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public Map<String, String[]> getCommandAliases() {
        return null;
    }

    @Override
    public int getSpawnRadius() {
        return ((DedicatedServer) (Object) this).getSpawnProtectionSize();
    }

    /**
     * TODO
     */
    @Override
    public void setSpawnRadius(int value) {

    }

    @Override
    public boolean getOnlineMode() {
        return this.shadow$getServerProperties().onlineMode;
    }

    @Override
    public boolean getAllowFlight() {
        return this.shadow$getServerProperties().allowFlight;
    }

    @Override
    public boolean isHardcore() {
        return this.shadow$getServerProperties().hardcore;
    }

    @Override
    public void shutdown() {
        ((MinecraftServer) (Object) this).initiateShutdown(false);
    }

    @Override
    public int broadcast(String message, String permission) {
        Set<CommandSender> recipients = new HashSet<>();
        for (Permissible permissible : this.getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                recipients.add((CommandSender) permissible);
            }
        }

        BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(!this.isPrimaryThread(), message, recipients);
        this.getPluginManager().callEvent(broadcastMessageEvent);

        if (broadcastMessageEvent.isCancelled()) {
            return 0;
        }

        message = broadcastMessageEvent.getMessage();

        for (CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    /**
     * TODO
     */
    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        return null;
    }

    @Override
    public Set<String> getIPBans() {
        return this.shadow$getPlayerList().getBannedIPs().getEntries().stream()
                .map(entry -> ((StringTextComponent) entry.getDisplayName()).getText())
                .collect(Collectors.toSet());
    }

    @Override
    public void banIP(String address) {
        Preconditions.checkNotNull(address, "address");

        this.getBanList(BanList.Type.IP).addBan(address, null, null, null);
    }

    @Override
    public void unbanIP(String address) {
        Preconditions.checkNotNull(address, "address");

        this.getBanList(BanList.Type.IP).pardon(address);
    }

    /**
     * TODO
     */
    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public BanList getBanList(BanList.Type type) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Set<OfflinePlayer> getOperators() {
        return null;
    }

    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.valueOf(this.shadow$getServerProperties().gamemode.name());
    }

    /**
     * TODO
     */
    @Override
    public void setDefaultGameMode(GameMode mode) {

    }

    /**
     * TODO
     */
    @Override
    public ConsoleCommandSender getConsoleSender() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public File getWorldContainer() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Messenger getMessenger() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public HelpMap getHelpMap() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Merchant createMerchant(String title) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public int getMonsterSpawnLimit() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public int getAnimalSpawnLimit() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public int getWaterAnimalSpawnLimit() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    @Override
    public boolean isPrimaryThread() {
        return ((MinecraftServer) (Object) this).isOnExecutionThread() || ((MinecraftServer) (Object) this).isServerStopped();
    }

    @Override
    public String getMotd() {
        return ((DedicatedServer) (Object) this).getMOTD();
    }

    @Override
    public String getShutdownMessage() {
        return "Server closed.";
    }

    @Override
    public Warning.WarningState getWarningState() {
        return Warning.WarningState.DEFAULT;
    }

    /**
     * TODO
     */
    @Override
    public ItemFactory getItemFactory() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public ScoreboardManager getScoreboardManager() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public CachedServerIcon getServerIcon() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
        return null;
    }

    @Override
    public int getIdleTimeout() {
        return ((DedicatedServer) (Object) this).getMaxPlayerIdleMinutes();
    }

    @Override
    public void setIdleTimeout(int threshold) {
        ((DedicatedServer) (Object) this).setPlayerIdleTimeout(threshold);
    }

    /**
     * TODO
     */
    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        // TODO
        throw new AbstractMethodError();
    }

    /**
     * TODO
     */
    @Override
    public BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public KeyedBossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Iterator<KeyedBossBar> getBossBars() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public KeyedBossBar getBossBar(NamespacedKey key) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public boolean removeBossBar(NamespacedKey key) {
        return false;
    }

    @Override
    public Entity getEntity(UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid");

        for (ServerWorld world : ((DedicatedServer) (Object) this).getWorlds()) {
            net.minecraft.entity.Entity entity = world.getEntityByUuid(uuid);
            if (entity != null) {
                return (Entity) entity;
            }
        }

        return null;
    }

    /**
     * TODO
     */
    @Override
    public Advancement getAdvancement(NamespacedKey key) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Iterator<Advancement> advancementIterator() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public BlockData createBlockData(Material material) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public BlockData createBlockData(Material material, Consumer<BlockData> consumer) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public BlockData createBlockData(String data) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public BlockData createBlockData(Material material, String data) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public <T extends Keyed> Tag<T> getTag(String registry, NamespacedKey tag, Class<T> clazz) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public <T extends Keyed> Iterable<Tag<T>> getTags(String registry, Class<T> clazz) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public LootTable getLootTable(NamespacedKey key) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public List<Entity> selectEntities(CommandSender sender, String selector) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public UnsafeValues getUnsafe() {
        return UnsafeValuesImpl.INSTANCE;
    }
}