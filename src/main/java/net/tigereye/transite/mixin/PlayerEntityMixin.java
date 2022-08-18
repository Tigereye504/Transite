package net.tigereye.transite.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.tigereye.transite.item.TransiteTool;
import net.tigereye.transite.register.TransiteItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), ordinal = 3, method = "attack")
    public boolean attack(boolean b4, Entity target) {
        ItemStack stack = ((PlayerEntity)(Object)this).getStackInHand(Hand.MAIN_HAND);
        if(stack.getItem() instanceof TransiteTool tTool){
            return b4 || tTool.isSword(stack);
        }
        return b4;
    }
}
