package server.models.employee;

import server.models.log.LogController;
import server.models.credentials.CredentialController;


public class EmployeeController {

    // Add a employee to the repository
    public static boolean removeEmployee(String branch, String userName) {
        EmployeeManager employeeManager = EmployeeManager.getInstance(branch);
        String name = employeeManager.getEmployee(userName).getName();
        boolean removedFromEmployees = employeeManager.removeEmployee(userName, branch);
        boolean isUserNameRemoved = CredentialController.removeUser(userName, branch);

        if (removedFromEmployees && isUserNameRemoved) {
            LogController.logEmployeeRemoval(branch, name);
            return true;
        }
        return false;
    }

    // Remove employee from the repository
    public static boolean addEmployee(String branch, String employeeId, String name, String phoneNumber, String accountNumber, String role, String userName) {
        EmployeeManager employeeManager = EmployeeManager.getInstance(branch);
        Employee employee = new Employee(name, employeeId, phoneNumber, accountNumber, branch, role, userName);
        boolean isAdded = employeeManager.addEmployee(employee, branch);
        if (isAdded) {
            LogController.logEmployeeCreation(branch, name);
            return true;
        }
        return false;
    }
}
