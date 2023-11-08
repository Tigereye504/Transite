package net.tigereye.transite.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;
import net.tigereye.transite.Transite;
import net.tigereye.transite.register.TransiteItems;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TransiteTool extends MiningToolItem {
    public static final String TRANSITE_FLEX_KEY = Transite.MODID+"Flex";
    public static final String TRANSITE_ASSUMED_TOOL_KEY = Transite.MODID+"AssumedTool";
    public static final String TRANSITE_LAST_TRANSITION_KEY = Transite.MODID+"LastTransition";
    public static final String TRANSITE_IS_WAXED = Transite.MODID+"IsWaxed";
    private static final List<Item> TRANSITION_OPTIONS = new ArrayList<>();
    private static final float MIN_FLEX = 0;
    private static final float MAX_FLEX = .8f;
    protected static final UUID ASSUMED_TOOL_ATTACK_DAMAGE_MODIFIER_ID = UUID.fromString("686ad534-1cf9-11ed-861d-0242ac120002");
    protected static final UUID ASSUMED_TOOL_ATTACK_SPEED_MODIFIER_ID = UUID.fromString("6e4f93b8-1cf9-11ed-861d-0242ac120002");

    public TransiteTool(float attackDamage, float attackSpeed, ToolMaterial material, TagKey<Block> effectiveBlocks, Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
        TRANSITION_OPTIONS.add(this);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if(nbt.contains(TRANSITE_ASSUMED_TOOL_KEY)){
            float flex = 0;
            float assumedItemSpeed = 0;
            flex = nbt.getFloat(TRANSITE_FLEX_KEY);
            assumedItemSpeed = ((TransiteTool)(Registries.ITEM.get(new Identifier(nbt.getString(TRANSITE_ASSUMED_TOOL_KEY))))).getUnflexedMiningSpeedMultiplier(stack,state);
            return (getUnflexedMiningSpeedMultiplier(stack,state)*(1-flex)) + assumedItemSpeed;
        }
        else{
            return getUnflexedMiningSpeedMultiplier(stack,state);
        }
    }

    public float getUnflexedMiningSpeedMultiplier(ItemStack stack, BlockState state){
        if(stack.getItem() == TransiteItems.TRANSITE_SWORD){
            return Items.IRON_SWORD.getMiningSpeedMultiplier(stack,state);
        }
        return super.getMiningSpeedMultiplier(stack,state);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult result = ActionResult.PASS;
        Item item = context.getStack().getItem();
        NbtCompound nbt = context.getStack().getOrCreateNbt();
        Item assumedItem = null;
        if(nbt.contains(TRANSITE_ASSUMED_TOOL_KEY)){
            assumedItem = Registries.ITEM.get(new Identifier(nbt.getString(TRANSITE_ASSUMED_TOOL_KEY)));
        }
        
        if(item == TransiteItems.TRANSITE_AXE || assumedItem == TransiteItems.TRANSITE_AXE) {
            result = Items.IRON_AXE.useOnBlock(context);
        }
        if(result == ActionResult.PASS
        && (item == TransiteItems.TRANSITE_SHOVEL || assumedItem == TransiteItems.TRANSITE_SHOVEL)){
            result = Items.IRON_SHOVEL.useOnBlock(context);
        }
        if(result == ActionResult.PASS
        && (item == TransiteItems.TRANSITE_HOE || assumedItem == TransiteItems.TRANSITE_HOE)){
            result = Items.IRON_HOE.useOnBlock(context);
        }
        if(result == ActionResult.PASS
        && (item == TransiteItems.TRANSITE_PICKAXE || assumedItem == TransiteItems.TRANSITE_PICKAXE)){
            result = Items.IRON_PICKAXE.useOnBlock(context);
        }
        if(result == ActionResult.PASS
        && (item == TransiteItems.TRANSITE_SWORD || assumedItem == TransiteItems.TRANSITE_SWORD)){
            result = Items.IRON_SWORD.useOnBlock(context);
        }
        return result;
    }

    public boolean isSword(ItemStack stack) {
        if(stack.getItem() == TransiteItems.TRANSITE_SWORD){
            return true;
        }
        else {
            NbtCompound nbt = stack.getOrCreateNbt();
            if (nbt.contains(TRANSITE_ASSUMED_TOOL_KEY)) {
                return Registries.ITEM.get(new Identifier(nbt.getString(TRANSITE_ASSUMED_TOOL_KEY))) == TransiteItems.TRANSITE_SWORD;
            }
            else{
                return false;
            }
        }
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
        if(!nbt.contains(TRANSITE_ASSUMED_TOOL_KEY)){
            Item itemToAssume;
            do {
                itemToAssume = TRANSITION_OPTIONS.get(world.getRandom().nextInt(TRANSITION_OPTIONS.size()));
                if (stack.getItem() != itemToAssume) {
                    nbt.putString(TRANSITE_ASSUMED_TOOL_KEY, Registries.ITEM.getId(itemToAssume).toString());
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
            nbt.remove(TRANSITE_ASSUMED_TOOL_KEY);
        }
        nbt.putFloat(TRANSITE_FLEX_KEY,flex);
        updateAttributeModifiers(stack,nbt);
    }

    private void updateAttributeModifiers(ItemStack stack, NbtCompound nbt){
        boolean resetAttributes = false;
        Item thisItem = stack.getItem();
        if(nbt.contains(TRANSITE_ASSUMED_TOOL_KEY)) {
            Item assumedItem = Registries.ITEM.get(new Identifier(nbt.getString(TRANSITE_ASSUMED_TOOL_KEY)));
            if(assumedItem instanceof MiningToolItem aItem && thisItem instanceof MiningToolItem tItem){
                float flex = nbt.getFloat(TRANSITE_FLEX_KEY);
                double attackModifier = (aItem.getAttackDamage()*flex) + (tItem.getAttackDamage()*(1-flex));

                AtomicReference<Double> thisSpeedModifier = new AtomicReference<>((double) 0);
                tItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_SPEED).forEach(entityAttributeModifier -> {
                    if(entityAttributeModifier.getId() == Item.ATTACK_SPEED_MODIFIER_ID){
                        thisSpeedModifier.set(entityAttributeModifier.getValue());
                    }
                });
                AtomicReference<Double> assumedSpeedModifier = new AtomicReference<>((double) 0);
                aItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_SPEED).forEach(entityAttributeModifier -> {
                    if(entityAttributeModifier.getId() == Item.ATTACK_SPEED_MODIFIER_ID){
                        assumedSpeedModifier.set(entityAttributeModifier.getValue());
                    }
                });

                double speedModifier = (assumedSpeedModifier.get()*flex)+(thisSpeedModifier.get()*(1-flex));

                replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(ASSUMED_TOOL_ATTACK_DAMAGE_MODIFIER_ID,"AssumedToolDamageModifier",
                                attackModifier, EntityAttributeModifier.Operation.ADDITION),
                        EquipmentSlot.MAINHAND);
                replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(ASSUMED_TOOL_ATTACK_SPEED_MODIFIER_ID,"AssumedToolSpeedModifier",
                                speedModifier, EntityAttributeModifier.Operation.ADDITION),
                        EquipmentSlot.MAINHAND);
            }
            else resetAttributes = true;
        }
        else resetAttributes = true;

        if(resetAttributes && thisItem instanceof MiningToolItem tItem){
            double attackModifier = tItem.getAttackDamage();

            AtomicReference<Double> thisSpeedModifier = new AtomicReference<>((double) 0);
            tItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_SPEED).forEach(entityAttributeModifier -> {
                if(entityAttributeModifier.getId() == Item.ATTACK_SPEED_MODIFIER_ID){
                    thisSpeedModifier.set(entityAttributeModifier.getValue());
                }
            });

            replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(ASSUMED_TOOL_ATTACK_DAMAGE_MODIFIER_ID,"AssumedToolDamageModifier",
                            attackModifier, EntityAttributeModifier.Operation.ADDITION),
                    EquipmentSlot.MAINHAND);
            replaceAttributeModifier(nbt,EntityAttributes.GENERIC_ATTACK_SPEED,
                    new EntityAttributeModifier(ASSUMED_TOOL_ATTACK_SPEED_MODIFIER_ID,"AssumedToolSpeedModifier",
                            thisSpeedModifier.get(), EntityAttributeModifier.Operation.ADDITION),
                    EquipmentSlot.MAINHAND);
        }
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
        if(nbt.contains(TRANSITE_ASSUMED_TOOL_KEY)){
            return Text.translatable(Registries.ITEM.get(new Identifier(nbt.getString(TRANSITE_ASSUMED_TOOL_KEY))).getTranslationKey());
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
