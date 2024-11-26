/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.crafting.IRecipe
 *  net.minecraft.item.crafting.Ingredient
 *  net.minecraft.item.crafting.ShapedRecipes
 *  net.minecraftforge.common.crafting.CraftingHelper
 *  net.minecraftforge.common.crafting.CraftingHelper$ShapedPrimer
 *  net.minecraftforge.event.RegistryEvent$Register
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.registries.IForgeRegistry
 *  net.minecraftforge.registries.IForgeRegistryEntry
 */
package mcheli.__helper;

import com.google.common.collect.Sets;
import java.util.Set;
import mcheli.MCH_ItemRecipe;
import mcheli.__helper.MCH_Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid="mcheli")
public class MCH_Recipes {
    private static final Set<IRecipe> registryWrapper = Sets.newLinkedHashSet();

    @SubscribeEvent
    static void onRecipeRegisterEvent(RegistryEvent.Register<IRecipe> event) {
        MCH_ItemRecipe.registerItemRecipe((IForgeRegistry<IRecipe>)event.getRegistry());
        for (IRecipe recipe : registryWrapper) {
            event.getRegistry().register((IForgeRegistryEntry)recipe);
        }
    }

    public static void register(String name, IRecipe recipe) {
        registryWrapper.add((IRecipe)recipe.setRegistryName(MCH_Utils.suffix(name)));
    }

    public static ShapedRecipes addShapedRecipe(String name, ItemStack output, Object ... params) {
        CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped((Object[])params);
        ShapedRecipes recipe = new ShapedRecipes("", primer.width, primer.height, primer.input, output);
        MCH_Recipes.register(name, (IRecipe)recipe);
        return recipe;
    }

    public static boolean canCraft(EntityPlayer player, IRecipe recipe) {
        for (Ingredient ingredient : recipe.func_192400_c()) {
            if (ingredient == Ingredient.field_193370_a) continue;
            boolean flag = false;
            for (ItemStack itemstack : player.field_71071_by.field_70462_a) {
                if (!ingredient.apply(itemstack)) continue;
                flag = true;
                break;
            }
            if (flag) continue;
            return false;
        }
        return true;
    }

    public static boolean consumeInventory(EntityPlayer player, IRecipe recipe) {
        for (Ingredient ingredient : recipe.func_192400_c()) {
            if (ingredient == Ingredient.field_193370_a) continue;
            int i = 0;
            boolean flag = false;
            for (ItemStack itemstack : player.field_71071_by.field_70462_a) {
                if (ingredient.apply(itemstack)) {
                    player.field_71071_by.func_70298_a(i, 1);
                    flag = true;
                    break;
                }
                ++i;
            }
            if (flag) continue;
            return false;
        }
        return true;
    }
}

