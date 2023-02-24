package data.impl;

import data.Speaker;
import data.Speech;

import java.util.ArrayList;

/**
 * Implementation of a speaker of a speech of an agenda item of a protocol
 * @author Jawwad Khan
 */
public class Speaker_File_Impl extends Speech_File_Impl implements Speaker {

    // creating the variable needed to set the information of a speaker
    private String sFirstName = "";
    private String sLastName = "";
    private String sNameExtra = "";
    private String sFraction = "";
    private String sParty = "";
    private String sID = "";
    private String sRole = "";
    private Integer sAmount = 0;
    private Integer average = 0;
    private ArrayList<Speech> sSpeech = new ArrayList<>();

    /**
     * constructor.
     */
    public Speaker_File_Impl() {
    }

    @Override
    public void setFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    @Override
    public String getFirstName() {
        return this.sFirstName;
    }

    @Override
    public void setLastName(String sLastName) {
        this.sLastName = sLastName;

    }

    @Override
    public String getLastName() {
        return this.sLastName;
    }

    @Override
    public void setID(String sID) {
        this.sID = sID;
    }

    @Override
    public String getID() {
        return this.sID;
    }

    @Override
    public void setNameExtra(String sNameExtra) {
        this.sNameExtra = sNameExtra;

    }

    @Override
    public String getNameExtra() {
        return this.sNameExtra;
    }

    @Override
    public void setFraction(String sFraction) {
        this.sFraction = sFraction;
    }

    @Override
    public String getFraction() {
        return this.sFraction;
    }

    @Override
    public void setParty(String sParty) {
        this.sParty = sParty;
    }

    @Override
    public String getParty() {
        return this.sParty;
    }

    @Override
    public void setRole(String sRole) {
        this.sRole = sRole;

    }

    @Override
    public String getRole() {
        return this.sRole;
    }

    @Override
    public void setAmountSpeeches(Integer sAmount) {
        this.sAmount = sAmount;

    }

    @Override
    public Integer getAmountSpeeches() {
        return this.sAmount;
    }

    @Override
    public void setaverageSpeechLength(Integer average) {
        this.average = average;

    }

    @Override
    public Integer getaverageSpeechLength() {
        return this.average;
    }


}
