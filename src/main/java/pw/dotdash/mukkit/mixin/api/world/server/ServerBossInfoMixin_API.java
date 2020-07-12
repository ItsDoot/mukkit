package pw.dotdash.mukkit.mixin.api.world.server;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.spongepowered.asm.mixin.*;
import pw.dotdash.mukkit.bridge.boss.BarColorBridge;
import pw.dotdash.mukkit.bridge.boss.BarStyleBridge;
import pw.dotdash.mukkit.mixin.api.world.BossInfoMixin_API;
import pw.dotdash.mukkit.util.BarFlagMapper;
import pw.dotdash.mukkit.util.TypeConversions;

import java.util.Collection;
import java.util.List;

@Mixin(ServerBossInfo.class)
@Implements(@Interface(iface = BossBar.class, prefix = "api$"))
public abstract class ServerBossInfoMixin_API extends BossInfoMixin_API implements BossBar {

    // --- Shadowed Methods ---

    @Shadow
    public abstract void shadow$setName(ITextComponent nameIn);

    @Shadow
    public abstract void shadow$setColor(BossInfo.Color colorIn);

    @Shadow
    public abstract void shadow$setOverlay(BossInfo.Overlay overlayIn);

    @Shadow
    public abstract boolean shadow$isVisible();

    @Shadow
    public abstract void shadow$setVisible(boolean visibleIn);

    @Shadow
    public abstract void shadow$setPercent(float percentIn);

    @Shadow
    public abstract Collection<ServerPlayerEntity> shadow$getPlayers();

    @Shadow
    public abstract void shadow$addPlayer(ServerPlayerEntity player);

    @Shadow
    public abstract void shadow$removePlayer(ServerPlayerEntity player);

    @Shadow
    public abstract void shadow$removeAllPlayers();

    // --- BossBar Implementation ---

    @Override
    public String getTitle() {
        return this.shadow$getName().getString();
    }

    @Override
    public void setTitle(String title) {
        this.shadow$setName(new StringTextComponent(title == null ? "" : title));
    }

    @Override
    public BarColor getColor() {
        return ((BarColorBridge) (Object) this.shadow$getColor()).bridge$toBukkit();
    }

    @Override
    public void setColor(BarColor color) {
        Preconditions.checkNotNull(color, "color");

        this.shadow$setColor(((BarColorBridge) (Object) color).bridge$toMojang());
    }

    @Override
    public BarStyle getStyle() {
        return ((BarStyleBridge) (Object) this.shadow$getOverlay()).bridge$toBukkit();
    }

    @Override
    public void setStyle(BarStyle style) {
        Preconditions.checkNotNull(style, "style");

        this.shadow$setOverlay(((BarStyleBridge) (Object) style).bridge$toMojang());
    }

    @Override
    public void removeFlag(BarFlag flag) {
        BarFlagMapper.get(flag).set((ServerBossInfo) (Object) this, false);
    }

    @Override
    public void addFlag(BarFlag flag) {
        BarFlagMapper.get(flag).set((ServerBossInfo) (Object) this, true);
    }

    @Override
    public boolean hasFlag(BarFlag flag) {
        return BarFlagMapper.get(flag).get((ServerBossInfo) (Object) this);
    }

    @Override
    public void setProgress(double progress) {
        this.shadow$setPercent((float) progress);
    }

    @Override
    public double getProgress() {
        return this.shadow$getPercent();
    }

    @Override
    public void addPlayer(Player player) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(((ServerPlayerEntity) player).connection, "player.connection");

        this.shadow$addPlayer((ServerPlayerEntity) player);
    }

    @Override
    public void removePlayer(Player player) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(((ServerPlayerEntity) player).connection, "player.connection");

        this.shadow$removePlayer((ServerPlayerEntity) player);
    }

    @Override
    public void removeAll() {
        this.shadow$removeAllPlayers();
    }

    @Override
    public List<Player> getPlayers() {
        ImmutableList.Builder<Player> players = ImmutableList.builder();

        for (ServerPlayerEntity player : this.shadow$getPlayers()) {
            players.add((Player) player);
        }

        return players.build();
    }

    @Intrinsic
    public void api$setVisible(boolean visible) {
        this.shadow$setVisible(visible);
    }

    @Intrinsic
    public boolean api$isVisible() {
        return this.shadow$isVisible();
    }

    @Override
    public void show() {
        this.api$setVisible(true);
    }

    @Override
    public void hide() {
        this.api$setVisible(false);
    }
}