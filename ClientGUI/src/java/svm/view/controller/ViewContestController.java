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
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import svm.domain.abstraction.exception.DomainAttributeException;
import svm.domain.abstraction.exception.DomainException;
import svm.domain.abstraction.exception.DomainParameterCheckException;
import svm.logic.abstraction.exception.IllegalGetInstanceException;
import svm.logic.abstraction.exception.LogicException;
import svm.logic.abstraction.exception.NotAllowException;
import svm.logic.abstraction.transferobjects.*;
import svm.persistence.abstraction.exceptions.ExistingTransactionException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NoTransactionException;
import svm.persistence.abstraction.exceptions.NotSupportedException;
import svm.rmi.abstraction.controller.IRMIContestController;
import svm.rmi.abstraction.controller.IRMISearchController;
import svm.rmi.abstraction.controller.IRMISubTeamController;
import svm.rmi.abstraction.factory.IRMIControllerFactory;
import svm.view.forms.PanelContests;
import svm.view.forms.PanelMembers;

/**
 *
 * @author Patrick
 */
public class ViewContestController {

    private IRMIControllerFactory factory = ApplicationController.factory;
    private IRMIContestController contestController;
    private IRMISearchController searchController;
    private PanelContests panelContests;
    private DefaultListModel<ITransferTeam> contestTeams = new DefaultListModel<>();
    private DefaultListModel<ITransferContest> showContests = new DefaultListModel<>();
    private DefaultListModel<ITransferMember> allTeamMembers = new DefaultListModel<>();
    private DefaultListModel<ITransferMember> contestTeamMembers = new DefaultListModel<>();
    private DefaultListModel<ITransferTeam> allContestTeams = new DefaultListModel<>();
    private DefaultListModel<ITransferTeam> teamA = new DefaultListModel<>();
    private DefaultListModel<ITransferTeam> teamB = new DefaultListModel<>();
    private DefaultComboBoxModel<ITransferLocation> showAllLocations = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<ITransferTeam> comboContestTeams = new DefaultComboBoxModel<>();
    private DefaultListModel<ITransferTeam> allTeamsInSport = new DefaultListModel<>();
    private DefaultComboBoxModel<ITransferSport> allSports = new DefaultComboBoxModel<>();
    private DefaultTableModel tableMatchOverview = new DefaultTableModel();
    private HashMap<ITransferTeam, LinkedList<ITransferMember>> participatingMembers;
    private IRMISubTeamController subTeamController;
    private List<ITransferMatch> overviewMatches;
    private int index;
    private boolean showContestsDone = false;

