package com.inbitart.kebolapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

/**
 * Created by XandrOSS on 09/08/2016.
 */
@DatabaseTable(tableName = "messages")
public class ChatMessage {

    // columns names
    public static final String ID_FIELD_NAME = "id";
    public static final String SENDER_FIELD_NAME = "sender";
    public static final String RECEIVER_FIELD_NAME = "receiver";
    public static final String STATE_FIELD_NAME = "state";
    public static final String SENT_DATETIME_FIELD_NAME = "sent_date";
    public static final String CONTENT_FIELD_NAME = "content";

    // posibles states for message state
    public static final int STATE_SENT = 0;
    public static final int STATE_QUEUED = 1;
    public static final int STATE_RECEIVED = 2;
    public static final int STATE_ERROR = 3;

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private Integer id;
    @DatabaseField(columnName = SENDER_FIELD_NAME, canBeNull = false, index = true, indexName = "sender_idx")
    private String senderUser;
    @DatabaseField(columnName = RECEIVER_FIELD_NAME, canBeNull = false)
    private String receiverUser;
    @DatabaseField(columnName = STATE_FIELD_NAME)
    private int state;
    @DatabaseField(columnName = SENT_DATETIME_FIELD_NAME)
    private Timestamp sentDate;
    @DatabaseField(columnName = CONTENT_FIELD_NAME)
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(String senderUser, String receiverUser, int state,
                        Timestamp sentTimestamp, String content) {
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
        this.state = state;
        this.sentDate = sentDate;
        this.content = content;
    }

    public ChatMessage(String senderUser, String receiverUser, int state,
                       String content) {
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
        this.state = state;
        this.content = content;
    }

    public boolean isForUser(String username){
        if (this.receiverUser.equals(username)) {
            return true;
        } else
            return false;
    }

    public boolean isFromUser(String username){
        if (this.senderUser.equals(username)) {
            return true;
        } else
            return false;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public String getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(String senderUser) {
        this.senderUser = senderUser;
    }

    public String getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(String receiverUser) {
        this.receiverUser = receiverUser;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getSentTimestamp() {
        return sentDate;
    }

    public void setSentTimestamp(Timestamp sentTimestamp) {
        this.sentDate = sentDate;
    }
}
