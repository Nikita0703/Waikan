package com.example.waikan.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ControllerUtils {

    public static List<String> getAllTowns() {
        List<String> towns = new ArrayList<>();
        String jsonFile = getJsonFile();
        try {
            JSONParser parser = new JSONParser();
            JSONArray townsInJson = (JSONArray) parser.parse(jsonFile);
            for (int i = 0; i < townsInJson.size(); i++) {
                JSONObject townInJson = (JSONObject) townsInJson.get(i);
                towns.add((String) townInJson.get("city"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return towns;
    }

    private static String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines =
                    Files.readAllLines(Paths.get("data/towns.json"));
            lines.forEach(line -> builder.append(line + "\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
