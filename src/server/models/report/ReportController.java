package server.models.report;

import java.util.Map;


public class ReportController {

    public static boolean removeReport(String branch, String date) {
        ReportManager reportManager = ReportManager.getInstance(branch);
        boolean removed = reportManager.removeReport(branch, date);
        if (removed) {
            return true;
        }
        return false;
    }

    public static boolean updateReport(String branch, Map<String, Integer> cart) {
        ReportManager reportManager = ReportManager.getInstance(branch);
        boolean updated = reportManager.updateReport(branch, cart);
        if (updated) {
            return true;
        }
        return false;
    }
}
