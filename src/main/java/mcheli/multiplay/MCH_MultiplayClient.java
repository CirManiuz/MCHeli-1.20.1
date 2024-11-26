/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraftforge.fml.common.Loader
 *  net.minecraftforge.fml.common.ModContainer
 *  net.minecraftforge.fml.relauncher.CoreModManager
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.opengl.GL11
 */
package mcheli.multiplay;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.imageio.ImageIO;
import mcheli.MCH_Config;
import mcheli.MCH_FileSearch;
import mcheli.MCH_Lib;
import mcheli.MCH_OStream;
import mcheli.multiplay.MCH_PacketLargeData;
import mcheli.multiplay.MCH_PacketModList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.CoreModManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class MCH_MultiplayClient {
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
    private static MCH_OStream dataOutputStream;
    private static List<String> modList;

    public static void startSendImageData() {
        Minecraft mc = Minecraft.func_71410_x();
        MCH_MultiplayClient.sendScreenShot(mc.field_71443_c, mc.field_71440_d, mc.func_147110_a());
    }

    public static void sendScreenShot(int displayWidth, int displayHeight, Framebuffer framebufferMc) {
        try {
            if (OpenGlHelper.func_148822_b()) {
                displayWidth = framebufferMc.field_147622_a;
                displayHeight = framebufferMc.field_147620_b;
            }
            int k = displayWidth * displayHeight;
            if (pixelBuffer == null || pixelBuffer.capacity() < k) {
                pixelBuffer = BufferUtils.createIntBuffer((int)k);
                pixelValues = new int[k];
            }
            GL11.glPixelStorei((int)3333, (int)1);
            GL11.glPixelStorei((int)3317, (int)1);
            pixelBuffer.clear();
            if (OpenGlHelper.func_148822_b()) {
                GL11.glBindTexture((int)3553, (int)framebufferMc.field_147617_g);
                GL11.glGetTexImage((int)3553, (int)0, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
            } else {
                GL11.glReadPixels((int)0, (int)0, (int)displayWidth, (int)displayHeight, (int)32993, (int)33639, (IntBuffer)pixelBuffer);
            }
            pixelBuffer.get(pixelValues);
            TextureUtil.func_147953_a((int[])pixelValues, (int)displayWidth, (int)displayHeight);
            BufferedImage bufferedimage = null;
            if (OpenGlHelper.func_148822_b()) {
                int l;
                bufferedimage = new BufferedImage(framebufferMc.field_147621_c, framebufferMc.field_147618_d, 1);
                for (int i1 = l = framebufferMc.field_147620_b - framebufferMc.field_147618_d; i1 < framebufferMc.field_147620_b; ++i1) {
                    for (int j1 = 0; j1 < framebufferMc.field_147621_c; ++j1) {
                        bufferedimage.setRGB(j1, i1 - l, pixelValues[i1 * framebufferMc.field_147622_a + j1]);
                    }
                }
            } else {
                bufferedimage = new BufferedImage(displayWidth, displayHeight, 1);
                bufferedimage.setRGB(0, 0, displayWidth, displayHeight, pixelValues, 0, displayWidth);
            }
            dataOutputStream = new MCH_OStream();
            ImageIO.write((RenderedImage)bufferedimage, "png", dataOutputStream);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void readImageData(DataOutputStream dos) throws IOException {
        dataOutputStream.write(dos);
    }

    public static void sendImageData() {
        if (dataOutputStream != null) {
            MCH_PacketLargeData.send();
            if (dataOutputStream.isDataEnd()) {
                dataOutputStream = null;
            }
        }
    }

    public static double getPerData() {
        return dataOutputStream == null ? -1.0 : (double)(MCH_MultiplayClient.dataOutputStream.index / dataOutputStream.size());
    }

    public static void readModList(String playerName, String commandSenderName) {
        ZipEntry zipEntry;
        Enumeration<JarEntry> jarEntries;
        JarFile jarFile;
        String jarPath;
        modList = new ArrayList<String>();
        modList.add(TextFormatting.RED + "###### Name:" + commandSenderName + " ######");
        modList.add(TextFormatting.RED + "###### ID  :" + playerName + " ######");
        String[] classFileNameList = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String classFileName : classFileNameList) {
            File javaClassFile;
            MCH_Lib.DbgLog(true, "java.class.path=" + classFileName, new Object[0]);
            if (classFileName.length() <= 1 || (javaClassFile = new File(classFileName)).getAbsolutePath().toLowerCase().indexOf("versions") < 0) continue;
            modList.add(TextFormatting.AQUA + "# Client class=" + javaClassFile.getName() + " : file size= " + javaClassFile.length());
        }
        modList.add(TextFormatting.YELLOW + "=== ActiveModList ===");
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            modList.add("" + mod + "  [" + mod.getModId() + "]  " + mod.getName() + "[" + mod.getDisplayVersion() + "]  " + mod.getSource().getName());
        }
        if (CoreModManager.getAccessTransformers().size() > 0) {
            modList.add(TextFormatting.YELLOW + "=== AccessTransformers ===");
            for (String s : CoreModManager.getAccessTransformers()) {
                modList.add(s);
            }
        }
        if (CoreModManager.getIgnoredMods().size() > 0) {
            modList.add(TextFormatting.YELLOW + "=== LoadedCoremods ===");
            for (String s : CoreModManager.getIgnoredMods()) {
                modList.add(s);
            }
        }
        if (CoreModManager.getReparseableCoremods().size() > 0) {
            modList.add(TextFormatting.YELLOW + "=== ReparseableCoremods ===");
            for (String s : CoreModManager.getReparseableCoremods()) {
                modList.add(s);
            }
        }
        Minecraft mc = Minecraft.func_71410_x();
        MCH_FileSearch search = new MCH_FileSearch();
        File[] files = search.listFiles(new File(mc.field_71412_D, "mods").getAbsolutePath(), "*.jar");
        modList.add(TextFormatting.YELLOW + "=== Manifest ===");
        for (File file : files) {
            try {
                jarPath = file.getCanonicalPath();
                jarFile = new JarFile(jarPath);
                jarEntries = jarFile.entries();
                String manifest = "";
                while (jarEntries.hasMoreElements()) {
                    zipEntry = jarEntries.nextElement();
                    if (!zipEntry.getName().equalsIgnoreCase("META-INF/MANIFEST.MF") || zipEntry.isDirectory()) continue;
                    InputStream is = jarFile.getInputStream(zipEntry);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    while (line != null) {
                        if (!(line = line.replace(" ", "").trim()).isEmpty()) {
                            manifest = manifest + " [" + line + "]";
                        }
                        line = br.readLine();
                    }
                    is.close();
                    break;
                }
                jarFile.close();
                if (manifest.isEmpty()) continue;
                modList.add(file.getName() + manifest);
            }
            catch (Exception e) {
                modList.add(file.getName() + " : Read Manifest failed.");
            }
        }
        search = new MCH_FileSearch();
        files = search.listFiles(new File(mc.field_71412_D, "mods").getAbsolutePath(), "*.litemod");
        modList.add(TextFormatting.LIGHT_PURPLE + "=== LiteLoader ===");
        for (File file : files) {
            try {
                jarPath = file.getCanonicalPath();
                jarFile = new JarFile(jarPath);
                jarEntries = jarFile.entries();
                String litemod_json = "";
                while (jarEntries.hasMoreElements()) {
                    zipEntry = jarEntries.nextElement();
                    String fname = zipEntry.getName().toLowerCase();
                    if (zipEntry.isDirectory()) continue;
                    if (fname.equals("litemod.json")) {
                        InputStream is = jarFile.getInputStream(zipEntry);
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line = br.readLine();
                        while (line != null) {
                            if ((line = line.replace(" ", "").trim()).toLowerCase().indexOf("name") >= 0) {
                                litemod_json = litemod_json + " [" + line + "]";
                                break;
                            }
                            line = br.readLine();
                        }
                        is.close();
                        continue;
                    }
                    int index = fname.lastIndexOf("/");
                    if (index >= 0) {
                        fname = fname.substring(index + 1);
                    }
                    if (fname.indexOf("litemod") < 0 || !fname.endsWith("class")) continue;
                    fname = zipEntry.getName();
                    if (index >= 0) {
                        fname = fname.substring(index + 1);
                    }
                    litemod_json = litemod_json + " [" + fname + "]";
                }
                jarFile.close();
                if (litemod_json.isEmpty()) continue;
                modList.add(file.getName() + litemod_json);
            }
            catch (Exception e) {
                modList.add(file.getName() + " : Read LiteLoader failed.");
            }
        }
    }

    public static void sendModsInfo(String playerName, String commandSenderName, int id) {
        if (MCH_Config.DebugLog) {
            modList.clear();
            MCH_MultiplayClient.readModList(playerName, commandSenderName);
        }
        MCH_PacketModList.send(modList, id);
    }

    static {
        modList = new ArrayList<String>();
    }
}

