/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.client;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface _IModelCustom {
    public String getType();

    @SideOnly(value=Side.CLIENT)
    public void renderAll();

    @SideOnly(value=Side.CLIENT)
    public void renderOnly(String ... var1);

    @SideOnly(value=Side.CLIENT)
    public void renderPart(String var1);

    @SideOnly(value=Side.CLIENT)
    public void renderAllExcept(String ... var1);
}

