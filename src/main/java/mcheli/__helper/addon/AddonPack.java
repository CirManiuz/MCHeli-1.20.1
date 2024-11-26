/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.util.JsonUtils
 *  org.apache.commons.io.IOUtils
 */
package mcheli.__helper.addon;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import mcheli.__helper.MCH_Logger;
import mcheli.__helper.io.ResourceLoader;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;

public class AddonPack {
    private final String addonDomain;
    private final String addonName;
    private final String addonVersion;
    private File addonFile;
    private String credits;
    private List<String> authors;
    private String description;
    private String loaderVersion;
    protected ImmutableMap<String, JsonElement> packMetaMap;

    public AddonPack(String addonDomain, String addonName, String addonVersion, File addonFile, String credits, List<String> authors, String description, String loaderVersion, ImmutableMap<String, JsonElement> packMetaMap) {
        this.addonDomain = addonDomain;
        this.addonName = addonName;
        this.addonVersion = addonVersion;
        this.addonFile = addonFile;
        this.credits = credits;
        this.authors = authors;
        this.description = description;
        this.loaderVersion = loaderVersion;
        this.packMetaMap = packMetaMap;
    }

    public String getDomain() {
        return this.addonDomain;
    }

    public String getName() {
        return this.addonName;
    }

    public String getVersion() {
        return this.addonVersion;
    }

    public String getAuthorsString() {
        return Joiner.on((String)", ").join(this.authors);
    }

    public String getCredits() {
        return this.credits;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLoaderVersion() {
        return this.loaderVersion;
    }

    public File getFile() {
        return this.addonFile;
    }

    public ImmutableMap<String, JsonElement> getPackMetaMap() {
        return this.packMetaMap;
    }

    public static AddonPack create(File addonFile) {
        JsonObject packMetaJson = AddonPack.loadPackMeta(addonFile);
        JsonObject packJson = JsonUtils.func_151218_a((JsonObject)packMetaJson, (String)"pack", (JsonObject)new JsonObject());
        JsonObject addonJson = JsonUtils.func_151218_a((JsonObject)packMetaJson, (String)"addon", (JsonObject)new JsonObject());
        String addonDomain = JsonUtils.func_151219_a((JsonObject)addonJson, (String)"domain", null);
        String packName = JsonUtils.func_151219_a((JsonObject)packJson, (String)"description", (String)addonFile.getName());
        String version = JsonUtils.func_151219_a((JsonObject)addonJson, (String)"version", (String)"0.0");
        if (addonDomain == null) {
            MCH_Logger.get().warn("A addon domain is not specified! file:{}", (Object)addonFile.getName());
            addonDomain = "<!mcheli_share_domain>";
        }
        String credits = JsonUtils.func_151219_a((JsonObject)addonJson, (String)"credits", (String)"");
        String description = JsonUtils.func_151219_a((JsonObject)addonJson, (String)"description", (String)"");
        String loaderVersion = JsonUtils.func_151219_a((JsonObject)addonJson, (String)"loader_version", (String)"1");
        List<String> authors = AddonPack.getAuthors(addonJson);
        return new AddonPack(addonDomain, packName, version, addonFile, credits, authors, description, loaderVersion, (ImmutableMap<String, JsonElement>)ImmutableMap.copyOf((Iterable)packMetaJson.entrySet()));
    }

    private static List<String> getAuthors(JsonObject jsonObject) {
        JsonElement jsonElement2;
        LinkedList list = Lists.newLinkedList();
        if (jsonObject.has("authors")) {
            JsonElement jsonElement = jsonObject.get("authors");
            if (jsonElement.isJsonArray()) {
                for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                    list.add(jsonElement1.getAsString());
                }
            }
        } else if (jsonObject.has("author") && (jsonElement2 = jsonObject.get("author")).isJsonPrimitive()) {
            list.add(jsonElement2.getAsString());
        }
        return list;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    private static JsonObject loadPackMeta(File addonFile) {
        block7: {
            JsonObject jsonObject;
            ResourceLoader loader = ResourceLoader.create(addonFile);
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(loader.getInputStream("pack.mcmeta"), StandardCharsets.UTF_8));
                jsonObject = new JsonParser().parse((Reader)bufferedReader).getAsJsonObject();
                IOUtils.closeQuietly((Reader)bufferedReader);
            }
            catch (FileNotFoundException e) {
                MCH_Logger.get().warn("'pack.mcmeta' does not found in '{}'", (Object)addonFile.getName());
                break block7;
            }
            catch (IOException e2) {
                e2.printStackTrace();
                break block7;
                {
                    catch (Throwable throwable) {
                        throw throwable;
                    }
                }
            }
            finally {
                IOUtils.closeQuietly(bufferedReader);
                IOUtils.closeQuietly((Closeable)loader);
            }
            IOUtils.closeQuietly((Closeable)loader);
            return jsonObject;
        }
        return new JsonObject();
    }
}

