/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import mcheli.eval.eval.ExpRuleFactory;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.rule.AbstractRule;
import mcheli.eval.eval.rule.ShareRuleValue;

public class JavaRuleFactory
extends ExpRuleFactory {
    private static JavaRuleFactory me;

    public static ExpRuleFactory getInstance() {
        if (me == null) {
            me = new JavaRuleFactory();
        }
        return me;
    }

    @Override
    protected AbstractRule createCommaRule(ShareRuleValue share) {
        return null;
    }

    @Override
    protected AbstractRule createPowerRule(ShareRuleValue share) {
        return null;
    }

    @Override
    protected AbstractExpression createLetPowerExpression() {
        return null;
    }
}

