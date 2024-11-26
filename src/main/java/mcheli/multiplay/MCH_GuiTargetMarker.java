/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package mcheli.multiplay;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.MCH_MarkEntityPos;
import mcheli.MCH_ServerSettings;
import mcheli.__helper.entity.ITargetMarkerObject;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.gui.MCH_Gui;
import mcheli.multiplay.MCH_Multiplay;
import mcheli.multiplay.MCH_TargetType;
import mcheli.particles.MCH_ParticlesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@SideOnly(value=Side.CLIENT)
public class MCH_GuiTargetMarker
extends MCH_Gui {
    private static FloatBuffer matModel = BufferUtils.createFloatBuffer((int)16);
    private static FloatBuffer matProjection = BufferUtils.createFloatBuffer((int)16);
    private static IntBuffer matViewport = BufferUtils.createIntBuffer((int)16);
    private static ArrayList<MCH_MarkEntityPos> entityPos = new ArrayList();
    private static HashMap<Integer, Integer> spotedEntity = new HashMap();
    private static Minecraft s_minecraft;
    private static int spotedEntityCountdown;

    public MCH_GuiTargetMarker(Minecraft minecraft) {
        super(minecraft);
        s_minecraft = minecraft;
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
    }

    @Override
    public boolean func_73868_f() {
        return false;
    }

    @Override
    public boolean isDrawGui(EntityPlayer player) {
        return player != null && player.field_70170_p != null;
    }

    public static void onClientTick() {
        if (!Minecraft.func_71410_x().func_147113_T()) {
            ++spotedEntityCountdown;
        }
        if (spotedEntityCountdown >= 20) {
            spotedEntityCountdown = 0;
            for (Integer key : spotedEntity.keySet()) {
                int count = spotedEntity.get(key);
                if (count <= 0) continue;
                spotedEntity.put(key, count - 1);
            }
            Iterator<Integer> i = spotedEntity.values().iterator();
            while (i.hasNext()) {
                if (i.next() > 0) continue;
                i.remove();
            }
        }
    }

    public static boolean isSpotedEntity(@Nullable Entity entity) {
        if (entity == null) {
            return false;
        }
        int entityId = entity.func_145782_y();
        for (int key : spotedEntity.keySet()) {
            if (key != entityId) continue;
            return true;
        }
        return false;
    }

    public static void addSpotedEntity(int entityId, int count) {
        if (spotedEntity.containsKey(entityId)) {
            int now = spotedEntity.get(entityId);
            if (count > now) {
                spotedEntity.put(entityId, count);
            }
        } else {
            spotedEntity.put(entityId, count);
        }
    }

    public static void addMarkEntityPos(int reserve, ITargetMarkerObject target, double x, double y, double z) {
        MCH_GuiTargetMarker.addMarkEntityPos(reserve, target, x, y, z, false);
    }

    public static void addMarkEntityPos(int reserve, ITargetMarkerObject target, double x, double y, double z, boolean nazo) {
        if (!MCH_GuiTargetMarker.isEnableEntityMarker()) {
            return;
        }
        MCH_TargetType spotType = MCH_TargetType.NONE;
        EntityPlayerSP clientPlayer = MCH_GuiTargetMarker.s_minecraft.field_71439_g;
        Entity entity = target.getEntity();
        if (entity instanceof MCH_EntityAircraft) {
            MCH_EntityAircraft ac = (MCH_EntityAircraft)entity;
            if (ac.isMountedEntity((Entity)clientPlayer)) {
                return;
            }
            if (ac.isMountedSameTeamEntity((EntityLivingBase)clientPlayer)) {
                spotType = MCH_TargetType.SAME_TEAM_PLAYER;
            }
        } else if (entity instanceof EntityPlayer) {
            if (entity == clientPlayer || entity.func_184187_bx() instanceof MCH_EntitySeat || entity.func_184187_bx() instanceof MCH_EntityAircraft) {
                return;
            }
            if (clientPlayer.func_96124_cp() != null && clientPlayer.func_184191_r(entity)) {
                spotType = MCH_TargetType.SAME_TEAM_PLAYER;
            }
        }
        if (spotType == MCH_TargetType.NONE && MCH_GuiTargetMarker.isSpotedEntity(entity)) {
            spotType = MCH_Multiplay.canSpotEntity((Entity)clientPlayer, clientPlayer.field_70165_t, clientPlayer.field_70163_u + (double)clientPlayer.func_70047_e(), clientPlayer.field_70161_v, entity, false);
        }
        if (reserve == 100) {
            spotType = MCH_TargetType.POINT;
        }
        if (spotType != MCH_TargetType.NONE) {
            MCH_MarkEntityPos e = new MCH_MarkEntityPos(spotType.ordinal(), target);
            GL11.glGetFloat((int)2982, (FloatBuffer)matModel);
            GL11.glGetFloat((int)2983, (FloatBuffer)matProjection);
            GL11.glGetInteger((int)2978, (IntBuffer)matViewport);
            if (nazo) {
                GLU.gluProject((float)((float)z), (float)((float)y), (float)((float)x), (FloatBuffer)matModel, (FloatBuffer)matProjection, (IntBuffer)matViewport, (FloatBuffer)e.pos);
                float yy = e.pos.get(1);
                GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)matModel, (FloatBuffer)matProjection, (IntBuffer)matViewport, (FloatBuffer)e.pos);
                e.pos.put(1, yy);
            } else {
                GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)matModel, (FloatBuffer)matProjection, (IntBuffer)matViewport, (FloatBuffer)e.pos);
            }
            entityPos.add(e);
        }
    }

    public static void clearMarkEntityPos() {
        entityPos.clear();
    }

    public static boolean isEnableEntityMarker() {
        return MCH_Config.DisplayEntityMarker.prmBool && (Minecraft.func_71410_x().func_71356_B() || MCH_ServerSettings.enableEntityMarker) && MCH_Config.EntityMarkerSize.prmDouble > 0.0;
    }

    @Override
    public void drawGui(EntityPlayer player, boolean isThirdPersonView) {
        GL11.glLineWidth((float)(scaleFactor * 2));
        if (!this.isDrawGui(player)) {
            return;
        }
        GL11.glDisable((int)3042);
        if (MCH_GuiTargetMarker.isEnableEntityMarker()) {
            this.drawMark();
        }
    }

    void drawMark() {
        int[] COLOR_TABLE = new int[]{0, -808464433, -805371904, -805306624, -822018049, -805351649, -65536, 0};
        int scale = scaleFactor > 0 ? scaleFactor : 2;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
        GL11.glDepthMask((boolean)false);
        int DW = this.field_146297_k.field_71443_c;
        int DSW = this.field_146297_k.field_71443_c / scale;
        int DSH = this.field_146297_k.field_71440_d / scale;
        double x = 9999.0;
        double z = 9999.0;
        double y = 9999.0;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        for (int i = 0; i < 2; ++i) {
            if (i == 0) {
                builder.func_181668_a(i == 0 ? 4 : 1, DefaultVertexFormats.field_181706_f);
            }
            for (MCH_MarkEntityPos e : entityPos) {
                int color = COLOR_TABLE[e.type];
                x = e.pos.get(0) / (float)scale;
                z = e.pos.get(2);
                y = e.pos.get(1) / (float)scale;
                if (z < 1.0) {
                    y = (double)DSH - y;
                } else if (x < (double)(DW / 2)) {
                    x = 10000.0;
                } else if (x >= (double)(DW / 2)) {
                    x = -10000.0;
                }
                if (i == 0) {
                    double size = MCH_Config.EntityMarkerSize.prmDouble;
                    if (e.type >= MCH_TargetType.POINT.ordinal() || !(z < 1.0) || !(x >= 0.0) || !(x <= (double)DSW) || !(y >= 0.0) || !(y <= (double)DSH)) continue;
                    this.drawTriangle1(builder, x, y, size, color);
                    continue;
                }
                if (e.type != MCH_TargetType.POINT.ordinal() || e.getTarget() == null) continue;
                ITargetMarkerObject target = e.getTarget();
                double MARK_SIZE = MCH_Config.BlockMarkerSize.prmDouble;
                if (z < 1.0 && x >= 0.0 && x <= (double)(DSW - 20) && y >= 0.0 && y <= (double)(DSH - 40)) {
                    double dist = this.field_146297_k.field_71439_g.func_70011_f(target.getX(), target.getY(), target.getZ());
                    GL11.glEnable((int)3553);
                    this.drawCenteredString(String.format("%.0fm", dist), (int)x, (int)(y + MARK_SIZE * 1.1 + 16.0), color);
                    if (x >= (double)(DSW / 2 - 20) && x <= (double)(DSW / 2 + 20) && y >= (double)(DSH / 2 - 20) && y <= (double)(DSH / 2 + 20)) {
                        this.drawString(String.format("x : %.0f", target.getX()), (int)(x + MARK_SIZE + 18.0), (int)y - 12, color);
                        this.drawString(String.format("y : %.0f", target.getY()), (int)(x + MARK_SIZE + 18.0), (int)y - 4, color);
                        this.drawString(String.format("z : %.0f", target.getZ()), (int)(x + MARK_SIZE + 18.0), (int)y + 4, color);
                    }
                    GL11.glDisable((int)3553);
                    builder.func_181668_a(1, DefaultVertexFormats.field_181706_f);
                    MCH_GuiTargetMarker.drawRhombus(builder, 15, x, y, this.field_73735_i, MARK_SIZE, color);
                } else {
                    builder.func_181668_a(1, DefaultVertexFormats.field_181706_f);
                    double S = 30.0;
                    if (x < S) {
                        MCH_GuiTargetMarker.drawRhombus(builder, 1, S, DSH / 2, this.field_73735_i, MARK_SIZE, color);
                    } else if (x > (double)DSW - S) {
                        MCH_GuiTargetMarker.drawRhombus(builder, 4, (double)DSW - S, DSH / 2, this.field_73735_i, MARK_SIZE, color);
                    }
                    if (y < S) {
                        MCH_GuiTargetMarker.drawRhombus(builder, 8, DSW / 2, S, this.field_73735_i, MARK_SIZE, color);
                    } else if (y > (double)DSH - S * 2.0) {
                        MCH_GuiTargetMarker.drawRhombus(builder, 2, DSW / 2, (double)DSH - S * 2.0, this.field_73735_i, MARK_SIZE, color);
                    }
                }
                tessellator.func_78381_a();
            }
            if (i != 0) continue;
            tessellator.func_78381_a();
        }
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void drawRhombus(BufferBuilder builder, int dir, double x, double y, double z, double size, int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color >> 0 & 0xFF;
        int alpha = color >> 24 & 0xFF;
        double M = (size *= 2.0) / 3.0;
        if ((dir & 1) != 0) {
            builder.func_181662_b(x - size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x - size + M, y - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x - size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x - size + M, y + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
        }
        if ((dir & 4) != 0) {
            builder.func_181662_b(x + size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x + size - M, y - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x + size, y, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x + size - M, y + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
        }
        if ((dir & 8) != 0) {
            builder.func_181662_b(x, y - size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x + M, y - size + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x, y - size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x - M, y - size + M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
        }
        if ((dir & 2) != 0) {
            builder.func_181662_b(x, y + size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x + M, y + size - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x, y + size, z).func_181669_b(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b(x - M, y + size - M, z).func_181669_b(red, green, blue, alpha).func_181675_d();
        }
    }

    public void drawTriangle1(BufferBuilder builder, double x, double y, double size, int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color >> 0 & 0xFF;
        int alpha = color >> 24 & 0xFF;
        builder.func_181662_b(x + size / 2.0, y - 10.0 - size, (double)this.field_73735_i).func_181669_b(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(x - size / 2.0, y - 10.0 - size, (double)this.field_73735_i).func_181669_b(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b(x + 0.0, y - 10.0, (double)this.field_73735_i).func_181669_b(red, green, blue, alpha).func_181675_d();
    }

    public static void markPoint(int px, int py, int pz) {
        EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
        if (player != null && player.field_70170_p != null) {
            if (py < 1000) {
                MCH_ParticlesUtil.spawnMarkPoint((EntityPlayer)player, 0.5 + (double)px, 1.0 + (double)py, 0.5 + (double)pz);
            } else {
                MCH_ParticlesUtil.clearMarkPoint();
            }
        }
    }

    static {
        spotedEntityCountdown = 0;
    }
}

