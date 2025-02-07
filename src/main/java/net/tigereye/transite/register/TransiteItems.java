package net.tigereye.transite.register;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.tigereye.transite.Transite;
import net.tigereye.transite.item.TransiteArmor;
import net.tigereye.transite.item.TransiteArmorMaterial;
import net.tigereye.transite.item.TransiteMaterial;
import net.tigereye.transite.item.TransiteTool;
import net.tigereye.transite.recipe.WaxTransiteToolRecipe;

public class TransiteItems {

    public static final Block TRANSITE_ORE = new Block(FabricBlockSettings
            .copyOf(Blocks.STONE)
            .strength(3f,50f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE));
    public static final BlockItem TRANSITE_ORE_ITEM = new BlockItem(TRANSITE_ORE, new Item.Settings());
    public static final Block DEEPSLATE_TRANSITE_ORE = new Block(FabricBlockSettings
            .copyOf(Blocks.STONE)
            .strength(3f,50f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE));
    public static final BlockItem DEEPSLATE_TRANSITE_ORE_ITEM = new BlockItem(DEEPSLATE_TRANSITE_ORE, new Item.Settings());
    public static final Block RAW_TRANSITE_BLOCK = new Block(FabricBlockSettings
            .copyOf(Blocks.STONE)
            .strength(3f,50f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE));
    public static final BlockItem RAW_TRANSITE_BLOCK_ITEM = new BlockItem(RAW_TRANSITE_BLOCK, new Item.Settings());
    public static final Block TRANSITE_BLOCK = new Block(FabricBlockSettings
            .copyOf(Blocks.STONE)
            .strength(3f,50f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE));
    public static final BlockItem TRANSITE_BLOCK_ITEM = new BlockItem(TRANSITE_BLOCK, new Item.Settings());

    public static final Item TRANSITE_INGOT = new Item(new Item.Settings());
    public static final Item RAW_TRANSITE = new Item(new Item.Settings());

    public static final ToolMaterial TRANSITE_MATERIAL = new TransiteMaterial();
    public static final TransiteTool TRANSITE_PICKAXE = new TransiteTool(1,-2.8F,TRANSITE_MATERIAL,BlockTags.PICKAXE_MINEABLE,new Item.Settings());
    public static final TransiteTool TRANSITE_AXE = new TransiteTool(5,-3,TRANSITE_MATERIAL,BlockTags.AXE_MINEABLE,new Item.Settings());
    public static final TransiteTool TRANSITE_SHOVEL = new TransiteTool(1.5F,-3,TRANSITE_MATERIAL,BlockTags.SHOVEL_MINEABLE,new Item.Settings());
    public static final TransiteTool TRANSITE_HOE = new TransiteTool(-4,0,TRANSITE_MATERIAL,BlockTags.HOE_MINEABLE,new Item.Settings());
    public static final TransiteTool TRANSITE_SWORD = new TransiteTool(3,-2.4F,TRANSITE_MATERIAL,BlockTags.PORTALS,new Item.Settings());
    public static final ArmorMaterial TRANSITE_ARMOR_MATERIAL = new TransiteArmorMaterial();
    public static final TransiteArmor TRANSITE_HELM = new TransiteArmor(TRANSITE_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings());
    public static final TransiteArmor TRANSITE_CHESTPLATE = new TransiteArmor(TRANSITE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings());
    public static final TransiteArmor TRANSITE_LEGGINGS = new TransiteArmor(TRANSITE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings());
    public static final TransiteArmor TRANSITE_BOOTS = new TransiteArmor(TRANSITE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings());
    public static RecipeSerializer<WaxTransiteToolRecipe> WAX_TRANSITE_TOOL_RECIPE = Registry.register(Registries.RECIPE_SERIALIZER,
            new Identifier(Transite.MODID,"crafting_special_wax_transite_tool"),
            new SpecialRecipeSerializer<>(WaxTransiteToolRecipe::new));

    public static void register(){
        registerItem("transite_ingot", TRANSITE_INGOT);
        registerItem("raw_transite", RAW_TRANSITE);

        registerItem("transite_pickaxe", TRANSITE_PICKAXE);
        registerItem("transite_axe", TRANSITE_AXE);
        registerItem("transite_shovel", TRANSITE_SHOVEL);
        registerItem("transite_hoe", TRANSITE_HOE);
        registerItem("transite_sword", TRANSITE_SWORD);
        //Registry.register(Registries.RECIPE_SERIALIZER, Transite.MODID+":crafting_special_wax_transite_tool", WAX_TRANSITE_TOOL_RECIPE);

        registerItem("transite_helmet", TRANSITE_HELM);
        registerItem("transite_chestplate", TRANSITE_CHESTPLATE);
        registerItem("transite_leggings", TRANSITE_LEGGINGS);
        registerItem("transite_boots", TRANSITE_BOOTS);

        registerBlock("transite_ore", TRANSITE_ORE);
        registerItem("transite_ore",TRANSITE_ORE_ITEM);
        registerBlock("deepslate_transite_ore", DEEPSLATE_TRANSITE_ORE);
        registerItem("deepslate_transite_ore", DEEPSLATE_TRANSITE_ORE_ITEM);
        registerBlock("raw_transite_block", RAW_TRANSITE_BLOCK);
        registerItem("raw_transite_block", RAW_TRANSITE_BLOCK_ITEM);
        registerBlock("transite_block", TRANSITE_BLOCK);
        registerItem("transite_block", TRANSITE_BLOCK_ITEM);
        
        registerItemGroups();
    }

    private static void registerItem(String name, Item item) {
        Registry.register(Registries.ITEM, Transite.MODID + ":" + name, item);
    }

    private static void registerBlock(String name, Block block) {
        Registry.register(Registries.BLOCK, Transite.MODID + ":" + name, block);
    }
    private static void registerItemGroups(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(TRANSITE_ORE_ITEM);
            entries.add(RAW_TRANSITE_BLOCK_ITEM);
            entries.add(TRANSITE_BLOCK_ITEM);
            entries.add(DEEPSLATE_TRANSITE_ORE_ITEM);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(TRANSITE_PICKAXE);
            entries.add(TRANSITE_AXE);
            entries.add(TRANSITE_SHOVEL);
            entries.add(TRANSITE_HOE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(TRANSITE_SWORD);
            entries.add(TRANSITE_HELM);
            entries.add(TRANSITE_CHESTPLATE);
            entries.add(TRANSITE_LEGGINGS);
            entries.add(TRANSITE_BOOTS);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(TRANSITE_INGOT);
            entries.add(RAW_TRANSITE);
        });
    }
}
