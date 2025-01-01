package model.log;


public class LogController {
        public static void addRecordToLog(LogManager logManager, String action, String details) {
            logManager.addLog(action, details);
            logManager.savelog();
        }
}
