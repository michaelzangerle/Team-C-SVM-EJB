/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.messages;

/**
 *
 * @author Gigis Home
 */
public class SubTeamMessage implements IMessage{
    
    private MessageType type;
    private int member;
    private int subTeam;
    private int receiver;

    public SubTeamMessage(MessageType type, int member, int subTeam) {
        this.type = type;
        this.member = member;
        this.subTeam = subTeam;
        this.receiver = member;
    }

    public MessageType getType() {
        return type;
    }

    public int getMember() {
        return member;
    }

    public int getSubTeam() {
        return subTeam;
    }

    public int getReceiverUID() {
        return receiver;
    }
}
