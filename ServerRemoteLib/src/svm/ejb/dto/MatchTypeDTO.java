/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IMatchType;

/**
 *
 * @author mike
 */
public class MatchTypeDTO extends DTO<IMatchType> {

    private String name;

    public String getName() {
        return name;
    }

    public MatchTypeDTO(IMatchType m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IMatchType matchType) {
        name = matchType.getName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
