/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  javax.vecmath.Color4f
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package mcheli.debug._v3;

import com.google.common.collect.Maps;
import java.util.HashMap;
import javax.vecmath.Color4f;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponSet;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class WeaponPointRenderer {
    private static final Color4f[] C = new Color4f[]{new Color4f(1.0f, 0.0f, 0.0f, 1.0f), new Color4f(0.0f, 1.0f, 0.0f, 1.0f), new Color4f(0.0f, 0.0f, 1.0f, 1.0f), new Color4f(1.0f, 1.0f, 0.0f, 1.0f), new Color4f(1.0f, 0.0f, 1.0f, 1.0f), new Color4f(0.0f, 1.0f, 1.0f, 1.0f), new Color4f(0.95686275f, 0.6431373f, 0.3764706f, 1.0f), new Color4f(0.5411765f, 0.16862746f, 0.42477876f, 1.0f)};

    public static void renderWeaponPoints(MCH_EntityAircraft ac, MCH_AircraftInfo info, double x, double y, double z) {
        int prevPointSize = GlStateManager.func_187397_v((int)2833);
        int id = 0;
        int prevFunc = GlStateManager.func_187397_v((int)2932);
        HashMap poses = Maps.newHashMap();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.func_179145_e();
        GlStateManager.func_179132_a((boolean)false);
        GlStateManager.func_179143_c((int)519);
        GL11.glPointSize((float)20.0f);
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b((double)x, (double)y, (double)z);
        for (MCH_AircraftInfo.WeaponSet wsInfo : info.weaponSetList) {
            MCH_WeaponSet ws = ac.getWeaponByName(wsInfo.type);
            if (ws == null) continue;
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder builder = tessellator.func_178180_c();
            builder.func_181668_a(0, DefaultVertexFormats.field_181706_f);
            for (int i = 0; i < ws.getWeaponNum(); ++i) {
                MCH_WeaponBase weapon = ws.getWeapon(i);
                if (weapon == null) continue;
                int j = 0;
                if (poses.containsKey(weapon.position)) {
                    j = (Integer)poses.get(weapon.position);
                    ++j;
                }
                poses.put(weapon.position, j);
                Vec3d vec3d = weapon.getShotPos(ac);
                Color4f c = C[id % C.length];
                float f = (float)i * 0.1f;
                double d = (double)j * 0.04;
                builder.func_181662_b(vec3d.field_72450_a, vec3d.field_72448_b + d, vec3d.field_72449_c).func_181666_a(WeaponPointRenderer.in(c.x + f), WeaponPointRenderer.in(c.y + f), WeaponPointRenderer.in(c.z + f), c.w).func_181675_d();
            }
            tessellator.func_78381_a();
            ++id;
        }
        GlStateManager.func_179121_F();
        GL11.glPointSize((float)prevPointSize);
        GlStateManager.func_179143_c((int)prevFunc);
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }

    static float in(float value) {
        return MathHelper.func_76131_a((float)value, (float)0.0f, (float)1.0f);
    }
}

