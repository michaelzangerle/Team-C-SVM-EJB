/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IContestHasTeam;

/**
 *
 * @author mike
 */
public class ContestHasTeamDTO extends DTO<IContestHasTeam> {

    private boolean confirmed;
    private String comment;
    private boolean paid;

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getComment() {
        return comment;
    }

    public boolean isPaid() {
        return paid;
    }

    public ContestHasTeamDTO(IContestHasTeam m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IContestHasTeam contestHasTeams) {
        confirmed = contestHasTeams.isConfirmed();
        comment = contestHasTeams.getComment();
        paid = contestHasTeams.isPaid();
    }
}
