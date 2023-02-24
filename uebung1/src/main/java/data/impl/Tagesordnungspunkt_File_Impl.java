package data.impl;

import data.Comment;
import data.Speech;
import data.Tagesordnungspunkt;

import java.util.ArrayList;

/**
 * implementation of an agenda iteam of a protocol
 */
public class Tagesordnungspunkt_File_Impl extends Protocol_File_Impl implements Tagesordnungspunkt {

    // creating the variable needed to set the information of an agenda item of a prtocol
    private String tTitle = "";

    //An agenda item can have more than one speech -> save all in one ArrayList
    private ArrayList<Speech> tSpeeches = new ArrayList<>();


    /**
     * constructor.
     */
    public Tagesordnungspunkt_File_Impl(){
    }

    @Override
    public void setTitle(String tTitle) {
        this.tTitle = tTitle;

    }

    @Override
    public String getTitle() {
        return this.tTitle;
    }

    @Override
     public void setSpeeches(Speech tSpeeches) {
        this.tSpeeches.add(tSpeeches);
    }

    @Override
    public ArrayList<Speech> getSpeeches() {
        return this.tSpeeches;
    }

}
