package com.example.a.customproject.posthome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a.customproject.R;
import com.example.a.customproject.model.Pet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_APPEND;


public class PetStatus extends Fragment {





    //firebase database
    DatabaseReference mInstaPostDatabase, mDatabaseLikes;
    FirebaseAuth mAuth;

    private  boolean mlikeProcess = false;

    //Recyclerview
    RecyclerView firebaseListRecyclerView;
    private StorageReference mStorageRef;



    public PetStatus() {
        // Required empty public constructor
    }




    // TODO: Rename and change types and number of parameters
    public static PetStatus newInstance() {
        PetStatus fragment = new PetStatus();



        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //INflate the layout for this fragment
        View roorView = inflater.inflate(R.layout.fragment_pet_status,container,false);


        //Assign ID
        mInstaPostDatabase = FirebaseDatabase.getInstance().getReference().child("Pets");

        mAuth = FirebaseAuth.getInstance();

        //mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseListRecyclerView=(RecyclerView) roorView.findViewById(R.id.recyclerPetStatus);
       firebaseListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mL = new LinearLayoutManager(getActivity());
        firebaseListRecyclerView.setLayoutManager(mL);
        Query myQuery = mInstaPostDatabase.orderByChild("userId").equalTo(mAuth.getUid().toString());
        final Context context;
        context = getActivity();

        FirebaseRecyclerAdapter<Pet, myViewHolder> adapter = new FirebaseRecyclerAdapter<Pet, myViewHolder>(Pet.class,R.layout.post_pet,myViewHolder.class,myQuery) {
            @Override
            protected void populateViewHolder(final myViewHolder viewHolder, final Pet model, int position) {
                viewHolder.petName.setText(model.getName());
                viewHolder.feedText.setText("Feed your "+model.getName() + " every "+model.getFeed()+" hours");
                viewHolder.showerText.setText("Clean your "+model.getName() + " every "+model.getShower()+" hours");
                viewHolder.walkText.setText("Walk your "+model.getName() + " every "+model.getFeed()+" hours");


                String profileUrl = model.getImageUrl();

                StorageReference ref = mStorageRef.child("images/"+ model.getImageUrl().toString());

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.with(context).load(uri.toString()).into(viewHolder.petImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                viewHolder.showerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();   // given date
                        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);   // assigns calendar to given date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date currenTimeZone = (Date) calendar.getTime();


                        int now = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes =calendar.get(Calendar.MINUTE);

                        try {
                            OutputStream os = getContext().openFileOutput("history.txt", MODE_APPEND);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

                            bw.newLine();
                            bw.write("You have showered "+model.getName().toString()+" at "+currenTimeZone.toString()+". ");

                            bw.close();
                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }


                        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                        intent.putExtra(AlarmClock.EXTRA_HOUR,(now+Integer.parseInt(model.getShower()))%24);
                        intent.putExtra(AlarmClock.EXTRA_MINUTES,minutes);
                        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Setting up Alarm to shower your pet");
                        context.startActivity(intent);

                    }
                });

                viewHolder.feedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();   // given date
                        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);   // assigns calendar to given date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date currenTimeZone = (Date) calendar.getTime();


                        int now = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes =calendar.get(Calendar.MINUTE);

                        try {
                            OutputStream os = getContext().openFileOutput("history.txt", MODE_APPEND);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

                            bw.newLine();
                            bw.write("You have feed "+model.getName().toString()+" at "+currenTimeZone.toString()+". ");

                            bw.close();
                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }


                        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                        intent.putExtra(AlarmClock.EXTRA_HOUR,(now+Integer.parseInt(model.getFeed()))%24);
                        intent.putExtra(AlarmClock.EXTRA_MINUTES,minutes);
                        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Setting up Alarm to feed your pet");
                        context.startActivity(intent);

                    }
                });
                viewHolder.walkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();   // given date
                        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);   // assigns calendar to given date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date currenTimeZone = (Date) calendar.getTime();


                        int now = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes =calendar.get(Calendar.MINUTE);

                        try {
                            OutputStream os = getContext().openFileOutput("history.txt", MODE_APPEND);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

                            bw.newLine();
                            bw.write("You have walked "+model.getName().toString()+" at "+currenTimeZone.toString()+". ");

                            bw.close();
                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }


                        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                        intent.putExtra(AlarmClock.EXTRA_HOUR,(now+Integer.parseInt(model.getWalk()))%24);
                        intent.putExtra(AlarmClock.EXTRA_MINUTES,minutes);
                        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Setting up Alarm to walk your pet");
                        context.startActivity(intent);

                    }
                });

                viewHolder.contactButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();   // given date
                        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);   // assigns calendar to given date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date currenTimeZone = (Date) calendar.getTime();
                        String vet="quanhuoc89@gmail.com";



                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, vet);
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Activities with my pets. Please confirm my pet health status after checking these data.");


                        String message=" History of activities with "+model.getName().toString()+": ";
                        try{
                            FileInputStream fs =context.openFileInput("history.txt");

                            BufferedReader br= new BufferedReader(new InputStreamReader(fs));
                            String l;
                            while((l=br.readLine())!=null)
                            {
                                message =message + l +"; ";

                            }
                            br.close();
                            Log.e("no more lines","left");

                        }
                        catch (FileNotFoundException e){
                            e.printStackTrace();
                            Log.e("can't","not found");
                        }
                        catch (IOException e){
                            e.printStackTrace();
                            Log.e("can't","left");
                        }

                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                        // Grant temporary read permission to the content URI
                        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });








            }
        };




        firebaseListRecyclerView.setAdapter(adapter);




           return roorView;





        }



    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView petName;
        ImageView petImage,showerButton,contactButton,feedButton,walkButton;

        // DatabaseReference mDatabaseLikes;
        FirebaseAuth mAuth;
        TextView feedText,walkText,showerText;


        public myViewHolder(View itemView) {
            super(itemView);

            //mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();

            petImage=(ImageView) itemView.findViewById(R.id.petImage);
            showerButton=(ImageView) itemView.findViewById(R.id.shower);
            feedButton=(ImageView) itemView.findViewById(R.id.feed);
            walkButton=(ImageView) itemView.findViewById(R.id.walk);
            contactButton=(ImageView) itemView.findViewById(R.id.contact);
            petName = (TextView) itemView.findViewById(R.id.petName);
           showerText =(TextView) itemView.findViewById(R.id.textShower);
           feedText =(TextView) itemView.findViewById(R.id.textFeed);
           walkText =(TextView) itemView.findViewById(R.id.textWalk);


        }
    }


}




