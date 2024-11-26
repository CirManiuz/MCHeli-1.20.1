/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.util;

public class CharUtil {
    public static String escapeString(String str) {
        return CharUtil.escapeString(str, 0, str.length());
    }

    public static String escapeString(String str, int pos, int len) {
        StringBuffer sb = new StringBuffer(len);
        int end_pos = pos + len;
        int[] ret = new int[1];
        while (pos < end_pos) {
            char c = CharUtil.escapeChar(str, pos, end_pos, ret);
            if (ret[0] <= 0) break;
            sb.append(c);
            pos += ret[0];
        }
        return sb.toString();
    }

    public static char escapeChar(String str, int pos, int end_pos, int[] ret) {
        if (pos >= end_pos) {
            ret[0] = 0;
            return '\u0000';
        }
        char c = str.charAt(pos);
        if (c != '\\') {
            ret[0] = 1;
            return c;
        }
        if (++pos >= end_pos) {
            ret[0] = 1;
            return c;
        }
        ret[0] = 2;
        c = str.charAt(pos);
        switch (c) {
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': {
                long code = c - 48;
                for (int i = 1; i < 3 && ++pos < end_pos && (c = str.charAt(pos)) >= '0' && c <= '7'; ++i) {
                    ret[0] = ret[0] + 1;
                    code *= 8L;
                    code += (long)(c - 48);
                }
                return (char)code;
            }
            case 'b': {
                return '\b';
            }
            case 'f': {
                return '\f';
            }
            case 'n': {
                return '\n';
            }
            case 'r': {
                return '\r';
            }
            case 't': {
                return '\t';
            }
            case 'u': {
                long code = 0L;
                for (int i = 0; i < 4 && ++pos < end_pos; ++i) {
                    c = str.charAt(pos);
                    if ('0' <= c && c <= '9') {
                        ret[0] = ret[0] + 1;
                        code *= 16L;
                        code += (long)(c - 48);
                        continue;
                    }
                    if ('a' <= c && c <= 'f') {
                        ret[0] = ret[0] + 1;
                        code *= 16L;
                        code += (long)(c - 97 + 10);
                        continue;
                    }
                    if ('A' > c || c > 'F') break;
                    ret[0] = ret[0] + 1;
                    code *= 16L;
                    code += (long)(c - 65 + 10);
                }
                return (char)code;
            }
        }
        return c;
    }
}

