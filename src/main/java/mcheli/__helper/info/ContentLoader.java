/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.LinkedHashMultimap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Multimap
 *  com.google.common.io.Files
 *  javax.annotation.Nullable
 */
package mcheli.__helper.info;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import mcheli.MCH_MOD;
import mcheli.__helper.MCH_Logger;
import mcheli.__helper.MCH_Utils;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.__helper.info.ContentFactories;
import mcheli.__helper.info.ContentParseException;
import mcheli.__helper.info.ContentType;
import mcheli.__helper.info.IContentData;
import mcheli.__helper.info.IContentFactory;

public abstract class ContentLoader {
    protected final String domain;
    protected final File dir;
    protected final String loaderVersion;
    private Predicate<String> fileFilter;

    public ContentLoader(String domain, File addonDir, String loaderVersion, Predicate<String> fileFilter) {
        this.domain = domain;
        this.dir = addonDir;
        this.loaderVersion = loaderVersion;
        this.fileFilter = fileFilter;
    }

    public boolean isReadable(String path) {
        return this.fileFilter.test(path);
    }

    @Nullable
    public IContentFactory getFactory(@Nullable String dirName) {
        return ContentFactories.getFactory(dirName);
    }

    public Multimap<ContentType, ContentEntry> load() {
        LinkedHashMultimap map = LinkedHashMultimap.create();
        List<ContentEntry> list = this.getEntries();
        for (ContentEntry entry : list) {
            map.put((Object)entry.getType(), (Object)entry);
        }
        return map;
    }

    protected abstract List<ContentEntry> getEntries();

    protected abstract InputStream getInputStreamByName(String var1) throws IOException;

    public <TYPE extends IContentData> List<TYPE> reloadAndParse(Class<TYPE> clazz, List<TYPE> oldContents, IContentFactory factory) {
        LinkedList list = Lists.newLinkedList();
        for (IContentData oldContent : oldContents) {
            try {
                ContentEntry entry = this.makeEntry(oldContent.getContentPath(), factory, true);
                IContentData content = entry.parse();
                if (content != null) {
                    content.onPostReload();
                } else {
                    content = oldContent;
                }
                if (!clazz.isInstance(content)) continue;
                list.add(clazz.cast(content));
            }
            catch (IOException e) {
                MCH_Logger.get().error("IO Error from file loader!", (Throwable)e);
            }
        }
        return list;
    }

    public IContentData reloadAndParseSingle(IContentData oldData, String dir) {
        IContentData content = oldData;
        try {
            ContentEntry entry = this.makeEntry(oldData.getContentPath(), this.getFactory(dir), true);
            content = entry.parse();
            if (content != null) {
                content.onPostReload();
            } else {
                content = oldData;
            }
        }
        catch (IOException e) {
            MCH_Logger.get().error("IO Error from file loader!", (Throwable)e);
        }
        return content;
    }

    protected ContentEntry makeEntry(String filepath, @Nullable IContentFactory factory, boolean reload) throws IOException {
        List lines = null;
        try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(this.getInputStreamByName(filepath), StandardCharsets.UTF_8));){
            lines = bufferedreader.lines().collect(Collectors.toList());
        }
        return new ContentEntry(filepath, this.domain, factory, lines, reload);
    }

    static class ContentEntry {
        private String filepath;
        private String domain;
        private IContentFactory factory;
        private List<String> lines;
        private boolean reload;

        private ContentEntry(String filepath, String domain, @Nullable IContentFactory factory, List<String> lines, boolean reload) {
            this.filepath = filepath;
            this.domain = domain;
            this.factory = factory;
            this.lines = lines;
            this.reload = reload;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Nullable
        public IContentData parse() {
            AddonResourceLocation location = MCH_Utils.addon(this.domain, Files.getNameWithoutExtension((String)this.filepath));
            if (!this.reload) {
                MCH_MOD.proxy.onParseStartFile(location);
            }
            try {
                IContentData content = this.factory.create(location, this.filepath);
                if (content != null) {
                    content.parse(this.lines, Files.getFileExtension((String)this.filepath), this.reload);
                    if (!content.validate()) {
                        MCH_Logger.get().debug("Invalid content info: " + this.filepath);
                    }
                }
                IContentData iContentData = content;
                return iContentData;
            }
            catch (Exception e) {
                String msg = "An error occurred while file loading ";
                if (e instanceof ContentParseException) {
                    msg = msg + "at line:" + ((ContentParseException)e).getLineNo() + ".";
                }
                MCH_Logger.get().error(msg + " file:{}, domain:{}", (Object)location.func_110623_a(), (Object)this.domain, (Object)e);
                IContentData iContentData = null;
                return iContentData;
            }
            finally {
                MCH_MOD.proxy.onParseFinishFile(location);
            }
        }

        public ContentType getType() {
            return this.factory.getType();
        }
    }
}

