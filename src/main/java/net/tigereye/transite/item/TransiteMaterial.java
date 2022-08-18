package net.tigereye.transite.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.tigereye.transite.Transite;
import net.tigereye.transite.register.TransiteItems;

public class TransiteMaterial implements ToolMaterial{

    @Override
    public int getDurability() {
        return 1015; //sub-diamond durability
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 8.0f; //diamond mining speed
    }

    @Override
    public float getAttackDamage() {
        return 4.0f; //netherite damage
    }

    @Override
    public int getMiningLevel() {
        return 3; //diamond tier
    }

    @Override
    public int getEnchantability() {
        return 14; //iron enchantability
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(TransiteItems.TRANSITE_INGOT);
    }
    
}