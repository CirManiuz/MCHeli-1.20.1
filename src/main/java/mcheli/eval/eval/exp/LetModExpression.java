/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.ModExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class LetModExpression
extends ModExpression {
    public LetModExpression() {
        this.setOperator("%=");
    }

    protected LetModExpression(LetModExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new LetModExpression(this, s);
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

