package com.example.ayushmittal.chatapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayushmittal.chatapp.R;
import com.example.ayushmittal.chatapp.message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class converstaionadapter extends RecyclerView.Adapter<converstaionadapter.myViewHolder> {

    ArrayList<message> al;
    public converstaionadapter(ArrayList<message> list) {
        al=list;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        myViewHolder vh = new myViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.username.setText(al.get(position).getusername());
        holder.time.setText(al.get(position).gettime());

        if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equalsIgnoreCase(al.get(position).getusername())){
            holder.textr.setText(al.get(position).getmessagetext());
            holder.textr.setVisibility(View.VISIBLE);
        }
        else {
            holder.textl.setText(al.get(position).getmessagetext());
            holder.textl.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return al.size();
    }



    public class myViewHolder extends RecyclerView.ViewHolder{


        TextView username , textl, textr ,time ;

        public myViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            textl = (TextView) itemView.findViewById(R.id.textl);
            textr = (TextView) itemView.findViewById(R.id.textr);
            time = (TextView) itemView.findViewById(R.id.time);

        }
    }

}
