/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import svm.ejb.*;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.view.forms.*;

/**
 *
 * @author Patrick
 */
public class ApplicationController implements Serializable {

    // @EJB
    // private static SubTeamMessageBeanRemote subTeamMessageBean;
    // @EJB
    // private static MemberMessageBeanRemote memberMessageBean;
    // @EJB
    // public static SvmSessionBeanRemote sessionBean;
    @EJB
    public static TeamBeanRemote teamBean;
    @EJB
    public static SubTeamConfirmationBeanRemote subTeamConfirmationBean;
    @EJB
    public static SubTeamBeanRemote subTeamBean;
    @EJB
    public static SearchBeanRemote searchBean;
    @EJB
    public static MemberBeanRemote memberBean;
    @EJB
    public static ContestConfirmationBeanRemote contestConfirmationBean;
    @EJB
    public static ContestBeanRemote contestBean;
    /**
     * The two main forms login and main applicationwindow
     *
     */
    private static MainForm mainForm;
    static Object notAuthMessage = "Sie sind nicht berechtigt.";
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
    private static int MESSAGE_COUNT = 0;

    public ApplicationController() {
    }

    public void init() throws LogicException, PersistenceException {
        this.panelContests = new PanelContests();
        this.panelMembers = new PanelMembers();
        this.panelMessages = new PanelMessages();
        this.viewContestCtrl = new ViewContestController(panelContests);
        this.viewMemberCtrl = new ViewMemberController(panelMembers);
        //this.viewRightsHandler = new ViewRightsHandler(sessionBean.getAuthObject(), this);
        this.viewRightsHandler = new ViewRightsHandler(null, this);
    }

    public static void main(String args[]) throws LogicException, PersistenceException {
        try {
            
             Properties props = new Properties();
             props.put("logoString", "SVM");
             com.jtattoo.plaf.acryl.AcrylLookAndFeel.setCurrentTheme(props);
             UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");             
             new ApplicationController().startMainForm();
             
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startMainForm() throws LogicException, PersistenceException {
        init();
        String username = "TEST-USER";
        mainForm = new MainForm(this);
        mainForm.setLblUser(username);
        mainForm.getLblPrivileges().setVisible(false);
        loadPrivileges();

        mainForm.pack();
        mainForm.setSize(995, 740);
        mainForm.setLocationRelativeTo(null);
        mainForm.setVisible(true);

        mainForm.toFront();
        mainForm.setAutoRequestFocus(true);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainForm.toFront();
                mainForm.repaint();
            }
        });

//        memberMessageBean.start();
//
//        Timer timer1 = new Timer();
//        timer1.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    List<MemberMessage> ls = memberMessageBean.updateMessages();
//                    for (MemberMessage m : ls) {
//                        onMessage(m);
//                    }
//                } catch (JMSException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (LogicException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (PersistenceException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            public void onMessage(MemberMessage imm) {
//                if (!imm.getType().equals(MessageType.REMOVED)) {
//                    panelMessages.addMemberMsg(imm);
//                    if (!mainForm.getTabPanelMainCenter().getSelectedComponent().equals(panelMessages)) {
//                        incrementMessageCount();
//                    }
//                }
//                if (imm.getType().equals(MessageType.NEW)) {
//                    try {
//                        searchBean.start();
//                        panelMessages.showMembersToAssign(searchBean.getMemberByUID(imm.getMember()));
//                        searchBean.commit();
//                        javax.swing.JOptionPane.showMessageDialog(mainForm, "Sie haben eine neue Nachricht.");
//                    } catch (PersistenceException ex) {
//                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (DomainException ex) {
//                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (LogicException ex) {
//                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                }
//            }
//        }, 60000);
//
//        subTeamMessageBean.start();
//
//        Timer timer2 = new Timer();
//        timer2.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    List<SubTeamMessage> ls = subTeamMessageBean.updateMessages();
//                    for (SubTeamMessage m : ls) {
//                        onMessage(m);
//                    }
//                } catch (JMSException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (LogicException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (PersistenceException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//            public void onMessage(SubTeamMessage istm) {
//                if (!istm.getType().equals(MessageType.REMOVED)) {
//                    javax.swing.JOptionPane.showMessageDialog(mainForm, "Sie haben eine neue Nachricht.");
//                    panelMessages.addSubTeamMessage(istm);
//                    if (!mainForm.getTabPanelMainCenter().getSelectedComponent().equals(panelMessages)) {
//                        incrementMessageCount();
//                    }
//                }
//            }
//        }, 60000);
    }

    public void loadPrivileges() {
        panelMembers.setName("Mitglieder");
        panelContests.setName("Wettbewerbe");
        panelMessages.setName(/*
                 * "(" + MESSAGE_COUNT + ") " +
                 */"Nachrichten");
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
        return ApplicationController.mainForm;
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