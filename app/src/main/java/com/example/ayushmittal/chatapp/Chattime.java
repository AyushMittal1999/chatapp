package com.example.ayushmittal.chatapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Chattime extends AppCompatActivity {



    private ArrayList<message> arrayList;
    private String mcurrentuser;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference2;
    private FirebaseAuth mAuth1;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private FloatingActionButton userbutton;
    private ListView userslist;
    private ArrayList arrayList1;
    private ProgressBar progressBar;


    public static final int RC_SIGN_IN = 1;
    private String touser=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattime);

        userbutton=(FloatingActionButton) findViewById(R.id.fab);


        arrayList=new ArrayList<message>();
        final userdapter arrayAdapter=new userdapter(Chattime.this,arrayList);

        arrayList1=new ArrayList<String>();
        userslist=(ListView)findViewById(R.id.list1);

        mAuth=FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);



        userbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userloading();

                userbutton.animate();
            }
        });



        userslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                touser= userslist.getItemAtPosition(position).toString();
                databaseReference=FirebaseDatabase.getInstance().getReference("message").child(mcurrentuser+" to "+touser);

                userslist.setAdapter(null);
                arrayList=new ArrayList<message>();


                Intent i= new Intent(Chattime.this,Chatroom.class);
                i.putExtra("mcurrentuser",mcurrentuser);
                i.putExtra("touser",touser);
                startActivity(i);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
    return true;}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected==R.id.action_search) {
            Toast.makeText(Chattime.this, "SignOut", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            arrayList.clear();
            Intent i=new Intent(Chattime.this,login.class);

            startActivity(i);
            this.finish();

        }

            return super.onOptionsItemSelected(item);
    }






    private void onSignedInInitialise(String username)
    {   mcurrentuser=username;

    }



    private void userloading(){


        arrayList1=new ArrayList<String>();
        arrayList1.clear();

        progressBar.setVisibility(View.VISIBLE);
        userbutton.setEnabled(false);

        databaseReference2=FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(!dataSnapshot.getKey().toString().equalsIgnoreCase( mcurrentuser)){
                arrayList1.add(dataSnapshot.getKey().toString());
                    ArrayAdapter<String> useradapter = new ArrayAdapter<String>(Chattime.this,android.R.layout.simple_list_item_1,arrayList1);
                    progressBar.setVisibility(View.GONE);
                    userbutton.setEnabled(true);
                userslist.setAdapter(useradapter);}
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        }

    @Override
    protected void onResume() {
        checkauthentication();
        super.onResume();
    }

    void checkauthentication(){


        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {

            onSignedInInitialise(firebaseAuth.getCurrentUser().getDisplayName());
            mcurrentuser=firebaseAuth.getCurrentUser().getDisplayName();

            databaseReference1=FirebaseDatabase.getInstance().getReference("users");
            databaseReference1.child(firebaseAuth.getCurrentUser().getDisplayName()).setValue("");

            userloading();
        }

        else
        {

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build() )
                            )
                            .build(),RC_SIGN_IN

            );

        }


    }

}

