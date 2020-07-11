package pw.dotdash.mukkit.mixin.api.entity.player;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.server.*;
import net.minecraft.particles.IParticleData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.WhitelistEntry;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.*;
import pw.dotdash.mukkit.mixin.accessor.net.minecraft.entity.player.PlayerAbilitiesAccessor;
import pw.dotdash.mukkit.mixin.accessor.net.minecraft.network.play.server.SPacketListHeaderFooterPacketAccessor;
import pw.dotdash.mukkit.mixin.accessor.net.minecraft.util.FoodStatsAccessor;
import pw.dotdash.mukkit.util.TypeConversions;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Mixin(ServerPlayerEntity.class)
@Implements(@Interface(iface = Player.class, prefix = "player$"))
public abstract class ServerPlayerEntityMixin_API extends PlayerEntityMixin_API implements Player {

    // --- Mixin'd Fields ---

    /**
     * TODO proper legacy text serialization
     */
    private StringTextComponent playerListHeader = null;
    private StringTextComponent playerListFooter = null;

    // --- Shadowed Fields ---

    @Shadow public ServerPlayNetHandler connection;
    @Shadow private int lastExperience;
    @Shadow private String language;
    @Shadow @Final public MinecraftServer server;
    @Shadow @Final public PlayerInteractionManager interactionManager;

    // --- Shadowed Methods ---

    @Shadow
    public abstract void shadow$giveExperiencePoints(int p_195068_1_);

    @Shadow
    public abstract void shadow$addExperienceLevel(int levels);

    @Shadow
    public abstract void shadow$loadResourcePack(String url, String hash);

    @Shadow
    public abstract net.minecraft.entity.Entity shadow$getSpectatingEntity();

    @Shadow
    public abstract void shadow$setSpectatingEntity(net.minecraft.entity.Entity entityToSpectate);

    @Shadow
    public abstract ServerWorld shadow$getServerWorld();

    // --- Player Implementation ---

    /**
     * TODO
     */
    @Override
    public String getDisplayName() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setDisplayName(String name) {

    }

    /**
     * TODO
     */
    @Override
    public String getPlayerListName() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setPlayerListName(String name) {

    }

    @Override
    public String getPlayerListHeader() {
        return this.playerListHeader.getString();
    }

    @Override
    public String getPlayerListFooter() {
        return this.playerListFooter.getText();
    }

