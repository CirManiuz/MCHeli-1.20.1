/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public abstract class Col1Expression
extends AbstractExpression {
    protected AbstractExpression exp;

    public static AbstractExpression create(AbstractExpression exp, String string, int pos, AbstractExpression x) {
        Col1Expression n = (Col1Expression)exp;
        n.setExpression(x);
        n.setPos(string, pos);
        return n;
    }

    protected Col1Expression() {
    }

    protected Col1Expression(Col1Expression from, ShareExpValue s) {
        super(from, s);
        if (from.exp != null) {
            this.exp = from.exp.dup(s);
        }
    }

    public void setExpression(AbstractExpression x) {
        this.exp = x;
    }

    @Override
    protected final int getCols() {
        return 1;
    }

    @Override
    protected final int getFirstPos() {
        return this.exp.getFirstPos();
    }

    @Override
    public long evalLong() {
        return this.operateLong(this.exp.evalLong());
    }

    @Override
    public double evalDouble() {
        return this.operateDouble(this.exp.evalDouble());
    }

    protected abstract long operateLong(long var1);

    protected abstract double operateDouble(double var1);

    @Override
    protected void search() {
        this.share.srch.search(this);
        if (this.share.srch.end()) {
            return;
        }
        if (this.share.srch.search1_begin(this)) {
            return;
        }
        if (this.share.srch.end()) {
            return;
        }
        this.exp.search();
        if (this.share.srch.end()) {
            return;
        }
        this.share.srch.search1_end(this);
    }

    @Override
    protected AbstractExpression replace() {
        this.exp = this.exp.replace();
        return this.share.repl.replace1(this);
    }

    @Override
    protected AbstractExpression replaceVar() {
        this.exp = this.exp.replaceVar();
        return this.share.repl.replaceVar1(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Col1Expression) {
            Col1Expression e = (Col1Expression)obj;
            if (this.getClass() == e.getClass()) {
                if (this.exp == null) {
                    return e.exp == null;
                }
                if (e.exp == null) {
                    return false;
                }
                return this.exp.equals(e.exp);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ this.exp.hashCode();
    }

    @Override
    public void dump(int n) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; ++i) {
            sb.append(' ');
        }
        sb.append(this.getOperator());
        System.out.println(sb.toString());
        if (this.exp != null) {
            this.exp.dump(n + 1);
        }
    }

    @Override
    public String toString() {
        if (this.exp == null) {
            return this.getOperator();
        }
        StringBuffer sb = new StringBuffer();
        if (this.exp.getPriority() > this.prio) {
            sb.append(this.getOperator());
            sb.append(this.exp.toString());
        } else if (this.exp.getPriority() == this.prio) {
            sb.append(this.getOperator());
            sb.append(' ');
            sb.append(this.exp.toString());
        } else {
            sb.append(this.getOperator());
            sb.append(this.share.paren.getOperator());
            sb.append(this.exp.toString());
            sb.append(this.share.paren.getEndOperator());
        }
        return sb.toString();
    }
}

