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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class W_WavefrontObject
extends W_ModelCustom {
    private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
    private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static Pattern groupObjectPattern = Pattern.compile("([go]( [-\\$\\w\\d]+) *\\n)|([go]( [-\\$\\w\\d]+) *$)");
    private static Matcher vertexMatcher;
    private static Matcher vertexNormalMatcher;
    private static Matcher textureCoordinateMatcher;
    private static Matcher face_V_VT_VN_Matcher;
    private static Matcher face_V_VT_Matcher;
    private static Matcher face_V_VN_Matcher;
    private static Matcher face_V_Matcher;
    private static Matcher groupObjectMatcher;
    public ArrayList<W_Vertex> vertices = new ArrayList();
    public ArrayList<W_Vertex> vertexNormals = new ArrayList();
    public ArrayList<W_TextureCoordinate> textureCoordinates = new ArrayList();
    public ArrayList<W_GroupObject> groupObjects = new ArrayList();
    private W_GroupObject currentGroupObject;
    private String fileName;

    public W_WavefrontObject(ResourceLocation location, IResource resource) throws _ModelFormatException {
        this.fileName = resource.toString();
        this.loadObjModel(resource.func_110527_b());
    }

    public W_WavefrontObject(ResourceLocation resource) throws _ModelFormatException {
        this.fileName = resource.toString();
        try {
            IResource res = Minecraft.func_71410_x().func_110442_L().func_110536_a(resource);
            this.loadObjModel(res.func_110527_b());
        }
        catch (IOException e) {
            throw new _ModelFormatException("IO Exception reading model format", e);
        }
    }

    public W_WavefrontObject(String fileName, URL resource) throws _ModelFormatException {
        this.fileName = fileName;
        try {
            this.loadObjModel(resource.openStream());
        }
        catch (IOException e) {
            throw new _ModelFormatException("IO Exception reading model format", e);
        }
    }

    public W_WavefrontObject(String filename, InputStream inputStream) throws _ModelFormatException {
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
                W_Vertex vertex;
                ++lineCount;
                if ((currentLine = currentLine.replaceAll("\\s+", " ").trim()).startsWith("#") || currentLine.length() == 0) continue;
                if (currentLine.startsWith("v ")) {
                    vertex = this.parseVertex(currentLine, lineCount);
                    if (vertex == null) continue;
                    this.checkMinMax(vertex);
                    this.vertices.add(vertex);
                    continue;
                }
                if (currentLine.startsWith("vn ")) {
                    vertex = this.parseVertexNormal(currentLine, lineCount);
                    if (vertex == null) continue;
                    this.vertexNormals.add(vertex);
                    continue;
                }
                if (currentLine.startsWith("vt ")) {
                    W_TextureCoordinate textureCoordinate = this.parseTextureCoordinate(currentLine, lineCount);
                    if (textureCoordinate == null) continue;
                    this.textureCoordinates.add(textureCoordinate);
                    continue;
                }
                if (currentLine.startsWith("f ")) {
                    W_Face face;
                    if (this.currentGroupObject == null) {
                        this.currentGroupObject = new W_GroupObject("Default");
                    }
                    if ((face = this.parseFace(currentLine, lineCount)) == null) continue;
                    this.currentGroupObject.faces.add(face);
                    continue;
                }
                if (!(currentLine.startsWith("g ") | currentLine.startsWith("o ")) || currentLine.charAt(2) != '$') continue;
                W_GroupObject group = this.parseGroupObject(currentLine, lineCount);
                if (group != null && this.currentGroupObject != null) {
                    this.groupObjects.add(this.currentGroupObject);
                }
                this.currentGroupObject = group;
            }
            this.groupObjects.add(this.currentGroupObject);
            return;
        }
        catch (IOException e) {
            throw new _ModelFormatException("IO Exception reading model format", e);
        }
        finally {
            this.checkMinMaxFinal();
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
        for (W_GroupObject groupObject : this.groupObjects) {
            if (!partName.equalsIgnoreCase(groupObject.name)) continue;
            groupObject.render();
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

    private W_Vertex parseVertex(String line, int lineCount) throws _ModelFormatException {
        W_Vertex vertex;
        block5: {
            vertex = null;
            if (W_WavefrontObject.isValidVertexLine(line)) {
                line = line.substring(line.indexOf(" ") + 1);
                String[] tokens = line.split(" ");
                try {
                    if (tokens.length == 2) {
                        return new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                    }
                    if (tokens.length == 3) {
                        return new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    }
                    break block5;
                }
                catch (NumberFormatException e) {
                    throw new _ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
                }
            }
            throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
        return vertex;
    }

    private W_Vertex parseVertexNormal(String line, int lineCount) throws _ModelFormatException {
        W_Vertex vertexNormal;
        block4: {
            vertexNormal = null;
            if (W_WavefrontObject.isValidVertexNormalLine(line)) {
                line = line.substring(line.indexOf(" ") + 1);
                String[] tokens = line.split(" ");
                try {
                    if (tokens.length == 3) {
                        return new W_Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    }
                    break block4;
                }
                catch (NumberFormatException e) {
                    throw new _ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
                }
            }
            throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
        return vertexNormal;
    }

    private W_TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws _ModelFormatException {
        W_TextureCoordinate textureCoordinate;
        block5: {
            textureCoordinate = null;
            if (W_WavefrontObject.isValidTextureCoordinateLine(line)) {
                line = line.substring(line.indexOf(" ") + 1);
                String[] tokens = line.split(" ");
                try {
                    if (tokens.length == 2) {
                        return new W_TextureCoordinate(Float.parseFloat(tokens[0]), 1.0f - Float.parseFloat(tokens[1]));
                    }
                    if (tokens.length == 3) {
                        return new W_TextureCoordinate(Float.parseFloat(tokens[0]), 1.0f - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    }
                    break block5;
                }
                catch (NumberFormatException e) {
                    throw new _ModelFormatException(String.format("Number formatting error at line %d", lineCount), e);
                }
            }
            throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
        return textureCoordinate;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private W_Face parseFace(String line, int lineCount) throws _ModelFormatException {
        W_Face face = null;
        if (!W_WavefrontObject.isValidFaceLine(line)) throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        face = new W_Face();
        String trimmedLine = line.substring(line.indexOf(" ") + 1);
        String[] tokens = trimmedLine.split(" ");
        String[] subTokens = null;
        if (tokens.length == 3) {
            if (this.currentGroupObject.glDrawingMode == -1) {
                this.currentGroupObject.glDrawingMode = 4;
            } else if (this.currentGroupObject.glDrawingMode != 4) {
                throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 4, found " + tokens.length + ")");
            }
        } else if (tokens.length == 4) {
            if (this.currentGroupObject.glDrawingMode == -1) {
                this.currentGroupObject.glDrawingMode = 7;
            } else if (this.currentGroupObject.glDrawingMode != 7) {
                throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Invalid number of points for face (expected 3, found " + tokens.length + ")");
            }
        }
        if (W_WavefrontObject.isValidFace_V_VT_VN_Line(line)) {
            face.vertices = new W_Vertex[tokens.length];
            face.textureCoordinates = new W_TextureCoordinate[tokens.length];
            face.vertexNormals = new W_Vertex[tokens.length];
            for (int i = 0; i < tokens.length; ++i) {
                subTokens = tokens[i].split("/");
                face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
            return face;
        } else if (W_WavefrontObject.isValidFace_V_VT_Line(line)) {
            face.vertices = new W_Vertex[tokens.length];
            face.textureCoordinates = new W_TextureCoordinate[tokens.length];
            for (int i = 0; i < tokens.length; ++i) {
                subTokens = tokens[i].split("/");
                face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                face.textureCoordinates[i] = this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
            return face;
        } else if (W_WavefrontObject.isValidFace_V_VN_Line(line)) {
            face.vertices = new W_Vertex[tokens.length];
            face.vertexNormals = new W_Vertex[tokens.length];
            for (int i = 0; i < tokens.length; ++i) {
                subTokens = tokens[i].split("//");
                face.vertices[i] = this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
                face.vertexNormals[i] = this.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
            return face;
        } else {
            if (!W_WavefrontObject.isValidFace_V_Line(line)) throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
            face.vertices = new W_Vertex[tokens.length];
            for (int i = 0; i < tokens.length; ++i) {
                face.vertices[i] = this.vertices.get(Integer.parseInt(tokens[i]) - 1);
            }
            face.faceNormal = face.calculateFaceNormal();
        }
        return face;
    }

    private W_GroupObject parseGroupObject(String line, int lineCount) throws _ModelFormatException {
        W_GroupObject group = null;
        if (W_WavefrontObject.isValidGroupObjectLine(line)) {
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            if (trimmedLine.length() > 0) {
                group = new W_GroupObject(trimmedLine);
            }
        } else {
            throw new _ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
        return group;
    }

    private static boolean isValidVertexLine(String line) {
        if (vertexMatcher != null) {
            vertexMatcher.reset();
        }
        vertexMatcher = vertexPattern.matcher(line);
        return vertexMatcher.matches();
    }

    private static boolean isValidVertexNormalLine(String line) {
        if (vertexNormalMatcher != null) {
            vertexNormalMatcher.reset();
        }
        vertexNormalMatcher = vertexNormalPattern.matcher(line);
        return vertexNormalMatcher.matches();
    }

    private static boolean isValidTextureCoordinateLine(String line) {
        if (textureCoordinateMatcher != null) {
            textureCoordinateMatcher.reset();
        }
        textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
        return textureCoordinateMatcher.matches();
    }

    private static boolean isValidFace_V_VT_VN_Line(String line) {
        if (face_V_VT_VN_Matcher != null) {
            face_V_VT_VN_Matcher.reset();
        }
        face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
        return face_V_VT_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_VT_Line(String line) {
        if (face_V_VT_Matcher != null) {
            face_V_VT_Matcher.reset();
        }
        face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
        return face_V_VT_Matcher.matches();
    }

    private static boolean isValidFace_V_VN_Line(String line) {
        if (face_V_VN_Matcher != null) {
            face_V_VN_Matcher.reset();
        }
        face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
        return face_V_VN_Matcher.matches();
    }

    private static boolean isValidFace_V_Line(String line) {
        if (face_V_Matcher != null) {
            face_V_Matcher.reset();
        }
        face_V_Matcher = face_V_Pattern.matcher(line);
        return face_V_Matcher.matches();
    }

    private static boolean isValidFaceLine(String line) {
        return W_WavefrontObject.isValidFace_V_VT_VN_Line(line) || W_WavefrontObject.isValidFace_V_VT_Line(line) || W_WavefrontObject.isValidFace_V_VN_Line(line) || W_WavefrontObject.isValidFace_V_Line(line);
    }

    private static boolean isValidGroupObjectLine(String line) {
        if (groupObjectMatcher != null) {
            groupObjectMatcher.reset();
        }
        groupObjectMatcher = groupObjectPattern.matcher(line);
        return groupObjectMatcher.matches();
    }

    @Override
    public String getType() {
        return "obj";
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
        return this.vertices.size();
    }

    @Override
    public int getFaceNum() {
        return this.getVertexNum() / 3;
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

