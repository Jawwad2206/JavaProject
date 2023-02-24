package data.impl.mongoDB_impl;

import data.Comment;
import data.Speaker;
import data.impl.Speech_File_Impl;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Extenstion of the Speech_File_Impl which maps a speech into an MongoDB Document
 * @author Jawwad Khan
 */
public class Speech_MongoDB_Impl extends Speech_File_Impl {
    //MongoDB Document
    private Document pDocument = null;

    /**
     * constructor
     * @param pDocument
     */
    public Speech_MongoDB_Impl(Document pDocument){
        this.pDocument = pDocument;
    }

    @Override
    public String getText(){
        return this.pDocument.getString("content");
    }
    @Override
    public String getSpeechID(){
        return this.pDocument.getString("_id");
    }
    @Override
    public ArrayList<Speaker> getSpeaker(){
        return (ArrayList<Speaker>) this.pDocument.get("speaker");
    }

    public ArrayList<Comment> getComments(){
        return (ArrayList<Comment>) this.pDocument.get("comments");
    }
}
