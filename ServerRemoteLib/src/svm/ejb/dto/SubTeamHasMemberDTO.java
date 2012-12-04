/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.ISubTeamsHasMembers;

/**
 *
 * @author mike
 */
public class SubTeamHasMemberDTO   extends DTO<ISubTeamsHasMembers> {

    public SubTeamHasMemberDTO(ISubTeamsHasMembers m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(ISubTeamsHasMembers m) {
    }
}
