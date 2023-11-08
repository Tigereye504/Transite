package net.tigereye.transite.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.tigereye.transite.Transite;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TransiteArmor extends ArmorItem {
    public static final String TRANSITE_FLEX_KEY = Transite.MODID+"Flex";
    public static final String TRANSITE_ASSUMED_ARMOR_KEY = Transite.MODID+"AssumedTool";
    public static final String TRANSITE_LAST_TRANSITION_KEY = Transite.MODID+"LastTransition";
    public static final String TRANSITE_IS_WAXED = Transite.MODID+"IsWaxed";
    private static final List<Item> TRANSITION_OPTIONS = new ArrayList<>();
    private static final float MIN_FLEX = 0;
    private static final float MAX_FLEX = .8f;

    private static final int[] TOUGHNESS_AMOUNTS = new int[] { 3, 0, 0, 5 };
    private static final float[] STABILITY_AMOUNTS = new float[] { .4f, .2f, 0, .1f };
    private static final UUID[] ASSUMED_ARMOR_PROTECTION_MODIFIER_IDS = new UUID[] {
            UUID.fromString("20e995f4-1db9-11ed-861d-0242ac120002"),
            UUID.fromString("20e995f4-1db9-11ed-861d-0242ac120003"),
            UUID.fromString("20e995f4-1db9-11ed-861d-0242ac120004"),
            UUID.fromString("20e995f4-1db9-11ed-861d-0242ac120005")};
    private static final UUID[] ASSUMED_ARMOR_TOUGHNESS_MODIFIER_IDS = new UUID[] {
            UUID.fromString("26f79234-1db9-11ed-861d-0242ac120002"),
            UUID.fromString("26f79234-1db9-11ed-861d-0242ac120003"),
            UUID.fromString("26f79234-1db9-11ed-861d-0242ac120004"),
            UUID.fromString("26f79234-1db9-11ed-861d-0242ac120005")};
    private static final UUID[] ASSUMED_ARMOR_STABILITY_MODIFIER_IDS = new UUID[] {
            UUID.fromString("cbb07c9e-1dbb-11ed-861d-0242ac120002"),
            UUID.fromString("cbb07c9e-1dbb-11ed-861d-0242ac120003"),
            UUID.fromString("cbb07c9e-1dbb-11ed-861d-0242ac120004"),
            UUID.fromString("cbb07c9e-1dbb-11ed-861d-0242ac120005")};

    public TransiteArmor(ArmorMaterial material, ArmorItem.Type slot, Settings settings) {
        super(material, slot, settings);
        TRANSITION_OPTIONS.add(this);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(selected && entity instanceof PlayerEntity && ((PlayerEntity) entity).handSwinging){
            return;
        }
        NbtCompound nbt = stack.getOrCreateNbt();
        if(!nbt.contains(TRANSITE_IS_WAXED)) {
            long time = nbt.getLong(TRANSITE_LAST_TRANSITION_KEY);
            if (world.getTime() - time > Transite.config.TRANSITION_FREQUENCY) {
                nbt.putLong(TRANSITE_LAST_TRANSITION_KEY, world.getTime());
                transition(stack, nbt, world);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void transition(ItemStack stack, NbtCompound nbt, World world){
        float flex = nbt.getFloat(TRANSITE_FLEX_KEY);
        if(flex >= MAX_FLEX) loseFlex(stack,nbt, flex);
        else if(flex <= MIN_FLEX) gainFlex(stack,nbt,world,flex);
        else {
            switch (world.getRandom().nextInt(2)) {
                case 0 -> gainFlex(stack, nbt, world, flex);
                case 1 -> loseFlex(stack, nbt, flex);
            }
        }
    }

    private void gainFlex(ItemStack stack, NbtCompound nbt, World world, float flex){
        flex = Math.min(MAX_FLEX,flex + .2F);
        if(!nbt.contains(TRANSITE_ASSUMED_ARMOR_KEY)){
            Item itemToAssume;
            do {
                itemToAssume = TRANSITION_OPTIONS.get(world.getRandom().nextInt(TRANSITION_OPTIONS.size()));
                if (stack.getItem() != itemToAssume) {
                    nbt.putString(TRANSITE_ASSUMED_ARMOR_KEY, Registries.ITEM.getId(itemToAssume).toString());
                }
            }while(stack.getItem() == itemToAssume);
        }
        nbt.putFloat(TRANSITE_FLEX_KEY,flex);
        updateAttributeModifiers(stack,nbt);
    }

    private void loseFlex(ItemStack stack, NbtCompound nbt, float flex){
        flex = Math.max(MIN_FLEX,flex - .2f);
        if(flex <= .1){
            flex = 0;
            nbt.remove(TRANSITE_ASSUMED_ARMOR_KEY);
        }
        nbt.putFloat(TRANSITE_FLEX_KEY,flex);
        updateAttributeModifiers(stack,nbt);
    }

    private void updateAttributeModifiers(ItemStack stack, NbtCompound nbt){
        boolean resetAttributes = false;
        Item thisItem = stack.getItem();
        if(nbt.contains(TRANSITE_ASSUMED_ARMOR_KEY)) {
            Item assumedItem = Registries.ITEM.get(new Identifier(nbt.getString(TRANSITE_ASSUMED_ARMOR_KEY)));
            if(assumedItem instanceof TransiteArmor aItem && thisItem instanceof TransiteArmor tItem){
                float flex = nbt.getFloat(TRANSITE_FLEX_KEY);
                double protectionModifier = (aItem.getProtection()*flex) + (tItem.getProtection()*(1-flex));
                double toughnessModifier = (aItem.getToughness(aItem.getSlotType())*flex) + (tItem.getToughness(tItem.getSlotType())*(1-flex));
                double knockbackResistanceModifier = (aItem.getKnockbackResistance(aItem.getSlotType())*flex) + (tItem.getKnockbackResistance(tItem.getSlotType())*(1-flex));

                replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ARMOR,
                        new EntityAttributeModifier(ASSUMED_ARMOR_PROTECTION_MODIFIER_IDS[tItem.getSlotType().getEntitySlotId()],"AssumedArmorDefenseModifier",
                                protectionModifier, EntityAttributeModifier.Operation.ADDITION),
                        tItem.getSlotType());
                replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                        new EntityAttributeModifier(ASSUMED_ARMOR_TOUGHNESS_MODIFIER_IDS[tItem.getSlotType().getEntitySlotId()],"AssumedArmorToughnessModifier",
                                toughnessModifier, EntityAttributeModifier.Operation.ADDITION),
                        tItem.getSlotType());
                replaceAttributeModifier(nbt,EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                        new EntityAttributeModifier(ASSUMED_ARMOR_STABILITY_MODIFIER_IDS[tItem.getSlotType().getEntitySlotId()],"AssumedArmorStabilityModifier",
                                knockbackResistanceModifier, EntityAttributeModifier.Operation.ADDITION),
                        tItem.getSlotType());
            }
            else resetAttributes = true;
        }
        else resetAttributes = true;

        if(resetAttributes && thisItem instanceof TransiteArmor tItem){
            double protectionModifier = tItem.getProtection();
            double toughnessModifier = tItem.getToughness(tItem.getSlotType());
            double knockbackResistanceModifier = tItem.getKnockbackResistance(tItem.getSlotType());

            replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ARMOR,
                    new EntityAttributeModifier(ASSUMED_ARMOR_PROTECTION_MODIFIER_IDS[tItem.getSlotType().getEntitySlotId()],"AssumedArmorDefenseModifier",
                            protectionModifier, EntityAttributeModifier.Operation.ADDITION),
                    tItem.getSlotType());
            replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                    new EntityAttributeModifier(ASSUMED_ARMOR_TOUGHNESS_MODIFIER_IDS[tItem.getSlotType().getEntitySlotId()],"AssumedArmorToughnessModifier",
                            toughnessModifier, EntityAttributeModifier.Operation.ADDITION),
                    tItem.getSlotType());
            replaceAttributeModifier(nbt,EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                    new EntityAttributeModifier(ASSUMED_ARMOR_STABILITY_MODIFIER_IDS[tItem.getSlotType().getEntitySlotId()],"AssumedArmorStabilityModifier",
                            knockbackResistanceModifier, EntityAttributeModifier.Operation.ADDITION),
                    tItem.getSlotType());
        }
    }

    private float getKnockbackResistance(EquipmentSlot slotType) {
        return STABILITY_AMOUNTS[slotType.getEntitySlotId()];
    }

    private float getToughness(EquipmentSlot slotType) {
        return TOUGHNESS_AMOUNTS[slotType.getEntitySlotId()];
    }

    public void replaceAttributeModifier(NbtCompound nbt, EntityAttribute attribute, EntityAttributeModifier modifier, @Nullable EquipmentSlot slot) {

        if (!nbt.contains("AttributeModifiers", 9)) {
            nbt.put("AttributeModifiers", new NbtList());
        }

        NbtList nbtList = nbt.getList("AttributeModifiers", 10);

        Iterator<NbtElement> iter = nbtList.iterator();
        while(iter.hasNext()){
            NbtCompound element = (NbtCompound) iter.next();
            if(element.getUuid("UUID").compareTo(modifier.getId()) == 0) {
                iter.remove();
            }
        }

        NbtCompound nbtCompound = modifier.toNbt();
        nbtCompound.putString("AttributeName", Registries.ATTRIBUTE.getId(attribute).toString());
        if (slot != null) {
            nbtCompound.putString("Slot", slot.getName());
        }

        nbtList.add(nbtCompound);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey();
    }

    @Override
    public Text getName(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(nbt.contains(TRANSITE_ASSUMED_ARMOR_KEY)){
            return Text.translatable(Registries.ITEM.get(new Identifier(nbt.getString(TRANSITE_ASSUMED_ARMOR_KEY))).getTranslationKey());
        }
        else {
            return Text.translatable(this.getTranslationKey(stack));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(nbt.contains(TRANSITE_IS_WAXED)) {
            tooltip.add(Text.translatable("text.transite.waxed"));
        }
        super.appendTooltip(stack,world,tooltip,context);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return true;
    }
}
