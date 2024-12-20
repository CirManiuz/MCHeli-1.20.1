/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.crafting.IRecipe
 */
package mcheli.throwable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mcheli.MCH_BaseInfo;
import mcheli.MCH_Color;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.__helper.client._IModelCustom;
import mcheli.__helper.info.IItemContent;
import mcheli.wrapper.W_Item;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

public class MCH_ThrowableInfo
extends MCH_BaseInfo
implements IItemContent {
    public final String name;
    public String displayName;
    public HashMap<String, String> displayNameLang;
    public int itemID;
    public W_Item item;
    public List<String> recipeString;
    public List<IRecipe> recipe;
    public boolean isShapedRecipe;
    public int power;
    public float acceleration;
    public float accelerationInWater;
    public float dispenseAcceleration;
    public int explosion;
    public int delayFuse;
    public float bound;
    public int timeFuse;
    public boolean flaming;
    public int stackSize;
    public float soundVolume;
    public float soundPitch;
    public float proximityFuseDist;
    public float accuracy;
    public int aliveTime;
    public int bomblet;
    public float bombletDiff;
    public _IModelCustom model;
    public float smokeSize;
    public int smokeNum;
    public float smokeVelocityVertical;
    public float smokeVelocityHorizontal;
    public float gravity;
    public float gravityInWater;
    public String particleName;
    public boolean disableSmoke;
    public MCH_Color smokeColor;

    public MCH_ThrowableInfo(AddonResourceLocation location, String path) {
        super(location, path);
        this.name = location.func_110623_a();
        this.displayName = location.func_110623_a();
        this.displayNameLang = new HashMap();
        this.itemID = 0;
        this.item = null;
        this.recipeString = new ArrayList<String>();
        this.recipe = new ArrayList<IRecipe>();
        this.isShapedRecipe = true;
        this.power = 0;
        this.acceleration = 1.0f;
        this.accelerationInWater = 1.0f;
        this.dispenseAcceleration = 1.0f;
        this.explosion = 0;
        this.delayFuse = 0;
        this.bound = 0.2f;
        this.timeFuse = 0;
        this.flaming = false;
        this.stackSize = 1;
        this.soundVolume = 1.0f;
        this.soundPitch = 1.0f;
        this.proximityFuseDist = 0.0f;
        this.accuracy = 0.0f;
        this.aliveTime = 10;
        this.bomblet = 0;
        this.bombletDiff = 0.3f;
        this.model = null;
        this.smokeSize = 10.0f;
        this.smokeNum = 0;
        this.smokeVelocityVertical = 1.0f;
        this.smokeVelocityHorizontal = 1.0f;
        this.gravity = 0.0f;
        this.gravityInWater = -0.04f;
        this.particleName = "explode";
        this.disableSmoke = true;
        this.smokeColor = new MCH_Color();
    }

    @Override
    public boolean validate() throws Exception {
        this.timeFuse *= 20;
        this.aliveTime *= 20;
        this.delayFuse *= 20;
        return super.validate();
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public void loadItemData(String item, String data) {
        String[] s;
        if (item.compareTo("displayname") == 0) {
            this.displayName = data;
        } else if (item.compareTo("adddisplayname") == 0) {
            String[] s2 = data.split("\\s*,\\s*");
            if (s2 != null && s2.length == 2) {
                this.displayNameLang.put(s2[0].trim(), s2[1].trim());
            }
        } else if (item.compareTo("itemid") == 0) {
            this.itemID = this.toInt(data, 0, 65535);
        } else if (item.compareTo("addrecipe") == 0 || item.compareTo("addshapelessrecipe") == 0) {
            this.isShapedRecipe = item.compareTo("addrecipe") == 0;
            this.recipeString.add(data.toUpperCase());
        } else if (item.compareTo("power") == 0) {
            this.power = this.toInt(data);
        } else if (item.compareTo("acceleration") == 0) {
            this.acceleration = this.toFloat(data, 0.0f, 100.0f);
        } else if (item.compareTo("accelerationinwater") == 0) {
            this.accelerationInWater = this.toFloat(data, 0.0f, 100.0f);
        } else if (item.equalsIgnoreCase("DispenseAcceleration")) {
            this.dispenseAcceleration = this.toFloat(data, 0.0f, 1000.0f);
        } else if (item.compareTo("explosion") == 0) {
            this.explosion = this.toInt(data, 0, 50);
        } else if (item.equalsIgnoreCase("DelayFuse")) {
            this.delayFuse = this.toInt(data, 0, 100000);
        } else if (item.equalsIgnoreCase("Bound")) {
            this.bound = this.toFloat(data, 0.0f, 100000.0f);
        } else if (item.equalsIgnoreCase("TimeFuse")) {
            this.timeFuse = this.toInt(data, 0, 100000);
        } else if (item.compareTo("flaming") == 0) {
            this.flaming = this.toBool(data);
        } else if (item.equalsIgnoreCase("StackSize")) {
            this.stackSize = this.toInt(data, 1, 64);
        } else if (item.compareTo("soundvolume") == 0) {
            this.soundVolume = this.toFloat(data, 0.0f, 1000.0f);
        } else if (item.compareTo("soundpitch") == 0) {
            this.soundPitch = this.toFloat(data, 0.0f, 1.0f);
        } else if (item.compareTo("proximityfusedist") == 0) {
            this.proximityFuseDist = this.toFloat(data, 0.0f, 20.0f);
        } else if (item.compareTo("accuracy") == 0) {
            this.accuracy = this.toFloat(data, 0.0f, 1000.0f);
        } else if (item.equalsIgnoreCase("aliveTime")) {
            this.aliveTime = this.toInt(data, 0, 1000000);
        } else if (item.compareTo("bomblet") == 0) {
            this.bomblet = this.toInt(data, 0, 1000);
        } else if (item.equalsIgnoreCase("BombletDiff")) {
            this.bombletDiff = this.toFloat(data, 0.0f, 1000.0f);
        } else if (item.equalsIgnoreCase("SmokeSize")) {
            this.smokeSize = this.toFloat(data, 0.0f, 1000.0f);
        } else if (item.equalsIgnoreCase("SmokeNum")) {
            this.smokeNum = this.toInt(data, 0, 1000);
        } else if (item.equalsIgnoreCase("SmokeVelocityVertical")) {
            this.smokeVelocityVertical = this.toFloat(data, -100.0f, 100.0f);
        } else if (item.equalsIgnoreCase("SmokeVelocityHorizontal")) {
            this.smokeVelocityHorizontal = this.toFloat(data, 0.0f, 1000.0f);
        } else if (item.compareTo("gravity") == 0) {
            this.gravity = this.toFloat(data, -50.0f, 50.0f);
        } else if (item.equalsIgnoreCase("gravityInWater")) {
            this.gravityInWater = this.toFloat(data, -50.0f, 50.0f);
        } else if (item.compareTo("particle") == 0) {
            this.particleName = data.toLowerCase().trim();
            if (this.particleName.equalsIgnoreCase("none")) {
                this.particleName = "";
            }
        } else if (item.equalsIgnoreCase("DisableSmoke")) {
            this.disableSmoke = this.toBool(data);
        } else if (item.equalsIgnoreCase("SmokeColor") && (s = data.split("\\s*,\\s*")).length >= 3) {
            this.smokeColor = new MCH_Color(1.0f, 0.003921569f * (float)this.toInt(s[0], 0, 255), 0.003921569f * (float)this.toInt(s[1], 0, 255), 0.003921569f * (float)this.toInt(s[2], 0, 255));
        }
    }

    public class RoundItem {
        public final int num;
        public final Item item;

        public RoundItem(MCH_ThrowableInfo paramMCH_ThrowableInfo, int n, Item i) {
            this.num = n;
            this.item = i;
        }
    }
}

