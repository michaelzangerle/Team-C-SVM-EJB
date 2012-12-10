/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.view.controller;

import java.awt.HeadlessException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import svm.ejb.ContestBeanRemote;
import svm.ejb.SearchBeanRemote;
import svm.ejb.SubTeamBeanRemote;
import svm.ejb.dto.*;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.view.forms.PanelContests;
import svm.view.forms.PanelMembers;

/**
 *
 * @author Patrick
 */
public class ViewContestController {

    private SearchBeanRemote searchController = ApplicationController.searchBean;
    private ContestBeanRemote contestController = ApplicationController.contestBean;
    private SubTeamBeanRemote subTeamController = ApplicationController.subTeamBean;
    private PanelContests panelContests;
    private DefaultListModel<TeamDTO> contestTeams = new DefaultListModel<TeamDTO>();
    private DefaultListModel<ContestDTO> showContests = new DefaultListModel<ContestDTO>();
    private DefaultListModel<MemberDTO> allTeamMembers = new DefaultListModel<MemberDTO>();
    private DefaultListModel<MemberDTO> contestTeamMembers = new DefaultListModel<MemberDTO>();
    private DefaultListModel<TeamDTO> allContestTeams = new DefaultListModel<TeamDTO>();
    private DefaultListModel<TeamDTO> teamA = new DefaultListModel<TeamDTO>();
    private DefaultListModel<TeamDTO> teamB = new DefaultListModel<TeamDTO>();
    private DefaultComboBoxModel<LocationDTO> showAllLocations = new DefaultComboBoxModel<LocationDTO>();
    private DefaultComboBoxModel<TeamDTO> comboContestTeams = new DefaultComboBoxModel<TeamDTO>();
    private DefaultListModel<TeamDTO> allTeamsInSport = new DefaultListModel<TeamDTO>();
    private DefaultComboBoxModel<SportDTO> allSports = new DefaultComboBoxModel<SportDTO>();
    private DefaultTableModel tableMatchOverview = new DefaultTableModel();
    private HashMap<TeamDTO, LinkedList<MemberDTO>> participatingMembers;
    private List<MatchDTO> overviewMatches;
    private int index;
    private boolean showContestsDone = false;

    /**
     * Controller for UseCases with Contests Does the Event-Handling
     *
     * @author Patrick
     */
    public ViewContestController(PanelContests panelContest) {
        this.panelContests = panelContest;
    }

    /**
     * Show all contests in overview list on the left
     *
     *
     */
    public void showContests() {

        if (showContestsDone == false) {
            try {
                this.searchController.start();
                ContestDTO selectedContest = null;
                try {
                    selectedContest = contestController.getTransferContest();
                } catch (Exception e) {
                    System.out.println("no contestController");
                }

                showContests.clear();

                for (ContestDTO c : searchController.getContests()) {
                    int i = 0;
                    boolean contains = false;
                    while (i < this.contestTeams.getSize()) {
                        if (this.contestTeams.getElementAt(i).getName().equalsIgnoreCase(c.getName())) {
                            contains = true;
                        }
                        i++;
                    }
                    if (contains == false) {
                        showContests.addElement(c);
                    }
                }
                this.panelContests.getListboxShowContests().setModel(showContests);
                //this.panelContests.getListboxShowContests().setSelectedIndex(0);

                if (selectedContest == null) {
                    selectedContest = (ContestDTO) this.showContests.get(0);
                    this.contestController.start(selectedContest);
                }

                this.panelContests.getTfContestName().setText(selectedContest.getName());
                this.panelContests.getDcContestStartDate().setDate(selectedContest.getStart());
                this.panelContests.getDcContestEndDate().setDate(selectedContest.getEnd());
                this.panelContests.getTfContestFee().setText(Float.toString(selectedContest.getFee()));

                this.searchController.commit();

                showContestsDone = true;

            } catch (DomainException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
        }
    }

    /**
     * Show matches, dates and results in the current contest
     *
     *
     */
    public void showMatchOverview() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - hh.mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -100);
        sdf.set2DigitYearStart(cal.getTime());
        this.panelContests.getTableMatchesOverview().setModel(tableMatchOverview);

