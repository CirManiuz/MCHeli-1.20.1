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
import mcheli.eval.eval.srch.Search;

public class SearchAdapter
implements Search {
    protected boolean end = false;

    @Override
    public boolean end() {
        return this.end;
    }

    protected void setEnd() {
        this.end = true;
    }

    @Override
    public void search(AbstractExpression exp) {
    }

    @Override
    public void search0(WordExpression exp) {
    }

    @Override
    public boolean search1_begin(Col1Expression exp) {
        return false;
    }

    @Override
    public void search1_end(Col1Expression exp) {
    }

    @Override
    public boolean search2_begin(Col2Expression exp) {
        return false;
    }

    @Override
    public boolean search2_2(Col2Expression exp) {
        return false;
    }

    @Override
    public void search2_end(Col2Expression exp) {
    }

    @Override
    public boolean search3_begin(Col3Expression exp) {
        return false;
    }

    @Override
    public boolean search3_2(Col3Expression exp3) {
        return false;
    }

    @Override
    public boolean search3_3(Col3Expression exp) {
        return false;
    }

    @Override
    public void search3_end(Col3Expression exp) {
    }

    @Override
    public boolean searchFunc_begin(FunctionExpression exp) {
        return false;
    }

    @Override
    public boolean searchFunc_2(FunctionExpression exp) {
        return false;
    }

    @Override
    public void searchFunc_end(FunctionExpression exp) {
    }
}
