/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import svm.ejb.dto.AuthDTO;
import svm.view.forms.MainForm;
import svm.view.forms.PanelContests;
import svm.view.forms.PanelMembers;
import svm.view.forms.PanelMessages;

/**
 *
 * @author Patrick
 */
public class ViewRightsHandler {

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
        this.user = user;
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
