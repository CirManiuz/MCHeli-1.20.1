/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public class BitNotExpression
extends Col1Expression {
    public BitNotExpression() {
        this.setOperator("~");
    }

    protected BitNotExpression(BitNotExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new BitNotExpression(this, s);
    }

    @Override
    protected long operateLong(long val) {
        return val ^ 0xFFFFFFFFFFFFFFFFL;
    }

    @Override
    protected double operateDouble(double val) {
        return (long)val ^ 0xFFFFFFFFFFFFFFFFL;
    }

    @Override
    public Object evalObject() {
        return this.share.oper.bitNot(this.exp.evalObject());
    }
}

