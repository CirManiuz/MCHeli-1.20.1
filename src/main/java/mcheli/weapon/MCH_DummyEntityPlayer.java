/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.wrapper.ChatMessageComponent;
import mcheli.wrapper.W_EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class MCH_DummyEntityPlayer
extends W_EntityPlayer {
    public MCH_DummyEntityPlayer(World worldIn, EntityPlayer player) {
        super(worldIn, player);
    }

    public void func_145747_a(ITextComponent var1) {
    }

    public boolean func_70003_b(int var1, String var2) {
        return false;
    }

    public Entity func_174793_f() {
        return super.func_174793_f();
    }

    public boolean func_175149_v() {
        return false;
    }

    public boolean func_184812_l_() {
        return false;
    }

    public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
    }
}

