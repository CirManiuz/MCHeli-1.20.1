/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.command.CommandBase
 *  net.minecraft.command.CommandException
 *  net.minecraft.command.CommandGameMode
 *  net.minecraft.command.ICommand
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.command.SyntaxErrorException
 *  net.minecraft.command.WrongUsageException
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.JsonToNBT
 *  net.minecraft.nbt.NBTException
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.ITextComponent$Serializer
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextComponentTranslation
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.world.World
 *  net.minecraftforge.event.CommandEvent
 *  org.apache.commons.lang3.exception.ExceptionUtils
 */
package mcheli.command;

import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mcheli.MCH_Config;
import mcheli.MCH_MOD;
import mcheli.MCH_PacketNotifyServerSettings;
import mcheli.__helper.MCH_Utils;
import mcheli.command.MCH_PacketTitle;
import mcheli.multiplay.MCH_MultiplayPacketHandler;
import mcheli.multiplay.MCH_PacketIndClient;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandGameMode;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.CommandEvent;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class MCH_Command
extends CommandBase {
    public static final String CMD_GET_SS = "sendss";
    public static final String CMD_MOD_LIST = "modlist";
    public static final String CMD_RECONFIG = "reconfig";
    public static final String CMD_TITLE = "title";
    public static final String CMD_FILL = "fill";
    public static final String CMD_STATUS = "status";
    public static final String CMD_KILL_ENTITY = "killentity";
    public static final String CMD_REMOVE_ENTITY = "removeentity";
    public static final String CMD_ATTACK_ENTITY = "attackentity";
    public static final String CMD_SHOW_BB = "showboundingbox";
    public static final String CMD_DELAY_BB = "delayhitbox";
    public static final String CMD_LIST = "list";
    public static String[] ALL_COMMAND = new String[]{"sendss", "modlist", "reconfig", "title", "fill", "status", "killentity", "removeentity", "attackentity", "showboundingbox", "list", "delayhitbox"};
    public static MCH_Command instance = new MCH_Command();

    public static boolean canUseCommand(Entity player) {
        return player instanceof EntityPlayer ? instance.canCommandSenderUseCommand((ICommandSender)player) : false;
    }

    public String func_71517_b() {
        return "mcheli";
    }

    public static boolean checkCommandPermission(MinecraftServer server, ICommandSender sender, String cmd) {
        if (new CommandGameMode().func_184882_a(server, sender)) {
            return true;
        }
        if (sender instanceof EntityPlayer && cmd.length() > 0) {
            String playerName = ((EntityPlayer)sender).func_146103_bH().getName();
            for (MCH_Config.CommandPermission c : MCH_Config.CommandPermissionList) {
                if (!c.name.equals(cmd)) continue;
                for (String s : c.players) {
                    if (!s.equalsIgnoreCase(playerName)) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static void onCommandEvent(CommandEvent event) {
        if (!(event.getCommand() instanceof MCH_Command)) {
            return;
        }
        if (event.getParameters().length <= 0 || event.getParameters()[0].length() <= 0) {
            event.setCanceled(true);
            return;
        }
        if (!MCH_Command.checkCommandPermission(MCH_Utils.getServer(), event.getSender(), event.getParameters()[0])) {
            event.setCanceled(true);
            TextComponentTranslation c = new TextComponentTranslation("commands.generic.permission", new Object[0]);
            c.func_150256_b().func_150238_a(TextFormatting.RED);
            event.getSender().func_145747_a((ITextComponent)c);
        }
    }

    public boolean canCommandSenderUseCommand(ICommandSender player) {
        return true;
    }

    public String func_71518_a(ICommandSender sender) {
        return "commands.mcheli.usage";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] prm) throws CommandException {
        if (!MCH_Config.EnableCommand.prmBool) {
            return;
        }
        if (!MCH_Command.checkCommandPermission(server, sender, prm[0])) {
            TextComponentTranslation c = new TextComponentTranslation("commands.generic.permission", new Object[0]);
            c.func_150256_b().func_150238_a(TextFormatting.RED);
            sender.func_145747_a((ITextComponent)c);
            return;
        }
        if (prm[0].equalsIgnoreCase(CMD_GET_SS)) {
            if (prm.length != 2) throw new CommandException("Parameter error! : /mcheli sendss playerName", new Object[0]);
            EntityPlayerMP player = MCH_Command.func_184888_a((MinecraftServer)server, (ICommandSender)sender, (String)prm[1]);
            if (player == null) return;
            MCH_PacketIndClient.send((EntityPlayer)player, 1, prm[1]);
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_MOD_LIST)) {
            if (prm.length < 2) throw new CommandException("Parameter error! : /mcheli modlist playerName", new Object[0]);
            EntityPlayerMP reqPlayer = sender instanceof EntityPlayerMP ? (EntityPlayerMP)sender : null;
            for (int i = 1; i < prm.length; ++i) {
                EntityPlayerMP player = MCH_Command.func_184888_a((MinecraftServer)server, (ICommandSender)sender, (String)prm[i]);
                if (player == null) continue;
                MCH_PacketIndClient.send((EntityPlayer)player, 2, "" + MCH_MultiplayPacketHandler.getPlayerInfoId((EntityPlayer)reqPlayer));
            }
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_RECONFIG)) {
            if (prm.length != 1) throw new CommandException("Parameter error! : /mcheli reconfig", new Object[0]);
            MCH_MOD.proxy.reconfig();
            if (sender.func_130014_f_() != null && !sender.func_130014_f_().field_72995_K) {
                MCH_PacketNotifyServerSettings.sendAll();
            }
            if (MCH_MOD.proxy.isSinglePlayer()) {
                sender.func_145747_a((ITextComponent)new TextComponentString("Reload mcheli.cfg"));
                return;
            } else {
                sender.func_145747_a((ITextComponent)new TextComponentString("Reload server side mcheli.cfg"));
            }
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_TITLE)) {
            int pos;
            if (prm.length < 4) {
                throw new WrongUsageException("Parameter error! : /mcheli title time[1~180] position[0~4] messege[JSON format]", new Object[0]);
            }
            String s = MCH_Command.func_180529_a((String[])prm, (int)3);
            int showTime = Integer.valueOf(prm[1]);
            if (showTime < 1) {
                showTime = 1;
            }
            if (showTime > 180) {
                showTime = 180;
            }
            if ((pos = Integer.valueOf(prm[2]).intValue()) < 0) {
                pos = 0;
            }
            if (pos > 5) {
                pos = 5;
            }
            try {
                ITextComponent ichatcomponent = ITextComponent.Serializer.func_150699_a((String)s);
                MCH_PacketTitle.send(ichatcomponent, 20 * showTime, pos);
                return;
            }
            catch (JsonParseException jsonparseexception) {
                Throwable throwable = ExceptionUtils.getRootCause((Throwable)jsonparseexception);
                throw new SyntaxErrorException("mcheli.title.jsonException", new Object[]{throwable == null ? "" : throwable.getMessage()});
            }
        } else if (prm[0].equalsIgnoreCase(CMD_FILL)) {
            this.executeFill(sender, prm);
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_STATUS)) {
            this.executeStatus(sender, prm);
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_KILL_ENTITY)) {
            this.executeKillEntity(sender, prm);
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_REMOVE_ENTITY)) {
            this.executeRemoveEntity(sender, prm);
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_ATTACK_ENTITY)) {
            this.executeAttackEntity(sender, prm);
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_SHOW_BB)) {
            if (prm.length != 2) {
                throw new CommandException("Parameter error! : /mcheli showboundingbox true or false", new Object[0]);
            }
            if (!MCH_Command.func_180527_d((String)prm[1])) {
                MCH_Config.EnableDebugBoundingBox.prmBool = false;
                MCH_PacketNotifyServerSettings.sendAll();
                sender.func_145747_a((ITextComponent)new TextComponentString("Disabled bounding box"));
            } else {
                MCH_Config.EnableDebugBoundingBox.prmBool = true;
                MCH_PacketNotifyServerSettings.sendAll();
                sender.func_145747_a((ITextComponent)new TextComponentString("Enabled bounding box [F3 + b]"));
            }
            MCH_MOD.proxy.save();
            return;
        } else if (prm[0].equalsIgnoreCase(CMD_LIST)) {
            String msg = "";
            for (String s : ALL_COMMAND) {
                msg = msg + s + ", ";
            }
            sender.func_145747_a((ITextComponent)new TextComponentString("/mcheli command list : " + msg));
            return;
        } else {
            if (!prm[0].equalsIgnoreCase(CMD_DELAY_BB)) throw new CommandException("Unknown mcheli command. please type /mcheli list", new Object[0]);
            if (prm.length == 1) {
                sender.func_145747_a((ITextComponent)new TextComponentString("Current delay of hitbox = " + MCH_Config.HitBoxDelayTick.prmInt + " [0 ~ 50]"));
                return;
            } else {
                if (prm.length != 2) throw new CommandException("Parameter error! : /mcheli delayhitbox 0 ~ 50", new Object[0]);
                MCH_Config.HitBoxDelayTick.prmInt = MCH_Command.func_175755_a((String)prm[1]);
                if (MCH_Config.HitBoxDelayTick.prmInt > 50) {
                    MCH_Config.HitBoxDelayTick.prmInt = 50;
                }
                MCH_MOD.proxy.save();
                sender.func_145747_a((ITextComponent)new TextComponentString("Current delay of hitbox = " + MCH_Config.HitBoxDelayTick.prmInt + " [0 ~ 50]"));
            }
        }
    }

    private void executeAttackEntity(ICommandSender sender, String[] args) throws WrongUsageException {
        if (args.length < 3) {
            throw new WrongUsageException("/mcheli attackentity <entity class name : example1 EntityBat , example2 minecraft.entity.passive> <damage> [damage source]", new Object[0]);
        }
        String className = args[1].toLowerCase();
        float damage = Float.valueOf(args[2]).floatValue();
        String damageName = args.length >= 4 ? args[3].toLowerCase() : "";
        DamageSource ds = DamageSource.field_76377_j;
        if (!damageName.isEmpty()) {
            if (damageName.equals("player")) {
                if (sender instanceof EntityPlayer) {
                    ds = DamageSource.func_76365_a((EntityPlayer)((EntityPlayer)sender));
                }
            } else if (damageName.equals("anvil")) {
                ds = DamageSource.field_82728_o;
            } else if (damageName.equals("cactus")) {
                ds = DamageSource.field_76367_g;
            } else if (damageName.equals("drown")) {
                ds = DamageSource.field_76369_e;
            } else if (damageName.equals("fall")) {
                ds = DamageSource.field_76379_h;
            } else if (damageName.equals("fallingblock")) {
                ds = DamageSource.field_82729_p;
            } else if (damageName.equals("generic")) {
                ds = DamageSource.field_76377_j;
            } else if (damageName.equals("infire")) {
                ds = DamageSource.field_76372_a;
            } else if (damageName.equals("inwall")) {
                ds = DamageSource.field_76368_d;
            } else if (damageName.equals("lava")) {
                ds = DamageSource.field_76371_c;
            } else if (damageName.equals("magic")) {
                ds = DamageSource.field_76376_m;
            } else if (damageName.equals("onfire")) {
                ds = DamageSource.field_76370_b;
            } else if (damageName.equals("starve")) {
                ds = DamageSource.field_76366_f;
            } else if (damageName.equals("wither")) {
                ds = DamageSource.field_82727_n;
            }
        }
        int attacked = 0;
        List list = sender.func_130014_f_().field_72996_f;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == null || list.get(i) instanceof EntityPlayer || ((Entity)list.get(i)).getClass().getName().toLowerCase().indexOf(className) < 0) continue;
            ((Entity)list.get(i)).func_70097_a(ds, damage);
            ++attacked;
        }
        sender.func_145747_a((ITextComponent)new TextComponentString(attacked + " entity attacked(" + args[1] + ", damage=" + damage + ")."));
    }

    private void executeKillEntity(ICommandSender sender, String[] args) throws WrongUsageException {
        if (args.length < 2) {
            throw new WrongUsageException("/mcheli killentity <entity class name : example1 EntityBat , example2 minecraft.entity.passive>", new Object[0]);
        }
        String className = args[1].toLowerCase();
        int killed = 0;
        List list = sender.func_130014_f_().field_72996_f;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == null || list.get(i) instanceof EntityPlayer || ((Entity)list.get(i)).getClass().getName().toLowerCase().indexOf(className) < 0) continue;
            ((Entity)list.get(i)).func_70106_y();
            ++killed;
        }
        sender.func_145747_a((ITextComponent)new TextComponentString(killed + " entity killed(" + args[1] + ")."));
    }

    private void executeRemoveEntity(ICommandSender sender, String[] args) throws WrongUsageException {
        if (args.length < 2) {
            throw new WrongUsageException("/mcheli removeentity <entity class name : example1 EntityBat , example2 minecraft.entity.passive>", new Object[0]);
        }
        String className = args[1].toLowerCase();
        List list = sender.func_130014_f_().field_72996_f;
        int removed = 0;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == null || list.get(i) instanceof EntityPlayer || ((Entity)list.get(i)).getClass().getName().toLowerCase().indexOf(className) < 0) continue;
            ((Entity)list.get((int)i)).field_70128_L = true;
            ++removed;
        }
        sender.func_145747_a((ITextComponent)new TextComponentString(removed + " entity removed(" + args[1] + ")."));
    }

    private void executeStatus(ICommandSender sender, String[] args) throws WrongUsageException {
        if (args.length < 2) {
            throw new WrongUsageException("/mcheli status <entity or tile> [min num]", new Object[0]);
        }
        if (args[1].equalsIgnoreCase("entity")) {
            this.executeStatusSub(sender, args, "Server loaded Entity List", sender.func_130014_f_().field_72996_f);
        } else if (args[1].equalsIgnoreCase("tile")) {
            this.executeStatusSub(sender, args, "Server loaded Tile Entity List", sender.func_130014_f_().field_147482_g);
        }
    }

    private void executeStatusSub(ICommandSender sender, String[] args, String title, List<?> list) {
        int minNum = args.length >= 3 ? Integer.valueOf(args[2]) : 0;
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < list.size(); ++i) {
            String key = list.get(i).getClass().getName();
            if (map.containsKey(key)) {
                map.put(key, (Integer)map.get(key) + 1);
                continue;
            }
            map.put(key, 1);
        }
        ArrayList entries = new ArrayList(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>(){

            @Override
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                return entry1.getKey().compareTo(entry2.getKey());
            }
        });
        boolean send = false;
        sender.func_145747_a((ITextComponent)new TextComponentString("--- " + title + " ---"));
        for (Map.Entry entry : entries) {
            if ((Integer)entry.getValue() < minNum) continue;
            String msg = " " + (String)entry.getKey() + " : " + entry.getValue();
            System.out.println(msg);
            sender.func_145747_a((ITextComponent)new TextComponentString(msg));
            send = true;
        }
        if (!send) {
            System.out.println("none");
            sender.func_145747_a((ITextComponent)new TextComponentString("none"));
        }
    }

    public void executeFill(ICommandSender sender, String[] args) throws CommandException {
        int t;
        if (args.length < 8) {
            throw new WrongUsageException("/mcheli fill <x1> <y1> <z1> <x2> <y2> <z2> <block name> [meta data] [oldBlockHandling] [data tag]", new Object[0]);
        }
        int x1 = sender.func_180425_c().func_177958_n();
        int y1 = sender.func_180425_c().func_177956_o();
        int z1 = sender.func_180425_c().func_177952_p();
        int x2 = sender.func_180425_c().func_177958_n();
        int y2 = sender.func_180425_c().func_177956_o();
        int z2 = sender.func_180425_c().func_177952_p();
        x1 = MathHelper.func_76128_c((double)MCH_Command.func_175770_a((double)x1, (String)args[1], (boolean)true).func_179628_a());
        y1 = MathHelper.func_76128_c((double)MCH_Command.func_175770_a((double)y1, (String)args[2], (boolean)true).func_179628_a());
        z1 = MathHelper.func_76128_c((double)MCH_Command.func_175770_a((double)z1, (String)args[3], (boolean)true).func_179628_a());
        x2 = MathHelper.func_76128_c((double)MCH_Command.func_175770_a((double)x2, (String)args[4], (boolean)true).func_179628_a());
        y2 = MathHelper.func_76128_c((double)MCH_Command.func_175770_a((double)y2, (String)args[5], (boolean)true).func_179628_a());
        z2 = MathHelper.func_76128_c((double)MCH_Command.func_175770_a((double)z2, (String)args[6], (boolean)true).func_179628_a());
        Block block = CommandBase.func_147180_g((ICommandSender)sender, (String)args[7]);
        IBlockState iblockstate = block.func_176223_P();
        if (args.length >= 9) {
            iblockstate = MCH_Command.func_190794_a((Block)block, (String)args[8]);
        }
        World world = sender.func_130014_f_();
        if (x1 > x2) {
            t = x1;
            x1 = x2;
            x2 = t;
        }
        if (y1 > y2) {
            t = y1;
            y1 = y2;
            y2 = t;
        }
        if (z1 > z2) {
            t = z1;
            z1 = z2;
            z2 = t;
        }
        if (y1 < 0 || y2 >= 256) {
            throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
        }
        int blockNum = (x2 - x1 + 1) * (y2 - y1 + 1) * (z2 - z1 + 1);
        if (blockNum > 3000000) {
            throw new CommandException("commands.setblock.tooManyBlocks " + blockNum + " limit=327680", new Object[]{blockNum, 0x320000});
        }
        boolean result = false;
        boolean keep = args.length >= 10 && args[9].equals("keep");
        boolean destroy = args.length >= 10 && args[9].equals("destroy");
        boolean override = args.length >= 10 && args[9].equals("override");
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        boolean flag = false;
        if (args.length >= 11 && block.hasTileEntity(iblockstate)) {
            String s = MCH_Command.func_147178_a((ICommandSender)sender, (String[])args, (int)10).func_150260_c();
            try {
                NBTTagCompound nbtbase = JsonToNBT.func_180713_a((String)s);
                if (!(nbtbase instanceof NBTTagCompound)) {
                    throw new CommandException("commands.setblock.tagError", new Object[]{"Not a valid tag"});
                }
                nbttagcompound = nbtbase;
                flag = true;
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.setblock.tagError", new Object[]{nbtexception.getMessage()});
            }
        }
        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    TileEntity tileentity;
                    TileEntity block2;
                    BlockPos blockpos = new BlockPos(x, y, z);
                    if (!world.func_175667_e(blockpos) || !(world.func_175623_d(blockpos) ? !override : !keep)) continue;
                    if (destroy) {
                        world.func_175655_b(blockpos, false);
                    }
                    if ((block2 = world.func_175625_s(blockpos)) instanceof IInventory) {
                        IInventory ii = (IInventory)block2;
                        for (int i = 0; i < ii.func_70302_i_(); ++i) {
                            ItemStack is = ii.func_70304_b(i);
                            if (is.func_190926_b()) continue;
                            is.func_190920_e(0);
                        }
                    }
                    if (!world.func_180501_a(blockpos, iblockstate, 3)) continue;
                    if (flag && (tileentity = world.func_175625_s(blockpos)) != null) {
                        nbttagcompound.func_74768_a("x", x);
                        nbttagcompound.func_74768_a("y", y);
                        nbttagcompound.func_74768_a("z", z);
                        tileentity.func_145839_a(nbttagcompound);
                    }
                    result = true;
                }
            }
        }
        if (!result) {
            throw new CommandException("commands.setblock.noChange", new Object[0]);
        }
        MCH_Command.func_152373_a((ICommandSender)sender, (ICommand)this, (String)"commands.setblock.success", (Object[])new Object[0]);
    }

    public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] prm, BlockPos targetPos) {
        if (!MCH_Config.EnableCommand.prmBool) {
            return super.func_184883_a(server, sender, prm, targetPos);
        }
        if (prm.length <= 1) {
            return MCH_Command.func_71530_a((String[])prm, (String[])ALL_COMMAND);
        }
        if (prm[0].equalsIgnoreCase(CMD_GET_SS)) {
            if (prm.length == 2) {
                return MCH_Command.func_71530_a((String[])prm, (String[])server.func_71213_z());
            }
        } else if (prm[0].equalsIgnoreCase(CMD_MOD_LIST)) {
            if (prm.length >= 2) {
                return MCH_Command.func_71530_a((String[])prm, (String[])server.func_71213_z());
            }
        } else {
            if (prm[0].equalsIgnoreCase(CMD_FILL)) {
                if ((prm.length == 2 || prm.length == 5) && sender instanceof Entity) {
                    Entity entity = (Entity)sender;
                    ArrayList<String> a = new ArrayList<String>();
                    int x = entity.field_70165_t < 0.0 ? (int)(entity.field_70165_t - 1.0) : (int)entity.field_70165_t;
                    int z = entity.field_70161_v < 0.0 ? (int)(entity.field_70161_v - 1.0) : (int)entity.field_70161_v;
                    a.add("" + x + " " + (int)(entity.field_70163_u + 0.5) + " " + z);
                    return a;
                }
                return prm.length == 10 ? MCH_Command.func_71530_a((String[])prm, (String[])new String[]{"replace", "destroy", "keep", "override"}) : (prm.length == 8 ? MCH_Command.func_175762_a((String[])prm, (Collection)Block.field_149771_c.func_148742_b()) : null);
            }
            if (prm[0].equalsIgnoreCase(CMD_STATUS)) {
                if (prm.length == 2) {
                    return MCH_Command.func_71530_a((String[])prm, (String[])new String[]{"entity", "tile"});
                }
            } else if (prm[0].equalsIgnoreCase(CMD_ATTACK_ENTITY)) {
                if (prm.length == 4) {
                    return MCH_Command.func_71530_a((String[])prm, (String[])new String[]{"player", "inFire", "onFire", "lava", "inWall", "drown", "starve", "cactus", "fall", "outOfWorld", "generic", "magic", "wither", "anvil", "fallingBlock"});
                }
            } else if (prm[0].equalsIgnoreCase(CMD_SHOW_BB) && prm.length == 2) {
                return MCH_Command.func_71530_a((String[])prm, (String[])new String[]{"true", "false"});
            }
        }
        return super.func_184883_a(server, sender, prm, targetPos);
    }
}

