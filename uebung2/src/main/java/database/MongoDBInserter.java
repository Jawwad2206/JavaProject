package database;


import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import data.Protocol;
import data.Speaker;
import data.impl.Comment_File_Impl;
import data.impl.Protocol_File_Impl;
import data.impl.Speech_File_Impl;
import data.impl.Tagesordnungspunkt_File_Impl;
import org.bson.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class has all the methods which adds the documents into the DB.
 * @author Jawwad Khan
 */
public class MongoDBInserter {

    /**
     * This Method inserts all protocols into the collection protocol
     *
     * @param xPath
     * @param pProtocol
     * @throws IOException
     */
    public static void protocolInserter(String xPath, ArrayList<Protocol> pProtocol, MongoCollection collection) throws IOException {
        ArrayList<Document> documentProtocol = new ArrayList<>(); //create one big ArrayList with documents
        for (int x = 0; x < pProtocol.size(); x++) { //go through every protocol
            Document docProtocol = new Document(); //for every protocol we create a new document
            Protocol_File_Impl protocol = (Protocol_File_Impl) pProtocol.get(x); // we create a new Protocol Object
            docProtocol.put("_id", protocol.getSitzungsnr()); //get the session nr
            docProtocol.put("date", protocol.getDate()); //get the date
            docProtocol.put("starttime", protocol.getStart()); //get the start time
            docProtocol.put("endtime", protocol.getEnd()); //get the end time
            docProtocol.put("startHour", protocol.getHourStart()); //get the start hour
            docProtocol.put("startMin", protocol.getMinuteStart()); //get the start min
            docProtocol.put("endHour", protocol.getHourEnd()); //get the end hour
            docProtocol.put("endMin", protocol.getMinuteEnd()); //get the end min
            docProtocol.put("mp", protocol.getLeader()); //get the mp of the session
            docProtocol.put("period", protocol.getPeriode()); //get the election period
            List<Document> agendaItems = new ArrayList<>(); // create another ArrayList which will contain agenda related information
            for (int y = 0; y < pProtocol.get(x).getTagesOrdnungsPunkte().size(); y++) { //for every agenda of one protocol
                Tagesordnungspunkt_File_Impl TOP = (Tagesordnungspunkt_File_Impl) pProtocol.get(x).getTagesOrdnungsPunkte().get(y); // create a new agenda Object
                String TopTitle = pProtocol.get(x).getSitzungsnr() + "-" + TOP.getTitle(); //create a title
                Document docAgenda = new Document();
                docAgenda.put("_id", TopTitle); // the id = the title created in line 39
                List<Document> speeches = new ArrayList<>();

                for (int i = 0; i < TOP.getSpeeches().size(); i++) { // for every speech of one agenda
                    Speech_File_Impl speech = (Speech_File_Impl) TOP.getSpeeches().get(i); // create a new speech object
                    Document docSpeech = new Document();

                    String speechText = speech.getText(); //Speech content
                    String speechID = speech.getSpeechID(); //speech id

                    docSpeech.put("_id", speechID); //speech Id is the _id for the db
                    docSpeech.put("content", speechText);
                    speeches.add(docSpeech);
                    List<Document> speaker = new ArrayList<>(); // create a list which will contain the speaker of the Speech
                    for (int a = 0; a < speech.getSpeaker().size(); a++) {
                        Document docSpeaker = new Document();
                        String speakerFirstName = speech.getSpeaker().get(a).getFirstName(); // get the speakers name
                        String speakerLastName = speech.getSpeaker().get(a).getLastName(); // get the speakers name
                        String speakerID = speech.getSpeaker().get(a).getID(); // get the speakers id
                        String speakerFraction = speech.getSpeaker().get(a).getFraction(); // get the speakers fraction
                        String speakerParty = speech.getSpeaker().get(a).getParty();
                        docSpeaker.put("_id", speakerID);
                        docSpeaker.put("firstname", speakerFirstName);
                        docSpeaker.put("lastname", speakerLastName);
                        docSpeaker.put("fraction", speakerFraction);
                        docSpeaker.put("party", speakerParty);
                        speaker.add(docSpeaker);
                        docSpeech.append("speaker", docSpeaker); //add speaker to the speech
                    }

                    List<Document> comments = new ArrayList<>(); // arraylist with the comments of one speech
                    for (int j = 0; j < speech.getComments().size(); j++) { // for every comment of one speech
                        Comment_File_Impl comment = (Comment_File_Impl) speech.getComments().get(j); //we create a new comment object
                        Document docComment = new Document();
                        String commentID = speech.getSpeechID(); // comment id = speech id
                        String commentContent = comment.getContent(); // content of the commen
                        docComment.put("_id", commentID);
                        docComment.put("comment", commentContent);
                        comments.add(docComment);
                    }
                    docSpeech.append("comments", comments); //add comments to the speech

                }
                docAgenda.append("speech", speeches); //add speech to the agenda
                agendaItems.add(docAgenda); //add the whole agenda to the list

            }
            docProtocol.append("Tagesordnungspunkte", agendaItems); //add all agendas of one protocol to the protocol
            documentProtocol.add(docProtocol);
        }
        //before adding the documents to the db we check if it is full.
        if (collection.countDocuments() == 0) { //if empty then all data is being added
            MongoDBConnectionHandler.dbInserter(xPath, documentProtocol, "protocol"); // insert the protocols to the db

        } else if (collection.countDocuments() != 239) {//if there are a few missing,the missing one is being added and the others are being checked

            System.out.println("A few Document are missing, correcting Data, please wait a min...");
            for (int j = 0; j < documentProtocol.size(); j++) { //every protocol is being checked
                Document document = documentProtocol.get(j);
                FindIterable findIterable = MongoDBConnectionHandler.find(document, collection); // search for the protocol in db
                try {
                    if (findIterable.iterator().equals(document)) { //if found then do nothing

                    } else {
                        MongoDBConnectionHandler.put(document, collection); //else add into the db

                        System.out.println("protocl inserted");
                    }
                } catch (MongoWriteException e) {
                    System.out.println("Protocol: " + j + " is complete and correct");
                }
            }
        } else if (collection.countDocuments() == 239) { //if db is full then we do nothing
            System.out.println("DB is full!");

        }

    }

