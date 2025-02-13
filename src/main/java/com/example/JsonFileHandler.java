package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;

public class JsonFileHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> readJsonFromFile(String filePath) {
        try {
            return objectMapper.readValue(new File(filePath), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public static void writeJsonToFile(String filePath, Map<String, Object> data) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filePath = "data/users.json";

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("First Name", "Gopi");
        userDetails.put("Last Name", "Krishna");
        userDetails.put("Date of Birth", "2000-01-30");
        userDetails.put("Password", "Qwerty@5");
        userDetails.put("Mobile Number", "1234567890");
        userDetails.put("Address", "Hyderabad");

        Map<String, Object> users = new HashMap<>();
        users.put("qwerty5", userDetails);

        writeJsonToFile(filePath, users);
        System.out.println("JSON written successfully!");

        Map<String, Object> jsonData = readJsonFromFile(filePath);
        System.out.println("Read JSON: " + jsonData);
    }
}
