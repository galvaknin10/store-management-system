package model.report;

import java.util.HashMap;
import java.util.Map;

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
    protected void addReport(Report report) {
        reports.put(report.getDay(), report);
    }

    // Remove a report by it's year
    protected boolean removeReport(String day) {
        return reports.remove(day) == null;
    }

    // Retrieve all reports
    public Map<String, Report> getAllReports() {
        return reports;
    }

    // Find a report by it's year
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

    // Save report for the branch
    protected void saveReports(String branch) {
        ReportFileHandler.saveReportsToFile(reports, branch);
    }
}
