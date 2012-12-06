/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.messages;

/**
 *
 * @author Gigis Home
 */
public class SubTeamMessage implements IMessage {

    private MessageType type;
    private int member;
    private int subTeam;
    private int receiver;
    private String text;

    public SubTeamMessage(MessageType type, int member, int subTeam, String text) {
        this.type = type;
        this.member = member;
        this.subTeam = subTeam;
        this.receiver = member;
        this.text = text;
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

    public String getText() {
        return text;
    }

    @Override
    public int getReceiverUID() {
        return receiver;
    }

    @Override
    public String toString() {
        return getText();
    }
}
