package ui;

import com.mongodb.client.*;
import data.Protocol;
import data.Speaker;
import data.Speech;
import data.impl.mongoDB_impl.Protocol_MongoDB_Impl;
import data.impl.mongoDB_impl.Speaker_MongoDB_Impl;
import database.MongoDBConnectionHandler;
import database.MongoDBInserter;
import exceptions.WrongInputException;
import funcations.HelperFunctions;
import funcations.ProtocolFromXML;
import funcations.StammdatenReader;
import org.bson.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * This is the UserInterface, the main will start this class and in this class all the Tasks will be answered
 * @author Jawwad Khan
 */
public class UserInterfaceMongoDB {

    boolean CLOSE;
    boolean CLOSING;
    boolean CLOSED;

    /**
     * Starts the different parts of the Program.
     * @param txtpath
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws WrongInputException
     * @throws SAXException
     * @throws ParseException
     */
    public static void main3(String txtpath, String StammPath, String XMLPath) throws ParserConfigurationException, IOException, WrongInputException, SAXException, ParseException {
        UserInterfaceMongoDB menuForDB = new UserInterfaceMongoDB(); //we create a new Object of the Class Userinterface and name it menu.
        menuForDB.printHeader();
        menuForDB.MainMenu(txtpath, StammPath, XMLPath);
    }
    /**
     * Greeting Header
     */
    public void printHeader() {
        System.out.println("x-------------------------------x");
        System.out.println("|-------------------------------|");
        System.out.println("|---------Welcome to the--------|");
        System.out.println("|--------Programm for the-------|");
        System.out.println("|------Programmierpraktikum-----|");
        System.out.println("|-------------------------------|");
        System.out.println("x-------------------------------x");
    }

    /**
     * Loads all Speakers from the Stammdatenblatt.
     * @param path
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArrayList<Speaker> loadStammDaten(String path) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("The date is being checked");
        System.out.println("[1/3]");
        ArrayList<Speaker> AbgeordentenListe; // Creates a new ArrayList which will contain objects of the Class Speaker.
        System.out.println("[2/3]");
        AbgeordentenListe = new StammdatenReader().StammdatenParser(path); // we create a new  Stammdatenparser and put the parsed infos in the ArrayList created above.

        System.out.println("[3/3]");

        return AbgeordentenListe; //return this ArrayList for other methods.
    }

    /**
     * Loads and parses all XML files and puts in an ArrayList of ArrayList.
     *
     * @param path
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArrayList<ArrayList> loadXMLFIle(String path, String spath) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("The Data is being checked, please wait a minute");
        System.out.println("[1/3]");
        ArrayList<ArrayList> informationen; // Creates a new ArrayList which will contain ArrayList.
        System.out.println("[2/3]");
        informationen = ProtocolFromXML.ProtocolCreater(path, spath); // All Parsed XML and there respective Information will be added in this ArrayList created above.

        System.out.println("[3/3]");

        return informationen; //return this ArrayList for other methods.
    }

    /**
     * Compares the Speakers of the XML files and from the Stammdatenblatt and corrects there name or adds there name if
     * it is missing
     *
     * @param sSpeaker
     * @param srAbgeordnete
     * @return
     */
    public static ArrayList<Speaker> correctSpeaker(ArrayList<Speaker> sSpeaker, ArrayList<Speaker> srAbgeordnete) {
        ArrayList<Speaker> Speaker = sSpeaker; //contains all Speakers from the XML files
        ArrayList<Speaker> Abgeordnete = srAbgeordnete; // contains all the Speaker from the Stammdatenblatt

        for (int f = 0; f < Speaker.size(); f++) {
            if (Speaker.get(f).getFirstName() == "Vornamelos") { //Checks if in the XML there is a Speaker with no name.
                for (int g = 0; g < Abgeordnete.size(); g++) {
                    if (Abgeordnete.get(g).getID().equals(Speaker.get(f).getID())) { //compares ID from both ArrayList and then corrects -/ adds missing information
                        String vorname = Abgeordnete.get(g).getFirstName();//gets the first name of the broken Speaker
                        String nachname = Abgeordnete.get(g).getLastName();//gets the last name of the brocken Speaker

                        Speaker.get(f).setFirstName(vorname);//sets the first name
                        Speaker.get(f).setLastName(nachname);//sets the last name
                    }
                }
            }

            if (Speaker.get(f).getFraction() == "fraktionslos") { //Checks if there are Speaker who have wrongly been added to fractionless.
                for (int h = 0; h < Abgeordnete.size(); h++) {
                    if (Abgeordnete.get(h).getID().equals(Speaker.get(f).getID())) {
                        if (Abgeordnete.get(h).getParty().equals("CDU") || Abgeordnete.get(h).getParty().equals("CSU")) { // CDU -> CDU/CSU
                            String party = "CDU/CSU";
                            Speaker.get(f).setParty(party);
                            Speaker.get(f).setFraction(party);


                        } else {
                            String party = Abgeordnete.get(h).getParty(); // Adds the Party-/Fraction mention in the Stammdatenblatt to the Speaker.
                            Speaker.get(f).setFraction(party);

                        }
                    }
                }
            }
        }
        for (int e = 0; e < Speaker.size(); e++) {
            for (int g = 0; g < Abgeordnete.size(); g++) {
                if (Abgeordnete.get(g).getID().equals(Speaker.get(e).getID())) { //compares ID from both ArrayList and then corrects -/ adds missing information
                    Speaker.get(e).setParty(Abgeordnete.get(g).getParty());

                }
            }
        }

        return Speaker; //return the list of the corrected Speaker information.
    }


