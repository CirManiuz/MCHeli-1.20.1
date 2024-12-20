/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class IncBeforeExpression
extends Col1Expression {
    public IncBeforeExpression() {
        this.setOperator("++");
    }

    protected IncBeforeExpression(IncBeforeExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new IncBeforeExpression(this, s);
    }

    @Override
    protected long operateLong(long val) {
        this.exp.let(++val, this.pos);
        return val;
    }

    @Override
    protected double operateDouble(double val) {
        this.exp.let(val += 1.0, this.pos);
        return val;
    }

    @Override
    public Object evalObject() {
        Object val = this.exp.evalObject();
        val = this.share.oper.inc(val, 1);
        this.exp.let(val, this.pos);
        return val;
    }

    @Override
    protected AbstractExpression replace() {
        this.exp = this.exp.replaceVar();
        return this.share.repl.replaceVar1(this);
    }
}

