/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import svm.logic.abstraction.exception.IllegalGetInstanceException;
import svm.logic.abstraction.exception.NotAllowException;
import svm.logic.abstraction.jmsobjects.IMemberMessage;
import svm.logic.abstraction.jmsobjects.ISubTeamMessage;
import svm.logic.abstraction.transferobjects.ITransferMember;
import svm.logic.abstraction.transferobjects.ITransferSubTeam;
import svm.logic.abstraction.transferobjects.ITransferTeam;
import svm.persistence.abstraction.exceptions.ExistingTransactionException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NoTransactionException;
import svm.persistence.abstraction.exceptions.NotSupportedException;
import svm.rmi.abstraction.controller.IRMIContestController;
import svm.rmi.abstraction.controller.IRMIMemberController;
import svm.rmi.abstraction.controller.IRMISearchController;
import svm.rmi.abstraction.controller.IRMISubTeamConfirmationController;
import svm.rmi.abstraction.factory.IRMIControllerFactory;
import svm.view.forms.PanelMessages;

/**
 *
 * @author Patrick
 */
public class ViewMessagesController {

    private IRMIControllerFactory factory = ApplicationController.factory;
    private IRMIContestController contestController;
    private IRMISearchController searchController;
    private PanelMessages panelMessages;
    private DefaultListModel listboxActiveRoles = new DefaultListModel();
    private DefaultComboBoxModel<ITransferTeam> cmbSelectTeam = new DefaultComboBoxModel();
    private DefaultListModel<ITransferMember> listboxNewMembersToAssign = new DefaultListModel();
    private DefaultListModel<String> listboxLog = new DefaultListModel();
    private DefaultListModel<ITransferMember> listboxMembersOfSelectedTeam = new DefaultListModel();
    private DefaultListModel<ITransferTeam> listboxAllTeamsInSport = new DefaultListModel<>();
    private DefaultListModel<ISubTeamMessage> listboxAssignedContests = new DefaultListModel<>();
    private ITransferSubTeam subteam;

