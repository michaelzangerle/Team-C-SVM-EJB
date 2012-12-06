/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import java.util.Date;
import svm.domain.abstraction.modelInterfaces.IMatch;

/**
 *
 * @author mike
 */
public class MatchDTO extends DTO<IMatch> {

    private String name;
    private Date start;
    private Date end;
    private boolean cancelled;
    private ContactDetailsDTO contactDetails;
    private MatchTypeDTO matchType;
    private String description;
    private String remarks;
    private TeamDTO home;
    private TeamDTO away;
    private Integer resultHome;
    private Integer resultAway;

    public String getName() {
        return name;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public ContactDetailsDTO getContactDetails() {
        return contactDetails;
    }

    public MatchTypeDTO getMatchType() {
        return matchType;
    }

    public String getDescription() {
        return description;
    }

    public String getRemarks() {
        return remarks;
    }

    public TeamDTO getHome() {
        return home;
    }

    public TeamDTO getAway() {
        return away;
    }

    public Integer getResultHome() {
        return resultHome;
    }

    public Integer getResultAway() {
        return resultAway;
    }

    public MatchDTO(IMatch m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IMatch match) {

        name = match.getName();
        start = match.getStart();
        end = match.getEnd();
        cancelled = match.getCancelled();
        contactDetails = new ContactDetailsDTO(match.getContactDetails());

        if (!match.getMatchType().isNull()) {
            matchType = new MatchTypeDTO(match.getMatchType());
        }

        description = match.getDescription();
        remarks = match.getRemarks();
        home = getHomeTeamTemp(match);
        away = getAwayTeamTemp(match);
        resultHome = match.getHomeResult();
        resultAway = match.getAwayResult();
    }

    private TeamDTO getAwayTeamTemp(IMatch match) {
        if (!match.getAwayExternal().isNull()) {
            return new ExternalTeamDTO(match.getAwayExternal());
        } else {
            return new InternalTeamDTO(match.getAwayInternal());
        }
    }

    private TeamDTO getHomeTeamTemp(IMatch match) {
        if (!match.getHomeExternal().isNull()) {
            return new ExternalTeamDTO(match.getHomeExternal());
        } else {
            return new InternalTeamDTO(match.getHomeInternal());
        }
    }

    @Override
    public String toString() {
        return getName() + " [" + getHome() + " vs. " + getAway() + ": " + getResultHome() + " : " + getResultAway() + "]";
    }
}
