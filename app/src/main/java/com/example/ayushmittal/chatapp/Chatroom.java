package com.example.ayushmittal.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ayushmittal.chatapp.adapter.converstaionadapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Chatroom extends AppCompatActivity {

    private DatabaseReference databaseReference;
   private ArrayList<message> arrayList;
    private RecyclerView mlistview;

    private EditText message1;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chatroom);




        final   String mcurrentuser=getIntent().getStringExtra("mcurrentuser");
       final String touser=getIntent().getStringExtra("touser");

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          getSupportActionBar().setTitle(touser);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        send=(Button) findViewById(R.id.button);
        message1=(EditText)findViewById(R.id.ssage);
        mlistview=(RecyclerView) findViewById(R.id.recyclerview);

        arrayList=new ArrayList<message>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mlistview.setLayoutManager(layoutManager);


        final converstaionadapter adapter = new converstaionadapter(arrayList);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Date d= Calendar.getInstance().getTime();
                String mess=message1.getText().toString();
                message1.setText("");
                message value= new message(mess,mcurrentuser,String.valueOf(d.toString()));
                FirebaseDatabase.getInstance().getReference("message").child(mcurrentuser+" to "+touser).push().setValue(value);
                FirebaseDatabase.getInstance().getReference("message").child(touser+" to "+mcurrentuser).push().setValue(value);



            }
        });


        findViewById(R.id.ll).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                scrollMyListViewToBottom();
            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference("message").child(mcurrentuser + " to " + touser);

         databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                message read = dataSnapshot.getValue(message.class);
                arrayList.add(read);
              //  Date d =(Date)read.time;
              //  conversation.add(FirebaseTextMessage.createForRemoteUser(read.text,read.time))
                scrollMyListViewToBottom();
                mlistview.setAdapter(adapter);

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



    private void scrollMyListViewToBottom() {
        mlistview.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mlistview.scrollToPosition(arrayList.size()-1);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Chatroom.this, Chattime.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
