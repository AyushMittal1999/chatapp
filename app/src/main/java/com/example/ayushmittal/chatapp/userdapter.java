package com.example.ayushmittal.chatapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class userdapter extends ArrayAdapter<message> {
    public userdapter(Context context, ArrayList<message> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        message user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.username);
        TextView tvHome = (TextView) convertView.findViewById(R.id.text);
        TextView tvtime = (TextView)convertView.findViewById(R.id.time);
        // Populate the data into the template view using the data object
        tvName.setText(user.getusername());
        tvHome.setText(user.getmessagetext());
        tvtime.setText(user.gettime());
        // Return the completed view to render on screen
        return convertView;
    }
}