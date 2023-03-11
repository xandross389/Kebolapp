package com.inbitart.kebolapp.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.inbitart.kebolapp.R;
import com.inbitart.kebolapp.model.ChatMessage;
import com.inbitart.kebolapp.model.Contact;
import com.inbitart.kebolapp.model.Country;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by XandrOSS on 06/08/2016.
 */
public class SQLiteDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "kebolapp.sqlite";
    // private static final String DATABASE_NAME = "/mnt/sdcard/kebolapp/kebolapp.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<Country, Integer> countryDao = null;
    private Dao<Contact, Integer> contactDao = null;
    private Dao<ChatMessage, Integer> chatMessageDao = null;
  //--  private static final AtomicInteger usageCounter = new AtomicInteger(0);

    // we do this so there is only one helper
   //-- private static DatabaseHelper helper = null;

    public SQLiteDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * Get the helper, possibly constructing it if necessary. For each call to this method, there should be 1 and only 1
     * call to {@link #close()}.

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
      //--  usageCounter.incrementAndGet();
        return helper;
    }
    */

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Country.class);
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, ChatMessage.class);

        } catch (SQLException e) {
            Log.d(SQLiteDatabaseHelper.class.getName(), "Error creando la base de datos", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Country.class, true);
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, ChatMessage.class, true);

            onCreate(sqLiteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.d(SQLiteDatabaseHelper.class.getName(),
                    "Error actualizando la base de datos de la versión " + oldVersion +
                            " a la " + newVersion, e);
        }
    }

    public Dao<Country, Integer> getCountryDao() throws SQLException {
        if (countryDao == null) {
            countryDao = getDao(Country.class);
        }
        return countryDao;
    }

    public Dao<Contact, Integer> getContactDao() throws SQLException {
        if (contactDao == null) {
            contactDao = getDao(Contact.class);
        }
        return contactDao;
    }

    public Dao<ChatMessage, Integer> getChatMessageDao() throws SQLException {
        if (chatMessageDao == null) {
            chatMessageDao = getDao(ChatMessage.class);
        }
        return chatMessageDao;
    }

    /**
     * Close the database connections and clear any cached DAOs. For each call to {@link #getHelper(Context)}, there
     * should be 1 and only 1 call to this method. If there were 3 calls to {@link #getHelper(Context)} then on the 3rd
     * call to this method, the helper and the underlying database connections will be closed.

    @Override
    public void close() {
        if (usageCounter.decrementAndGet() == 0) {
            super.close();
            countryDao = null;
            helper = null;
        }
    }
     */
}
