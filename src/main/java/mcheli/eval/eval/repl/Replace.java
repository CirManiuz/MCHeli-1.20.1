/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.repl;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.Col2OpeExpression;
import mcheli.eval.eval.exp.Col3Expression;
import mcheli.eval.eval.exp.FunctionExpression;
import mcheli.eval.eval.exp.WordExpression;

public interface Replace {
    public AbstractExpression replace0(WordExpression var1);

    public AbstractExpression replace1(Col1Expression var1);

    public AbstractExpression replace2(Col2Expression var1);

    public AbstractExpression replace2(Col2OpeExpression var1);

    public AbstractExpression replace3(Col3Expression var1);

    public AbstractExpression replaceVar0(WordExpression var1);

    public AbstractExpression replaceVar1(Col1Expression var1);

    public AbstractExpression replaceVar2(Col2Expression var1);

    public AbstractExpression replaceVar2(Col2OpeExpression var1);

    public AbstractExpression replaceVar3(Col3Expression var1);

    public AbstractExpression replaceFunc(FunctionExpression var1);

    public AbstractExpression replaceLet(Col2Expression var1);
}