    /**
     * Controller for UseCases with Contests Does the Event-Handling
     *
     * @author Patrick
     */
    public ViewContestController(PanelContests panelContest) {
        try {
            this.searchController = factory.getRMISearchController(ApplicationController.user);

        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                ITransferContest selectedContest = null;
                try {
                    selectedContest = contestController.getTransferContest();
                } catch (Exception e) {
                    System.out.println("no contestController");
                }

                showContests.clear();

                for (ITransferContest c : searchController.getContests()) {
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
                    selectedContest = (ITransferContest) this.showContests.get(0);
                    this.contestController = this.factory.getRMIContestController(selectedContest, ApplicationController.user);
                    this.contestController.start();
                }

                this.panelContests.getTfContestName().setText(selectedContest.getName());
                this.panelContests.getDcContestStartDate().setDate(selectedContest.getStart());
                this.panelContests.getDcContestEndDate().setDate(selectedContest.getEnd());
                this.panelContests.getTfContestFee().setText(Float.toString(selectedContest.getFee()));

                this.searchController.commit();
                
                showContestsDone = true;

            } catch (NotAllowException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSessionFoundException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalGetInstanceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PanelMembers.class.getName()).log(Level.SEVERE, null, ex);
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
            Vector vector;

            try {
                this.overviewMatches = contestController.getMatches();
                for (ITransferMatch m : this.overviewMatches) {
                    vector = new Vector();
                    vector.add(sdf.format(m.getStart()));
                    vector.add(m.getHomeTeam());
                    vector.add(m.getAwayTeam());
                    vector.add(m.getResultHome());
                    vector.add(m.getResultAway());
                    this.tableMatchOverview.addRow(vector);
                }
            } catch (IllegalGetInstanceException | RemoteException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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
            }
            else {
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
            this.contestController.setSport((ITransferSport) panelContests.getCmbAllSports().getSelectedItem());
            this.contestController.setFinished(finished);
            this.contestController.commit();
        } catch (NotAllowException | DomainParameterCheckException | ExistingTransactionException | NoSessionFoundException | NoTransactionException | DomainAttributeException | RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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

        } catch (ExistingTransactionException | NoSessionFoundException | NoTransactionException | RemoteException ex) {
        }

        try {
            this.clearForNewContest();
            this.contestController = this.factory.getRMIContestController(ApplicationController.user);
            this.contestController.start();
            ITransferContest tmp = this.contestController.getTransferContest();
            panelContests.getTfContestName().setText(tmp.getName());

        } catch (NoSessionFoundException | IllegalGetInstanceException | RemoteException | InstantiationException | IllegalAccessException | NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add a team to a contest
     *
     *
     */
    public void addTeamToContest() {
        try {
            ITransferTeam team = (ITransferTeam) this.panelContests.getListboxAllTeamsInSport().getSelectedValue();

            if (team != null && !this.contestTeams.contains(team)) {
                this.contestController.addTeam(team);
                this.contestTeams.addElement(team);
                this.panelContests.getListboxContestTeams().updateUI();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Team ist bereits in Auswahl vorhanden");
            }
        } catch (RemoteException | NoSessionFoundException | DomainException | InstantiationException | IllegalAccessException | NotAllowException | HeadlessException e) {
            javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Bitte eine Auswahl treffen");
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
            ITransferTeam team = (ITransferTeam) this.panelContests.getListboxContestTeams().getSelectedValue();
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
                for (ITransferTeam team : searchController.getTeams()) {
                    allTeamsInSport.addElement(team);
                }
                searchController.commit();
            }
            if (contestTeams.isEmpty()) {

                for (ITransferTeam team : contestController.getTeams()) {
                    System.out.println(team.getName());
                    contestTeams.addElement(team);
                }
            }
        } catch (InstantiationException | IllegalAccessException | NotSupportedException | NotAllowException | ExistingTransactionException | NoTransactionException | NoSessionFoundException | RemoteException | IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save edited data from match overview table to database
     *
     *
     */
    public void saveMatchOverview() {
        try {
            int i = 0;
            LinkedList dates = new LinkedList();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - hh.mm");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -100);
            sdf.set2DigitYearStart(cal.getTime());

            //this.contestController = factory.getRMIContestController((ITransferContest) this.panelContests.getListboxShowContests().getSelectedValue(), ApplicationController.user);

            int entriesIterator = 0;

            for (ITransferMatch t : this.overviewMatches) {
                String dateString;
                try {
                    dateString = (String) this.panelContests.getTableMatchesOverview().getValueAt(entriesIterator, 0);
                    this.contestController.setDateForMatch(t, sdf.parse(dateString));
                } catch (NotAllowException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(PanelContests.class.getName()).log(Level.SEVERE, null, ex);
                    javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Error while parsing Date, corrupted cell: " + entriesIterator + ",0\nCorrect Format is dd.MM.yyyy - hh.mm");
                    dates.clear();
                    break;
                }

                try {
                    try {
                        this.contestController.setResult(t,
                                Integer.valueOf(this.panelContests.getTableMatchesOverview().getValueAt(entriesIterator, 3).toString()),
                                Integer.valueOf(this.panelContests.getTableMatchesOverview().getValueAt(entriesIterator, 4).toString()));
                    } catch (NoSessionFoundException | DomainException | InstantiationException | IllegalAccessException | NotAllowException ex) {
                        Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (ClassCastException ex) {
                    Logger.getLogger(PanelContests.class.getName()).log(Level.SEVERE, null, ex);
                    javax.swing.JOptionPane.showMessageDialog(this.panelContests, "Error while parsing result, corrupted resultrow: " + entriesIterator);
                    dates.clear();
                    break;
                }
                entriesIterator++;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            contestController.commit();
            contestController.start();
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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
                List<ITransferTeam> teams = contestController.getTeams();
                for (ITransferTeam c : teams) {
                    this.comboContestTeams.addElement(c);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalGetInstanceException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Add member to subteam
     *
     *
     */
    public void addToSubTeam() {
        ITransferMember member = (ITransferMember) this.panelContests.getListboxAllTeamMembers().getSelectedValue();
        if (member != null) {
            try {
                this.contestTeamMembers.addElement(member);
                this.allTeamMembers.removeElement(member);
                this.subTeamController.addMember(member);
            } catch (NotAllowException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LogicException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSessionFoundException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DomainException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotSupportedException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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
        ITransferMember member = (ITransferMember) this.panelContests.getListboxContestTeamMembers().getSelectedValue();
        if (member != null) {
            try {
                this.subTeamController.removeMember(member);
                this.allTeamMembers.addElement(member);
                this.contestTeamMembers.removeElement(member);
            } catch (NotAllowException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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

            ITransferContest selectedContest = contestController.getTransferContest();

            if (allContestTeams.isEmpty()) {
                for (ITransferTeam t : contestController.getTeams()) {
                    this.allContestTeams.addElement(t);
                }
            }
        } catch (RemoteException | IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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
        teamB.addElement((ITransferTeam) this.panelContests.getListboxAllContestTeams().getSelectedValue());
    }

    /**
     * Create match - add as team a
     *
     */
    public void addToTeamA() {
        teamA.addElement((ITransferTeam) this.panelContests.getListboxAllContestTeams().getSelectedValue());
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
        showAllLocations = new DefaultComboBoxModel<>();
        comboContestTeams = new DefaultComboBoxModel<>();
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
            ITransferContest selectedContest = (ITransferContest) this.showContests.get(this.panelContests.getListboxShowContests().getSelectedIndex());
            this.contestController = factory.getRMIContestController(selectedContest, ApplicationController.user);
            this.contestController.start();

            this.panelContests.getTfContestName().setText(selectedContest.getName());
            this.panelContests.getDcContestStartDate().setDate(selectedContest.getStart());
            this.panelContests.getDcContestEndDate().setDate(selectedContest.getEnd());
            this.panelContests.getTfContestFee().setText(Float.toString(selectedContest.getFee()));
            this.panelContests.getCmbAllSports().setSelectedIndex(getIndexByObject(allSports, selectedContest.getSport()));
            //System.out.println(getIndexByObject(allSports, selectedContest.getSport()));
            this.panelContests.getIsFinished().setSelected(selectedContest.isFinished());
            refreshContestGUI();
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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
                    contestController.addTeam((ITransferTeam) this.panelContests.getListboxContestTeams().getSelectedValue());
                    i++;
                } catch (DomainException | NotAllowException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.contestController.commit();
            this.contestController.start();
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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
                    try {
                        this.contestController.addMatch(teamA.get(entryIterator), teamB.get(entryIterator), new Date(), new Date());
                        entryIterator++;
                    } catch (NotSupportedException | NotAllowException ex) {
                        Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (LogicException | DomainException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.contestController.commit();
            this.contestController.start();
            this.panelContests.getListboxAllTeamsInSport().updateUI();
            this.panelContests.getListboxContestTeams().updateUI();

        } catch (InstantiationException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add member to subteam
     *
     */
    public void changeContestTeamSelection() {

        this.allTeamMembers.clear();
        this.contestTeamMembers.clear();
        ITransferTeam selectedTeam = (ITransferTeam) this.panelContests.getCmbContestTeams().getSelectedItem();

        try {
            this.subTeamController = factory.getRMISubTeamController(selectedTeam, contestController.getTransferContest(), ApplicationController.user);
            this.subTeamController.start();

            for (ITransferMember member : subTeamController.getMembersOfSubTeam()) {
                this.contestTeamMembers.addElement(member);
            }

            for (ITransferMember member : subTeamController.getMemberOfTeam()) {
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
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Save subteams
     *
     */
    public void saveSubteam() {
        try {
            this.subTeamController.commit();
            this.subTeamController.start();
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExistingTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoTransactionException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    public void getAllSports()
    { 
            try {

                searchController.start();
                for (ITransferSport sport : searchController.getSports()) {
                    
                    this.allSports.addElement(sport);
                }
                this.panelContests.getCmbAllSports().setModel(allSports);
            } catch (NoSessionFoundException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalGetInstanceException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotAllowException  ex) {
            Logger.getLogger(ViewContestController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    private Integer getIndexByObject(AbstractListModel<ITransferSport> listModel, ITransferSport sport){
        int i=0;
        
        for (i=0; i < listModel.getSize() ; i++) {
            
            if (listModel.getElementAt(i).getName().equals(sport.getName())) {
                
                return i;
            }
        }        
        return 0;
    }
}
