/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.BitOrExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class LetOrExpression
extends BitOrExpression {
    public LetOrExpression() {
        this.setOperator("|=");
    }

    protected LetOrExpression(LetOrExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new LetOrExpression(this, s);
    }

    @Override
    public long evalLong() {
        long val = super.evalLong();
        this.expl.let(val, this.pos);
        return val;
    }

    @Override
    public double evalDouble() {
        double val = super.evalDouble();
        this.expl.let(val, this.pos);
        return val;
    }

    @Override
    public Object evalObject() {
        Object val = super.evalObject();
        this.expl.let(val, this.pos);
        return val;
    }

    @Override
    protected AbstractExpression replace() {
        this.expl = this.expl.replaceVar();
        this.expr = this.expr.replace();
        return this.share.repl.replaceLet(this);
    }
}

