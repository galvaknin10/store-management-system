package model.report;

import java.util.Map;

public class Report {
    private String branchName;
    private String day;
    private Map<String, Integer> salesData; // Product(Smartphone)/ -> Total Sales
    private int totalSales;

    // Constructor
    public Report(String branchName, String day, Map<String, Integer> salesData) {
        this.branchName = branchName;
        this.day = day;
        this.salesData = salesData;

        // Calculate total sales based on salesData
        this.totalSales = salesData.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Getters
    public String getBranchName() {
        return branchName;
    }

    public String getDay() {
        return day;
    }

    public Map<String, Integer> getSalesData() {
        return salesData;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int sales) {
        this.totalSales = sales;
    }

    // Generate a nicely formatted report
    public String generateReportString() {
        StringBuilder report = new StringBuilder();
        report.append("Branch: ").append(branchName).append("\n")
              .append("Day: ").append(day).append("\n")
              .append("-----------------------------------\n")
              .append("Phones                  Total Sales\n")
              .append("-----------------------------------\n");

        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            report.append(String.format("%-20s %10d\n", entry.getKey(), entry.getValue()));
        }

        report.append("-----------------------------------\n")
              .append("Total Sales for Branch: ").append(totalSales).append(" Devices").append("\n");
        return report.toString();
    }

    @Override
    public String toString() {
        return generateReportString();
    }
}
