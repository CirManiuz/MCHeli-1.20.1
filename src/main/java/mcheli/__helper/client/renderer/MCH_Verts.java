/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.client.renderer.vertex.VertexFormatElement
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumType
 *  net.minecraft.client.renderer.vertex.VertexFormatElement$EnumUsage
 */
package mcheli.__helper.client.renderer;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class MCH_Verts {
    public static final VertexFormatElement TEX_2S = new VertexFormatElement(0, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
    public static final VertexFormat POS_COLOR_LMAP = new VertexFormat().func_181721_a(DefaultVertexFormats.field_181713_m).func_181721_a(DefaultVertexFormats.field_181714_n).func_181721_a(TEX_2S);
}

