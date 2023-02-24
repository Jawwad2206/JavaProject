package data;

/**
 * interface for mapping the comments of a speech
 * @author Jawwad Khan
 */
public interface Comment {

    /**
     * set the comments of a speech
     * @param cContent
     */
    public void setContent(String cContent);

    /**
     * return the comments of a speech
     * @return
     */
    public String getContent();

    public void setCommentLength(Float length);

    public Float getCommentLength();

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
     * set the SpeechID of the Speech
     * @param sID
     */
    public void setSpeechID(String sID);

    /**
     * get the Speechid of the speech
     * @return
     */
    public String getSpeechID();

}
