package data;

import java.util.ArrayList;

/**
 * Interface for mapping an agenda item of the protocol
 */
public interface Tagesordnungspunkt extends Protocol {

    /**
     * set the title of the agenda item
     * @param tTitle
     */
    public void setTitle(String tTitle);

    /**
     * return the title of the protocol
     * @return
     */
    public String getTitle();

    /**
     * set the Speechhes of one agenda item
     * @param tSpeeches
     */
    public void setSpeeches(Speech tSpeeches);

    /**
     * return all the speeches in one agenda item
     * @return
     */
    public ArrayList<Speech> getSpeeches();

}
