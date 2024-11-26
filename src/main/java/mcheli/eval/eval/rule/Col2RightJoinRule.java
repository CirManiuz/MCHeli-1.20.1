/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.eval.rule.AbstractRule;
import mcheli.eval.eval.rule.ShareRuleValue;

public class Col2RightJoinRule
extends AbstractRule {
    public Col2RightJoinRule(ShareRuleValue share) {
        super(share);
    }

    @Override
    protected AbstractExpression parse(Lex lex) {
        AbstractExpression x = this.nextRule.parse(lex);
        switch (lex.getType()) {
            case 0x7FFFFFF2: {
                String ope = lex.getOperator();
                if (this.isMyOperator(ope)) {
                    int pos = lex.getPos();
                    AbstractExpression y = this.parse(lex.next());
                    x = Col2Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, x, y);
                }
                return x;
            }
        }
        return x;
    }
}