    /**
     * This is the MainMenu for Tasksheet 1 with this you can answer the task 2f and 3. This method will use all the
     * List and protocols created above.
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void MainMenu(String txtpath, String StammPath, String XMLPath) throws ParserConfigurationException, IOException, SAXException, WrongInputException, ParseException {
        System.out.println("Checking if DB is full, please wait a minute....");
        MongoDatabase database = MongoDBConnectionHandler.dbConnector(txtpath);
        MongoCollection mCollection = database.getCollection("speaker"); //get a connection with the collection speaker
        MongoCollection pCollection = database.getCollection("protocol"); //get a connection with the collection protocol
        MongoCollection sCollection = database.getCollection("stammdatenblatt"); //get a connection with the collection stammdatenblatt
        MongoCollection speechCollection = database.getCollection("speech");

        if (pCollection.countDocuments() == 239 && mCollection.countDocuments() == 783 && sCollection.countDocuments() == 4368) {
            System.out.println("All Protocols,Speaker and Abgeordnete are in the DB, do you want to replace all Data?");
            System.out.println("1: Check existing Documents for validity");
            System.out.println("2: Skip check and start the App");
            while (true) {
                Integer decision = HelperFunctions.integerInputs(1, 2);
                if (decision == 1) {
                    ArrayList<Speaker> allspeaker = new ArrayList<>();
                    ArrayList<Protocol> allProtocols = new ArrayList<>();
                    ArrayList<Speech> allSpeeches = new ArrayList<>();
                    ArrayList<Speaker> Abgeordnete = loadStammDaten(StammPath);// Creates an ArrayList with all the Speaker from the Stammdatenblatt
                    System.out.println("StammDatenBlatt download was complete!");
                    ArrayList<ArrayList> XMLInformatoíon = loadXMLFIle(XMLPath, StammPath);//Creates an ArrayList of ArrayList with all the Information
                    allspeaker = XMLInformatoíon.get(2); // ArrayList with all the speaker -> 783
                    allProtocols = XMLInformatoíon.get(0); //ArrayList with all the protocols
                    allSpeeches = XMLInformatoíon.get(0);


//                    System.out.println("A few Protocols are missing, updating everything...Please wait a min");
//                    MongoDBInserter.protocolInserter(txtpath, allProtocols, pCollection); //Inserts all Documents into the MongoDB
//                    System.out.println("[1/3]");
//
//                    System.out.println("A few speakers are missing, updating everything...Please wait a min");
//                    ArrayList<Speaker> speaker = correctSpeaker(allspeaker, Abgeordnete); //corrects all Speakers
//                    MongoDBInserter.speakerInserter(txtpath, speaker, mCollection);
//                    System.out.println("[2/3]");
//
//                    System.out.println("A few Abgeordnete are missing, updating everything...Please wait a min");
//                    MongoDBInserter.abgeordnetenInserter(txtpath, Abgeordnete, sCollection);
//                    System.out.println("[3/3]");

                    System.out.println("Inserting SPeeches");
                    MongoDBInserter.SpeechInserter(txtpath, allSpeeches, speechCollection);
                    break;

                }
                if (decision == 2) {
                    System.out.println("Preparing DB and starting the App.....");
                    break;
                }
            }

        } else if (pCollection.countDocuments() != 239 || mCollection.countDocuments() != 783 || sCollection.countDocuments() != 4368) {
            System.out.println("A few Protocols,Speaker and Abgeordnete are missing in the DB, do you want to replace all Data?");
            System.out.println("1: Add missing Documents and check for validity of the rest");
            System.out.println("2: Skip check and start the App");
            while (true) {
                Integer incomplete = HelperFunctions.integerInputs(1, 2);
                if (incomplete == 1) {
                    ArrayList<Speaker> allspeaker = new ArrayList<>();
                    ArrayList<Protocol> allProtocols = new ArrayList<>();
                    ArrayList<Speaker> Abgeordnete = loadStammDaten(StammPath);// Creates an ArrayList with all the Speaker from the Stammdatenblatt
                    System.out.println("StammDatenBlatt download was complete!");
                    ArrayList<ArrayList> XMLInformatoíon = loadXMLFIle(XMLPath, StammPath);//Creates an ArrayList of ArrayList with all the Information
                    allspeaker = XMLInformatoíon.get(1); // ArrayList with all the speaker -> 783
                    allProtocols = XMLInformatoíon.get(0); //ArrayList with all the protocols

                    System.out.println("A few Protocols are missing, updating everything...Please wait a min");
                    MongoDBInserter.protocolInserter(txtpath, allProtocols, pCollection); //Inserts all Documents into the MongoDB
                    System.out.println("[1/3]");

                    System.out.println("A few speakers are missing, updating everything...Please wait a min");
                    ArrayList<Speaker> speaker = correctSpeaker(allspeaker, Abgeordnete); //corrects all Speakers
                    MongoDBInserter.speakerInserter(txtpath, speaker, mCollection);
                    System.out.println("[2/3]");

                    System.out.println("A few Abgeordnete are missing, updating everything...Please wait a min");
                    MongoDBInserter.abgeordnetenInserter(txtpath, Abgeordnete, sCollection);
                    System.out.println("[3/3]");
                    break;
                }
                if (incomplete == 2) {
                    System.out.println("!Warning: You will start the App with incomplete Data");
                    System.out.println("Are you sure, you want to proceed? [1: yes or 2: no]");
                    while (true) {
                        Integer proceed = HelperFunctions.integerInputs(1, 2);
                        if (proceed == 1) {
                            break;
                        }
                        if (proceed == 2) {
                            System.out.println("Updating the Data");
                            ArrayList<Speaker> allspeaker = new ArrayList<>();
                            ArrayList<Protocol> allProtocols = new ArrayList<>();
                            ArrayList<Speaker> Abgeordnete = loadStammDaten(StammPath);// Creates an ArrayList with all the Speaker from the Stammdatenblatt
                            System.out.println("StammDatenBlatt download was complete!");
                            ArrayList<ArrayList> XMLInformatoíon = loadXMLFIle(XMLPath, StammPath);//Creates an ArrayList of ArrayList with all the Information
                            allspeaker = XMLInformatoíon.get(1); // ArrayList with all the speaker -> 783
                            allProtocols = XMLInformatoíon.get(0); //ArrayList with all the protocols

                            System.out.println("A few Protocols are missing, updating everything...Please wait a min");
                            MongoDBInserter.protocolInserter(txtpath, allProtocols, pCollection); //Inserts all Documents into the MongoDB
                            System.out.println("[1/3]");

                            System.out.println("A few speakers are missing, updating everything...Please wait a min");
                            ArrayList<Speaker> speaker = correctSpeaker(allspeaker, Abgeordnete); //corrects all Speakers
                            MongoDBInserter.speakerInserter(txtpath, speaker, mCollection);
                            System.out.println("[2/3]");

                            System.out.println("A few Abgeordnete are missing, updating everything...Please wait a min");
                            MongoDBInserter.abgeordnetenInserter(txtpath, Abgeordnete, sCollection);
                            System.out.println("[3/3]");
                            break;
                        }

                    }
                    break;
                }
            }

        } else if (pCollection.countDocuments() == 0 || mCollection.countDocuments() == 0 || sCollection.countDocuments() == 0) {
            ArrayList<Speaker> allspeaker = new ArrayList<>();
            ArrayList<Protocol> allProtocols = new ArrayList<>();
            ArrayList<Speaker> Abgeordnete = loadStammDaten(StammPath);// Creates an ArrayList with all the Speaker from the Stammdatenblatt
            System.out.println("StammDatenBlatt download was complete!");
            ArrayList<ArrayList> XMLInformatoíon = loadXMLFIle(XMLPath, StammPath);//Creates an ArrayList of ArrayList with all the Information
            allspeaker = XMLInformatoíon.get(1); // ArrayList with all the speaker -> 783
            allProtocols = XMLInformatoíon.get(0); //ArrayList with all the protocols


            MongoDBInserter.protocolInserter(txtpath, allProtocols, pCollection); //Inserts all Documents into the MongoDB
            System.out.println("[1/3]");


            ArrayList<Speaker> speaker = correctSpeaker(allspeaker, Abgeordnete); //corrects all Speakers
            MongoDBInserter.speakerInserter(txtpath, speaker, mCollection);
            System.out.println("[2/3]");


            MongoDBInserter.abgeordnetenInserter(txtpath, Abgeordnete, sCollection);
            System.out.println("[3/3]");
        }

        System.out.println("|----------Welcome to the Plenarprotocol App----------|");
        CLOSE = false;
        while (!CLOSE) {
            System.out.println("\nBelow you will see some Actions this Programm can perform");
            System.out.println("Please press the one of the following Numbers to perfrom an action: ");
            System.out.println("1. Task2\n");
            System.out.println("2. Tasksheet 1 with mongodb querys\n");
            System.out.println("3. End the Programm\n");
            int CHOICE = HelperFunctions.getInput();
            doMenu(CHOICE, pCollection, mCollection, sCollection); //Starts the Menu with the the given parameters
        }
    }

    /**
     * Through the input entered from the getInput one of the following task will be performed.
     *
     * @param CHOICE
     */
    public void doMenu(int CHOICE, MongoCollection pCollection, MongoCollection aCollection, MongoCollection sCollection) throws WrongInputException, IOException, ParseException {
        switch (CHOICE) {
            case 1:
                Task2Menu(pCollection); //starts the Menu for the second task
                break;
            case 2:
                TaskSheet1MongoQuery(pCollection, aCollection, sCollection);//starts the Menu for the Task Sheet 1 with MongoQuery
                break;
            case 3:
                System.out.println("|--------------------------------------------|");
                System.out.println("|------------See you next time---------------|");
                System.out.println("|--------------------------------------------|");
                CLOSE = true; // closes the MainMenu.
                break;
        }
    }