    public ViewMessagesController(PanelMessages panelMessages) {
        try {
            this.panelMessages = panelMessages;
            this.searchController = factory.getRMISearchController(ApplicationController.user);
            //initializeModels();
        } catch (RemoteException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptSelectedContests() {
        try {
            ISubTeamMessage msg = (ISubTeamMessage) this.panelMessages.getListboxAssignedContests().getSelectedValue();
            searchController.start();
            ITransferMember member = searchController.getMemberByUID(msg.getMember());
            ITransferSubTeam subTeam = searchController.getSubTeam(msg.getSubTeam());
            searchController.commit();
            IRMISubTeamConfirmationController subTeamConfirmationController = this.factory.getRMISubTeamConfirmationController(ApplicationController.user, member, subTeam);
            subTeamConfirmationController.start();
            subTeamConfirmationController.setConfirmation(true, "");
            subTeamConfirmationController.commit();

            int i = 0;
            while (i < this.listboxAssignedContests.getSize()) {
                if (this.listboxAssignedContests.getElementAt(i).equals(msg)) {
                    this.listboxAssignedContests.remove(i);
                    break;
                }
            }
            javax.swing.JOptionPane.showMessageDialog(this.panelMessages, "Sie haben Ihre Teilnahme bestätigt.");
            ApplicationController.decrementMessageCount();

        } catch (InstantiationException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void denySelectedContests() {
        try {
            ISubTeamMessage msg = (ISubTeamMessage) this.panelMessages.getListboxAssignedContests().getSelectedValue();
            searchController.start();
            ITransferMember member = searchController.getMemberByUID(msg.getMember());
            ITransferSubTeam subTeam = searchController.getSubTeam(msg.getSubTeam());
            searchController.commit();
            IRMISubTeamConfirmationController subTeamConfirmationController = this.factory.getRMISubTeamConfirmationController(ApplicationController.user, member, subTeam);
            subTeamConfirmationController.start();
            subTeamConfirmationController.setConfirmation(false, "");
            subTeamConfirmationController.commit();

            this.panelMessages.getListboxAssignedContests().updateUI();
            System.out.println(this.listboxAssignedContests.getSize());
            int i = 0;
            while (i < this.listboxAssignedContests.getSize()) {
                if (this.listboxAssignedContests.getElementAt(i).equals(msg)) {
                    this.listboxAssignedContests.remove(i);
                    break;
                }
            }
            ApplicationController.decrementMessageCount();

        } catch (InstantiationException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveTeamMembers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showMembersToAssign(ITransferMember imm) {


        this.panelMessages.getListboxNewMembersToAssign().setModel(listboxNewMembersToAssign);
        this.listboxNewMembersToAssign.addElement(imm);
    }

    /**
     * Show all teams
     *
     *
     */
    public void showAllTeams() {
        this.panelMessages.getCmbSelectTeam().setModel(cmbSelectTeam);
        if (this.panelMessages.getCmbSelectTeam().getModel().getSize() == 0) {
            try {
                searchController.start();
                try {
                    for (ITransferTeam team : searchController.getTeams()) {
                        cmbSelectTeam.addElement(team);
                    }
                } catch (IllegalGetInstanceException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSessionFoundException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotAllowException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    searchController.commit();
                } catch (ExistingTransactionException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSessionFoundException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoTransactionException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (NoSessionFoundException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalGetInstanceException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addMemberMsg(IMemberMessage imm) {
        try {
            searchController.start();
            ITransferMember member = searchController.getMemberByUID(imm.getMember());
            searchController.commit();
            this.listboxLog.addElement("MemberMessage: " + imm.getType().toString() + " " + member.getFirstName() + " " + member.getLastName());
            this.panelMessages.getListboxLog().setModel(this.listboxLog);
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSubTeamMsg(ISubTeamMessage tm) {
        try {
            searchController.start();
            ITransferMember member = searchController.getMemberByUID(tm.getMember());
            ITransferSubTeam subTeam = searchController.getSubTeam(tm.getSubTeam());
            searchController.commit();
            this.listboxLog.addElement("SubTeamMessage: " + subTeam.getName() + " " + tm.getType().toString() + " " + member.getFirstName() + " " + member.getLastName());
            this.panelMessages.getListboxLog().setModel(this.listboxLog);
            this.listboxAssignedContests.addElement(tm);
            this.panelMessages.getListboxAssignedContests().setModel(listboxAssignedContests);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Weist Listboxen/Comboboxen Models zu
     */
    private void initializeModels() {
        this.panelMessages.getCmbSelectTeam().setModel(cmbSelectTeam);
        this.panelMessages.getListboxNewMembersToAssign().setModel(listboxNewMembersToAssign);
        this.panelMessages.getListboxLog().setModel(listboxLog);
        this.panelMessages.getListboxMembersOfSelectedTeam().setModel(listboxMembersOfSelectedTeam);
        this.panelMessages.getListboxAssignedContests().setModel(listboxAssignedContests);
    }

    public void assignMemberToTeam() {
        this.panelMessages.getListboxMembersOfSelectedTeam().setModel(listboxMembersOfSelectedTeam);
        try {
            ITransferMember member = (ITransferMember) this.panelMessages.getListboxNewMembersToAssign().getSelectedValue();
            if (member != null) {
                IRMIMemberController memberController = factory.getRMIMemberController(member, ApplicationController.user);
                memberController.start();
                memberController.addMemberToTeam((ITransferTeam) this.panelMessages.getCmbSelectTeam().getSelectedItem());
                memberController.commit();
                this.listboxMembersOfSelectedTeam.addElement(member);
                //this.panelMessages.getListboxMembersOfSelectedTeam().updateUI();
                this.listboxMembersOfSelectedTeam.getSize();
                this.listboxNewMembersToAssign.getSize();

                //this.panelMessages.getListboxNewMembersToAssign().remove(this.panelMessages.getListboxNewMembersToAssign().getSelectedIndex());
                //this.listboxNewMembersToAssign.removeElement(member);
                ApplicationController.decrementMessageCount();
                /*int i = 0;
                while (i < this.listboxNewMembersToAssign.getSize()) {
                    if (this.listboxNewMembersToAssign.getElementAt(i).equals(member)) {
                        //this.listboxNewMembersToAssign.remove(i);
                        this.listboxNewMembersToAssign.removeElement(member);
                        break;
                    }
                    i++;
                }*/
                //this.panelMessages.getListboxNewMembersToAssign().setModel(listboxNewMembersToAssign);
            }
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showTeamMembers() {
            

    }
}