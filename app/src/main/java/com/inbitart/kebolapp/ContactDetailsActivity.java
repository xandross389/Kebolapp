package com.inbitart.kebolapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.inbitart.kebolapp.controller.ContactsManager;
import com.inbitart.kebolapp.model.Contact;

import java.util.Calendar;

public class ContactDetailsActivity extends AppCompatActivity {

    private ContactsManager contactsManager;
    private Contact contact;
    private SwitchCompat swBlockMessages;
    private TextView txtLastKnownUserState;
    private TextView txtLastKnownUserStateDate;
    private TextView txtUserMail;
    private TextView txtUserRegisterPhoneNumber;
    private ImageButton btnMail;
    private ImageButton btnPhone;
    private ImageButton btnMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Integer CONTACT_ID = getIntent().getExtras().getInt("CONTACT_ID", 0);

        contactsManager = new ContactsManager(getApplicationContext());
        contact = contactsManager.findContactById(CONTACT_ID);

       //contact = new Contact();
//        contact.setName("XandrOSS");
//        contact.setMailAddress("xandross389@yahoo.es");
//        contact.setRegisterPhoneNumber("+5354389534");
//        contact.blockMessages(true);
//        contact.setStateMessage("Hola, estoy usando la última versión de Kebolapp!!!");
        //contact.setStateTimestamp();
/*
        int id = contactsManager.insertContact(contact);

        if (id != 0) {
            Toast.makeText(getApplicationContext(), "Se inserto el contacto",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No se inserto el contacto",
                    Toast.LENGTH_SHORT).show();
        }
       */
        //   contact = contactsManager.getContactsList().get(0);
        setTitle(contact.getName());

        initViewObjects();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent chatSession = new Intent(getApplicationContext(), ChatSessionActivity.class);
                chatSession.putExtra("CONTACT_ID", contact.getId());
                startActivity(chatSession);
            }
        });

    }

    private void initViewObjects() {
        swBlockMessages = (SwitchCompat) findViewById(R.id.swBlockMessages);
        txtLastKnownUserState = (TextView) findViewById(R.id.txtLastKnownUserState);
        txtLastKnownUserStateDate = (TextView) findViewById(R.id.txtLastKnownUserStateDate);
        txtUserMail = (TextView) findViewById(R.id.txtUserMail);
        txtUserRegisterPhoneNumber = (TextView) findViewById(R.id.txtUserRegisterPhoneNumber);
        btnMail = (ImageButton) findViewById(R.id.btnMail);
        btnPhone = (ImageButton) findViewById(R.id.btnPhone);
        btnMessage = (ImageButton) findViewById(R.id.btnMessage);

        txtUserMail.setText(contact.getMailAddress());
        txtLastKnownUserState.setText(contact.getStateMessage());

        if (contact.getStateDate() != null) {
            txtLastKnownUserStateDate.setText("2016-08-26 12:13");
        } else {
            txtLastKnownUserStateDate.setText("");
        }

        swBlockMessages.setChecked(contact.isBlocked());
        txtUserRegisterPhoneNumber.setText(contact.getRegisterPhoneNumber());

        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] addressesArray = {txtUserMail.getText().toString()};
                writeEmail(addressesArray, getString(R.string.suggest_email_subject),
                        getString(R.string.suggest_email_body));
            }
        });

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPhoneCall(contact.getRegisterPhoneNumber());
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeSMS(contact.getRegisterPhoneNumber(), getString(R.string.write_sms_body));
            }
        });

        swBlockMessages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // contactsManager.setBlocked(contact.getId(), b);
                contact.blockMessages(b);
                contactsManager.updateContact(contact);
            }
        });

    }

    void writeSMS(String destinationNumber, String messageBody){
        Uri uri = Uri.parse("smsto:"+destinationNumber.trim());
        Intent writeSMS = new Intent(Intent.ACTION_SENDTO, uri);
        writeSMS.putExtra("sms_body", messageBody);
        startActivity(writeSMS);
    }

    void pickPhoneCall(String pPhoneNumber) {
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(
                    Uri.parse("tel:"+pPhoneNumber));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }catch(Exception e){
            Toast.makeText(getBaseContext(),
                    getString(R.string.pick_call_contact_err_message)+" ("+e.getMessage()+")",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    void pickPhoneDial(String pPhoneNumber) {
        try{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(
                    Uri.parse("tel:"+pPhoneNumber));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }catch(Exception e){
            Toast.makeText(getBaseContext(),
                    getString(R.string.pick_call_contact_err_message)+" ("+e.getMessage()+")",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    void writeEmail(String[] addresses, String subject, String message) {
        try {
            Intent emailActivity = new Intent(Intent.ACTION_SENDTO);
            emailActivity.setData(Uri.parse("mailto:")); // only email apps should handle this
            emailActivity.putExtra(Intent.EXTRA_EMAIL, addresses);
            emailActivity.putExtra(Intent.EXTRA_SUBJECT, subject);

            emailActivity.putExtra(Intent.EXTRA_TEXT, message);
            emailActivity.setType("message/rfc822");

            startActivity(Intent.createChooser(emailActivity, getString(R.string.suggest_email_chooser_title)));

            /*if (emailActivity.resolveActivity(getPackageManager()) != null) {
                startActivity(emailActivity);
            }*/

        } catch (Exception e){
            Toast.makeText(getBaseContext(),
                    getString(R.string.suggest_email_chooser_err_message)+" ("+e.getMessage()+")",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    void editContact(String contactName) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(
                Uri.parse("content://com.android.contacts/people/"+contactName));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            // launch edit contact intent

            editContact(contact.getName());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