        if (tableMatchOverview.getColumnCount() == 0) {
            this.tableMatchOverview.addColumn("Date");
            this.tableMatchOverview.addColumn("TeamA");
            this.tableMatchOverview.addColumn("TeamB");
            this.tableMatchOverview.addColumn("ScoreA");
            this.tableMatchOverview.addColumn("ScoreB");
        }

        if (tableMatchOverview.getRowCount() == 0) {
            try {
                Vector vector;
                this.overviewMatches = contestController.getMatches();
                for (MatchDTO m : this.overviewMatches) {
                    vector = new Vector();
                    vector.add(sdf.format(m.getStart()));
                    vector.add(m.getHome());
                    vector.add(m.getAway());
                    vector.add(m.getResultHome());
                    vector.add(m.getResultAway());
                    this.tableMatchOverview.addRow(vector);
                }
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
        }
    }

    /**
     * Save the edited contest to database
     *
     */
    public void saveContest() {
        boolean finished = false;
        Date today = new Date();

        if (!finished) {
            if (panelContests.getDcContestEndDate().getDate().compareTo(today) < 0) {

                finished = true;
            } else {
                finished = panelContests.getIsFinished().isSelected();
            }
        } else {

            finished = panelContests.getIsFinished().isSelected();
        }

        try {
            this.contestController.setContestName(panelContests.getTfContestName().getText());
            this.contestController.setContestStartDate(panelContests.getDcContestStartDate().getDate());
            this.contestController.setContestEndDate(panelContests.getDcContestEndDate().getDate());
            this.contestController.setContestFee(Float.parseFloat(panelContests.getTfContestFee().getText()));
            this.contestController.setSport((SportDTO) panelContests.getCmbAllSports().getSelectedItem());
            this.contestController.setFinished(finished);
            this.contestController.commit();
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }
    }

    /**
     * Create new contest from the entered data
     *
     *
     */
    public void createNewContest() {

        try {
            if (this.contestController != null) {
                this.contestController.abort();
            }
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }

        try {
            this.clearForNewContest();
            this.contestController.start();
            ContestDTO tmp = this.contestController.getTransferContest();
            panelContests.getTfContestName().setText(tmp.getName());
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }
    }

    /**
     * Add a team to a contest
     *
     *
     */
    public void addTeamToContest() {
        try {
            TeamDTO team = (TeamDTO) this.panelContests.getListboxAllTeamsInSport().getSelectedValue();

            if (team != null && !this.contestTeams.contains(team)) {
                this.contestController.addTeam(team);
                this.contestTeams.addElement(team);
                this.panelContests.getListboxContestTeams().updateUI();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Team ist bereits in Auswahl vorhanden");
            }
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }
    }

    /**
     * Remove a team from a contest
     *
     *
     *
     */
    public void removeTeamFromContest() {
        try {
            TeamDTO team = (TeamDTO) this.panelContests.getListboxContestTeams().getSelectedValue();
            if (team != null) {
                this.contestController.removeTeam(team);
                this.contestTeams.removeElement(team);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Bitte eine Auswahl treffen");
        }
    }

    /**
     * Show all teams participating in contest
     *
     *
     */
    public void showAllTeams() {
        try {
            //this.panelContests.getListboxContestTeams().setModel(contestTeams);
            this.panelContests.getListboxAllTeamsInSport().setModel(allTeamsInSport);
            if (allTeamsInSport.isEmpty()) {
                searchController.start();
                for (TeamDTO team : searchController.getTeams()) {
                    allTeamsInSport.addElement(team);
                }
                searchController.commit();
            }
            if (contestTeams.isEmpty()) {

                for (TeamDTO team : contestController.getTeams()) {
                    System.out.println(team.getName());
                    contestTeams.addElement(team);
                }
            }
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }
    }

    /**
     * Save edited data from match overview table to database
     *
     *
     */
    public void saveMatchOverview() {
        int i = 0;
        LinkedList dates = new LinkedList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - hh.mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -100);
        sdf.set2DigitYearStart(cal.getTime());

        int entriesIterator = 0;

        for (MatchDTO t : this.overviewMatches) {
            String dateString;
            try {
                dateString = (String) this.panelContests.getTableMatchesOverview().getValueAt(entriesIterator, 0);
                this.contestController.setDateForMatch(t, sdf.parse(dateString));
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(PanelContests.class.getName()).log(Level.SEVERE, null, ex);
                javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Error while parsing Date, corrupted cell: " + entriesIterator + ",0\nCorrect Format is dd.MM.yyyy - hh.mm");
                dates.clear();
                break;
            } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }

            try {
                this.contestController.setResult(t,
                        Integer.valueOf(this.panelContests.getTableMatchesOverview().getValueAt(entriesIterator, 3).toString()),
                        Integer.valueOf(this.panelContests.getTableMatchesOverview().getValueAt(entriesIterator, 4).toString()));
            } catch (ClassCastException ex) {
                Logger.getLogger(PanelContests.class.getName()).log(Level.SEVERE, null, ex);
                javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Error while parsing result, corrupted resultrow: " + entriesIterator);
                dates.clear();
                break;
            } catch (DomainException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
            entriesIterator++;
        }
        try {
            contestController.commit();
            contestController.restart();
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }
    }

