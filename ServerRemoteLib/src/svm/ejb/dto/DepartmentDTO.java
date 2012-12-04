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
    private MemberDTO departmentHead;
    private IContactDetails contactDetails;
    private String description;
    private String alias;

    public String getName() {
        return name;
    }

    public MemberDTO getDepartmentHead() {
        return departmentHead;
    }

    public IContactDetails getContactDetails() {
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
        this.departmentHead = new MemberDTO(department.getDepartmentHead());
        this.contactDetails = department.getContactDetails();
        this.description = department.getDescription();
        this.alias = department.getAlias();
    }
}
