package pw.dotdash.mukkit.mixin.accessor.network.play.server;

import net.minecraft.network.play.server.SPlayerListHeaderFooterPacket;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPlayerListHeaderFooterPacket.class)
public interface SPacketListHeaderFooterPacketAccessor {

    @Accessor("header")
    ITextComponent getHeader();

    @Accessor("footer")
    ITextComponent getFooter();

    @Accessor("header")
    void setHeader(ITextComponent header);

    @Accessor("footer")
    void setFooter(ITextComponent footer);
}