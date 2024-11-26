/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.item.crafting.IRecipe
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mcheli.MCH_IRecipeList;
import mcheli.MCH_ItemRecipe;
import mcheli.__helper.MCH_Utils;
import mcheli.helicopter.MCH_HeliInfoManager;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.tank.MCH_TankInfoManager;
import mcheli.vehicle.MCH_VehicleInfoManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class RecipeDescriptionManager {
    private static final Map<ResourceLocation, DescriptionInfo> INFO_TABLE = Maps.newHashMap();

    public static void registerDescriptionInfos(IResourceManager resourceManager) {
        INFO_TABLE.clear();
        RecipeDescriptionManager.registerDescriptions(resourceManager, MCH_ItemRecipe.getInstance());
        RecipeDescriptionManager.registerDescriptions(resourceManager, MCH_HeliInfoManager.getInstance());
        RecipeDescriptionManager.registerDescriptions(resourceManager, MCP_PlaneInfoManager.getInstance());
        RecipeDescriptionManager.registerDescriptions(resourceManager, MCH_TankInfoManager.getInstance());
        RecipeDescriptionManager.registerDescriptions(resourceManager, MCH_VehicleInfoManager.getInstance());
    }

    private static void registerDescriptions(IResourceManager resourceManager, MCH_IRecipeList recipeList) {
        for (int i = 0; i < recipeList.getRecipeListSize(); ++i) {
            IRecipe recipe = recipeList.getRecipe(i);
            DescriptionInfo info = RecipeDescriptionManager.createDescriptionInfo(resourceManager, recipe);
            ResourceLocation registryName = recipe.getRegistryName();
            INFO_TABLE.put(registryName, info);
        }
    }

    private static DescriptionInfo createDescriptionInfo(IResourceManager resourceManager, IRecipe recipe) {
        LinkedList textures = Lists.newLinkedList();
        for (int i = 0; i < 20; ++i) {
            String itemName = recipe.func_77571_b().func_77977_a();
            if (itemName.startsWith("tile.")) {
                itemName = itemName.substring(5);
            }
            if (itemName.indexOf(":") >= 0) {
                itemName = itemName.substring(itemName.indexOf(":") + 1);
            }
            String filepath = "textures/drafting_table_desc/" + itemName + "#" + i + ".png";
            try (IResource resource2 = resourceManager.func_110536_a(MCH_Utils.suffix(filepath));){
                textures.add(resource2.func_177241_a());
                continue;
            }
            catch (FileNotFoundException resource2) {
                continue;
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return new DescriptionInfo(textures);
    }

    public static ImmutableList<ResourceLocation> getDescriptionTextures(ResourceLocation recipeRegistryName) {
        return INFO_TABLE.getOrDefault(recipeRegistryName, new DescriptionInfo(Collections.emptyList())).getTextures();
    }

    static class DescriptionInfo {
        private ImmutableList<ResourceLocation> textures;

        public DescriptionInfo(List<ResourceLocation> textures) {
            this.textures = ImmutableList.copyOf(textures);
        }

        public ImmutableList<ResourceLocation> getTextures() {
            return this.textures;
        }
    }
}

