/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import svm.domain.abstraction.exception.DomainAttributeException;
import svm.domain.abstraction.exception.DomainParameterCheckException;
import svm.domain.abstraction.modelInterfaces.IContactDetails;
import svm.domain.abstraction.modelInterfaces.IContestHasTeam;
import svm.domain.abstraction.modelInterfaces.IDepartment;
import svm.domain.abstraction.modelInterfaces.IDepartmentsHasMembers;
import svm.domain.abstraction.modelInterfaces.IMember;
import svm.domain.abstraction.modelInterfaces.ISport;
import svm.domain.abstraction.modelInterfaces.ISubTeamsHasMembers;
import svm.domain.abstraction.modelInterfaces.IUserPrivilege;
import svm.domain.abstraction.modelInterfaces.IUserPrivilege.Privileges;
import svm.ejb.dto.AuthDTO;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NotSupportedException;
import svm.view.forms.MainForm;
import svm.view.forms.PanelContests;
import svm.view.forms.PanelMembers;
import svm.view.forms.PanelMessages;

/**
 *
 * @author Patrick
 */
public class ViewRightsHandler {

    public static class AuthDummyDTO extends AuthDTO {

        public AuthDummyDTO() {
            super(new IMember() {
                @Override
                public String getTitle() {
                    return "DUMMY";
                }

                @Override
                public void setTitle(String string) {
                }

                @Override
                public String getFirstName() {
                    return "Dummy";
                }

                @Override
                public void setFirstName(String string) throws DomainAttributeException {
                }

                @Override
                public String getLastName() {
                    return "Dummy";
                }

                @Override
                public void setLastName(String string) throws DomainAttributeException {
                }

                @Override
                public String getSocialNumber() {
                    return "Dummy";
                }

                @Override
                public void setSocialNumber(String string) throws DomainAttributeException {
                }

                @Override
                public Date getBirthDate() {
                    return new Date();
                }

                @Override
                public void setBirthDate(Date date) throws DomainParameterCheckException {
                }

                @Override
                public String getGender() {
                    return "M";
                }

                @Override
                public void setGender(String string) throws DomainAttributeException, DomainParameterCheckException {
                }

                @Override
                public Date getEntryDate() {
                    return new Date();
                }

                @Override
                public void setEntryDate(Date date) throws DomainParameterCheckException {
                }

                @Override
                public String getAvatar() {
                    return "DUMMY";
                }

                @Override
                public void setAvatar(String string) {
                }

                @Override
                public String getUrl() {
                    return "DUMMY";
                }

                @Override
                public void setUrl(String string) {
                }

                @Override
                public String getUserName() {
                    return "DUMMY";
                }

                @Override
                public void setUserName(String string) throws DomainAttributeException {
                }

                @Override
                public IContactDetails getContactDetails() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void setContactDetails(IContactDetails icd) throws DomainAttributeException {
                }

                @Override
                public Double getFee() {
                    return 5.0;
                }

                @Override
                public boolean hasPaidFee(Integer intgr) throws DomainParameterCheckException {
                    return true;
                }

                @Override
                public List<IContestHasTeam> getContestsHasTeamsForPerson() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public List<IDepartmentsHasMembers> getDepartmentsHasMembers() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public Boolean isIn(IDepartment id) {
                    return true;
                }

                @Override
                public Integer getAge() {
                    return 18;
                }

                @Override
                public List<ISubTeamsHasMembers> getSubTeamsHasMembersForPerson() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void setPaidCurrentYear() throws NoSessionFoundException, IllegalAccessException, InstantiationException, NotSupportedException {
                }

                @Override
                public Boolean isIn(Privileges prvlgs) {
                    return true;
                }

                @Override
                public void removePrivilege(IUserPrivilege iup) throws DomainParameterCheckException, DomainAttributeException {
                }

                @Override
                public void addPrivilege(IUserPrivilege iup) throws DomainParameterCheckException, DomainAttributeException, NoSessionFoundException, IllegalAccessException, InstantiationException {
                }

                @Override
                public List<IUserPrivilege> getPrivileges() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void setSport(ISport isport) {
                }

                @Override
                public ISport getSport() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public boolean isNull() {
                    return false;
                }

                @Override
                public Integer getUID() {
                    return 1;
                }
            });
        }

        @Override
        public boolean isAllowedForSearching() {
            return true;
        }

        @Override
        public boolean isAllowedForMemberViewing() {
            return true;
        }

        @Override
        public boolean isAllowedForMemberChanging() {
            return true;
        }

        @Override
        public boolean isAllowedForMemberDeleting() {
            return true;
        }

        @Override
        public boolean isAllowedForMemberAdding() {
            return true;
        }

        @Override
        public boolean isAllowedForMemberAddingPrivileges() {
            return true;
        }

        @Override
        public boolean isAllowedForContestViewing() {
            return true;
        }

        @Override
        public boolean isAllowedForContestDetailsChanging() {
            return true;
        }

        @Override
        public boolean isAllowedForContestDeleting() {
            return true;
        }

        @Override
        public boolean isAllowedForContestAdding() {
            return true;
        }

        @Override
        public boolean isAllowedForContestResultChanging() {
            return true;
        }

        @Override
        public boolean isAllowedForContestMatchAdding() {
            return true;
        }

        @Override
        public boolean isAllowedForContestTeamsChanging() {
            return true;
        }

        @Override
        public boolean isAllowedForContestSubTeamChanging() {
            return true;
        }
    }
    private final AuthDTO user;
    private final ApplicationController appController;
    private MainForm mainForm;
    private PanelContests panelContests;
    private PanelMembers panelMembers;
    private PanelMessages panelMessages;
    private JTabbedPane tabPanelContestDetails;
    private HashMap<String, JPanel> mainPanelsByName;
    private HashMap<String, JPanel> contestSubPanelsByName;

