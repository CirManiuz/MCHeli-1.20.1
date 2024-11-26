/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package mcheli.__helper.info;

import com.google.common.collect.Lists;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import mcheli.__helper.MCH_Logger;
import mcheli.__helper.info.ContentLoader;
import mcheli.__helper.info.IContentFactory;

public class FileContentLoader
extends ContentLoader
implements Closeable {
    private ZipFile resourcePackZipFile;

    public FileContentLoader(String domain, File addonDir, String loaderVersion, Predicate<String> fileFilter) {
        super(domain, addonDir, loaderVersion, fileFilter);
    }

    private ZipFile getResourcePackZipFile() throws IOException {
        if (this.resourcePackZipFile == null) {
            this.resourcePackZipFile = new ZipFile(this.dir);
        }
        return this.resourcePackZipFile;
    }

    @Override
    protected List<ContentEntry> getEntries() {
        return this.walkEntries("2".equals(this.loaderVersion));
    }

    private List<ContentEntry> walkEntries(boolean findDeep) {
        LinkedList list = Lists.newLinkedList();
        try {
            ZipFile zipfile = this.getResourcePackZipFile();
            Iterator itr = zipfile.stream().filter(e -> this.filter((ZipEntry)e, findDeep)).iterator();
            while (itr.hasNext()) {
                String name = ((ZipEntry)itr.next()).getName();
                String[] s = name.split("/");
                String typeDirName = s.length >= 3 ? s[2] : null;
                IContentFactory factory = this.getFactory(typeDirName);
                if (factory == null) continue;
                list.add(this.makeEntry(name, factory, false));
            }
        }
        catch (IOException e2) {
            MCH_Logger.get().error("IO Error from file loader!", (Throwable)e2);
        }
        return list;
    }

    private boolean filter(ZipEntry zipEntry, boolean deep) {
        String lootDir;
        String name = zipEntry.getName();
        String[] split = name.split("/");
        String string = lootDir = split.length >= 2 ? split[0] : "";
        if (!zipEntry.isDirectory() && (deep || "assets".equals(lootDir) || split.length <= 2)) {
            return this.isReadable(name);
        }
        return false;
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        ZipFile zipfile = this.getResourcePackZipFile();
        ZipEntry zipentry = zipfile.getEntry(name);
        if (zipentry == null) {
            throw new FileNotFoundException(String.format("'%s' in AddonPack '%s'", this.dir, name));
        }
        return zipfile.getInputStream(zipentry);
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    @Override
    public void close() throws IOException {
        if (this.resourcePackZipFile != null) {
            this.resourcePackZipFile.close();
            this.resourcePackZipFile = null;
        }
    }
}