    @Override
    public void setPlayerListHeader(String header) {
        this.playerListHeader = new StringTextComponent(header);
        this.updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListFooter(String footer) {
        this.playerListFooter = new StringTextComponent(footer);
        this.updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        this.playerListHeader = new StringTextComponent(header);
        this.playerListFooter = new StringTextComponent(footer);
        this.updatePlayerListHeaderFooter();
    }

    private void updatePlayerListHeaderFooter() {
        if (this.connection == null) return;

        SPlayerListHeaderFooterPacket packet = new SPlayerListHeaderFooterPacket();
        ((SPacketListHeaderFooterPacketAccessor) packet)
                .setHeader(this.playerListHeader == null ? new StringTextComponent("") : this.playerListHeader);
        ((SPacketListHeaderFooterPacketAccessor) packet)
                .setFooter(this.playerListFooter == null ? new StringTextComponent("") : this.playerListFooter);

        this.connection.sendPacket(packet);
    }

    @Override
    public void setCompassTarget(Location loc) {

    }

    /**
     * TODO
     */
    @Override
    public Location getCompassTarget() {
        return this.getLocation();
    }

    @Override
    public InetSocketAddress getAddress() {
        if (this.connection == null) return null;

        SocketAddress address = this.connection.netManager.getRemoteAddress();
        if (address instanceof InetSocketAddress) {
            return (InetSocketAddress) address;
        }

        return null;
    }

    /**
     * TODO proper legacy text serialization
     */
    @Override
    public void sendRawMessage(String message) {
        if (this.connection == null) return;

        this.connection.sendPacket(new SChatPacket(new StringTextComponent(message), ChatType.CHAT));
    }

    /**
     * TODO proper legacy text serialization
     */
    @Override
    public void kickPlayer(String message) {
        if (this.connection == null) return;

        this.connection.disconnect(new StringTextComponent(message == null ? "" : message));
    }

    /**
     * TODO
     */
    @Override
    public void chat(String msg) {

    }

    @Override
    public boolean performCommand(String command) {
        return this.getServer().dispatchCommand(this, command);
    }

    @Intrinsic
    public boolean player$isSneaking() {
        return ((ServerPlayerEntity) (Object) this).isSneaking();
    }

    @Intrinsic
    public void player$setSneaking(boolean sneak) {
        ((ServerPlayerEntity) (Object) this).setSneaking(sneak);
    }

    @Intrinsic
    public boolean player$isSprinting() {
        return ((ServerPlayerEntity) (Object) this).isSprinting();
    }

    @Intrinsic
    public void player$setSprinting(boolean sprinting) {
        ((ServerPlayerEntity) (Object) this).setSprinting(sprinting);
    }

    /**
     * TODO
     */
    @Override
    public void saveData() {

    }

    /**
     * TODO
     */
    @Override
    public void loadData() {

    }

    /**
     * TODO
     */
    @Override
    public void setSleepingIgnored(boolean isSleeping) {

    }

    /**
     * TODO
     */
    @Override
    public boolean isSleepingIgnored() {
        return false;
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        this.playNote(loc, Instrument.getByType(instrument), new Note(note));
    }

    /**
     * TODO
     */
    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {

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
        this.playSound(location, sound.name().toLowerCase().replace('_', '.'), category, volume, pitch);
    }

    @Override
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
        if (this.connection == null) return;

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
        this.connection.sendPacket(packet);
    }

    @Override
    public void stopSound(Sound sound) {
        this.stopSound(sound, null);
    }

    @Override
    public void stopSound(String sound) {
        this.stopSound(sound, null);
    }

    @Override
    public void stopSound(Sound sound, SoundCategory category) {
        this.stopSound(sound.name().toLowerCase().replace('_', '.'), category);
    }

    @Override
    public void stopSound(String sound, SoundCategory category) {
        if (this.connection == null) return;

        net.minecraft.util.SoundCategory packetCategory =
                category == null ? net.minecraft.util.SoundCategory.MASTER : TypeConversions.fromBukkit(category);

        SStopSoundPacket packet = new SStopSoundPacket(
                new ResourceLocation(sound),
                packetCategory
        );
        this.connection.sendPacket(packet);
    }

    /**
     * TODO
     */
    @Override
    public void playEffect(Location loc, Effect effect, int data) {

    }

    /**
     * TODO
     */
    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {

    }

    /**
     * TODO
     */
    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {

    }

    /**
     * TODO
     */
    @Override
    public void sendBlockChange(Location loc, BlockData block) {

    }

    /**
     * TODO
     */
    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void sendMap(MapView map) {

    }

    /**
     * TODO
     */
    @Override
    public void updateInventory() {

    }

    /**
     * TODO
     */
    @Override
    public void setPlayerTime(long time, boolean relative) {

    }

    /**
     * TODO
     */
    @Override
    public long getPlayerTime() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public long getPlayerTimeOffset() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public boolean isPlayerTimeRelative() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void resetPlayerTime() {

    }

    /**
     * TODO
     */
    @Override
    public void setPlayerWeather(WeatherType type) {

    }

    /**
     * TODO
     */
    @Override
    public WeatherType getPlayerWeather() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void resetPlayerWeather() {

    }

    @Override
    public void giveExp(int amount) {
        this.shadow$giveExperiencePoints(amount);
    }

    @Override
    public void giveExpLevels(int amount) {
        this.shadow$addExperienceLevel(amount);
    }

    @Override
    public float getExp() {
        return this.experience;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(0.0 <= exp && exp <= 1.0, "Experience must be between 0.0 and 1.0");

        this.experience = exp;
        this.lastExperience = -1;
    }

