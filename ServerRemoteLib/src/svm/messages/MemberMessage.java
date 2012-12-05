/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package svm.messages;

import java.io.Serializable;

/**
 *
 * @author Gigis Home
 */
public class MemberMessage  implements IMessage,Serializable{
    
    private MessageType type;
    private int member;
    private int receiver;
    private String text;

    public MemberMessage(MessageType type, int member, int receiver, String text) {
        this.type = type;
        this.member = member;
        this.receiver = receiver;
        this.text = text;
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
