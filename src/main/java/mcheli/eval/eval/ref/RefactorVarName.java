/*
 * Decompiled with CFR 0.152.
 */
package mcheli.eval.eval.ref;

import mcheli.eval.eval.ref.RefactorAdapter;

public class RefactorVarName
extends RefactorAdapter {
    protected Class<?> targetClass;
    protected String oldName;
    protected String newName;

    public RefactorVarName(Class<?> targetClass, String oldName, String newName) {
        this.targetClass = targetClass;
        this.oldName = oldName;
        this.newName = newName;
        if (oldName == null || newName == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public String getNewName(Object target, String name) {
        if (!name.equals(this.oldName)) {
            return null;
        }
        if (this.targetClass == null ? target == null : target != null && this.targetClass.isAssignableFrom(target.getClass())) {
            return this.newName;
        }
        return null;
    }
}

