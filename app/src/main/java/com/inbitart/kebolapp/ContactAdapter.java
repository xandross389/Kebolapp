package com.inbitart.kebolapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.inbitart.kebolapp.model.Contact;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by XandrOSS on 18/08/2016.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private Context mContext;
    private ArrayList<Contact> contacts;
    private static ContactItemClickListener contactItemClickListener;

    public ContactAdapter(Context mContext, ArrayList<Contact> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
    }

    public void setOnItemClickListener(ContactItemClickListener contactItemClickListener) {
        this.contactItemClickListener = contactItemClickListener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        //  View itemView = inflater.inflate(R.layout.item_contact_1, parent, false);

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_1, parent, false);

        ContactViewHolder contactVH = new ContactViewHolder(itemView);
        return contactVH;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int position) {
        contactViewHolder.txtContactName.setText("");
        contactViewHolder.txtContactName.setText(contacts.get(position).getName());

        contactViewHolder.txtLastMessageContent.setText("");
        contactViewHolder.txtLastMessageContent.setText(contacts.get(position).getStateMessage());

        contactViewHolder.txtUserRegisterPhoneNumber.setText("");
        contactViewHolder.txtUserRegisterPhoneNumber.setText(
                contacts.get(position).getRegisterPhoneNumber().toString());

        if (contacts.get(position).getStateDate() != null) {
            contactViewHolder.txtLastMessageTime.setText("2016-08-26 12:13");
        } else {
            contactViewHolder.txtLastMessageTime.setText("");
        }

       // load image into view
       // Glide.with(mContext).load(contacts.get(position).getImageBytes()).
       //         centerCrop().into(contactViewHolder.imgContactPicture);

        if (contacts.get(position).isRegistered()) {
            contactViewHolder.btnInvite.setVisibility(View.INVISIBLE);
        } else {
            contactViewHolder.btnInvite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void refreshAdapterData(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtContactName;
        private TextView txtLastMessageContent;
        private TextView txtUserRegisterPhoneNumber;
        private TextView txtLastMessageTime;
        private Button btnInvite;
        private ImageButton imgContactPicture;

        public ContactViewHolder(View itemView) {
            super(itemView);

            // set animation
//            YoYo.with(Techniques.FlipInX)
//                    .duration(500)
//                    .playOn(itemView);

            itemView.setOnClickListener(this);

            txtContactName = (TextView) itemView.findViewById(R.id.txtContactName);
            txtLastMessageContent = (TextView) itemView.findViewById(R.id.txtLastMessageContent);
            txtUserRegisterPhoneNumber = (TextView) itemView.findViewById(R.id.txtUserRegisterPhoneNumber);
            txtLastMessageTime = (TextView) itemView.findViewById(R.id.txtLastMessageTime);
            btnInvite = (Button) itemView.findViewById(R.id.btnInvite);
            imgContactPicture = (ImageButton) itemView.findViewById(R.id.imgContactPicture);
        }

        @Override
        public void onClick(View view) {
            contactItemClickListener.onItemClick(getAdapterPosition(), view);
        }
    }
}
