/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.material.MaterialLogic
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.Item$ToolMaterial
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  net.minecraftforge.oredict.OreDictionary
 */
package mcheli.tool;

import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Random;
import mcheli.MCH_MOD;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.wrapper.W_Item;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLogic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class MCH_ItemWrench
extends W_Item {
    private float damageVsEntity;
    private final Item.ToolMaterial toolMaterial;
    private static Random rand = new Random();

    public MCH_ItemWrench(int itemId, Item.ToolMaterial material) {
        super(itemId);
        this.toolMaterial = material;
        this.field_77777_bU = 1;
        this.func_77656_e(material.func_77997_a());
        this.damageVsEntity = 4.0f + material.func_78000_c();
    }

    public boolean func_150897_b(IBlockState blockIn) {
        Material material = blockIn.func_185904_a();
        if (material == Material.field_151573_f) {
            return true;
        }
        return material instanceof MaterialLogic;
    }

    public float func_150893_a(ItemStack stack, IBlockState state) {
        Material material = state.func_185904_a();
        if (material == Material.field_151573_f) {
            return 20.5f;
        }
        if (material instanceof MaterialLogic) {
            return 5.5f;
        }
        return 2.0f;
    }

    public static float getUseAnimSmooth(ItemStack stack, float partialTicks) {
        int i = Math.abs(MCH_ItemWrench.getUseAnimCount(stack) - 8);
        int j = Math.abs(MCH_ItemWrench.getUseAnimPrevCount(stack) - 8);
        return (float)j + (float)(i - j) * partialTicks;
    }

    public static int getUseAnimPrevCount(ItemStack stack) {
        return MCH_ItemWrench.getAnimCount(stack, "MCH_WrenchAnimPrev");
    }

    public static int getUseAnimCount(ItemStack stack) {
        return MCH_ItemWrench.getAnimCount(stack, "MCH_WrenchAnim");
    }

    public static void setUseAnimCount(ItemStack stack, int n, int prev) {
        MCH_ItemWrench.setAnimCount(stack, "MCH_WrenchAnim", n);
        MCH_ItemWrench.setAnimCount(stack, "MCH_WrenchAnimPrev", prev);
    }

    public static int getAnimCount(ItemStack stack, String name) {
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        if (stack.func_77978_p().func_74764_b(name)) {
            return stack.func_77978_p().func_74762_e(name);
        }
        stack.func_77978_p().func_74768_a(name, 0);
        return 0;
    }

    public static void setAnimCount(ItemStack stack, String name, int n) {
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        stack.func_77978_p().func_74768_a(name, n);
    }

    public boolean func_77644_a(ItemStack itemStack, EntityLivingBase entity, EntityLivingBase player) {
        if (!player.field_70170_p.field_72995_K) {
            if (rand.nextInt(40) == 0) {
                entity.func_70099_a(new ItemStack(W_Item.getItemByName("iron_ingot"), 1, 0), 0.0f);
            } else if (rand.nextInt(20) == 0) {
                entity.func_70099_a(new ItemStack(W_Item.getItemByName("gunpowder"), 1, 0), 0.0f);
            }
        }
        itemStack.func_77972_a(2, player);
        return true;
    }

    public void func_77615_a(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        MCH_ItemWrench.setUseAnimCount(stack, 0, 0);
    }

    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        MCH_EntityAircraft ac;
        if (player.field_70170_p.field_72995_K && (ac = this.getMouseOverAircraft(player)) != null) {
            int cnt;
            int prev = cnt = MCH_ItemWrench.getUseAnimCount(stack);
            cnt = cnt <= 0 ? 16 : --cnt;
            MCH_ItemWrench.setUseAnimCount(stack, cnt, prev);
        }
        if (!player.field_70170_p.field_72995_K && count < this.func_77626_a(stack) && count % 20 == 0 && (ac = this.getMouseOverAircraft(player)) != null && ac.getHP() > 0 && ac.repair(10)) {
            stack.func_77972_a(1, player);
            W_WorldFunc.MOD_playSoundEffect(player.field_70170_p, (int)ac.field_70165_t, (int)ac.field_70163_u, (int)ac.field_70161_v, "wrench", 1.0f, 0.9f + rand.nextFloat() * 0.2f);
        }
    }

    public void func_77663_a(ItemStack item, World world, Entity entity, int n, boolean b) {
        EntityPlayer player;
        ItemStack itemStack;
        if (entity instanceof EntityPlayer && (itemStack = (player = (EntityPlayer)entity).func_184614_ca()) == item) {
            MCH_MOD.proxy.setCreativeDigDelay(0);
        }
    }

    public MCH_EntityAircraft getMouseOverAircraft(EntityLivingBase entity) {
        RayTraceResult m = this.getMouseOver(entity, 1.0f);
        MCH_EntityAircraft ac = null;
        if (m != null) {
            MCH_EntitySeat seat;
            if (m.field_72308_g instanceof MCH_EntityAircraft) {
                ac = (MCH_EntityAircraft)m.field_72308_g;
            } else if (m.field_72308_g instanceof MCH_EntitySeat && (seat = (MCH_EntitySeat)m.field_72308_g).getParent() != null) {
                ac = seat.getParent();
            }
        }
        return ac;
    }

    private static RayTraceResult rayTrace(EntityLivingBase entity, double dist, float tick) {
        Vec3d vec3 = new Vec3d(entity.field_70165_t, entity.field_70163_u + (double)entity.func_70047_e(), entity.field_70161_v);
        Vec3d vec31 = entity.func_70676_i(tick);
        Vec3d vec32 = vec3.func_72441_c(vec31.field_72450_a * dist, vec31.field_72448_b * dist, vec31.field_72449_c * dist);
        return entity.field_70170_p.func_147447_a(vec3, vec32, false, false, true);
    }

    private RayTraceResult getMouseOver(EntityLivingBase user, float tick) {
        Entity pointedEntity = null;
        double d0 = 4.0;
        RayTraceResult objectMouseOver = MCH_ItemWrench.rayTrace(user, d0, tick);
        double d1 = d0;
        Vec3d vec3 = new Vec3d(user.field_70165_t, user.field_70163_u + (double)user.func_70047_e(), user.field_70161_v);
        if (objectMouseOver != null) {
            d1 = objectMouseOver.field_72307_f.func_72438_d(vec3);
        }
        Vec3d vec31 = user.func_70676_i(tick);
        Vec3d vec32 = vec3.func_72441_c(vec31.field_72450_a * d0, vec31.field_72448_b * d0, vec31.field_72449_c * d0);
        pointedEntity = null;
        Vec3d vec33 = null;
        float f1 = 1.0f;
        List list = user.field_70170_p.func_72839_b((Entity)user, user.func_174813_aQ().func_72321_a(vec31.field_72450_a * d0, vec31.field_72448_b * d0, vec31.field_72449_c * d0).func_72314_b((double)f1, (double)f1, (double)f1));
        double d2 = d1;
        for (int i = 0; i < list.size(); ++i) {
            double d3;
            Entity entity = (Entity)list.get(i);
            if (!entity.func_70067_L()) continue;
            float f2 = entity.func_70111_Y();
            AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_72314_b((double)f2, (double)f2, (double)f2);
            RayTraceResult movingobjectposition = axisalignedbb.func_72327_a(vec3, vec32);
            if (axisalignedbb.func_72318_a(vec3)) {
                if (!(0.0 < d2) && d2 != 0.0) continue;
                pointedEntity = entity;
                vec33 = movingobjectposition == null ? vec3 : movingobjectposition.field_72307_f;
                d2 = 0.0;
                continue;
            }
            if (movingobjectposition == null || !((d3 = vec3.func_72438_d(movingobjectposition.field_72307_f)) < d2) && d2 != 0.0) continue;
            if (entity == user.func_184187_bx() && !entity.canRiderInteract()) {
                if (d2 != 0.0) continue;
                pointedEntity = entity;
                vec33 = movingobjectposition.field_72307_f;
                continue;
            }
            pointedEntity = entity;
            vec33 = movingobjectposition.field_72307_f;
            d2 = d3;
        }
        if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
            objectMouseOver = new RayTraceResult(pointedEntity, vec33);
        }
        return objectMouseOver;
    }

    public boolean func_179218_a(ItemStack itemStack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        if ((double)state.func_185887_b(world, pos) != 0.0) {
            itemStack.func_77972_a(2, entity);
        }
        return true;
    }

    @SideOnly(value=Side.CLIENT)
    public boolean func_77662_d() {
        return true;
    }

    public EnumAction func_77661_b(ItemStack itemStack) {
        return EnumAction.BLOCK;
    }

    public int func_77626_a(ItemStack itemStack) {
        return 72000;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
        player.func_184598_c(handIn);
        return ActionResult.newResult((EnumActionResult)EnumActionResult.SUCCESS, (Object)player.func_184586_b(handIn));
    }

    public int func_77619_b() {
        return this.toolMaterial.func_77995_e();
    }

    public String getToolMaterialName() {
        return this.toolMaterial.toString();
    }

    public boolean func_82789_a(ItemStack item1, ItemStack item2) {
        ItemStack mat = this.toolMaterial.getRepairItemStack();
        if (!mat.func_190926_b() && OreDictionary.itemMatches((ItemStack)mat, (ItemStack)item2, (boolean)false)) {
            return true;
        }
        return super.func_82789_a(item1, item2);
    }

    public Multimap<String, AttributeModifier> func_111205_h(EntityEquipmentSlot equipmentSlot) {
        Multimap multimap = super.func_111205_h(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put((Object)SharedMonsterAttributes.field_111264_e.func_111108_a(), (Object)new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.damageVsEntity, 0));
        }
        return multimap;
    }
}