    /**
     * This method inserts the speakers into the collection speaker
     *
     * @param xPath
     * @param sProtocol
     * @throws IOException
     */
    public static void speakerInserter(String xPath, ArrayList<Speaker> sProtocol, MongoCollection collection) throws IOException {
        ArrayList<Document> documentSpeaker = new ArrayList<>(); // one big arraylist with all the speakers
        for (int i = 0; i < sProtocol.size(); i++) { // for every speaker
            Document docSpeaker = new Document(); // create a new document
            String sFirstName = sProtocol.get(i).getFirstName(); // get the speakers' info from arraylist
            String sLastName = sProtocol.get(i).getLastName(); // get the speakers' info from arraylist
            String sID = sProtocol.get(i).getID(); // get the speakers' info from arraylist
            String sFraction = sProtocol.get(i).getFraction(); // get the speakers' info from arraylist
            String sParty = sProtocol.get(i).getParty(); // get the speakers' info from arraylist
            String sRole = sProtocol.get(i).getRole(); // get the speakers' info from arraylist
            docSpeaker.put("_id", i); // add the speakers' info into the document
            docSpeaker.put("id", sID); // add the speakers' info into the document
            docSpeaker.put("firstname", sFirstName); // add the speakers' info into the document
            docSpeaker.put("lastname", sLastName); // add the speakers' info into the document
            docSpeaker.put("fraction", sFraction); // add the speakers' info into the document
            docSpeaker.put("party", sParty); // add the speakers' info into the document
            docSpeaker.put("role", sRole); // add the speakers' info into the document
            documentSpeaker.add(docSpeaker); // add the speakers into the arraylist

        }
        //before adding the documents to the db we check if it is full.
        if (collection.countDocuments() == 0) { //if empty then all data is being added
            System.out.println("DB is empty, adding all data, please wait a min...");
            MongoDBConnectionHandler.dbInserter(xPath, documentSpeaker, "speaker"); //insert the speaker

        } else if (collection.countDocuments() != 783) { //if there are a few missing,the missing one is being added and the others are being checked

            System.out.println("A few Document are missing, correcting Data, please wait a min...");
            for (int j = 0; j < documentSpeaker.size(); j++) { //every speaker is being checked
                Document document = documentSpeaker.get(j);
                FindIterable findIterable = MongoDBConnectionHandler.find(document, collection); // search for the speaker in db
                try {
                    if (findIterable.iterator().equals(document)) {

                    } else {
                        MongoDBConnectionHandler.put(document, collection); // if we do not find the speaker than we add it into the db

                        System.out.println("speaker inserted");
                    }
                } catch (MongoWriteException e) {
                    System.out.println("speaker: " + j + " is complete and correct");
                }
            }
        } else if (collection.countDocuments() == 783) { //if db is full then we do nothing
            System.out.println("DB is full!");

        }
    }

