/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$CullFace
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.crafting.IRecipe
 *  net.minecraft.item.crafting.Ingredient
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.input.Mouse
 */
package mcheli.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_IRecipeList;
import mcheli.MCH_ItemRecipe;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_Recipes;
import mcheli.__helper.client.renderer.GlUtil;
import mcheli.aircraft.MCH_RenderAircraft;
import mcheli.block.MCH_CurrentRecipe;
import mcheli.block.MCH_DraftingTableCreatePacket;
import mcheli.block.MCH_DraftingTableGuiContainer;
import mcheli.gui.MCH_GuiSliderVertical;
import mcheli.helicopter.MCH_HeliInfoManager;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.tank.MCH_TankInfoManager;
import mcheli.vehicle.MCH_VehicleInfoManager;
import mcheli.wrapper.W_GuiButton;
import mcheli.wrapper.W_GuiContainer;
import mcheli.wrapper.W_KeyBinding;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.modelloader.W_ModelCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class MCH_DraftingTableGui
extends W_GuiContainer {
    private final EntityPlayer thePlayer;
    private MCH_GuiSliderVertical listSlider;
    private GuiButton buttonCreate;
    private GuiButton buttonNext;
    private GuiButton buttonPrev;
    private GuiButton buttonNextPage;
    private GuiButton buttonPrevPage;
    private int drawFace;
    private int buttonClickWait;
    public static final int RECIPE_HELI = 0;
    public static final int RECIPE_PLANE = 1;
    public static final int RECIPE_VEHICLE = 2;
    public static final int RECIPE_TANK = 3;
    public static final int RECIPE_ITEM = 4;
    public MCH_IRecipeList currentList;
    public MCH_CurrentRecipe current;
    public static final int BUTTON_HELI = 10;
    public static final int BUTTON_PLANE = 11;
    public static final int BUTTON_VEHICLE = 12;
    public static final int BUTTON_TANK = 13;
    public static final int BUTTON_ITEM = 14;
    public static final int BUTTON_NEXT = 20;
    public static final int BUTTON_PREV = 21;
    public static final int BUTTON_CREATE = 30;
    public static final int BUTTON_SELECT = 40;
    public static final int BUTTON_NEXT_PAGE = 50;
    public static final int BUTTON_PREV_PAGE = 51;
    public List<List<GuiButton>> screenButtonList;
    public int screenId = 0;
    public static final int SCREEN_MAIN = 0;
    public static final int SCREEN_LIST = 1;
    public static float modelZoom = 1.0f;
    public static float modelRotX = 0.0f;
    public static float modelRotY = 0.0f;
    public static float modelPosX = 0.0f;
    public static float modelPosY = 0.0f;

    public MCH_DraftingTableGui(EntityPlayer player, int posX, int posY, int posZ) {
        super(new MCH_DraftingTableGuiContainer(player, posX, posY, posZ));
        this.thePlayer = player;
        this.field_146999_f = 400;
        this.field_147000_g = 240;
        this.screenButtonList = new ArrayList<List<GuiButton>>();
        this.drawFace = 0;
        this.buttonClickWait = 0;
        MCH_Lib.DbgLog(player.field_70170_p, "MCH_DraftingTableGui.MCH_DraftingTableGui", new Object[0]);
    }

    public void func_73866_w_() {
        super.func_73866_w_();
        this.field_146292_n.clear();
        this.screenButtonList.clear();
        this.screenButtonList.add(new ArrayList());
        this.screenButtonList.add(new ArrayList());
        List<GuiButton> list = this.screenButtonList.get(0);
        GuiButton btnHeli = new GuiButton(10, this.field_147003_i + 20, this.field_147009_r + 20, 90, 20, "Helicopter List");
        GuiButton btnPlane = new GuiButton(11, this.field_147003_i + 20, this.field_147009_r + 40, 90, 20, "Plane List");
        GuiButton btnVehicle = new GuiButton(12, this.field_147003_i + 20, this.field_147009_r + 60, 90, 20, "Vehicle List");
        GuiButton btnTank = new GuiButton(13, this.field_147003_i + 20, this.field_147009_r + 80, 90, 20, "Tank List");
        GuiButton btnItem = new GuiButton(14, this.field_147003_i + 20, this.field_147009_r + 100, 90, 20, "Item List");
        btnHeli.field_146124_l = MCH_HeliInfoManager.getInstance().getRecipeListSize() > 0;
        btnPlane.field_146124_l = MCP_PlaneInfoManager.getInstance().getRecipeListSize() > 0;
        btnVehicle.field_146124_l = MCH_VehicleInfoManager.getInstance().getRecipeListSize() > 0;
        btnTank.field_146124_l = MCH_TankInfoManager.getInstance().getRecipeListSize() > 0;
        btnItem.field_146124_l = MCH_ItemRecipe.getInstance().getRecipeListSize() > 0;
        list.add(btnHeli);
        list.add(btnPlane);
        list.add(btnVehicle);
        list.add(btnTank);
        list.add(btnItem);
        this.buttonCreate = new GuiButton(30, this.field_147003_i + 120, this.field_147009_r + 89, 50, 20, "Create");
        this.buttonPrev = new GuiButton(21, this.field_147003_i + 120, this.field_147009_r + 111, 36, 20, "<<");
        this.buttonNext = new GuiButton(20, this.field_147003_i + 155, this.field_147009_r + 111, 35, 20, ">>");
        list.add(this.buttonCreate);
        list.add(this.buttonPrev);
        list.add(this.buttonNext);
        this.buttonPrevPage = new GuiButton(51, this.field_147003_i + 210, this.field_147009_r + 210, 60, 20, "Prev Page");
        this.buttonNextPage = new GuiButton(50, this.field_147003_i + 270, this.field_147009_r + 210, 60, 20, "Next Page");
        list.add(this.buttonPrevPage);
        list.add(this.buttonNextPage);
        list = this.screenButtonList.get(1);
        int i = 0;
        for (int y = 0; y < 3; ++y) {
            int x = 0;
            while (x < 2) {
                int px = this.field_147003_i + 30 + x * 140;
                int py = this.field_147009_r + 40 + y * 70;
                list.add(new GuiButton(40 + i, px, py, 45, 20, "Select"));
                ++x;
                ++i;
            }
        }
        this.listSlider = new MCH_GuiSliderVertical(0, this.field_147003_i + 360, this.field_147009_r + 20, 20, 200, "", 0.0f, 0.0f, 0.0f, 1.0f);
        list.add(this.listSlider);
        for (i = 0; i < this.screenButtonList.size(); ++i) {
            list = this.screenButtonList.get(i);
            for (int j = 0; j < list.size(); ++j) {
                this.field_146292_n.add(list.get(j));
            }
        }
        this.switchScreen(0);
        MCH_DraftingTableGui.initModelTransform();
        modelRotX = 180.0f;
        modelRotY = 90.0f;
        if (MCH_ItemRecipe.getInstance().getRecipeListSize() > 0) {
            this.switchRecipeList(MCH_ItemRecipe.getInstance());
        } else if (MCH_HeliInfoManager.getInstance().getRecipeListSize() > 0) {
            this.switchRecipeList(MCH_HeliInfoManager.getInstance());
        } else if (MCP_PlaneInfoManager.getInstance().getRecipeListSize() > 0) {
            this.switchRecipeList(MCP_PlaneInfoManager.getInstance());
        } else if (MCH_VehicleInfoManager.getInstance().getRecipeListSize() > 0) {
            this.switchRecipeList(MCH_VehicleInfoManager.getInstance());
        } else if (MCH_TankInfoManager.getInstance().getRecipeListSize() > 0) {
            this.switchRecipeList(MCH_TankInfoManager.getInstance());
        } else {
            this.switchRecipeList(MCH_ItemRecipe.getInstance());
        }
    }

    public static void initModelTransform() {
        modelRotX = 0.0f;
        modelRotY = 0.0f;
        modelPosX = 0.0f;
        modelPosY = 0.0f;
        modelZoom = 1.0f;
    }

    public void updateListSliderSize(int listSize) {
        int s = listSize / 2;
        if (listSize % 2 != 0) {
            ++s;
        }
        this.listSlider.valueMax = s > 3 ? (float)(s - 3) : 0.0f;
        this.listSlider.setSliderValue(0.0f);
    }

    public void switchScreen(int id) {
        this.screenId = id;
        for (int i = 0; i < this.field_146292_n.size(); ++i) {
            W_GuiButton.setVisible((GuiButton)this.field_146292_n.get(i), false);
        }
        if (id < this.screenButtonList.size()) {
            List<GuiButton> list = this.screenButtonList.get(id);
            for (GuiButton b : list) {
                W_GuiButton.setVisible(b, true);
            }
        }
        if (this.getScreenId() == 0 && this.current != null && this.current.getDescMaxPage() > 1) {
            W_GuiButton.setVisible(this.buttonNextPage, true);
            W_GuiButton.setVisible(this.buttonPrevPage, true);
        } else {
            W_GuiButton.setVisible(this.buttonNextPage, false);
            W_GuiButton.setVisible(this.buttonPrevPage, false);
        }
    }

    public void setCurrentRecipe(MCH_CurrentRecipe currentRecipe) {
        modelPosX = 0.0f;
        modelPosY = 0.0f;
        if (this.current == null || currentRecipe == null || !this.current.recipe.func_77571_b().func_77969_a(currentRecipe.recipe.func_77571_b())) {
            this.drawFace = 0;
        }
        this.current = currentRecipe;
        if (this.getScreenId() == 0 && this.current != null && this.current.getDescMaxPage() > 1) {
            W_GuiButton.setVisible(this.buttonNextPage, true);
            W_GuiButton.setVisible(this.buttonPrevPage, true);
        } else {
            W_GuiButton.setVisible(this.buttonNextPage, false);
            W_GuiButton.setVisible(this.buttonPrevPage, false);
        }
        this.updateEnableCreateButton();
    }

    public MCH_IRecipeList getCurrentList() {
        return this.currentList;
    }

    public void switchRecipeList(MCH_IRecipeList list) {
        if (this.getCurrentList() != list) {
            this.setCurrentRecipe(new MCH_CurrentRecipe(list, 0));
            this.currentList = list;
            this.updateListSliderSize(list.getRecipeListSize());
        } else {
            this.listSlider.setSliderValue(this.current.index / 2);
        }
    }

    public void func_73876_c() {
        super.func_73876_c();
        if (this.buttonClickWait > 0) {
            --this.buttonClickWait;
        }
    }

    public void func_146281_b() {
        super.func_146281_b();
        MCH_Lib.DbgLog(this.thePlayer.field_70170_p, "MCH_DraftingTableGui.onGuiClosed", new Object[0]);
    }

    protected void func_146284_a(GuiButton button) throws IOException {
        super.func_146284_a(button);
        if (this.buttonClickWait > 0) {
            return;
        }
        if (!button.field_146124_l) {
            return;
        }
        this.buttonClickWait = 3;
        int index = 0;
        int page = this.current.getDescCurrentPage();
        switch (button.field_146127_k) {
            case 30: {
                MCH_DraftingTableCreatePacket.send(this.current.recipe);
                break;
            }
            case 21: {
                if (this.current.isCurrentPageTexture()) {
                    page = 0;
                }
                if ((index = this.current.index - 1) < 0) {
                    index = this.getCurrentList().getRecipeListSize() - 1;
                }
                this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
                this.current.setDescCurrentPage(page);
                break;
            }
            case 20: {
                if (this.current.isCurrentPageTexture()) {
                    page = 0;
                }
                index = (this.current.index + 1) % this.getCurrentList().getRecipeListSize();
                this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
                this.current.setDescCurrentPage(page);
                break;
            }
            case 10: {
                MCH_DraftingTableGui.initModelTransform();
                modelRotX = 180.0f;
                modelRotY = 90.0f;
                this.switchRecipeList(MCH_HeliInfoManager.getInstance());
                this.switchScreen(1);
                break;
            }
            case 11: {
                MCH_DraftingTableGui.initModelTransform();
                modelRotX = 90.0f;
                modelRotY = 180.0f;
                this.switchRecipeList(MCP_PlaneInfoManager.getInstance());
                this.switchScreen(1);
                break;
            }
            case 13: {
                MCH_DraftingTableGui.initModelTransform();
                modelRotX = 180.0f;
                modelRotY = 90.0f;
                this.switchRecipeList(MCH_TankInfoManager.getInstance());
                this.switchScreen(1);
                break;
            }
            case 12: {
                MCH_DraftingTableGui.initModelTransform();
                modelRotX = 180.0f;
                modelRotY = 90.0f;
                this.switchRecipeList(MCH_VehicleInfoManager.getInstance());
                this.switchScreen(1);
                break;
            }
            case 14: {
                this.switchRecipeList(MCH_ItemRecipe.getInstance());
                this.switchScreen(1);
                break;
            }
            case 50: {
                if (this.current == null) break;
                this.current.switchNextPage();
                break;
            }
            case 51: {
                if (this.current == null) break;
                this.current.switchPrevPage();
                break;
            }
            case 40: 
            case 41: 
            case 42: 
            case 43: 
            case 44: 
            case 45: {
                index = (int)this.listSlider.getSliderValue() * 2 + (button.field_146127_k - 40);
                if (index >= this.getCurrentList().getRecipeListSize()) break;
                this.setCurrentRecipe(new MCH_CurrentRecipe(this.getCurrentList(), index));
                this.switchScreen(0);
            }
        }
    }

    private void updateEnableCreateButton() {
        MCH_DraftingTableGuiContainer container = (MCH_DraftingTableGuiContainer)this.field_147002_h;
        this.buttonCreate.field_146124_l = false;
        if (!container.func_75139_a(container.outputSlotIndex).func_75216_d()) {
            this.buttonCreate.field_146124_l = MCH_Recipes.canCraft(this.thePlayer, this.current.recipe);
        }
        if (this.thePlayer.field_71075_bZ.field_75098_d) {
            this.buttonCreate.field_146124_l = true;
        }
    }

    protected void func_73869_a(char par1, int keycode) throws IOException {
        if (keycode == 1 || keycode == W_KeyBinding.getKeyCode(Minecraft.func_71410_x().field_71474_y.field_151445_Q)) {
            if (this.getScreenId() == 0) {
                this.field_146297_k.field_71439_g.func_71053_j();
            } else {
                this.switchScreen(0);
            }
        }
        if (this.getScreenId() == 0) {
            if (keycode == 205) {
                this.func_146284_a(this.buttonNext);
            }
            if (keycode == 203) {
                this.func_146284_a(this.buttonPrev);
            }
        } else if (this.getScreenId() == 1) {
            if (keycode == 200) {
                this.listSlider.scrollDown(1.0f);
            }
            if (keycode == 208) {
                this.listSlider.scrollUp(1.0f);
            }
        }
    }

    protected void func_146979_b(int mx, int my) {
        int i;
        super.func_146979_b(mx, my);
        this.field_73735_i = 0.0f;
        GlStateManager.func_179147_l();
        if (this.getScreenId() == 0) {
            ArrayList<String> list = new ArrayList<String>();
            if (this.current != null) {
                if (this.current.isCurrentPageTexture()) {
                    GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    this.field_146297_k.func_110434_K().func_110577_a(this.current.getCurrentPageTexture());
                    this.drawTexturedModalRect(210, 20, 170, 190, 0, 0, 340, 380);
                } else if (this.current.isCurrentPageAcInfo()) {
                    for (i = 0; i < this.current.infoItem.size(); ++i) {
                        this.field_146289_q.func_78276_b(this.current.infoItem.get(i), 210, 40 + 10 * i, -9491968);
                        String data = this.current.infoData.get(i);
                        if (data.isEmpty()) continue;
                        this.field_146289_q.func_78276_b(data, 280, 40 + 10 * i, -9491968);
                    }
                } else {
                    W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
                    this.drawTexturedModalRect(340, 215, 45, 15, 400, 60, 90, 30);
                    if (mx >= 350 && mx <= 400 && my >= 214 && my <= 230) {
                        boolean lb = Mouse.isButtonDown((int)0);
                        boolean rb = Mouse.isButtonDown((int)1);
                        boolean mb = Mouse.isButtonDown((int)2);
                        list.add((lb ? TextFormatting.AQUA : "") + "Mouse left button drag : Rotation model");
                        list.add((rb ? TextFormatting.AQUA : "") + "Mouse right button drag : Zoom model");
                        list.add((mb ? TextFormatting.AQUA : "") + "Mouse middle button drag : Move model");
                    }
                }
            }
            this.drawString(this.current.displayName, 120, 20, -1);
            this.drawItemRecipe(this.current.recipe, 121, 34);
            if (list.size() > 0) {
                this.drawHoveringText(list, mx - 30, my - 0, this.field_146289_q);
            }
        }
        if (this.getScreenId() == 1) {
            int ry;
            int rx;
            int c;
            int r;
            int index = 2 * (int)this.listSlider.getSliderValue();
            i = 0;
            for (r = 0; r < 3; ++r) {
                for (c = 0; c < 2; ++c) {
                    if (index + i < this.getCurrentList().getRecipeListSize()) {
                        rx = 110 + 140 * c;
                        ry = 20 + 70 * r;
                        String s = this.getCurrentList().getRecipe(index + i).func_77571_b().func_82833_r();
                        this.drawCenteredString(s, rx, ry, -1);
                    }
                    ++i;
                }
            }
            W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
            i = 0;
            for (r = 0; r < 3; ++r) {
                for (c = 0; c < 2; ++c) {
                    if (index + i < this.getCurrentList().getRecipeListSize()) {
                        rx = 80 + 140 * c - 1;
                        ry = 30 + 70 * r - 1;
                        this.func_73729_b(rx, ry, 400, 0, 75, 54);
                    }
                    ++i;
                }
            }
            i = 0;
            for (r = 0; r < 3; ++r) {
                for (c = 0; c < 2; ++c) {
                    if (index + i < this.getCurrentList().getRecipeListSize()) {
                        rx = 80 + 140 * c;
                        ry = 30 + 70 * r;
                        this.drawItemRecipe(this.getCurrentList().getRecipe(index + i), rx, ry);
                    }
                    ++i;
                }
            }
        }
    }

    protected void func_184098_a(Slot slotIn, int slotId, int clickedButton, ClickType clickType) {
        if (this.getScreenId() != 1) {
            super.func_184098_a(slotIn, slotId, clickedButton, clickType);
        }
    }

    private int getScreenId() {
        return this.screenId;
    }

    public void drawItemRecipe(IRecipe recipe, int x, int y) {
        if (recipe == null) {
            return;
        }
        if (recipe.func_77571_b().func_190926_b()) {
            return;
        }
        if (recipe.func_77571_b().func_77973_b() == null) {
            return;
        }
        RenderHelper.func_74520_c();
        NonNullList ingredients = recipe.func_192400_c();
        for (int i = 0; i < ingredients.size(); ++i) {
            this.drawIngredient((Ingredient)ingredients.get(i), x + i % 3 * 18, y + i / 3 * 18);
        }
        this.drawItemStack(recipe.func_77571_b(), x + 54 + 3, y + 18);
        RenderHelper.func_74518_a();
    }

    public void func_146274_d() throws IOException {
        int wheel;
        super.func_146274_d();
        int dx = Mouse.getEventDX();
        int dy = Mouse.getEventDY();
        if (this.getScreenId() == 0 && Mouse.getX() > this.field_146297_k.field_71443_c / 2) {
            if (Mouse.isButtonDown((int)0) && (dx != 0 || dy != 0)) {
                modelRotX = (float)((double)modelRotX - (double)dy / 2.0);
                modelRotY = (float)((double)modelRotY - (double)dx / 2.0);
                if (modelRotX > 360.0f) {
                    modelRotX -= 360.0f;
                }
                if (modelRotX < -360.0f) {
                    modelRotX += 360.0f;
                }
                if (modelRotY > 360.0f) {
                    modelRotY -= 360.0f;
                }
                if (modelRotY < -360.0f) {
                    modelRotY += 360.0f;
                }
            }
            if (Mouse.isButtonDown((int)2) && (dx != 0 || dy != 0)) {
                modelPosX = (float)((double)modelPosX + (double)dx / 2.0);
                modelPosY = (float)((double)modelPosY - (double)dy / 2.0);
                if (modelRotX > 1000.0f) {
                    modelRotX = 1000.0f;
                }
                if (modelRotX < -1000.0f) {
                    modelRotX = -1000.0f;
                }
                if (modelRotY > 1000.0f) {
                    modelRotY = 1000.0f;
                }
                if (modelRotY < -1000.0f) {
                    modelRotY = -1000.0f;
                }
            }
            if (Mouse.isButtonDown((int)1) && dy != 0) {
                if ((double)(modelZoom = (float)((double)modelZoom + (double)dy / 100.0)) < 0.1) {
                    modelZoom = 0.1f;
                }
                if (modelZoom > 10.0f) {
                    modelZoom = 10.0f;
                }
            }
        }
        if ((wheel = Mouse.getEventDWheel()) != 0) {
            if (this.getScreenId() == 1) {
                if (wheel > 0) {
                    this.listSlider.scrollDown(1.0f);
                } else if (wheel < 0) {
                    this.listSlider.scrollUp(1.0f);
                }
            } else if (this.getScreenId() == 0) {
                if (wheel > 0) {
                    this.func_146284_a(this.buttonPrev);
                } else if (wheel < 0) {
                    this.func_146284_a(this.buttonNext);
                }
            }
        }
    }

    @Override
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (this.getScreenId() == 0) {
            super.func_73863_a(mouseX, mouseY, partialTicks);
        } else {
            List inventory = this.field_147002_h.field_75151_b;
            this.field_147002_h.field_75151_b = new ArrayList();
            super.func_73863_a(mouseX, mouseY, partialTicks);
            this.field_147002_h.field_75151_b = inventory;
        }
        if (this.getScreenId() == 0 && this.current.isCurrentPageModel()) {
            RenderHelper.func_74520_c();
            this.drawModel(partialTicks);
        }
    }

    public void drawModel(float partialTicks) {
        W_ModelCustom model = this.current.getModel();
        double scl = 162.0 / ((double)MathHelper.func_76135_e((float)model.size) < 0.01 ? 0.01 : (double)model.size);
        this.field_146297_k.func_110434_K().func_110577_a(this.current.getModelTexture());
        GlStateManager.func_179094_E();
        double cx = (double)(model.maxX - model.minX) * 0.5 + (double)model.minX;
        double cy = (double)(model.maxY - model.minY) * 0.5 + (double)model.minY;
        double cz = (double)(model.maxZ - model.minZ) * 0.5 + (double)model.minZ;
        if (this.current.modelRot == 0) {
            GlStateManager.func_179137_b((double)(cx * scl), (double)(cz * scl), (double)0.0);
        } else {
            GlStateManager.func_179137_b((double)(cz * scl), (double)(cy * scl), (double)0.0);
        }
        GlStateManager.func_179137_b((double)((float)(this.field_147003_i + 300) + modelPosX), (double)((float)(this.field_147009_r + 110) + modelPosY), (double)550.0);
        GlStateManager.func_179114_b((float)modelRotX, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)modelRotY, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179139_a((double)(scl * (double)modelZoom), (double)(scl * (double)modelZoom), (double)(-scl * (double)modelZoom));
        GlStateManager.func_179101_C();
        GlStateManager.func_179140_f();
        GlStateManager.func_179141_d();
        GlStateManager.func_179147_l();
        int faceNum = model.getFaceNum();
        if (this.drawFace < faceNum * 2) {
            GlStateManager.func_179131_c((float)0.1f, (float)0.1f, (float)0.1f, (float)1.0f);
            GlStateManager.func_179090_x();
            GlUtil.polygonMode(GlStateManager.CullFace.FRONT_AND_BACK, GlUtil.RasterizeType.LINE);
            GlUtil.pushLineWidth(1.0f);
            model.renderAll(this.drawFace - faceNum, this.drawFace);
            MCH_RenderAircraft.renderCrawlerTrack(null, this.current.getAcInfo(), partialTicks);
            GlUtil.popLineWidth();
            GlUtil.polygonMode(GlStateManager.CullFace.FRONT_AND_BACK, GlUtil.RasterizeType.FILL);
            GlStateManager.func_179098_w();
        }
        if (this.drawFace >= faceNum) {
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            model.renderAll(0, this.drawFace - faceNum);
            MCH_RenderAircraft.renderCrawlerTrack(null, this.current.getAcInfo(), partialTicks);
        }
        GlStateManager.func_179091_B();
        GlStateManager.func_179145_e();
        GlStateManager.func_179121_F();
        if (this.drawFace < 10000000) {
            this.drawFace = (int)((float)this.drawFace + 20.0f);
        }
    }

    protected void func_146976_a(float var1, int var2, int var3) {
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float z = this.field_73735_i;
        this.field_73735_i = 0.0f;
        W_McClient.MOD_bindTexture("textures/gui/drafting_table.png");
        if (this.getScreenId() == 0) {
            this.func_73729_b(this.field_147003_i, this.field_147009_r, 0, 0, this.field_146999_f, this.field_147000_g);
        }
        if (this.getScreenId() == 1) {
            this.func_73729_b(this.field_147003_i, this.field_147009_r, 0, this.field_147000_g, this.field_146999_f, this.field_147000_g);
            List<GuiButton> list = this.screenButtonList.get(1);
            int index = (int)this.listSlider.getSliderValue() * 2;
            for (int i = 0; i < 6; ++i) {
                W_GuiButton.setVisible(list.get(i), index + i < this.getCurrentList().getRecipeListSize());
            }
        }
        this.field_73735_i = z;
    }

    public void func_73729_b(int par1, int par2, int par3, int par4, int par5, int par6) {
        float w = 0.001953125f;
        float h = 0.001953125f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder buffer = tessellator.func_178180_c();
        buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        buffer.func_181662_b((double)(par1 + 0), (double)(par2 + par6), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + 0) * w), (double)((float)(par4 + par6) * h)).func_181675_d();
        buffer.func_181662_b((double)(par1 + par5), (double)(par2 + par6), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + par5) * w), (double)((float)(par4 + par6) * h)).func_181675_d();
        buffer.func_181662_b((double)(par1 + par5), (double)(par2 + 0), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + par5) * w), (double)((float)(par4 + 0) * h)).func_181675_d();
        buffer.func_181662_b((double)(par1 + 0), (double)(par2 + 0), (double)this.field_73735_i).func_187315_a((double)((float)(par3 + 0) * w), (double)((float)(par4 + 0) * h)).func_181675_d();
        tessellator.func_78381_a();
    }

    public void drawTexturedModalRect(int dx, int dy, int dw, int dh, int u, int v, int tw, int th) {
        float w = 0.001953125f;
        float h = 0.001953125f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder buffer = tessellator.func_178180_c();
        buffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        buffer.func_181662_b((double)(dx + 0), (double)(dy + dh), (double)this.field_73735_i).func_187315_a((double)((float)(u + 0) * w), (double)((float)(v + th) * h)).func_181675_d();
        buffer.func_181662_b((double)(dx + dw), (double)(dy + dh), (double)this.field_73735_i).func_187315_a((double)((float)(u + tw) * w), (double)((float)(v + th) * h)).func_181675_d();
        buffer.func_181662_b((double)(dx + dw), (double)(dy + 0), (double)this.field_73735_i).func_187315_a((double)((float)(u + tw) * w), (double)((float)(v + 0) * h)).func_181675_d();
        buffer.func_181662_b((double)(dx + 0), (double)(dy + 0), (double)this.field_73735_i).func_187315_a((double)((float)(u + 0) * w), (double)((float)(v + 0) * h)).func_181675_d();
        tessellator.func_78381_a();
    }

    public void drawTexturedModalRectWithColor(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int color) {
        float w = 0.001953125f;
        float h = 0.001953125f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder buf = tessellator.func_178180_c();
        buf.func_181668_a(7, DefaultVertexFormats.field_181709_i);
        buf.func_181662_b((double)x, (double)(y + height), (double)this.field_73735_i).func_187315_a((double)((float)u * w), (double)((float)(v + vHeight) * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
        buf.func_181662_b((double)(x + width), (double)(y + height), (double)this.field_73735_i).func_187315_a((double)((float)(u + uWidth) * w), (double)((float)(v + vHeight) * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
        buf.func_181662_b((double)(x + width), (double)y, (double)this.field_73735_i).func_187315_a((double)((float)(u + uWidth) * w), (double)((float)v * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
        buf.func_181662_b((double)x, (double)y, (double)this.field_73735_i).func_187315_a((double)((float)u * w), (double)((float)v * h)).func_181666_a(f, f1, f2, f3).func_181675_d();
        tessellator.func_78381_a();
    }
}

