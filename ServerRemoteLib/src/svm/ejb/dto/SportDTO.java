/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.ISport;

/**
 *
 * @author Administrator
 */
public class SportDTO extends DTO<ISport> {

    private String name;
    private DepartmentDTO department;

    public DepartmentDTO getDepartment() {
        return department;
    }

    public String getName() {
        return name;
    }

    public SportDTO(ISport m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(ISport m) {
        this.name = m.getName();
        this.department = new DepartmentDTO(m.getDepartment());
    }
}
