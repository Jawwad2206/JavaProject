package ui;

import data.Protocol;
import data.Speaker;
import data.impl.Protocol_File_Impl;
import funcations.HelperFunctions;
import funcations.ProtocolFromXML;
import funcations.StammdatenReader;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the UserInterface, the main will start this class and in this class all the Tasks will be answered
 */
public class UserInterface {

    boolean CLOSE; // Default Value is fault (used) for while Loops
    boolean CLOSING; // Default Value is fault (uses) for while Loops
    boolean CLOSED; // Default Value is fault (uses) for while Loops

    /**
     * Starts the defferant parts of the Programm.
     * @param spath
     * @param xpath
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void main3(String spath, String xpath) throws ParserConfigurationException, IOException, SAXException {
        UserInterface menu = new UserInterface(); // we create a new Object of the Class Userinterface and name it menu.
        menu.printHeader();
        menu.MainMenu(spath, xpath); // Starts the MainMenu.

    }

    /**
     * Greeting Header
     */
    private void printHeader() {
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
     * @param path
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArrayList<ArrayList> loadXMLFIle(String path) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("The Data is being checked, please wait a minute");
        System.out.println("[1/3]");
        ArrayList<ArrayList> informationen; // Creates a new ArrayList which will contain ArrayList.
        System.out.println("[2/3]");
        informationen = ProtocolFromXML.ProtocolCreater(path); // All Parsed XML and there respective Information will be added in this ArrayList created above.

        System.out.println("[3/3]");

        return informationen; //return this ArrayList for other methods.
    }

