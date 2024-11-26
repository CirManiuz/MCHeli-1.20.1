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
import mcheli.__helper.debug.DebugException;
import mcheli.debug._v1.model.MqoModel;
import mcheli.debug._v1.model._Face;
import mcheli.debug._v1.model._GroupObject;
import mcheli.debug._v1.model._TextureCoord;
import mcheli.debug._v1.model._Vertex;

public class MqoParser {
    public static MqoModel parse(InputStream inputStream) throws DebugException {
        ArrayList<_Vertex> vertices = new ArrayList<_Vertex>();
        ArrayList<_Face> faceList = new ArrayList<_Face>();
        ArrayList<_GroupObject> groupObjects = new ArrayList<_GroupObject>();
        int vertexNum = 0;
        int faceNum = 0;
        BufferedReader reader = null;
        String currentLine = null;
        int lineCount = 0;
        try {
            Object group;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((currentLine = reader.readLine()) != null) {
                String[] s;
                if (!MqoParser.isValidGroupObjectLine(currentLine = currentLine.replaceAll("\\s+", " ").trim()) || (group = MqoParser.parseGroupObject(currentLine, ++lineCount)) == null) continue;
                vertices.clear();
                faceList.clear();
                int vertexNum2 = 0;
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
                    if (!MqoParser.isValidVertexLine(currentLine)) continue;
                    vertexNum2 = Integer.valueOf(currentLine.split(" ")[1]);
                    break;
                }
                if (vertexNum2 > 0) {
                    while ((currentLine = reader.readLine()) != null) {
                        ++lineCount;
                        s = (currentLine = currentLine.replaceAll("\\s+", " ").trim()).split(" ");
                        if (s.length == 3) {
                            _Vertex v = new _Vertex(Float.valueOf(s[0]).floatValue() / 100.0f, Float.valueOf(s[1]).floatValue() / 100.0f, Float.valueOf(s[2]).floatValue() / 100.0f);
                            vertices.add(v);
                            if (--vertexNum2 > 0) continue;
                            break;
                        }
                        if (s.length <= 0) continue;
                        throw new DebugException("format error : line=" + lineCount);
                    }
                    int faceNum2 = 0;
                    while ((currentLine = reader.readLine()) != null) {
                        ++lineCount;
                        if (!MqoParser.isValidFaceLine(currentLine = currentLine.replaceAll("\\s+", " ").trim())) continue;
                        faceNum2 = Integer.valueOf(currentLine.split(" ")[1]);
                        break;
                    }
                    if (faceNum2 > 0) {
                        while ((currentLine = reader.readLine()) != null) {
                            ++lineCount;
                            String[] s2 = (currentLine = currentLine.replaceAll("\\s+", " ").trim()).split(" ");
                            if (s2.length > 2) {
                                if (Integer.valueOf(s2[0]) >= 3) {
                                    _Face[] faces;
                                    for (_Face face : faces = MqoParser.parseFace(currentLine, lineCount, mirror, vertices)) {
                                        faceList.add(face);
                                    }
                                }
                                if (--faceNum2 > 0) continue;
                                break;
                            }
                            if (s2.length <= 2 || Integer.valueOf(s2[0]) == 3) continue;
                            throw new DebugException("found face is not triangle : line=" + lineCount);
                        }
                        for (_Face face : faceList) {
                            ((_GroupObject.Builder)group).addFace(face.calcVerticesNormal(faceList, shading, facet));
                        }
                    }
                }
                vertexNum += vertices.size();
                faceNum += ((_GroupObject.Builder)group).faceSize();
                vertices.clear();
                faceList.clear();
                groupObjects.add(((_GroupObject.Builder)group).build());
            }
            group = new MqoModel(groupObjects, vertexNum, faceNum);
            return group;
        }
        catch (IOException e) {
            throw new DebugException("IO Exception reading model format.", e);
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

    private static _GroupObject.Builder parseGroupObject(String line, int lineCount) throws DebugException {
        _GroupObject.Builder group = null;
        if (MqoParser.isValidGroupObjectLine(line)) {
            String[] s = line.split(" ");
            String trimmedLine = s[1].substring(1, s[1].length() - 1);
            if (trimmedLine.length() > 0) {
                group = _GroupObject.builder().name(trimmedLine);
            }
        } else {
            throw new DebugException("Error parsing entry ('" + line + "', line " + lineCount + ") in file - Incorrect format");
        }
        return group;
    }

    private static _Face[] parseFace(String line, int lineCount, boolean mirror, List<_Vertex> vertices) {
        String[] s = line.split("[ VU)(M]+");
        int vnum = Integer.valueOf(s[0]);
        if (vnum != 3 && vnum != 4) {
            return new _Face[0];
        }
        if (vnum == 3) {
            int[] verticesID = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
            _Vertex[] verts = new _Vertex[]{vertices.get(verticesID[0]), vertices.get(verticesID[1]), vertices.get(verticesID[2])};
            _TextureCoord[] texCoords = s.length >= 11 ? new _TextureCoord[]{new _TextureCoord(Float.valueOf(s[9]).floatValue(), Float.valueOf(s[10]).floatValue()), new _TextureCoord(Float.valueOf(s[7]).floatValue(), Float.valueOf(s[8]).floatValue()), new _TextureCoord(Float.valueOf(s[5]).floatValue(), Float.valueOf(s[6]).floatValue())} : new _TextureCoord[]{new _TextureCoord(0.0f, 0.0f), new _TextureCoord(0.0f, 0.0f), new _TextureCoord(0.0f, 0.0f)};
            return new _Face[]{new _Face(verticesID, verts, texCoords)};
        }
        int[] verticesID1 = new int[]{Integer.valueOf(s[3]), Integer.valueOf(s[2]), Integer.valueOf(s[1])};
        _Vertex[] verts1 = new _Vertex[]{vertices.get(verticesID1[0]), vertices.get(verticesID1[1]), vertices.get(verticesID1[2])};
        _TextureCoord[] texCoords1 = s.length >= 12 ? new _TextureCoord[]{new _TextureCoord(Float.valueOf(s[10]).floatValue(), Float.valueOf(s[11]).floatValue()), new _TextureCoord(Float.valueOf(s[8]).floatValue(), Float.valueOf(s[9]).floatValue()), new _TextureCoord(Float.valueOf(s[6]).floatValue(), Float.valueOf(s[7]).floatValue())} : new _TextureCoord[]{new _TextureCoord(0.0f, 0.0f), new _TextureCoord(0.0f, 0.0f), new _TextureCoord(0.0f, 0.0f)};
        int[] verticesID2 = new int[]{Integer.valueOf(s[4]), Integer.valueOf(s[3]), Integer.valueOf(s[1])};
        _Vertex[] verts2 = new _Vertex[]{vertices.get(verticesID2[0]), vertices.get(verticesID2[1]), vertices.get(verticesID2[2])};
        _TextureCoord[] texCoords2 = s.length >= 14 ? new _TextureCoord[]{new _TextureCoord(Float.valueOf(s[12]).floatValue(), Float.valueOf(s[13]).floatValue()), new _TextureCoord(Float.valueOf(s[10]).floatValue(), Float.valueOf(s[11]).floatValue()), new _TextureCoord(Float.valueOf(s[6]).floatValue(), Float.valueOf(s[7]).floatValue())} : new _TextureCoord[]{new _TextureCoord(0.0f, 0.0f), new _TextureCoord(0.0f, 0.0f), new _TextureCoord(0.0f, 0.0f)};
        return new _Face[]{new _Face(verticesID1, verts1, texCoords1), new _Face(verticesID2, verts2, texCoords2)};
    }

    private static boolean isValidGroupObjectLine(String line) {
        String[] s = line.split(" ");
        if (s.length < 2 || !s[0].equals("Object")) {
            return false;
        }
        return s[1].length() >= 4 && s[1].charAt(0) == '\"';
    }

    private static boolean isValidVertexLine(String line) {
        String[] s = line.split(" ");
        return s[0].equals("vertex");
    }

    private static boolean isValidFaceLine(String line) {
        String[] s = line.split(" ");
        return s[0].equals("face");
    }
}

