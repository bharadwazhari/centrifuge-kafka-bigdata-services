package com.streaming.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.streaming.utilities.JsonProcessor;
import com.streaming.utilities.SpecInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;

public class JsonProcessorTest {

    private JsonParser parser = null;
    private JsonProcessor processor = null;

    @BeforeEach
    void init() {
        parser = new JsonParser();
        processor = new JsonProcessor();
    }

    @Test
    void processSimpleJsonTest() throws IOException {
        JsonElement element = parser.parse(new JsonReader(new StringReader(getJSONContentFromClasspath("test-spec.json"))));
        SpecInfo sinfo = new JsonProcessor().processJsonSpecRoot(element);
        String json = getJSONContentFromClasspath("test.json");
        JsonReader reader = new JsonReader(new StringReader(json));
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));
        String outjson = processor.process(reader, writer, sinfo);
        Assertions.assertNotNull(outjson);
        writer.flush();
        System.out.println("\n");
    }

    @Test
    void processArrayTypeJsonTest() throws IOException {
        JsonElement element = parser.parse(new JsonReader(new StringReader(getJSONContentFromClasspath("books-spec.json"))));
        SpecInfo sinfo = new JsonProcessor().processJsonSpecRoot(element);
        String json = getJSONContentFromClasspath("books.json");
        JsonReader reader = new JsonReader(new StringReader(json));
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));
        String outjson = processor.process(reader, writer, sinfo);
        Assertions.assertNotNull(outjson);
        writer.flush();
        System.out.println("\n");
    }

    @Test
    void processPropertiesJsonTest() throws IOException {
        JsonElement element = parser.parse(new JsonReader(new StringReader(getJSONContentFromClasspath("citylots-spec.json"))));
        SpecInfo sinfo = new JsonProcessor().processJsonSpecRoot(element);
        String json = getJSONContentFromClasspath("citylots.json");
        JsonReader reader = new JsonReader(new StringReader(json));
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(System.out));
        String outjson = processor.process(reader, writer, sinfo);
        Assertions.assertNotNull(outjson);
        writer.flush();
        System.out.println("\n");
    }

    private static String getJSONContentFromClasspath(String fileName) {
        URI uri = null;
        String content = "{}";
        try {
            uri = ClassLoader.getSystemResource(fileName).toURI();
            String mainPath = Paths.get(uri).toString();
            content = new String(Files.readAllBytes(Paths.get(mainPath)));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
