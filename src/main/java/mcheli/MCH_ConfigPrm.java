/*
 * Decompiled with CFR 0.152.
 */
package mcheli;

import mcheli.MCH_Lib;

public class MCH_ConfigPrm {
    public final int type;
    public final String name;
    public int prmInt = 0;
    public String prmString = "";
    public boolean prmBool = false;
    public double prmDouble = 0.0;
    public String desc = "";
    public int prmIntDefault = 0;
    public String validVer = "";

    public String toString() {
        if (this.type == 0) {
            return String.valueOf(this.prmInt);
        }
        if (this.type == 1) {
            return this.prmString;
        }
        if (this.type == 2) {
            return String.valueOf(this.prmBool);
        }
        if (this.type == 3) {
            return String.format("%.2f", this.prmDouble).replace(',', '.');
        }
        return "";
    }

    public MCH_ConfigPrm(String parameterName, int defaultParameter) {
        this.prmInt = defaultParameter;
        this.prmIntDefault = defaultParameter;
        this.type = 0;
        this.name = parameterName;
    }

    public MCH_ConfigPrm(String parameterName, String defaultParameter) {
        this.prmString = defaultParameter;
        this.type = 1;
        this.name = parameterName;
    }

    public MCH_ConfigPrm(String parameterName, boolean defaultParameter) {
        this.prmBool = defaultParameter;
        this.type = 2;
        this.name = parameterName;
    }

    public MCH_ConfigPrm(String parameterName, double defaultParameter) {
        this.prmDouble = defaultParameter;
        this.type = 3;
        this.name = parameterName;
    }

    public boolean compare(String s) {
        return this.name.equalsIgnoreCase(s);
    }

    public boolean isValidVer(String configVer) {
        if (configVer.length() >= 5 && this.validVer.length() >= 5) {
            String[] configVerSplit = configVer.split("\\.");
            String[] validVerSplit = this.validVer.split("\\.");
            if (configVerSplit.length == 3 && validVerSplit.length == 3) {
                for (int i = 0; i < 3; ++i) {
                    int n2;
                    int n1 = Integer.valueOf(configVerSplit[i].replaceAll("[a-zA-Z-_]", "").trim());
                    if (n1 > (n2 = Integer.valueOf(validVerSplit[i].replaceAll("[a-zA-Z-_]", "").trim()).intValue())) {
                        return true;
                    }
                    if (n1 >= n2) continue;
                    return false;
                }
            }
        }
        return true;
    }

    public void setPrm(int n) {
        if (this.type == 0) {
            this.prmInt = n;
        }
    }

    public void setPrm(String s) {
        if (this.type == 0) {
            this.prmInt = Integer.parseInt(s);
        }
        if (this.type == 1) {
            this.prmString = s;
        }
        if (this.type == 2) {
            if ((s = s.toLowerCase()).compareTo("true") == 0) {
                this.prmBool = true;
            }
            if (s.compareTo("false") == 0) {
                this.prmBool = false;
            }
        }
        if (this.type == 3 && !s.isEmpty()) {
            this.prmDouble = MCH_Lib.parseDouble(s);
        }
    }

    public void setPrm(boolean b) {
        if (this.type == 2) {
            this.prmBool = b;
        }
    }

    public void setPrm(double f) {
        if (this.type == 3) {
            this.prmDouble = f;
        }
    }
}

