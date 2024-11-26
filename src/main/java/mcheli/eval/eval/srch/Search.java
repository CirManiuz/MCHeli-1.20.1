/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.srch;

import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.Col1Expression;
import mcheli.eval.eval.exp.Col2Expression;
import mcheli.eval.eval.exp.Col3Expression;
import mcheli.eval.eval.exp.FunctionExpression;
import mcheli.eval.eval.exp.WordExpression;

public interface Search {
    public boolean end();

    public void search(AbstractExpression var1);

    public void search0(WordExpression var1);

    public boolean search1_begin(Col1Expression var1);

    public void search1_end(Col1Expression var1);

    public boolean search2_begin(Col2Expression var1);

    public boolean search2_2(Col2Expression var1);

    public void search2_end(Col2Expression var1);

    public boolean search3_begin(Col3Expression var1);

    public boolean search3_2(Col3Expression var1);

    public boolean search3_3(Col3Expression var1);

    public void search3_end(Col3Expression var1);

    public boolean searchFunc_begin(FunctionExpression var1);

    public boolean searchFunc_2(FunctionExpression var1);

    public void searchFunc_end(FunctionExpression var1);
}

