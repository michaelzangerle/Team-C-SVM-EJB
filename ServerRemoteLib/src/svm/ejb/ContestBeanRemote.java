/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import svm.domain.abstraction.modelInterfaces.IContest;
import svm.ejb.dto.ContestDTO;
import svm.ejb.dto.LocationDTO;
import svm.ejb.dto.MatchDTO;
import svm.ejb.dto.SportDTO;
import svm.ejb.dto.TeamDTO;
import svm.ejb.exceptions.DomainException;
import svm.ejb.exceptions.LogicException;
import svm.ejb.exceptions.PersistenceException;

/**
 *
 * @author mike
 */
@Remote
public interface ContestBeanRemote extends SvmBean{
    
     public void start(ContestDTO contest) throws PersistenceException, DomainException, LogicException;

      void setContestName(String name) throws DomainException,LogicException;
      
    public void setContestStartDate(Date start) throws DomainException,LogicException;

    public void setContestEndDate(Date end) throws DomainException,LogicException;

    public void setContestFee(float val) throws DomainException,LogicException;
    

    public ContestDTO getTransferContest()throws LogicException;

    public void setPhone1(String val) throws DomainException,LogicException;

    public void setPhone2(String val) throws DomainException,LogicException;

    public void setEmail1(String val) throws DomainException,LogicException;

    public void setEmail2(String val) throws DomainException,LogicException;

    public void setFax(String val) throws DomainException,LogicException;

    public void setStreet(String val) throws DomainException,LogicException;

    public void setStreetNumber(String val) throws DomainException,LogicException;

    public void setLat(String val) throws DomainException,LogicException;

    public void setLong(String val) throws DomainException,LogicException;


    public void setLocation(LocationDTO location) throws DomainException,LogicException,PersistenceException;


    public void addMatch(TeamDTO home, TeamDTO away, Date start, Date end) throws LogicException,PersistenceException,DomainException;

    public void setDateForMatch(MatchDTO match, Date start) throws LogicException,PersistenceException;


    public void setResult(MatchDTO match, Integer home, Integer away) throws DomainException,PersistenceException,LogicException;

    public void setSport(SportDTO sport) throws LogicException,PersistenceException;


    public void setFinished(boolean finished) throws LogicException;

    public void addTeam(TeamDTO team) throws DomainException, PersistenceException,LogicException;


    public void removeTeam(TeamDTO team) throws DomainException,LogicException,PersistenceException;

    public List<TeamDTO> getTeams() throws PersistenceException,LogicException;
    
    public List<MatchDTO> getMatches() throws PersistenceException,LogicException;

    public List<TeamDTO> getPossibleTeams() throws PersistenceException, DomainException,LogicException;

    
}
