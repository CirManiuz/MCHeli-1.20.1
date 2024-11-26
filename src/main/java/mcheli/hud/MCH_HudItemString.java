/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 */
package mcheli.hud;

import java.util.Date;
import mcheli.MCH_Config;
import mcheli.MCH_KeyName;
import mcheli.MCH_Lib;
import mcheli.MCH_MOD;
import mcheli.hud.MCH_HudItem;
import mcheli.hud.MCH_HudItemStringArgs;
import net.minecraft.util.math.MathHelper;

public class MCH_HudItemString
extends MCH_HudItem {
    private final String posX;
    private final String posY;
    private final String format;
    private final MCH_HudItemStringArgs[] args;
    private final boolean isCenteredString;

    public MCH_HudItemString(int fileLine, String posx, String posy, String fmt, String[] arg, boolean centered) {
        super(fileLine);
        this.posX = posx.toLowerCase();
        this.posY = posy.toLowerCase();
        this.format = fmt;
        int len = arg.length < 3 ? 0 : arg.length - 3;
        this.args = new MCH_HudItemStringArgs[len];
        for (int i = 0; i < len; ++i) {
            this.args[i] = MCH_HudItemStringArgs.toArgs(arg[3 + i]);
        }
        this.isCenteredString = centered;
    }

    @Override
    public void execute() {
        int x = (int)(centerX + MCH_HudItemString.calc(this.posX));
        int y = (int)(centerY + MCH_HudItemString.calc(this.posY));
        int worldTime = (int)((MCH_HudItemString.ac.field_70170_p.func_72820_D() + 6000L) % 24000L);
        Date date = new Date();
        Object[] prm = new Object[this.args.length];
        double hp_per = ac.getMaxHP() > 0 ? (double)ac.getHP() / (double)ac.getMaxHP() : 0.0;
        block40: for (int i = 0; i < prm.length; ++i) {
            switch (this.args[i]) {
                case NAME: {
                    prm[i] = MCH_HudItemString.ac.getAcInfo().displayName;
                    continue block40;
                }
                case ALTITUDE: {
                    prm[i] = Altitude;
                    continue block40;
                }
                case DATE: {
                    prm[i] = date;
                    continue block40;
                }
                case MC_THOR: {
                    prm[i] = worldTime / 1000;
                    continue block40;
                }
                case MC_TMIN: {
                    prm[i] = worldTime % 1000 * 36 / 10 / 60;
                    continue block40;
                }
                case MC_TSEC: {
                    prm[i] = worldTime % 1000 * 36 / 10 % 60;
                    continue block40;
                }
                case MAX_HP: {
                    prm[i] = ac.getMaxHP();
                    continue block40;
                }
                case HP: {
                    prm[i] = ac.getHP();
                    continue block40;
                }
                case HP_PER: {
                    prm[i] = hp_per * 100.0;
                    continue block40;
                }
                case POS_X: {
                    prm[i] = MCH_HudItemString.ac.field_70165_t;
                    continue block40;
                }
                case POS_Y: {
                    prm[i] = MCH_HudItemString.ac.field_70163_u;
                    continue block40;
                }
                case POS_Z: {
                    prm[i] = MCH_HudItemString.ac.field_70161_v;
                    continue block40;
                }
                case MOTION_X: {
                    prm[i] = MCH_HudItemString.ac.field_70159_w;
                    continue block40;
                }
                case MOTION_Y: {
                    prm[i] = MCH_HudItemString.ac.field_70181_x;
                    continue block40;
                }
                case MOTION_Z: {
                    prm[i] = MCH_HudItemString.ac.field_70179_y;
                    continue block40;
                }
                case INVENTORY: {
                    prm[i] = ac.func_70302_i_();
                    continue block40;
                }
                case WPN_NAME: {
                    prm[i] = WeaponName;
                    if (CurrentWeapon != null) continue block40;
                    return;
                }
                case WPN_AMMO: {
                    prm[i] = WeaponAmmo;
                    if (CurrentWeapon == null) {
                        return;
                    }
                    if (CurrentWeapon.getAmmoNumMax() > 0) continue block40;
                    return;
                }
                case WPN_RM_AMMO: {
                    prm[i] = WeaponAllAmmo;
                    if (CurrentWeapon == null) {
                        return;
                    }
                    if (CurrentWeapon.getAmmoNumMax() > 0) continue block40;
                    return;
                }
                case RELOAD_PER: {
                    prm[i] = Float.valueOf(ReloadPer);
                    if (CurrentWeapon != null) continue block40;
                    return;
                }
                case RELOAD_SEC: {
                    prm[i] = Float.valueOf(ReloadSec);
                    if (CurrentWeapon != null) continue block40;
                    return;
                }
                case MORTAR_DIST: {
                    prm[i] = Float.valueOf(MortarDist);
                    if (CurrentWeapon != null) continue block40;
                    return;
                }
                case MC_VER: {
                    prm[i] = "1.12.2";
                    continue block40;
                }
                case MOD_VER: {
                    prm[i] = MCH_MOD.VER;
                    continue block40;
                }
                case MOD_NAME: {
                    prm[i] = "MC Helicopter MOD";
                    continue block40;
                }
                case YAW: {
                    prm[i] = MCH_Lib.getRotate360(ac.getRotYaw() + 180.0f);
                    continue block40;
                }
                case PITCH: {
                    prm[i] = Float.valueOf(-ac.getRotPitch());
                    continue block40;
                }
                case ROLL: {
                    prm[i] = Float.valueOf(MathHelper.func_76142_g((float)ac.getRotRoll()));
                    continue block40;
                }
                case PLYR_YAW: {
                    prm[i] = MCH_Lib.getRotate360(MCH_HudItemString.player.field_70177_z + 180.0f);
                    continue block40;
                }
                case PLYR_PITCH: {
                    prm[i] = Float.valueOf(-MCH_HudItemString.player.field_70125_A);
                    continue block40;
                }
                case TVM_POS_X: {
                    prm[i] = TVM_PosX;
                    continue block40;
                }
                case TVM_POS_Y: {
                    prm[i] = TVM_PosY;
                    continue block40;
                }
                case TVM_POS_Z: {
                    prm[i] = TVM_PosZ;
                    continue block40;
                }
                case TVM_DIFF: {
                    prm[i] = TVM_Diff;
                    continue block40;
                }
                case CAM_ZOOM: {
                    prm[i] = Float.valueOf(MCH_HudItemString.ac.camera.getCameraZoom());
                    continue block40;
                }
                case UAV_DIST: {
                    prm[i] = UAV_Dist;
                    continue block40;
                }
                case KEY_GUI: {
                    prm[i] = MCH_KeyName.getDescOrName(MCH_Config.KeyGUI.prmInt);
                    continue block40;
                }
                case THROTTLE: {
                    prm[i] = ac.getCurrentThrottle() * 100.0;
                    continue block40;
                }
            }
        }
        if (this.isCenteredString) {
            this.drawCenteredString(String.format(this.format, prm), x, y, colorSetting);
        } else {
            this.drawString(String.format(this.format, prm), x, y, colorSetting);
        }
    }
}

