/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateful;
import svm.domain.abstraction.DomainFacade;
import svm.domain.abstraction.exception.DomainAttributeException;
import svm.domain.abstraction.exception.DomainParameterCheckException;
import svm.domain.abstraction.modelInterfaces.*;
import svm.domain.abstraction.modeldao.IContestModelDAO;
import svm.domain.abstraction.modeldao.IMatchModelDAO;
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
public class ContestBean extends ControllerDBSessionBean<IContestModelDAO> implements ContestBeanRemote {

    private IContest contest;
    private ContestDTO contestDTO;
    private boolean isNewContest;

    public ContestBean() {
        super(DomainFacade.getContestModelDAO());
    }

    @Override
    public void start() throws PersistenceException, DomainException, LogicException {
        super.start();
        try {
            this.contest = getModelDAO().generateObject(getSessionId());
        } catch (InstantiationException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        isNewContest = true;
        reattachObjectToSession(contest);
        contestDTO = new ContestDTO(contest);
    }

    @Override
    public void restart() throws PersistenceException, DomainException, LogicException {
        start(this.contestDTO);
    }

    @Override
    @PermitAll
    public void start(ContestDTO contest) throws PersistenceException, DomainException, LogicException {
        super.start();
        try {
            this.contest = getModelDAO().getByUID(getSessionId(), contest.getUID());
            isNewContest = false;
            reattachObjectToSession(this.contest);
            this.contestDTO = new ContestDTO(this.contest);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void commit() throws LogicException, PersistenceException {
        try {
            startTransaction();
            getModelDAO().saveOrUpdate(getSessionId(), this.contest);
            flush();
            commitTransaction();
            super.commit();
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void abort() throws PersistenceException, LogicException {
        super.abort();
    }

    @Override
    public void setContestName(String name) throws DomainException, LogicException {
        check();
        try {
            this.contest.setName(name);
        } catch (DomainAttributeException ex) {
            throw new DomainException(ex.getMessage(), ex);
        }
        contestDTO.update(contest);
    }

    @Override
    public void setContestStartDate(Date start) throws DomainException, LogicException {
        check();
        try {
            this.contest.setStart(start);
        } catch (DomainParameterCheckException ex) {
            throw new DomainException(ex.getMessage(), ex);
        }
        contestDTO.update(contest);
    }

    @Override
    public void setContestEndDate(Date end) throws DomainException, LogicException {
        check();
        try {
            this.contest.setEnd(end);
        } catch (DomainParameterCheckException ex) {
            throw new DomainException(ex.getMessage(), ex);
        }
        contestDTO.update(contest);
    }

    @Override
    public void setContestFee(float val) throws DomainException, LogicException {
        check();
        try {
            this.contest.setFee(val);
        } catch (DomainParameterCheckException ex) {
            throw new DomainException(ex.getMessage(), ex);
        } catch (DomainAttributeException ex) {
            throw new DomainException(ex.getMessage(), ex);
        }
        contestDTO.update(contest);
    }

    @Override
    public ContestDTO getTransferContest() throws LogicException {
        check();
        return contestDTO;
    }

    @Override
    public void setPhone1(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setPhone1(val);
        contestDTO.update(contest);
    }

    @Override
    public void setPhone2(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setPhone2(val);
        contestDTO.update(contest);
    }

    @Override
    public void setEmail1(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setEmail1(val);
        contestDTO.update(contest);
    }

    @Override
    public void setEmail2(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setEmail2(val);
        contestDTO.update(contest);
    }

    @Override
    public void setFax(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setFax(val);
        contestDTO.update(contest);
    }

    @Override
    public void setStreet(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setStreet(val);
        contestDTO.update(contest);
    }

    @Override
    public void setStreetNumber(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setStreetNumber(val);
        contestDTO.update(contest);
    }

    @Override
    public void setLat(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setCoordLat(val);
        contestDTO.update(contest);
    }

    @Override
    public void setLong(String val) throws DomainException, LogicException {
        check();
        this.contest.getContactDetails().setCoordLong(val);
        contestDTO.update(contest);
    }

    @Override
    public void setLocation(LocationDTO location) throws DomainException, LogicException, PersistenceException {
        check();
        try {
            ILocation loc;
            loc = DomainFacade.getLocationModelDAO().getByUID(getSessionId(), location.getUID());
            this.contest.getContactDetails().setLocation(loc);
        } catch (DomainAttributeException ex) {
            throw new DomainException(ex.getMessage(), ex);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
        contestDTO.update(contest);
    }

    @Override
    public void addMatch(TeamDTO home, TeamDTO away, Date start, Date end) throws LogicException, PersistenceException, DomainException {
        check();
        try {

            IMatchModelDAO matchDao = DomainFacade.getMatchModelDAO();
            IMatch match;
            match = matchDao.generateObject(getSessionId());

            match.setStart(start);
            match.setEnd(end);
            match.setName(home.getName() + " - " + away.getName());

            if (home instanceof ExternalTeamDTO && away instanceof ExternalTeamDTO) {
                IExternalTeam a = DomainFacade.getExternalTeamModelDAO().getByUID(getSessionId(), home.getUID());
                IExternalTeam b = DomainFacade.getExternalTeamModelDAO().getByUID(getSessionId(), away.getUID());
                match.setContestants(a, b);
            } else if (home instanceof InternalTeamDTO && away instanceof InternalTeamDTO) {
                ITeam a = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), home.getUID());
                ITeam b = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), away.getUID());
                match.setContestants(a, b);
            } else if (home instanceof InternalTeamDTO && away instanceof ExternalTeamDTO) {
                ITeam a = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), home.getUID());
                IExternalTeam b = DomainFacade.getExternalTeamModelDAO().getByUID(getSessionId(), away.getUID());
                match.setContestants(a, b);
            } else if (home instanceof ExternalTeamDTO && away instanceof InternalTeamDTO) {
                IExternalTeam a = DomainFacade.getExternalTeamModelDAO().getByUID(getSessionId(), home.getUID());
                ITeam b = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), away.getUID());
                match.setContestants(a, b);
            } else {
                throw new LogicException("Neither internal nor external team!");
            }

