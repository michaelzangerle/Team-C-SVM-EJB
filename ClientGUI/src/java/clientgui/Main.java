/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientgui;

import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.view.controller.ApplicationController;

/**
 *
 * @author Administrator
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws LogicException, PersistenceException {
        ApplicationController.main(args);
    }
}