    @Override
    public int getLevel() {
        return this.experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative");

        this.experienceLevel = level;
    }

    @Override
    public int getTotalExperience() {
        return this.experienceTotal;
    }

    @Override
    public void setTotalExperience(int exp) {
        Preconditions.checkArgument(exp >= 0, "Total experience must not be negative");

        this.experienceTotal = exp;
    }

    @Override
    public void sendExperienceChange(float progress) {
        this.sendExperienceChange(progress, this.getLevel());
    }

    @Override
    public void sendExperienceChange(float progress, int level) {
        Preconditions.checkArgument(0.0 <= progress && progress <= 1.0, "Progress must be between 0.0 and 1.0");
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative");

        if (this.connection == null) return;

        SSetExperiencePacket packet = new SSetExperiencePacket(progress, this.getTotalExperience(), level);
        this.connection.sendPacket(packet);
    }

    @Override
    public float getExhaustion() {
        return ((FoodStatsAccessor) this.shadow$getFoodStats()).getFoodExhaustionLevel();
    }

    @Override
    public void setExhaustion(float value) {
        ((FoodStatsAccessor) this.shadow$getFoodStats()).setFoodExhaustionLevel(value);
    }

    @Override
    public float getSaturation() {
        return this.shadow$getFoodStats().getSaturationLevel();
    }

    @Override
    public void setSaturation(float value) {
        ((FoodStatsAccessor) this.shadow$getFoodStats()).setFoodSaturationLevel(value);
    }

    @Override
    public int getFoodLevel() {
        return this.shadow$getFoodStats().getFoodLevel();
    }

    @Override
    public void setFoodLevel(int value) {
        this.shadow$getFoodStats().setFoodLevel(value);
    }

    @Override
    public boolean getAllowFlight() {
        return this.abilities.allowFlying;
    }

    @Override
    public void setAllowFlight(boolean flight) {
        this.abilities.allowFlying = flight;
    }

    /**
     * TODO
     */
    @Override
    public void hidePlayer(Player player) {

    }

    /**
     * TODO
     */
    @Override
    public void hidePlayer(Plugin plugin, Player player) {

    }

    /**
     * TODO
     */
    @Override
    public void showPlayer(Player player) {

    }

    /**
     * TODO
     */
    @Override
    public void showPlayer(Plugin plugin, Player player) {

    }

    /**
     * TODO
     */
    @Override
    public boolean canSee(Player player) {
        return true;
    }

    @Override
    public boolean isFlying() {
        return this.abilities.isFlying;
    }

    @Override
    public void setFlying(boolean value) {
        this.abilities.isFlying = value;
    }

    @Override
    public float getFlySpeed() {
        return this.abilities.getFlySpeed() * 2.0f;
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        ((PlayerAbilitiesAccessor) this.abilities).setFlySpeed(value / 2.0f);
    }

    @Override
    public float getWalkSpeed() {
        return this.abilities.getWalkSpeed() * 2.0f;
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        ((PlayerAbilitiesAccessor) this.abilities).setWalkSpeed(value / 2.0f);
        this.shadow$getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(value / 2.0f);
    }

    @Override
    public void setTexturePack(String url) {
        this.setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        Preconditions.checkNotNull(url, "url");

        this.shadow$loadResourcePack(url, "null");
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(hash, "hash");
        Preconditions.checkArgument(hash.length == 20, "Resource pack hash must be 20 bytes long");

        this.shadow$loadResourcePack(url, BaseEncoding.base16().lowerCase().encode(hash));
    }

    /**
     * TODO
     */
    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {

    }

    /**
     * TODO
     */
    @Override
    public boolean isHealthScaled() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void setHealthScaled(boolean scale) {

    }

    /**
     * TODO
     */
    @Override
    public void setHealthScale(double scale) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public double getHealthScale() {
        return 0;
    }

