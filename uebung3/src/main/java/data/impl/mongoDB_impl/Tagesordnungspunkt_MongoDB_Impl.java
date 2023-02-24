package data.impl.mongoDB_impl;

import data.Speech;
import data.impl.Tagesordnungspunkt_File_Impl;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Extenstion of the Tagesordnungspunkt_File_Impl which maps a Agenda into an MongoDB Document
 * @author Jawwad Khan
 */
public class Tagesordnungspunkt_MongoDB_Impl extends Tagesordnungspunkt_File_Impl {

    //MongoDB Document
    private Document pDocument = null;

    /**
     * constructor
     * @param pDocument
     */
    public Tagesordnungspunkt_MongoDB_Impl(Document pDocument){
         this.pDocument = pDocument;
    }

    @Override
    public String getTitle(){
        return this.pDocument.getString("_id");
    }

    @Override
    public ArrayList<Speech> getSpeeches() {
        return (ArrayList<Speech>) this.pDocument.get("speech");
    }

}
