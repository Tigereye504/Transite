package net.tigereye.transite.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.tigereye.transite.register.TransiteItems;


public class TransiteArmorMaterial implements ArmorMaterial {
    private static final int[] BASE_DURABILITY = new int[] { 13, 15, 16, 11 };
    private static final int[] PROTECTION_AMOUNTS = new int[] { 2, 5, 6, 2 };

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()]*33;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return PROTECTION_AMOUNTS[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 12;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(TransiteItems.TRANSITE_INGOT);
    }

    @Override
    public String getName() {
        return "transite";
    }

    @Override
    public float getToughness() {
        return 2;
    }

    @Override
    public float getKnockbackResistance() {
        return .2f;
    }
}