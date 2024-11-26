/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.ShareExpValue;

public abstract class Col2OpeExpression
extends Col2Expression {
    protected Col2OpeExpression() {
    }

    protected Col2OpeExpression(Col2Expression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    protected final long operateLong(long vl, long vr) {
        throw new RuntimeException("\u3053\u306e\u95a2\u6570\u304c\u547c\u3070\u308c\u3066\u306f\u3044\u3051\u306a\u3044");
    }

    @Override
    protected final double operateDouble(double vl, double vr) {
        throw new RuntimeException("\u3053\u306e\u95a2\u6570\u304c\u547c\u3070\u308c\u3066\u306f\u3044\u3051\u306a\u3044");
    }

    @Override
    protected final Object operateObject(Object vl, Object vr) {
        throw new RuntimeException("\u3053\u306e\u95a2\u6570\u304c\u547c\u3070\u308c\u3066\u306f\u3044\u3051\u306a\u3044");
    }

    @Override
    protected AbstractExpression replace() {
        this.expl = this.expl.replace();
        this.expr = this.expr.replace();
        return this.share.repl.replace2(this);
    }

    @Override
    protected AbstractExpression replaceVar() {
        this.expl = this.expl.replaceVar();
        this.expr = this.expr.replaceVar();
        return this.share.repl.replaceVar2(this);
    }
}

