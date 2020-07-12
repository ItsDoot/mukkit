package pw.dotdash.mukkit.mixin.api.inventory;

import net.minecraft.inventory.IInventory;
import org.bukkit.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IInventory.class)
public interface IInventoryMixin_API extends Inventory {

    // --- Shadowed Methods ---

    @Shadow
    int shadow$getSizeInventory();

    @Shadow
    int shadow$getInventoryStackLimit();

    // --- Inventory Implementation ---

    @Override
    default int getSize() {
        return this.shadow$getSizeInventory();
    }

    @Override
    default int getMaxStackSize() {
        return this.shadow$getInventoryStackLimit();
    }

    /**
     * TODO
     */
    @Override
    default void setMaxStackSize(int size) {

    }


}