/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import svm.logic.abstraction.controller.IMessageController;
import svm.logic.abstraction.exception.IllegalGetInstanceException;
import svm.logic.abstraction.jmsobjects.IMemberMessage;
import svm.logic.abstraction.jmsobjects.IMessageObserver;
import svm.logic.abstraction.jmsobjects.ISubTeamMessage;
import svm.logic.abstraction.jmsobjects.MessageType;
import svm.logic.abstraction.transferobjects.ITransferAuth;
import svm.persistence.abstraction.exceptions.ExistingTransactionException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NoTransactionException;
import svm.persistence.abstraction.exceptions.NotSupportedException;
import svm.rmi.abstraction.controller.IRMIMessageController;
import svm.rmi.abstraction.controller.IRMISearchController;
import svm.rmi.abstraction.factory.IRMIControllerFactory;
import svm.view.forms.LoginForm;
import svm.view.forms.MainForm;
import svm.view.forms.PanelContests;
import svm.view.forms.PanelMembers;
import svm.view.forms.PanelMessages;

/**
 *
 * @author Patrick
 */
public class ApplicationController {

    /* Factory */
    public static IRMIControllerFactory factory;
    public static ITransferAuth user;
    /**
     * The two main forms login and main applicationwindow
     *
     */
    private static LoginForm loginForm;
    private static MainForm mainForm;
    /**
     * Panels according to the UseCases
     *
     */
    private PanelContests panelContests;
    private PanelMembers panelMembers;
    private PanelMessages panelMessages;
    /**
     * UseCase Controller
     *
     */
    private ViewContestController viewContestCtrl;
    private ViewMemberController viewMemberCtrl;
    private ViewRightsHandler viewRightsHandler;
    private IRMIMessageController messageController;
    private static int MESSAGE_COUNT = 0;

    public ApplicationController() {
    }

    public void init() {
        this.panelContests = new PanelContests();
        this.panelMembers = new PanelMembers();
        this.panelMessages = new PanelMessages();
        this.viewContestCtrl = new ViewContestController(panelContests);
        this.viewMemberCtrl = new ViewMemberController(panelMembers);
        this.viewRightsHandler = new ViewRightsHandler(this.user, this);
    }

    public static void main(String args[]) throws UnknownHostException {

        try {
            String policy = PolicyFileLocator.getLocationOfPolicyFile();
            System.setProperty("java.security.policy", policy);
            System.out.println("Policy: " + policy);

            System.setSecurityManager(new RMISecurityManager());

            String ip = InetAddress.getLocalHost().getHostAddress();
            if (args.length > 0) {
                ip = args[0];
            }

            try {
                //  ip="172.16.63.174";
                //Lookup Objekt    Hole ATM Fabrik
                factory = (IRMIControllerFactory) Naming.lookup("rmi://" + ip + ":1099/RMI");
            } catch (MalformedURLException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Starte die Testmethoden
            System.out.println("Path: rmi://" + ip + ":1099/RMI");
            System.out.println("Client runs");
            //IRMIContestController contestController = factory.getRMIContestController();

            // Setup the look and feel properties
            Properties props = new Properties();

            // This property sets the text which will be rendered on the left side of pop-up menus.
            props.put("logoString", "SVM");

            // To remove the bar painted in pop-up menus you have to put an empty string as the logo string
            // props.put("logoString", "");

            // To paint an empty bar just put a non breakeable space as logo string
            // props.put("logoString", "\u00A0");

            // Set theme
            com.jtattoo.plaf.acryl.AcrylLookAndFeel.setCurrentTheme(props);

            // select the look and feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            //Image icon = Toolkit.getDefaultToolkit().getImage("/svm/view/resources/svm_lg_140.png");


            // Start the application
            startSVM();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


    }

    private static void startSVM() {

        loginForm = new LoginForm(factory);
        loginForm.pack();
        loginForm.setSize(640, 550);
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
    }

    private void startMainForm(String username) throws RemoteException {
        init();
        mainForm = new MainForm(this);
        mainForm.setLblUser(username);
        mainForm.getLblPrivileges().setVisible(false);
        loadPrivileges();

        mainForm.pack();
        mainForm.setSize(995, 740);
        mainForm.setLocationRelativeTo(null);
        mainForm.setVisible(true);

        mainForm.toFront();
        loginForm.setVisible(false);
        mainForm.setAutoRequestFocus(true);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainForm.toFront();
                mainForm.repaint();
            }
        });

        
        this.messageController = factory.getRMIMessageController(user);
        this.messageController.addObserver(new IMessageObserver() {
           
            @Override
            public void updateMemberMessage(IMemberMessage imm) {

                if (!imm.getType().equals(MessageType.REMOVED)) {
                    panelMessages.addMemberMsg(imm);
                    if (!mainForm.getTabPanelMainCenter().getSelectedComponent().equals(panelMessages)) {
                        incrementMessageCount();
                    }
                }

                if (imm.getType().equals(MessageType.NEW)) {

                    try {
                        javax.swing.JOptionPane.showMessageDialog(mainForm,"Sie haben eine neue Nachricht.");
                        IRMISearchController search = factory.getRMISearchController(user);
                        search.start();
                        panelMessages.showMembersToAssign(search.getMemberByUID(imm.getMember()));
                        search.commit();
                    } catch (ExistingTransactionException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoTransactionException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InstantiationException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NotSupportedException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSessionFoundException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalGetInstanceException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void updateSubTeamMessage(ISubTeamMessage istm) {
                if (!istm.getType().equals(MessageType.REMOVED)) {
					javax.swing.JOptionPane.showMessageDialog(mainForm,"Sie haben eine neue Nachricht.");
                    panelMessages.addSubTeamMessage(istm);
                    if (!mainForm.getTabPanelMainCenter().getSelectedComponent().equals(panelMessages)) {
                        incrementMessageCount();
                    }
                }
            }
        });
        try {
            this.messageController.start();
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void login(String username, String password) {
        try {
            startMainForm(username);
        } catch (RemoteException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadPrivileges() {
        panelMembers.setName("Mitglieder");
        panelContests.setName("Wettbewerbe");
        panelMessages.setName(/*"(" + MESSAGE_COUNT + ") " +*/ "Nachrichten");
        mainForm.getTabPanelMainCenter().add(panelMembers);
        mainForm.getTabPanelMainCenter().add(panelContests);
        mainForm.getTabPanelMainCenter().add(panelMessages);
        viewRightsHandler.setRights();
    }

    public void switchMainTab() {

        if (mainForm.getTabPanelMainCenter().getSelectedComponent().getName().equalsIgnoreCase("Mitglieder")) {
            panelMembers.getViewMemberController().showDepartments();
        } else if (mainForm.getTabPanelMainCenter().getSelectedComponent().getName().equalsIgnoreCase("Wettbewerbe")) {
            panelContests.getViewContestController().showContests();
        }
    }

    public MainForm getMainForm() {
        return this.mainForm;
    }

    public PanelContests getPanelContests() {
        return panelContests;
    }

    public PanelMembers getPanelMembers() {
        return panelMembers;
    }

    public PanelMessages getPanelMessages() {
        return panelMessages;
    }

    public static void incrementMessageCount() {
        MESSAGE_COUNT++;
    }

    public static void decrementMessageCount() {
        MESSAGE_COUNT--;
    }

    public void refreshMessages() {
        mainForm.getTabPanelMainCenter().remove(panelMessages);
        mainForm.getTabPanelMainCenter().insertTab("(" + MESSAGE_COUNT + ") " + "Nachrichten", null, panelMessages, null, 1);
    }
}