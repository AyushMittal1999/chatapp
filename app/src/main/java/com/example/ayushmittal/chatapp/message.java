package com.example.ayushmittal.chatapp;

public class message {
    String text;
    String username;
    String time;


    public message() {
    }

    public message(String text,String username,String time){
        this.username=username;
        this.text=text;
        this.time=time;
    }

public String getmessagetext(){
        return text;
}
public String getusername(){
        return username;
}

    public String gettime() {
        return time;
    }

 public void setime(String time){
        this.time=time;
 }

    public void setmessagetext(String text){
        this.text=text;
}

public void setmessageuser(String username){
        this.username=username;
}


}