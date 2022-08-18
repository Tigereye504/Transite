package net.tigereye.transite.register;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.tigereye.transite.Transite;
import net.tigereye.transite.item.TransiteTool;
import org.jetbrains.annotations.Nullable;

public class TransiteModelPredicates {


    public static void register(){
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_AXE, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_HOE, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_PICKAXE, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_SHOVEL, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_SWORD, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_HELM, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_CHESTPLATE, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_LEGGINGS, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
        ModelPredicateProviderRegistry.register(TransiteItems.TRANSITE_BOOTS, new Identifier("flex"), TransiteModelPredicates::flexPredicateProvider);
    }

    public static float flexPredicateProvider(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        float flex = 0;
        NbtCompound tag = stack.getOrCreateNbt();
        if (tag.contains(TransiteTool.TRANSITE_FLEX_KEY)) {
            flex = tag.getFloat(TransiteTool.TRANSITE_FLEX_KEY);
        }
        return flex;
    }
}
