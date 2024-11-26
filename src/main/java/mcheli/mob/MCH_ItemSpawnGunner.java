/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextComponentTranslation
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.mob;

import java.util.List;
import mcheli.MCH_MOD;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.mob.MCH_EntityGunner;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_ItemSpawnGunner
extends W_Item {
    public int primaryColor = 0xFFFFFF;
    public int secondaryColor = 0xFFFFFF;
    public int targetType = 0;

    public MCH_ItemSpawnGunner() {
        this.field_77777_bU = 1;
        this.func_77637_a(CreativeTabs.field_78029_e);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
        MCH_EntityAircraft ac;
        ItemStack itemstack = player.func_184586_b(handIn);
        float f = 1.0f;
        float pitch = player.field_70127_C + (player.field_70125_A - player.field_70127_C) * f;
        float yaw = player.field_70126_B + (player.field_70177_z - player.field_70126_B) * f;
        double dx = player.field_70169_q + (player.field_70165_t - player.field_70169_q) * (double)f;
        double dy = player.field_70167_r + (player.field_70163_u - player.field_70167_r) * (double)f + (double)player.func_70047_e();
        double dz = player.field_70166_s + (player.field_70161_v - player.field_70166_s) * (double)f;
        Vec3d vec3 = new Vec3d(dx, dy, dz);
        float f3 = MathHelper.func_76134_b((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI));
        float f5 = -MathHelper.func_76134_b((float)(-pitch * ((float)Math.PI / 180)));
        float f6 = MathHelper.func_76126_a((float)(-pitch * ((float)Math.PI / 180)));
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0;
        Vec3d vec31 = vec3.func_72441_c((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        List list = world.func_72872_a(MCH_EntityGunner.class, player.func_174813_aQ().func_72314_b(5.0, 5.0, 5.0));
        Object target = null;
        for (int i = 0; i < list.size(); ++i) {
            MCH_EntityGunner gunner = (MCH_EntityGunner)((Object)list.get(i));
            if (gunner.func_174813_aQ().func_72327_a(vec3, vec31) == null || target != null && !(player.func_70068_e((Entity)gunner) < player.func_70068_e((Entity)target))) continue;
            target = gunner;
        }
        if (target == null) {
            List list1 = world.func_72872_a(MCH_EntitySeat.class, player.func_174813_aQ().func_72314_b(5.0, 5.0, 5.0));
            for (int i = 0; i < list1.size(); ++i) {
                MCH_EntitySeat seat = (MCH_EntitySeat)list1.get(i);
                if (seat.getParent() == null || seat.getParent().getAcInfo() == null || seat.func_174813_aQ().func_72327_a(vec3, vec31) == null || target != null && !(player.func_70068_e((Entity)seat) < player.func_70068_e((Entity)target))) continue;
                target = seat.getRiddenByEntity() instanceof MCH_EntityGunner ? seat.getRiddenByEntity() : seat;
            }
        }
        if (target == null) {
            List list2 = world.func_72872_a(MCH_EntityAircraft.class, player.func_174813_aQ().func_72314_b(5.0, 5.0, 5.0));
            for (int i = 0; i < list2.size(); ++i) {
                ac = (MCH_EntityAircraft)list2.get(i);
                if (ac.isUAV() || ac.getAcInfo() == null || ac.func_174813_aQ().func_72327_a(vec3, vec31) == null || target != null && !(player.func_70068_e((Entity)ac) < player.func_70068_e((Entity)target))) continue;
                target = ac.getRiddenByEntity() instanceof MCH_EntityGunner ? ac.getRiddenByEntity() : ac;
            }
        }
        if (target instanceof MCH_EntityGunner) {
            target.func_184230_a(player, handIn);
            return ActionResult.newResult((EnumActionResult)EnumActionResult.SUCCESS, (Object)itemstack);
        }
        if (this.targetType == 1 && !world.field_72995_K && player.func_96124_cp() == null) {
            player.func_145747_a((ITextComponent)new TextComponentString("You are not on team."));
            return ActionResult.newResult((EnumActionResult)EnumActionResult.FAIL, (Object)itemstack);
        }
        if (target == null) {
            if (!world.field_72995_K) {
                player.func_145747_a((ITextComponent)new TextComponentString("Right click to seat."));
            }
            return ActionResult.newResult((EnumActionResult)EnumActionResult.FAIL, (Object)itemstack);
        }
        if (!world.field_72995_K) {
            MCH_EntityGunner gunner = new MCH_EntityGunner(world, ((Entity)target).field_70165_t, ((Entity)target).field_70163_u, ((Entity)target).field_70161_v);
            gunner.field_70177_z = ((MathHelper.func_76128_c((double)((double)(player.field_70177_z * 4.0f / 360.0f) + 0.5)) & 3) - 1) * 90;
            gunner.isCreative = player.field_71075_bZ.field_75098_d;
            gunner.targetType = this.targetType;
            gunner.ownerUUID = player.func_110124_au().toString();
            ScorePlayerTeam team = world.func_96441_U().func_96509_i(player.func_145748_c_().func_150254_d());
            if (team != null) {
                gunner.setTeamName(team.func_96661_b());
            }
            world.func_72838_d((Entity)gunner);
            gunner.func_184220_m((Entity)target);
            W_WorldFunc.MOD_playSoundAtEntity((Entity)gunner, "wrench", 1.0f, 3.0f);
            ac = target instanceof MCH_EntityAircraft ? (MCH_EntityAircraft)target : ((MCH_EntitySeat)target).getParent();
            String teamPlayerName = ScorePlayerTeam.func_96667_a((Team)player.func_96124_cp(), (String)player.func_145748_c_().func_150254_d());
            String displayName = TextFormatting.GOLD + ac.getAcInfo().displayName + TextFormatting.RESET;
            int seatNo = ac.getSeatIdByEntity((Entity)gunner) + 1;
            if (MCH_MOD.isTodaySep01()) {
                String msg = "Hi " + teamPlayerName + "! I sat in the " + seatNo + " seat of " + displayName + "!";
                player.func_145747_a((ITextComponent)new TextComponentTranslation("chat.type.text", new Object[]{"EMB4", new TextComponentString(msg)}));
            } else {
                player.func_145747_a((ITextComponent)new TextComponentString("The gunner was put on " + displayName + " seat " + seatNo + " by " + teamPlayerName));
            }
        }
        if (!player.field_71075_bZ.field_75098_d) {
            itemstack.func_190918_g(1);
        }
        return ActionResult.newResult((EnumActionResult)EnumActionResult.SUCCESS, (Object)itemstack);
    }

    @SideOnly(value=Side.CLIENT)
    public static int getColorFromItemStack(ItemStack stack, int tintIndex) {
        MCH_ItemSpawnGunner item = (MCH_ItemSpawnGunner)stack.func_77973_b();
        return tintIndex == 0 ? item.primaryColor : item.secondaryColor;
    }
}

