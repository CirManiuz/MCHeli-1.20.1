/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli;

import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public abstract class MCH_ClientTickHandlerBase {
    protected Minecraft mc;
    public static float playerRotMinPitch = -90.0f;
    public static float playerRotMaxPitch = 90.0f;
    public static boolean playerRotLimitPitch = false;
    public static float playerRotMinYaw = -180.0f;
    public static float playerRotMaxYaw = 180.0f;
    public static boolean playerRotLimitYaw = false;
    private static int mouseWheel = 0;

    public abstract void updateKeybind(MCH_Config var1);

    public static void setRotLimitPitch(float min, float max, Entity player) {
        playerRotMinPitch = min;
        playerRotMaxPitch = max;
        playerRotLimitPitch = true;
        if (player != null) {
            player.field_70125_A = MCH_Lib.RNG(player.field_70125_A, playerRotMinPitch, playerRotMaxPitch);
        }
    }

    public static void setRotLimitYaw(float min, float max, Entity e) {
        playerRotMinYaw = min;
        playerRotMaxYaw = max;
        playerRotLimitYaw = true;
        if (e != null) {
            if (e.field_70125_A < playerRotMinPitch) {
                e.field_70125_A = playerRotMinPitch;
                e.field_70127_C = playerRotMinPitch;
            } else if (e.field_70125_A > playerRotMaxPitch) {
                e.field_70125_A = playerRotMaxPitch;
                e.field_70127_C = playerRotMaxPitch;
            }
        }
    }

    public static void initRotLimit() {
        playerRotMinPitch = -90.0f;
        playerRotMaxPitch = 90.0f;
        playerRotLimitYaw = false;
        playerRotMinYaw = -180.0f;
        playerRotMaxYaw = 180.0f;
        playerRotLimitYaw = false;
    }

    public static void applyRotLimit(Entity e) {
        if (e != null) {
            if (playerRotLimitPitch) {
                if (e.field_70125_A < playerRotMinPitch) {
                    e.field_70125_A = playerRotMinPitch;
                    e.field_70127_C = playerRotMinPitch;
                } else if (e.field_70125_A > playerRotMaxPitch) {
                    e.field_70125_A = playerRotMaxPitch;
                    e.field_70127_C = playerRotMaxPitch;
                }
            }
            if (!playerRotLimitYaw) {
                // empty if block
            }
        }
    }

    public MCH_ClientTickHandlerBase(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public static boolean updateMouseWheel(int wheel) {
        boolean cancelEvent = false;
        if (wheel != 0 && MCH_Config.SwitchWeaponWithMouseWheel.prmBool) {
            int nwid;
            int cwid;
            MCH_EntityAircraft ac;
            MCH_ClientTickHandlerBase.setMouseWheel(0);
            EntityPlayerSP player = Minecraft.func_71410_x().field_71439_g;
            if (player != null && (ac = MCH_EntityAircraft.getAircraft_RiddenOrControl((Entity)player)) != null && (cwid = ac.getWeaponIDBySeatID(ac.getSeatIdByEntity((Entity)player))) != (nwid = ac.getNextWeaponID((Entity)player, 1))) {
                MCH_ClientTickHandlerBase.setMouseWheel(wheel);
                cancelEvent = true;
            }
        }
        return cancelEvent;
    }

    protected abstract void onTick(boolean var1);

    public static void playSoundOK() {
        W_McClient.playSoundClick(1.0f, 1.0f);
    }

    public static void playSoundNG() {
        W_McClient.MOD_playSoundFX("ng", 1.0f, 1.0f);
    }

    public static void playSound(String name) {
        W_McClient.MOD_playSoundFX(name, 1.0f, 1.0f);
    }

    public static void playSound(String name, float vol, float pitch) {
        W_McClient.MOD_playSoundFX(name, vol, pitch);
    }

    public static int getMouseWheel() {
        return mouseWheel;
    }

    public static void setMouseWheel(int mouseWheel) {
        MCH_ClientTickHandlerBase.mouseWheel = mouseWheel;
    }
}

