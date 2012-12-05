/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import svm.ejb.MemberBeanRemote;
import svm.ejb.SearchBeanRemote;
import svm.ejb.dto.DepartmentDTO;
import svm.ejb.dto.MemberDTO;
import svm.ejb.dto.SportDTO;
import svm.ejb.dto.UserPrivilegeDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.view.forms.PanelMembers;

/**
 *
 * @author Patrick
 */
public class ViewMemberController {

    private MemberBeanRemote memberController = ApplicationController.memberBean;
    private SearchBeanRemote searchController = ApplicationController.searchBean;
    private PanelMembers panelMembers;
    private DefaultListModel listboxActiveRoles = new DefaultListModel();
    private DefaultListModel listboxAllRoles = new DefaultListModel();
    private DefaultListModel<MemberDTO> listboxShowMembers = new DefaultListModel();
    private DefaultComboBoxModel<SportDTO> cmbSport = new DefaultComboBoxModel();
    private boolean isCmbSportInitialized = false;

    public ViewMemberController(PanelMembers panelMembers) {
        this.panelMembers = panelMembers;
    }

    public void searchMembers() {
        try {
            this.searchController.start();

            List<MemberDTO> members = this.searchController.getMembers(
                    panelMembers.getTfSearchFirstName().getText(),
                    panelMembers.getTfSearchLastName().getText());//,chosenDepartment,panelMembers.getCbxSearchFee().isSelected());
            listboxShowMembers = new DefaultListModel();
            for (MemberDTO m : members) {
                listboxShowMembers.addElement(m);
            }
            panelMembers.getListboxShowMembers().setModel(listboxShowMembers);

            this.searchController.commit();
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showMembers() {
        try {
            MemberDTO member = (MemberDTO) panelMembers.getListboxShowMembers().getSelectedValue();
            if (member == null) {
                return;
            }
            if (this.memberController != null) {
                try {
                    this.memberController.abort();
                } catch (LogicException ex) {
                    Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PersistenceException ex) {
                    Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.memberController.start(member);
            MemberDTO tmp = this.memberController.getMember();
            // panelMembers.getTfFirstName().setText(tmp.getFirstName());
            // panelMembers.getTfLastName().setText(tmp.getLastName());

            showMemberDetails(tmp);

            listboxActiveRoles = new DefaultListModel();
            listboxAllRoles = new DefaultListModel();
            panelMembers.getListboxActiveRoles().setModel(listboxActiveRoles);
            panelMembers.getListboxAllRoles().setModel(listboxAllRoles);

            for (UserPrivilegeDTO privilege : memberController.getPrivileges()) {
                listboxActiveRoles.addElement(privilege);
            }

            searchController.start();
            for (UserPrivilegeDTO privilege : searchController.getUserPrivileges()) {
                int i = 0;
                boolean contains = false;
                while ((i < listboxActiveRoles.getSize()) && contains == false) {
                    UserPrivilegeDTO privFromActiveRoles = (UserPrivilegeDTO) listboxActiveRoles.getElementAt(i);
                    if (privilege.getName().equals(privFromActiveRoles.getName())) {
                        contains = true;
                    }
                    i++;
                }
                if (contains == false) {
                    listboxAllRoles.addElement(privilege);
                }
            }
            searchController.commit();
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveMember() {
        try {
            searchController.start();
            List<SportDTO> sport = searchController.getSports();
            searchController.commit();
            for (SportDTO sp : sport) {
                if (sp.equals(this.panelMembers.getCmbSport().getSelectedItem())) {
                    try {
                        this.memberController.setSport(sp);
                    } catch (PersistenceException ex) {
                        Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            this.memberController.setFirstName(panelMembers.getTfFirstName().getText());
            this.memberController.setLastName(panelMembers.getTfLastName().getText());
            this.memberController.setBirthDate(panelMembers.getDcBirthDate().getDate());
            this.memberController.setEntryDate(panelMembers.getDcEntryDate().getDate());
            this.memberController.setGender(panelMembers.getCmbGender().getSelectedItem().toString());
            this.memberController.setSocialNumber(panelMembers.getTfSocialNumber().getText());
            this.memberController.setTitle(" ");
            this.memberController.setEmail1(panelMembers.getTfMail1().getText());
            this.memberController.setEmail2(panelMembers.getTfMail2().getText());
            this.memberController.setPhone1(panelMembers.getTfPhone1().getText());
            this.memberController.setPhone2(panelMembers.getTfPhone2().getText());
            this.memberController.setSport((SportDTO) this.panelMembers.getCmbSport().getSelectedItem());
            if (panelMembers.getCheckMemberFee().isEnabled() && panelMembers.getCheckMemberFee().isSelected()) {
            }
            this.memberController.setStreet(panelMembers.getTfStreet().getText());
            this.memberController.setStreetNumber(panelMembers.getTfStreetNumber().getText());
            this.memberController.setUsername(panelMembers.getTfUserName().getText());
            this.memberController.commit();
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createNewMember() {
        try {
            clearMemberFields();

            if (this.memberController != null) {
                try {
                    this.memberController.abort();
                } catch (LogicException ex) {
                    Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PersistenceException ex) {
                    Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            this.memberController.start();
            MemberDTO tmp = this.memberController.getMember();
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showDepartments() {
        try {
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            this.searchController.start();
            for (DepartmentDTO department : searchController.getDepartments()) {
                model.addElement(department);
            }
            if (isCmbSportInitialized == false) {
                List<SportDTO> sports = searchController.getSports();
                for (SportDTO sp : sports) {
                    this.cmbSport.addElement(sp);
                }
                this.panelMembers.getCmbSport().setModel(cmbSport);
                isCmbSportInitialized = true;
            }
            panelMembers.getCmbSearchDepartment().setModel(model);
            this.searchController.commit();
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showMemberDetails(MemberDTO tmp) {
        panelMembers.getTfFirstName().setText(tmp.getFirstName());
        panelMembers.getTfLastName().setText(tmp.getLastName());
        if (tmp.getGender().equalsIgnoreCase("m")) {
            panelMembers.getCmbGender().setSelectedIndex(0);
        } else {
            panelMembers.getCmbGender().setSelectedIndex(1);
        }
        panelMembers.getTfUserName().setText(tmp.getUserName());
        panelMembers.getTfMail1().setText(tmp.getEmail1());
        panelMembers.getTfMail2().setText(tmp.getEmail2());
        panelMembers.getTfPhone1().setText(tmp.getPhone1());
        panelMembers.getTfSocialNumber().setText(tmp.getSocialNumber());
        panelMembers.getTfStreet().setText(tmp.getStreet());
        panelMembers.getTfStreetNumber().setText(tmp.getStreetNumber());
        panelMembers.getDcBirthDate().setDate(tmp.getBirthDate());
        panelMembers.getDcEntryDate().setDate(tmp.getEntryDate());
        panelMembers.getCheckMemberFee().setSelected(tmp.isHasPaidFee());
        if (tmp.isHasPaidFee()) {
            panelMembers.getCheckMemberFee().setEnabled(false);
        }
        this.cmbSport.setSelectedItem(tmp.getSport());
    }

    private void clearMemberFields() {

        panelMembers.getTfFirstName().setText("");
        panelMembers.getTfLastName().setText("");
        panelMembers.getTfUserName().setText("");
        panelMembers.getTfMail1().setText("");
        panelMembers.getTfMail2().setText("");
        panelMembers.getTfPhone1().setText("");
        panelMembers.getTfSocialNumber().setText("");
        panelMembers.getTfStreet().setText("");
        panelMembers.getTfStreetNumber().setText("");
        panelMembers.getDcBirthDate().setDate(new Date());
        panelMembers.getDcEntryDate().setDate(new Date());
        panelMembers.getCheckMemberFee().setSelected(false);
    }

    public void addPrivilege() {
        UserPrivilegeDTO privilege = (UserPrivilegeDTO) this.panelMembers.getListboxAllRoles().getSelectedValue();

        if (privilege != null) {
            try {
                memberController.addPrivilege(privilege);
                listboxActiveRoles.addElement(privilege);

                int i = 0;
                while (i < listboxAllRoles.getSize()) {
                    UserPrivilegeDTO privFromAllRoles = (UserPrivilegeDTO) listboxAllRoles.getElementAt(i);
                    if (privFromAllRoles.getName().equalsIgnoreCase(privilege.getName())) {
                        listboxAllRoles.remove(i);
                    }
                    i++;
                }
            } catch (LogicException ex) {
                Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DomainException ex) {
                Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void removePrivilege() {
        UserPrivilegeDTO privilege = (UserPrivilegeDTO) this.panelMembers.getListboxActiveRoles().getSelectedValue();

        if (privilege != null) {
            try {
                this.listboxAllRoles.addElement(privilege);
                memberController.removePrivilege(privilege);

                int i = 0;
                while (i < listboxActiveRoles.getSize()) {
                    UserPrivilegeDTO privFromAllRoles = (UserPrivilegeDTO) listboxActiveRoles.getElementAt(i);
                    if (privFromAllRoles.getName().equalsIgnoreCase(privilege.getName())) {
                        listboxActiveRoles.remove(i);
                    }
                    i++;
                }
            } catch (LogicException ex) {
                Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DomainException ex) {
                Logger.getLogger(ViewMemberController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clearMemberList() {
        listboxShowMembers.clear();
    }

    public void selectFirstMember() {
        panelMembers.getListboxShowMembers().setSelectedIndex(0);
    }
}
