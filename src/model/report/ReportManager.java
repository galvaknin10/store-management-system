package model.report;

import java.util.HashMap;
import java.util.Map;


public class ReportManager {
    private static final Map<String, ReportManager> instances = new HashMap<>();

    static {
        // Automatically create instances for predefined branches
        initializeBranch("EILAT");
        initializeBranch("JERUSALEM");
    }
    
    private Map<Integer, Report> reports;
    
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
    public void addReport(Report report) {
        reports.put(report.getYear(), report);
    }

    // Remove a report by it's year
    public boolean removeReport(int year) {
        return reports.remove(year) == null;
    }

    // Retrieve all reports
    public Map<Integer, Report> getAllReports() {
        return reports;
    }

    // Find a report by it's year
    public Report findReportByYear(int year) {
        return reports.get(year);
    }

    // Save report for the branch
    public void saveReports(String branch) {
        ReportFileHandler.saveReportsToFile(reports, branch);
    }
}
