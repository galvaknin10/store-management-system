package model.credentials;


public class CredentialController {
    public static void addUserToRepo(CredentialsManager credentailManager, String userName, String password, String branch) {
        credentailManager.addCredentials(userName, password, branch);
        credentailManager.saveCredentials(branch);
    }

    public static boolean removeUserFromRepo(CredentialsManager credentailManager, String userName, String branch) {
        boolean isRemoveSuceed = credentailManager.removeCredentials(userName);
        if (isRemoveSuceed) {
            credentailManager.saveCredentials(branch);
        }
        return isRemoveSuceed;
    }
}