/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package mcheli.hud;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import mcheli.MCH_ClientCommonTickHandler;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.MCH_LowPassFilterFloat;
import mcheli.MCH_Vector2;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.eval.eval.ExpRuleFactory;
import mcheli.eval.eval.Expression;
import mcheli.eval.eval.var.MapVariable;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.hud.MCH_Hud;
import mcheli.hud.MCH_HudItemExit;
import mcheli.plane.MCP_EntityPlane;
import mcheli.weapon.MCH_EntityTvMissile;
import mcheli.weapon.MCH_SightType;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponSet;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_OpenGlHelper;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public abstract class MCH_HudItem
extends Gui {
    public final int fileLine;
    public static Minecraft mc;
    public static EntityPlayer player;
    public static MCH_EntityAircraft ac;
    protected static double centerX;
    protected static double centerY;
    public static double width;
    public static double height;
    protected static Random rand;
    public static int scaleFactor;
    public static int colorSetting;
    protected static int altitudeUpdateCount;
    protected static int Altitude;
    protected static float prevRadarRot;
    protected static String WeaponName;
    protected static String WeaponAmmo;
    protected static String WeaponAllAmmo;
    protected static MCH_WeaponSet CurrentWeapon;
    protected static float ReloadPer;
    protected static float ReloadSec;
    protected static float MortarDist;
    protected static MCH_LowPassFilterFloat StickX_LPF;
    protected static MCH_LowPassFilterFloat StickY_LPF;
    protected static double StickX;
    protected static double StickY;
    protected static double TVM_PosX;
    protected static double TVM_PosY;
    protected static double TVM_PosZ;
    protected static double TVM_Diff;
    protected static double UAV_Dist;
    protected static int countFuelWarn;
    protected static ArrayList<MCH_Vector2> EntityList;
    protected static ArrayList<MCH_Vector2> EnemyList;
    protected static Map<Object, Object> varMap;
    protected MCH_Hud parent;
    protected static float partialTicks;
    private static MCH_HudItemExit dummy;

    public MCH_HudItem(int fileLine) {
        this.fileLine = fileLine;
        this.field_73735_i = -110.0f;
    }

    public abstract void execute();

    public boolean canExecute() {
        return !this.parent.isIfFalse;
    }

    public static void update() {
        MCH_WeaponSet ws = ac.getCurrentWeapon((Entity)player);
        MCH_HudItem.updateRadar(ac);
        MCH_HudItem.updateStick();
        MCH_HudItem.updateAltitude(ac);
        MCH_HudItem.updateTvMissile(ac);
        MCH_HudItem.updateUAV(ac);
        MCH_HudItem.updateWeapon(ac, ws);
        MCH_HudItem.updateVarMap(ac, ws);
    }

    public static String toFormula(String s) {
        return s.toLowerCase().replaceAll("#", "0x").replace("\t", " ").replace(" ", "");
    }

    public static double calc(String s) {
        Expression exp = ExpRuleFactory.getDefaultRule().parse(s);
        exp.setVariable(new MapVariable(varMap));
        return exp.evalDouble();
    }

    public static long calcLong(String s) {
        Expression exp = ExpRuleFactory.getDefaultRule().parse(s);
        exp.setVariable(new MapVariable(varMap));
        return exp.evalLong();
    }

    public void drawCenteredString(String s, int x, int y, int color) {
        this.func_73732_a(MCH_HudItem.mc.field_71466_p, s, x, y, color);
    }

    public void drawString(String s, int x, int y, int color) {
        this.func_73731_b(MCH_HudItem.mc.field_71466_p, s, x, y, color);
    }

    public void drawTexture(String name, double left, double top, double width, double height, double uLeft, double vTop, double uWidth, double vHeight, float rot, int textureWidth, int textureHeight) {
        W_McClient.MOD_bindTexture("textures/gui/" + name + ".png");
        GL11.glPushMatrix();
        GL11.glTranslated((double)(left + width / 2.0), (double)(top + height / 2.0), (double)0.0);
        GL11.glRotatef((float)rot, (float)0.0f, (float)0.0f, (float)1.0f);
        float fx = (float)(1.0 / (double)textureWidth);
        float fy = (float)(1.0 / (double)textureHeight);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        builder.func_181662_b(-width / 2.0, height / 2.0, (double)this.field_73735_i).func_187315_a(uLeft * (double)fx, (vTop + vHeight) * (double)fy).func_181675_d();
        builder.func_181662_b(width / 2.0, height / 2.0, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)fx, (vTop + vHeight) * (double)fy).func_181675_d();
        builder.func_181662_b(width / 2.0, -height / 2.0, (double)this.field_73735_i).func_187315_a((uLeft + uWidth) * (double)fx, vTop * (double)fy).func_181675_d();
        builder.func_181662_b(-width / 2.0, -height / 2.0, (double)this.field_73735_i).func_187315_a(uLeft * (double)fx, vTop * (double)fy).func_181675_d();
        tessellator.func_78381_a();
        GL11.glPopMatrix();
    }

    public static void drawRect(double par0, double par1, double par2, double par3, int par4) {
        double j1;
        if (par0 < par2) {
            j1 = par0;
            par0 = par2;
            par2 = j1;
        }
        if (par1 < par3) {
            j1 = par1;
            par1 = par3;
            par3 = j1;
        }
        float f3 = (float)(par4 >> 24 & 0xFF) / 255.0f;
        float f = (float)(par4 >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(par4 >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(par4 & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        W_OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float)f, (float)f1, (float)f2, (float)f3);
        builder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        builder.func_181662_b(par0, par3, 0.0).func_181675_d();
        builder.func_181662_b(par2, par3, 0.0).func_181675_d();
        builder.func_181662_b(par2, par1, 0.0).func_181675_d();
        builder.func_181662_b(par0, par1, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public void drawLine(double[] line, int color) {
        this.drawLine(line, color, 1);
    }

    public void drawLine(double[] line, int color, int mode) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color >> 0 & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(mode, DefaultVertexFormats.field_181705_e);
        for (int i = 0; i < line.length; i += 2) {
            builder.func_181662_b(line[i + 0], line[i + 1], (double)this.field_73735_i).func_181675_d();
        }
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
        GL11.glPopMatrix();
    }

    public void drawLineStipple(double[] line, int color, int factor, int pattern) {
        GL11.glEnable((int)2852);
        GL11.glLineStipple((int)(factor * scaleFactor), (short)((short)pattern));
        this.drawLine(line, color);
        GL11.glDisable((int)2852);
    }

    public void drawPoints(ArrayList<Double> points, int color, int pointWidth) {
        int prevWidth = GL11.glGetInteger((int)2833);
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4ub((byte)((byte)(color >> 16 & 0xFF)), (byte)((byte)(color >> 8 & 0xFF)), (byte)((byte)(color >> 0 & 0xFF)), (byte)((byte)(color >> 24 & 0xFF)));
        GL11.glPointSize((float)pointWidth);
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(0, DefaultVertexFormats.field_181705_e);
        for (int i = 0; i < points.size(); i += 2) {
            builder.func_181662_b(points.get(i).doubleValue(), points.get(i + 1).doubleValue(), 0.0).func_181675_d();
        }
        tessellator.func_78381_a();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4b((byte)-1, (byte)-1, (byte)-1, (byte)-1);
        GL11.glPointSize((float)prevWidth);
    }

    public static void updateVarMap(MCH_EntityAircraft ac, MCH_WeaponSet ws) {
        if (varMap == null) {
            varMap = new LinkedHashMap<Object, Object>();
        }
        MCH_HudItem.updateVarMapItem("color", MCH_HudItem.getColor());
        MCH_HudItem.updateVarMapItem("center_x", centerX);
        MCH_HudItem.updateVarMapItem("center_y", centerY);
        MCH_HudItem.updateVarMapItem("width", width);
        MCH_HudItem.updateVarMapItem("height", height);
        MCH_HudItem.updateVarMapItem("time", MCH_HudItem.player.field_70170_p.func_72820_D() % 24000L);
        MCH_HudItem.updateVarMapItem("test_mode", MCH_Config.TestMode.prmBool ? 1.0 : 0.0);
        MCH_HudItem.updateVarMapItem("plyr_yaw", MathHelper.func_76142_g((float)MCH_HudItem.player.field_70177_z));
        MCH_HudItem.updateVarMapItem("plyr_pitch", MCH_HudItem.player.field_70125_A);
        MCH_HudItem.updateVarMapItem("yaw", MathHelper.func_76142_g((float)ac.getRotYaw()));
        MCH_HudItem.updateVarMapItem("pitch", ac.getRotPitch());
        MCH_HudItem.updateVarMapItem("roll", MathHelper.func_76142_g((float)ac.getRotRoll()));
        MCH_HudItem.updateVarMapItem("altitude", Altitude);
        MCH_HudItem.updateVarMapItem("sea_alt", MCH_HudItem.getSeaAltitude(ac));
        MCH_HudItem.updateVarMapItem("have_radar", ac.isEntityRadarMounted() ? 1.0 : 0.0);
        MCH_HudItem.updateVarMapItem("radar_rot", MCH_HudItem.getRadarRot(ac));
        MCH_HudItem.updateVarMapItem("hp", ac.getHP());
        MCH_HudItem.updateVarMapItem("max_hp", ac.getMaxHP());
        MCH_HudItem.updateVarMapItem("hp_rto", ac.getMaxHP() > 0 ? (double)ac.getHP() / (double)ac.getMaxHP() : 0.0);
        MCH_HudItem.updateVarMapItem("throttle", ac.getCurrentThrottle());
        MCH_HudItem.updateVarMapItem("pos_x", ac.field_70165_t);
        MCH_HudItem.updateVarMapItem("pos_y", ac.field_70163_u);
        MCH_HudItem.updateVarMapItem("pos_z", ac.field_70161_v);
        MCH_HudItem.updateVarMapItem("motion_x", ac.field_70159_w);
        MCH_HudItem.updateVarMapItem("motion_y", ac.field_70181_x);
        MCH_HudItem.updateVarMapItem("motion_z", ac.field_70179_y);
        MCH_HudItem.updateVarMapItem("speed", Math.sqrt(ac.field_70159_w * ac.field_70159_w + ac.field_70181_x * ac.field_70181_x + ac.field_70179_y * ac.field_70179_y));
        MCH_HudItem.updateVarMapItem("fuel", ac.getFuelP());
        MCH_HudItem.updateVarMapItem("low_fuel", MCH_HudItem.isLowFuel(ac));
        MCH_HudItem.updateVarMapItem("stick_x", StickX);
        MCH_HudItem.updateVarMapItem("stick_y", StickY);
        MCH_HudItem.updateVarMap_Weapon(ws);
        MCH_HudItem.updateVarMapItem("vtol_stat", MCH_HudItem.getVtolStat(ac));
        MCH_HudItem.updateVarMapItem("free_look", MCH_HudItem.getFreeLook(ac, player));
        MCH_HudItem.updateVarMapItem("gunner_mode", ac.getIsGunnerMode((Entity)player) ? 1.0 : 0.0);
        MCH_HudItem.updateVarMapItem("cam_mode", ac.getCameraMode(player));
        MCH_HudItem.updateVarMapItem("cam_zoom", ac.camera.getCameraZoom());
        MCH_HudItem.updateVarMapItem("auto_pilot", MCH_HudItem.getAutoPilot(ac, player));
        MCH_HudItem.updateVarMapItem("have_flare", ac.haveFlare() ? 1.0 : 0.0);
        MCH_HudItem.updateVarMapItem("can_flare", ac.canUseFlare() ? 1.0 : 0.0);
        MCH_HudItem.updateVarMapItem("inventory", ac.func_70302_i_());
        MCH_HudItem.updateVarMapItem("hovering", ac instanceof MCH_EntityHeli && ac.isHoveringMode() ? 1.0 : 0.0);
        MCH_HudItem.updateVarMapItem("is_uav", ac.isUAV() ? 1.0 : 0.0);
        MCH_HudItem.updateVarMapItem("uav_fs", MCH_HudItem.getUAV_Fs(ac));
    }

    public static void updateVarMapItem(String key, double value) {
        varMap.put(key, value);
    }

    public static void drawVarMap() {
        if (MCH_Config.TestMode.prmBool) {
            int i = 0;
            int x = (int)(-300.0 + centerX);
            int y = (int)(-100.0 + centerY);
            for (Object keyObj : varMap.keySet()) {
                String key = (String)keyObj;
                dummy.drawString(key, x, y, 52992);
                Double d = (Double)varMap.get(key);
                String fmt = key.equalsIgnoreCase("color") ? String.format(": 0x%08X", d.intValue()) : String.format(": %.2f", d);
                dummy.drawString(fmt, x + 50, y, 52992);
                y += 8;
                if (++i != varMap.size() / 2) continue;
                x = (int)(200.0 + centerX);
                y = (int)(-100.0 + centerY);
            }
        }
    }

    private static double getUAV_Fs(MCH_EntityAircraft ac) {
        double uav_fs = 0.0;
        if (ac.isUAV() && ac.getUavStation() != null) {
            double dx = ac.field_70165_t - ac.getUavStation().field_70165_t;
            double dz = ac.field_70161_v - ac.getUavStation().field_70161_v;
            float dist = (float)Math.sqrt(dx * dx + dz * dz);
            if (dist > 120.0f) {
                dist = 120.0f;
            }
            uav_fs = 1.0f - dist / 120.0f;
        }
        return uav_fs;
    }

    private static void updateVarMap_Weapon(MCH_WeaponSet ws) {
        boolean reloading = false;
        double wpn_heat = 0.0;
        boolean is_heat_wpn = false;
        int sight_type = 0;
        double lock = 0.0;
        float rel_time = 0.0f;
        boolean display_mortar_dist = false;
        if (ws != null) {
            MCH_WeaponBase wb = ws.getCurrentWeapon();
            MCH_WeaponInfo wi = wb.getInfo();
            if (wi == null) {
                return;
            }
            is_heat_wpn = wi.maxHeatCount > 0;
            reloading = ws.isInPreparation();
            boolean bl = display_mortar_dist = wi.displayMortarDistance;
            if (wi.delay > wi.reloadTime) {
                rel_time = (float)ws.countWait / (float)(wi.delay > 0 ? wi.delay : 1);
                if (rel_time < 0.0f) {
                    rel_time = -rel_time;
                }
                if (rel_time > 1.0f) {
                    rel_time = 1.0f;
                }
            } else {
                rel_time = (float)ws.countReloadWait / (float)(wi.reloadTime > 0 ? wi.reloadTime : 1);
            }
            if (wi.maxHeatCount > 0) {
                double hpp = (double)ws.currentHeat / (double)wi.maxHeatCount;
                wpn_heat = hpp > 1.0 ? 1.0 : hpp;
            }
            int cntLockMax = wb.getLockCountMax();
            MCH_SightType sight = wb.getSightType();
            if (sight == MCH_SightType.LOCK && cntLockMax > 0) {
                lock = (double)wb.getLockCount() / (double)cntLockMax;
                sight_type = 2;
            }
            if (sight == MCH_SightType.ROCKET) {
                sight_type = 1;
            }
        }
        MCH_HudItem.updateVarMapItem("reloading", (double)reloading);
        MCH_HudItem.updateVarMapItem("reload_time", rel_time);
        MCH_HudItem.updateVarMapItem("wpn_heat", wpn_heat);
        MCH_HudItem.updateVarMapItem("is_heat_wpn", (double)is_heat_wpn);
        MCH_HudItem.updateVarMapItem("sight_type", sight_type);
        MCH_HudItem.updateVarMapItem("lock", lock);
        MCH_HudItem.updateVarMapItem("dsp_mt_dist", (double)display_mortar_dist);
        MCH_HudItem.updateVarMapItem("mt_dist", MortarDist);
    }

    public static int isLowFuel(MCH_EntityAircraft ac) {
        int is_low_fuel = 0;
        if (countFuelWarn <= 0) {
            countFuelWarn = 280;
        }
        if (--countFuelWarn < 160 && ac.getMaxFuel() > 0 && ac.getFuelP() < 0.1f && !ac.isInfinityFuel((Entity)player, false)) {
            is_low_fuel = 1;
        }
        return is_low_fuel;
    }

    public static double getSeaAltitude(MCH_EntityAircraft ac) {
        double a = ac.field_70163_u - ac.field_70170_p.func_72919_O();
        return a >= 0.0 ? a : 0.0;
    }

    public static float getRadarRot(MCH_EntityAircraft ac) {
        float prevRot;
        float rot = ac.getRadarRotate();
        if (rot < (prevRot = prevRadarRot)) {
            rot += 360.0f;
        }
        prevRadarRot = ac.getRadarRotate();
        return MCH_Lib.smooth(rot, prevRot, partialTicks);
    }

    public static int getVtolStat(MCH_EntityAircraft ac) {
        if (ac instanceof MCP_EntityPlane) {
            return ((MCP_EntityPlane)ac).getVtolMode();
        }
        return 0;
    }

    public static int getFreeLook(MCH_EntityAircraft ac, EntityPlayer player) {
        if (ac.isPilot((Entity)player) && ac.canSwitchFreeLook() && ac.isFreeLookMode()) {
            return 1;
        }
        return 0;
    }

    public static int getAutoPilot(MCH_EntityAircraft ac, EntityPlayer player) {
        if (ac instanceof MCP_EntityPlane && ac.isPilot((Entity)player) && ac.getIsGunnerMode((Entity)player)) {
            return 1;
        }
        return 0;
    }

    public static double getColor() {
        long l = colorSetting;
        return l &= 0xFFFFFFFFFFFFFFFFL;
    }

    private static void updateStick() {
        StickX_LPF.put((float)(MCH_ClientCommonTickHandler.getCurrentStickX() / MCH_ClientCommonTickHandler.getMaxStickLength()));
        StickY_LPF.put((float)(-MCH_ClientCommonTickHandler.getCurrentStickY() / MCH_ClientCommonTickHandler.getMaxStickLength()));
        StickX = StickX_LPF.getAvg();
        StickY = StickY_LPF.getAvg();
    }

    private static void updateRadar(MCH_EntityAircraft ac) {
        EntityList = ac.getRadarEntityList();
        EnemyList = ac.getRadarEnemyList();
    }

    private static void updateAltitude(MCH_EntityAircraft ac) {
        if (altitudeUpdateCount <= 0) {
            int heliY = (int)ac.field_70163_u;
            if (heliY > 256) {
                heliY = 256;
            }
            for (int i = 0; i < 256 && heliY - i > 0; ++i) {
                int id = W_WorldFunc.getBlockId(ac.field_70170_p, (int)ac.field_70165_t, heliY - i, (int)ac.field_70161_v);
                if (id == 0) continue;
                Altitude = i;
                if (ac.field_70163_u <= 256.0) break;
                Altitude = (int)((double)Altitude + (ac.field_70163_u - 256.0));
                break;
            }
            altitudeUpdateCount = 30;
        } else {
            --altitudeUpdateCount;
        }
    }

    public static void updateWeapon(MCH_EntityAircraft ac, MCH_WeaponSet ws) {
        if (ac.getWeaponNum() <= 0) {
            return;
        }
        if (ws == null) {
            return;
        }
        CurrentWeapon = ws;
        String string = WeaponName = ac.isPilotReloading() ? "-- Reloading --" : ws.getName();
        if (ws.getAmmoNumMax() > 0) {
            WeaponAmmo = ac.isPilotReloading() ? "----" : String.format("%4d", ws.getAmmoNum());
            WeaponAllAmmo = ac.isPilotReloading() ? "----" : String.format("%4d", ws.getRestAllAmmoNum());
        } else {
            WeaponAmmo = "";
            WeaponAllAmmo = "";
        }
        MCH_WeaponInfo wi = ws.getInfo();
        MortarDist = wi.displayMortarDistance ? (float)ac.getLandInDistance((Entity)player) : -1.0f;
        if (wi.delay > wi.reloadTime) {
            ReloadSec = ws.countWait >= 0 ? (float)ws.countWait : (float)(-ws.countWait);
            ReloadPer = (float)ws.countWait / (float)(wi.delay > 0 ? wi.delay : 1);
            if (ReloadPer < 0.0f) {
                ReloadPer = -ReloadPer;
            }
            if (ReloadPer > 1.0f) {
                ReloadPer = 1.0f;
            }
        } else {
            ReloadSec = ws.countReloadWait;
            ReloadPer = (float)ws.countReloadWait / (float)(wi.reloadTime > 0 ? wi.reloadTime : 1);
        }
        ReloadSec /= 20.0f;
        ReloadPer = (1.0f - ReloadPer) * 100.0f;
    }

    public static void updateUAV(MCH_EntityAircraft ac) {
        if (ac.isUAV() && ac.getUavStation() != null) {
            double dx = ac.field_70165_t - ac.getUavStation().field_70165_t;
            double dz = ac.field_70161_v - ac.getUavStation().field_70161_v;
            UAV_Dist = (float)Math.sqrt(dx * dx + dz * dz);
        } else {
            UAV_Dist = 0.0;
        }
    }

    private static void updateTvMissile(MCH_EntityAircraft ac) {
        MCH_EntityTvMissile tvmissile = ac.getTVMissile();
        if (tvmissile != null) {
            TVM_PosX = tvmissile.field_70165_t;
            TVM_PosY = tvmissile.field_70163_u;
            TVM_PosZ = tvmissile.field_70161_v;
            double dx = tvmissile.field_70165_t - ac.field_70165_t;
            double dy = tvmissile.field_70163_u - ac.field_70163_u;
            double dz = tvmissile.field_70161_v - ac.field_70161_v;
            TVM_Diff = Math.sqrt(dx * dx + dy * dy + dz * dz);
        } else {
            TVM_PosX = 0.0;
            TVM_PosY = 0.0;
            TVM_PosZ = 0.0;
            TVM_Diff = 0.0;
        }
    }

    static {
        centerX = 0.0;
        centerY = 0.0;
        rand = new Random();
        colorSetting = -16777216;
        altitudeUpdateCount = 0;
        Altitude = 0;
        WeaponName = "";
        WeaponAmmo = "";
        WeaponAllAmmo = "";
        CurrentWeapon = null;
        ReloadPer = 0.0f;
        ReloadSec = 0.0f;
        MortarDist = 0.0f;
        StickX_LPF = new MCH_LowPassFilterFloat(4);
        StickY_LPF = new MCH_LowPassFilterFloat(4);
        varMap = null;
        dummy = new MCH_HudItemExit(0);
    }
}