    /**
     * Shows the Task which can be performed with the Database
     *
     * @param pCollection
     * @throws WrongInputException
     * @throws IOException
     */
    public void Task2Menu(MongoCollection pCollection) throws WrongInputException, IOException, ParseException {
        CLOSING = false;
        while (!CLOSING) {
            System.out.println("\nBelow you will see some actions which you can perform");
            System.out.println("1.Speakers sorted(desc) by the number speeches with filter for fraction and party");
            System.out.println("2.Get the average speech length of every Speaker, fraction and party");
            System.out.println("3.Get the duration of every session sorted desc");
            System.out.println("4.Return to menu");
            int CHOICE = HelperFunctions.integerInputs(1,4);
            mainMenuTask2(CHOICE, pCollection); // starts one of the task from above
        }
    }


    /**
     * Main Menu for Task 2.
     *
     * @param CHOICE
     * @param pCollection
     * @throws WrongInputException
     * @throws IOException
     */
    public void mainMenuTask2(int CHOICE, MongoCollection pCollection) throws WrongInputException, IOException, ParseException {
        switch (CHOICE) {
            case 1:
                System.out.println("0: Return to selection screen; " +
                        "\n1: Get all Speaker; " +
                        "\n2: Get Speaker of a certain fraction; " +
                        "\n3: Get Speaker of a certain Party; ");
                Integer choice = HelperFunctions.integerInputs(1, 3); //get the Input to perform an action listed above
                if (choice == 1) {
                    System.out.println("0: Return to selection screen; " +
                            "\n1: Search for a specific date; " +
                            "\n2: Search in a range of date");
                    Integer options = HelperFunctions.integerInputs(1, 2); //get the Input to perform an action listed above
                    if (options == 1) {
                        while (true) {
                            try {
                                System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                        "the last meeting was: 2021-09-06");
                                String date = HelperFunctions.dateInput(); //gets a string Input for date
                                String dateIncrbyOne = date; // set another String which has the same value as the date input
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //we set a date format
                                Calendar c = Calendar.getInstance(); //create a new calender instance
                                c.setTime(sdf.parse(dateIncrbyOne)); // we parse date 2 into the format from above and set it as time for the calender instance
                                c.add(Calendar.DATE, 1);  // add one day
                                dateIncrbyOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countSpeechInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrbyOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),//open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")), // gets all the speaker from the agendas
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("amountspeeches", new Document().append("$sum", 1.0))),//counts how often a certain speaker talked
                                            new Document().append("$sort", new Document().append("amountspeeches", -1.0)));//sorts desc

                                    MongoCursor<Document> cursor = pCollection.aggregate(countSpeechInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry


                                    while (cursor.hasNext()) { //go through every document
                                        Document document = cursor.next();
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id")); //get the document in the document and parse it back as speaker
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the Fraction: " + speaker.getFraction()
                                                + " had: " + document.get("amountspeeches"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;

                    }

                    if (options == 2) {
                        System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                "the last meeting was: 2021-09-06");
                        while (true) {
                            try {
                                System.out.println("Information -> To get the full range: First Date = 2017-10-23 " +
                                        "and Second Date = 2021-09-06\n");
                                System.out.println("First Date");
                                String date = HelperFunctions.dateInput(); //gets a string Input for date
                                System.out.println("Second Date");
                                String dateIncrByOne = HelperFunctions.dateInput(); //gets a string Input for date
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //set a format for the date
                                Calendar c = Calendar.getInstance(); // create a new date instance
                                c.setTime(sdf.parse(dateIncrByOne)); //increment the second date by one
                                c.add(Calendar.DATE, 1);  // number of days to add
                                dateIncrByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countSpeechInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),//open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")), // gets all the speaker from the agendas
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("amountspeeches", new Document().append("$sum", 1.0))),//counts how often a certain speaker talked
                                            new Document().append("$sort", new Document().append("amountspeeches", -1.0)));//sorts desc

                                    MongoCursor<Document> cursor = pCollection.aggregate(countSpeechInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry


                                    while (cursor.hasNext()) { //go through every document
                                        Document document = cursor.next();
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id")); //get the document in the document
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the Fraction: " + speaker.getFraction()
                                                + " had: " + document.get("amountspeeches"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;
                    }
                }

                if (choice == 2) {
                    System.out.println("0: Return to selection screen; " +
                            "\n1: Search for a specific date; " +
                            "\n2: Search in a range of date");
                    Integer options = HelperFunctions.integerInputs(1, 2);
                    if (options == 1) {
                        while (true) {
                            try {
                                System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                        "the last meeting was: 2021-09-06\n");
                                String fraction = HelperFunctions.FractionInput();// gets the name of the fraction
                                System.out.println("\n");
                                String date = HelperFunctions.dateInput(); //gets a string Input for date
                                String dateIncrByOne = date; // set another String which has the same value as the date input
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //we set a date format
                                Calendar c = Calendar.getInstance(); //create a new calender instance
                                c.setTime(sdf.parse(dateIncrByOne)); // we parse date 2 into the format from above and set it as time for the calender instance
                                c.add(Calendar.DATE, 1);  // add one day
                                dateIncrByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countFractionInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),//open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")),//open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")),// gets all the speaker from the agendas
                                            new Document().append("$match", new Document().append("Tagesordnungspunkte.speech.speaker.fraction", fraction)),// gets all the speaker from the certain fraction
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("amountSpeeches", new Document().append("$sum", 1.0))),//counts how often a certain speaker talked
                                            new Document().append("$sort", new Document().append("amountSpeeches", -1.0)));//sorts desc

                                    MongoCursor<Document> cursor = pCollection.aggregate(countFractionInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry

                                    while (cursor.hasNext()) {//go through every document
                                        Document document = cursor.next();
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id"));//get the document in the document
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the Fraction: " + speaker.getFraction()
                                                + " had: " + document.get("amountSpeeches"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;

                    }
                    if (options == 2) {
                        System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                "the last meeting was: 2021-09-06");
                        while (true) {
                            try {
                                System.out.println("Information -> To get the full range: First Date = 2017-10-23 " +
                                        "and Second Date = 2021-09-06\n");
                                String fraction = HelperFunctions.FractionInput();// gets the name of the fraction
                                System.out.println("\n");

                                System.out.println("First Date");
                                String date = HelperFunctions.dateInput(); //gets a string Input for date
                                System.out.println("Second Date");
                                String dateIncreByOne = HelperFunctions.dateInput(); //gets a string Input for date
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //set a format for the date
                                Calendar c = Calendar.getInstance(); // create a new date instance
                                c.setTime(sdf.parse(dateIncreByOne)); //increment the second date by one
                                c.add(Calendar.DATE, 1);  // add date 2 by one
                                dateIncreByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countFractionInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncreByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),//open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")),//open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")),// gets all the speaker from the agendas
                                            new Document().append("$match", new Document().append("Tagesordnungspunkte.speech.speaker.fraction", fraction)),// gets all the speaker from the certain fraction
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("amountSpeeches", new Document().append("$sum", 1.0))),//counts how often a certain speaker talked
                                            new Document().append("$sort", new Document().append("amountSpeeches", -1.0)));//sorts desc

                                    MongoCursor<Document> cursor = pCollection.aggregate(countFractionInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry

                                    while (cursor.hasNext()) {//go through every document
                                        Document document = cursor.next();
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id"));//get the document in the document
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the Fraction: " + speaker.getFraction()
                                                + " had: " + document.get("amountSpeeches"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;
                    }
                }

                if (choice == 3) {
                    System.out.println("0: Return to selection screen; " +
                            "\n1: Search for a specific date; " +
                            "\n2: Search in a range of date");
                    Integer options = HelperFunctions.integerInputs(1, 2); //gets an int input to perfom an action listed above
                    if (options == 1) {
                        while (true) {
                            try {
                                System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                        "the last meeting was: 2021-09-06\n");
                                String party = HelperFunctions.PartyInput();// gets the name of the fraction
                                System.out.println("\n");
                                String date = HelperFunctions.dateInput(); //gets a string Input for date
                                String dateIncreByOne = date; // set another String which has the same value as the date input
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //we set a date format
                                Calendar c = Calendar.getInstance(); //create a new calender instance
                                c.setTime(sdf.parse(dateIncreByOne)); // we parse date 2 into the format from above and set it as time for the calender instance
                                c.add(Calendar.DATE, 1);   // add one day
                                dateIncreByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countFractionInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncreByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),//open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")),//open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")),// gets all the speaker from the agendas
                                            new Document().append("$match", new Document().append("Tagesordnungspunkte.speech.speaker.party", party)),// gets all the speaker from the certain fraction
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("amountSpeeches", new Document().append("$sum", 1.0))),//counts how often a certain speaker talked
                                            new Document().append("$sort", new Document().append("amountSpeeches", -1.0)));//sorts desc

                                    MongoCursor<Document> cursor = pCollection.aggregate(countFractionInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry

                                    while (cursor.hasNext()) {//go through every document
                                        Document document = cursor.next();
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id"));//get the document in the document
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the party: " + speaker.getParty()
                                                + " had: " + document.get("amountSpeeches"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;
                    }
                    if (options == 2) {
                        System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                "the last meeting was: 2021-09-06");
                        while (true) {
                            try {
                                System.out.println("Information -> To get the full range: First Date = 2017-10-23 " +
                                        "and Second Date = 2021-09-06\n");
                                String party = HelperFunctions.PartyInput();// gets the name of the fraction
                                System.out.println("\n");

                                System.out.println("First Date");
                                String date = HelperFunctions.dateInput(); // gets a string date input
                                System.out.println("Second Date");
                                String dateIncreByOne = HelperFunctions.dateInput(); //get a second date input
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //create a format for the date
                                Calendar c = Calendar.getInstance(); //create a new calender instance
                                c.setTime(sdf.parse(dateIncreByOne)); //parse the second date
                                c.add(Calendar.DATE, 1);  // add one day
                                dateIncreByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countFractionInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncreByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),//open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")),//open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")),// gets all the speaker from the agendas
                                            new Document().append("$match", new Document().append("Tagesordnungspunkte.speech.speaker.party", party)),// gets all the speaker from the certain fraction
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("amountSpeeches", new Document().append("$sum", 1.0))),//counts how often a certain speaker talked
                                            new Document().append("$sort", new Document().append("amountSpeeches", -1.0)));//sorts desc

                                    MongoCursor<Document> cursor = pCollection.aggregate(countFractionInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry

                                    while (cursor.hasNext()) {//go through every document
                                        Document document = cursor.next();
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id"));//get the document in the document
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the party: " + speaker.getParty()
                                                + " had: " + document.get("amountSpeeches"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;
                    }
                }
                break;
            case 2:
                System.out.println("0: Return to selection screen; " +
                        "\n1: Get average speech length of every Speaker; " +
                        "\n2: Get the average speech length of every fraction; " +
                        "\n3: Get the average speech length of every Party; ");
                Integer decisionAverageLength = HelperFunctions.integerInputs(1, 3); //get the Input to perform an action listed above
                if (decisionAverageLength == 1) {
                    System.out.println("0: Return to selection screen; " +
                            "\n1: Search for a specific date; " +
                            "\n2: Search in a range of date");
                    Integer options = HelperFunctions.integerInputs(1, 2); //get the Input to perform an action listed above
                    if (options == 1) {
                        while (true) {
                            try {
                                System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                        "the last meeting was: 2021-09-06\n");
                                String date = HelperFunctions.dateInput(); //get a date Input
                                String dateIncreByOne = date; //create a second String which has the same value as the input
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //create a format for the date
                                Calendar c = Calendar.getInstance(); //create a new Calender instance
                                c.setTime(sdf.parse(dateIncreByOne)); //parse the second date
                                c.add(Calendar.DATE, 1);  // add by one day
                                dateIncreByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countSpeechInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncreByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")), //open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")),// gets all the speaker from the agendas
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every speech of a speaker


                                    MongoCursor<Document> cursor = pCollection.aggregate(countSpeechInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry


                                    while (cursor.hasNext()) { //go through every document
                                        Document document = cursor.next();  // get the content of the next document and create a doc from it
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id")); //get the document in the document
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the Fraction: " + speaker.getFraction()
                                                + " had: " + document.get("length"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;

                    }
                    if(options == 2){
                        System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                "the last meeting was: 2021-09-06");
                        while (true) {
                            try {
                                System.out.println("Information -> To get the full range: First Date = 2017-10-23 " +
                                        "and Second Date = 2021-09-06\n");
                                System.out.println("First Date");
                                String date = HelperFunctions.dateInput(); //get a string input which will be the date
                                System.out.println("Second Date");
                                String dateIncrebyOne = HelperFunctions.dateInput(); // get another string input which will be another date
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // set a date format
                                Calendar c = Calendar.getInstance(); //create a new calender instance
                                c.setTime(sdf.parse(dateIncrebyOne)); // parse the date with the format from above and set it as the time for the calender
                                c.add(Calendar.DATE, 1);  // add one day to the date
                                dateIncrebyOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> countSpeechInquiry = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrebyOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),//open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")), // gets all the speaker from the agendas
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every speech of a speaker

                                    MongoCursor<Document> cursor = pCollection.aggregate(countSpeechInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry


                                    while (cursor.hasNext()) { //go through every document
                                        Document document = cursor.next();
                                        Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id")); //get the document in the document
                                        System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                                                + " with the ID: " + speaker.getID() + " from the Fraction: " + speaker.getFraction()
                                                + " had: " + document.get("length"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;
                    }
                }
                if (decisionAverageLength == 2) {
                    System.out.println("0: Return to selection screen; " +
                            "\n1: Search for a specific date; " +
                            "\n2: Search in a range of date");
                    Integer options = HelperFunctions.integerInputs(1, 2); //get an input to perfom an action listed above
                    if (options == 1) {
                        try {
                            System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                    "the last meeting was: 2021-09-06\n");
                            String date = HelperFunctions.dateInput(); // get a String input which will be the date
                            String dateIncreByOne = date; // set the value of the input as another date
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //create a date format
                            Calendar c = Calendar.getInstance(); //create a new calendar instance
                            c.setTime(sdf.parse(dateIncreByOne)); //set the formated date 2 for the calendar date
                            c.add(Calendar.DATE, 1);  // add day by one
                            dateIncreByOne = sdf.format(c.getTime());  // dt is now the new date
                            try {
                                List<Document> averageSpeechLength = Arrays.asList(
                                        new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncreByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")), //open the arraylist which contains all the agenda items
                                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")),  // gets all the speaker from the agendas
                                        new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker.fraction").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every fraction

                                MongoCursor<Document> cursor = pCollection.aggregate(averageSpeechLength).cursor();//aggregates through the collection with the instructions in countSpeechInquir
                                while (cursor.hasNext()) {
                                    Document document = cursor.next();
                                    System.out.println("The fraction: " + document.get("_id") + " have an average speech length of: " + document.get("length"));
                                }

                            } catch (ParseException e) {

                            }
                            break;
                        } catch (ParseException e) {
                            System.out.println("Invalid Input");
                        }
                        break;
                    }
                    if (options == 2) {
                        System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                "the last meeting was: 2021-09-06");
                        while (true) {
                            try {
                                System.out.println("Information -> To get the full range: First Date = 2017-10-23 " +
                                        "and Second Date = 2021-09-06\n");
                                System.out.println("First Date");
                                String date = HelperFunctions.dateInput(); //get a string input which will be the date
                                System.out.println("Second Date");
                                String dateIncrByOne = HelperFunctions.dateInput(); // get a second input which will be the date
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //create a date format
                                Calendar c = Calendar.getInstance();//create a new calendar instance
                                c.setTime(sdf.parse(dateIncrByOne));//set the formated date 2 for the calendar date
                                c.add(Calendar.DATE, 1);  // add day by one
                                dateIncrByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> averageSpeechLength = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")), //open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")), // gets all the speaker from the agendas
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker.fraction").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every fraction
                                    MongoCursor<Document> cursor = pCollection.aggregate(averageSpeechLength).cursor();//aggregates through the collection with the instructions in countSpeechInquir
                                    while (cursor.hasNext()) {
                                        Document document = cursor.next();
                                        System.out.println("The fraction: " + document.get("_id") + " have an average speech length of: " + document.get("length"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;
                    }
                }
                if(decisionAverageLength == 3){
                    System.out.println("0: Return to selection screen; " +
                            "\n1: Search for a specific date; " +
                            "\n2: Search in a range of date");
                    Integer options = HelperFunctions.integerInputs(1, 2); //get an input to perfom an action listed above
                    if (options == 1) {
                        try {
                            String date = HelperFunctions.dateInput();// get a String input which will be the date
                            String dateIncrByOne = date; // set the value of the input as another date
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //create a date format
                            Calendar c = Calendar.getInstance(); //create a new calendar instance
                            c.setTime(sdf.parse(dateIncrByOne)); //set the formated date 2 for the calendar date
                            c.add(Calendar.DATE, 1);  // add day by one
                            dateIncrByOne = sdf.format(c.getTime());  // dt is now the new date
                            try {
                                List<Document> averageSpeechLength = Arrays.asList(
                                        new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")),  //open the arraylist which contains all the agenda items
                                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")), // gets all the speaker from the agendas
                                        new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker.party").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every party

                                MongoCursor<Document> cursor = pCollection.aggregate(averageSpeechLength).cursor();//aggregates through the collection with the instructions in countSpeechInquir
                                while (cursor.hasNext()) {
                                    Document document = cursor.next();
                                    System.out.println("The party: " + document.get("_id") + " have an average speech length of: " + document.get("length"));
                                }

                            } catch (ParseException e) {

                            }
                            break;
                        } catch (ParseException e) {
                            System.out.println("Invalid Input");
                        }
                        break;
                    }
                    if (options == 2) {
                        System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                "the last meeting was: 2021-09-06");
                        while (true) {
                            try {
                                System.out.println("Information -> To get the full range: First Date = 2017-10-23 " +
                                        "and Second Date = 2021-09-06\n");
                                System.out.println("First Date");
                                String date = HelperFunctions.dateInput(); // get a String input which will be the date
                                System.out.println("Second Date");
                                String dateIncrByOne = HelperFunctions.dateInput(); // get a String input which will be the date
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //create a date format
                                Calendar c = Calendar.getInstance(); //create a new calendar instance
                                c.setTime(sdf.parse(dateIncrByOne)); //set the formated date 2 for the calendar date
                                c.add(Calendar.DATE, 1);   // add day by one
                                dateIncrByOne = sdf.format(c.getTime());  // dt is now the new date
                                try {
                                    List<Document> averageSpeechLength = Arrays.asList(
                                            new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")), //open the arraylist which contains all the agenda items
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")),//open the arraylist which contains all the speeches of one agenda
                                            new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")), // gets all the speaker from the agendas
                                            new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker.party").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every party
                                    MongoCursor<Document> cursor = pCollection.aggregate(averageSpeechLength).cursor();//aggregates through the collection with the instructions in countSpeechInquir
                                    while (cursor.hasNext()) {
                                        Document document = cursor.next();
                                        System.out.println("The party: " + document.get("_id") + " have an average speech length of: " + document.get("length"));
                                    }

                                } catch (ParseException e) {

                                }
                                break;
                            } catch (ParseException e) {
                                System.out.println("Invalid Input");
                            }
                        }
                        break;
                    }
                }
            case 3:
                System.out.println("0: Return to selection screen; " +
                        "\n1: Search for a specific date; " +
                        "\n2: Search in a range of date");
                Integer options = HelperFunctions.integerInputs(1, 2); //get an input to perfom an action listed above
                if (options == 1) {
                    while (true) {
                        try {
                            System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                                    "the last meeting was: 2021-09-06\n");
                            String date = HelperFunctions.dateInput(); // get a String input which will be the date
                            String dateIncrByOne = date; // set the value of the input as another date
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //create a date format
                            Calendar c = Calendar.getInstance(); //create a new calendar instance
                            c.setTime(sdf.parse(dateIncrByOne)); //set the formated date 2 for the calendar date
                            c.add(Calendar.DATE, 1);  // add day by one
                            dateIncrByOne = sdf.format(c.getTime());  // dt is now the new date
                            try {
                                List<Document> getDuration = Arrays.asList(
                                        new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrByOne.toString() + " 02:00:00.000+0200")))),//matches the documents and return only the document which are in range of the date
                                        new Document().append("$addFields", new Document().append("duration", new Document().append("$subtract", Arrays.asList("$endHour", "$startHour")))), //get the entry for start and end time in hour and sub them
                                        new Document().append("$addFields", new Document().append("durationMin", new Document().append("$abs", new Document().append("$subtract", Arrays.asList("$endMin", "$startMin"))))), //get the entry with start and end time in min and sub them
                                        new Document().append("$project", new Document().append("_id", 1.0).append("duration", 1.0).append("durationMin", 1.0)), //project them so it can load faster
                                        new Document().append("$sort", new Document().append("duration", -1.0).append("durationMin", -1.0))); //sort the hour desc and if tie then after the min.

                                MongoCursor<Document> cursor = pCollection.aggregate(getDuration).allowDiskUse(true).cursor(); // create a new Mongocursor and allowDiskUse (for unlimited memory usage)
                                while (cursor.hasNext()) {
                                    Document document = cursor.next();
                                    Protocol_MongoDB_Impl prot = new Protocol_MongoDB_Impl(document);
                                    System.out.println("The session with the nr: " + prot.getSitzungsnr() + " had a duration of: " + prot.getduractionHour() + " hours and " + prot.getduractionMin() + " min");
                                }

                            } catch (ParseException e) {

                            }
                            break;
                        } catch (ParseException e) {
                            System.out.println("Invalid Input");
                        }
                    }
                    break;
                }
                if(options == 2){
                    System.out.println("For this election Period: The first meeting was: 2017-10-23 and " +
                            "the last meeting was: 2021-09-06");
                    while (true) {
                        try {
                            System.out.println("Information -> To get the full range: First Date = 2017-10-23 " +
                                    "and Second Date = 2021-09-06\n");
                            System.out.println("First Date");
                            String date = HelperFunctions.dateInput(); // get a String input which will be the date
                            System.out.println("Second Date");
                            String dateIncrByOne = HelperFunctions.dateInput(); // get a String input which will be the date
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //create a date format
                            Calendar c = Calendar.getInstance(); //create a new calendar instance
                            c.setTime(sdf.parse(dateIncrByOne)); //set the formated date 2 for the calendar date
                            c.add(Calendar.DATE, 1);  // add day by one
                            dateIncrByOne = sdf.format(c.getTime());  // dt is now the new date
                            try {
                                List<Document> getDuration = Arrays.asList(
                                        new Document().append("$match", new Document().append("date", new Document().append("$gte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(date + " 02:00:00.000+0200")).append("$lte", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(dateIncrByOne.toString() + " 02:00:00.000+0200")))), //matches the documents and return only the docu
                                        new Document().append("$addFields", new Document().append("duration", new Document().append("$subtract", Arrays.asList("$endHour", "$startHour")))), //get the entry for start and end time in hour and sub them
                                        new Document().append("$addFields", new Document().append("durationMin", new Document().append("$abs", new Document().append("$subtract", Arrays.asList("$endMin", "$startMin"))))), //get the entry with start and end time in min and sub them
                                        new Document().append("$project", new Document().append("_id", 1.0).append("duration", 1.0).append("durationMin", 1.0)), //project them so it can load faster
                                        new Document().append("$sort", new Document().append("duration", -1.0).append("durationMin", -1.0))); //sort the hour desc and if tie then after the min.

                                MongoCursor<Document> cursor = pCollection.aggregate(getDuration).allowDiskUse(true).cursor(); // create a new Mongocursor and allowDiskUse (for unlimited memory usage)
                                while (cursor.hasNext()) {
                                    Document document = cursor.next();
                                    Protocol_MongoDB_Impl prot = new Protocol_MongoDB_Impl(document);
                                    System.out.println("The session with the nr: " + prot.getSitzungsnr() + " had a duration of: " + prot.getduractionHour() + " hours and " + prot.getduractionMin() + " min");
                                }

                            } catch (ParseException e) {

                            }
                            break;
                        } catch (ParseException e) {
                            System.out.println("Invalid Input");
                        }
                    }
                    break;

                }
                break;
            case 4:
                CLOSING = true;
                break;
            default:
                System.out.println("Wrong Input! Please enter a number between 1 and 7");
        }
    }

    /**
     * Shows the Task which can be performed with the Database
     * @param pCollection
     */
    public void TaskSheet1MongoQuery(MongoCollection pCollection, MongoCollection aCollection, MongoCollection sCollection){
        CLOSED = false;
        while (!CLOSED) {
            System.out.println("\nBelow you will see some actions which you can perform");
            System.out.println("1.Get a sorted list of all the Speakers");
            System.out.println("2.Search for a certain Speaker or search for party");
            System.out.println("3.Get how many times a MP lead chaired a session.");
            System.out.println("4.Get the average speech length of all speeches");
            System.out.println("5.Get the average speech length of every speaker");
            System.out.println("6.Get the average speech length of every fraction");
            System.out.println("7.Return to menu");
            int CHOICE = HelperFunctions.integerInputs(1,7);
            mainMenuTaskSheet1(CHOICE, pCollection, aCollection, sCollection); // starts one of the task from above
        }
    }

    /**
     * Main Menu for TaskSheet 1.
     * @param CHOICE
     * @param pCollection
     */
    public void mainMenuTaskSheet1(int CHOICE, MongoCollection pCollection, MongoCollection aCollection, MongoCollection sCollection){
        switch (CHOICE) {
            case 1:
                Document query = new Document(); // create a new document

                Document sort = new Document(); // create a new document

                sort.append("firstname", 1.0); //append firstname sorted asc
                sort.append("lastname", 1.0); //append lastname sorted asc

               MongoCursor<Document> cursor = aCollection.find(query).sort(sort).cursor(); // use find all and sort according to sort

               while(cursor.hasNext()){ // while we have documents
                   Document document = cursor.next();
                   Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl(document); //get the document in the document and parse it back as speaker
                   System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                           + " with the ID: " + document.get("id") + " from the Fraction: " + speaker.getFraction()
                           + " and from the party: " + speaker.getParty());
               }
               break;
            case 2:
                String filter = HelperFunctions.Input(); //get a filter string
                List<Document> nameFilter = Arrays.asList(
                        new Document().append("$sort", new Document().append("firstname", 1.0).append("lastname", 1.0)), //sort all speaker
                        new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$firstname", " ", "$lastname", " ", "$party")))), //create a new field named full name for the filter
                        new Document().append("$match", new Document().append("fullname", new Document().append("$regex", filter)))); // search all names with regex and return all matches.

                MongoCursor<Document> filterName = aCollection.aggregate(nameFilter).cursor(); // use the nameFilter above to get documents
                while(filterName.hasNext()){ // while we have documents
                    Document document = filterName.next();
                    Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl(document); //get the document in the document and parse it back as speaker
                    System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                            + " with the ID: " + document.get("id") + " from the Fraction: " + speaker.getFraction()
                            + " and from the party: " + speaker.getParty());
                }
                break;
            case 3:
                List<Document> countMP = Arrays.asList(
                        new Document().append("$group", new Document().append("_id", "$mp").append("count", new Document().append("$sum", 1.0)))); //get every Mp and count how often they occur

                MongoCursor<Document> mp = pCollection.aggregate(countMP).cursor(); //get the documents with the countMP
                while (mp.hasNext()){ // while we have documents
                    Document document = mp.next();
                    System.out.println("The MP: " + document.get("_id") + " has lead: " + document.get("count") + " sessions");
                }
                break;
            case 4:
                List<Document> averageOfAll = Arrays.asList(
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")), //open all arraylist with agendas
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open all arraylist with agenda speeches.
                        new Document().append("$group", new Document().append("_id", "Tagesordnungspunkte").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average of all speeches

                MongoCursor<Document> average = pCollection.aggregate(averageOfAll).cursor();
                while (average.hasNext()){ // while we have documents
                    Document document = average.next();
                    System.out.println("The average speech length is " + document.get("length") + " chars");
                }
                break;
            case 5:
                List<Document> countSpeechInquiry = Arrays.asList(
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")), //open the arraylist which contains all the agenda items
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")),// gets all the speaker from the agendas
                        new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every speech of a speaker


                MongoCursor<Document> averageSpeaker = pCollection.aggregate(countSpeechInquiry).cursor();//aggregates through the collection with the instructions in countSpeechInquiry


                while (averageSpeaker.hasNext()) { //go through every document
                    Document document = averageSpeaker.next();  // get the content of the next document and create a doc from it
                    Speaker_MongoDB_Impl speaker = new Speaker_MongoDB_Impl((Document) document.get("_id")); //get the document in the document
                    System.out.println("The speaker: " + speaker.getFirstName() + " " + speaker.getLastName()
                            + " with the ID: " + speaker.getID() + " from the Fraction: " + speaker.getFraction()
                            + " had: " + document.get("length"));
                }

            case 6:
                List<Document> averageSpeechLength = Arrays.asList(
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte")), //open the arraylist which contains all the agenda items
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech")), //open the arraylist which contains all the speeches of one agenda
                        new Document().append("$unwind", new Document().append("path", "$Tagesordnungspunkte.speech.speaker")), // gets all the speaker from the agendas
                        new Document().append("$group", new Document().append("_id", "$Tagesordnungspunkte.speech.speaker.fraction").append("length", new Document().append("$avg", new Document().append("$strLenCP", "$Tagesordnungspunkte.speech.content"))))); //calculate the average speech length of every fraction
                MongoCursor<Document> averageFraction = pCollection.aggregate(averageSpeechLength).cursor();//aggregates through the collection with the instructions in countSpeechInquir
                while (averageFraction.hasNext()) {
                    Document document = averageFraction.next();
                    System.out.println("The fraction: " + document.get("_id") + " have an average speech length of: " + document.get("length"));
                }
                break;
            case 7:
                CLOSED = true;
                break;
            default:
                System.out.println("Wrong Input, Please enter a number between 1 and 9");

        }
    }

}

