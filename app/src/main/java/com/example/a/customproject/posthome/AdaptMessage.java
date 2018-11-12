package com.example.a.customproject.posthome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a.customproject.R;
import com.example.a.customproject.model.ChatMessage;
import com.example.a.customproject.model.Pet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.Context.MODE_APPEND;


public class AdaptMessage extends Fragment {
    //firebase database
    DatabaseReference mInstaPostDatabase, mDatabaseMessage;
    FirebaseAuth mAuth;

    private  boolean mlikeProcess = false;

    //Recyclerview
    RecyclerView firebaseListRecyclerView;
    private StorageReference mStorageRef;

    public AdaptMessage() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AdaptMessage newInstance() {
        AdaptMessage fragment = new AdaptMessage();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //INflate the layout for this fragment
        final View roorView = inflater.inflate(R.layout.fragment_adapt_message,container,false);


        //Assign ID
        mInstaPostDatabase = FirebaseDatabase.getInstance().getReference().child("Message");


        mAuth = FirebaseAuth.getInstance();

        //mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseListRecyclerView=(RecyclerView) roorView.findViewById(R.id.recyclerChat);
        firebaseListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mL = new LinearLayoutManager(getActivity());
        firebaseListRecyclerView.setLayoutManager(mL);
        final Context context;
        context = getActivity();


        FirebaseRecyclerAdapter<ChatMessage, myViewHolder> adapter = new FirebaseRecyclerAdapter<ChatMessage, myViewHolder>(ChatMessage.class,R.layout.message,myViewHolder.class,mInstaPostDatabase) {
            @Override
            protected void populateViewHolder(final myViewHolder viewHolder, final ChatMessage model, int position) {

                viewHolder.userName.setText(model.getMessageUser());
                viewHolder.message.setText(model.getMessageTime() + " : "+model.getMessageText());




            }
        };

        firebaseListRecyclerView.setAdapter(adapter);



        Button send = (Button) roorView.findViewById(R.id.sendMessage);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageNeedToSend = (EditText) roorView.findViewById(R.id.messageNeed);
                mInstaPostDatabase.push().setValue(new ChatMessage(messageNeedToSend.getText().toString(),mAuth.getCurrentUser().getEmail().toString()));

                // Clear the input
               messageNeedToSend.setText("");

            }
        });






        return roorView;

    }


    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView userName,message;
        FirebaseAuth mAuth;
        Button send;



        public myViewHolder(View itemView) {
            super(itemView);

            //mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();


            userName = (TextView) itemView.findViewById(R.id.userNameMessage);
            message = (TextView) itemView.findViewById(R.id.MessageSent);
            send = (Button) itemView.findViewById(R.id.sendMessage);




        }
    }

}
