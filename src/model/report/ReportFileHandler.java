package model.report;

import com.google.gson.reflect.TypeToken;
import utils.FileHandlerUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReportFileHandler {

    // Save reports to JSON file
    protected static void saveReportsToFile(Map<String, Report> reports, String branch) {
        String fileName = "data/" + branch + "_reports.json";
        FileHandlerUtils.createJsonFile(reports, fileName);
    }

    // Load reports from JSON file
    protected static Map<String, Report> loadReportsFromFile(String branch) {
        String fileName = "data/" + branch + "_reports.json";
        Type reportMapType = new TypeToken<Map<String, Report>>() {}.getType();

        Map<String, Report> reports = FileHandlerUtils.loadJsonFile(fileName, reportMapType);

        if (reports == null) {
            System.out.println(branch + " is a brand new branch with no report information available yet.");
            reports = new TreeMap<>();
            saveReportsToFile(reports, branch);
        }

        return reports;
    }

    // Create default report repositories
    public static void createDefaultReportsRepo() {
        FileHandlerUtils.ensureDataDirectoryExists();

        // Create Eilat reports file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/EILAT_reports.json")) {
            createDefaultRepo("data/EILAT_reports.json", Map.of(
                "27/12/2024", new Report("Eilat", "27/12/2024", Map.of(
                    "iPhone 14", 50, "Samsung Galaxy S23", 40, "Google Pixel 8", 30)),
                "28/12/2024", new Report("Eilat", "28/12/2024", Map.of(
                    "iPhone 14", 60, "Samsung Galaxy S23", 50, "Google Pixel 8", 35)),
                "29/12/2024", new Report("Eilat", "29/12/2024", Map.of(
                    "iPhone 14", 70, "Samsung Galaxy S23", 55, "Google Pixel 8", 40))
            ));
        }

        // Create Jerusalem reports file if it doesn't exist
        if (!FileHandlerUtils.fileExists("data/JERUSALEM_reports.json")) {
            createDefaultRepo("data/JERUSALEM_reports.json", Map.of(
                "27/12/2024", new Report("Jerusalem", "27/12/2024", Map.of(
                    "iPhone 14", 45, "Samsung Galaxy S23", 35, "OnePlus 11", 25)),
                "28/12/2024", new Report("Jerusalem", "28/12/2024", Map.of(
                    "iPhone 14", 50, "Samsung Galaxy S23", 40, "OnePlus 11", 30)),
                "29/12/2024", new Report("Jerusalem", "29/12/2024", Map.of(
                    "iPhone 14", 55, "Samsung Galaxy S23", 45, "OnePlus 11", 35))
            ));
        }
    }

    // Helper method to create a default repository
    private static void createDefaultRepo(String fileName, Map<String, Report> defaultRepo) {
        FileHandlerUtils.createJsonFile(defaultRepo, fileName);
    }
}
