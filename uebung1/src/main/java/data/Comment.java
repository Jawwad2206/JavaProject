package data;

/**
 * interface for mapping the comments of a speech
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

}
