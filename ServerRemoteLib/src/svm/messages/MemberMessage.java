/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.messages;

/**
 *
 * @author Gigis Home
 */
public class MemberMessage  implements IMessage{
    
    private MessageType type;
    private int member;
    private int receiver;

    public MemberMessage(MessageType type, int member, int receiver) {
        this.type = type;
        this.member = member;
        this.receiver = receiver;
    }


    public MessageType getType() {
        return type;
    }

    public int getMember() {
        return member;
    }

    @Override
    public int getReceiverUID() {
        return receiver;
    }
}
