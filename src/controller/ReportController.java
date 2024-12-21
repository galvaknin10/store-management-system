package controller;

import model.report.*;

public class ReportController {
        public static void addGeneratedReportTORepo(ReportManager reportManager, Report report, String branch) {
        reportManager.addReport(report);
        reportManager.saveReports(branch);
    }

    public static boolean RemoveCustomerFromRepo(ReportManager reportManager, int year, String branch) {
        boolean isRemoveSucceed = reportManager.removeReport(year);
        if (isRemoveSucceed) {
            reportManager.saveReports(branch);
        }
        return isRemoveSucceed;
    }
}
