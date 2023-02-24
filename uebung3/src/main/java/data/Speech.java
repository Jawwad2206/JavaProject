package data;

import org.apache.uima.jcas.JCas;

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

  /**
   * Gets rede cas.
   *
   * @return the rede cas
   */
  JCas getRedeCAS();

  /**
   * Sets rede cas.
   *
   * @param RedeCAS the rede cas
   */
  void setRedeCAS(JCas RedeCAS);

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
   * set the session number of the protocol
   * @param pSrNummer
   */
  public void setSitzungsnr(String pSrNummer);

  /**
   * return the session number of the protocol
   * @return
   */
  public String getSitzungsnr();


}
