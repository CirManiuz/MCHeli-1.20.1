/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col3Expression;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.eval.rule.AbstractRule;
import mcheli.eval.eval.rule.ShareRuleValue;

public class IfRule
extends AbstractRule {
    public AbstractExpression cond;

    public IfRule(ShareRuleValue share) {
        super(share);
    }

    @Override
    protected AbstractExpression parse(Lex lex) {
        AbstractExpression x = this.nextRule.parse(lex);
        switch (lex.getType()) {
            case 0x7FFFFFF2: {
                String ope = lex.getOperator();
                int pos = lex.getPos();
                if (this.isMyOperator(ope) && lex.isOperator(this.cond.getOperator())) {
                    x = this.parseCond(lex, x, ope, pos);
                }
                return x;
            }
        }
        return x;
    }

    protected AbstractExpression parseCond(Lex lex, AbstractExpression x, String ope, int pos) {
        AbstractExpression y = this.parse(lex.next());
        if (!lex.isOperator(this.cond.getEndOperator())) {
            throw new EvalException(1001, new String[]{this.cond.getEndOperator()}, lex);
        }
        AbstractExpression z = this.parse(lex.next());
        x = Col3Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, x, y, z);
        return x;
    }
}