    @Override
    public Entity getSpectatorTarget() {
        return (Entity) this.shadow$getSpectatingEntity();
    }

    @Override
    public void setSpectatorTarget(Entity entity) {
        Preconditions.checkState(this.getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");

        this.shadow$setSpectatingEntity((net.minecraft.entity.Entity) entity);
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        this.sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (this.connection == null) return;

        STitlePacket titleTimePacket = new STitlePacket(fadeIn, stay, fadeOut);
        this.connection.sendPacket(titleTimePacket);

        if (title != null) {
            STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE, new StringTextComponent(title));
            this.connection.sendPacket(titlePacket);
        }
        if (subtitle != null) {
            STitlePacket subtitlePacket = new STitlePacket(STitlePacket.Type.SUBTITLE, new StringTextComponent(subtitle));
            this.connection.sendPacket(subtitlePacket);
        }
    }

    @Override
    public void resetTitle() {
        if (this.connection == null) return;

        STitlePacket packet = new STitlePacket(STitlePacket.Type.RESET, null, -1, -1, -1);
        this.connection.sendPacket(packet);
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
        this.spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
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

    /**
     * TODO
     */
    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        if (data != null && !particle.getDataType().isInstance(data)) {
            throw new IllegalArgumentException("Data should be " + particle.getDataType() + " but was " + data.getClass());
        }

        if (this.connection == null) return;

        IParticleData particleData = null;

        SSpawnParticlePacket packet = new SSpawnParticlePacket(
                particleData,
                true,
                x, y, z,
                (float) offsetX, (float) offsetY, (float) offsetZ,
                (float) extra,
                count
        );
        this.connection.sendPacket(packet);
    }

    /**
     * TODO
     */
    @Override
    public AdvancementProgress getAdvancementProgress(Advancement advancement) {
        return null;
    }

    @Override
    public int getClientViewDistance() {
        return this.getServer().getViewDistance();
    }

    @Override
    public String getLocale() {
        return this.language;
    }

    @Override
    public void updateCommands() {
        this.server.getCommandManager().send((ServerPlayerEntity) (Object) this);
    }

    /**
     * TODO
     */
    @Override
    public void openBook(ItemStack book) {

    }

    // --- OfflinePlayer Implementation ---

    @Override
    public boolean isOnline() {
        return this.getServer().getPlayer(this.getUniqueId()) != null;
    }

