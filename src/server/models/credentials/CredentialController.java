package server.models.credentials;

import server.models.log.LogController;


public class CredentialController {

    // Add a user to the repository
    public static boolean addUser(String userName, String password, String branch) {
        CredentialsManager credentialsManager = CredentialsManager.getInstance(branch);
        boolean isAdded = credentialsManager.addCredentials(userName, password, branch);
        if (isAdded) {
            LogController.logUserCreation(branch, userName);
            return true;
        }
        return false;
    }

    // Remove a user from the repository
    public static boolean removeUser(String userName, String branch) {
        CredentialsManager credentialsManager = CredentialsManager.getInstance(branch);
        boolean isRemove = credentialsManager.removeCredentials(userName, branch);
        if (isRemove) {
            LogController.logUserRemoval(branch, userName);
            return true;
        }
        return false;
    }
}
