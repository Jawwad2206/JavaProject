package data;

import java.util.ArrayList;
import java.util.Date;


/**
 * Interface for mapping a protocol
 * @author Jawwad Khan
 */
public interface Protocol  {


    /**
     * Set the date the protocol
     * @param pDate
     */
    public void setDate(Date pDate);

    /**
     * Return the date of the Protocol
     * @return
     */
    public Date getDate();

    /**
     * set the start time of the Protocol
     * @param pStart
     */
    public void setStart(String pStart);

    /**
     * Return the start time of the protocol
     * @return
     */
    public String getStart();

    /**
     * set the end time of the protocol
     * @param pEnd
     */
    public void setEnd(String pEnd);

    /**
     * Return the end time of the protocol
     * @return
     */
    public String getEnd();

    /**
     * set the election period of the protocol
     * @param pPeriode
     */
    public void setPeriode(String pPeriode);

    /**
     * return the election period of the protocol
     * @return
     */
    public String getPeriode();

    /**
     * set the session number of the protocol
     * @param pSrNummer
     */
    public void setSitzungsnr(String pSrNummer);

    /**
     * return the session number of the protocol
     * @return
     */
    public String getSitzungsnr();

    /**
     * set the MP of the protocol
     * @param pLeader
     */
    public void setLeader(String pLeader);

    /**
     * return the MP of the protocol
     * @return
     */
    public String getLeader();

    /**
     * set the agenda items of the protocol
     * @param pTOPs
     */
    public void setTagesOrdnungsPunkte(Tagesordnungspunkt pTOPs);

    /**
     * return the agenda items of the protocol
     * @return
     */
    public ArrayList<Tagesordnungspunkt> getTagesOrdnungsPunkte();

    /**
     * set the extra agenda documents for the protocol
     * @param Extra
     */
    public void setExtra(String Extra);

    /**
     * get the extra agenda documents for the protocol
     * @return
     */
    public String getExtra();

    /**
     * set the Hour of the start time
     * @param Hour
     */
    public void setHourStart(Integer Hour);

    /**
     * get the Hour of the start time
     * @return
     */
    public Integer getHourStart();

    /**
     * set the Hour of the end time
     * @param Hour
     */
    public void setHourEnd(Integer Hour);

    /**
     * get the hour of the end time
     * @return
     */
    public Integer getHourEnd();

    /**
     * set the Minute of the start time
     * @param Min
     */
    public void setMinuteStart(Integer Min);

    /**
     * get the Minute of the start time
     * @return
     */
    public Integer getMinuteStart();

    /**
     * set the Minute of the end time
     * @param Min
     */
    public void setMinuteEnd(Integer Min);

    /**
     * get the Minute of the end time
     * @return
     */
    public Integer getMinuteEnd();

}
