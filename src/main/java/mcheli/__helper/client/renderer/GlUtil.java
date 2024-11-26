/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Queues
 *  javax.vecmath.AxisAngle4f
 *  javax.vecmath.Matrix4f
 *  javax.vecmath.Quat4f
 *  javax.vecmath.Vector3f
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$CullFace
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraftforge.common.model.TRSRTransformation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.apache.commons.lang3.tuple.Pair
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Sphere
 */
package mcheli.__helper.client.renderer;

import com.google.common.collect.Queues;
import java.util.Deque;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

@SideOnly(value=Side.CLIENT)
public class GlUtil
extends GlStateManager {
    private static final StencilState stencilState = new StencilState();
    private static final ClearState clearState = new ClearState();
    private static final BooleanState scissorState = new BooleanState(3089);
    private static final BooleanState stippleState = new BooleanState(2852);
    private static final Deque<Float> lineWidthState = Queues.newArrayDeque();
    private static final Deque<Float> pointSizeState = Queues.newArrayDeque();
    private static final Deque<Pair<Integer, Integer>> blendFancState = Queues.newArrayDeque();

    public static float getFloat(int pname) {
        return GL11.glGetFloat((int)pname);
    }

    public static float getLineWidth() {
        return GL11.glGetFloat((int)2849);
    }

    public static float getPointSize() {
        return GL11.glGetFloat((int)2833);
    }

    public static int getBlendSrcFactor() {
        return GL11.glGetInteger((int)3041);
    }

    public static int getBlendDstFactor() {
        return GL11.glGetInteger((int)3040);
    }

    public static void pointSize(float size) {
        GL11.glPointSize((float)size);
    }

    public static void pushLineWidth(float width) {
        lineWidthState.push(Float.valueOf(GlUtil.getLineWidth()));
        GlUtil.func_187441_d((float)width);
    }

    public static float popLineWidth() {
        float f = lineWidthState.pop().floatValue();
        GlUtil.func_187441_d((float)f);
        return f;
    }

    public static void pushPointSize(float size) {
        pointSizeState.push(Float.valueOf(GlUtil.getPointSize()));
        GlUtil.pointSize(size);
    }

    public static float popPointSize() {
        float f = pointSizeState.pop().floatValue();
        GlUtil.pointSize(f);
        return f;
    }

    public static void pushBlendFunc(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor) {
        GlUtil.pushBlendFunc(srcFactor.field_187395_p, dstFactor.field_187345_o);
    }

    public static void pushBlendFunc(int srcFactor, int dstFactor) {
        blendFancState.push((Pair<Integer, Integer>)Pair.of((Object)GlUtil.getBlendSrcFactor(), (Object)GlUtil.getBlendDstFactor()));
        GlUtil.func_179112_b((int)srcFactor, (int)dstFactor);
    }

    public static void popBlendFunc() {
        Pair<Integer, Integer> func = blendFancState.pop();
        GlUtil.func_179112_b((int)((Integer)func.getLeft()), (int)((Integer)func.getRight()));
    }

    public static void polygonMode(GlStateManager.CullFace face, RasterizeType mode) {
        GlUtil.func_187409_d((int)face.field_187328_d, (int)mode.mode);
    }

    public static void depthFunc(Function depthFunc) {
        GlUtil.func_179143_c((int)depthFunc.func);
    }

    public static void clearStencilBufferBit() {
        GlUtil.func_179086_m((int)1024);
    }

    public static void clearStencil(int stencil) {
        if (stencil != GlUtil.clearState.stencil) {
            GlUtil.clearState.stencil = stencil;
            GL11.glClearStencil((int)stencil);
        }
    }

    public static void enableStencil() {
        GlUtil.stencilState.stencilTest.setEnabled();
    }

    public static void disableStencil() {
        GlUtil.stencilState.stencilTest.setDisabled();
    }

    public static void stencilFunc(Function stencilFunc, int ref, int mask) {
        GlUtil.stencilFunc(stencilFunc.func, ref, mask);
    }

    public static void stencilFunc(int stencilFunc, int ref, int mask) {
        if (stencilFunc != GlUtil.stencilState.func.func || ref != GlUtil.stencilState.func.ref || mask != GlUtil.stencilState.func.mask) {
            GlUtil.stencilState.func.func = stencilFunc;
            GlUtil.stencilState.func.ref = ref;
            GlUtil.stencilState.func.mask = mask;
            GL11.glStencilFunc((int)stencilFunc, (int)ref, (int)mask);
        }
    }

    public static void stencilMask(int mask) {
        if (mask != GlUtil.stencilState.mask) {
            GlUtil.stencilState.mask = mask;
            GL11.glStencilMask((int)mask);
        }
    }

    public static void stencilOp(StencilAction fail, StencilAction zfail, StencilAction zpass) {
        GlUtil.stencilOp(fail.action, zfail.action, zpass.action);
    }

    public static void stencilOp(int fail, int zfail, int zpass) {
        if (fail != GlUtil.stencilState.fail || zfail != GlUtil.stencilState.zfail || zpass != GlUtil.stencilState.zpass) {
            GlUtil.stencilState.fail = fail;
            GlUtil.stencilState.zfail = zfail;
            GlUtil.stencilState.zpass = zpass;
            GL11.glStencilOp((int)fail, (int)zfail, (int)zpass);
        }
    }

    public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
        Sphere s = new Sphere();
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)1.2f);
        GL11.glDisable((int)3553);
        s.setDrawStyle(100013);
        GL11.glTranslatef((float)((float)x), (float)((float)y), (float)((float)z));
        s.draw(size, slices, stacks);
        GL11.glLineWidth((float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void enableScissor() {
        scissorState.setEnabled();
    }

    public static void disableScissor() {
        scissorState.setDisabled();
    }

    public static void scissor(int x, int y, int width, int height) {
        GL11.glScissor((int)x, (int)y, (int)width, (int)height);
    }

    public static void scissorGui(Minecraft mc, int x, int y, int width, int height) {
        ScaledResolution res = new ScaledResolution(mc);
        double scaleW = (double)mc.field_71443_c / res.func_78327_c();
        double scaleH = (double)mc.field_71440_d / res.func_78324_d();
        GlUtil.scissor((int)((double)x * scaleW), (int)((double)mc.field_71440_d - (double)(y + height) * scaleH), (int)((double)width * scaleW), (int)((double)height * scaleH));
    }

    public static void enableStipple() {
        stippleState.setEnabled();
    }

    public static void disableStipple() {
        stippleState.setDisabled();
    }

    public static void lineStipple(int factor, short pattern) {
        GL11.glLineStipple((int)factor, (short)pattern);
    }

    public static boolean enableStencilBuffer() {
        Framebuffer framebuffer = Minecraft.func_71410_x().func_147110_a();
        if (!framebuffer.isStencilEnabled() && OpenGlHelper.func_148822_b()) {
            framebuffer.enableStencil();
            return true;
        }
        return false;
    }

    public static boolean isEnableStencilBuffer() {
        return Minecraft.func_71410_x().func_147110_a().isStencilEnabled();
    }

    public static Matrix4f translateAsMatrix(float x, float y, float z) {
        return TRSRTransformation.mul((Vector3f)new Vector3f(x, y, z), null, null, null);
    }

    public static Matrix4f rotateAsMatrix(float angle, float x, float y, float z) {
        Quat4f quat = new Quat4f(0.0f, 0.0f, 0.0f, 1.0f);
        Quat4f t = new Quat4f();
        t.set(new AxisAngle4f(x, y, z, angle * ((float)Math.PI / 180)));
        quat.mul(t);
        return TRSRTransformation.mul(null, (Quat4f)quat, null, null);
    }

    public static Matrix4f scaleAsMatrix(float x, float y, float z) {
        return TRSRTransformation.mul(null, null, (Vector3f)new Vector3f(x, y, z), null);
    }

    @SideOnly(value=Side.CLIENT)
    protected static class BooleanState {
        private final int capability;
        private boolean currentState;

        public BooleanState(int capabilityIn) {
            this.capability = capabilityIn;
        }

        public void setDisabled() {
            this.setState(false);
        }

        public void setEnabled() {
            this.setState(true);
        }

        public void setState(boolean state) {
            if (state != this.currentState) {
                this.currentState = state;
                if (state) {
                    GL11.glEnable((int)this.capability);
                } else {
                    GL11.glDisable((int)this.capability);
                }
            }
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum RasterizeType {
        POINT(6912),
        LINE(6913),
        FILL(6914);

        public final int mode;

        private RasterizeType(int mode) {
            this.mode = mode;
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class StencilState {
        public BooleanState stencilTest = new BooleanState(2960);
        public StencilFunc func = new StencilFunc();
        public int mask = -1;
        public int fail = 7680;
        public int zfail = 7680;
        public int zpass = 7680;

        private StencilState() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class StencilFunc {
        public int func = 519;
        public int ref = 0;
        public int mask = -1;

        private StencilFunc() {
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum StencilAction {
        KEEP(7680),
        ZERO(0),
        INCR(7682),
        INCR_WRAP(34055),
        DECR(7683),
        DECR_WRAP(34056),
        REPLACE(7681),
        INVERT(5386);

        public final int action;

        private StencilAction(int action) {
            this.action = action;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static enum Function {
        NEVER(512),
        LESS(513),
        EQUAL(514),
        LEQUAL(515),
        GREATER(516),
        NOTEQUAL(517),
        GEQUAL(518),
        ALWAYS(519);

        public final int func;

        private Function(int func) {
            this.func = func;
        }
    }

    @SideOnly(value=Side.CLIENT)
    static class ClearState {
        public int stencil = 0;

        private ClearState() {
        }
    }
}

