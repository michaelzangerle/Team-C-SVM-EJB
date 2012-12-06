/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IUserPrivilege;

/**
 *
 * @author Administrator
 */
public class UserPrivilegeDTO extends DTO<IUserPrivilege> {

    String name;

    public String getName() {
        return name;
    }

    public UserPrivilegeDTO(IUserPrivilege m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IUserPrivilege m) {
        name = m.getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
