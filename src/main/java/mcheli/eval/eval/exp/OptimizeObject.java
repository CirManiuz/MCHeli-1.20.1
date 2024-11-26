/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.AndExpression;
import mcheli.eval.eval.exp.ArrayExpression;
import mcheli.eval.eval.exp.CharExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.Col2OpeExpression;
import mcheli.eval.eval.exp.Col3Expression;
import mcheli.eval.eval.exp.CommaExpression;
import mcheli.eval.eval.exp.FieldExpression;
import mcheli.eval.eval.exp.NumberExpression;
import mcheli.eval.eval.exp.OrExpression;
import mcheli.eval.eval.exp.ParenExpression;
import mcheli.eval.eval.exp.SignPlusExpression;
import mcheli.eval.eval.exp.StringExpression;
import mcheli.eval.eval.exp.VariableExpression;
import mcheli.eval.eval.exp.WordExpression;
import mcheli.eval.eval.repl.ReplaceAdapter;

public class OptimizeObject
extends ReplaceAdapter {
    protected boolean isConst(AbstractExpression x) {
        return x instanceof NumberExpression || x instanceof StringExpression || x instanceof CharExpression;
    }

    protected boolean isTrue(AbstractExpression x) {
        return x.evalLong() != 0L;
    }

    protected AbstractExpression toConst(AbstractExpression exp) {
        try {
            Object val = exp.evalObject();
            if (val instanceof String) {
                return StringExpression.create(exp, (String)val);
            }
            if (val instanceof Character) {
                return CharExpression.create(exp, val.toString());
            }
            return NumberExpression.create(exp, val.toString());
        }
        catch (Exception exception) {
            return exp;
        }
    }

    @Override
    public AbstractExpression replace0(WordExpression exp) {
        if (exp instanceof VariableExpression) {
            return this.toConst(exp);
        }
        return exp;
    }

    @Override
    public AbstractExpression replace1(Col1Expression exp) {
        if (exp instanceof ParenExpression) {
            return exp.exp;
        }
        if (exp instanceof SignPlusExpression) {
            return exp.exp;
        }
        if (this.isConst(exp.exp)) {
            return this.toConst(exp);
        }
        return exp;
    }

    @Override
    public AbstractExpression replace2(Col2Expression exp) {
        boolean const_l = this.isConst(exp.expl);
        boolean const_r = this.isConst(exp.expr);
        if (const_l && const_r) {
            return this.toConst(exp);
        }
        return exp;
    }

    @Override
    public AbstractExpression replace2(Col2OpeExpression exp) {
        if (exp instanceof ArrayExpression) {
            if (this.isConst(exp.expr)) {
                return this.toConst(exp);
            }
            return exp;
        }
        if (exp instanceof FieldExpression) {
            return this.toConst(exp);
        }
        boolean const_l = this.isConst(exp.expl);
        if (exp instanceof AndExpression) {
            if (const_l) {
                if (this.isTrue(exp.expl)) {
                    return exp.expr;
                }
                return exp.expl;
            }
            return exp;
        }
        if (exp instanceof OrExpression) {
            if (const_l) {
                if (this.isTrue(exp.expl)) {
                    return exp.expl;
                }
                return exp.expr;
            }
            return exp;
        }
        if (exp instanceof CommaExpression) {
            if (const_l) {
                return exp.expr;
            }
            return exp;
        }
        return exp;
    }

    @Override
    public AbstractExpression replace3(Col3Expression exp) {
        if (this.isConst(exp.exp1)) {
            if (this.isTrue(exp.exp1)) {
                return exp.exp2;
            }
            return exp.exp3;
        }
        return exp;
    }
}

