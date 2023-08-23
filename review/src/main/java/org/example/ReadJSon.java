package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadJSon {

    public JsonObject read(){
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get("tickets.json"));

        } catch (IOException e) {
            System.out.println("File not found, program will be stopped");
            System.exit(0);
        }
        return JsonParser.parseReader(reader).getAsJsonObject();
    }
}