    /**
     * Manage subteams - add and remove members to the participating team in
     * contest
     *
     */
    public void manageSubteams() {
        this.panelContests.getListboxAllTeamMembers().setModel(allTeamMembers);
        this.panelContests.getListboxContestTeamMembers().setModel(contestTeamMembers);
        this.panelContests.getCmbContestTeams().setModel(comboContestTeams);

        if (this.panelContests.getCmbContestTeams().getModel().getSize() == 0) {
            try {
                List<TeamDTO> teams = contestController.getTeams();
                for (TeamDTO c : teams) {
                    this.comboContestTeams.addElement(c);
                }
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
        }
    }

    /**
     * Add member to subteam
     *
     *
     */
    public void addToSubTeam() {
        MemberDTO member = (MemberDTO) this.panelContests.getListboxAllTeamMembers().getSelectedValue();
        if (member != null) {
            try {
                this.contestTeamMembers.addElement(member);
                this.allTeamMembers.removeElement(member);
                this.subTeamController.addMember(member);
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PersistenceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DomainException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Bitte eine Auswahl treffen");
        }
    }

    /**
     * Remove member from subteam
     *
     */
    public void removeFromSubTeam() {
        MemberDTO member = (MemberDTO) this.panelContests.getListboxContestTeamMembers().getSelectedValue();
        if (member != null) {
            try {
                this.subTeamController.removeMember(member);
                this.allTeamMembers.addElement(member);
                this.contestTeamMembers.removeElement(member);
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DomainException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Bitte eine Auswahl treffen");
        }
    }

    /**
     * Manage teams in the current contest
     *
     *
     */
    public void manageContestTeams() {
        try {
            this.panelContests.getListboxAllContestTeams().setModel(allContestTeams);
            this.panelContests.getListboxTeamA().setModel(teamA);
            this.panelContests.getListboxTeamB().setModel(teamB);

            ContestDTO selectedContest = contestController.getTransferContest();

            if (allContestTeams.isEmpty()) {
                for (TeamDTO t : contestController.getTeams()) {
                    this.allContestTeams.addElement(t);
                }
            }
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }

    }

    /**
     * Remove team from current contest
     *
     */
    public void removeFromTeam() {
        if (this.panelContests.getListboxTeamA().getSelectedValue() != null) {
            teamA.removeElement(this.panelContests.getListboxTeamA().getSelectedValue());
        }
        if (this.panelContests.getListboxTeamB().getSelectedValue() != null) {
            teamB.removeElement(this.panelContests.getListboxTeamB().getSelectedValue());
        }
    }

    /**
     * Create match - add as team b
     *
     */
    public void addToTeamB() {
        teamB.addElement((TeamDTO) this.panelContests.getListboxAllContestTeams().getSelectedValue());
    }

    /**
     * Create match - add as team a
     *
     */
    public void addToTeamA() {
        teamA.addElement((TeamDTO) this.panelContests.getListboxAllContestTeams().getSelectedValue());
    }

    /**
     * Change the selected contest
     *
     */
    public void contestChange() {
        contestTeams.clear();
        allTeamMembers.clear();
        contestTeamMembers.clear();
        allContestTeams.clear();
        teamA.clear();
        teamB.clear();
        showAllLocations = new DefaultComboBoxModel<LocationDTO>();
        comboContestTeams = new DefaultComboBoxModel<TeamDTO>();
        tableMatchOverview = new DefaultTableModel();
        refreshContestGUI();
    }

    /**
     * Update contest data with entered data
     *
     *
     */
    public void updateContests() {
        try {
            if (this.panelContests.getListboxShowContests().getSelectedIndex() == -1) {
                return;
            }
            ContestDTO selectedContest = (ContestDTO) this.showContests.get(this.panelContests.getListboxShowContests().getSelectedIndex());
            this.contestController.start(selectedContest);

            this.panelContests.getTfContestName().setText(selectedContest.getName());
            this.panelContests.getDcContestStartDate().setDate(selectedContest.getStart());
            this.panelContests.getDcContestEndDate().setDate(selectedContest.getEnd());
            this.panelContests.getTfContestFee().setText(Float.toString(selectedContest.getFee()));
            this.panelContests.getCmbAllSports().setSelectedIndex(getIndexByObject(allSports, selectedContest.getSport()));
            //System.out.println(getIndexByObject(allSports, selectedContest.getSport()));
            this.panelContests.getIsFinished().setSelected(selectedContest.isFinished());
            refreshContestGUI();
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
            JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
        }
    }

    /**
     * Clear fields to enter new data for new contest
     *
     *
     */
    public void clearForNewContest() {
        this.panelContests.getTfContestName().setText("");
        this.panelContests.getDcContestStartDate().setDate(null);
        this.panelContests.getDcContestEndDate().setDate(null);
        this.panelContests.getTfContestFee().setText("");
    }

    /**
     * Save selected teams for contest
     *
     *
     */
    public void saveContestTeams() {
        try {
            int i = 0;
            while (i < this.panelContests.getListboxContestTeams().getModel().getSize()) {
                try {
                    contestController.addTeam((TeamDTO) this.panelContests.getListboxContestTeams().getSelectedValue());
                    i++;
                } catch (DomainException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PersistenceException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (LogicException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
            }
            this.contestController.commit();
            this.contestController.restart();
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
    }

    /**
     * Save the created matches
     *
     */
    public void saveTeamComposition() {
        try {
            int entryIterator = 0;
            while ((entryIterator < teamB.getSize()) && (entryIterator < teamA.getSize())) {
                try {
                    this.contestController.addMatch(teamA.get(entryIterator), teamB.get(entryIterator), new Date(), new Date());
                    entryIterator++;
                } catch (LogicException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PersistenceException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DomainException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
            }
            this.contestController.commit();
            this.contestController.restart();
            this.panelContests.getListboxAllTeamsInSport().updateUI();
            this.panelContests.getListboxContestTeams().updateUI();

        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
    }

    /**
     * Add member to subteam
     *
     */
    public void changeContestTeamSelection() {

        this.allTeamMembers.clear();
        this.contestTeamMembers.clear();
        TeamDTO selectedTeam = (TeamDTO) this.panelContests.getCmbContestTeams().getSelectedItem();

        try {
            this.subTeamController.start(selectedTeam, contestController.getTransferContest());

            for (MemberDTO member : subTeamController.getMembersOfSubTeam()) {
                this.contestTeamMembers.addElement(member);
            }

            for (MemberDTO member : subTeamController.getMemberOfTeam()) {
                int i = 0;
                boolean contains = false;
                while ((i < contestTeamMembers.getSize()) && (contains == false)) {
                    if ((member.getFirstName().equalsIgnoreCase(contestTeamMembers.get(i).getFirstName()))
                            && member.getLastName().equalsIgnoreCase(contestTeamMembers.get(i).getLastName())) {
                        contains = true;
                    }
                    i++;
                }
                if (contains == false) {
                    this.allTeamMembers.addElement(member);
                }
            }

            this.panelContests.getListboxAllTeamMembers().setModel(allTeamMembers);
            this.panelContests.getListboxContestTeamMembers().setModel(contestTeamMembers);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
    }

    /**
     * Save subteams
     *
     */
    public void saveSubteam() {
        try {
            this.subTeamController.commit();
            this.subTeamController.restart();
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
    }

    /**
     * Change contest - tabs Event-Handling delegation
     *
     *
     */
    public void contestDetailsTabChanged() {

        if (panelContests.getTabPanelContestDetails().getSelectedComponent().getName().equalsIgnoreCase("Teams")) {
            this.index = panelContests.getListboxShowContests().getSelectedIndex();
            contestChange();
            showContests();
            showAllTeams();
            panelContests.getListboxShowContests().setSelectedIndex(index);
            refreshContestGUI();
        } else if (panelContests.getTabPanelContestDetails().getSelectedComponent().getName().equalsIgnoreCase("Wettkampfteilnehmer")) {
            this.index = panelContests.getListboxShowContests().getSelectedIndex();
            contestChange();
            manageSubteams();
            panelContests.getListboxShowContests().setSelectedIndex(index);
            refreshContestGUI();
        } else if (panelContests.getTabPanelContestDetails().getSelectedComponent().getName().equalsIgnoreCase("Neue Matches anlegen")) {
            this.index = panelContests.getListboxShowContests().getSelectedIndex();
            contestChange();
            manageContestTeams();
            panelContests.getListboxShowContests().setSelectedIndex(index);
            refreshContestGUI();
        } else if (panelContests.getTabPanelContestDetails().getSelectedComponent().getName().equalsIgnoreCase("Matchübersicht")) {
            this.index = panelContests.getListboxShowContests().getSelectedIndex();
            contestChange();
            showMatchOverview();
            panelContests.getListboxShowContests().setSelectedIndex(index);
            refreshContestGUI();
        }
    }

    public void refreshContestGUI() {
        this.panelContests.getListboxAllContestTeams().updateUI();
        this.panelContests.getListboxAllTeamMembers().updateUI();
        this.panelContests.getListboxAllTeamsInSport().updateUI();
        this.panelContests.getListboxContestTeamMembers().updateUI();
        this.panelContests.getListboxContestTeams().updateUI();
        this.panelContests.getListboxTeamA().updateUI();
        this.panelContests.getListboxTeamB().updateUI();
    }

    public void initializeModels() {

        try {
            this.panelContests.getListboxContestTeams().setModel(contestTeams);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getListboxShowContests().setModel(showContests);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getListboxAllTeamMembers().setModel(allTeamMembers);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getListboxContestTeamMembers().setModel(contestTeamMembers);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getListboxAllContestTeams().setModel(allContestTeams);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getListboxTeamA().setModel(teamA);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getListboxTeamB().setModel(teamB);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getCmbContestTeams().setModel(comboContestTeams);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getListboxAllTeamsInSport().setModel(allTeamsInSport);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
        try {
            this.panelContests.getTableMatchesOverview().setModel(tableMatchOverview);
        } catch (Exception e) {
            System.out.println("Not sufficient privilege");
        }
    }

    public void resetShowContestsDone() {
        showContestsDone = false;
    }

    public void getAllSports() {
        try {
            searchController.start();
            for (SportDTO sport : searchController.getSports()) {

                this.allSports.addElement(sport);
            }
            searchController.abort();
            this.panelContests.getCmbAllSports().setModel(allSports);
        } catch (PersistenceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DomainException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LogicException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.ejb.EJBAccessException ex) {
                JOptionPane.showMessageDialog(null, ApplicationController.notAuthMessage);
            }
    }

    private Integer getIndexByObject(AbstractListModel<SportDTO> listModel, SportDTO sport) {
        int i = 0;

        for (i = 0; i < listModel.getSize(); i++) {

            if (listModel.getElementAt(i).getName().equals(sport.getName())) {

                return i;
            }
        }
        return 0;
    }
}
