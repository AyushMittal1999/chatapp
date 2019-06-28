package com.example.ayushmittal.chatapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class signup extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputEditText email, username, password, password2;
    private Button btn;
    private ProgressBar progressBar;
    private View v;

    private OnFragmentInteractionListener mListener;

    public signup() {
        // Required empty public constructor
    }


    public static signup newInstance(String param1, String param2) {
        signup fragment = new signup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_signup, container, false);


        email = (TextInputEditText) v.findViewById(R.id.email);
        password = (TextInputEditText) v.findViewById(R.id.password);
        password2 = (TextInputEditText) v.findViewById(R.id.password2);
        username = (TextInputEditText) v.findViewById(R.id.username);
        btn = (Button) v.findViewById(R.id.btn);
        progressBar = (ProgressBar)v.findViewById(R.id.progressBar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chk()) {

                    btn.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    request_signup();
                }
            }
        });

        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private boolean chk() {
        if (username.getText() == null || username.getText().toString().trim().equalsIgnoreCase("")) {
            username.setFocusable(true);
            return false;
        }
        if (password.getText() == null || password.getText().toString().trim().equalsIgnoreCase("")) {
            password.setFocusable(true);
            return false;
        }
        if (password2.getText() == null || password2.getText().toString().trim().equalsIgnoreCase("")) {
            password.setFocusable(true);
            return false;
        }
        if (!password2.getText().toString().equals(password.getText().toString())) {
            password2.setFocusable(true);
            Snackbar.make(v, "Password doesnot match", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void request_signup() {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    possible();

                } else {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    Snackbar.make(v, "Sign Up failed", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }


    public void updatedisplayname() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getEmail().equalsIgnoreCase(email.getText().toString().trim())) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username.getText().toString().toUpperCase()).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    Toast.makeText(getActivity(),"Signup Sucessfull",Toast.LENGTH_LONG).show();
                        changeactivity();
                    } else {

                        //Username not succesfull So delete user
                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressBar.setVisibility(View.GONE);
                                btn.setEnabled(true);
                                Snackbar.make(v, "Some error Occurred", Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        } else {
            FirebaseAuth.getInstance().signOut();
            Snackbar.make(v, "Some error Occurred", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void possible() {

        final ArrayList<String> userlist = new ArrayList<String>();
        final boolean[] a = {true};
       DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users");
       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot s:dataSnapshot.getChildren()){
                   if(username.getText().toString().equalsIgnoreCase(s.getKey())){
                        a[0] =false;
                   }
               }
               if(a[0]){
                   updatedisplayname();
               }
               else {
                   FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           progressBar.setVisibility(View.GONE);
                           btn.setEnabled(true);
                           Snackbar.make(v, "Username Not available", Snackbar.LENGTH_LONG).show();
                       }
                   });

               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void changeactivity(){
        Intent i=new Intent(getActivity(),Chattime.class);

        startActivity(i);
        getActivity().finish();

    }

}
