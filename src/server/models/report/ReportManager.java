package server.models.report;

import java.util.HashMap;
import java.util.Map;
import server.models.inventory.InventoryManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ReportManager {
    private static final Map<String, ReportManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }
    
    private Map<String, Report> reports;
    
    private ReportManager(String branch) {
        this.reports = ReportFileHandler.loadReportsFromFile(branch);
    }
    
    private static void initializeBranch(String branch) {
        ReportManager instance = new ReportManager(branch);
        instances.put(branch, instance);
    }
    
    public static ReportManager getInstance(String branch) {
        if (instances.containsKey(branch)) {
            return instances.get(branch);
        } else {
            System.out.println("Branch not recognized. Please verify the branch name and try again.");
            return null;  // Return null to indicate that the branch is not valid
        }
    }
    

    // Add a new report to the map
    protected void addReport(Report report, String branch) {
        reports.put(report.getDate(), report);
        ReportFileHandler.saveReportsToFile(reports, branch);
    }

    protected boolean updateReport(String branch, Map<String, Integer> cart) {
        if (cart == null) {
            return false;
        }

        InventoryManager inventoryManager = InventoryManager.getInstance(branch);
        String currentDay = getCurrentDay();

        // Fetch or create the report for the current day
        Report currentReport = getReport(currentDay);
        if (currentReport == null) {
            currentReport = new Report(branch, currentDay, new HashMap<>());
            addReport(currentReport, branch);
        }

        // Update the sales data in the report
        Map<String, Integer> salesData = currentReport.getSalesData();
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String SerialNum = entry.getKey();
            int quantity = entry.getValue();
            String productName = inventoryManager.getProduct(SerialNum).getName();
            salesData.put(productName, salesData.getOrDefault(productName, 0) + quantity);
        }

        // Update total sales in the report
        int updatedTotalSales = salesData.values().stream().mapToInt(Integer::intValue).sum();
        currentReport.setTotalSales(updatedTotalSales);

        // Save the updated report
        ReportFileHandler.saveReportsToFile(reports, branch);
        return true;
    }


    // Remove a report by it's year
    protected boolean removeReport(String day, String branch) {
        if (reports.remove(day) != null) {
            ReportFileHandler.saveReportsToFile(reports, branch);
            return true;
        }
        return false;
    }

    // Retrieve all reports
    public Map<String, Report> getAllReports() {
        return reports;
    }

    public Report getReport(String day) {
        return reports.get(day);
    }

    protected String getCurrentDay() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
    
        // Format the date as "d/M/yy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDate.format(formatter);
    }

}
