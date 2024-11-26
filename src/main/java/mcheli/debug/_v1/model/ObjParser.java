/*
 * Decompiled with CFR 0.152.
 */
package mcheli.debug._v1.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import mcheli.__helper.debug.DebugException;
import mcheli.debug._v1.model.ObjModel;
import mcheli.debug._v1.model._Face;
import mcheli.debug._v1.model._GroupObject;
import mcheli.debug._v1.model._TextureCoord;
import mcheli.debug._v1.model._Vertex;

public class ObjParser {
    private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
    private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static Pattern groupObjectPattern = Pattern.compile("([go]( [-\\$\\w\\d]+) *\\n)|([go]( [-\\$\\w\\d]+) *$)");

    public static ObjModel parse(InputStream inputStream) throws DebugException {
        ArrayList<_Vertex> vertices = new ArrayList<_Vertex>();
        ArrayList<_TextureCoord> textureCoordinates = new ArrayList<_TextureCoord>();
        ArrayList<_Vertex> vertexNormals = new ArrayList<_Vertex>();
        ArrayList<_GroupObject> groupObjects = new ArrayList<_GroupObject>();
        Object group = null;
        int vertexNum = 0;
        int faceNum = 0;
        BufferedReader reader = null;
        String currentLine = null;
        int lineCount = 0;
        try {
            Object group2;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((currentLine = reader.readLine()) != null) {
                _Vertex vertex;
                ++lineCount;
                if ((currentLine = currentLine.replaceAll("\\s+", " ").trim()).startsWith("#") || currentLine.length() == 0) continue;
                if (currentLine.startsWith("v ")) {
                    vertex = ObjParser.parseVertex(currentLine, lineCount);
                    if (vertex == null) continue;
                    vertices.add(vertex);
                    continue;
                }
                if (currentLine.startsWith("vn ")) {
                    vertex = ObjParser.parseVertexNormal(currentLine, lineCount);
                    if (vertex == null) continue;
                    vertexNormals.add(vertex);
                    continue;
                }
                if (currentLine.startsWith("vt ")) {
                    _TextureCoord textureCoordinate = ObjParser.parseTextureCoordinate(currentLine, lineCount);
                    if (textureCoordinate == null) continue;
                    textureCoordinates.add(textureCoordinate);
                    continue;
                }
                if (currentLine.startsWith("f ")) {
                    _Face face;
                    if (group == null) {
                        group = _GroupObject.builder().name("Default");
                    }
                    if ((face = ObjParser.parseFace(currentLine, lineCount, vertices, textureCoordinates, vertexNormals)) == null) continue;
                    ((_GroupObject.Builder)group).addFace(face);
                    continue;
                }
                if (!(currentLine.startsWith("g ") | currentLine.startsWith("o ")) || currentLine.charAt(2) != '$') continue;
                group2 = ObjParser.parseGroupObject(currentLine, lineCount);
                if (group2 != null && group != null) {
                    groupObjects.add(((_GroupObject.Builder)group).build());
                }
                group = group2;
            }
            groupObjects.add(((_GroupObject.Builder)group).build());
            group2 = new ObjModel(groupObjects, vertexNum, faceNum);
            return group2;
        }
        catch (IOException e) {
            throw new DebugException("IO Exception reading model format", e);
        }
        finally {
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

    private static _Vertex parseVertex(String line, int lineCount) throws DebugException {
        _Vertex vertex;
        block5: {
            vertex = null;
            if (ObjParser.isValidVertexLine(line)) {
                line = line.substring(line.indexOf(" ") + 1);
                String[] tokens = line.split(" ");
                try {
                    if (tokens.length == 2) {
                        return new _Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]));
                    }
                    if (tokens.length == 3) {
                        return new _Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    }
                    break block5;
                }
                catch (NumberFormatException e) {
                    throw new DebugException(String.format("Number formatting error at line %d", lineCount), e);
                }
            }
            throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        }
        return vertex;
    }

    private static _Vertex parseVertexNormal(String line, int lineCount) throws DebugException {
        _Vertex vertexNormal;
        block4: {
            vertexNormal = null;
            if (ObjParser.isValidVertexNormalLine(line)) {
                line = line.substring(line.indexOf(" ") + 1);
                String[] tokens = line.split(" ");
                try {
                    if (tokens.length == 3) {
                        return new _Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    }
                    break block4;
                }
                catch (NumberFormatException e) {
                    throw new DebugException(String.format("Number formatting error at line %d", lineCount), e);
                }
            }
            throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        }
        return vertexNormal;
    }

    private static _TextureCoord parseTextureCoordinate(String line, int lineCount) throws DebugException {
        _TextureCoord textureCoordinate;
        block5: {
            textureCoordinate = null;
            if (ObjParser.isValidTextureCoordinateLine(line)) {
                line = line.substring(line.indexOf(" ") + 1);
                String[] tokens = line.split(" ");
                try {
                    if (tokens.length == 2) {
                        return new _TextureCoord(Float.parseFloat(tokens[0]), 1.0f - Float.parseFloat(tokens[1]));
                    }
                    if (tokens.length == 3) {
                        return new _TextureCoord(Float.parseFloat(tokens[0]), 1.0f - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
                    }
                    break block5;
                }
                catch (NumberFormatException e) {
                    throw new DebugException(String.format("Number formatting error at line %d", lineCount), e);
                }
            }
            throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        }
        return textureCoordinate;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static _Face parseFace(String line, int lineCount, List<_Vertex> vertices, List<_TextureCoord> textureCoordinates, List<_Vertex> vertexNormals) throws DebugException {
        _Face face = null;
        if (!ObjParser.isValidFaceLine(line)) throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        String trimmedLine = line.substring(line.indexOf(" ") + 1);
        String[] tokens = trimmedLine.split(" ");
        String[] subTokens = null;
        int[] verticesID = new int[tokens.length];
        _Vertex[] verts = new _Vertex[tokens.length];
        _TextureCoord[] texCoords = new _TextureCoord[tokens.length];
        _Vertex[] normals = new _Vertex[tokens.length];
        if (ObjParser.isValidFace_V_VT_VN_Line(line)) {
            int i = 0;
            while (i < tokens.length) {
                subTokens = tokens[i].split("/");
                verticesID[i] = Integer.parseInt(subTokens[0]) - 1;
                verts[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                texCoords[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                normals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                ++i;
            }
            return new _Face(verticesID, verts, normals, texCoords);
        }
        if (ObjParser.isValidFace_V_VT_Line(line)) {
            int i = 0;
            while (i < tokens.length) {
                subTokens = tokens[i].split("/");
                verticesID[i] = Integer.parseInt(subTokens[0]) - 1;
                verts[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                texCoords[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                ++i;
            }
            return new _Face(verticesID, verts, new _Vertex[0], texCoords);
        }
        if (ObjParser.isValidFace_V_VN_Line(line)) {
            int i = 0;
            while (i < tokens.length) {
                subTokens = tokens[i].split("//");
                verticesID[i] = Integer.parseInt(subTokens[0]) - 1;
                verts[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                normals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                ++i;
            }
            return new _Face(verticesID, verts, normals, new _TextureCoord[0]);
        }
        if (!ObjParser.isValidFace_V_Line(line)) throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        int i = 0;
        while (i < tokens.length) {
            verticesID[i] = Integer.parseInt(tokens[0]) - 1;
            verts[i] = vertices.get(Integer.parseInt(tokens[0]) - 1);
            ++i;
        }
        return new _Face(verticesID, verts, new _Vertex[0], new _TextureCoord[0]);
    }

    private static _GroupObject.Builder parseGroupObject(String line, int lineCount) throws DebugException {
        _GroupObject.Builder group = null;
        if (ObjParser.isValidGroupObjectLine(line)) {
            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            if (trimmedLine.length() > 0) {
                group = _GroupObject.builder().name(trimmedLine);
            }
        } else {
            throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        }
        return group;
    }

    private static boolean isValidVertexLine(String line) {
        return vertexPattern.matcher(line).matches();
    }

    private static boolean isValidVertexNormalLine(String line) {
        return vertexNormalPattern.matcher(line).matches();
    }

    private static boolean isValidTextureCoordinateLine(String line) {
        return textureCoordinatePattern.matcher(line).matches();
    }

    private static boolean isValidFace_V_VT_VN_Line(String line) {
        return face_V_VT_VN_Pattern.matcher(line).matches();
    }

    private static boolean isValidFace_V_VT_Line(String line) {
        return face_V_VT_Pattern.matcher(line).matches();
    }

    private static boolean isValidFace_V_VN_Line(String line) {
        return face_V_VN_Pattern.matcher(line).matches();
    }

    private static boolean isValidFace_V_Line(String line) {
        return face_V_Pattern.matcher(line).matches();
    }

    private static boolean isValidFaceLine(String line) {
        return ObjParser.isValidFace_V_VT_VN_Line(line) || ObjParser.isValidFace_V_VT_Line(line) || ObjParser.isValidFace_V_VN_Line(line) || ObjParser.isValidFace_V_Line(line);
    }

    private static boolean isValidGroupObjectLine(String line) {
        return groupObjectPattern.matcher(line).matches();
    }
}

