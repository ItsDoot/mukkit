package pw.dotdash.mukkit.mixin.api.world;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.network.play.server.SUpdateTimePacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SessionLockException;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.*;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.*;
import pw.dotdash.mukkit.bridge.DifficultyBridge;
import pw.dotdash.mukkit.bridge.HeightMapBridge;
import pw.dotdash.mukkit.mixin.accessor.world.GameRulesAccessor;
import pw.dotdash.mukkit.util.TypeConversions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;

@Mixin(ServerWorld.class)
@Implements(@Interface(iface = World.class, prefix = "api$"))
public abstract class ServerWorldMixin_API extends WorldMixin_API implements World {

    // --- Shadowed Fields ---

    @Shadow @Final private Int2ObjectMap<net.minecraft.entity.Entity> entitiesById;
    @Shadow @Final private List<ServerPlayerEntity> players;
    @Shadow public boolean disableLevelSaving;

    // --- Shadowed Methods ---

    @Shadow
    public abstract boolean shadow$addEntity(net.minecraft.entity.Entity entityIn);

    @Shadow
    public abstract void shadow$setSpawnPoint(BlockPos pos);

    @Shadow
    @Nonnull
    public abstract MinecraftServer shadow$getServer();

    @Shadow
    public abstract void shadow$save(@Nullable IProgressUpdate progress, boolean flush, boolean skipSave) throws SessionLockException;

    @Shadow
    public abstract <T extends IParticleData> boolean shadow$spawnParticle(ServerPlayerEntity player, T type, boolean longDistance, double posX, double posY, double posZ, int particleCount, double xOffset, double yOffset, double zOffset, double speed);

