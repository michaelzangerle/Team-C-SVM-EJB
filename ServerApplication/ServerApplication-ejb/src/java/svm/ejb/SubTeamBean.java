/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.modelInterfaces.*;
import svm.domain.abstraction.modeldao.IContestModelDAO;
import svm.domain.abstraction.modeldao.ISubTeamModelDAO;
import svm.ejb.dto.*;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;
import svm.persistence.abstraction.exceptions.NoSessionFoundException;
import svm.persistence.abstraction.exceptions.NotSupportedException;

/**
 *
 * @author mike
 */
@Stateful
public class SubTeamBean extends ControllerDBSessionBean<ISubTeamModelDAO> implements SubTeamBeanRemote {

    public SubTeamBean() {
        super(DomainFacade.getSubTeamModelDAO());
    }
    private ITeam team;
    private TeamDTO teamDTO;
    private IContest contest;
    private ContestDTO contestDTO;
    private ISubTeam subTeam;
    private SubTeamDTO subTeamDTO;
    private List<IMember> addedMember;
    private List<IMember> removedMember;

    @Override
    public void start(TeamDTO team, ContestDTO contest) throws PersistenceException, DomainException, LogicException {
        super.start();



        try {
            this.team = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), team.getUID());
            reattachObjectToSession(this.team);
            teamDTO = new InternalTeamDTO(this.team);

            this.contest = DomainFacade.getContestModelDAO().getByUID(getSessionId(), contest.getUID());
            reattachObjectToSession(this.contest);
            ContestDTO contestDTO = new ContestDTO(this.contest);

            this.subTeam = DomainFacade.getSubTeamModelDAO().get(getSessionId(), this.team, this.contest);
            reattachObjectToSession(this.contest);

            try {
                reattachObjectToSession(this.subTeam.getTeam());
                reattachObjectToSession(this.subTeam.getContest());

            } catch (Exception e) {
                System.out.println("ERROR");
            }
            this.subTeamDTO = new SubTeamDTO(subTeam);
            this.addedMember = new LinkedList<IMember>();
            this.removedMember = new LinkedList<IMember>();

        } catch (svm.domain.abstraction.exception.DomainException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new DomainException(ex.getMessage(), ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (NotSupportedException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }

    }

    @Override
    public void commit() throws LogicException, PersistenceException {
        try {
            startTransaction();
            getModelDAO().saveOrUpdate(getSessionId(), subTeam);
            flush();
            commitTransaction();
            super.commit();
            // TODO JMS

            addedMember.clear();
            removedMember.clear();
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void abort() throws PersistenceException {
        super.abort();
    }

    @Override
    public SubTeamDTO getSubTeam() throws LogicException {
        check();
        return subTeamDTO;
    }

    @Override
    public void setName(String name) throws LogicException {
        check();
        subTeam.setName(name);
        subTeamDTO.update(subTeam);
    }

    @Override
    public void addMember(MemberDTO member) throws LogicException, PersistenceException, DomainException {
        try {
            check();

            IMember m = null;
            IMember toSearch = DomainFacade.getMemberModelDAO().getByUID(getSessionId(), member.getUID());
            for (IMember x : subTeam.getTeam().getMembers()) {
                if (x.equals(toSearch)) {
                    m = x;
                }
            }

            if (m != null) {
                this.subTeam.addMember(m);
                if (!this.addedMember.contains(m)) {
                    this.addedMember.add(m);
                }
            } else {
                System.out.println("NULL addMember [subTeam]");
            }

            subTeamDTO.update(subTeam);
        } catch (svm.domain.abstraction.exception.DomainException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeMember(MemberDTO member) throws LogicException, DomainException {
        try {
            check();

            IMember m = null;
            IMember toSearch = DomainFacade.getMemberModelDAO().getByUID(getSessionId(), member.getUID());

            for (ISubTeamsHasMembers x : subTeam.getSubTeamMembers()) {
                if (x.getMember().equals(toSearch)) {
                    m = x.getMember();
                }
            }

            if (m != null) {
                this.subTeam.removeMember(m);
                if (!removedMember.contains(m)) {
                    removedMember.add(m);
                }
                System.out.println("subteamcontroller remove member finsh");
            } else {
                System.out.println("NULL removeMember [subTeam]");
            }


            subTeamDTO.update(subTeam);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(SubTeamBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<MemberDTO> getMembersOfSubTeam() throws LogicException, PersistenceException {
        check();

        List<MemberDTO> members = new LinkedList<MemberDTO>();
        for (ISubTeamsHasMembers member : subTeam.getSubTeamMembers()) {
            members.add(new MemberDTO(member.getMember()));
        }
        return members;


    }

    @Override
    public List<MemberDTO> getMemberOfTeam() throws LogicException, PersistenceException {
        List<MemberDTO> members = new LinkedList<MemberDTO>();
        for (IMember member : subTeam.getTeam().getMembers()) {
            members.add(new MemberDTO(member));
        }
        return members;

    }
}
