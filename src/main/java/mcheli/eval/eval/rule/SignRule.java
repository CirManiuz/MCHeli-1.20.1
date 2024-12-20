/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.rule;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.lex.Lex;
import mcheli.eval.eval.rule.AbstractRule;
import mcheli.eval.eval.rule.ShareRuleValue;

public class SignRule
extends AbstractRule {
    public SignRule(ShareRuleValue share) {
        super(share);
    }

    @Override
    public AbstractExpression parse(Lex lex) {
        switch (lex.getType()) {
            case 0x7FFFFFF2: {
                String ope = lex.getOperator();
                if (this.isMyOperator(ope)) {
                    int pos = lex.getPos();
                    return Col1Expression.create(this.newExpression(ope, lex.getShare()), lex.getString(), pos, this.parse(lex.next()));
                }
                return this.nextRule.parse(lex);
            }
        }
        return this.nextRule.parse(lex);
    }
}

