package data;


/**
 * interface for mapping a speaker of a speech of an agenda item of a protocol
 * @author Jawwad Khan
 */
public interface Speaker {

    /**
     * set the first name of a speaker
     * @param sFirstName
     */
    public void setFirstName(String sFirstName);

    /**
     * return the first name of a speaker
     * @return
     */
    public String getFirstName();

    /**
     * set the last name of a speaker
     * @param sLastName
     */
    public void setLastName(String sLastName);

    /**
     * return the last name of the speaker
     * @return
     */
    public String getLastName();

    /**
     * set the speaker id of a speaker
     * @param sID
     */
    public void setID(String sID);

    /**
     * return the speaker id of speaker
     * @return
     */
    public String getID();

    /**
     * set extra name titles of a speaker
     * @param sNameExtra
     */
    public void setNameExtra(String sNameExtra);

    /**
     * return the extra name title of speaker
     * @return
     */
    public String getNameExtra();

    /**
     * set the fraction of a speaker
     * @param sFraction
     */
    public void setFraction(String sFraction);

    /**
     * return the fraction of a speaker
     * @return
     */
    public String getFraction();

    /**
     * set the party of a speaker
     * @param sParty
     */
    public void setParty(String sParty);

    /**
     * return the party of speaker
     * @return
     */
    public String getParty();

    /**
     * set the Role of a speaker
     * @param sRole
     */
    public void setRole(String sRole);

    /**
     * return the Role of a speaker
     * @return
     */
    public String getRole();

    /**
     * set the amount of Speeches a Speaker has
     * @param sAmount
     */
    public void setAmountSpeeches(Integer sAmount);

    /**
     * return the amount of speeches a Speaker has
     * @return
     */
    public Integer getAmountSpeeches();

    /**
     * set the average Speech length of a Speaker
     * @param average
     */
    public void setaverageSpeechLength(Integer average);

    /**
     * return the average Speech length of a Speaker
     * @return
     */
    public Integer getaverageSpeechLength();

}