            match.setContest(this.contest);

            this.contest.addMatch(match);
        } catch (svm.domain.abstraction.exception.DomainException ex) {
            throw new DomainException(ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSessionFoundException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotSupportedException ex) {
            Logger.getLogger(ContestBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        contestDTO.update(contest);
    }

    @Override
    public void setDateForMatch(MatchDTO match, Date start) throws LogicException, PersistenceException {
        check();
        IMatch m = null;
        IMatch toSearch;
        try {
            toSearch = DomainFacade.getMatchModelDAO().getByUID(getSessionId(), match.getUID());

            for (IMatch x : contest.getMatches()) {
                if (x.equals(toSearch)) {
                    m = x;
                }
            }

            if (m != null) {
                m.setStart(start);
                m.setEnd(start);
            }

        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }


        contestDTO.update(contest);
    }

    @Override
    public void setResult(MatchDTO match, Integer home, Integer away) throws DomainException, PersistenceException, LogicException {
        check();

        try {

            IMatch m = null;
            IMatch toSearch = DomainFacade.getMatchModelDAO().getByUID(getSessionId(), match.getUID());
            for (IMatch x : contest.getMatches()) {
                if (x.equals(toSearch)) {
                    m = x;
                }
            }

            if (m != null) {
                m.setResult(home, away);
                System.out.println(m.getAwayInternal().getName() + " - " + m.getHomeInternal().getName() + ": "
                        + m.getHomeResult() + " : " + m.getAwayResult());
            }
        } catch (svm.domain.abstraction.exception.DomainException ex) {
            throw new DomainException(ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }

        contestDTO.update(contest);
    }

    @Override
    public void setSport(SportDTO sport) throws LogicException, PersistenceException {
        try {
            check();
            this.contest.setSport(DomainFacade.getSportModelDAO().getByUID(getSessionId(), sport.getUID()));
            contestDTO.update(contest);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void setFinished(boolean finished) throws LogicException {
        check();
        this.contest.setFinished(finished);
        contestDTO.update(contest);
    }

    @Override
    public void addTeam(TeamDTO team) throws DomainException, PersistenceException, LogicException {
        check();
        try {
            if (team instanceof ExternalTeamDTO) {

                IExternalTeam t = DomainFacade.getExternalTeamModelDAO().getByUID(getSessionId(), team.getUID());
                this.contest.addExternalTeam(t);


            } else if (team instanceof InternalTeamDTO) {
                ITeam t = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), team.getUID());
                this.contest.addInternalTeam(t);
            }

        } catch (svm.domain.abstraction.exception.DomainException ex) {
            throw new DomainException(ex.getMessage(), ex);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (InstantiationException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }

        contestDTO.update(contest);
    }

    @Override
    public void removeTeam(TeamDTO team) throws DomainException, LogicException, PersistenceException {
        check();
        try {
            if (team instanceof ExternalTeamDTO) {

                IExternalTeam t = DomainFacade.getExternalTeamModelDAO().getByUID(getSessionId(), team.getUID());
                this.contest.removeExternalTeam(t);


            } else if (team instanceof InternalTeamDTO) {
                ITeam t = DomainFacade.getTeamModelDAO().getByUID(getSessionId(), team.getUID());
                this.contest.removeInternalTeam(t);
            }

        } catch (svm.domain.abstraction.exception.DomainException ex) {
            throw new DomainException(ex.getMessage(), ex);
        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }

        contestDTO.update(contest);
    }

    @Override
    public List<TeamDTO> getTeams() throws PersistenceException, LogicException {
        check();

        List<TeamDTO> result = new LinkedList<TeamDTO>();
        List<ITeam> internalTeams = this.contest.getTeams();
        List<IExternalTeam> externalTeams = this.contest.getExternalTeams();

        for (ITeam t : internalTeams) {

            result.add(new InternalTeamDTO(t));
        }

        for (IExternalTeam t : externalTeams) {

            result.add(new ExternalTeamDTO(t));
        }

        return result;

    }

    @Override
    public List<MatchDTO> getMatches() throws PersistenceException, LogicException {
        check();

        List<IMatch> matches = this.contest.getMatches();
        List<MatchDTO> result = new LinkedList<MatchDTO>();

        for (IMatch m : matches) {
            result.add(new MatchDTO(m));
        }

        return result;


    }

    @Override
    public List<TeamDTO> getPossibleTeams() throws PersistenceException, DomainException, LogicException {
        check();
        List<TeamDTO> result = new LinkedList<TeamDTO>();
        try {


            for (ITeam team : DomainFacade.getTeamModelDAO().getAll(getSessionId())) {
                if (isPossible(team)) {
                    result.add(new InternalTeamDTO(team));
                }
            }

            for (IExternalTeam extTeam : DomainFacade.getExternalTeamModelDAO().getAll(getSessionId())) {
                result.add(new ExternalTeamDTO(extTeam));
            }

        } catch (NoSessionFoundException ex) {
            throw new PersistenceException(ex.getMessage(), ex);
        }
        return result;
    }

    private boolean isPossible(ITeam team) {
        if (contest.getSport().isNull()) {
            return true;
        }
        return team.getSport().equals(contest.getSport());
    }
}
