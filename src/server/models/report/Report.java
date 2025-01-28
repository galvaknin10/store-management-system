package server.models.report;

import java.util.Map;


public class Report {
    private String branch;
    private String date;
    private Map<String, Integer> salesData;
    private int totalSales;


    public Report(String branch, String date, Map<String, Integer> salesData) {
        this.branch = branch;
        this.date = date;
        this.salesData = salesData;

        // Calculate total sales based on salesData
        this.totalSales = salesData.values().stream().mapToInt(Integer::intValue).sum();
    }

    public String getBranch() {
        return branch;
    }

    public String getDate() {
        return date;
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
}
