/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.wrapper.modelloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import mcheli.__helper.client._ModelFormatException;
import mcheli.wrapper.modelloader.W_Face;
import mcheli.wrapper.modelloader.W_GroupObject;
import mcheli.wrapper.modelloader.W_ModelCustom;
import mcheli.wrapper.modelloader.W_TextureCoordinate;
import mcheli.wrapper.modelloader.W_Vertex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class W_MetasequoiaObject
extends W_ModelCustom {
    public ArrayList<W_Vertex> vertices = new ArrayList();
    public ArrayList<W_GroupObject> groupObjects = new ArrayList();
    private W_GroupObject currentGroupObject = null;
    private String fileName;
    private int vertexNum = 0;
    private int faceNum = 0;

    public W_MetasequoiaObject(ResourceLocation location, IResource resource) throws _ModelFormatException {
        this.fileName = resource.toString();
        this.loadObjModel(resource.func_110527_b());
    }

    public W_MetasequoiaObject(ResourceLocation resource) throws _ModelFormatException {
        this.fileName = resource.toString();
        try {
            IResource res = Minecraft.func_71410_x().func_110442_L().func_110536_a(resource);
            this.loadObjModel(res.func_110527_b());
        }
        catch (IOException e) {
            throw new _ModelFormatException("IO Exception reading model format:" + this.fileName, e);
        }
    }

    public W_MetasequoiaObject(String fileName, URL resource) throws _ModelFormatException {
        this.fileName = fileName;
        try {
            this.loadObjModel(resource.openStream());
        }
        catch (IOException e) {
            throw new _ModelFormatException("IO Exception reading model format:" + this.fileName, e);
        }
    }

    public W_MetasequoiaObject(String filename, InputStream inputStream) throws _ModelFormatException {
        this.fileName = filename;
        this.loadObjModel(inputStream);
    }

    @Override
    public boolean containsPart(String partName) {
        for (W_GroupObject groupObject : this.groupObjects) {
            if (!partName.equalsIgnoreCase(groupObject.name)) continue;
            return true;
        }
        return false;
    }

    private void loadObjModel(InputStream inputStream) throws _ModelFormatException {
        BufferedReader reader = null;
        String currentLine = null;
        int lineCount = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((currentLine = reader.readLine()) != null) {
                String[] s;
                W_GroupObject group;
                if (!W_MetasequoiaObject.isValidGroupObjectLine(currentLine = currentLine.replaceAll("\\s+", " ").trim()) || (group = this.parseGroupObject(currentLine, ++lineCount)) == null) continue;
                group.glDrawingMode = 4;
                this.vertices.clear();
                int vertexNum = 0;
                boolean mirror = false;
                double facet = Math.cos(0.785398163375);
                boolean shading = false;
                while ((currentLine = reader.readLine()) != null) {
                    ++lineCount;
                    if ((currentLine = currentLine.replaceAll("\\s+", " ").trim()).equalsIgnoreCase("mirror 1")) {
                        mirror = true;
                    }
                    if (currentLine.equalsIgnoreCase("shading 1")) {
                        shading = true;
                    }
                    if ((s = currentLine.split(" ")).length == 2 && s[0].equalsIgnoreCase("facet")) {
                        facet = Math.cos(Double.parseDouble(s[1]) * 3.1415926535 / 180.0);
                    }
                    if (!W_MetasequoiaObject.isValidVertexLine(currentLine)) continue;
                    vertexNum = Integer.valueOf(currentLine.split(" ")[1]);
                    break;
                }
                if (vertexNum > 0) {
                    while ((currentLine = reader.readLine()) != null) {
                        ++lineCount;
                        s = (currentLine = currentLine.replaceAll("\\s+", " ").trim()).split(" ");
                        if (s.length == 3) {
                            W_Vertex v = new W_Vertex(Float.valueOf(s[0]).floatValue() / 100.0f, Float.valueOf(s[1]).floatValue() / 100.0f, Float.valueOf(s[2]).floatValue() / 100.0f);
                            this.checkMinMax(v);
                            this.vertices.add(v);
                            if (--vertexNum > 0) continue;
                            break;
                        }
                        if (s.length <= 0) continue;
                        throw new _ModelFormatException("format error : " + this.fileName + " : line=" + lineCount);
                    }
                    int faceNum = 0;
                    while ((currentLine = reader.readLine()) != null) {
                        ++lineCount;
                        if (!W_MetasequoiaObject.isValidFaceLine(currentLine = currentLine.replaceAll("\\s+", " ").trim())) continue;
                        faceNum = Integer.valueOf(currentLine.split(" ")[1]);
                        break;
                    }
                    if (faceNum > 0) {
                        while ((currentLine = reader.readLine()) != null) {
                            ++lineCount;
                            String[] s2 = (currentLine = currentLine.replaceAll("\\s+", " ").trim()).split(" ");
                            if (s2.length > 2) {
                                if (Integer.valueOf(s2[0]) >= 3) {
                                    W_Face[] faces;
                                    for (W_Face face : faces = this.parseFace(currentLine, lineCount, mirror)) {
                                        group.faces.add(face);
                                    }
                                }
                                if (--faceNum > 0) continue;
                                break;
                            }
                            if (s2.length <= 2 || Integer.valueOf(s2[0]) == 3) continue;
                            throw new _ModelFormatException("found face is not triangle : " + this.fileName + " : line=" + lineCount);
                        }
                        this.calcVerticesNormal(group, shading, facet);
                    }
                }
                this.vertexNum += this.vertices.size();
                this.faceNum += group.faces.size();
                this.vertices.clear();
                this.groupObjects.add(group);
            }
            return;
        }
        catch (IOException e) {
            throw new _ModelFormatException("IO Exception reading model format : " + this.fileName, e);
        }
        finally {
            this.checkMinMaxFinal();
            this.vertices = null;
            try {
                reader.close();
            }
            catch (IOException iOException) {}
            try {
                inputStream.close();
            }
            catch (IOException iOException) {}
        }
    }

    public void calcVerticesNormal(W_GroupObject group, boolean shading, double facet) {
        for (W_Face f : group.faces) {
            f.vertexNormals = new W_Vertex[f.verticesID.length];
            for (int i = 0; i < f.verticesID.length; ++i) {
                W_Vertex vn = this.getVerticesNormalFromFace(f.faceNormal, f.verticesID[i], group, (float)facet);
                vn.normalize();
                if (shading) {
                    if ((double)(f.faceNormal.x * vn.x + f.faceNormal.y * vn.y + f.faceNormal.z * vn.z) >= facet) {
                        f.vertexNormals[i] = vn;
                        continue;
                    }
                    f.vertexNormals[i] = f.faceNormal;
                    continue;
                }
                f.vertexNormals[i] = f.faceNormal;
            }
        }
    }

    public W_Vertex getVerticesNormalFromFace(W_Vertex faceNormal, int verticesID, W_GroupObject group, float facet) {
        W_Vertex v = new W_Vertex(0.0f, 0.0f, 0.0f);
        block0: for (W_Face f : group.faces) {
            for (int id : f.verticesID) {
                if (id != verticesID) continue;
                if (f.faceNormal.x * faceNormal.x + f.faceNormal.y * faceNormal.y + f.faceNormal.z * faceNormal.z < facet) continue block0;
                v.add(f.faceNormal);
                continue block0;
            }
        }
        v.normalize();
        return v;
    }

    @Override
    public void renderAll() {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        if (this.currentGroupObject != null) {
            builder.func_181668_a(this.currentGroupObject.glDrawingMode, DefaultVertexFormats.field_181710_j);
        } else {
            builder.func_181668_a(4, DefaultVertexFormats.field_181710_j);
        }
        this.tessellateAll(tessellator);
        tessellator.func_78381_a();
    }

    public void tessellateAll(Tessellator tessellator) {
        for (W_GroupObject groupObject : this.groupObjects) {
            groupObject.render(tessellator);
        }
    }

    @Override
    public void renderOnly(String ... groupNames) {
        for (W_GroupObject groupObject : this.groupObjects) {
            for (String groupName : groupNames) {
                if (!groupName.equalsIgnoreCase(groupObject.name)) continue;
                groupObject.render();
            }
        }
    }

    public void tessellateOnly(Tessellator tessellator, String ... groupNames) {
        for (W_GroupObject groupObject : this.groupObjects) {
            for (String groupName : groupNames) {
                if (!groupName.equalsIgnoreCase(groupObject.name)) continue;
                groupObject.render(tessellator);
            }
        }
    }

    @Override
    public void renderPart(String partName) {
        if (partName.charAt(0) == '$') {
            block0: for (int i = 0; i < this.groupObjects.size(); ++i) {
                W_GroupObject groupObject = this.groupObjects.get(i);
                if (!partName.equalsIgnoreCase(groupObject.name)) continue;
                groupObject.render();
                ++i;
                while (i < this.groupObjects.size()) {
                    groupObject = this.groupObjects.get(i);
                    if (groupObject.name.charAt(0) == '$') continue block0;
                    groupObject.render();
                    ++i;
                }
            }
        } else {
            for (W_GroupObject groupObject : this.groupObjects) {
                if (!partName.equalsIgnoreCase(groupObject.name)) continue;
                groupObject.render();
            }
        }
    }

    public void tessellatePart(Tessellator tessellator, String partName) {
        for (W_GroupObject groupObject : this.groupObjects) {
            if (!partName.equalsIgnoreCase(groupObject.name)) continue;
            groupObject.render(tessellator);
        }
    }

    @Override
    public void renderAllExcept(String ... excludedGroupNames) {
        for (W_GroupObject groupObject : this.groupObjects) {
            boolean skipPart = false;
            for (String excludedGroupName : excludedGroupNames) {
                if (!excludedGroupName.equalsIgnoreCase(groupObject.name)) continue;
                skipPart = true;
            }
            if (skipPart) continue;
            groupObject.render();
        }
    }

    public void tessellateAllExcept(Tessellator tessellator, String ... excludedGroupNames) {
        for (W_GroupObject groupObject : this.groupObjects) {
            boolean exclude = false;
            for (String excludedGroupName : excludedGroupNames) {
                if (!excludedGroupName.equalsIgnoreCase(groupObject.name)) continue;
                exclude = true;
            }
            if (exclude) continue;
            groupObject.render(tessellator);
        }
    }

    private W_Face[] parseFace(String line, int lineCount, boolean mirror) {
        String[] s = line.split("[ VU)(M]+");
        int vnum = Integer.valueOf(s[0]);
        if (vnum != 3 && vnum != 4) {
            return new W_Face[0];
        }
        if (vnum == 3) {
            W_Face face = new W_Face();
            face.verticesID = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
            face.vertices = new W_Vertex[]{this.vertices.get(face.verticesID[0]), this.vertices.get(face.verticesID[1]), this.vertices.get(face.verticesID[2])};
            face.textureCoordinates = s.length >= 11 ? new W_TextureCoordinate[]{new W_TextureCoordinate(Float.valueOf(s[9]).floatValue(), Float.valueOf(s[10]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[7]).floatValue(), Float.valueOf(s[8]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[5]).floatValue(), Float.valueOf(s[6]).floatValue())} : new W_TextureCoordinate[]{new W_TextureCoordinate(0.0f, 0.0f), new W_TextureCoordinate(0.0f, 0.0f), new W_TextureCoordinate(0.0f, 0.0f)};
            face.faceNormal = face.calculateFaceNormal();
            return new W_Face[]{face};
        }
        W_Face face1 = new W_Face();
        face1.verticesID = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
        face1.vertices = new W_Vertex[]{this.vertices.get(face1.verticesID[0]), this.vertices.get(face1.verticesID[1]), this.vertices.get(face1.verticesID[2])};
        face1.textureCoordinates = s.length >= 12 ? new W_TextureCoordinate[]{new W_TextureCoordinate(Float.valueOf(s[10]).floatValue(), Float.valueOf(s[11]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[8]).floatValue(), Float.valueOf(s[9]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[6]).floatValue(), Float.valueOf(s[7]).floatValue())} : new W_TextureCoordinate[]{new W_TextureCoordinate(0.0f, 0.0f), new W_TextureCoordinate(0.0f, 0.0f), new W_TextureCoordinate(0.0f, 0.0f)};
        face1.faceNormal = face1.calculateFaceNormal();
        W_Face face2 = new W_Face();
        face2.verticesID = new int[]{Integer.valueOf(s[4]), Integer.valueOf(s[3]), Integer.valueOf(s[1])};
        face2.vertices = new W_Vertex[]{this.vertices.get(face2.verticesID[0]), this.vertices.get(face2.verticesID[1]), this.vertices.get(face2.verticesID[2])};
        face2.textureCoordinates = s.length >= 14 ? new W_TextureCoordinate[]{new W_TextureCoordinate(Float.valueOf(s[12]).floatValue(), Float.valueOf(s[13]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[10]).floatValue(), Float.valueOf(s[11]).floatValue()), new W_TextureCoordinate(Float.valueOf(s[6]).floatValue(), Float.valueOf(s[7]).floatValue())} : new W_TextureCoordinate[]{new W_TextureCoordinate(0.0f, 0.0f), new W_TextureCoordinate(0.0f, 0.0f), new W_TextureCoordinate(0.0f, 0.0f)};
        face2.faceNormal = face2.calculateFaceNormal();
        return new W_Face[]{face1, face2};
    }

    private static boolean isValidGroupObjectLine(String line) {
        String[] s = line.split(" ");
        if (s.length < 2 || !s[0].equals("Object")) {
            return false;
        }
        return s[1].length() >= 4 && s[1].charAt(0) == '\"';
    }

    private W_GroupObject parseGroupObject(String line, int lineCount) throws _ModelFormatException {
        W_GroupObject group = null;
        if (W_MetasequoiaObject.isValidGroupObjectLine(line)) {
            String[] s = line.split(" ");
            String trimmedLine = s[1].substring(1, s[1].length() - 1);
            if (trimmedLine.length() > 0) {
                group = new W_GroupObject(trimmedLine);
            }
        } else {
            throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
        return group;
    }

    private static boolean isValidVertexLine(String line) {
        String[] s = line.split(" ");
        return s[0].equals("vertex");
    }

    private static boolean isValidFaceLine(String line) {
        String[] s = line.split(" ");
        return s[0].equals("face");
    }

    @Override
    public String getType() {
        return "mqo";
    }

    @Override
    public void renderAllLine(int startLine, int maxLine) {
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(1, DefaultVertexFormats.field_181705_e);
        this.renderAllLine(tessellator, startLine, maxLine);
        tessellator.func_78381_a();
    }

    public void renderAllLine(Tessellator tessellator, int startLine, int maxLine) {
        int lineCnt = 0;
        BufferBuilder builder = tessellator.func_178180_c();
        for (W_GroupObject groupObject : this.groupObjects) {
            if (groupObject.faces.size() <= 0) continue;
            for (W_Face face : groupObject.faces) {
                for (int i = 0; i < face.vertices.length / 3; ++i) {
                    W_Vertex v1 = face.vertices[i * 3 + 0];
                    W_Vertex v2 = face.vertices[i * 3 + 1];
                    W_Vertex v3 = face.vertices[i * 3 + 2];
                    if (++lineCnt > maxLine) {
                        return;
                    }
                    builder.func_181662_b((double)v1.x, (double)v1.y, (double)v1.z).func_181675_d();
                    builder.func_181662_b((double)v2.x, (double)v2.y, (double)v2.z).func_181675_d();
                    if (++lineCnt > maxLine) {
                        return;
                    }
                    builder.func_181662_b((double)v2.x, (double)v2.y, (double)v2.z).func_181675_d();
                    builder.func_181662_b((double)v3.x, (double)v3.y, (double)v3.z).func_181675_d();
                    if (++lineCnt > maxLine) {
                        return;
                    }
                    builder.func_181662_b((double)v3.x, (double)v3.y, (double)v3.z).func_181675_d();
                    builder.func_181662_b((double)v1.x, (double)v1.y, (double)v1.z).func_181675_d();
                }
            }
        }
    }

    @Override
    public int getVertexNum() {
        return this.vertexNum;
    }

    @Override
    public int getFaceNum() {
        return this.faceNum;
    }

    @Override
    public void renderAll(int startFace, int maxFace) {
        if (startFace < 0) {
            startFace = 0;
        }
        Tessellator tessellator = Tessellator.func_178181_a();
        BufferBuilder builder = tessellator.func_178180_c();
        builder.func_181668_a(4, DefaultVertexFormats.field_181710_j);
        this.renderAll(tessellator, startFace, maxFace);
        tessellator.func_78381_a();
    }

    public void renderAll(Tessellator tessellator, int startFace, int maxLine) {
        int faceCnt = 0;
        for (W_GroupObject groupObject : this.groupObjects) {
            if (groupObject.faces.size() <= 0) continue;
            for (W_Face face : groupObject.faces) {
                if (++faceCnt < startFace) continue;
                if (faceCnt > maxLine) {
                    return;
                }
                face.addFaceForRender(tessellator);
            }
        }
    }
}