    @Override
    public boolean isBanned() {
        return this.getServer().getBanList(BanList.Type.NAME).isBanned(this.getName());
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.getPlayerList().getWhitelistedPlayers()
                .isWhitelisted(this.shadow$getGameProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        this.server.getPlayerList().getWhitelistedPlayers()
                .addEntry(new WhitelistEntry(this.shadow$getGameProfile()));
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    /**
     * TODO
     */
    @Override
    public long getFirstPlayed() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public long getLastPlayed() {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {

    }

    /**
     * TODO
     */
    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {

    }

    /**
     * TODO
     */
    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {

    }

    // --- ConfigurationSerializable Implementation ---

    /**
     * TODO
     */
    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    // --- Conversable Implementation ---

    /**
     * TODO
     */
    @Override
    public boolean isConversing() {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void acceptConversationInput(String input) {

    }

    /**
     * TODO
     */
    @Override
    public boolean beginConversation(Conversation conversation) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void abandonConversation(Conversation conversation) {

    }

    /**
     * TODO
     */
    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {

    }

    // --- HumanEntity Implementation ---

    /**
     * TODO
     */
    @Override
    public PlayerInventory getInventory() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public Inventory getEnderChest() {
        return null;
    }

    @Override
    public MainHand getMainHand() {
        return ((ServerPlayerEntity) (Object) this).getPrimaryHand() == HandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    /**
     * TODO
     */
    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public InventoryView getOpenInventory() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public InventoryView openInventory(Inventory inventory) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void openInventory(InventoryView inventory) {

    }

    /**
     * TODO
     */
    @Override
    public InventoryView openMerchant(Villager trader, boolean force) {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        return null;
    }

    @Override
    public void closeInventory() {
        ((ServerPlayerEntity) (Object) this).closeContainer();
    }

    @Override
    public ItemStack getItemInHand() {
        return this.getInventory().getItemInMainHand();
    }

    @Override
    public void setItemInHand(ItemStack item) {
        Preconditions.checkNotNull(item, "item");

        this.getInventory().setItemInMainHand(item);
    }

    /**
     * TODO
     */
    @Override
    public ItemStack getItemOnCursor() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setItemOnCursor(ItemStack item) {

    }

    @Override
    public boolean hasCooldown(Material material) {
        Preconditions.checkNotNull(material, "material");

        Item item = TypeConversions.fromBukkit(material);
        return this.shadow$getCooldownTracker().hasCooldown(item);
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkNotNull(material, "material");

        Item item = TypeConversions.fromBukkit(material);
        return (int) this.shadow$getCooldownTracker().getCooldown(item, 0.0f);
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        Preconditions.checkNotNull(material, "material");
        Preconditions.checkArgument(ticks >= 0, "Ticks must not be negative");

        Item item = TypeConversions.fromBukkit(material);
        this.shadow$getCooldownTracker().setCooldown(item, ticks);
    }

    @Override
    public int getSleepTicks() {
        return this.shadow$getSleepTimer();
    }

    /**
     * TODO
     */
    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setBedSpawnLocation(Location location) {

    }

    /**
     * TODO
     */
    @Override
    public void setBedSpawnLocation(Location location, boolean force) {

    }

    /**
     * TODO
     */
    @Override
    public boolean sleep(Location location, boolean force) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public void wakeup(boolean setSpawnLocation) {

    }

    /**
     * TODO
     */
    @Override
    public Location getBedLocation() {
        return null;
    }

    @Override
    public GameMode getGameMode() {
        return TypeConversions.fromMojang(this.interactionManager.getGameType());
    }

    @Override
    public void setGameMode(GameMode mode) {
        this.interactionManager.setGameType(TypeConversions.fromBukkit(mode));
    }

    @Override
    public boolean isBlocking() {
        return this.shadow$isActiveItemStackBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return this.shadow$isHandActive();
    }

    /**
     * TODO
     */
    @Override
    public int getExpToLevel() {
        return 0;
    }

    @Override
    public float getAttackCooldown() {
        return this.shadow$getCooldownPeriod();
    }

    /**
     * TODO
     */
    @Override
    public boolean discoverRecipe(NamespacedKey recipe) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public int discoverRecipes(Collection<NamespacedKey> recipes) {
        return 0;
    }

    /**
     * TODO
     */
    @Override
    public boolean undiscoverRecipe(NamespacedKey recipe) {
        return false;
    }

    /**
     * TODO
     */
    @Override
    public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
        return 0;
    }

    @Override
    public Entity getShoulderEntityLeft() {
        CompoundNBT leftNbt = this.shadow$getLeftShoulderEntity();

        if (!leftNbt.isEmpty()) {
            Optional<net.minecraft.entity.Entity> entity = net.minecraft.entity.EntityType.loadEntityUnchecked(leftNbt, this.shadow$getServerWorld());

            return (Entity) entity.orElse(null);
        }

        return null;
    }

    @Override
    public Entity getShoulderEntityRight() {
        CompoundNBT rightNbt = this.shadow$getRightShoulderEntity();

        if (!rightNbt.isEmpty()) {
            Optional<net.minecraft.entity.Entity> entity = net.minecraft.entity.EntityType.loadEntityUnchecked(rightNbt, this.shadow$getServerWorld());

            return (Entity) entity.orElse(null);
        }

        return null;
    }

    /**
     * TODO
     */
    @Override
    public void setShoulderEntityLeft(Entity entity) {

    }

    /**
     * TODO
     */
    @Override
    public void setShoulderEntityRight(Entity entity) {

    }
}