    // --- World Implementation ---

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return null;
    }

    @Override
    public Block getBlockAt(Location location) {
        return null;
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return this.getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING);
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return this.getHighestBlockYAt(location, HeightMap.MOTION_BLOCKING);
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z), z);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        Preconditions.checkNotNull(location, "location");

        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z, HeightMap heightMap) {
        Preconditions.checkNotNull(heightMap, "heightMap");

        return this.shadow$getChunk(x >> 4, z >> 4)
                .getTopBlockY(((HeightMapBridge) (Object) heightMap).bridge$toMojang(), x, z);
    }

    @Override
    public int getHighestBlockYAt(Location location, HeightMap heightMap) {
        Preconditions.checkNotNull(location, "location");

        return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public Block getHighestBlockAt(int x, int z, HeightMap heightMap) {
        return this.getBlockAt(x, this.getHighestBlockYAt(x, z, heightMap), z);
    }

    @Override
    public Block getHighestBlockAt(Location location, HeightMap heightMap) {
        Preconditions.checkNotNull(location, "location");

        return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return (Chunk) this.shadow$getChunk(x, z);
    }

    @Override
    public Chunk getChunkAt(Location location) {
        Preconditions.checkNotNull(location, "location");

        return this.getChunkAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(Block block) {
        Preconditions.checkNotNull(block, "block");

        return this.getChunkAt(block.getLocation());
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        Preconditions.checkNotNull(chunk, "chunk");

        return this.shadow$getChunkProvider().isChunkLoaded(((net.minecraft.world.chunk.Chunk) chunk).getPos());
    }

    @Override
    public Chunk[] getLoadedChunks() {
        return new Chunk[0];
    }

    @Override
    public void loadChunk(Chunk chunk) {

    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return this.shadow$getChunkProvider().isChunkLoaded(new ChunkPos(x, z));
    }

    /**
     * TODO - check player chunk map
     */
    @Override
    public boolean isChunkGenerated(int x, int z) {
        return this.isChunkLoaded(x, z);
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return this.isChunkLoaded(x, z);
    }

    @Override
    public void loadChunk(int x, int z) {
        this.loadChunk(x, z, true);
    }

    /**
     * TODO
     */
    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        IChunk chunk = this.shadow$getChunkProvider().getChunk(x, z, generate ? ChunkStatus.FULL : ChunkStatus.EMPTY, true);
        return true;
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        Preconditions.checkNotNull(chunk, "chunk");

        return this.unloadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return this.unloadChunk(x, z, true);
    }

    /**
     * TODO
     */
    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        if (!this.isChunkLoaded(x, z)) {
            return true;
        }

        net.minecraft.world.chunk.Chunk chunk = this.shadow$getChunk(x, z);

        this.unloadChunkRequest(x, z);

        return !this.isChunkLoaded(x, z);
    }

    /**
     * TODO
     */
    @Override
    public boolean unloadChunkRequest(int x, int z) {
        return false;
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO
     */
    @Override
    public boolean refreshChunk(int x, int z) {
        if (!this.isChunkLoaded(x, z)) {
            return false;
        }

        int px = x << 4;
        int pz = z << 4;

        int height = this.getMaxHeight() / 16;
        for (int i = 0; i < 64; i++) {
            this.shadow$notifyBlockUpdate(new BlockPos(px + (i / height), (i % height) * 16, pz), Blocks.AIR.getDefaultState(), Blocks.STONE.getDefaultState(), 3);
        }
        this.shadow$notifyBlockUpdate(new BlockPos(px + 15, height * 16 - 1, pz + 15), Blocks.AIR.getDefaultState(), Blocks.STONE.getDefaultState(), 3);

        return false;
    }

    /**
     * TODO
     */
    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {

    }

    /**
     * TODO
     */
    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public boolean addPluginChunkTicket(int x, int z, Plugin plugin) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public boolean removePluginChunkTicket(int x, int z, Plugin plugin) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void removePluginChunkTickets(Plugin plugin) {

    }

    /**
     * TODO
     */
    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        return null;
    }

    @Override
    public Item dropItem(Location location, ItemStack item) {
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(item, "item");

        ItemEntity entity = new ItemEntity((net.minecraft.world.World) (Object) this, location.getX(), location.getY(), location.getZ());
        entity.setPickupDelay(10);

        this.shadow$addEntity(entity);

        return (Item) entity;
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack item) {
        Preconditions.checkNotNull(location, "location");

        double xs = this.shadow$getRandom().nextFloat() * 0.5f + 0.25d;
        double ys = this.shadow$getRandom().nextFloat() * 0.5f + 0.25d;
        double zs = this.shadow$getRandom().nextFloat() * 0.5f + 0.25d;
        Location newLoc = location.clone().add(xs, ys, zs);

        return this.dropItem(newLoc, item);
    }

    @Override
    public Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
        return this.spawnArrow(location, direction, speed, spread, Arrow.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractArrow> T spawnArrow(Location location, Vector direction, float speed, float spread, Class<T> clazz) {
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(direction, "direction");
        Preconditions.checkNotNull(clazz, "clazz");

        AbstractArrowEntity entity;
        if (SpectralArrow.class.isAssignableFrom(clazz)) {
            entity = new SpectralArrowEntity(net.minecraft.entity.EntityType.SPECTRAL_ARROW, (net.minecraft.world.World) (Object) this);
        } else if (Trident.class.isAssignableFrom(clazz)) {
            entity = new TridentEntity(net.minecraft.entity.EntityType.TRIDENT, (net.minecraft.world.World) (Object) this);
        } else {
            entity = new ArrowEntity(net.minecraft.entity.EntityType.ARROW, (net.minecraft.world.World) (Object) this);
        }

        entity.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entity.shoot(direction.getX(), direction.getY(), direction.getZ(), speed, spread);

        this.shadow$addEntity(entity);

        return (T) entity;
    }

    /**
     * TODO
     */
    @Override
    public boolean generateTree(Location location, TreeType type) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate) {
        return false;
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type) {
        return this.spawn(loc, type.getEntityClass());
    }

    @Override
    public LightningStrike strikeLightning(Location loc) {
        LightningBoltEntity entity = new LightningBoltEntity((net.minecraft.world.World) (Object) this, loc.getX(), loc.getY(), loc.getZ(), false);

        this.shadow$addEntity(entity);

        return (LightningStrike) entity;
    }

    @Override
    public LightningStrike strikeLightningEffect(Location loc) {
        LightningBoltEntity entity = new LightningBoltEntity((net.minecraft.world.World) (Object) this, loc.getX(), loc.getY(), loc.getZ(), true);

        this.shadow$addEntity(entity);

        return (LightningStrike) entity;
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();

        for (net.minecraft.entity.Entity entity : this.entitiesById.values()) {
            entities.add((Entity) entity);
        }

        return entities;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> entities = new ArrayList<>();

        for (net.minecraft.entity.Entity entity : this.entitiesById.values()) {
            if (entity instanceof net.minecraft.entity.LivingEntity) {
                entities.add((LivingEntity) entity);
            }
        }

        return entities;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return (Collection<T>) this.getEntitiesByClasses(classes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        List<T> entities = new ArrayList<>();

        for (net.minecraft.entity.Entity entity : this.entitiesById.values()) {
            if (cls.isInstance(entity)) {
                entities.add((T) entity);
            }
        }

        return entities;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        List<Entity> entities = new ArrayList<>();

        for (net.minecraft.entity.Entity entity : this.entitiesById.values()) {
            for (Class<?> clazz : classes) {
                if (clazz.isInstance(entity)) {
                    entities.add((Entity) entity);
                }
            }
        }

        return entities;
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        for (ServerPlayerEntity player : this.players) {
            players.add((Player) player);
        }

        return players;
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        return this.getNearbyEntities(location, x, y, z, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, Predicate<Entity> filter) {
        Preconditions.checkNotNull(location, "location");

        BoundingBox aabb = BoundingBox.of(location, x, y, z);
        return this.getNearbyEntities(aabb, filter);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox) {
        return this.getNearbyEntities(boundingBox, null);
    }

    @Override
    public Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter) {
        Preconditions.checkNotNull(boundingBox, "boundingBox");

        AxisAlignedBB aabb = TypeConversions.fromBukkit(boundingBox);
        List<Entity> entities = new ArrayList<>();

        for (net.minecraft.entity.Entity entity : this.shadow$getEntitiesInAABBexcluding(null, aabb, entity -> filter.test((Entity) entity))) {
            entities.add((Entity) entity);
        }

        return entities;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, Predicate<Entity> filter) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceEntities(Location start, Vector direction, double maxDistance, double raySize, Predicate<Entity> filter) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTraceBlocks(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, Predicate<Entity> filter) {
        return null;
    }

    @Override
    public String getName() {
        return this.shadow$getWorldInfo().getWorldName();
    }

    /**
     * TODO
     */
    @Override
    public UUID getUID() {
        return null;
    }

    @Override
    public Location getSpawnLocation() {
        BlockPos pos = this.shadow$getSpawnPoint();
        return new Location(this, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean setSpawnLocation(Location location) {
        return this.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        this.shadow$setSpawnPoint(new BlockPos(x, y, z));
        return true;
    }

    @Override
    public long getTime() {
        long time = this.getFullTime() % 24000;
        if (time < 0) {
            time += 24000;
        }
        return time;
    }

    @Override
    public void setTime(long time) {
        long margin = (time - this.getFullTime()) % 24000;
        if (margin < 0) {
            margin += 24000;
        }
        this.setFullTime(this.getFullTime() + margin);
    }

    @Override
    public long getFullTime() {
        return this.shadow$getDayTime();
    }

    @Override
    public void setFullTime(long time) {
        TimeSkipEvent event = new TimeSkipEvent(this, TimeSkipEvent.SkipReason.CUSTOM, time - this.shadow$getDayTime());
        ((Server) this.shadow$getServer()).getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        this.shadow$setDayTime(this.shadow$getDayTime() + event.getSkipAmount());

        // Update all clients immediately
        for (ServerPlayerEntity player : this.players) {
            if (player.connection == null) continue;

            SUpdateTimePacket packet = new SUpdateTimePacket(
                    this.shadow$getGameTime(),
                    this.shadow$getDayTime(),
                    Preconditions.checkNotNull(this.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE))
            );

            player.connection.sendPacket(packet);
        }
    }

    @Override
    public boolean hasStorm() {
        return this.isThundering();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        this.setThundering(hasStorm);
    }

    @Override
    public int getWeatherDuration() {
        return this.shadow$getWorldInfo().getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) {
        this.shadow$getWorldInfo().setRainTime(duration);
    }

    @Override
    public boolean isThundering() {
        return this.shadow$getWorldInfo().isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        this.shadow$getWorldInfo().setThundering(thundering);
    }

    @Override
    public int getThunderDuration() {
        return this.shadow$getWorldInfo().getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) {
        this.shadow$getWorldInfo().setThunderTime(duration);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return this.createExplosion(x, y, z, power, false, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return this.createExplosion(x, y, z, power, setFire, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return this.createExplosion(x, y, z, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, Entity source) {
        this.shadow$createExplosion((net.minecraft.entity.Entity) source, x, y, z, power, setFire, breakBlocks ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
        return true;
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks);
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source) {
        return this.createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks, source);
    }

    @Override
    public Environment getEnvironment() {
        return TypeConversions.fromMojang(this.shadow$getDimension().getType());
    }

    /**
     * TODO
     */
    @Override
    public boolean getPVP() {
        return true;
    }

    /**
     * TODO
     */
    @Override
    public void setPVP(boolean pvp) {

    }

    /**
     * TODO
     */
    @Override
    public ChunkGenerator getGenerator() {
        return null;
    }

    @Override
    public void save() {
        boolean oldSave = this.disableLevelSaving;

        this.disableLevelSaving = false;
        try {
            this.shadow$save(null, false, false);
        } catch (SessionLockException e) {
            ((Server) this.shadow$getServer()).getLogger().log(Level.SEVERE, "Failed to manually save world", e);
        }

        this.disableLevelSaving = oldSave;
    }

    /**
     * TODO
     */
    @Override
    public List<BlockPopulator> getPopulators() {
        return new ArrayList<>();
    }

    /**
     * TODO
     */
    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public FallingBlock spawnFallingBlock(Location location, MaterialData data) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public FallingBlock spawnFallingBlock(Location location, BlockData data) throws IllegalArgumentException {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public FallingBlock spawnFallingBlock(Location location, Material material, byte data) throws IllegalArgumentException {
        return null;
    }

    @Override
    public void playEffect(Location location, Effect effect, int data) {
        this.playEffect(location, effect, data, 64);
    }

    /**
     * TODO
     */
    @Override
    public void playEffect(Location location, Effect effect, int data, int radius) {
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(effect, "effect");
        Preconditions.checkNotNull(location.getWorld(), "location.getWorld()");
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T data) {
        this.playEffect(location, effect, data, 64);
    }

    /**
     * TODO
     */
    @Override
    public <T> void playEffect(Location location, Effect effect, T data, int radius) {
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(effect, "effect");
        Preconditions.checkNotNull(location.getWorld(), "location.getWorld()");
    }

    /**
     * TODO
     */
    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {

    }

    /**
     * TODO
     */
    @Override
    public boolean getAllowAnimals() {
        return true;
    }

    /**
     * TODO
     */
    @Override
    public boolean getAllowMonsters() {
        return true;
    }

    /**
     * TODO
     */
    @Override
    public Biome getBiome(int x, int z) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Biome getBiome(int x, int y, int z) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setBiome(int x, int z, Biome bio) {

    }

    /**
     * TODO
     */
    @Override
    public void setBiome(int x, int y, int z, Biome bio) {

    }

    /**
     * TODO
     */
    @Override
    public double getTemperature(int x, int z) {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public double getTemperature(int x, int y, int z) {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public double getHumidity(int x, int z) {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public double getHumidity(int x, int y, int z) {
        return 0;
    }

    @Override
    public int getMaxHeight() {
        return this.shadow$getActualHeight();
    }

    @Intrinsic
    public int api$getSeaLevel() {
        return this.shadow$getSeaLevel();
    }

    /**
     * TODO
     */
    @Override
    public boolean getKeepSpawnInMemory() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {

    }

    /**
     * TODO
     */
    @Override
    public boolean isAutoSave() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void setAutoSave(boolean value) {

    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.shadow$getWorldInfo().setDifficulty(((DifficultyBridge) (Object) difficulty).bridge$toMojang());
    }

    @Override
    public Difficulty getDifficulty() {
        return ((DifficultyBridge) (Object) this.shadow$getWorldInfo().getDifficulty()).bridge$toBukkit();
    }

    /**
     * TODO
     */
    @Override
    public File getWorldFolder() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public WorldType getWorldType() {
        return WorldType.NORMAL;
    }

    /**
     * TODO
     */
    @Override
    public boolean canGenerateStructures() {
        return true;
    }

    @Override
    public boolean isHardcore() {
        return this.shadow$getWorldInfo().isHardcore();
    }

    @Override
    public void setHardcore(boolean hardcore) {
        this.shadow$getWorldInfo().setHardcore(hardcore);
    }

    /**
     * TODO
     */
    @Override
    public long getTicksPerAnimalSpawns() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {

    }

    /**
     * TODO
     */
    @Override
    public long getTicksPerMonsterSpawns() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {

    }

    /**
     * TODO
     */
    @Override
    public long getTicksPerWaterSpawns() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {

    }

    /**
     * TODO
     */
    @Override
    public long getTicksPerAmbientSpawns() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {

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
    public void setMonsterSpawnLimit(int limit) {

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
    public void setAnimalSpawnLimit(int limit) {

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
    public void setWaterAnimalSpawnLimit(int limit) {

    }

    /**
     * TODO
     */
    @Override
    public int getAmbientSpawnLimit() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void setAmbientSpawnLimit(int limit) {

    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        this.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location location, String sound, float volume, float pitch) {
        this.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
        Preconditions.checkNotNull(sound, "sound");

        this.playSound(location, sound.name().toLowerCase().replace('_', '.'), category, volume, pitch);
    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        Preconditions.checkNotNull(location, "location");
        Preconditions.checkNotNull(sound, "sound");
        Preconditions.checkNotNull(category, "category");

        SPlaySoundEffectPacket packet = new SPlaySoundEffectPacket(
                new SoundEvent(new ResourceLocation(sound)),
                TypeConversions.fromBukkit(category),
                location.getX(),
                location.getY(),
                location.getZ(),
                volume,
                pitch
        );

        this.shadow$getServer().getPlayerList().sendToAllNearExcept(
                null,
                location.getX(), location.getY(), location.getZ(),
                volume > 1.0 ? volume * 16.0 : 16.0,
                this.shadow$getDimension().getType(),
                packet
        );
    }

    @Override
    public String[] getGameRules() {
        return ((GameRulesAccessor) this.shadow$getGameRules()).accessor$getRules()
                .keySet()
                .stream()
                .map(GameRules.RuleKey::getName)
                .toArray(String[]::new);
    }

    @Override
    public String getGameRuleValue(String rule) {
        return ((GameRulesAccessor) this.shadow$getGameRules()).accessor$getRules()
                .get(new GameRules.RuleKey<>(rule)).toString();
    }

    /**
     * TODO
     */
    @Override
    public boolean setGameRuleValue(String rule, String value) {
        return false;
    }

    @Override
    public boolean isGameRule(String rule) {
        return ((GameRulesAccessor) this.shadow$getGameRules()).accessor$getRules()
                .get(new GameRules.RuleKey<>(rule)) != null;
    }

    /**
     * TODO
     */
    @Override
    public <T> T getGameRuleValue(GameRule<T> rule) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public <T> T getGameRuleDefault(GameRule<T> rule) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public <T> boolean setGameRule(GameRule<T> rule, T newValue) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public WorldBorder getWorldBorder() {
        return (WorldBorder) this.shadow$getWorldBorder();
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        this.spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        this.spawnParticle(particle, x, y, z, count, 0.0, 0.0, 0.0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    /**
     * TODO
     */
    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("Data should be " + particle.getDataType() + " but was " + data.getClass());
        }

        this.shadow$spawnParticle(
                null,
                null,
                force,
                x, y, z,
                count,
                offsetX, offsetY, offsetZ,
                extra
        );
    }

    /**
     * TODO
     */
    @Override
    public Location locateNearestStructure(Location origin, StructureType structureType, int radius, boolean findUnexplored) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Raid locateNearestRaid(Location location, int radius) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public List<Raid> getRaids() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public DragonBattle getEnderDragonBattle() {
        return null;
    }
}