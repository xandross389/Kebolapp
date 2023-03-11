package com.inbitart.kebolapp.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.inbitart.kebolapp.model.Contact;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by XandrOSS on 17/08/2016.
 */
public class ContactsManager {
    private Context mContext;
    private List<Contact> contactsList;
    private SQLiteDatabaseHelper sqliteDatabaseHelper = null;

    public ContactsManager(Context context) {
        this.mContext = context;

        // init database helper
        if (sqliteDatabaseHelper == null) {
            sqliteDatabaseHelper = OpenHelperManager
                    .getHelper(mContext, SQLiteDatabaseHelper.class);
        }

        // load messages from database
        try {
            contactsList = sqliteDatabaseHelper.getContactDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // release this instance of database helper
    public void closeSQLiteDatabaseHelper() {
        releaseHelper();
    }

    // release this instance of database helper
    public void releaseHelper() {
        if (sqliteDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            sqliteDatabaseHelper = null;
        }
    }

    public List<Contact> getContactsList() {
        return contactsList;
    }

    public Contact findContactByName(String contactName) {
        for (int i = 0; i < contactsList.size(); i++) {
          //  if (contactsList.get(i).getName().equalsIgnoreCase(contactName)) {
            if (true) {
                return contactsList.get(i);
            }
        }
        return null;
    }

    public int insertContact(Contact contact) {
        int insertedCotnactId = 0;
        try {
            final Dao<Contact, Integer> insertContact = sqliteDatabaseHelper.getContactDao();
            insertedCotnactId = insertContact.create(contact);

            contactsList.clear();
            contactsList = sqliteDatabaseHelper.getContactDao().queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
            insertedCotnactId = 0;
        }

        return insertedCotnactId;
    }

    public void setBlocked(Integer id, Boolean blockMessages) {

        try {
            final Dao<Contact, Integer> updateContactDao = sqliteDatabaseHelper.getContactDao();

            for (int i = 0; i > contactsList.size(); i++) {
                if (contactsList.get(i).getId() == id) {
                    contactsList.get(i).blockMessages(true);
                    updateContactDao.update(contactsList.get(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int updateContact(Contact contact) {
        int updatedMessageId = 0;

        try {
            final Dao<Contact, Integer> updateContactDao = sqliteDatabaseHelper.getContactDao();
            updatedMessageId = updateContactDao.update(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updatedMessageId;

    }

    public Contact findContactById(Integer contactId) {
        for (int i = 0; i < contactsList.size(); i++) {
            if (contactsList.get(i).getId() == contactId) {
               // contact = contactsList.get(i);
                //return contact;
                return contactsList.get(i);
            }
        }
        return null;
    }

    public int getCount() {
        return contactsList.size();
    }

    /**
     * Sincroniza la base de datos interna de contactos con los contactos del telefono
     * del usuario.
     * Para listar los usuarios del telefono sigue la siguiente politica:
     *
     * 1. Si no esta registrado, chequear que el contacto tenga numero celular (para poder enviar un sms
     *    via red movil o red wifi mediante el servidor Kebolapp de no estar registrado - INVITAR).
     *    Si tiene solo uno se usa el unico que tiene.
     * 2. Si el contacto tiene mas de un numero celular se usa el que sea de tipo MOVIL
     *    y en su defecto el primero de la lista.
     * 3. Agregar los del contacto a la lista interna y marcarlo como registered = false
     */
    public void synchronizeLocalContactsList() {

    }

     /*
     * Sincroniza la base de datos interna de contactos con los contactos del telefono
     * del usuario.
     * 1. Genera una lista con los numeros celulares de contactos registrados desde la base de datos
     *    interna y la del ContentProvider de contactos del telefono
     */
    public void generateContactsListToServer() {
        // TODO
    }

    /*
    * Envia al servidor, en formato JSON, la lista de numeros moviles de los cotnactos del usaurio
    * para que el servidor las sincrnonize
    * y recibe la lista de usuarios registrados (con todos los datos necesarios)
    * en el servidor que coinciden con los moviles proporcionados.
    * Posteriormente actualiza la base de datos de contactos local.
   */
    public void synchronizeServerContactList() {

    }

    private String getPhone(Uri uri) {
        /*
        Variables temporales para el id y el teléfono
         */
        String id = null;
        String phone = null;

        /************* PRIMERA CONSULTA ************/
        /*
        Obtener el _ID del contacto
         */
        Cursor contactCursor = mContext.getContentResolver().query(
                uri, new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();

        /************* SEGUNDA CONSULTA ************/
        /*
        Sentencia WHERE para especificar que solo deseamos
        números de telefonía móvil
         */
        String selectionArgs =
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE+"= " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

        /*
        Obtener el número telefónico
         */
        Cursor phoneCursor = mContext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                selectionArgs
                ,
                new String[] { id },
                null);

        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0);
        }

        phoneCursor.close();

        return phone;
    }


}