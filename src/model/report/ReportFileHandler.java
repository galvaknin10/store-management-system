package model.report;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReportFileHandler {


    // Save customers to JSON file
    protected static void saveReportsToFile(Map<String, Report> reports, String branch) {
        String fileName = "data/" + branch + "_reports.json"; 
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Dynamically create the file name based on the branch
    
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(reports, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while trying to append the report to the repository.");
        }
    }


    protected static Map<String, Report> loadReportsFromFile(String branch) {
        String fileName = "data/" + branch + "_reports.json"; // File name for branch-specific customers
        File file = new File(fileName);
    
        if (!file.exists()) {
            System.out.println(branch + " is a brand new branch with no report information available yet.");
            Map<String, Report> reportRepo = new HashMap<>();
            createJsonFile(reportRepo, fileName);

        }
    
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type reportMaType = new TypeToken<Map<String, Report>>() {}.getType();
            return gson.fromJson(reader, reportMaType);
        } catch (IOException e) {
            System.err.println("Error loading reports for branch '" + branch + "': " + e.getMessage());
        }
        return new HashMap<>(); // Return an empty map in case of error
    }


    public static void createDefaultReportsRepo() {
        File eilatFile = new File("data/EILAT_reports.json");
        File jerusalemFile = new File("data/JERUSALEM_reports.json");
    
        if (!eilatFile.exists()) {
            Map<String, Report> eilatRepo = new TreeMap<>();
    
            // Creating inventory as HashMap for each report
            Map<String, Integer> eilatDay1 = new HashMap<>();
            eilatDay1.put("iPhone 14", 50);
            eilatDay1.put("Samsung Galaxy S23", 40);
            eilatDay1.put("Google Pixel 8", 30);
            eilatRepo.put("27/12/2024", new Report("Eilat", "27/12/2024", eilatDay1));
    
            Map<String, Integer> eilatDay2 = new HashMap<>();
            eilatDay2.put("iPhone 14", 60);
            eilatDay2.put("Samsung Galaxy S23", 50);
            eilatDay2.put("Google Pixel 8", 35);
            eilatRepo.put("28/12/2024", new Report("Eilat", "28/12/2024", eilatDay2));
    
            Map<String, Integer> eilatDay3 = new HashMap<>();
            eilatDay3.put("iPhone 14", 70);
            eilatDay3.put("Samsung Galaxy S23", 55);
            eilatDay3.put("Google Pixel 8", 40);
            eilatRepo.put("29/12/2024", new Report("Eilat", "29/12/2024", eilatDay3));
    
            createJsonFile(eilatRepo, "data/EILAT_reports.json");
        }
    
        if (!jerusalemFile.exists()) {
            Map<String, Report> jerusalemRepo = new TreeMap<>();
    
            // Creating inventory as HashMap for each report
            Map<String, Integer> jerusalemDay1 = new HashMap<>();
            jerusalemDay1.put("iPhone 14", 45);
            jerusalemDay1.put("Samsung Galaxy S23", 35);
            jerusalemDay1.put("OnePlus 11", 25);
            jerusalemRepo.put("27/12/2024", new Report("Jerusalem", "27/12/2024", jerusalemDay1));
    
            Map<String, Integer> jerusalemDay2 = new HashMap<>();
            jerusalemDay2.put("iPhone 14", 50);
            jerusalemDay2.put("Samsung Galaxy S23", 40);
            jerusalemDay2.put("OnePlus 11", 30);
            jerusalemRepo.put("28/12/2024", new Report("Jerusalem", "28/12/2024", jerusalemDay2));
    
            Map<String, Integer> jerusalemDay3 = new HashMap<>();
            jerusalemDay3.put("iPhone 14", 55);
            jerusalemDay3.put("Samsung Galaxy S23", 45);
            jerusalemDay3.put("OnePlus 11", 35);
            jerusalemRepo.put("29/12/2024", new Report("Jerusalem", "29/12/2024", jerusalemDay3));
    
            createJsonFile(jerusalemRepo, "data/JERUSALEM_reports.json");
        }
    }
    

    // Create a default JSON file with sample customers for a specific branch
    private static void createJsonFile(Map<String, Report> reportRepo, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(reportRepo, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while creating specific JSON file: '" + fileName + "': " + e.getMessage());
        }
    }
}