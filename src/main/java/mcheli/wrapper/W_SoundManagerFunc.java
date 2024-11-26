/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.SoundManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 */
package mcheli.wrapper;

import mcheli.wrapper.W_MOD;
import mcheli.wrapper.W_Sound;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class W_SoundManagerFunc {
    public static void DEF_playEntitySound(SoundManager sm, String name, Entity entity, float volume, float pitch, boolean par5) {
        sm.func_148611_c((ISound)new W_Sound(new ResourceLocation(name), volume, pitch, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
    }

    public static void MOD_playEntitySound(SoundManager sm, String name, Entity entity, float volume, float pitch, boolean par5) {
        W_SoundManagerFunc.DEF_playEntitySound(sm, W_MOD.DOMAIN + ":" + name, entity, volume, pitch, par5);
    }
}

