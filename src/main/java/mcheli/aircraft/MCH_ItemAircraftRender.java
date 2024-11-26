/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package mcheli.aircraft;

import mcheli.MCH_ModelManager;
import mcheli.__helper.client._IItemRenderer;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_ItemAircraft;
import mcheli.wrapper.W_McClient;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemAircraftRender
implements _IItemRenderer {
    float size = 0.1f;
    float x = 0.1f;
    float y = 0.1f;
    float z = 0.1f;

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (item != null && item.func_77973_b() instanceof MCH_ItemAircraft) {
            MCH_AircraftInfo info = ((MCH_ItemAircraft)item.func_77973_b()).getAircraftInfo();
            if (info == null) {
                return false;
            }
            if (info != null && info.name.equalsIgnoreCase("mh-60l_dap")) {
                return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.ENTITY || type == ItemRenderType.INVENTORY;
            }
        }
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY || type == ItemRenderType.INVENTORY;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        W_McClient.MOD_bindTexture("textures/helicopters/mh-60l_dap.png");
        switch (type) {
            case ENTITY: {
                GL11.glEnable((int)32826);
                GL11.glEnable((int)2903);
                GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                GL11.glDisable((int)32826);
                break;
            }
            case EQUIPPED: {
                GL11.glEnable((int)32826);
                GL11.glEnable((int)2903);
                GL11.glTranslatef((float)0.0f, (float)0.005f, (float)-0.165f);
                GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
                GL11.glRotatef((float)-10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)-1.0f, (float)0.0f);
                GL11.glRotatef((float)-50.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                GL11.glDisable((int)32826);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                GL11.glEnable((int)32826);
                GL11.glEnable((int)2903);
                GL11.glTranslatef((float)0.3f, (float)0.5f, (float)-0.5f);
                GL11.glScalef((float)0.1f, (float)0.1f, (float)0.1f);
                GL11.glRotatef((float)10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glRotatef((float)140.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)-10.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                GL11.glDisable((int)32826);
                break;
            }
            case INVENTORY: {
                GL11.glTranslatef((float)this.x, (float)this.y, (float)this.z);
                GL11.glScalef((float)this.size, (float)this.size, (float)this.size);
                MCH_ModelManager.render("helicopters", "mh-60l_dap");
                break;
            }
        }
        GL11.glPopMatrix();
    }
}

