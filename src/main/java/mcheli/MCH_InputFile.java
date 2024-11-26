/*
 * Decompiled with CFR 0.152.
 */
package mcheli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import mcheli.MCH_Lib;

public class MCH_InputFile {
    public File file = null;
    public BufferedReader br = null;

    public boolean open(String path) {
        this.close();
        this.file = new File(path);
        String filePath = this.file.getAbsolutePath();
        try {
            this.br = new BufferedReader(new FileReader(this.file));
        }
        catch (FileNotFoundException e) {
            MCH_Lib.DbgLog(true, "FILE open failed MCH_InputFile.open:" + filePath, new Object[0]);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean openUTF8(File file) {
        return this.openUTF8(file.getPath());
    }

    public boolean openUTF8(String path) {
        this.close();
        this.file = new File(path);
        try {
            this.br = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(this.file), "UTF-8"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String readLine() {
        try {
            return this.br != null ? this.br.readLine() : null;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    public void close() {
        try {
            if (this.br != null) {
                this.br.close();
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.br = null;
    }
}
