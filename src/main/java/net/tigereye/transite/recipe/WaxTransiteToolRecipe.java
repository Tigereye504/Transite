package net.tigereye.transite.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.tigereye.transite.item.TransiteArmor;
import net.tigereye.transite.item.TransiteTool;
import net.tigereye.transite.register.TransiteItems;

public class WaxTransiteToolRecipe extends SpecialCraftingRecipe {

    public WaxTransiteToolRecipe(Identifier id) {
        super(id);
    }

    public boolean matches(CraftingInventory craftingInventory, World world) {
        boolean foundTool = false;
        boolean foundWax = false;
        for(int i = 0; i < craftingInventory.getWidth(); ++i) {
            for(int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getStack(i + j * craftingInventory.getWidth());
                Item item = itemStack.getItem();
                if (item instanceof TransiteTool || item instanceof TransiteArmor) {
                    if(foundTool){
                        return false;
                    }
                    foundTool = true;
                }
                else if (item == Items.HONEYCOMB) {
                    if(foundWax){
                        return false;
                    }
                    foundWax = true;
                }
            }
        }
        return foundTool && foundWax;
    }

    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack tool = null;
        boolean foundWax = false;
        for(int i = 0; i < craftingInventory.getWidth(); ++i) {
            for(int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getStack(i + j * craftingInventory.getWidth());
                Item item = itemStack.getItem();
                if (item instanceof TransiteTool || item instanceof TransiteArmor) {
                    if(tool != null){
                        return null;
                    }
                    tool = itemStack;
                }
                else if (item == Items.HONEYCOMB) {
                    if(foundWax){
                        return null;
                    }
                    foundWax = true;
                }
            }
        }
        if(tool != null && foundWax) {
            ItemStack output = tool.copy();
            output.getOrCreateNbt().putBoolean(TransiteTool.TRANSITE_IS_WAXED, true);
            return output;
        }
        return null;
    }

    public boolean fits(int width, int height) {
        return (width*height >= 2);
    }

    public RecipeSerializer<?> getSerializer() {
        return TransiteItems.WAX_TRANSITE_TOOL_RECIPE;
    }
    
}
