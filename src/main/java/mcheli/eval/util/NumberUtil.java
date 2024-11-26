/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.util;

public class NumberUtil {
    public static long parseLong(String str) {
        if (str == null) {
            return 0L;
        }
        int len = (str = str.trim()).length();
        if (len <= 0) {
            return 0L;
        }
        switch (str.charAt(len - 1)) {
            case '.': 
            case 'L': 
            case 'l': {
                --len;
            }
        }
        if (len >= 3 && str.charAt(0) == '0') {
            switch (str.charAt(1)) {
                case 'B': 
                case 'b': {
                    return NumberUtil.parseLongBin(str, 2, len - 2);
                }
                case 'O': 
                case 'o': {
                    return NumberUtil.parseLongOct(str, 2, len - 2);
                }
                case 'X': 
                case 'x': {
                    return NumberUtil.parseLongHex(str, 2, len - 2);
                }
            }
        }
        return NumberUtil.parseLongDec(str, 0, len);
    }

    public static long parseLongBin(String str) {
        if (str == null) {
            return 0L;
        }
        return NumberUtil.parseLongBin(str, 0, str.length());
    }

    public static long parseLongBin(String str, int pos, int len) {
        long ret = 0L;
        block4: for (int i = 0; i < len; ++i) {
            ret *= 2L;
            char c = str.charAt(pos++);
            switch (c) {
                case '0': {
                    continue block4;
                }
                case '1': {
                    ++ret;
                    continue block4;
                }
                default: {
                    throw new NumberFormatException(str.substring(pos, len));
                }
            }
        }
        return ret;
    }

    public static long parseLongOct(String str) {
        if (str == null) {
            return 0L;
        }
        return NumberUtil.parseLongOct(str, 0, str.length());
    }

    public static long parseLongOct(String str, int pos, int len) {
        long ret = 0L;
        block4: for (int i = 0; i < len; ++i) {
            ret *= 8L;
            char c = str.charAt(pos++);
            switch (c) {
                case '0': {
                    continue block4;
                }
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': {
                    ret += (long)(c - 48);
                    continue block4;
                }
                default: {
                    throw new NumberFormatException(str.substring(pos, len));
                }
            }
        }
        return ret;
    }

    public static long parseLongDec(String str) {
        if (str == null) {
            return 0L;
        }
        return NumberUtil.parseLongDec(str, 0, str.length());
    }

    public static long parseLongDec(String str, int pos, int len) {
        long ret = 0L;
        block4: for (int i = 0; i < len; ++i) {
            ret *= 10L;
            char c = str.charAt(pos++);
            switch (c) {
                case '0': {
                    continue block4;
                }
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9': {
                    ret += (long)(c - 48);
                    continue block4;
                }
                default: {
                    throw new NumberFormatException(str.substring(pos, len));
                }
            }
        }
        return ret;
    }

    public static long parseLongHex(String str) {
        if (str == null) {
            return 0L;
        }
        return NumberUtil.parseLongHex(str, 0, str.length());
    }

    public static long parseLongHex(String str, int pos, int len) {
        long ret = 0L;
        block6: for (int i = 0; i < len; ++i) {
            ret *= 16L;
            char c = str.charAt(pos++);
            switch (c) {
                case '0': {
                    continue block6;
                }
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9': {
                    ret += (long)(c - 48);
                    continue block6;
                }
                case 'a': 
                case 'b': 
                case 'c': 
                case 'd': 
                case 'e': 
                case 'f': {
                    ret += (long)(c - 97 + 10);
                    continue block6;
                }
                case 'A': 
                case 'B': 
                case 'C': 
                case 'D': 
                case 'E': 
                case 'F': {
                    ret += (long)(c - 65 + 10);
                    continue block6;
                }
                default: {
                    throw new NumberFormatException(str.substring(pos, len));
                }
            }
        }
        return ret;
    }
}

