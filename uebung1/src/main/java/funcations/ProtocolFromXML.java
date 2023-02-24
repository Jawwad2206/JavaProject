package funcations;

import data.*;
import data.impl.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper Class for the UserInterface. This Class parses the XML files and returns all
 * filterd information in form a ArrayList.
 *
 * @author Jawwad
 */
public class ProtocolFromXML {

    /**
     * Creates an ArrayList which contains ArrayLists. The ArrayList contain Information
     * regarding all the protocols and Speakers. I
     * @param XmlPath
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArrayList<ArrayList> ProtocolCreater(String XmlPath) throws ParserConfigurationException, IOException, SAXException {

        ArrayList<ArrayList> information = new ArrayList<>();//Empty ArrayList where the ArrayList below will be added
        ArrayList<Object> protocols = new ArrayList();// Empty ArrayList which will have all the 239 protocols
        ArrayList<Object> allSpeaker = new ArrayList<>();// Empty ArrayList

        DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
        for (int x = 1; x <= 239; x++) { //For each protocol it will parse it and get the Information below.

            String newPath = (XmlPath + Integer.toString(x) + ".xml"); // Builds the path of one Xml file.
            if (x == 1) {
                System.out.println("Prozess[1/3] 10%|#_________|");
            } else if (x == 48) {
                System.out.println("Prozess[1/3] 20%|##________|");
            } else if (x == 72) {
                System.out.println("Prozess[1/3] 30%|###_______|");
            } else if (x == 120) {
                System.out.println("Prozess[1/3] 50%|#####_____|");
            } else if (x == 168) {
                System.out.println("Prozess[2/3] 70%|#######___|");
            } else if (x == 210) {
                System.out.println("Prozess[2/3] 90%|#########_|");
            }


            DocumentBuilder dBuilder = bFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(newPath)); // Creating a document class doc which contains the XML parsed file
            doc.getDocumentElement().normalize();
            Protocol pProtocol = new Protocol_File_Impl(); // Creating a new instance of the Protocol interface and Implementation for every Protocol we get

            //Set the date of a protocol
            NodeList date = doc.getElementsByTagName("datum"); // Creating a NodeList which contain all Elements where the NodeName = datum

            Node nDate = date.item(0);

            try {

                if (nDate.getNodeType() == Node.ELEMENT_NODE) { // Checking if the Node typ equal to the Typ Element Node
                    Element element = (Element) nDate;
                    String datum = element.getAttribute("date"); // Getting the content of the Node with the Attribute "date"
                    pProtocol.setDate(datum); // Set the Date of the protocol

                }
            } catch (NullPointerException e) {

            }

            // Set the session Number of a Protocol
            NodeList sessionNr = doc.getElementsByTagName("kopfdaten"); // Creating a NodeList with all the Nodes of "Kopfdaten"

            for (int i = 0; i <= sessionNr.getLength(); i++) {

                Node node = sessionNr.item(i);
                try {

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String snr = element.getElementsByTagName("sitzungsnr").item(0).getTextContent(); //Getting the Content of the Node which contains the session number
                        pProtocol.setSitzungsnr(snr); // set the session number of the protocol
                    }
                } catch (NullPointerException e) {

                }
            }

            //Set the start and end time of the session.
            NodeList protocolStart = doc.getElementsByTagName("sitzungsbeginn");
            NodeList protocolEnd = doc.getElementsByTagName("sitzungsende");


            Node pStart = protocolStart.item(0);
            Node pEnd = protocolEnd.item(0);
            try {
                if (pStart.getNodeType() == Node.ELEMENT_NODE) {

                    //Datum
                    Element element = (Element) pStart;
                    Element end = (Element) pEnd;
                    String start = element.getAttribute("sitzung-start-uhrzeit"); //Get start time
                    String ende = end.getAttribute("sitzung-ende-uhrzeit"); // Get end time

                    pProtocol.setEnd(ende); //set the end time
                    pProtocol.setStart(start); //set the start time
                }
            } catch (NullPointerException e) {
            }

            //Set the election period of the protocol
            NodeList wPeriod = doc.getElementsByTagName("kopfdaten");
            Node pPeriod = wPeriod.item(0); //get the first time of the Node
            try {
                if (pPeriod.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) pPeriod;
                    String period = element.getElementsByTagName("wahlperiode").item(0).getTextContent(); // get the election period
                    pProtocol.setPeriode(period);

                }
            } catch (NullPointerException e) {
            }

            //Set the MP of a session.
            String pMP = "";
            NodeList sessionStart = doc.getElementsByTagName("sitzungsbeginn"); // MP is mentioned in the NodeList "sitzungsbeginn" as one of his CHildnodes
            Element session = (Element) sessionStart.item(0);

            NodeList sMP = session.getElementsByTagName("p"); //Mp name is in the Childnode named "p"
            if(sMP.getLength() == 0){ // If there is no Childnode p, then it is Alterspresident Dr. Otto Solms who started the session
                pMP = "Alterspresident Dr. Otto Solms";
                pProtocol.setLeader(pMP);

            }else {
                Element nMP = (Element) sMP.item(0);
                if(nMP.getAttribute("klasse").equals("N")){ //The Childnode has the structure klasse N = name of MP. if True then get his name
                    pMP = nMP.getTextContent(); //get the string which contains the name
                    pProtocol.setLeader(pMP);

                }else {
                    NodeList nameList = session.getElementsByTagName("name"); //get the Childnode with the tag "name" (alternative because the XMl changed the way they save the MP)
                    Element name = (Element) nameList.item(0);
                    pMP = name.getTextContent(); // get the name of the class
                    pProtocol.setLeader(pMP);
                }
            }

            //Get and set the required information from an agenda item
            NodeList agenda = doc.getElementsByTagName("tagesordnungspunkt"); //An agenda Item is saved in the Childnode named "Tagesordnungspunkt"

            for (int i = 0; i < agenda.getLength(); i++) { //we iterate through every agenda Item one by one and get the information separately


                Tagesordnungspunkt pTOP = new Tagesordnungspunkt_File_Impl(); //We create a new Object of the classs TagesOrdnungsPunkt (TOP) everytime we iterate though the for loop

                Node aItem = agenda.item(i);
                try {

                    if (aItem.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) aItem;
                        String TOPTitle = element.getAttribute("top-id");// get the tilte of the agenda
                        pTOP.setTitle(TOPTitle);


                        try {
                            for (int p = 0; p < agenda.item(i).getChildNodes().getLength(); p++) { //We iterate through all the Childnoded of the specific agenda to get the Speech and Speaker
                                Speech pSpeech = new Speech_File_Impl(); // Creating everytime we iterate a new Speech Object
                                Speaker pSpeaker = new Speaker_File_Impl(); // Creating everytime we iterate a new Speaker Object


                                NodeList sSpeechContent = element.getElementsByTagName("rede"); // get the Text of the Speech


                                String speechTextWithComments = sSpeechContent.item(p).getTextContent(); // get the Content of the Speech

                                String sSpeechWithoutComments = speechTextWithComments.replaceAll("\\(.*\\)", "");

                                String speechWithoutFirstLine = sSpeechWithoutComments.substring(sSpeechWithoutComments.indexOf("\n") + 1);

                                String speechWithoutFirstLineLine = speechWithoutFirstLine.substring(speechWithoutFirstLine.indexOf("\n") + 1);

                                pSpeech.setText(speechWithoutFirstLineLine);

                                //System.out.println(speechWithoutFirstLineLine);

                                Node rComment = sSpeechContent.item(p); //For every Speech we try to get all the comments
                                try {
                                    if (rComment.getNodeType() == Node.ELEMENT_NODE) {
                                        Element speechComments = (Element) rComment;
                                        float commentlength = 0;

                                        NodeList comments = speechComments.getElementsByTagName("kommentar"); // Comments are in the Childnode named "kommentar"
                                        for (int z = 0; z < comments.getLength(); z++) {

                                            Comment pComment = new Comment_File_Impl(); //For every comment we find we create a new Comment class object

                                            String pComments = comments.item(z).getTextContent(); // get the actual content of the node

                                            pComment.setContent(pComments); // set the Comment of one Speech.
                                            commentlength += pComments.length();
                                            pComment.setCommentLength(commentlength);
                                            pSpeech.setComments(pComment);// set all the Comments of the Speech
                                        }
                                    }

                                } catch (NullPointerException e) {

                                }

                                //get the Speaker of a Speech
                                NodeList nSpeaker = element.getElementsByTagName("redner"); // NodeList with all the ChildNodes with the Tag "redner"

                                try {
                                    Node sSpeaker = nSpeaker.item(p);
                                    if (sSpeaker.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eSpeaker = (Element) sSpeaker;

                                        String sID = eSpeaker.getAttribute("id"); // get the id of the Speaker

                                        pSpeaker.setID(sID); //set the id of the speaker

                                        //Get the first name
                                        try {
                                            NodeList nFirstName = eSpeaker.getElementsByTagName("vorname"); // get the NodeList where the first name of the speakers is entered
                                            String pFirstName = nFirstName.item(0).getTextContent(); // get the content -> name
                                            pSpeaker.setFirstName(pFirstName);
                                        } catch (NullPointerException e) {
                                            String pNameless = "Vornamelos"; // if there is no name enterd in the node, default value will be "Vornamelos"
                                            pSpeaker.setFirstName(pNameless);
                                        }

                                        //Get the Last name
                                        try {
                                            NodeList nLastName = eSpeaker.getElementsByTagName("nachname"); // get the  NodeList where the last name of the Speaker is entered
                                            String pLastName = nLastName.item(0).getTextContent();
                                            pSpeaker.setLastName(pLastName);
                                        } catch (NullPointerException e) {
                                            String pLastLess = "Nachnamelos"; // if there is no name enterd in the node, default value will be "Nachnamelos"
                                            pSpeaker.setLastName(pLastLess);
                                        }

                                        //Get the Party or Fraction of the Speaker. (Fraction = party)
                                        try {
                                            NodeList fraktion = eSpeaker.getElementsByTagName("fraktion"); // get the fraction of the speaker
                                            String pfraktion = fraktion.item(0).getTextContent();
                                            pSpeaker.setFraction(pfraktion);


                                        } catch (NullPointerException e) {
                                            String pFrakless = "fraktionslos"; //If the speaker is in no party or fraction, deafult Value : "fraktionslos"
                                            pSpeaker.setFraction(pFrakless);

                                        }
                                        try{
                                            NodeList role = eSpeaker.getElementsByTagName("rolle_lang");
                                            String pRole = role.item(0).getTextContent();
                                            pSpeaker.setRole(pRole);

                                        }catch (NullPointerException e){


                                        }

                                    }
                                } catch (NullPointerException e) {

                                }
                                //set the Speaker his Speech
                                pSpeech.setSpeaker(pSpeaker);

                                //Adds all the Speakers in a seperate List (helper Array for other Tasks)
                                allSpeaker.add(pSpeaker);

                                //set the Speech to the agenda Item
                                pTOP.setSpeeches(pSpeech);

                            }

                        } catch (NullPointerException e) {

                        }
                    }
                } catch (NullPointerException e) {

                }
                //set the agenda item to the protocol
                pProtocol.setTagesOrdnungsPunkte(pTOP);
            }


            // Add all the protocols in a helper Array
            protocols.add(pProtocol);

            //Add the ArraysList with all the protocols
            information.add(protocols);

            //The problem with the ArrayList of the all the Speaker is, that it contains duplicates, this for loops will delete all duplicates by matching the id.
            for (int p = 0; p < allSpeaker.size(); p++) {
                Speaker oSpeaker = (Speaker) allSpeaker.get(p);
                for(int k = p+1; k < allSpeaker.size(); k++){
                    Speaker objectSpeaker = (Speaker) allSpeaker.get(k);
                    if(oSpeaker.getID().equals(objectSpeaker.getID())){
                        allSpeaker.remove(k);
                    }
                }

                information.add(allSpeaker); // adds the ArrayList of the Speakers to

            }


        }
        return information; // Return the ArrayList of the ArrayList

    }
}









