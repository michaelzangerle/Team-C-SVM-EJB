/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IExternalTeam;
import svm.domain.abstraction.modelInterfaces.IModel;

/**
 *
 * @author mike
 */
public class ExternalTeamDTO extends TeamDTO {

    private String name;
    private ContactDetailsDTO contactDetails;
    private String alias;

    public String getName() {
        return name;
    }

    public ContactDetailsDTO getContactDetails() {
        return contactDetails;
    }

    public String getAlias() {
        return alias;
    }

    public ExternalTeamDTO(IExternalTeam m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IModel m) {
        IExternalTeam team = (IExternalTeam)m;
        this.name = team.getName();
        this.contactDetails = new ContactDetailsDTO(team.getContactDetails());
        this.alias = team.getAlias();
    }

    @Override
    public String toString() {
        return getName();
    }
}
