/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.CharMatcher
 *  com.google.common.collect.Lists
 *  javax.annotation.Nullable
 *  org.jline.utils.OSUtils
 */
package mcheli.__helper.io;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import org.jline.utils.OSUtils;

public abstract class ResourceLoader
implements Closeable {
    protected final File dir;

    ResourceLoader(File file) {
        this.dir = file;
    }

    public List<ResourceEntry> loadAll() throws IOException {
        return this.loadAll(null);
    }

    public abstract List<ResourceEntry> loadAll(@Nullable Predicate<? super ResourceEntry> var1) throws IOException;

    public Optional<ResourceEntry> loadFirst() throws IOException {
        return this.loadAll(null).stream().findFirst();
    }

    public abstract ResourceEntry load(String var1) throws IOException, FileNotFoundException;

    public abstract InputStream getInputStreamFromEntry(ResourceEntry var1) throws IOException;

    public InputStream getInputStream(String relativePath) throws IOException {
        return this.getInputStreamFromEntry(this.load(relativePath));
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public static ResourceLoader create(File file) {
        if (file.isDirectory()) {
            return new DirectoryLoader(file);
        }
        return new ZipJarFileLoader(file);
    }

    static class DirectoryLoader
    extends ResourceLoader {
        private static final boolean ON_WINDOWS = OSUtils.IS_WINDOWS;
        private static final CharMatcher BACKSLASH_MATCHER = CharMatcher.is((char)'\\');

        DirectoryLoader(File file) {
            super(file);
        }

        @Override
        public List<ResourceEntry> loadAll(@Nullable Predicate<? super ResourceEntry> filePathFilter) throws IOException {
            LinkedList list = Lists.newLinkedList();
            filePathFilter = filePathFilter == null ? path -> true : filePathFilter;
            this.loadFiles(this.dir, list, filePathFilter);
            return list;
        }

        private void loadFiles(File dir, List<ResourceEntry> list, Predicate<? super ResourceEntry> filePathFilter) throws IOException {
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    for (File file : dir.listFiles()) {
                        this.loadFiles(file, list, filePathFilter);
                    }
                } else {
                    ResourceEntry resourceFile;
                    Path file = dir.toPath();
                    Path root = this.dir.toPath();
                    String s = root.relativize(file).toString();
                    if (ON_WINDOWS) {
                        s = BACKSLASH_MATCHER.replaceFrom((CharSequence)s, '/');
                    }
                    if (filePathFilter.test(resourceFile = new ResourceEntry(s, dir.isDirectory()))) {
                        list.add(resourceFile);
                    }
                }
            }
        }

        @Override
        public ResourceEntry load(String relativePath) throws IOException, FileNotFoundException {
            File file = this.getFile(relativePath);
            if (file != null && file.exists()) {
                return new ResourceEntry(relativePath, file.isDirectory());
            }
            throw new FileNotFoundException(relativePath);
        }

        @Override
        public InputStream getInputStreamFromEntry(ResourceEntry resource) throws IOException {
            File file1 = this.getFile(resource.getPath());
            if (file1 == null) {
                throw new FileNotFoundException(String.format("'%s' in ResourcePack '%s'", this.dir, resource.getPath()));
            }
            return new BufferedInputStream(new FileInputStream(file1));
        }

        @Nullable
        private File getFile(String filepath) {
            try {
                File file1 = new File(this.dir, filepath);
                if (file1.isFile() && this.validatePath(file1, filepath)) {
                    return file1;
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return null;
        }

        private boolean validatePath(File file, String filepath) throws IOException {
            String s = file.getCanonicalPath();
            if (ON_WINDOWS) {
                s = BACKSLASH_MATCHER.replaceFrom((CharSequence)s, '/');
            }
            return s.endsWith(filepath);
        }

        @Override
        public void close() throws IOException {
        }
    }

    static class ZipJarFileLoader
    extends ResourceLoader {
        private ZipFile resourcePackZipFile;

        ZipJarFileLoader(File file) {
            super(file);
        }

        @Override
        public List<ResourceEntry> loadAll(@Nullable Predicate<? super ResourceEntry> filePathFilter) throws IOException {
            ZipFile zipfile = this.getResourcePackZipFile();
            filePathFilter = filePathFilter == null ? path -> true : filePathFilter;
            List<ResourceEntry> list = zipfile.stream().map(enrty -> new ResourceEntry(enrty.getName(), enrty.isDirectory())).filter(filePathFilter).collect(Collectors.toList());
            return list;
        }

        @Override
        public ResourceEntry load(String relativePath) throws IOException, FileNotFoundException {
            ZipFile zipfile = this.getResourcePackZipFile();
            ZipEntry zipEntry = zipfile.getEntry(relativePath);
            if (zipEntry != null) {
                return new ResourceEntry(zipEntry.getName(), zipEntry.isDirectory());
            }
            throw new FileNotFoundException(relativePath);
        }

        @Override
        public InputStream getInputStreamFromEntry(ResourceEntry resource) throws IOException {
            ZipFile zipfile = this.getResourcePackZipFile();
            ZipEntry zipentry = zipfile.getEntry(resource.getPath());
            if (zipentry == null) {
                throw new FileNotFoundException(String.format("'%s' in ResourcePack '%s'", this.dir, resource.getPath()));
            }
            return zipfile.getInputStream(zipentry);
        }

        private ZipFile getResourcePackZipFile() throws IOException {
            if (this.resourcePackZipFile == null) {
                this.resourcePackZipFile = new ZipFile(this.dir);
            }
            return this.resourcePackZipFile;
        }

        @Override
        public void close() throws IOException {
            if (this.resourcePackZipFile != null) {
                this.resourcePackZipFile.close();
                this.resourcePackZipFile = null;
            }
        }
    }

    public static class ResourceEntry {
        private String path;
        private boolean isDirectory;

        public ResourceEntry(String path, boolean isDirectory) {
            this.path = path;
            this.isDirectory = isDirectory;
        }

        public boolean isDirectory() {
            return this.isDirectory;
        }

        public String getPath() {
            return this.path;
        }
    }
}

