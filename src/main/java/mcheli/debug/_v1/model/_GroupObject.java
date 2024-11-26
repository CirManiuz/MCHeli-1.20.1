/*
 * Decompiled with CFR 0.152.
 */
package mcheli.debug._v1.model;

import java.util.ArrayList;
import java.util.List;
import mcheli.__helper.debug.DebugInfoObject;
import mcheli.debug._v1.PrintStreamWrapper;
import mcheli.debug._v1.model._Face;

class _GroupObject
implements DebugInfoObject {
    public final String name;
    private List<_Face> faces;

    private _GroupObject(String name, List<_Face> faces) {
        this.name = name;
        this.faces = faces;
    }

    @Override
    public void printInfo(PrintStreamWrapper stream) {
        stream.push(String.format("G: [name: %s]", this.name));
        this.faces.forEach(f -> f.printInfo(stream));
        stream.pop();
        stream.println();
    }

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private String name;
        private List<_Face> faces = new ArrayList<_Face>();

        Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addFace(_Face face) {
            this.faces.add(face);
            return this;
        }

        public int faceSize() {
            return this.faces.size();
        }

        public _GroupObject build() {
            return new _GroupObject(this.name, this.faces);
        }
    }
}

