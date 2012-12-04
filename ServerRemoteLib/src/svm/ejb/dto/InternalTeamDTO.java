/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IModel;
import svm.domain.abstraction.modelInterfaces.ITeam;

/**
 *
 * @author mike
 */
public class InternalTeamDTO extends TeamDTO {

    private String name;
    private String alias;
    private MemberDTO contactPerson;

    public MemberDTO getContactPerson() {
        return contactPerson;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public InternalTeamDTO(ITeam m) {
        super(m);
        doUpdate((IModel) m);
    }

    @Override
    protected void doUpdate(IModel m) {
        ITeam team = (ITeam) m;
        this.name = team.getName();
        this.contactPerson = new MemberDTO(team.getContactPerson());
        this.alias = team.getAlias();
    }
}
