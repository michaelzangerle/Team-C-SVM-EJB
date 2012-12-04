/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IContactDetails;
import svm.domain.abstraction.modelInterfaces.IDepartment;

/**
 *
 * @author mike
 */
public class DepartmentDTO extends DTO<IDepartment> {

    private String name;
    private ContactDetailsDTO contactDetails;
    private String description;
    private String alias;

    public String getName() {
        return name;
    }

    public ContactDetailsDTO getContactDetails() {
        return contactDetails;
    }

    public String getDescription() {
        return description;
    }

    public String getAlias() {
        return alias;
    }

    public DepartmentDTO(IDepartment m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IDepartment department) {
        this.name = department.getName();
        this.contactDetails = new ContactDetailsDTO(department.getContactDetails());
        this.description = department.getDescription();
        this.alias = department.getAlias();
    }
}
