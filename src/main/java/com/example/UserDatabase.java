package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    private static final String USERS_FILE = "data/Users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static List<Users> users = new ArrayList<>();

    static {
        loadUsers(); // Load users at startup
    }

    public static void saveUsers() {
        try (Writer writer = new FileWriter(USERS_FILE)) {
            gson.toJson(users, new TypeToken<List<Users>>() {}.getType(), writer);  // ✅ Force JSON as an array
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            users = new ArrayList<>();
            return;
        }
    
        try (Reader reader = new FileReader(USERS_FILE)) {
            users = gson.fromJson(reader, new TypeToken<List<Users>>() {}.getType());
            if (users == null) {
                users = new ArrayList<>();  // ✅ Prevent null errors
            }
        } catch (Exception e) {
            System.out.println("Error loading users. Resetting file...");
            users = new ArrayList<>();
            saveUsers();  // ✅ Reset file if corrupt
        }
    }    

    // ✅ Check if Mobile Number Already Exists
    public static boolean isMobileNumberExists(String mobileNumber) {
        return users.stream().anyMatch(user -> user.getMobileNumber().equals(mobileNumber));
    }

    public static void addUser(Users user) {
        users.add(user);
        saveUsers();
    }

    public static Users findUserById(String userId) {
        return users.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
    }
}
