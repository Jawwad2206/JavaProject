package data.impl.mongoDB_impl;

import data.impl.Comment_File_Impl;
import org.bson.Document;

/**
 * Extenstion of the Comment_File_Impl which maps a comment into an MongoDB Document
 * @author Jawwad Khan
 */
public class Comment_MongoDB_Impl extends Comment_File_Impl {
    //MongoDB Document
    private Document pDocument = new Document();

    /**
     * constructor
     * @param pDocument
     */
    public Comment_MongoDB_Impl(Document pDocument){
        this.pDocument = pDocument;
    }

    @Override
    public String getContent(){
        return this.pDocument.getString("comment");
    }
}
