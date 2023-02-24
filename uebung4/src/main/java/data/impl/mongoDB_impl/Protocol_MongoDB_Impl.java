package data.impl.mongoDB_impl;

import data.Tagesordnungspunkt;
import data.impl.Protocol_File_Impl;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Date;


/**
 *Extenstion of the Protocol_File_Impl which maps a Protocol into an MongoDB Document
 * @author Jawwad Khan
 */
public class Protocol_MongoDB_Impl extends Protocol_File_Impl {
    //MongoDB Document
    private Document pDocument = null;

    /**
     * constructor
     * @param pDocument
     */
    public Protocol_MongoDB_Impl(Document pDocument){
        this.pDocument = pDocument;
    }

    @Override
    public String getSitzungsnr(){
        return this.pDocument.getString("_id");
    }

    @Override
    public Date getDate(){
        return this.pDocument.getDate("date");
    }
    @Override
    public String getStart(){
        return this.pDocument.getString("starttime");
    }

    @Override
    public String getEnd(){
        return this.pDocument.getString("endtime");
    }
    @Override
    public String getLeader(){
        return this.pDocument.getString("mp");
    }
    @Override
    public ArrayList<Tagesordnungspunkt> getTagesOrdnungsPunkte(){
        return (ArrayList<Tagesordnungspunkt>) this.pDocument.get("TagesOrdnungspunkte");
    }
    @Override
    public Integer getHourStart(){
        return this.pDocument.getInteger("startHour");
    }
    @Override
    public Integer getHourEnd(){
        return this.pDocument.getInteger("endHour");
    }
    @Override
    public Integer getMinuteStart(){
        return this.pDocument.getInteger("startMin");
    }
    @Override
    public Integer getMinuteEnd(){
        return this.pDocument.getInteger("endMin");
    }
    public Integer getduractionHour(){
        return this.pDocument.getInteger("duration");
    }
    public Integer getduractionMin(){
        return this.pDocument.getInteger("durationMin");
    }



}
