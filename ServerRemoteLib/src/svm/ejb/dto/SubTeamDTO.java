/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.ISubTeam;

/**
 *
 * @author mike
 */
public class SubTeamDTO extends DTO<ISubTeam> {

    private String name;

    public String getName() {
        return name;
    }

    public SubTeamDTO(ISubTeam m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(ISubTeam subTeam) {
        name = subTeam.getName();
    }
}
