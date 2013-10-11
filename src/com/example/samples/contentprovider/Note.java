package com.example.samples.contentprovider;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 9
 * Time: 오후 5:32
 * To change this template use File | Settings | File Templates.
 */
public class Note {
    public Note(String date, String name, String note) {
        this.date = date;
        this.name = name;
        this.note = note;
    }

    public String date;
    public String name;
    public String note;


}