    public ViewRightsHandler(AuthDTO user, ApplicationController appController) {
        this.appController = appController;
        if (user == null) {
            this.user = new AuthDummyDTO();
        } else {
            this.user = user;
        }
        this.contestSubPanelsByName = new HashMap<String, JPanel>();
        this.mainPanelsByName = new HashMap<String, JPanel>();
    }

    public void setRights() {
        init();

        /**
         * View_Only *
         */
        if (user.isAllowedForMemberAdding()
                || user.isAllowedForMemberAddingPrivileges()
                || user.isAllowedForMemberDeleting()
                || user.isAllowedForMemberChanging()
                || user.isAllowedForMemberViewing()) {
        } else {
            mainForm.getTabPanelMainCenter().remove(panelMembers);

            setMatchDetailsControls(user.isAllowedForContestResultChanging());
            setContestDetailsControls(user.isAllowedForContestDetailsChanging());
        }

        /**
         * Contest_SubTeam_Manager *
         */
        if (user.isAllowedForContestSubTeamChanging()) {

            tabPanelContestDetails.add(this.contestSubPanelsByName.get("Wettkampfteilnehmer"));
        }

        /**
         * Contest_Team_Manager *
         */
        if (user.isAllowedForContestTeamsChanging()) {

            tabPanelContestDetails.add(this.contestSubPanelsByName.get("Teams"));

        }

        if (user.isAllowedForContestMatchAdding()) {

            tabPanelContestDetails.add(this.contestSubPanelsByName.get("Neue Matches anlegen"));
            setMatchDetailsControls(user.isAllowedForContestMatchAdding());
        }


        /**
         * Contest_Manager *
         */
        if (user.isAllowedForContestDetailsChanging()) {

            setContestDetailsControls(user.isAllowedForContestDetailsChanging());
        }

        /**
         * Member_Manager *
         */
        if (user.isAllowedForMemberChanging()) {

            mainForm.getTabPanelMainCenter().add(this.mainPanelsByName.get("Mitglieder"));
            setContestDetailsControls(user.isAllowedForContestDetailsChanging());
            setMatchDetailsControls(user.isAllowedForContestResultChanging());
            setMemberControls();
        }
    }

    private void init() {

        this.mainForm = appController.getMainForm();
        this.panelContests = appController.getPanelContests();
        this.panelMembers = appController.getPanelMembers();
        this.panelMessages = appController.getPanelMessages();
        this.tabPanelContestDetails = panelContests.getTabPanelContestDetails();
        this.contestSubPanelsByName = this.initPanelsByName(contestSubPanelsByName, tabPanelContestDetails);

        initPanelsByName(this.contestSubPanelsByName, this.tabPanelContestDetails);
        initPanelsByName(this.mainPanelsByName, this.mainForm.getTabPanelMainCenter());

        tabPanelContestDetails.removeTabAt(getTabByName(tabPanelContestDetails, "Teams"));
        tabPanelContestDetails.removeTabAt(getTabByName(tabPanelContestDetails, "Wettkampfteilnehmer"));
        tabPanelContestDetails.removeTabAt(getTabByName(tabPanelContestDetails, "Neue Matches anlegen"));

    }

    private Integer getTabByName(JTabbedPane tabPane, String tabName) {

        int i = 0;

        while (i < tabPane.getTabCount()) {

            tabPane.setSelectedIndex(i);

            if (tabPane.getTitleAt(i).equalsIgnoreCase(tabName)) {

                return i;
            }

            i++;
        }

        return -1;
    }

    private HashMap initPanelsByName(HashMap<String, JPanel> panels, JTabbedPane tablePane) {

        for (int i = 0; i < tablePane.getTabCount(); i++) {

            panels.put(tablePane.getTitleAt(i), (JPanel) tablePane.getComponentAt(i));
        }

        return panels;
    }

    private void setContestDetailsControls(boolean set) {

        panelContests.getBtnContestSave().setEnabled(set);
        panelContests.getBtnContestNew().setEnabled(set);
        panelContests.getTfContestFee().setEnabled(set);
        panelContests.getIsFinished().setEnabled(set);
        panelContests.getTfContestName().setEnabled(set);
        panelContests.getDcContestEndDate().setEnabled(set);
        panelContests.getDcContestStartDate().setEnabled(set);
    }

    private void setMatchDetailsControls(boolean set) {

        panelContests.getBtnMatchOverviewSave().setVisible(set);
        panelContests.getTableMatchesOverview().setEnabled(set);
    }

    private void setMemberControls() {

        panelMembers.getBtnMemberNew().setEnabled(user.isAllowedForMemberAdding());
        panelMembers.getBtnMemberSave().setEnabled(user.isAllowedForMemberDeleting());
        panelMembers.getBtnMemberSave().setEnabled(user.isAllowedForMemberChanging());

        panelMembers.getBtnAddRole().setVisible(user.isAllowedForMemberAddingPrivileges());
        panelMembers.getBtnRemoveRole().setVisible(user.isAllowedForMemberAddingPrivileges());
        //panelMembers.getListboxActiveRoles().setVisible(false);
        //panelMembers.getListboxAllRoles().setVisible(false);
        panelMembers.getListScrollActiveRoles().setVisible(user.isAllowedForMemberAddingPrivileges());
        panelMembers.getListScrollAllRoles().setVisible(user.isAllowedForMemberAddingPrivileges());
        panelMembers.getLblActiveRoles().setVisible(user.isAllowedForMemberAddingPrivileges());
        panelMembers.getLblAllRoles().setVisible(user.isAllowedForMemberAddingPrivileges());
        panelMembers.getLblMemberRoles().setVisible(user.isAllowedForMemberAddingPrivileges());

    }
}
