package data.impl;


import data.Protocol;
import data.Tagesordnungspunkt;

import java.util.ArrayList;


/**
 * Implementation of a protocol
 */
public class Protocol_File_Impl implements Protocol {

    // creating the variable needed to set the information of a protocol
    private String pDate = "";
    private String pStart = "";
    private String pPeriode = "";

    private String pSitzungsnr = "";

    private String pEnd = "";
    private String pLeader = "";
    private String Extra = "";
    private ArrayList<Tagesordnungspunkt> pTOPs = new ArrayList<>();


    /**
     * constructor.
     */
    public Protocol_File_Impl(){

    }

    @Override
    public void setDate(String pDate) {
        this.pDate = pDate;

    }

    @Override
    public String getDate() {
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
}
