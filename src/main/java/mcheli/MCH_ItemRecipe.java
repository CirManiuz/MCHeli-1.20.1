/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.crafting.IRecipe
 *  net.minecraft.item.crafting.Ingredient
 *  net.minecraft.item.crafting.ShapedRecipes
 *  net.minecraft.item.crafting.ShapelessRecipes
 *  net.minecraft.util.NonNullList
 *  net.minecraftforge.registries.IForgeRegistry
 */
package mcheli;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_IRecipeList;
import mcheli.MCH_MOD;
import mcheli.MCH_RecipeFuel;
import mcheli.MCH_RecipeReloadRangeFinder;
import mcheli.__helper.MCH_Recipes;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.info.ContentRegistries;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_AircraftInfoManager;
import mcheli.helicopter.MCH_HeliInfo;
import mcheli.helicopter.MCH_HeliInfoManager;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.tank.MCH_TankInfo;
import mcheli.tank.MCH_TankInfoManager;
import mcheli.throwable.MCH_ThrowableInfo;
import mcheli.vehicle.MCH_VehicleInfo;
import mcheli.vehicle.MCH_VehicleInfoManager;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Item;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.IForgeRegistry;

public class MCH_ItemRecipe
implements MCH_IRecipeList {
    private static final MCH_ItemRecipe instance = new MCH_ItemRecipe();
    private static List<IRecipe> commonItemRecipe = new ArrayList<IRecipe>();

    public static MCH_ItemRecipe getInstance() {
        return instance;
    }

    @Override
    public int getRecipeListSize() {
        return commonItemRecipe.size();
    }

    @Override
    public IRecipe getRecipe(int index) {
        return commonItemRecipe.get(index);
    }

    private static void addRecipeList(IRecipe recipe) {
        if (recipe != null) {
            commonItemRecipe.add(recipe);
        }
    }

    private static void registerCommonItemRecipe(IForgeRegistry<IRecipe> registry) {
        commonItemRecipe.clear();
        MCH_Recipes.register("charge_fuel", new MCH_RecipeFuel());
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fuel")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("gltd")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("chain")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("parachute")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("container")));
        for (int i = 0; i < MCH_MOD.itemUavStation.length; ++i) {
            MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("uav_station" + (i > 0 ? "" + (i + 1) : ""))));
        }
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("wrench")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("range_finder")));
        MCH_Recipes.register("charge_power_range_finder", new MCH_RecipeReloadRangeFinder());
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fim92")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fim92_bullet")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fgm148")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("fgm148_bullet")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("spawn_gunner_vs_monster")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("spawn_gunner_vs_player")));
        MCH_ItemRecipe.addRecipeList((IRecipe)registry.getValue(MCH_Utils.suffix("drafting_table")));
    }

    public static void registerItemRecipe(IForgeRegistry<IRecipe> registry) {
        MCH_ItemRecipe.registerCommonItemRecipe(registry);
        for (MCH_HeliInfo mCH_HeliInfo : ContentRegistries.heli().values()) {
            MCH_ItemRecipe.addRecipeAndRegisterList(mCH_HeliInfo, mCH_HeliInfo.item, MCH_HeliInfoManager.getInstance());
        }
        for (MCP_PlaneInfo mCP_PlaneInfo : ContentRegistries.plane().values()) {
            MCH_ItemRecipe.addRecipeAndRegisterList(mCP_PlaneInfo, mCP_PlaneInfo.item, MCP_PlaneInfoManager.getInstance());
        }
        for (MCH_TankInfo mCH_TankInfo : ContentRegistries.tank().values()) {
            MCH_ItemRecipe.addRecipeAndRegisterList(mCH_TankInfo, mCH_TankInfo.item, MCH_TankInfoManager.getInstance());
        }
        for (MCH_VehicleInfo mCH_VehicleInfo : ContentRegistries.vehicle().values()) {
            MCH_ItemRecipe.addRecipeAndRegisterList(mCH_VehicleInfo, mCH_VehicleInfo.item, MCH_VehicleInfoManager.getInstance());
        }
        for (MCH_ThrowableInfo mCH_ThrowableInfo : ContentRegistries.throwable().values()) {
            for (String s : mCH_ThrowableInfo.recipeString) {
                IRecipe recipe;
                if (s.length() < 3 || (recipe = MCH_ItemRecipe.addRecipe(mCH_ThrowableInfo.name, mCH_ThrowableInfo.item, s, mCH_ThrowableInfo.isShapedRecipe)) == null) continue;
                mCH_ThrowableInfo.recipe.add(recipe);
                MCH_ItemRecipe.addRecipeList(recipe);
            }
            mCH_ThrowableInfo.recipeString = null;
        }
    }

    private static <T extends MCH_AircraftInfo> void addRecipeAndRegisterList(MCH_AircraftInfo info, Item item, MCH_AircraftInfoManager<T> im) {
        int count = 0;
        for (String s : info.recipeString) {
            IRecipe recipe;
            ++count;
            if (s.length() < 3 || (recipe = MCH_ItemRecipe.addRecipe(info.name, item, s, info.isShapedRecipe)) == null) continue;
            info.recipe.add(recipe);
            im.addRecipe(recipe, count, info.name, s);
        }
        info.recipeString = null;
    }

    public static IRecipe addRecipe(String name, Item item, String data) {
        return MCH_ItemRecipe.addShapedRecipe(name, item, data);
    }

    @Nullable
    public static IRecipe addRecipe(String name, Item item, String data, boolean isShaped) {
        if (isShaped) {
            return MCH_ItemRecipe.addShapedRecipe(name, item, data);
        }
        return MCH_ItemRecipe.addShapelessRecipe(name, item, data);
    }

    @Nullable
    public static IRecipe addShapedRecipe(String name, Item item, String data) {
        ShapedRecipes r;
        ArrayList<Object> rcp = new ArrayList<Object>();
        String[] s = data.split("\\s*,\\s*");
        if (s.length < 3) {
            return null;
        }
        int start = 0;
        int createNum = 1;
        if (MCH_ItemRecipe.isNumber(s[0])) {
            start = 1;
            createNum = Integer.valueOf(s[0]);
            if (createNum <= 0) {
                createNum = 1;
            }
        }
        HashSet needShortChars = Sets.newHashSet();
        int idx = start;
        for (int i = start; i < 3 + start; ++i) {
            if (s[idx].length() <= 0 || s[idx].charAt(0) != '\"' || s[idx].charAt(s[idx].length() - 1) != '\"') continue;
            String ingredientStr = s[idx].substring(1, s[idx].length() - 1);
            ingredientStr.toUpperCase().chars().forEach(needShortChars::add);
            rcp.add(s[idx].subSequence(1, s[idx].length() - 1));
            ++idx;
        }
        if (idx == 0) {
            return null;
        }
        boolean isChar = true;
        boolean flag = false;
        while (idx < s.length) {
            if (s[idx].length() <= 0) {
                return null;
            }
            if (isChar) {
                if (s[idx].length() != 1) {
                    return null;
                }
                char c = s[idx].toUpperCase().charAt(0);
                if (c < 'A' || c > 'Z') {
                    return null;
                }
                if (!needShortChars.contains(c)) {
                    MCH_Utils.logger().warn("Key defines symbols that aren't used in pattern: [" + c + "], item:" + name);
                    flag = true;
                }
                if (!flag) {
                    rcp.add(Character.valueOf(c));
                }
            } else {
                if (!flag) {
                    String nm = s[idx].trim().toLowerCase();
                    int dmg = 0;
                    if (idx + 1 < s.length && MCH_ItemRecipe.isNumber(s[idx + 1])) {
                        dmg = Integer.parseInt(s[++idx]);
                    }
                    if (MCH_ItemRecipe.isNumber(nm)) {
                        return null;
                    }
                    rcp.add(new ItemStack(W_Item.getItemByName(nm), 1, dmg));
                }
                flag = false;
            }
            isChar = !isChar;
            ++idx;
        }
        Object[] recipe = new Object[rcp.size()];
        for (int i = 0; i < recipe.length; ++i) {
            recipe[i] = rcp.get(i);
        }
        try {
            r = MCH_Recipes.addShapedRecipe(name, new ItemStack(item, createNum), recipe);
        }
        catch (Exception e) {
            MCH_Utils.logger().warn(e.getMessage() + ", name:" + name);
            return null;
        }
        for (int i = 0; i < r.field_77574_d.size(); ++i) {
            if (r.field_77574_d.get(i) == Ingredient.field_193370_a || !Arrays.stream(((Ingredient)r.field_77574_d.get(i)).func_193365_a()).anyMatch(stack -> stack.func_77973_b() == null)) continue;
            throw new RuntimeException("Error: Invalid ShapedRecipes! " + item + " : " + data);
        }
        return r;
    }

    @Nullable
    public static IRecipe addShapelessRecipe(String name, Item item, String data) {
        ArrayList<ItemStack> rcp = new ArrayList<ItemStack>();
        String[] s = data.split("\\s*,\\s*");
        if (s.length < 1) {
            return null;
        }
        int start = 0;
        int createNum = 1;
        if (MCH_ItemRecipe.isNumber(s[0]) && createNum <= 0) {
            createNum = 1;
        }
        for (int idx = start; idx < s.length; ++idx) {
            if (s[idx].length() <= 0) {
                return null;
            }
            String nm = s[idx].trim().toLowerCase();
            int dmg = 0;
            if (idx + 1 < s.length && MCH_ItemRecipe.isNumber(s[idx + 1])) {
                dmg = Integer.parseInt(s[++idx]);
            }
            if (MCH_ItemRecipe.isNumber(nm)) {
                int n = Integer.parseInt(nm);
                if (n <= 255) {
                    rcp.add(new ItemStack(W_Block.getBlockById(n), 1, dmg));
                    continue;
                }
                if (n <= 511) {
                    rcp.add(new ItemStack(W_Item.getItemById(n), 1, dmg));
                    continue;
                }
                if (n <= 2255) {
                    rcp.add(new ItemStack(W_Block.getBlockById(n), 1, dmg));
                    continue;
                }
                if (n <= 2267) {
                    rcp.add(new ItemStack(W_Item.getItemById(n), 1, dmg));
                    continue;
                }
                if (n <= 4095) {
                    rcp.add(new ItemStack(W_Block.getBlockById(n), 1, dmg));
                    continue;
                }
                if (n > 31999) continue;
                rcp.add(new ItemStack(W_Item.getItemById(n + 256), 1, dmg));
                continue;
            }
            rcp.add(new ItemStack(W_Item.getItemByName(nm), 1, dmg));
        }
        Object[] recipe = new Object[rcp.size()];
        for (int i = 0; i < recipe.length; ++i) {
            recipe[i] = rcp.get(i);
        }
        ShapelessRecipes r = MCH_ItemRecipe.getShapelessRecipe(new ItemStack(item, createNum), recipe);
        for (int i = 0; i < r.field_77579_b.size(); ++i) {
            Ingredient ingredient = (Ingredient)r.field_77579_b.get(i);
            if (!Arrays.stream(ingredient.func_193365_a()).anyMatch(stack -> stack.func_77973_b() == null)) continue;
            throw new RuntimeException("Error: Invalid ShapelessRecipes! " + item + " : " + data);
        }
        MCH_Recipes.register(name, (IRecipe)r);
        return r;
    }

    public static ShapelessRecipes getShapelessRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj) {
        NonNullList list = NonNullList.func_191196_a();
        Object[] aobject = par2ArrayOfObj;
        int i = par2ArrayOfObj.length;
        for (int j = 0; j < i; ++j) {
            Object object1 = aobject[j];
            if (object1 instanceof ItemStack) {
                list.add((Object)Ingredient.func_193369_a((ItemStack[])new ItemStack[]{((ItemStack)object1).func_77946_l()}));
                continue;
            }
            if (object1 instanceof Item) {
                list.add((Object)Ingredient.func_193369_a((ItemStack[])new ItemStack[]{new ItemStack((Item)object1)}));
                continue;
            }
            if (!(object1 instanceof Block)) {
                throw new RuntimeException("Invalid shapeless recipy!");
            }
            list.add((Object)Ingredient.func_193369_a((ItemStack[])new ItemStack[]{new ItemStack((Block)object1)}));
        }
        return new ShapelessRecipes("", par1ItemStack, list);
    }

    public static boolean isNumber(@Nullable String s) {
        byte[] buf;
        if (s == null || s.isEmpty()) {
            return false;
        }
        for (byte b : buf = s.getBytes()) {
            if (b >= 48 && b <= 57) continue;
            return false;
        }
        return true;
    }
}

