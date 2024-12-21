package model.report;

import java.util.Map;

public class Report {
    private String branchName;
    private int year;
    private Map<String, Integer> salesData; // Product/Category -> Total Sales
    private int totalSales;

    // Constructor
    public Report(String branchName, int year, Map<String, Integer> salesData) {
        this.branchName = branchName;
        this.year = year;
        this.salesData = salesData;

        // Calculate total sales based on salesData
        this.totalSales = salesData.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Getters
    public String getBranchName() {
        return branchName;
    }

    public int getYear() {
        return year;
    }

    public Map<String, Integer> getSalesData() {
        return salesData;
    }

    public int getTotalSales() {
        return totalSales;
    }

    // Generate a nicely formatted report
    public String generateReportString() {
        StringBuilder report = new StringBuilder();
        report.append("Branch: ").append(branchName).append("\n")
              .append("Year: ").append(year).append("\n")
              .append("-----------------------------------\n")
              .append("Product/Category        Total Sales\n")
              .append("-----------------------------------\n");

        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            report.append(String.format("%-20s %10d\n", entry.getKey(), entry.getValue()));
        }

        report.append("-----------------------------------\n")
              .append("Total Sales for Branch: ").append(totalSales).append("\n");
        return report.toString();
    }

    @Override
    public String toString() {
        return generateReportString();
    }
}