    /**
     * This method inserts all abgeordnete from the Stammdatenblatt
     *
     * @param xPath
     * @param sProtocol
     */
    public static void abgeordnetenInserter(String xPath, ArrayList<Speaker> sProtocol, MongoCollection collection) throws IOException {
        ArrayList<Document> abgeordnete = new ArrayList<>();
        for (int i = 0; i < sProtocol.size(); i++) {
            Document docSpeaker = new Document(); // create a new document
            String sFirstName = sProtocol.get(i).getFirstName(); // get the speakers' info from arraylist
            String sLastName = sProtocol.get(i).getLastName(); // get the speakers' info from arraylist
            String sID = sProtocol.get(i).getID(); // get the speakers' info from arraylist
            String sFraction = sProtocol.get(i).getFraction(); // get the speakers' info from arraylist
            String sParty = sProtocol.get(i).getParty(); // get the speakers' info from arraylist
            docSpeaker.put("_id", sID); // add the speakers' info into the document
            docSpeaker.put("firstname", sFirstName); // add the speakers' info into the document
            docSpeaker.put("lastname", sLastName); // add the speakers' info into the document
            docSpeaker.put("fraction", sFraction); // add the speakers' info into the document
            docSpeaker.put("party", sParty); // add the speakers' info into the document

            abgeordnete.add(docSpeaker); // add the speakers into the arraylist

        }
        //before adding the documents to the db we check if it is full.
        if (collection.countDocuments() == 0) { //if empty then all data is being added
            MongoDBConnectionHandler.dbInserter(xPath,abgeordnete,"stammdatenblatt"); //insert the abgeordnete

        } else if (collection.countDocuments() != 4368) { //if there are a few missing,the missing one is being added and the others are being checked

            System.out.println("A few Document are missing, correcting Data, please wait a min...");
            for (int j = 0; j < abgeordnete.size(); j++) { //every Abgeordneter is being checked
                Document document = abgeordnete.get(j);
                FindIterable findIterable = MongoDBConnectionHandler.find(document, collection);
                try {
                    if (findIterable.iterator().equals(document)) {

                    } else {
                        MongoDBConnectionHandler.put(document, collection); // if we do not find the speaker than we add it into the db

                        System.out.println("Abgeordnete inserted");
                    }
                } catch (MongoWriteException e) {
                    System.out.println("Abgeordnete: " + j + " is complete and correct");
                }
            }
        } else if (collection.countDocuments() == 4368) { //if db is full then we do nothing
            System.out.println("DB is full!");
        }
    }
}


