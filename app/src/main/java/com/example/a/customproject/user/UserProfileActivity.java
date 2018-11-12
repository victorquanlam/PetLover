package com.example.a.customproject.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a.customproject.R;
import com.example.a.customproject.petinfo.UserPet;
import com.example.a.customproject.posthome.PetCareHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {


    //FIREBASE AUTHENTICATION FIELDS
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    //PROGRESS DIALOG
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        //PROGRESS DIALOG INSTANCE
        mProgressDialog = new ProgressDialog(this);
        //ASSIGN ID'S
        final EditText userNameEditText = (EditText) findViewById(R.id.userProfileName);
        final EditText userPasswordEditText = (EditText) findViewById(R.id.userProfileStatus);
        LinearLayout changeUser = (LinearLayout) findViewById(R.id.saveProfile);
        LinearLayout addNewPet = (LinearLayout) findViewById(R.id.addNewPet);

        final Context context;
        context = this;

        //CREATE ON CLICK LISTENER
        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser user = mAuth.getCurrentUser();

                user.updateEmail(userNameEditText.getText().toString());


                user.updatePassword(userPasswordEditText.getText().toString());
                Toast.makeText(context, "Updating Success", Toast.LENGTH_SHORT).show();
            }



        });

        //CREATE ON CLICK LISTENER
        addNewPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(context, "Going to Add Page", Toast.LENGTH_SHORT).show();
                Intent moveToHome = new Intent(UserProfileActivity.this, UserPet.class);
                moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(moveToHome);
            }



        });

        //ASSIGN INSTANCE TO FIREBASE AUTH
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //LOGIC CHECK USER
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    finish();
                    Intent moveToHome = new Intent(UserProfileActivity.this, PetCareHome.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }

            }
        };


    }
}



