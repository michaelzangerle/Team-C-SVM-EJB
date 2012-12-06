/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import svm.domain.abstraction.modelInterfaces.IMember;
import svm.domain.abstraction.modelInterfaces.IUserPrivilege;

/**
 *
 * @author Administrator
 */
public class AuthDTO extends DTO<IMember> {

    private String firstName;
    private String userName;
    private String lastName;
    private String title;
    private boolean isAllowedForSearching = false;
    private boolean isAllowedForMemberViewing = false;
    private boolean isAllowedForMemberChanging = false;
    private boolean isAllowedForMemberDeleting = false;
    private boolean isAllowedForMemberAdding = false;
    private boolean isAllowedForMemberAddingPrivileges = false;
    private boolean isAllowedForContestViewing = false;
    private boolean isAllowedForContestDetailsChanging = false;
    private boolean isAllowedForContestTeamsChanging = false;
    private boolean isAllowedForContestMatchAdding = false;
    private boolean isAllowedForContestResultChanging = false;
    private boolean isAllowedForContestDeleting = false;
    private boolean isAllowedForContestAdding = false;
    private boolean isAllowedForContestSubTeamChanging = false;

    public String getUsername() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAllowedForSearching() {
        return isAllowedForSearching;
    }

    public boolean isAllowedForMemberViewing() {
        return isAllowedForMemberViewing;
    }

    public boolean isAllowedForMemberChanging() {
        return isAllowedForMemberChanging;
    }

    public boolean isAllowedForMemberDeleting() {
        return isAllowedForMemberDeleting;
    }

    public boolean isAllowedForMemberAdding() {
        return isAllowedForMemberAdding;
    }

    public boolean isAllowedForMemberAddingPrivileges() {
        return isAllowedForMemberAddingPrivileges;
    }

    public boolean isAllowedForContestViewing() {
        return isAllowedForContestViewing;
    }

    public boolean isAllowedForContestDetailsChanging() {
        return isAllowedForContestDetailsChanging;
    }

    public boolean isAllowedForContestDeleting() {
        return isAllowedForContestDeleting;
    }

    public boolean isAllowedForContestAdding() {
        return isAllowedForContestAdding;
    }

    public boolean isAllowedForContestResultChanging() {
        return isAllowedForContestResultChanging;
    }

    public boolean isAllowedForContestMatchAdding() {
        return isAllowedForContestMatchAdding;
    }

    public boolean isAllowedForContestTeamsChanging() {
        return isAllowedForContestTeamsChanging;
    }

    public boolean isAllowedForContestSubTeamChanging() {
        return isAllowedForContestSubTeamChanging;
    }

    public AuthDTO(IMember m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IMember m) {
        this.firstName = m.getFirstName();
        this.userName = m.getUserName();
        this.lastName = m.getLastName();
        this.title = m.getTitle();

        IMember member = m;
        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER) || member.isIn(IUserPrivilege.Privileges.MEMBER_MANAGER) || member.isIn(IUserPrivilege.Privileges.VIEW_ONLY) || member.isIn(IUserPrivilege.Privileges.CONTEST_MATCH_MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_TEAM_MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_SUBTEAM_MANAGER)) {
            isAllowedForSearching = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.MEMBER_MANAGER)) {
            isAllowedForMemberViewing = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.MEMBER_MANAGER)) {
            isAllowedForMemberChanging = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.MEMBER_MANAGER)) {
            isAllowedForMemberDeleting = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.MEMBER_MANAGER)) {
            isAllowedForMemberAdding = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN)) {
            isAllowedForMemberAddingPrivileges = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER) || member.isIn(IUserPrivilege.Privileges.MEMBER_MANAGER) || member.isIn(IUserPrivilege.Privileges.VIEW_ONLY) || member.isIn(IUserPrivilege.Privileges.CONTEST_MATCH_MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_TEAM_MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_SUBTEAM_MANAGER)) {
            isAllowedForContestViewing = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER)) {
            isAllowedForContestDetailsChanging = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MATCH_MANAGER)) {
            isAllowedForContestResultChanging = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER)) {
            isAllowedForContestMatchAdding = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_TEAM_MANAGER)) {
            isAllowedForContestTeamsChanging = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_SUBTEAM_MANAGER)) {
            isAllowedForContestSubTeamChanging = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER)) {
            isAllowedForContestDeleting = true;
        }

        if (member.isIn(IUserPrivilege.Privileges.ADMIN) || member.isIn(IUserPrivilege.Privileges.MANAGER) || member.isIn(IUserPrivilege.Privileges.CONTEST_MANAGER)) {
            isAllowedForContestAdding = true;
        }
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
