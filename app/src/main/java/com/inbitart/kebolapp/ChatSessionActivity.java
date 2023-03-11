package com.inbitart.kebolapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.inbitart.kebolapp.controller.ContactsManager;
import com.inbitart.kebolapp.controller.MessagesManager;
import com.inbitart.kebolapp.model.ChatMessage;
import com.inbitart.kebolapp.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ChatSessionActivity extends AppCompatActivity {
    private ImageButton imgbtnSend;
    private EditText edtMessage;
    private Menu mMenu;

    private MessagesManager messagesManager;
    private Contact contactChat;
    private ArrayList<ChatMessage> messages;
    private RecyclerView recViewMessages;
    private ChatSessionAdapter chatSessionAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Integer CONTACT_ID = getIntent().getExtras().getInt("CONTACT_ID", 0);

      //  Toast.makeText(ChatSessionActivity.this, CONTACT_ID.toString(), Toast.LENGTH_SHORT).show();

        // search data for picked contact to show in ChatSession
        if (CONTACT_ID != 0) {
           ContactsManager contactsManager = new ContactsManager(getApplicationContext());

            contactChat = contactsManager.findContactById(CONTACT_ID);
            setTitle(contactChat.getName());

            messagesManager = new MessagesManager(getApplicationContext());

            // only for probe proposes
            if (messagesManager.getCount() == 0) {
                messagesManager.insertMessage(new ChatMessage("Xandross", contactChat.getName(),
                        ChatMessage.STATE_SENT, "Hola como esta todo por alla?"));

                messagesManager.insertMessage(new ChatMessage(contactChat.getName(),"Xandross",
                        ChatMessage.STATE_SENT, "muy bien"));

                messagesManager.insertMessage(new ChatMessage(contactChat.getName(),"Xandross",
                        ChatMessage.STATE_SENT, "Y por alla que tal"));

                messagesManager.insertMessage(new ChatMessage("Xandross",contactChat.getName(),
                        ChatMessage.STATE_SENT, "Igual, como siempre, que tal el chat"));

                messagesManager.insertMessage(new ChatMessage(contactChat.getName(),"Xandross",
                        ChatMessage.STATE_SENT, "que tal"));

                messagesManager.insertMessage(new ChatMessage("Xandross",contactChat.getName(),
                        ChatMessage.STATE_SENT, "Ok"));
            }

            messages = (ArrayList<ChatMessage>) messagesManager.getMessagesChatSession(contactChat.getName());
        }

        initViewObjects();
        initRecyclerView();
    }

    private void initViewObjects() {
        imgbtnSend = (ImageButton) findViewById(R.id.imgbtnSend);
        edtMessage = (EditText) findViewById(R.id.edtMessage);

        imgbtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (edtMessage.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.message_write_something_to_send, Toast.LENGTH_SHORT).show();
            } else {
                ChatMessage message = new ChatMessage("Xandross", contactChat.getName(),
                        ChatMessage.STATE_QUEUED, edtMessage.getText().toString());

                if (sendMessage(message) != 0) {
                    edtMessage.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), R.string.message_err_sending_message,
                            Toast.LENGTH_SHORT).show();
                }
            }
            }


        });
    }

    private int sendMessage(ChatMessage message) {
        int id;
        id = messagesManager.sendMessage(message);

        if (id != 0) {
            refreshMessagesList();
        }
        return id;
    }

    private void refreshMessagesList() {
        messages.clear();
        messages = (ArrayList<ChatMessage>) messagesManager.
                getMessagesChatSession(contactChat.getName());

      //  adaptador = (ChatSessionAdapter) recViewMessages.getAdapter();
        chatSessionAdapter.refreshAdapterData(messages);
        chatSessionAdapter.notifyDataSetChanged();

        ((RecyclerView) recViewMessages).scrollToPosition(chatSessionAdapter.getItemCount() - 1);

        // disable Clean chat option menu if there are not messages or enable it otherwise
        mMenu.setGroupVisible(R.id.group_unclear_chat, messages.size() > 0);
    }

    private void initRecyclerView() {

        //Inicialización RecyclerView
        recViewMessages = (RecyclerView) findViewById(R.id.recViewMessages);
        chatSessionAdapter = new ChatSessionAdapter(
                getApplicationContext(), messages);
        recViewMessages.setAdapter(chatSessionAdapter);

        //Lista VERTICAL
        recViewMessages.setLayoutManager(
               new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        //Grid de 2 elementos
       // recViewMessages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recViewMessages.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();

        chatSessionAdapter
                .setOnItemClickListener(new ChatMessageItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        /*Intent detailsIntent = new Intent(v.getContext(), DetailsActivity.class);
                        detailsIntent.putExtra("GAME_ROW_ID", searchResultGameList.get(position).getId());
                        v.getContext().startActivity(detailsIntent);*/

                        Toast.makeText(getApplicationContext(), "Mensaje de "+
                        messages.get(position).getSenderUser(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_session, menu);
        mMenu = menu;

        menu.setGroupVisible(R.id.group_unclear_chat, messages.size() > 0);
        menu.setGroupVisible(R.id.group_unblocked_contact, ! contactChat.isBlocked());
        menu.setGroupVisible(R.id.group_blocked_contact, contactChat.isBlocked());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_attach) {

            return true;
        } else if (id == R.id.action_block_contact) {

            ContactsManager contactsManager = new ContactsManager(getApplicationContext());

           // mMenu.setGroupVisible();

            contactChat.blockMessages(true);
            contactsManager.updateContact(contactChat);

            Toast.makeText(getApplicationContext(),
                    R.string.contact_blocked_message,
                    Toast.LENGTH_SHORT).show();

            mMenu.setGroupVisible(R.id.group_unblocked_contact, ! contactChat.isBlocked());
            mMenu.setGroupVisible(R.id.group_blocked_contact, contactChat.isBlocked());

            return true;
        } else if (id == R.id.action_unblock_contact) {

        ContactsManager contactsManager = new ContactsManager(getApplicationContext());

        contactChat.blockMessages(false);
        contactsManager.updateContact(contactChat);

        Toast.makeText(getApplicationContext(),
                R.string.contact_unblocked_message,
                Toast.LENGTH_SHORT).show();

        mMenu.setGroupVisible(R.id.group_unblocked_contact, ! contactChat.isBlocked());
        mMenu.setGroupVisible(R.id.group_blocked_contact, contactChat.isBlocked());

        return true;
        } else if (id == R.id.action_clean_chat) {

            messagesManager.clearMessagesFromUserName(contactChat.getName());
            refreshMessagesList();

            return true;
        } else if (id == R.id.action_contact_details) {
            Intent contactDetails = new Intent(getApplicationContext(), ContactDetailsActivity.class);
            contactDetails.putExtra("CONTACT_ID", contactChat.getId());
            startActivity(contactDetails);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
