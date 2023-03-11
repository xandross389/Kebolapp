package com.inbitart.kebolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.inbitart.kebolapp.controller.ContactsManager;
import com.inbitart.kebolapp.model.ChatMessage;
import com.inbitart.kebolapp.model.Contact;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ContactsManager contactsManager;
    private ArrayList<Contact> contacts;
    private RecyclerView recViewContacts;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactsManager = new ContactsManager(getApplicationContext());

        // only for probe proposes
        if (contactsManager.getCount() == 0) {

            contactsManager.insertContact(new Contact("Alejandro",true,"+5354389434","+5354389434","xandross389@yahoo.es",
                    "Estoy usando la ultima version de Kebolapp!!!",
                    new Date(),
                    false, null));
            contactsManager.insertContact(new Contact("Manuel", true,"+5354389678","+5354389678","manuel297yahoo.es",
                    "Estoy usando la ultima version de Kebolapp!!!",
                    new Date(),
                    false, null));
            contactsManager.insertContact(new Contact("Jose Luis",true,"+5354844787","+5354844787","jose2557@yahoo.es",
                    "Estoy usando la ultima version de Kebolapp!!!",
                    new Date(),
                    false, null));
            contactsManager.insertContact(new Contact("Ernesto",false,"+5357690225","+5357690225","erne93@yahoo.es",
                    "Estoy usando la ultima version de Kebolapp!!!",
                    new Date(),
                    false, null));
            contactsManager.insertContact(new Contact("Dalia", true,"+5359976737","+5359976737","daliaoz457@yahoo.es",
                    "Estoy usando la ultima version de Kebolapp!!!",
                    new Date(),
                    false, null));
            contactsManager.insertContact(new Contact("Julian",false,"+5358485477","+5358485477","juliprof8@yahoo.es",
                    "Estoy usando la ultima version de Kebolapp!!!",
                    new Date(),
                    false, null));
            contactsManager.insertContact(new Contact("Reinier",false,"+533955549","+533955549","reini87@yahoo.es",
                    "Estoy usando la ultima version de Kebolapp!!!",
                    new Date(),
                    false, null));
        }

        // loads contacts list
        contacts = (ArrayList<Contact>) contactsManager.getContactsList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent chatSession = new Intent(getApplicationContext(), ChatSessionActivity.class);
                startActivity(chatSession);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initViewObjects();
        initRecyclerView();

    }

    private void initRecyclerView() {
        //Inicialización RecyclerView
        recViewContacts = (RecyclerView) findViewById(R.id.recViewContacts);
        contactAdapter = new ContactAdapter(
                getApplicationContext(), contacts);
        recViewContacts.setAdapter(contactAdapter);

        //Lista VERTICAL
        recViewContacts.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        //Grid de 2 elementos
        // recViewMessages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recViewContacts.setItemAnimator(new DefaultItemAnimator());
    }

    private void initViewObjects() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent contactDetails = new Intent(getApplicationContext(),
                    ContactDetailsActivity.class);
            startActivity(contactDetails);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        contactAdapter.setOnItemClickListener(new ContactItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                    Intent chatSession = new Intent(v.getContext(), ChatSessionActivity.class);
                    chatSession.putExtra("CONTACT_ID", contacts.get(position).getId());
                    v.getContext().startActivity(chatSession);
            }
        });
    }
}
