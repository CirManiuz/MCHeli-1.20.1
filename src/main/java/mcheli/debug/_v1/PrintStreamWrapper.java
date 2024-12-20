/*
 * Decompiled with CFR 0.152.
 */
package mcheli.debug._v1;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;

public class PrintStreamWrapper {
    private final PrintStream ps;
    private Deque<String> stack;
    private String nestStr;

    private PrintStreamWrapper(PrintStream stream) {
        this.ps = stream;
        this.stack = new ArrayDeque<String>();
        this.nestStr = "  ";
    }

    public void setNestStr(String nestStr) {
        this.nestStr = nestStr;
    }

    public PrintStreamWrapper push(String label) {
        this.println(label);
        return this.push();
    }

    public PrintStreamWrapper push() {
        this.stack.addLast(this.nestStr);
        return this;
    }

    public PrintStreamWrapper pop() {
        this.stack.removeLast();
        return this;
    }

    private void printNest() {
        this.stack.forEach(str -> this.ps.print((String)str));
    }

    public void println(Object o) {
        this.printNest();
        this.ps.println(o);
    }

    public void println(String s) {
        this.printNest();
        this.ps.println(s);
    }

    public void println() {
        this.printNest();
        this.ps.println();
    }

    public static PrintStreamWrapper create(PrintStream stream) {
        return new PrintStreamWrapper(stream);
    }
}

