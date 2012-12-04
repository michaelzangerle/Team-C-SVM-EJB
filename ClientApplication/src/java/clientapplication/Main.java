/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientapplication;

import javax.ejb.EJB;
import svm.ejb.MemberBeanRemote;

/**
 *
 * @author mike
 */
public class Main {
    @EJB
    private static MemberBeanRemote memberBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("hannes");
    }
}
