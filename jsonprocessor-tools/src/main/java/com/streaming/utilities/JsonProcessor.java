package com.streaming.utilities;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonProcessor {

    public String process(final JsonReader reader, final JsonWriter writer, SpecInfo sinfo) throws IOException {
        String lnpath = StringUtils.EMPTY, pnode = StringUtils.EMPTY, mnode = StringUtils.EMPTY;
        while (true) {
            JsonToken token = reader.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    reader.beginArray();
                    writer.beginArray();
                    break;
                case END_ARRAY:
                    reader.endArray();
                    writer.endArray();
                    break;
                case BEGIN_OBJECT:
                    reader.beginObject();
                    writer.beginObject();
                    if(StringUtils.EMPTY.equals(lnpath)) lnpath = pnode;
                    break;
                case END_OBJECT:
                    reader.endObject();
                    writer.endObject();
                    lnpath = StringUtils.substringBeforeLast(lnpath, IJsonProcessorConstants.DOT);
                    break;
                case NAME:
                    String name = reader.nextName();
                    if(!StringUtils.EMPTY.equals(lnpath)) lnpath = lnpath + IJsonProcessorConstants.DOT + name;
                    String ctoken = StringUtils.EMPTY.equals(lnpath) ? name : lnpath;
                    mnode = ctoken;
                    String content = validateElementContent(ctoken, sinfo);
                    pnode = name;
                    if(IJsonProcessorConstants.SKIP.equals(content))  {
                        reader.skipValue();
                        lnpath = StringUtils.removeEnd(lnpath, IJsonProcessorConstants.DOT + pnode);
                    } else {
                        writer.name(name);
                    }
                    break;
                case STRING:
                    String s = reader.nextString();
                    String outText = validateTextContent(mnode, s, sinfo);
                    lnpath = StringUtils.removeEnd(lnpath, IJsonProcessorConstants.DOT + pnode);
                    pnode = StringUtils.EMPTY;
                    writer.value(outText);
                    break;
                case NUMBER:
                    String n = reader.nextString();
                    String outnum = validateTextContent(mnode, n, sinfo);
                    lnpath = StringUtils.removeEnd(lnpath, IJsonProcessorConstants.DOT + pnode);
                    pnode = StringUtils.EMPTY;
                    writer.value(new BigDecimal(outnum));
                    break;
                case BOOLEAN:
                    boolean b = reader.nextBoolean();
                    lnpath = StringUtils.removeEnd(lnpath, IJsonProcessorConstants.DOT + pnode);
                    pnode = StringUtils.EMPTY;
                    writer.value(b);
                    break;
                case NULL:
                    reader.nextNull();
                    lnpath = StringUtils.removeEnd(lnpath, IJsonProcessorConstants.DOT + pnode);
                    pnode = StringUtils.EMPTY;
                    writer.nullValue();
                    break;
                case END_DOCUMENT:
                    return writer.toString();
            }
        }
    }//end of process

    private String validateElementContent(String name, SpecInfo sinfo) {
        String token = IJsonProcessorConstants.SKIP;
        if(isPartOfSpecList(name, sinfo.getRlist()))
            return token;
        return displayText(name, sinfo.getDlist(), sinfo.getMlist());
    }

    private String validateTextContent(String name, String content, SpecInfo sinfo) {
        String token = content;
        if(isPartOfSpecList(name, sinfo.getMlist()))
            token =  maskText(content);
        return token;
    }

    public boolean isPartOfSpecList(String token, List<SpecNode> rlist) {
        boolean isPartOfSpecList = CollectionUtils.isEmpty(rlist);
        if(isPartOfSpecList) return !isPartOfSpecList;
        isPartOfSpecList = rlist.stream().filter(t -> t.getJpath().equals(token)).findFirst().isPresent();
        return isPartOfSpecList;
    }

    private String maskText(String text) {
        return StringUtils.repeat(IJsonProcessorConstants.MASK_CHAR, text.length());
    }

    private String displayText(String text, List<SpecNode> dlist, List<SpecNode> mlist) {
        if(CollectionUtils.isEmpty(dlist)
                || isPartOfSpecList(text, dlist)
                || isPartOfSpecList(text, mlist)) return text;
        else return IJsonProcessorConstants.SKIP;
    }

    private String parentPathIsPresent(String text, List<SpecNode> plist) {
        return "";
    }

    public SpecInfo processJsonSpecRoot(JsonElement e) {
        List<String> paths = new ArrayList<>();
        processJsonSpecElement(e, paths, StringUtils.EMPTY);
        SpecInfo specInfo = prepareSpecification(paths);
        return specInfo;
    }

    private void processJsonSpecElement(JsonElement e, List<String> paths, String parent) {
        if (e.isJsonArray()) {
            processJsonArray(e.getAsJsonArray(), paths, parent);
        } else if (e.isJsonNull()) {
            processJsonNull(e.getAsJsonNull(), paths, parent);
        } else if (e.isJsonObject()) {
            processJsonObject(e.getAsJsonObject(), paths, parent);
        } else if (e.isJsonPrimitive()) {
            processJsonPrimitive(e.getAsJsonPrimitive(), paths, parent);
        }
    }

    private void processJsonArray(JsonArray a, List<String> paths, String parent) {
        for (JsonElement e : a) {
            processJsonSpecElement(e, paths, parent);
        }
    }

    private void processJsonNull(JsonNull n, List<String> paths, String parent) {
        paths.add(n.getAsString());
    }

    private void processJsonObject(JsonObject o, List<String> paths, String parent) {
        Set<Map.Entry<String, JsonElement>> members= o.entrySet();
        for (Map.Entry<String, JsonElement> e : members) {
            String cparent = prepareTraversePath(e, paths, parent);
            processJsonSpecElement(e.getValue(), paths, cparent);
        }
    }

    private void processJsonPrimitive(JsonPrimitive p, List<String> paths, String parent) {
       // System.out.println("Leaf Node:"+p.getAsString());
    }

    private String prepareTraversePath(Map.Entry<String, JsonElement> e, List<String> paths, String parent) {
        if(e.getValue().isJsonObject()) {
            parent = parent + (StringUtils.EMPTY.equals(parent) ? e.getKey() : IJsonProcessorConstants.DOT + e.getKey());
            paths.add(parent);
        } else if(e.getValue().isJsonPrimitive()) {
            if (StringUtils.EMPTY.equals(parent)) {
                paths.add(e.getKey()+ IJsonProcessorConstants.EQUALS + e.getValue().getAsString());
            } else if (!StringUtils.EMPTY.equals(parent)) {
                parent = parent + IJsonProcessorConstants.DOT + e.getKey();
                paths.add(parent + IJsonProcessorConstants.EQUALS + e.getValue().getAsString());
                parent = StringUtils.removeEnd(parent, IJsonProcessorConstants.DOT + e.getKey().toString());
            }
        }
        return parent;
    }

    private SpecInfo prepareSpecification (List<String> paths) {
        SpecInfo sinfo = new SpecInfo();
        for(String path : paths) {
            SpecNode snode = new SpecNode();
            snode.setJpath(StringUtils.substringBeforeLast(path, IJsonProcessorConstants.EQUALS));
            snode.setOperation(StringUtils.substringAfterLast(path, IJsonProcessorConstants.EQUALS));
            switch(snode.getOperation()) {
                case "!" : sinfo.getRlist().add(snode);
                           break;
                case "*" : sinfo.getMlist().add(snode);
                           break;
                default  : sinfo.getDlist().add(snode);
                           break;

            }
        }
        return sinfo;
    }

}
