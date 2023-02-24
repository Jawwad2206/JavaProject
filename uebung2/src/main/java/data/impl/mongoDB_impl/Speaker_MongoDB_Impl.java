package data.impl.mongoDB_impl;

import data.impl.Speaker_File_Impl;
import org.bson.Document;

/**
 * Extenstion of the Speaker_File_Impl which maps a speaker into an MongoDB Document
 * @author Jawwad Khan
 */
public class Speaker_MongoDB_Impl extends Speaker_File_Impl {
    //MongoDB Document
    private Document sDocument = null;

    /**
     * constructor
     * @param pDocument
     */
    public Speaker_MongoDB_Impl(Document pDocument) {
        this.sDocument = pDocument;
    }

    @Override
    public String getID() {
        return this.sDocument.getString("_id");
    }

    @Override
    public String getFirstName() {
        return this.sDocument.getString("firstname");
    }

    @Override
    public String getLastName(){
        return this.sDocument.getString("lastname");
    }

    @Override
    public String getRole() {
        return this.sDocument.getString("role");
    }


    @Override
    public String getParty() {
        return this.sDocument.getString("party");
    }

    @Override
    public String getFraction() {
        return this.sDocument.getString("fraction");
    }

    @Override
    public Integer getAmountSpeeches() {
        return this.sDocument.getInteger("AmountSpeeches");
    }

    @Override
    public Integer getaverageSpeechLength() {
        return this.sDocument.getInteger("AverageSpeechLength");
    }
}
