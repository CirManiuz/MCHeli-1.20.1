/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2OpeExpression;
import mcheli.eval.eval.exp.ShareExpValue;

public class FieldExpression
extends Col2OpeExpression {
    public FieldExpression() {
        this.setOperator(".");
    }

    protected FieldExpression(FieldExpression from, ShareExpValue s) {
        super(from, s);
    }

    @Override
    public AbstractExpression dup(ShareExpValue s) {
        return new FieldExpression(this, s);
    }

    @Override
    public long evalLong() {
        try {
            return this.share.var.evalLong(this.getVariable());
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2003, this.toString(), this.string, this.pos, e);
        }
    }

    @Override
    public double evalDouble() {
        try {
            return this.share.var.evalDouble(this.getVariable());
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2003, this.toString(), this.string, this.pos, e);
        }
    }

    @Override
    public Object evalObject() {
        return this.getVariable();
    }

    @Override
    protected Object getVariable() {
        Object obj = this.expl.getVariable();
        if (obj == null) {
            throw new EvalException(2104, this.expl.toString(), this.string, this.pos, null);
        }
        String word = this.expr.getWord();
        try {
            return this.share.var.getObject(obj, word);
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2301, this.toString(), this.string, this.pos, e);
        }
    }

    @Override
    protected void let(Object val, int pos) {
        Object obj = this.expl.getVariable();
        if (obj == null) {
            throw new EvalException(2104, this.expl.toString(), this.string, pos, null);
        }
        String word = this.expr.getWord();
        try {
            this.share.var.setValue(obj, word, val);
        }
        catch (EvalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new EvalException(2302, this.toString(), this.string, pos, e);
        }
    }

    @Override
    protected AbstractExpression replace() {
        this.expl = this.expl.replaceVar();
        return this.share.repl.replace2(this);
    }

    @Override
    protected AbstractExpression replaceVar() {
        this.expl = this.expl.replaceVar();
        return this.share.repl.replaceVar2(this);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.expl.toString());
        sb.append('.');
        sb.append(this.expr.toString());
        return sb.toString();
    }
}

