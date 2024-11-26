/*
 * Decompiled with CFR 0.152.
 */
package mcheli;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

public class MCH_FileSearch {
    public static final int TYPE_FILE_OR_DIR = 1;
    public static final int TYPE_FILE = 2;
    public static final int TYPE_DIR = 3;
    private TreeSet<File> set = new TreeSet();

    public File[] listFiles(String directoryPath, String fileName) {
        if (fileName != null) {
            fileName = fileName.replace(".", "\\.");
            fileName = fileName.replace("*", ".*");
        }
        return this.listFiles(directoryPath, fileName, 2, true, 0);
    }

    public File[] listFiles(String directoryPath, String fileNamePattern, int type, boolean isRecursive, int period) {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("\u5f15\u6570\u3067\u6307\u5b9a\u3055\u308c\u305f\u30d1\u30b9[" + dir.getAbsolutePath() + "]\u306f\u30c7\u30a3\u30ec\u30af\u30c8\u30ea\u3067\u306f\u3042\u308a\u307e\u305b\u3093\u3002");
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            File file = files[i];
            this.addFile(type, fileNamePattern, this.set, file, period);
            if (!isRecursive || !file.isDirectory()) continue;
            this.listFiles(file.getAbsolutePath(), fileNamePattern, type, isRecursive, period);
        }
        return this.set.toArray(new File[this.set.size()]);
    }

    private void addFile(int type, String match, TreeSet<File> set, File file, int period) {
        switch (type) {
            case 2: {
                if (file.isFile()) break;
                return;
            }
            case 3: {
                if (file.isDirectory()) break;
                return;
            }
        }
        if (match != null && !file.getName().matches(match)) {
            return;
        }
        if (period != 0) {
            Date lastModifiedDate = new Date(file.lastModified());
            String lastModifiedDateStr = new SimpleDateFormat("yyyyMMdd").format(lastModifiedDate);
            long oneDayTime = 86400000L;
            long periodTime = oneDayTime * (long)Math.abs(period);
            Date designatedDate = new Date(System.currentTimeMillis() - periodTime);
            String designatedDateStr = new SimpleDateFormat("yyyyMMdd").format(designatedDate);
            if (period > 0) {
                if (lastModifiedDateStr.compareTo(designatedDateStr) >= 0) {
                    // empty if block
                }
            } else if (lastModifiedDateStr.compareTo(designatedDateStr) > 0) {
                return;
            }
        }
        set.add(file);
    }

    public void clear() {
        this.set.clear();
    }
}

