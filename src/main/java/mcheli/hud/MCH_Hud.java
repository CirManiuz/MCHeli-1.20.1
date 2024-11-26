/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 */
package mcheli.hud;

import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_BaseInfo;
import mcheli.MCH_Lib;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.__helper.info.ContentParseException;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.hud.MCH_HudItem;
import mcheli.hud.MCH_HudItemCall;
import mcheli.hud.MCH_HudItemCameraRot;
import mcheli.hud.MCH_HudItemColor;
import mcheli.hud.MCH_HudItemConditional;
import mcheli.hud.MCH_HudItemExit;
import mcheli.hud.MCH_HudItemGraduation;
import mcheli.hud.MCH_HudItemLine;
import mcheli.hud.MCH_HudItemLineStipple;
import mcheli.hud.MCH_HudItemRadar;
import mcheli.hud.MCH_HudItemRect;
import mcheli.hud.MCH_HudItemString;
import mcheli.hud.MCH_HudItemTexture;
import mcheli.wrapper.W_ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_Hud
extends MCH_BaseInfo {
    public static final MCH_Hud NoDisp = new MCH_Hud(MCH_Utils.buildinAddon("none"), "none");
    public final String name;
    public final String fileName;
    private List<MCH_HudItem> list;
    public boolean isWaitEndif;
    private boolean isDrawing;
    public boolean isIfFalse;
    public boolean exit;

    public MCH_Hud(AddonResourceLocation location, String filePath) {
        super(location, filePath);
        this.name = location.func_110623_a();
        this.fileName = filePath;
        this.list = new ArrayList<MCH_HudItem>();
        this.isDrawing = false;
        this.isIfFalse = false;
        this.exit = false;
    }

    @Override
    public boolean validate() throws Exception {
        for (MCH_HudItem hud : this.list) {
            hud.parent = this;
        }
        if (this.isWaitEndif) {
            throw new RuntimeException("Endif not found!");
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void loadItemData(int fileLine, String item, String data) {
        String[] prm = data.split("\\s*,\\s*");
        if (prm == null || prm.length == 0) {
            return;
        }
        if (item.equalsIgnoreCase("If")) {
            if (this.isWaitEndif) {
                throw new RuntimeException("Endif not found!");
            }
            this.list.add(new MCH_HudItemConditional(fileLine, false, prm[0]));
            this.isWaitEndif = true;
            return;
        } else if (item.equalsIgnoreCase("Endif")) {
            if (!this.isWaitEndif) throw new RuntimeException("IF in a pair can not be found!");
            this.list.add(new MCH_HudItemConditional(fileLine, true, ""));
            this.isWaitEndif = false;
            return;
        } else if (item.equalsIgnoreCase("DrawString") || item.equalsIgnoreCase("DrawCenteredString")) {
            String s;
            if (prm.length < 3 || (s = prm[2]).charAt(0) != '\"' || s.charAt(s.length() - 1) != '\"') return;
            s = s.substring(1, s.length() - 1);
            this.list.add(new MCH_HudItemString(fileLine, prm[0], prm[1], s, prm, item.equalsIgnoreCase("DrawCenteredString")));
            return;
        } else if (item.equalsIgnoreCase("Exit")) {
            this.list.add(new MCH_HudItemExit(fileLine));
            return;
        } else if (item.equalsIgnoreCase("Color")) {
            if (prm.length == 1) {
                MCH_HudItemColor c = MCH_HudItemColor.createByParams(fileLine, new String[]{prm[0]});
                if (c == null) return;
                this.list.add(c);
                return;
            } else {
                String[] s;
                MCH_HudItemColor c;
                if (prm.length != 4 || (c = MCH_HudItemColor.createByParams(fileLine, s = new String[]{prm[0], prm[1], prm[2], prm[3]})) == null) return;
                this.list.add(c);
            }
            return;
        } else if (item.equalsIgnoreCase("DrawTexture")) {
            if (prm.length < 9 || prm.length > 10) return;
            String rot = prm.length == 10 ? prm[9] : "0";
            this.list.add(new MCH_HudItemTexture(fileLine, prm[0], prm[1], prm[2], prm[3], prm[4], prm[5], prm[6], prm[7], prm[8], rot));
            return;
        } else if (item.equalsIgnoreCase("DrawRect")) {
            if (prm.length != 4) return;
            this.list.add(new MCH_HudItemRect(fileLine, prm[0], prm[1], prm[2], prm[3]));
            return;
        } else if (item.equalsIgnoreCase("DrawLine")) {
            int len = prm.length;
            if (len < 4 || len % 2 != 0) return;
            this.list.add(new MCH_HudItemLine(fileLine, prm));
            return;
        } else if (item.equalsIgnoreCase("DrawLineStipple")) {
            int len = prm.length;
            if (len < 6 || len % 2 != 0) return;
            this.list.add(new MCH_HudItemLineStipple(fileLine, prm));
            return;
        } else if (item.equalsIgnoreCase("Call")) {
            int len = prm.length;
            if (len != 1) return;
            this.list.add(new MCH_HudItemCall(fileLine, prm[0]));
            return;
        } else if (item.equalsIgnoreCase("DrawEntityRadar") || item.equalsIgnoreCase("DrawEnemyRadar")) {
            if (prm.length != 5) return;
            this.list.add(new MCH_HudItemRadar(fileLine, item.equalsIgnoreCase("DrawEntityRadar"), prm[0], prm[1], prm[2], prm[3], prm[4]));
            return;
        } else if (item.equalsIgnoreCase("DrawGraduationYaw") || item.equalsIgnoreCase("DrawGraduationPitch1") || item.equalsIgnoreCase("DrawGraduationPitch2") || item.equalsIgnoreCase("DrawGraduationPitch3")) {
            if (prm.length != 4) return;
            int type = -1;
            if (item.equalsIgnoreCase("DrawGraduationYaw")) {
                type = 0;
            }
            if (item.equalsIgnoreCase("DrawGraduationPitch1")) {
                type = 1;
            }
            if (item.equalsIgnoreCase("DrawGraduationPitch2")) {
                type = 2;
            }
            if (item.equalsIgnoreCase("DrawGraduationPitch3")) {
                type = 3;
            }
            this.list.add(new MCH_HudItemGraduation(fileLine, type, prm[0], prm[1], prm[2], prm[3]));
            return;
        } else {
            if (!item.equalsIgnoreCase("DrawCameraRot") || prm.length != 2) return;
            this.list.add(new MCH_HudItemCameraRot(fileLine, prm[0], prm[1]));
        }
    }

    public void draw(MCH_EntityAircraft ac, EntityPlayer player, float partialTicks) {
        if (MCH_HudItem.mc == null) {
            MCH_HudItem.mc = Minecraft.func_71410_x();
        }
        MCH_HudItem.ac = ac;
        MCH_HudItem.player = player;
        MCH_HudItem.partialTicks = partialTicks;
        W_ScaledResolution scaledresolution = new W_ScaledResolution(MCH_HudItem.mc, MCH_HudItem.mc.field_71443_c, MCH_HudItem.mc.field_71440_d);
        MCH_HudItem.scaleFactor = scaledresolution.func_78325_e();
        if (MCH_HudItem.scaleFactor <= 0) {
            MCH_HudItem.scaleFactor = 1;
        }
        MCH_HudItem.width = MCH_HudItem.mc.field_71443_c / MCH_HudItem.scaleFactor;
        MCH_HudItem.height = MCH_HudItem.mc.field_71440_d / MCH_HudItem.scaleFactor;
        MCH_HudItem.centerX = MCH_HudItem.width / 2.0;
        MCH_HudItem.centerY = MCH_HudItem.height / 2.0;
        this.isIfFalse = false;
        this.isDrawing = false;
        this.exit = false;
        if (ac != null && ac.getAcInfo() != null && player != null) {
            MCH_HudItem.update();
            this.drawItems();
            MCH_HudItem.drawVarMap();
        }
    }

    protected void drawItems() {
        if (!this.isDrawing) {
            this.isDrawing = true;
            for (MCH_HudItem hud : this.list) {
                int line = -1;
                try {
                    line = hud.fileLine;
                    if (!hud.canExecute()) continue;
                    hud.execute();
                    if (!this.exit) continue;
                    break;
                }
                catch (Exception e) {
                    MCH_Lib.Log("#### Draw HUD Error!!!: line=%d, file=%s", line, this.fileName);
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            this.exit = false;
            this.isIfFalse = false;
            this.isDrawing = false;
        }
    }

    @Override
    public void parse(List<String> lines, String fileExtension, boolean reload) throws Exception {
        if ("txt".equals(fileExtension)) {
            int line = 0;
            try {
                for (String str : lines) {
                    int eqIdx;
                    ++line;
                    if ((str = str.trim()).equalsIgnoreCase("endif")) {
                        str = "endif=0";
                    }
                    if (str.equalsIgnoreCase("exit")) {
                        str = "exit=0";
                    }
                    if ((eqIdx = str.indexOf(61)) < 0 || str.length() <= eqIdx + 1) continue;
                    this.loadItemData(line, str.substring(0, eqIdx).trim().toLowerCase(), str.substring(eqIdx + 1).trim());
                }
            }
            catch (Exception e) {
                throw new ContentParseException(e, line);
            }
        }
    }
}

