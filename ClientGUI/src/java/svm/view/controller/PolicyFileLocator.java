package svm.view.controller;

import java.io.*;

/**
 * Projectteam : Team C
 * Date: 29.10.12
 * Policy File Locator
 */
public class PolicyFileLocator {
    public static final String POLICY_FILE_NAME = "/client.policy";     //Filename unsers Policy Files

    /**
     * Methode zum Laden des Policy Files
     *
     * @return
     */
    public static String getLocationOfPolicyFile() {
        try {
            File tempFile = File.createTempFile("rmi-client", ".policy");  //Erzeuge Tempfile
            InputStream is = PolicyFileLocator.class.getResourceAsStream(POLICY_FILE_NAME);  //Hole File
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            int read = 0;
            while ((read = is.read()) != -1) {
                writer.write(read);
            }
            writer.close();
            tempFile.deleteOnExit();
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
