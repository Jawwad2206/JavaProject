package funcations;

import data.Speaker;
import data.impl.Speaker_File_Impl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parser for the Stammdatenblatt.
 */
public class StammdatenReader {

    /**
     * StammdatenParser, get all Politician with there name and party and return them all in one ArrayList.
     * @param path
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public ArrayList<Speaker> StammdatenParser(String path) throws ParserConfigurationException, IOException, SAXException {
        File inputFile = new File (path); // Path to the Stammdatenblatt

        ArrayList<Speaker> Abgeordnete = new ArrayList<>();


        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile); // Creating a document class doc which contains the XML parsed file

        NodeList abgeordente = doc.getElementsByTagName("MDB"); //All politician are in the Node MDB.

        for(int i = 0; i < abgeordente.getLength(); i++){
            Speaker ParlamentPeople = new Speaker_File_Impl(); // We create for every Politician a new Speaker class Object
            try{
                Node nNode = abgeordente.item(i);
                if(nNode.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) nNode;

                    NodeList allID = element.getElementsByTagName("ID"); // get the id of the Politician
                    String id = allID.item(0).getTextContent();
                    ParlamentPeople.setID(id);

                    NodeList allFirstName = element.getElementsByTagName("VORNAME"); // get the first name of the Politician
                    String vorname = allFirstName.item(0).getTextContent();
                    ParlamentPeople.setFirstName(vorname);

                    NodeList allLastName = element.getElementsByTagName("NACHNAME"); // get the last name of the Politician
                    String nachname = allLastName.item(0).getTextContent();
                    ParlamentPeople.setLastName(nachname);

                    NodeList allParties = element.getElementsByTagName("PARTEI_KURZ"); // get the party/fraction of the Politician
                    String party = allParties.item(0).getTextContent();
                    ParlamentPeople.setParty(party);

                    NodeList allFraction = element.getElementsByTagName("INS_LANG"); // get the party/fraction of the Politician
                    String fraction = allFraction.item(0).getTextContent();
                    ParlamentPeople.setFraction(fraction);

                    Abgeordnete.add(ParlamentPeople); // add them all in the ArrayList

                }

            }catch (NullPointerException e){

            }
        }
        return Abgeordnete; // return the array list with all the Politician
    }
}
