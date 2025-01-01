package model.report;

import java.util.HashMap;
import java.util.Map;

import model.inventory.InventoryManager;

public class ReportController {
        public static void addGeneratedReportToRepo(ReportManager reportManager, Report report, String branch) {
        reportManager.addReport(report);
        reportManager.saveReports(branch);
    }

    public static boolean RemoveReportFromRepo(ReportManager reportManager, String day, String branch) {
        boolean isRemoveSucceed = reportManager.removeReport(day);
        if (isRemoveSucceed) {
            reportManager.saveReports(branch);
        }
        return isRemoveSucceed;
    }


    public static void updateReport(Map<String, Integer> cart, String branch, InventoryManager inventoryManager, ReportManager reportManager) {
    // Get the current day (assuming you have a method to get the current day as a string)
    String currentDay = reportManager.getCurrentDay();

    // Fetch the report for the current day and branch
    Report currentReport = reportManager.getReport(currentDay);
    if (currentReport == null) {
        System.out.println("No report found for the current day. Creating a new report...");
        currentReport = new Report(branch, currentDay, new HashMap<>());
        addGeneratedReportToRepo(reportManager, currentReport, branch);
    }

    // Update sales data in the report
    Map<String, Integer> salesData = currentReport.getSalesData();
    for (Map.Entry<String, Integer> entry : cart.entrySet()) {
        String productId = entry.getKey();
        int quantity = entry.getValue();
        String productName = inventoryManager.getInventory().get(productId).getName();
        salesData.put(productName, salesData.getOrDefault(productName, 0) + quantity);
    }

    // Update total sales in the report
    int updatedTotalSales = salesData.values().stream().mapToInt(Integer::intValue).sum();
    currentReport.setTotalSales(updatedTotalSales);

    // Save the updated reports
    addGeneratedReportToRepo(reportManager, currentReport, branch);
}
}
