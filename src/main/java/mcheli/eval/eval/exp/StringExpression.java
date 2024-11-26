/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.ShareExpValue;
import mcheli.eval.eval.exp.WordExpression;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.util.CharUtil;
import mcheli.eval.util.NumberUtil;

public class StringExpression
extends WordExpression {
    public static AbstractExpression create(Lex lex, int prio) {
        String str = lex.getWord();
        str = CharUtil.escapeString(str, 1, str.length() - 2);
        StringExpression exp = new StringExpression(str);
        exp.setPos(lex.getString(), lex.getPos());
        exp.setPriority(prio);
        exp.share = lex.getShare();
        return exp;
    }

    public StringExpression(String str) {
        super(str);
        this.setOperator("\"");
        this.setEndOperator("\"");
    }

    protected StringExpression(StringExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new StringExpression(this, s);
    }

    public static StringExpression create(AbstractExpression from, String word) {
        StringExpression n = new StringExpression(word);
        n.string = from.string;
        n.pos = from.pos;
        n.prio = from.prio;
        n.share = from.share;
        return n;
    }

    @Override
    public long evalLong() {
        try {
            return NumberUtil.parseLong(this.word);
        }
        catch (Exception e) {
            try {
                return Long.parseLong(this.word);
            }
            catch (Exception e1) {
                try {
                    return (long)Double.parseDouble(this.word);
                }
                catch (Exception e2) {
                    throw new EvalException(2003, this.word, this.string, this.pos, e2);
                }
            }
        }
    }

    @Override
    public double evalDouble() {
        try {
            return Double.parseDouble(this.word);
        }
        catch (Exception e) {
            try {
                return NumberUtil.parseLong(this.word);
            }
            catch (Exception e2) {
                throw new EvalException(2003, this.word, this.string, this.pos, e);
            }
        }
    }

    @Override
    public Object evalObject() {
        return this.word;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringExpression) {
            StringExpression e = (StringExpression)obj;
            return this.word.equals(e.word);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.word.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getOperator());
        sb.append(this.word);
        sb.append(this.getEndOperator());
        return sb.toString();
    }
}

