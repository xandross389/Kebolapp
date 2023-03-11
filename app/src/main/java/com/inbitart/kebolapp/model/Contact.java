package com.inbitart.kebolapp.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.ByteArrayType;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by XandrOSS on 17/08/2016.
 */
@DatabaseTable(tableName = "contacts")
public class Contact {

    public static final String ID_FIELD_NAME = "id";
    public static final String NAME_FIELD_NAME = "name";
    public static final String REGISTERED_FIELD_NAME = "registered";
    public static final String REGISTER_PHONE_NUMBER_FIELD_NAME = "register_phone_number";
    public static final String MAIL_ADDRESS_FIELD_NAME = "mail_address";
    public static final String STATE_MESSAGE_FIELD_NAME = "state";
    public static final String STATE_DATE_FIELD_NAME = "satate_date";
    public static final String BLOCK_MESSAGES_FIELD_NAME = "block_messages";
    public static final String MOBILE_PHONE_NUMBER_FIELD_NAME = "mobile_phone_number";
    public static final String PHOTO_FIELD_NAME = "user_image";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false, index = true, indexName = "sender_idx")
    private String name;
    @DatabaseField(columnName = REGISTERED_FIELD_NAME, canBeNull = false)
    private boolean registered;
    @DatabaseField(columnName = MOBILE_PHONE_NUMBER_FIELD_NAME)
    private String mobilePhoneNumber;
    @DatabaseField(columnName = REGISTER_PHONE_NUMBER_FIELD_NAME)
    private String registerPhoneNumber;
    @DatabaseField(columnName = MAIL_ADDRESS_FIELD_NAME)
    private String mailAddress;
    @DatabaseField(columnName = STATE_MESSAGE_FIELD_NAME)
    private String stateMessage;
    @DatabaseField(columnName = STATE_DATE_FIELD_NAME)
    private Date stateDate;
    @DatabaseField(columnName = BLOCK_MESSAGES_FIELD_NAME)
    private boolean blockMessages;
    @DatabaseField(columnName = PHOTO_FIELD_NAME, dataType = DataType.BYTE_ARRAY)
    private byte[] imageBytes;

    public Contact() {
        // Empty constructor needed for ORMLite
    }

    public Contact(String name, boolean registered, String mobilePhoneNumber,
                   String registerPhoneNumber, String mailAddress, String stateMessage,
                   Date stateDate, boolean blockMessages, byte[] imageBytes) {
        this.name = name;
        this.registered = registered;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.registerPhoneNumber = registerPhoneNumber;
        this.mailAddress = mailAddress;
        this.stateMessage = stateMessage;
        this.stateDate = stateDate;
        this.blockMessages = blockMessages;
        this.imageBytes = imageBytes;
    }

    public Contact(String name, Boolean registered, String registerPhoneNumber, String mailAddress,
                   String stateMessage, Date stateDate, Boolean blockMessages) {
        this.name = name;
        this.registered = registered;
        this.registerPhoneNumber = registerPhoneNumber;
        this.mailAddress = mailAddress;
        this.stateMessage = stateMessage;
        this.stateDate = stateDate;
        this.blockMessages = blockMessages;
    }

    public Contact(String name, String registerPhoneNumber, String mailAddress,
                   String stateMessage, Date stateDate, Boolean blockMessages) {
        this.name = name;
        this.registerPhoneNumber = registerPhoneNumber;
        this.mailAddress = mailAddress;
        this.stateMessage = stateMessage;
        this.stateDate = stateDate;
        this.blockMessages = blockMessages;
    }

    public Contact(String name, Boolean registered, String mobilePhoneNumber,
                   String registerPhoneNumber, String mailAddress) {
        this.name = name;
        this.registered = registered;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.registerPhoneNumber = registerPhoneNumber;
        this.mailAddress = mailAddress;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegisterPhoneNumber() {
        return registerPhoneNumber;
    }

    public void setRegisterPhoneNumber(String registerPhoneNumber) {
        this.registerPhoneNumber = registerPhoneNumber;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public Date getStateDate() {
        return stateDate;
    }

    public void setStateDate(Date stateDate) {
        this.stateDate = stateDate;
    }

    public Boolean isBlocked() {
        return blockMessages;
    }

    public void blockMessages(Boolean blockMessages) {
        this.blockMessages = blockMessages;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}
