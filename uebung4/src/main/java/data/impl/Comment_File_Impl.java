package data.impl;

import data.Comment;

/**
 * Implementation of comments of a speech
 * @author Jawwad Khan
 */
public class Comment_File_Impl  implements Comment {

    // creating the variable needed to set the information of a comment
    private String comments = "";
    private float length = 0;
    private String sID = "";
    private String ssID = "";



    /**
     * constructor.
     */
    public Comment_File_Impl(){
    }

    @Override
    public void setContent(String cContent) {
        this.comments = cContent;
    }

    @Override
    public String getContent() {
        return this.comments;
    }

    @Override
    public void setCommentLength(Float cContent) {
        this.length = cContent;

    }

    @Override
    public Float getCommentLength() {
        return this.length;
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
    public void setSpeechID(String sID) {
        this.ssID = sID;

    }

    @Override
    public String getSpeechID() {
        return this.ssID;
    }
}
