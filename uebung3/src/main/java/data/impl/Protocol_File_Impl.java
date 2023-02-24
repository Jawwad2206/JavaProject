package data.impl;


import data.Protocol;
import data.Tagesordnungspunkt;

import java.util.ArrayList;
import java.util.Date;


/**
 * Implementation of a protocol
 * @author Jawwad Khan
 */
public class Protocol_File_Impl implements Protocol {

    // creating the variable needed to set the information of a protocol
    private Date pDate = null;
    private String pStart = "";
    private String pPeriode = "";
    private String pSitzungsnr = "";
    private String pEnd = "";
    private String pLeader = "";
    private String Extra = "";
    private Integer pHourStart = null;
    private Integer pHourEnd = null;
    private Integer pMinStart = null;
    private Integer pMinEnd = null;

    private ArrayList<Tagesordnungspunkt> pTOPs = new ArrayList<>();


    /**
     * constructor.
     */
    public Protocol_File_Impl(){

    }

    @Override
    public void setDate(Date pDate) {
        this.pDate = pDate;

    }

    @Override
    public Date getDate() {
        return this.pDate;
    }

    @Override
    public void setStart(String pStart) {
        this.pStart = pStart;

    }

    @Override
    public String getStart() {
        return this.pStart;
    }

    @Override
    public void setEnd(String pEnd) {
        this.pEnd = pEnd;

    }

    @Override
    public String getEnd() {
        return this.pEnd;
    }

    @Override
    public void setPeriode(String pPeriode) {
        this.pPeriode = pPeriode;
    }

    @Override
    public String getPeriode() {
        return this.pPeriode;
    }

    @Override
    public void setSitzungsnr(String pSrNummer) {
        this.pSitzungsnr = pSrNummer;

    }

    @Override
    public String getSitzungsnr() {
        return this.pSitzungsnr;
    }

    @Override
    public void setLeader(String pLeader) {
        this.pLeader = pLeader;

    }

    @Override
    public String getLeader() {
        return this.pLeader;
    }

    @Override
    public void setTagesOrdnungsPunkte(Tagesordnungspunkt pTOPs) {
        this.pTOPs.add(pTOPs);
    }

    @Override
    public ArrayList<Tagesordnungspunkt> getTagesOrdnungsPunkte() {
        return this.pTOPs;
    }

    @Override
    public void setExtra(String Extra) {
        this.Extra = Extra;
    }

    @Override
    public String getExtra() {
        return this.Extra;
    }

    @Override
    public void setHourStart(Integer Hour) {
        this.pHourStart = Hour;

    }

    @Override
    public Integer getHourStart() {
        return this.pHourStart;
    }

    @Override
    public void setHourEnd(Integer Hour) {
        this.pHourEnd = Hour;

    }

    @Override
    public Integer getHourEnd() {
        return this.pHourEnd;
    }

    @Override
    public void setMinuteStart(Integer Min) {
        this.pMinStart = Min;

    }

    @Override
    public Integer getMinuteStart() {
        return this.pMinStart;
    }

    @Override
    public void setMinuteEnd(Integer Min) {
        this.pMinEnd = Min;

    }

    @Override
    public Integer getMinuteEnd() {
        return this.pMinEnd;
    }
}
