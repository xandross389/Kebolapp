package com.inbitart.kebolapp.controller;

import android.content.Context;
import android.os.Build;

import com.inbitart.kebolapp.model.ChatMessage;
import com.inbitart.kebolapp.model.Contact;
import com.inbitart.kebolapp.model.Country;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by XandrOSS on 17/08/2016.
 */
public class MessagesManager {
    private Context mContext;
    private List<ChatMessage> messagesList;
    private SQLiteDatabaseHelper sqliteDatabaseHelper = null;

    public MessagesManager(Context context) {
        this.mContext = context;

        // init database helper
        if (sqliteDatabaseHelper == null) {
            sqliteDatabaseHelper = OpenHelperManager
                    .getHelper(mContext, SQLiteDatabaseHelper.class);
        }

        // load messages from database
        final Dao<ChatMessage, Integer> chatMessageDao;
        try {
            chatMessageDao = sqliteDatabaseHelper.getChatMessageDao();
            messagesList = chatMessageDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // release this instance of database helper
    private void closeSQLiteDatabaseHelper() {
        releaseHelper();
    }

    // release this instance of database helper
    private void releaseHelper() {
        if (sqliteDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            sqliteDatabaseHelper = null;
        }
    }

    public List<ChatMessage> getMessagesList() {
        return messagesList;
    }

    public List<ChatMessage> getMessagesBySender(String contactName) {
        List<ChatMessage> messages = new ArrayList<>();

        for (int i = 0; i < messagesList.size(); i++) {
            if (messagesList.get(i).getSenderUser().equalsIgnoreCase(contactName)) {
                messages.add(messagesList.get(i));
            }
        }
        return messages;
    }

    public List<ChatMessage> getMessagesChatSession(String contactName) {
        List<ChatMessage> messages = new ArrayList<>();

        for (int i = 0; i < messagesList.size(); i++) {
            if (messagesList.get(i).getSenderUser().equalsIgnoreCase(contactName) ||
                    messagesList.get(i).getReceiverUser().equalsIgnoreCase(contactName)    ) {
                messages.add(messagesList.get(i));
            }
        }
        return messages;
    }

    public List<ChatMessage> clearMessagesFromUserName(String contactName) {
        List<ChatMessage> messages = new ArrayList<>();
        Collection<Integer> deleteMessagesId = new ArrayList<>();

        for (int i = 0; i < messagesList.size(); i++) {
            if (messagesList.get(i).getSenderUser().equalsIgnoreCase(contactName) ||
                    messagesList.get(i).getReceiverUser().equalsIgnoreCase(contactName)    ) {
                deleteMessagesId.add(messagesList.get(i).getId());
               // deleteMessageDao.deleteById(chatMessage);
            }
        }

        try {
            final Dao<ChatMessage, Integer> deleteMessageDao = sqliteDatabaseHelper.getChatMessageDao();
            deleteMessageDao.deleteIds(deleteMessagesId);
            reloadMessagesList();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        messagesList.clear();
        try {
            messagesList = sqliteDatabaseHelper.getChatMessageDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }


    public List<ChatMessage> getMessagesByReceiver(String contactName) {
        List<ChatMessage> messages = new ArrayList<>();

        for (int i = 0; i < messagesList.size(); i++) {
            if (messagesList.get(i).getReceiverUser().equalsIgnoreCase(contactName)) {
                messages.add(messagesList.get(i));
            }
        }
        return messages;
    }

    public int insertMessage(ChatMessage chatMessage) {
        int insertedMessageId = 0;
        try {
            final Dao<ChatMessage, Integer> insertMessage = sqliteDatabaseHelper.getChatMessageDao();
            insertedMessageId = insertMessage.create(chatMessage);
            reloadMessagesList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insertedMessageId;
    }

    private void reloadMessagesList() {
        messagesList.clear();
        try {
            messagesList = sqliteDatabaseHelper.getChatMessageDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int sendMessage(ChatMessage chatMessage) {
        chatMessage.setState(ChatMessage.STATE_QUEUED);
        return insertMessage(chatMessage);
    }

    public int updateMessage(ChatMessage chatMessage) {
        int updatedMessageId = 0;

        try {
            final Dao<ChatMessage, Integer> updateMessageDao = sqliteDatabaseHelper.getChatMessageDao();
            updatedMessageId = updateMessageDao.update(chatMessage);
            reloadMessagesList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedMessageId;
    }

    public int updateMessageState(int messageId) {
        int updatedMessageId = 0;

        for (int i = 0; i < messagesList.size(); i++) {
            if (messagesList.get(i).getId() == messageId) {
                updatedMessageId = updateMessage(messagesList.get(i));
            }
        }

        if (updatedMessageId != 0) {
        reloadMessagesList(); }

        return updatedMessageId;
    }

    public int getCount() {
        return messagesList.size();
    }
}
