/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import java.io.Serializable;
import svm.domain.abstraction.modelInterfaces.IModel;

/**
 *
 * @author Administrator
 */
public abstract class DTO<T extends IModel> implements Serializable {

    private int uid;

    public DTO(T m) {
        this.uid = m.getUID();
    }

    public int getUID() {
        return uid;
    }

    public void update(T m) {
        this.uid = m.getUID();
        doUpdate(m);
    }

    protected abstract void doUpdate(T m);
}
