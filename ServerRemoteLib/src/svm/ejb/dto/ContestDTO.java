/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.ejb.dto;

import java.util.Date;
import svm.domain.abstraction.modelInterfaces.IContest;

/**
 *
 * @author mike
 */
public class ContestDTO extends DTO<IContest> {

    private String name;
    private Date start;
    private Date end;
    private float fee;
    private ContactDetailsDTO contactDetails;
    private SportDTO sport;
    private boolean finished;

    public String getName() {
        return name;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public float getFee() {
        return fee;
    }

    public ContactDetailsDTO getContactDetails() {
        return contactDetails;
    }

    public SportDTO getSport() {
        return sport;
    }

    public boolean isFinished() {
        return finished;
    }

    public ContestDTO(IContest m) {
        super(m);
        doUpdate(m);
    }

    @Override
    protected void doUpdate(IContest contest) {

        this.name = contest.getName();
        this.start = contest.getStart();
        this.end = contest.getEnd();
        this.fee = contest.getFee();
        if (!contest.getContactDetails().isNull()) {
            this.contactDetails = new ContactDetailsDTO(contest.getContactDetails());
        }
        if (!contest.getSport().isNull()) {
            this.sport = new SportDTO(contest.getSport());
        }
        this.finished = contest.getFinished();
    }
}
