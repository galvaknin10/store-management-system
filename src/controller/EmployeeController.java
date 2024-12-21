package controller;
import model.employee.*;

public class EmployeeController {

    public static void addEmployeeToRepo(Employee employee, EmployeeManager employeeManager, String branch) {
        employeeManager.addEmployee(employee);
        employeeManager.saveEmployees(branch);
    }

    public static boolean removeEmployeeFromRepo(EmployeeManager employeeManager,String employeeId, String branch) {
        boolean isRemoveSucceed = employeeManager.removeEmployee(employeeId);
        if (isRemoveSucceed) {
            employeeManager.saveEmployees(branch);
        }
        return isRemoveSucceed;
    }
}
