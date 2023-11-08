package net.tigereye.transite.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.tigereye.transite.register.TransiteItems;

import java.util.EnumMap;


public class TransiteArmorMaterial implements ArmorMaterial {

    private static final EnumMap<ArmorItem.Type, Integer> BASE_DURABILITY = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 13);
        map.put(ArmorItem.Type.LEGGINGS, 15);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 11);
    });
    private static final EnumMap<ArmorItem.Type, Integer> PROTECTION_AMOUNTS = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 2);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 6);
        map.put(ArmorItem.Type.HELMET, 2);
    });
    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY.get(type) * 33;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return PROTECTION_AMOUNTS.get(type);
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