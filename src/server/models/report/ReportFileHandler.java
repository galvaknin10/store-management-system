package server.models.report;

import com.google.gson.reflect.TypeToken;
import server.utils.FileHandler;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;


public class ReportFileHandler {

    // Save reports to JSON file
    protected synchronized static void saveReportsToFile(Map<String, Report> reports, String branch) {
        String fileName = "data/" + branch + "_reports.json";
        FileHandler.createJsonFile(reports, fileName);
    }

    // Load reports from JSON file
    protected static Map<String, Report> loadReportsFromFile(String branch) {
        String fileName = "data/" + branch + "_reports.json";
        Type reportMapType = new TypeToken<Map<String, Report>>() {}.getType();

        Map<String, Report> reports = FileHandler.loadJsonFile(fileName, reportMapType);

        if (reports == null) {
            System.out.println(branch + " is a brand new branch with no report information available yet.");
            reports = new TreeMap<>();
            saveReportsToFile(reports, branch);
        }

        return reports;
    }

    // Create default report repositories
    public static void createDefaultReportsRepo() {
        FileHandler.ensureDataDirectoryExists();
    
        // Create Eilat reports file if it doesn't exist
        if (!FileHandler.fileExists("data/EILAT_reports.json")) {
            createDefaultRepo("data/EILAT_reports.json", Map.of(
                "27/12/2024", new Report("Eilat", "27/12/2024", Map.of(
                    "Men's Leather Jacket", 20, "Women's Winter Coat", 30, "Classic Blue Jeans", 50)),
                "28/12/2024", new Report("Eilat", "28/12/2024", Map.of(
                    "Formal Dress Shirt", 40, "Running Shoes", 25, "Summer T-Shirt", 60)),
                "29/12/2024", new Report("Eilat", "29/12/2024", Map.of(
                    "Casual Sneakers", 35, "Men's Leather Jacket", 18, "Women's Winter Coat", 28))
            ));
        }
    
        // Create Jerusalem reports file if it doesn't exist
        if (!FileHandler.fileExists("data/JERUSALEM_reports.json")) {
            createDefaultRepo("data/JERUSALEM_reports.json", Map.of(
                "27/12/2024", new Report("Jerusalem", "27/12/2024", Map.of(
                    "Men's Hoodie", 25, "Women's Maxi Dress", 15, "Leather Handbag", 10)),
                "28/12/2024", new Report("Jerusalem", "28/12/2024", Map.of(
                    "Men's Formal Trousers", 20, "Women's High Heels", 5, "Kids' Winter Jacket", 12)),
                "29/12/2024", new Report("Jerusalem", "29/12/2024", Map.of(
                    "Sports Backpack", 18, "Men's Hoodie", 22, "Women's Maxi Dress", 12))
            ));
        }
    }
    

    // Helper method to create a default repository
    private static void createDefaultRepo(String fileName, Map<String, Report> defaultRepo) {
        FileHandler.createJsonFile(defaultRepo, fileName);
    }
}
