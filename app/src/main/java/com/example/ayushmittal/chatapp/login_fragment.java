package com.example.ayushmittal.chatapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class login_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextInputEditText username,password;
    private Button btn;
    private ProgressBar progressBar;
    private View v;

    private OnFragmentInteractionListener mListener;

    public login_fragment() {
        // Required empty public constructor
    }


    public static login_fragment newInstance(String param1, String param2) {
        login_fragment fragment = new login_fragment();
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
         v= inflater.inflate(R.layout.fragment_login_fragment, container, false);

        btn =(Button) v.findViewById(R.id.btn);

        username = (TextInputEditText) v.findViewById(R.id.username);
        password = (TextInputEditText) v.findViewById(R.id.password);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBar);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setTag("Authenticating...");

                if(chk()){

                    login_request();
                }


            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        } else {

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


    private boolean chk(){
        if(username.getText()==null||username.getText().toString().trim().equalsIgnoreCase(""))
        {   username.setFocusable(true);
            return false;}
        if(password.getText()==null||password.getText().toString().trim().equalsIgnoreCase("")){
            password.setFocusable(true);
            return false;
        }
        return true;
    }

    private void login_request(){


                FirebaseAuth mAuth = FirebaseAuth.getInstance();


                mAuth.signInWithEmailAndPassword(username.getText().toString().trim(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getActivity(), "login succesful", Toast.LENGTH_LONG).show();
                            changeactivity();
                            btn.setEnabled(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            btn.setEnabled(true);
                            Snackbar.make(v, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });



    }

    private void changeactivity(){
        Intent i=new Intent(getActivity(),Chattime.class);

        startActivity(i);
        getActivity().finish();

    }



}
