/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.lex;

import java.util.List;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.ShareExpValue;
import mcheli.eval.util.CharUtil;

public class Lex {
    protected List<String>[] opeList;
    protected String string;
    protected int pos = 0;
    protected int len = 0;
    protected int type = -1;
    public static final int TYPE_WORD = 0x7FFFFFF0;
    public static final int TYPE_NUM = 0x7FFFFFF1;
    public static final int TYPE_OPE = 0x7FFFFFF2;
    public static final int TYPE_STRING = 0x7FFFFFF3;
    public static final int TYPE_CHAR = 0x7FFFFFF4;
    public static final int TYPE_EOF = Integer.MAX_VALUE;
    public static final int TYPE_ERR = -1;
    protected String ope;
    protected ShareExpValue expShare;
    protected String SPC_CHAR = " \t\r\n";
    protected String NUMBER_CHAR = "._";

    protected Lex(String str, List<String>[] lists, AbstractExpression paren, ShareExpValue exp) {
        this.string = str;
        this.opeList = lists;
        this.expShare = exp;
        if (this.expShare.paren == null) {
            this.expShare.paren = paren;
        }
    }

    protected boolean isSpace(int pos) {
        if (pos >= this.string.length()) {
            return true;
        }
        char c = this.string.charAt(pos);
        return this.SPC_CHAR.indexOf(c) >= 0;
    }

    protected boolean isNumberTop(int pos) {
        if (pos >= this.string.length()) {
            return false;
        }
        char c = this.string.charAt(pos);
        return '0' <= c && c <= '9';
    }

    protected boolean isSpecialNumber(int pos) {
        if (pos >= this.string.length()) {
            return false;
        }
        char c = this.string.charAt(pos);
        return this.NUMBER_CHAR.indexOf(c) >= 0;
    }

    protected String isOperator(int pos) {
        for (int i = this.opeList.length - 1; i >= 0; --i) {
            List<String> list;
            if (pos + i >= this.string.length() || (list = this.opeList[i]) == null) continue;
            block1: for (int j = 0; j < list.size(); ++j) {
                String ope = list.get(j);
                for (int k = 0; k <= i; ++k) {
                    char o;
                    char c = this.string.charAt(pos + k);
                    if (c != (o = ope.charAt(k))) continue block1;
                }
                return ope;
            }
        }
        return null;
    }

    protected boolean isStringTop(int pos) {
        if (pos >= this.string.length()) {
            return false;
        }
        char c = this.string.charAt(pos);
        return c == '\"';
    }

    protected boolean isStringEnd(int pos) {
        return this.isStringTop(pos);
    }

    protected boolean isCharTop(int pos) {
        if (pos >= this.string.length()) {
            return false;
        }
        char c = this.string.charAt(pos);
        return c == '\'';
    }

    protected boolean isCharEnd(int pos) {
        return this.isCharTop(pos);
    }

    public void check() {
        while (this.isSpace(this.pos)) {
            if (this.pos >= this.string.length()) {
                this.type = Integer.MAX_VALUE;
                this.len = 0;
                return;
            }
            ++this.pos;
        }
        if (this.isStringTop(this.pos)) {
            this.processString();
            return;
        }
        if (this.isCharTop(this.pos)) {
            this.processChar();
            return;
        }
        String ope = this.isOperator(this.pos);
        if (ope != null) {
            this.type = 0x7FFFFFF2;
            this.ope = ope;
            this.len = ope.length();
            return;
        }
        boolean number = this.isNumberTop(this.pos);
        this.type = number ? 0x7FFFFFF1 : 0x7FFFFFF0;
        this.len = 1;
        while (!this.isSpace(this.pos + this.len) && (number && this.isSpecialNumber(this.pos + this.len) || this.isOperator(this.pos + this.len) == null)) {
            ++this.len;
        }
    }

    protected void processString() {
        int[] ret = new int[1];
        this.type = 0x7FFFFFF3;
        this.len = 1;
        do {
            this.len += this.getCharLen(this.pos + this.len, ret);
            if (this.pos + this.len < this.string.length()) continue;
            this.type = Integer.MAX_VALUE;
            break;
        } while (!this.isStringEnd(this.pos + this.len));
        ++this.len;
    }

    protected void processChar() {
        int[] ret = new int[1];
        this.type = 0x7FFFFFF4;
        this.len = 1;
        do {
            this.len += this.getCharLen(this.pos + this.len, ret);
            if (this.pos + this.len < this.string.length()) continue;
            this.type = Integer.MAX_VALUE;
            break;
        } while (!this.isCharEnd(this.pos + this.len));
        ++this.len;
    }

    protected int getCharLen(int pos, int[] ret) {
        CharUtil.escapeChar(this.string, pos, this.string.length(), ret);
        return ret[0];
    }

    public Lex next() {
        this.pos += this.len;
        this.check();
        return this;
    }

    public int getType() {
        return this.type;
    }

    public String getOperator() {
        return this.ope;
    }

    public boolean isOperator(String ope) {
        if (this.type == 0x7FFFFFF2) {
            return this.ope.equals(ope);
        }
        return false;
    }

    public String getWord() {
        return this.string.substring(this.pos, this.pos + this.len);
    }

    public String getString() {
        return this.string;
    }

    public int getPos() {
        return this.pos;
    }

    public ShareExpValue getShare() {
        return this.expShare;
    }
}

