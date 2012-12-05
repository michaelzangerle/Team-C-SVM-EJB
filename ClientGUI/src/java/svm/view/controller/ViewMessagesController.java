/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import svm.ejb.ContestBeanRemote;
import svm.ejb.MemberBeanRemote;
import svm.ejb.SearchBeanRemote;
import svm.ejb.SubTeamConfirmationBeanRemote;
import svm.ejb.dto.MemberDTO;
import svm.ejb.dto.SubTeamDTO;
import svm.ejb.dto.TeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.messages.MemberMessage;
import svm.view.forms.PanelMessages;

/**
 *
 * @author Patrick
 */
public class ViewMessagesController {

    @EJB
    private ContestBeanRemote contestController;
    @EJB
    private SearchBeanRemote searchController;
    @EJB
    private SubTeamConfirmationBeanRemote subTeamConfirmationController;
    
    @EJB
    private MemberBeanRemote memberController;
    private PanelMessages panelMessages;
    private DefaultListModel listboxActiveRoles = new DefaultListModel();
    private DefaultComboBoxModel<TeamDTO> cmbSelectTeam = new DefaultComboBoxModel();
    private DefaultListModel<MemberDTO> listboxNewMembersToAssign = new DefaultListModel();
    private DefaultListModel<String> listboxLog = new DefaultListModel();
    private DefaultListModel<MemberDTO> listboxMembersOfSelectedTeam = new DefaultListModel();
    private DefaultListModel<TeamDTO> listboxAllTeamsInSport = new DefaultListModel<TeamDTO>();
    private DefaultListModel<SubTeamDTO> listboxAssignedContests = new DefaultListModel<SubTeamDTO>();
    private SubTeamDTO subteam;

    public ViewMessagesController(PanelMessages panelMessages) {

        this.panelMessages = panelMessages;
        //initializeModels(); 
    }

    public void acceptSelectedContests() {
        try {
            SubTeamDTO msg = (SubTeamDTO) this.panelMessages.getListboxAssignedContests().getSelectedValue();
            searchController.start();
            MemberDTO member = searchController.getMemberByUID(msg.getMember());
            SubTeamDTO subTeam = searchController.getSubTeam(msg.getSubTeam());
            searchController.commit();
            subTeamConfirmationController.start(member, subTeam);
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
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void denySelectedContests() {
        try {
            SubTeamDTO msg = (SubTeamDTO) this.panelMessages.getListboxAssignedContests().getSelectedValue();
            searchController.start();
            MemberDTO member = searchController.getMemberByUID(msg.getMember());
            SubTeamDTO subTeam = searchController.getSubTeam(msg.getSubTeam());
            searchController.commit();
            subTeamConfirmationController.start(member,subTeam);
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
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveTeamMembers() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showMembersToAssign(MemberDTO imm) {


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

                for (TeamDTO team : searchController.getTeams()) {
                    cmbSelectTeam.addElement(team);
                }

                searchController.commit();
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DomainException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LogicException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addMemberMsg(MemberMessage imm) {
        try {
            searchController.start();
            MemberDTO member = searchController.getMemberByUID(imm.getMember());
            searchController.commit();
            this.listboxLog.addElement("MemberMessage: " + imm.getType().toString() + " " + member.getFirstName() + " " + member.getLastName());
            this.panelMessages.getListboxLog().setModel(this.listboxLog);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addSubTeamMsg(SubTeamDTO tm) {
        try {
            searchController.start();
            MemberDTO member = searchController.getMemberByUID(tm.getMember());
            SubTeamDTO subTeam = searchController.getSubTeam(tm.getSubTeam());
            searchController.commit();
            this.listboxLog.addElement("SubTeamMessage: " + subTeam.getName() + " " + tm.getType().toString() + " " + member.getFirstName() + " " + member.getLastName());
            this.panelMessages.getListboxLog().setModel(this.listboxLog);
            this.listboxAssignedContests.addElement(tm);
            this.panelMessages.getListboxAssignedContests().setModel(listboxAssignedContests);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
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

        MemberDTO member = (MemberDTO) this.panelMessages.getListboxNewMembersToAssign().getSelectedValue();
        if (member != null) {
            try {
                memberController.start(member);
                memberController.addMemberToTeam((TeamDTO) this.panelMessages.getCmbSelectTeam().getSelectedItem());
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
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DomainException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LogicException ex) {
                Logger.getLogger(ViewMessagesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
