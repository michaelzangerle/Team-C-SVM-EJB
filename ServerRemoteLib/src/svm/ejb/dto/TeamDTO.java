/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IModel;

/**
 *
 * @author Administrator
 */
public abstract class TeamDTO extends DTO<IModel> {

    public TeamDTO(IModel m) {
        super(m);
        doUpdate(m);
    }

    public abstract String getName();

    @Override
    protected abstract void doUpdate(IModel m);

    @Override
    public String toString() {
        return getName();
    }
}
