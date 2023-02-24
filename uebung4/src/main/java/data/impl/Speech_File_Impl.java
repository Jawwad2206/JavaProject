package data.impl;

import data.Comment;
import data.Speaker;
import data.Speech;
import org.apache.uima.jcas.JCas;

import java.util.ArrayList;

/**
 * Implementation of a speech of an agenda item of a protocol
 * @author Jawwad Khan
 */
public class Speech_File_Impl extends Tagesordnungspunkt_File_Impl implements Speech {

    // creating the variable needed to set the information of a speech
    private String sText = "";

    private String ssID = "";

    private JCas redeCAS;

    private String sID = "";

    private String pSitzungsnr = "";

    // Depending on the amount of speeches, there will be more then one speaker
    private ArrayList<Speaker> sSpeaker = new ArrayList();

    //In one speech there can be more than one comments
    private ArrayList<Comment> sComments = new ArrayList();


    /**
     * constructor.
     */
    public Speech_File_Impl(){

    }

    @Override
    public void setText(String sText) {
        this.sText = sText;
    }

    @Override
    public String getText() {
        return this.sText;
    }

    @Override
    public void setSpeaker(Speaker sSpeaker) {
         this.sSpeaker.add(sSpeaker);
    }

    @Override
    public ArrayList<Speaker> getSpeaker() {
        return this.sSpeaker;
    }

    @Override
    public void setComments(Comment sComment) {
        this.sComments.add(sComment);
    }

    @Override
    public ArrayList<Comment> getComments() {
        return this.sComments;
    }

    @Override
    public void setSpeechID(String sID) {
        this.ssID = sID;

    }
    @Override
    public String getSpeechID() {
        return this.ssID;
    }

    @Override
    public JCas getRedeCAS() {
        return this.redeCAS;
    }

    @Override
    public void setRedeCAS(JCas RedeCAS) {
        this.redeCAS = RedeCAS;

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
    public void setSitzungsnr(String pSrNummer) {
        this.pSitzungsnr = pSrNummer;

    }

    @Override
    public String getSitzungsnr() {
        return this.pSitzungsnr;
    }


}
