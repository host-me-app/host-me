package ch.epfl.sweng.hostme.chat;

import java.util.Date;

/**
 * message chat with all relevant informations
 */
public class ChatMessage {
    public String senderId, receiverId, message, dateTime, apartId, documentName, image;
    public Boolean isDocument;
    public Date dateObject;
    public String conversionId, conversionName;
}
