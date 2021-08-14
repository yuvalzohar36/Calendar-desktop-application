package com.example.newapp;

public class TableViewHandler {
    private final String subject;
    private final String date;

    public TableViewHandler(String subject, String date){
        this.subject = subject;
        this.date = date;
    }
    public String getSubject(){return this.subject;}
    public String getDate(){
        return this.date;
    }
    //setters unnecessary
}
