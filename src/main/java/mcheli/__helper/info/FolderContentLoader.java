/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.CharMatcher
 *  com.google.common.collect.Lists
 *  javax.annotation.Nullable
 *  org.jline.utils.OSUtils
 */
package mcheli.__helper.info;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mcheli.__helper.info.ContentLoader;
import mcheli.__helper.info.IContentFactory;
import org.jline.utils.OSUtils;

public class FolderContentLoader
extends ContentLoader {
    private static final boolean ON_WINDOWS = OSUtils.IS_WINDOWS;
    private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is((char)'\\');
    private File addonFolder;

    public FolderContentLoader(String domain, File addonDir, String loaderVersion, Predicate<String> fileFilter) {
        super(domain, addonDir, loaderVersion, fileFilter);
        this.addonFolder = addonDir.getParentFile();
    }

    @Override
    protected List<ContentEntry> getEntries() {
        return this.walkDir(this.dir, null, this.loaderVersion.equals("2"), 0);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private List<ContentEntry> walkDir(File dir, @Nullable IContentFactory factory, boolean loadDeep, int depth) {
        ArrayList list = Lists.newArrayList();
        if (dir == null || !dir.exists()) {
            return Lists.newArrayList();
        }
        if (dir.isDirectory()) {
            if (!loadDeep && depth > 1) return list;
            for (File f : dir.listFiles()) {
                boolean flag;
                IContentFactory contentFactory = factory;
                boolean bl = flag = loadDeep || depth == 0 && "assets".equals(f.getName());
                if (contentFactory == null) {
                    contentFactory = this.getFactory(f.getName());
                }
                list.addAll(this.walkDir(f, contentFactory, flag, depth + 1));
            }
            return list;
        } else {
            try {
                String s = this.getDirPath(dir);
                if (!this.isReadable(s) || factory == null) return list;
                list.add(this.makeEntry(s, factory, false));
                return list;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private String getDirPath(File file) throws IOException {
        String[] split;
        String s = this.dir.getName();
        String s1 = file.getCanonicalPath();
        if (ON_WINDOWS) {
            s1 = BACKSLASH_MATCHER.replaceFrom((CharSequence)s1, '/');
        }
        if ((split = s1.split(this.addonFolder.getName() + "/" + s + "/", 2)).length < 2) {
            throw new FileNotFoundException(String.format("'%s' in AddonPack '%s'", this.dir, s));
        }
        return split[1];
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        File file1 = this.getFile(name);
        if (file1 == null) {
            throw new FileNotFoundException(String.format("'%s' in AddonPack '%s'", this.dir, name));
        }
        return new BufferedInputStream(new FileInputStream(file1));
    }

    @Nullable
    private File getFile(String filepath) {
        try {
            File file1 = new File(this.dir, filepath);
            if (file1.isFile() && FolderContentLoader.validatePath(file1, filepath)) {
                return file1;
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return null;
    }

    protected static boolean validatePath(File file, String filepath) throws IOException {
        String s = file.getCanonicalPath();
        if (ON_WINDOWS) {
            s = BACKSLASH_MATCHER.replaceFrom((CharSequence)s, '/');
        }
        return s.endsWith(filepath);
    }
}

