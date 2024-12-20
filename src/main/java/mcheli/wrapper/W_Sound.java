/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.audio.MovingSound
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.SoundEvent
 */
package mcheli.wrapper;

import mcheli.__helper.MCH_SoundEvents;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class W_Sound
extends MovingSound {
    protected W_Sound(ResourceLocation r, float volume, float pitch, double x, double y, double z) {
        super(MCH_SoundEvents.getSound(r), SoundCategory.MASTER);
        this.setVolumeAndPitch(volume, pitch);
        this.setPosition(x, y, z);
    }

    protected W_Sound(ResourceLocation r, float volume, float pitch) {
        this(MCH_SoundEvents.getSound(r), volume, pitch);
    }

    protected W_Sound(SoundEvent soundEvent, float volume, float pitch) {
        super(soundEvent, SoundCategory.MASTER);
        this.setVolumeAndPitch(volume, pitch);
        Entity entity = W_McClient.getRenderEntity();
        if (entity != null) {
            this.setPosition(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v);
        }
    }

    public void setRepeat(boolean b) {
        this.field_147659_g = b;
    }

    public void setSoundParam(Entity e, float v, float p) {
        this.setPosition(e);
        this.setVolumeAndPitch(v, p);
    }

    public void setVolumeAndPitch(float v, float p) {
        this.setVolume(v);
        this.setPitch(p);
    }

    public void setVolume(float v) {
        this.field_147662_b = v;
    }

    public void setPitch(float p) {
        this.field_147663_c = p;
    }

    public void setPosition(double x, double y, double z) {
        this.field_147660_d = (float)x;
        this.field_147661_e = (float)y;
        this.field_147658_f = (float)z;
    }

    public void setPosition(Entity e) {
        this.setPosition(e.field_70165_t, e.field_70163_u, e.field_70161_v);
    }

    public void func_73660_a() {
    }
}

