/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package mcheli.lweapon;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.annotation.Nullable;
import mcheli.MCH_ClientTickHandlerBase;
import mcheli.MCH_Config;
import mcheli.MCH_Key;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.gltd.MCH_EntityGLTD;
import mcheli.lweapon.MCH_ItemLightWeaponBase;
import mcheli.lweapon.MCH_PacketLightWeaponPlayerControl;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponCreator;
import mcheli.weapon.MCH_WeaponGuidanceSystem;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_EntityPlayer;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_Network;
import mcheli.wrapper.W_Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class MCH_ClientLightWeaponTickHandler
extends MCH_ClientTickHandlerBase {
    private static FloatBuffer screenPos = BufferUtils.createFloatBuffer((int)3);
    private static FloatBuffer screenPosBB = BufferUtils.createFloatBuffer((int)3);
    private static FloatBuffer matModel = BufferUtils.createFloatBuffer((int)16);
    private static FloatBuffer matProjection = BufferUtils.createFloatBuffer((int)16);
    private static IntBuffer matViewport = BufferUtils.createIntBuffer((int)16);
    protected boolean isHeldItem = false;
    protected boolean isBeforeHeldItem = false;
    protected EntityPlayer prevThePlayer = null;
    protected ItemStack prevItemStack = ItemStack.field_190927_a;
    public MCH_Key KeyAttack;
    public MCH_Key KeyUseWeapon;
    public MCH_Key KeySwWeaponMode;
    public MCH_Key KeyZoom;
    public MCH_Key KeyCameraMode;
    public MCH_Key[] Keys;
    protected static MCH_WeaponBase weapon;
    public static int reloadCount;
    public static int lockonSoundCount;
    public static int weaponMode;
    public static int selectedZoom;
    public static Entity markEntity;
    public static Vec3d markPos;
    public static MCH_WeaponGuidanceSystem gs;
    public static double lockRange;

    public MCH_ClientLightWeaponTickHandler(Minecraft minecraft, MCH_Config config) {
        super(minecraft);
        this.updateKeybind(config);
        MCH_ClientLightWeaponTickHandler.gs.canLockInAir = false;
        MCH_ClientLightWeaponTickHandler.gs.canLockOnGround = false;
        MCH_ClientLightWeaponTickHandler.gs.canLockInWater = false;
        gs.setLockCountMax(40);
        MCH_ClientLightWeaponTickHandler.gs.lockRange = 120.0;
        lockonSoundCount = 0;
        this.initWeaponParam(null);
    }

    public static void markEntity(Entity entity, double x, double y, double z) {
        if (gs.getLockingEntity() == entity) {
            MCH_AircraftInfo i;
            GL11.glGetFloat((int)2982, (FloatBuffer)matModel);
            GL11.glGetFloat((int)2983, (FloatBuffer)matProjection);
            GL11.glGetInteger((int)2978, (IntBuffer)matViewport);
            GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)matModel, (FloatBuffer)matProjection, (IntBuffer)matViewport, (FloatBuffer)screenPos);
            MCH_AircraftInfo mCH_AircraftInfo = i = entity instanceof MCH_EntityAircraft ? ((MCH_EntityAircraft)entity).getAcInfo() : null;
            float w = entity.field_70130_N > entity.field_70131_O ? entity.field_70130_N : (i != null ? i.markerWidth : entity.field_70131_O);
            float h = i != null ? i.markerHeight : entity.field_70131_O;
            GLU.gluProject((float)((float)x + w), (float)((float)y + h), (float)((float)z + w), (FloatBuffer)matModel, (FloatBuffer)matProjection, (IntBuffer)matViewport, (FloatBuffer)screenPosBB);
            markEntity = entity;
        }
    }

    @Nullable
    public static Vec3d getMartEntityPos() {
        if (gs.getLockingEntity() == markEntity && markEntity != null) {
            return new Vec3d((double)screenPos.get(0), (double)screenPos.get(1), (double)screenPos.get(2));
        }
        return null;
    }

    @Nullable
    public static Vec3d getMartEntityBBPos() {
        if (gs.getLockingEntity() == markEntity && markEntity != null) {
            return new Vec3d((double)screenPosBB.get(0), (double)screenPosBB.get(1), (double)screenPosBB.get(2));
        }
        return null;
    }

    public void initWeaponParam(EntityPlayer player) {
        reloadCount = 0;
        weaponMode = 0;
        selectedZoom = 0;
    }

    @Override
    public void updateKeybind(MCH_Config config) {
        this.KeyAttack = new MCH_Key(MCH_Config.KeyAttack.prmInt);
        this.KeyUseWeapon = new MCH_Key(MCH_Config.KeyUseWeapon.prmInt);
        this.KeySwWeaponMode = new MCH_Key(MCH_Config.KeySwWeaponMode.prmInt);
        this.KeyZoom = new MCH_Key(MCH_Config.KeyZoom.prmInt);
        this.KeyCameraMode = new MCH_Key(MCH_Config.KeyCameraMode.prmInt);
        this.Keys = new MCH_Key[]{this.KeyAttack, this.KeyUseWeapon, this.KeySwWeaponMode, this.KeyZoom, this.KeyCameraMode};
    }

    @Override
    protected void onTick(boolean inGUI) {
        ItemStack is;
        for (MCH_Key k : this.Keys) {
            k.update();
        }
        this.isBeforeHeldItem = this.isHeldItem;
        EntityPlayerSP player = this.mc.field_71439_g;
        if (this.prevThePlayer == null || this.prevThePlayer != player) {
            this.initWeaponParam((EntityPlayer)player);
            this.prevThePlayer = player;
        }
        ItemStack itemStack = is = player != null ? player.func_184614_ca() : ItemStack.field_190927_a;
        if (player == null || player.func_184187_bx() instanceof MCH_EntityGLTD || player.func_184187_bx() instanceof MCH_EntityAircraft) {
            is = ItemStack.field_190927_a;
        }
        if (gs.getLockingEntity() == null) {
            markEntity = null;
        }
        if (!is.func_190926_b() && is.func_77973_b() instanceof MCH_ItemLightWeaponBase) {
            MCH_ItemLightWeaponBase lweapon = (MCH_ItemLightWeaponBase)is.func_77973_b();
            if (this.prevItemStack.func_190926_b() || !this.prevItemStack.func_77969_a(is) && !this.prevItemStack.func_77977_a().equals(is.func_77977_a())) {
                this.initWeaponParam((EntityPlayer)player);
                weapon = MCH_WeaponCreator.createWeapon(player.field_70170_p, MCH_ItemLightWeaponBase.getName(is), Vec3d.field_186680_a, 0.0f, 0.0f, null, false);
                if (weapon != null && weapon.getInfo() != null && weapon.getGuidanceSystem() != null) {
                    gs = weapon.getGuidanceSystem();
                }
            }
            if (weapon == null || gs == null) {
                return;
            }
            gs.setWorld(player.field_70170_p);
            MCH_ClientLightWeaponTickHandler.gs.lockRange = lockRange;
            if (player.func_184612_cw() > 10) {
                W_Reflection.setCameraZoom(MCH_ClientLightWeaponTickHandler.weapon.getInfo().zoom[selectedZoom %= MCH_ClientLightWeaponTickHandler.weapon.getInfo().zoom.length]);
            } else {
                W_Reflection.restoreCameraZoom();
            }
            if (is.func_77960_j() < is.func_77958_k()) {
                if (player.func_184612_cw() > 10) {
                    gs.lock((Entity)player);
                    if (gs.getLockCount() > 0) {
                        if (lockonSoundCount > 0) {
                            --lockonSoundCount;
                        } else {
                            lockonSoundCount = 7;
                            if ((lockonSoundCount = (int)((double)lockonSoundCount * (1.0 - (double)((float)gs.getLockCount() / (float)gs.getLockCountMax())))) < 3) {
                                lockonSoundCount = 2;
                            }
                            W_McClient.MOD_playSoundFX("lockon", 1.0f, 1.0f);
                        }
                    }
                } else {
                    W_Reflection.restoreCameraZoom();
                    gs.clearLock();
                }
                reloadCount = 0;
            } else {
                lockonSoundCount = 0;
                if (W_EntityPlayer.hasItem((EntityPlayer)player, lweapon.bullet) && player.func_184605_cv() <= 0) {
                    if (reloadCount == 10) {
                        W_McClient.MOD_playSoundFX("fim92_reload", 1.0f, 1.0f);
                    }
                    if (reloadCount < 40 && ++reloadCount == 40) {
                        this.onCompleteReload();
                    }
                } else {
                    reloadCount = 0;
                }
                gs.clearLock();
            }
            if (!inGUI) {
                this.playerControl((EntityPlayer)player, is, (MCH_ItemLightWeaponBase)is.func_77973_b());
            }
            this.isHeldItem = MCH_ItemLightWeaponBase.isHeld((EntityPlayer)player);
        } else {
            lockonSoundCount = 0;
            reloadCount = 0;
            this.isHeldItem = false;
        }
        if (this.isBeforeHeldItem != this.isHeldItem) {
            MCH_Lib.DbgLog(true, "LWeapon cancel", new Object[0]);
            if (!this.isHeldItem) {
                if (MCH_ClientLightWeaponTickHandler.getPotionNightVisionDuration((EntityPlayer)player) < 250) {
                    MCH_PacketLightWeaponPlayerControl pc = new MCH_PacketLightWeaponPlayerControl();
                    pc.camMode = 1;
                    W_Network.sendToServer(pc);
                    player.func_184589_d(MobEffects.field_76439_r);
                }
                W_Reflection.restoreCameraZoom();
            }
        }
        this.prevItemStack = is;
        gs.update();
    }

    protected void onCompleteReload() {
        MCH_PacketLightWeaponPlayerControl pc = new MCH_PacketLightWeaponPlayerControl();
        pc.cmpReload = 1;
        W_Network.sendToServer(pc);
    }

    protected void playerControl(EntityPlayer player, ItemStack is, MCH_ItemLightWeaponBase item) {
        int prevZoom;
        MCH_PacketLightWeaponPlayerControl pc = new MCH_PacketLightWeaponPlayerControl();
        boolean send = false;
        boolean autoShot = false;
        if (MCH_Config.LWeaponAutoFire.prmBool && is.func_77960_j() < is.func_77958_k() && gs.isLockComplete()) {
            autoShot = true;
        }
        if (this.KeySwWeaponMode.isKeyDown() && MCH_ClientLightWeaponTickHandler.weapon.numMode > 1) {
            weaponMode = (weaponMode + 1) % MCH_ClientLightWeaponTickHandler.weapon.numMode;
            W_McClient.MOD_playSoundFX("pi", 0.5f, 0.9f);
        }
        if (this.KeyAttack.isKeyPress() || autoShot) {
            boolean result = false;
            if (is.func_77960_j() < is.func_77958_k() && gs.isLockComplete()) {
                boolean canFire = true;
                if (weaponMode > 0 && gs.getTargetEntity() != null) {
                    double dx = MCH_ClientLightWeaponTickHandler.gs.getTargetEntity().field_70165_t - player.field_70165_t;
                    double dz = MCH_ClientLightWeaponTickHandler.gs.getTargetEntity().field_70161_v - player.field_70161_v;
                    boolean bl = canFire = Math.sqrt(dx * dx + dz * dz) >= 40.0;
                }
                if (canFire) {
                    pc.useWeapon = true;
                    pc.useWeaponOption1 = W_Entity.getEntityId(MCH_ClientLightWeaponTickHandler.gs.lastLockEntity);
                    pc.useWeaponOption2 = weaponMode;
                    pc.useWeaponPosX = player.field_70165_t;
                    pc.useWeaponPosY = player.field_70163_u + (double)player.func_70047_e();
                    pc.useWeaponPosZ = player.field_70161_v;
                    gs.clearLock();
                    send = true;
                    result = true;
                }
            }
            if (this.KeyAttack.isKeyDown() && !result && player.func_184612_cw() > 5) {
                MCH_ClientLightWeaponTickHandler.playSoundNG();
            }
        }
        if (this.KeyZoom.isKeyDown() && (prevZoom = selectedZoom) != (selectedZoom = (selectedZoom + 1) % MCH_ClientLightWeaponTickHandler.weapon.getInfo().zoom.length)) {
            MCH_ClientLightWeaponTickHandler.playSound("zoom", 0.5f, 1.0f);
        }
        if (this.KeyCameraMode.isKeyDown()) {
            PotionEffect pe = player.func_70660_b(MobEffects.field_76439_r);
            MCH_Lib.DbgLog(true, "LWeapon NV %s", pe != null ? "ON->OFF" : "OFF->ON");
            if (pe != null) {
                player.func_184589_d(MobEffects.field_76439_r);
                pc.camMode = 1;
                send = true;
                W_McClient.MOD_playSoundFX("pi", 0.5f, 0.9f);
            } else if (player.func_184612_cw() > 60) {
                pc.camMode = 2;
                send = true;
                W_McClient.MOD_playSoundFX("pi", 0.5f, 0.9f);
            } else {
                MCH_ClientLightWeaponTickHandler.playSoundNG();
            }
        }
        if (send) {
            W_Network.sendToServer(pc);
        }
    }

    public static int getPotionNightVisionDuration(EntityPlayer player) {
        PotionEffect cpe = player.func_70660_b(MobEffects.field_76439_r);
        return player == null || cpe == null ? 0 : cpe.func_76459_b();
    }

    static {
        markEntity = null;
        markPos = Vec3d.field_186680_a;
        gs = new MCH_WeaponGuidanceSystem();
        lockRange = 120.0;
    }
}

