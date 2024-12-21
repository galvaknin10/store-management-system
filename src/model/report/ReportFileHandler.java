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

public class ReportFileHandler {


    // Save customers to JSON file
    public static void saveReportsToFile(Map<Integer, Report> reports, String branch) {
        String fileName = "data/" + branch + "_reports.json"; 
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Dynamically create the file name based on the branch
    
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(reports, writer);
            System.out.println("Report successfully appended to the repository.");
        } catch (IOException e) {
            System.err.println("Error occurred while trying to append the report to the repository.");
        }
    }


    public static Map<Integer, Report> loadReportsFromFile(String branch) {
        String fileName = "data/" + branch + "_reports.json"; // File name for branch-specific customers
        File file = new File(fileName);
    
        if (!file.exists()) {
            System.out.println(branch + " is a brand new branch with no report information available yet.");
            Map<Integer, Report> reportRepo = new HashMap<>();
            createJsonFile(reportRepo, fileName);

        }
    
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            Type reportMaType = new TypeToken<Map<Integer, Report>>() {}.getType();
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
            Map<Integer, Report> eilatRepo = new HashMap<>();
            eilatRepo.put(2020, new Report("Eilat", 2020, Map.of(
                "Smartphones", 120,
                "Laptops", 90,
                "Accessories", 150
            )));
            eilatRepo.put(2021, new Report("Eilat", 2021, Map.of(
                "Smartphones", 130,
                "Laptops", 80,
                "Accessories", 160
            )));
            eilatRepo.put(2022, new Report("Eilat", 2022, Map.of(
                "Smartphones", 140,
                "Laptops", 100,
                "Accessories", 170
            )));
            createJsonFile(eilatRepo, "data/EILAT_reports.json");
        }

        if (!jerusalemFile.exists()) {
            Map<Integer, Report> jerusalemRepo = new HashMap<>();
            jerusalemRepo.put(2020, new Report("Jerusalem", 2020, Map.of(
                "Smartphones", 100,
                "Laptops", 110,
                "Accessories", 140
            )));
            jerusalemRepo.put(2021, new Report("Jerusalem", 2021, Map.of(
                "Smartphones", 120,
                "Laptops", 130,
                "Accessories", 150
            )));
            jerusalemRepo.put(2022, new Report("Jerusalem", 2022, Map.of(
                "Smartphones", 150,
                "Laptops", 120,
                "Accessories", 160
            )));
            createJsonFile(jerusalemRepo, "data/JERUSALEM_reports.json");
        }
    }

    // Create a default JSON file with sample customers for a specific branch
    private static void createJsonFile(Map<Integer, Report> reportRepo, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(reportRepo, writer);
        } catch (IOException e) {
            System.err.println("Error occurred while creating specific JSON file: '" + fileName + "': " + e.getMessage());
        }
    }
}