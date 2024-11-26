/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.item.crafting.IRecipe
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.block;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_IRecipeList;
import mcheli.MCH_ModelManager;
import mcheli.__helper.client.RecipeDescriptionManager;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_AircraftInfoManager;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponInfoManager;
import mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class MCH_CurrentRecipe {
    public final IRecipe recipe;
    public final int index;
    public final String displayName;
    public final List<ResourceLocation> descTexture;
    private final MCH_AircraftInfo acInfo;
    public List<String> infoItem;
    public List<String> infoData;
    private int descMaxPage;
    private int descPage;
    private W_ModelCustom model;
    public int modelRot;
    private ResourceLocation modelTexture;

    public MCH_CurrentRecipe(MCH_IRecipeList list, int idx) {
        this.recipe = list.getRecipeListSize() > 0 ? list.getRecipe(idx) : null;
        this.index = idx;
        this.displayName = this.recipe != null ? this.recipe.func_77571_b().func_82833_r() : "None";
        this.descTexture = this.getDescTexture(this.recipe);
        this.descPage = 0;
        this.descMaxPage = this.descTexture.size();
        MCH_AircraftInfo info = null;
        if (list instanceof MCH_AircraftInfoManager && (info = ((MCH_AircraftInfoManager)list).getAcInfoFromItem(this.recipe)) != null) {
            ++this.descMaxPage;
            String dir = info.getDirectoryName();
            String name = info.name;
            this.model = MCH_ModelManager.get(dir, name);
            if (this.model != null) {
                this.modelTexture = new ResourceLocation("mcheli", "textures/" + dir + "/" + name + ".png");
                ++this.descMaxPage;
                this.modelRot = list instanceof MCP_PlaneInfoManager ? 0 : 1;
            }
        }
        this.getAcInfoText(info);
        this.acInfo = info;
    }

    private void getAcInfoText(MCH_AircraftInfo info) {
        this.infoItem = new ArrayList<String>();
        this.infoData = new ArrayList<String>();
        if (info == null) {
            return;
        }
        this.getAcInfoTextSub("Name", info.getItemStack().func_82833_r());
        this.getAcInfoTextSub("HP", "" + info.maxHp);
        int seatNum = !info.isUAV ? info.getNumSeat() : info.getNumSeat() - 1;
        this.getAcInfoTextSub("Num of Seat", "" + seatNum);
        this.getAcInfoTextSub("GunnerMode", info.isEnableGunnerMode ? "YES" : "NO");
        this.getAcInfoTextSub("NightVision", info.isEnableNightVision ? "YES" : "NO");
        this.getAcInfoTextSub("Radar", info.isEnableEntityRadar ? "YES" : "NO");
        this.getAcInfoTextSub("Inventory", "" + info.inventorySize);
        if (info instanceof MCP_PlaneInfo) {
            MCP_PlaneInfo pinfo = (MCP_PlaneInfo)info;
            this.getAcInfoTextSub("VTOL", pinfo.isEnableVtol ? "YES" : "NO");
        }
        if (info.getWeaponNum() > 0) {
            this.getAcInfoTextSub("Armed----------------");
            for (int i = 0; i < info.getWeaponNum(); ++i) {
                String type = info.getWeaponSetById((int)i).type;
                MCH_WeaponInfo winfo = MCH_WeaponInfoManager.get(type);
                if (winfo != null) {
                    this.getAcInfoTextSub(winfo.getWeaponTypeName(), winfo.displayName);
                    continue;
                }
                this.getAcInfoTextSub("ERROR", "Not found weapon " + (i + 1));
            }
        }
    }

    private void getAcInfoTextSub(String item, String data) {
        this.infoItem.add(item + " :");
        this.infoData.add(data);
    }

    private void getAcInfoTextSub(String item) {
        this.infoItem.add(item);
        this.infoData.add("");
    }

    public void switchNextPage() {
        this.descPage = this.descMaxPage >= 2 ? (this.descPage + 1) % this.descMaxPage : 0;
    }

    public void switchPrevPage() {
        --this.descPage;
        this.descPage = this.descPage < 0 && this.descMaxPage >= 2 ? this.descMaxPage - 1 : 0;
    }

    public int getDescCurrentPage() {
        return this.descPage;
    }

    public void setDescCurrentPage(int page) {
        this.descPage = this.descMaxPage > 0 ? (page < this.descMaxPage ? page : this.descMaxPage - 1) : 0;
    }

    public int getDescMaxPage() {
        return this.descMaxPage;
    }

    @Nullable
    public ResourceLocation getCurrentPageTexture() {
        if (this.descPage < this.descTexture.size()) {
            return this.descTexture.get(this.descPage);
        }
        return null;
    }

    public W_ModelCustom getModel() {
        return this.model;
    }

    public ResourceLocation getModelTexture() {
        return this.modelTexture;
    }

    @Nullable
    public MCH_AircraftInfo getAcInfo() {
        return this.acInfo;
    }

    public boolean isCurrentPageTexture() {
        return this.descPage >= 0 && this.descPage < this.descTexture.size();
    }

    public boolean isCurrentPageModel() {
        return this.getAcInfo() != null && this.getModel() != null && this.descPage == this.descTexture.size();
    }

    public boolean isCurrentPageAcInfo() {
        return this.getAcInfo() != null && this.descPage == this.descMaxPage - 1;
    }

    private List<ResourceLocation> getDescTexture(@Nullable IRecipe r) {
        return RecipeDescriptionManager.getDescriptionTextures(r.getRegistryName());
    }
}