    /**
     *Compares the Speakers of the XML files and from the Stammdatenblatt and corrects there name or adds there name if
     * it is missing
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
        for(int e = 0; e < Speaker.size(); e++ ) {
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
     * @param StammPath
     * @param XMLPath
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void MainMenu(String StammPath, String XMLPath) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<Speaker> Abgeordnete = loadStammDaten(StammPath);// Creates an ArrayList with all the Speaker from the Stammdatenblatt
        System.out.println("StammDatenBlatt download was complete!");
        ArrayList<ArrayList> XMLInformatoíon = loadXMLFIle(XMLPath);//Creates an ArrayList of ArrayList with all the Information
        ArrayList<Speaker> allspeaker = XMLInformatoíon.get(1); // ArrayList with all the speaker -> 783
        ArrayList<Protocol> allProtocols = XMLInformatoíon.get(0); //ArrayList with all the protocols
        ArrayList<Speaker> speaker = correctSpeaker(allspeaker, Abgeordnete); //corrects all Speakers
        ArrayList<String> fraction = getEveryFraction(speaker);

        System.out.println("All XML Files were sucessfully found!");
        System.out.println("|----------Welcome to the Plenarprotocol App----------|");
        CLOSE = false;
        while (!CLOSE) {
            System.out.println("\nBelow you will see some Actions this Programm can perform");
            System.out.println("Please press the one of the following Numbers to perfrom an action: ");
            System.out.println("1. Task2f\n");
            System.out.println("2. Task3\n");
            System.out.println("3. End the Programm\n");
            int CHOICE = HelperFunctions.getInput();
            doMenu(CHOICE, speaker, allProtocols, fraction); //Starts the Menu with the the given parameters
        }
    }


    /**
     * Through the input entered from the getInput one of the following task will be performed.
     * @param CHOICE
     * @param sProtocol
     * @param pProtocol
     * @param fraction
     */
    private void doMenu(int CHOICE, ArrayList<Speaker> sProtocol, ArrayList<Protocol> pProtocol, ArrayList<String> fraction) {
        switch (CHOICE) {
            case 1:
                Task2Menu(sProtocol, pProtocol); //starts the Menu for the second task
                break;
            case 2:
                Task3Menu(sProtocol, pProtocol, fraction);//starts the Menu for the third task.
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
     * Sorts all the Speakers.
     * @param sSpeaker
     */
    private void allSortedSpeaker(ArrayList<Speaker> sSpeaker) {
        List<String> sortedSpeaker = new ArrayList<>();
        for (int e = 0; e < sSpeaker.size(); e++) { //we iterate through all the Speakers and get there name and party

            sortedSpeaker.add(sSpeaker.get(e).getFirstName() + " " + sSpeaker.get(e).getLastName() + " " + sSpeaker.get(e).getFraction() + " " + sSpeaker.get(e).getID() + " " + sSpeaker.get(e).getParty());

        }
        sortedSpeaker = sortedSpeaker.stream().sorted().collect(Collectors.<String>toList()); // with this expression we sort the speakers alphabetically.
        for (int u = 0; u < sortedSpeaker.size(); u++) {
            System.out.println(sortedSpeaker.get(u));
        }
    }

    /**
     * gets all the Speakers in a specific session in there respective agenda items where they spoke.
     * @param sProtocol
     * @param Sitzungsnummer
     */
    private void sortedSpeakerAgenda(ArrayList<Protocol> sProtocol, Integer Sitzungsnummer) {

        Protocol_File_Impl protocol = (Protocol_File_Impl) sProtocol.get(Sitzungsnummer); // we create one new Protocol Object according the session number

        for (int i = 0; i < protocol.getTagesOrdnungsPunkte().size(); i++) { //we iterate through all the agenda items

            System.out.println("\n" + protocol.getTagesOrdnungsPunkte().get(i).getTitle() + "\n"); //print the title of the agenda
            for (int j = 0; j < protocol.getTagesOrdnungsPunkte().get(i).getSpeeches().size(); j++) { // we iterate through all the speeches from one agenda
                for (int k = 0; k < protocol.getTagesOrdnungsPunkte().get(i).getSpeeches().get(j).getSpeaker().size(); k++) {//we iterate through through all speakers from a speech

                    String vorname = protocol.getTagesOrdnungsPunkte().get(i).getSpeeches().get(j).getSpeaker().get(k).getFirstName();//get the first name
                    String nachname = protocol.getTagesOrdnungsPunkte().get(i).getSpeeches().get(j).getSpeaker().get(k).getLastName();//get the last name
                    String party = protocol.getTagesOrdnungsPunkte().get(i).getSpeeches().get(j).getSpeaker().get(k).getParty();//get the party


                    System.out.println(vorname + " " + nachname + " " + party);
                }
            }
        }
    }

    /**
     * Filter for the alphabetically sorted Speakers.
     * @param sSpeaker
     * @param name
     */
    public void sortedSpeakerFilter(ArrayList<Speaker> sSpeaker, String name){
        List<String> sortedSpeaker = new ArrayList<>();
        for (int e = 0; e < sSpeaker.size(); e++) {//we iterate through all the Speakers and get there name and party

            sortedSpeaker.add(sSpeaker.get(e).getFirstName() + " " + sSpeaker.get(e).getLastName() + " " + sSpeaker.get(e).getFraction());

        }
        sortedSpeaker = sortedSpeaker.stream().sorted().collect(Collectors.<String>toList()); //with this expression we sort the speakers alphabetically.
        for (int u = 0; u < sortedSpeaker.size(); u++) {
            if (sortedSpeaker.get(u).contains(name.substring(0, name.length()))) { //we check if the name contain the substring(input) of the user.

                System.out.println(sortedSpeaker.get(u)); //prints everyone who contains this substring.
            }
        }
    }


    /**
     * Main Menu for the second task question f.
     * @param sProtocol
     * @param pProtocol
     */
    public void Task2Menu(ArrayList<Speaker> sProtocol, ArrayList<Protocol> pProtocol){
        CLOSING = false;
        while(!CLOSING) {
            System.out.println("\nBelow you will see some actions which you can perform");
            System.out.println("1.Get general Information of all Protocols (Sitzungsnr, Datum, Leiter, etc)");
            System.out.println("2.Get information of a specific Protocol(Sitzungsnr, Datum, Leiter, etc");
            System.out.println("3.Get a sorted list of all the Speakers");
            System.out.println("4.Search for a certain Speaker or search for Fraction/Party members");
            System.out.println("5.List of all speakers per agenda item of a respective session.");
            System.out.println("6.Get how many times a MP lead chaired a seasson");
            System.out.println("7.Return to menu");
            int CHOICE = HelperFunctions.InputTask2();
            mainMenuTask2(CHOICE, sProtocol, pProtocol); // starts one of the task from above
        }
    }

    /**
     * Main Menu for Task 2f.
     * @param CHOICE
     * @param sProtocol
     * @param pProtocol
     */
    private void mainMenuTask2(int CHOICE, ArrayList<Speaker> sProtocol, ArrayList<Protocol> pProtocol){
        switch (CHOICE){
            case 1:
                protocolinformation(pProtocol); //gets all information of every protocol
                break;
            case 2:
                int sNR = HelperFunctions.integerInputs(1, 239);
                singleprotocolinformation(pProtocol, (sNR-1)); //gets the information of a specific protocol
                break;
            case 3:
                allSortedSpeaker(sProtocol); //gets a sorted list of all speakers
                break;
            case 4:
                String search = HelperFunctions.Input();
                sortedSpeakerFilter(sProtocol, search); //filter for speaker
                break;
            case 5:
                int srNr = HelperFunctions.integerInputs(1, 239);
                sortedSpeakerAgenda(pProtocol, (srNr-1)); //all speakers of a protocol in there agenda
                break;
            case 6:
                MpSeassonCounter(pProtocol); //How often a MP has lead a session
                break;
            case 7:
                CLOSING = true;
                break;
            default:
                System.out.println("Wrong Input, Please enter a number between 1 and 7");
        }
    }

    private void Task3Menu(ArrayList<Speaker> sProtocol, ArrayList<Protocol> pProtocol, ArrayList<String> fraction) {
        CLOSED = false;
        while(!CLOSED) {
            System.out.println("\nBelow you will see some actions which you can perform");
            System.out.println("1.Get the average speech length of all speeches");
            System.out.println("2.Get the average speech length of every speaker");
            System.out.println("3.Get the average speech length of every fraction");
            System.out.println("4.Find the longest and shortest speech");
            System.out.println("5.Get the Speeches with the most comments with a limit to an entered quantity");
            System.out.println("6.Get the amount of Comments every Speaker received and from which party");
            System.out.println("7.Get the amount of comments every Fraction received");
            System.out.println("8.Get the average amount of comments each Fraction received from the other fraction");
            System.out.println("9.Return to menu");

            int CHOICE = HelperFunctions.InputTask3();
            mainMenuTask3(CHOICE, sProtocol, pProtocol, fraction); // starts one of the task from above
        }
    }


    private void mainMenuTask3(int CHOICE, ArrayList<Speaker> sProtocol, ArrayList<Protocol> pProtocol, ArrayList<String> fraction){
        switch (CHOICE){
            case 1:
                getSpeechAverageLength(pProtocol); // gets the average Speech length of all speeches
                break;
            case 2:
                getSpeakerAverageLength(pProtocol, sProtocol); // gets the average Speech length of every Speaker
                break;
            case 3:
                getFractionAverageLength(pProtocol, fraction); // get the average Speech length of every Fraction
                break;
            case 4:
                getLongestSpeech(pProtocol); // get the longest speech
                System.out.println("\n");
                getshortestSpeech(pProtocol); // get the shortest speech
                break;
            case 5:
                System.out.println("!There are 25164 speeches so theoretically you can get every comment ranked");
                System.out.println("!Warning if you enter 25164 it will take some time!");
                int limit = HelperFunctions.integerInputs(1, 25164); // gets an input for the function

                topCommentsList(pProtocol, limit); // get the top comments
                break;
            case 6:
                commentsGivenSpeaker(pProtocol, sProtocol); // get the amount of comments every Speaker received.
                break;
            case 7:
                commentsGivenFraction(pProtocol,fraction); // gets the amount of comments every fraction received.
                break;
            case 8:
                averageCommentsFraction(pProtocol,fraction);
                break;
            case 9:
                CLOSED = true;
                break;
            default:
                System.out.println("Wrong Input, Please enter a number between 1 and 7");

        }

    }

    /**
     * This method Counts how often a MP has lead a session.
     * @param pProtocol
     */
    public static void MpSeassonCounter(ArrayList<Protocol> pProtocol) {
        ArrayList<Object> pMP = new ArrayList<>();
        Set<String> nMP = new HashSet<>(); // Hashset for no dupicates
        for (int i = 0; i < pProtocol.size(); i++) { // we go throug every protocol and get the MP
            String name = pProtocol.get(i).getLeader(); // we get the name of the MP
            nMP.add(name); // we add the name to the Hashset with the purpose to eliminate every duplicate.

        }
        for(Object e : nMP){ // we put every Object(MP) in an ArrayList.

            pMP.add(e);
        }

        for(int j = 0; j < pMP.size(); j++){ // for every MP we go through all Protocols and count how often they lead a session.

            ArrayList counter = new ArrayList(); //

            for(int k = 0; k < pProtocol.size(); k++){
                if(pMP.get(j).equals(pProtocol.get(k).getLeader())){
                    counter.add(pProtocol.get(k).getLeader());
                }
            }
            System.out.println(pMP.get(j) + " lead " + counter.size() + " Seassons");
        }
    }

    /**
     * This Method returns a list with all the fractions in the Parliament
     * @param sProtocol
     * @return
     */
    public static ArrayList<String> getEveryFraction(ArrayList<Speaker> sProtocol){
        ArrayList<String> fraction = new ArrayList<>(); // will contain all the fractions present in the parliament
        ArrayList<Speaker> sSpeaker = sProtocol; // ArrayList with all the Speaker
        Set<String> deleteDuplicates = new HashSet<>(); // Hashset for deleting all duplicates
        for(int i = 0; i < sSpeaker.size(); i++){ // ArrayList with all the Speakers
            String sfraction = sSpeaker.get(i).getFraction(); // get the Fraction of every Speaker
            deleteDuplicates.add(sfraction); // add the fraction to the Hashset

        }
        for(String e : deleteDuplicates){ //converting the Hashset into an ArrayList
            fraction.add(e);
        }

        return fraction; //return the ArrayList with all the fractions
    }

    /**
     * Gets general Information of every protocol parsed.
     * @param pProtocol
     */
    public static void protocolinformation(ArrayList<Protocol> pProtocol) {
        for (int i = 0; i < pProtocol.size(); i++) {
            System.out.println("ProtokollNr: " + (i + 1));
            System.out.println("The Meetings was on the: " + pProtocol.get(i).getDate());
            System.out.println("The election Period is: " + pProtocol.get(i).getPeriode());
            System.out.println("The Season Leader was: " + pProtocol.get(i).getLeader());
            System.out.println("The Season Started: " + pProtocol.get(i).getStart() + " and ended: " + pProtocol.get(i).getEnd());
            System.out.println("The seassion number is: " + pProtocol.get(i).getSitzungsnr());
            System.out.println("\n");
        }
    }

    /**
     *
     * gets the general information of a specific protocol.
     * @param pProtocol
     * @param i
     */
    public static void singleprotocolinformation(ArrayList<Protocol> pProtocol, int i) {
        System.out.println("ProtokollNr: " + (i + 1));
        System.out.println("The Meetings was on the: " + pProtocol.get(i).getDate());
        System.out.println("The election Period is: " + pProtocol.get(i).getPeriode());
        System.out.println("The Season Leader was: " + pProtocol.get(i).getLeader());
        System.out.println("The Season Started: " + pProtocol.get(i).getStart() + " and ended: " + pProtocol.get(i).getEnd());
        System.out.println("The seassion number is: " + pProtocol.get(i).getSitzungsnr());
        System.out.println("\n");
    }

    /**
     * This method calculates the average speech length of all speeches.
     * @param pProtocol
     */
    private static void getSpeechAverageLength(ArrayList<Protocol> pProtocol){
        int sLength = 0; // will contain the total amount of letters in all Speeches spoken.
        int sCount = 0; // will contain the amount of speeches spoken in the parliament.
        int sSpeaker = 0; // will contain the amount of letters of all Speakers full name + party/fraction.
        for(int i = 0; i < pProtocol.size(); i++){ // iterate through every protocol
            for(int j = 0; j < pProtocol.get(i).getTagesOrdnungsPunkte().size(); j++){ // through every agenda
                for(int k = 0; k < pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().size(); k++){ //through every speech
                    int singleSpeechLenght = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getText().length(); // length of a single speech
                    sLength = sLength + singleSpeechLenght; // add the amount of chars counted to the previous amount
                    sCount += 1; // counter for the amount of speeches
                    for(int l = 0; l < pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().size(); l++){
                        String firstName = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(l).getFirstName();
                        String lastName = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(l).getLastName();
                        String Speaker = (lastName + firstName);
                        sSpeaker += Speaker.length();

                    }
                }
            }
        }
        System.out.println("The average speech length is " + ((sLength - sSpeaker)/sCount) + " chars"); // result
    }

    /**
     *
     * @param pProtocol
     * @param sProtocol
     */
    public static void getSpeakerAverageLength(ArrayList<Protocol> pProtocol, ArrayList<Speaker> sProtocol){

        for(int m = 0; m < sProtocol.size(); m++) {
            int sLength = 0; // will contain the total amount of letters in all Speeches spoken of the Speaker.
            int sCount = 0; // will contain the amount of speeches spoken in the parliament.
            int sSpeaker = 0; // will contain the amount of letters of all Speakers full name + party/fraction.
            String speakerId = sProtocol.get(m).getID();
            String speakerName = sProtocol.get(m).getFirstName();
            String speakerlName = sProtocol.get(m).getLastName();
            String sName = speakerlName + speakerName;
            for (int i = 0; i < pProtocol.size(); i++) { // iterate through every protocol
                for (int j = 0; j < pProtocol.get(i).getTagesOrdnungsPunkte().size(); j++) { // through every agenda
                    for (int k = 0; k < pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().size(); k++) { //through every speech
                        for (int l = 0; l < pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().size(); l++) { //through every Speaker
                            if(pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(l).getID().equals(speakerId)){ // Check if the SpeakerId is the same as the SpeakerId of the Speaker of the Speech
                                if(pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(l).getFirstName().equals(speakerName)) { //Check if the firstname is the same
                                   if(pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(l).getLastName().equals(speakerlName)){ // check if the last name is the same
                                       int singleSpeechLenght = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getText().length(); //if the above if-Statements are all true then add the speech length
                                       sSpeaker += sName.length();
                                       sLength = sLength + singleSpeechLenght; //Add the length of other speeches of the Speaker as well.
                                       sCount += 1; // counts the amount of speeches the Speaker held in the election Period
                                   }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("The average speech length of: " + speakerName + " " + speakerlName + " is "  + ((sLength - sSpeaker)/sCount) + " chars"); // result
        }

    }

    /**
     * This method calculates the average speech length of every fraction
     * @param pProtocol
     * @param fraction
     */
    public static void getFractionAverageLength(ArrayList<Protocol> pProtocol, ArrayList<String> fraction){
        for(int i = 0; i < fraction.size(); i++){
            String pFraction = fraction.get(i);
            int sLength = 0; // will contain the total amount of letters in all Speeches spoken of the fraction.
            int sCount = 0; // will contain the amount of speeches spoken of the fraction in the parliament.
            int sSpeaker = 0; // will contain the amount of letters of all Speakers full name + party/fraction.
            for(int j = 0; j < pProtocol.size(); j++){ // iterate through every protocol
                for(int k = 0; k < pProtocol.get(j).getTagesOrdnungsPunkte().size(); k++){ // through every agenda
                    for(int l = 0; l < pProtocol.get(j).getTagesOrdnungsPunkte().get(k).getSpeeches().size(); l++){ //through every speech
                        for(int m = 0; m < pProtocol.get(j).getTagesOrdnungsPunkte().get(k).getSpeeches().get(l).getSpeaker().size(); m++){ // through every speaker
                            if(pProtocol.get(j).getTagesOrdnungsPunkte().get(k).getSpeeches().get(l).getSpeaker().get(m).getFraction().equals(pFraction)){ //compare if the fraction of the speaker is the same as the fraction we are searching for
                                int singleSpeechLenght = pProtocol.get(j).getTagesOrdnungsPunkte().get(k).getSpeeches().get(l).getText().length(); //if the above if-Statements are all true then add the speech length
                                sLength = sLength + singleSpeechLenght; //Add the length of other speeches of the Speaker as well.
                                sCount += 1; // counts the amount of speeches the Speaker held in the election Period
                            }
                        }
                    }
                }
            }
            System.out.println("The average speech length of: " + pFraction + " is " + (sLength/sCount) + " chars");
        }
    }

    /**
     * This method get the longest Speech.
     * @param pProtocol
     */
    public static void getLongestSpeech(ArrayList<Protocol> pProtocol) {
        int sLength = 0; // will contain the total amount of letters in all Speeches spoken of the Speaker.
        String sName = ""; // will contain the first name of the speaker
        String lName = ""; // will contain the last name of the speaker
        String fraction = ""; // will contain the fraction of the speaker
        String date = ""; // will contain the date of the speech
        String srNR = ""; // will contain the session nr of the speech
        String TOP = ""; // will contain the Top of the speech.

        for (int i = 0; i < pProtocol.size(); i++) { // through every protocol
            for (int j = 0; j < pProtocol.get(i).getTagesOrdnungsPunkte().size(); j++) { //through every agenda
                for (int k = 0; k < pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().size(); k++) { //through every speech of an agenda
                    if (pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getText().length() > sLength) { // we check if the previous declared longest speech is being toped
                        sLength = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getText().length(); //set the new High score of the longest speech
                        sName = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(0).getFirstName(); // get the first name of the speaker
                        lName = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(0).getLastName(); // get the last name of the speaker
                        date = pProtocol.get(i).getDate();  // date of the speech
                        srNR = pProtocol.get(i).getSitzungsnr(); // session nr of the speech
                        TOP = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getTitle(); // agenda title of the speech
                        if (pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(0).getParty().equals("")) { // if no fraction mentioned show the role
                            fraction = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getSpeaker().get(0).getRole();
                        }

                    }
                }
            }
        }

        System.out.println("The longest speech is held by: " + sName + " " + lName + " from/as " + fraction + " " + date + " in the sessionNr: " + srNR + " for the " + TOP);
        System.out.println("She spoke: " + sLength + " words");
    }


    /**
     * This Method get the SPeaker with the shortest Speech.
     * @param pProtocol
     */
    public static void getshortestSpeech(ArrayList<Protocol> pProtocol) {
        int sLength = 2000; // will contain the total amount of letters in all Speeches spoken of the Speaker.
        String sName = ""; // will contain the first name of the speaker
        String lName = ""; // will contain the last name of the speaker
        String fraction = ""; // will contain the fraction of the speaker
        String date = ""; // will contain the date of the speech
        String srNR = ""; // will contain the session nr of the speech
        String TOP = ""; // will contain the Top of the speech.
        int a = 0;
        int b = 0;
        int c = 0;

        for (int i = 0; i < pProtocol.size(); i++) { //go through every protocol
            for (int j = 0; j < pProtocol.get(i).getTagesOrdnungsPunkte().size(); j++) { //through every agenda
                for (int k = 0; k < pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().size(); k++) { // through every speech in the agenda
                    if (pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getText().length() < sLength) { // we check if the previous declared longest speech is being toped
                        sLength = pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(k).getText().length(); //set the new low score
                        a = i; // get the protocol Nr of the speech
                        b = j; // the agenda of the speech
                        c = k; // the position of the speech in the agenda (+1) for correct position because for breaks before the actual position
                    }

                }
            }
        }

        sName = pProtocol.get(a).getTagesOrdnungsPunkte().get(b).getSpeeches().get(c+1).getSpeaker().get(0).getFirstName(); // get the first name of the speaker
        lName = pProtocol.get(a).getTagesOrdnungsPunkte().get(b).getSpeeches().get(c+1).getSpeaker().get(0).getLastName();// get the last name of the speaker
        fraction = pProtocol.get(a).getTagesOrdnungsPunkte().get(b).getSpeeches().get(c+1).getSpeaker().get(0).getFraction(); // get the fraction of the speaker
        if(fraction == "fraktionslos"){
            fraction = pProtocol.get(a).getTagesOrdnungsPunkte().get(b).getSpeeches().get(c+1).getSpeaker().get(0).getRole(); // if no fraction mentioned show the role
        }
        date = pProtocol.get(a).getDate(); // date of the speech
        srNR = pProtocol.get(a).getSitzungsnr(); // session nr of the speech
        TOP = pProtocol.get(a).getTagesOrdnungsPunkte().get(b).getTitle(); //agenda title of the speech

        int subNameLength = (sName + lName).length(); // correction term for the length of the speech

        System.out.println("The shortest speech is held by: " + sName + " " + lName + " from/as " + fraction + " " + date + " in the sessionNr: " + srNR + " for the " + TOP);
        System.out.println("He spoke: " + (sLength - subNameLength) + " words");

    }

    /**
     * This method returns the Top Comments with limit.
     * @param pProtocol
     * @param limit
     */
    public static void topCommentsList(ArrayList<Protocol> pProtocol, int limit) {
        ArrayList<Integer> topComments = new ArrayList<>(); // ArrayList which will contain every comments given of a Speech
        ArrayList<String> Date = new ArrayList<>(); // ArrayList with all the dates of the speech
        ArrayList<String> pTOP = new ArrayList<>(); // ArrayList with the agenda where the comments were given
        ArrayList<String> SrNr = new ArrayList<>(); // ArrayList with the session nr of the comments
        int counter = 0;


        for (int i = 0; i < pProtocol.size(); i++) { //we go through every comment
            for (int j = 0; j < pProtocol.get(i).getTagesOrdnungsPunkte().size(); j++) { // we go throug every agenda
                for (int n = 0; n < pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().size(); n++){ //go through every speech
                    topComments.add(pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getSpeeches().get(n).getComments().size()); // the amount of comments given in the speech
                    Date.add(pProtocol.get(i).getDate()); // date of the commens
                    pTOP.add(pProtocol.get(i).getTagesOrdnungsPunkte().get(j).getTitle()); //agenda of the speech
                    SrNr.add(pProtocol.get(i).getSitzungsnr()); // session nr of the speech.


                }
            }
        }

        while (counter < limit) { // to return the top list
            Integer amountComments = 0;
            String cDate = "";
            String cTop = "";
            String cSrNR = "";

            counter += 1;

            amountComments = Collections.max(topComments); // we get the max value = most comment
            cDate = Date.get(topComments.indexOf(Collections.max(topComments))); // the ArrayList are all symmetrical, so the position 1 of the others
            cTop = pTOP.get(topComments.indexOf(Collections.max(topComments))); // are the position of the comment, date and session nr.
            cSrNR = SrNr.get(topComments.indexOf(Collections.max(topComments)));

            System.out.println("Top " + counter + ". At the sessionnr: " + cSrNR + " on the " + cDate + " at the agenda " + cTop + " were: " + amountComments + " comments given");

            topComments.remove(topComments.indexOf(Collections.max(topComments))); // after getting ge max, we need to remove it, so we can get the second.., third..., most comment.
            Date.remove(topComments.indexOf(Collections.max(topComments))); // remove the date of the given max
            pTOP.remove(topComments.indexOf(Collections.max(topComments))); // remove the agenda of the given max
            SrNr.remove(topComments.indexOf(Collections.max(topComments))); // remove the SrNr of the given max

        }
    }

    /**
     * This Method return the amount of Comments every Speaker recived and from which fractions there comments were
     * @param pProtocol
     * @param sProtocol
     *
     */
    public static void commentsGivenSpeaker(ArrayList<Protocol> pProtocol, ArrayList<Speaker> sProtocol) {
        ArrayList<Speaker> sSpeaker = sProtocol; // ArrayList with all thr Speaker
        ArrayList<Protocol> protocols = pProtocol; // ArrayList with all the protocols

        for (int i = 0; i < sSpeaker.size(); i++) { //We iterate through every Speaker
            Integer amountComments = 0; // Init every amount is 0
            Integer CDUCSU = 0;
            Integer FDP = 0;
            Integer Bündnis = 0;
            Integer Afd = 0;
            Integer SPD = 0;
            Integer Linke = 0;
            Integer Beifall = 0;
            for (int j = 0; j < protocols.size(); j++) { // we go through every Protocol
                for (int m = 0; m < protocols.get(j).getTagesOrdnungsPunkte().size(); m++) { // through every agenda
                    for (int n = 0; n < protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().size(); n++) { // through every speech
                        if (sSpeaker.get(i).getID().equals(protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getSpeaker().get(0).getID())) { // compare the Speaker Id
                            amountComments += protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().size();
                            for (int o = 0; o < protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().size(); o++) {
                                if(protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("CDU/CSU")){ // if the comment contain certain key words then increase the counter
                                    CDUCSU += 1;
                                }
                                if(protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("FDP")){
                                    FDP += 1;
                                }
                                if(protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("DIE LINKE")){
                                    Linke += 1;
                                }
                                if(protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("SPD")){
                                    SPD += 1;
                                }
                                if(protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("BÜNDNIS 90/DIE GRÜNEN")){
                                    Bündnis += 1;
                                }
                                if(protocols.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("AfD")){
                                    Afd += 1;
                                }

                            }
                        }
                    }
                }
            }
            Beifall = (amountComments - CDUCSU - SPD - Afd - Bündnis - Linke - FDP);
            System.out.println("The Speaker: " + sSpeaker.get(i).getFirstName() + " " + sSpeaker.get(i).getLastName() + " recived: " + amountComments + " comments");
            System.out.println("From " + amountComments + " comments were from -> " + " CDU/CSU: " + CDUCSU + " SPD: " + SPD + " AfD: " + Afd + " BÜNDNIS 90/DIE GRÜNEN: " + Bündnis + " DIE LINKE: " + Linke + " FDP: " + FDP + " Beifall: " + Beifall);
            System.out.println("\n");
        }
    }

    /**
     * This Method gets the amount of comments every fraction received and how many where from which fraction.
     * @param pProtocol
     * @param fraction
     */
    public static void commentsGivenFraction(ArrayList<Protocol> pProtocol, ArrayList<String> fraction){
        for (int i = 0; i < fraction.size(); i++) { //We iterate through every fraction
            Integer amountComments = 0; // Init every amount is 0
            Integer CDUCSU = 0;
            Integer FDP = 0;
            Integer Bündnis = 0;
            Integer Afd = 0;
            Integer SPD = 0;
            Integer Linke = 0;
            Integer Beifall = 0;
            for (int j = 0; j < pProtocol.size(); j++) { // we go through every Protocol
                for (int m = 0; m < pProtocol.get(j).getTagesOrdnungsPunkte().size(); m++) { // through every agenda
                    for (int n = 0; n < pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().size(); n++) { // through every speech
                        if (fraction.get(i).equals(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getSpeaker().get(0).getFraction())) { // compare the fraction
                            amountComments += pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().size();
                            for (int o = 0; o < pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().size(); o++) {
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("CDU/CSU")){ // if the comment contain certain key words then increase the counter
                                    CDUCSU += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("FDP")){
                                    FDP += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("DIE LINKE")){
                                    Linke += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("SPD")){
                                    SPD += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("BÜNDNIS 90/DIE GRÜNEN")){
                                    Bündnis += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("AfD")){
                                    Afd += 1;
                                }

                            }
                        }
                    }
                }
            }
            Beifall = (amountComments - CDUCSU - SPD - Afd - Bündnis - Linke - FDP);
            if(Beifall < 0){
                Beifall = 0;
            }

            System.out.println("The Fraction: " + fraction.get(i) + " recived: " + amountComments + " comments");
            System.out.println("From " + amountComments + " comments were from -> " + " CDU/CSU: " + CDUCSU + " SPD: " + SPD + " AfD: " + Afd + " BÜNDNIS 90/DIE GRÜNEN: " + Bündnis + " DIE LINKE: " + Linke + " FDP: " + FDP + " Beifall: " + Beifall);
            System.out.println("\n");
        }
    }

    /**
     * Get the average amount of Speeches each Speaker from a fraction received from each fraction.
     * @param pProtocol
     * @param fraction
     */
    public static void averageCommentsFraction(ArrayList<Protocol> pProtocol, ArrayList<String> fraction){
        for (int i = 0; i < fraction.size(); i++) { //We iterate through every fraction
            Integer amountComments = 0; // Init every amount is 0
            Integer CDUCSU = 0;
            Integer FDP = 0;
            Integer Bündnis = 0;
            Integer Afd = 0;
            Integer SPD = 0;
            Integer Linke = 0;
            Integer Beifall = 0;
            Integer amountspeeches = 0;
            for (int j = 0; j < pProtocol.size(); j++) { // we go through every Protocol
                for (int m = 0; m < pProtocol.get(j).getTagesOrdnungsPunkte().size(); m++) { // through every agenda
                    for (int n = 0; n < pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().size(); n++) { // through every speech
                        if (fraction.get(i).equals(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getSpeaker().get(0).getFraction())) { // compare the fraction
                            amountComments += pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().size();
                            amountspeeches += 1;
                            for (int o = 0; o < pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().size(); o++) {
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("CDU/CSU")){ // if the comment contain certain key words then increase the counter
                                    CDUCSU += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("FDP")){
                                    FDP += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("DIE LINKE")){
                                    Linke += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("SPD")){
                                    SPD += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("BÜNDNIS 90/DIE GRÜNEN")){
                                    Bündnis += 1;
                                }
                                if(pProtocol.get(j).getTagesOrdnungsPunkte().get(m).getSpeeches().get(n).getComments().get(o).getContent().contains("AfD")){
                                    Afd += 1;
                                }

                            }
                        }
                    }
                }
            }
            Beifall = ((amountComments - CDUCSU - SPD - Afd - Bündnis - Linke - FDP)/amountspeeches);
            if(Beifall < 0){
                Beifall = 0;
            }

            System.out.println("The Fraction: " + fraction.get(i) + " recived: " + amountComments + " comments");
            System.out.println("The average per Speech comments are -> " + " CDU/CSU: " + (CDUCSU/amountspeeches) + " SPD: " + (SPD/amountspeeches) + " AfD: " + (Afd/amountspeeches) + " BÜNDNIS 90/DIE GRÜNEN: " + (Bündnis/amountspeeches) + " DIE LINKE: " + (Linke/amountspeeches) + " FDP: " + (FDP/amountspeeches) + " Beifall: " + Beifall);
            System.out.println("\n");
        }
    }




}


