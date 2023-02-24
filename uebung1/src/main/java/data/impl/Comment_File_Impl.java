package data.impl;

import data.Comment;

/**
 * Implementation of comments of a speech
 */
public class Comment_File_Impl  implements Comment {

    // creating the variable needed to set the information of a comment
    private String comments = "";
    private float length = 0;



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
}
