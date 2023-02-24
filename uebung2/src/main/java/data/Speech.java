package data;

import java.util.ArrayList;

/**
 * interface for mapping a speech of an agenda item of a protocol
 * @author Jawwad Khan
 */
public interface Speech extends Tagesordnungspunkt {

  /**
   * set the text of a speech
   * @param sText
   */
  public void setText(String sText);

  /**
   * return the text of a speech
   * @return
   */
  public String getText();

  /**
   * set the speaker of a speech
   * @param sSpeaker
   */
  public void setSpeaker(Speaker sSpeaker);

  /**
   * return the speaker of a speech
   * @return
   */
  public ArrayList<Speaker> getSpeaker();

  /**
   * set the comments of a speech
   * @param sComment
   */
  public void setComments(Comment sComment);

  /**
   * get the comments of a speech
   * @return
   */
  public ArrayList<Comment> getComments();

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
