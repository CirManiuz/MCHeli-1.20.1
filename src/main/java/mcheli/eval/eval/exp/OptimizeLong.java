/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.exp;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.NumberExpression;
import mcheli.eval.eval.exp.OptimizeObject;

public class OptimizeLong
extends OptimizeObject {
    @Override
    protected boolean isTrue(AbstractExpression x) {
        return x.evalLong() != 0L;
    }

    @Override
    protected AbstractExpression toConst(AbstractExpression exp) {
        try {
            long val = exp.evalLong();
            return NumberExpression.create(exp, Long.toString(val));
        }
        catch (Exception exception) {
            return exp;
        }
    }
}

