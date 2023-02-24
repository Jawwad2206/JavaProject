package ui;

import com.mongodb.Mongo;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import data.Protocol;
import data.Speaker;
import data.impl.mongoDB_impl.Protocol_MongoDB_Impl;
import data.impl.mongoDB_impl.Speech_MongoDB_Impl;
import database.MongoDBConnectionHandler;
import exceptions.WrongInputException;
import funcations.HelperFunctions;
import funcations.RedeAnalyzer;
import org.apache.uima.UIMAException;
import org.bson.Document;
import org.neo4j.cypher.internal.frontend.v3_2.phases.Do;
import org.xml.sax.SAXException;
import scala.Int;

import javax.print.Doc;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * This is the UserInterface, the main will start this class and in this class all the Tasks will be answered
 * @author Jawwad Khan
 */
public class UserInterfaceNLP {

    Boolean CLOSE; // Default Value is fault (used) for while Loops
    Boolean CLOSING;// Default Value is fault (used) for while Loops

    public static void main4(String txtpath, String ddcPath, String posPath) throws ParserConfigurationException, IOException, WrongInputException, SAXException, ParseException, UIMAException {
        MongoDatabase database = MongoDBConnectionHandler.dbConnector(txtpath); // we connect with the MongoDB
        MongoCollection speeches = database.getCollection("speech"); // we get the desired collection
        UserInterfaceNLP menuForNLP = new UserInterfaceNLP(); //create a new Userinterface object
        menuForNLP.printHeader();
        menuForNLP.MainMenu(speeches);

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
     * This is the MainMenu for Tasksheet 1 with this you can answer the task 2.
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void MainMenu(MongoCollection speeches) throws ParserConfigurationException, IOException, SAXException, WrongInputException {
        System.out.println("All XML Files were sucessfully found!");
        System.out.println("|----------Welcome to the Plenarprotocol App----------|");
        CLOSE = false;
        while (!CLOSE) {
            System.out.println("\nBelow you will see some Actions this Programm can perform");
            System.out.println("Please press the one of the following Numbers to perfrom an action: ");
            System.out.println("1. Task2\n");
            System.out.println("2. End the Programm\n");
            int CHOICE = HelperFunctions.integerInputs(1, 2);
            doMenu(CHOICE, speeches); //Starts the Menu with the the given parameters
        }
    }
    /**
     * Through the input entered from the getInput one of the following task will be performed.
     * @param CHOICE
     */
    public void doMenu(int CHOICE, MongoCollection speeches) throws WrongInputException {
        switch (CHOICE) {
            case 1:
                Task2Menu(speeches); //starts the Menu for the second task
                break;
            case 2:
                System.out.println("|--------------------------------------------|");
                System.out.println("|------------See you next time---------------|");
                System.out.println("|--------------------------------------------|");
                CLOSE = true; // closes the MainMenu.
                break;
        }
    }
    /**
     * Main Menu for the second task question f.
     */
    public void Task2Menu(MongoCollection speeches) throws WrongInputException {
        CLOSING = false;
        while(!CLOSING) {
            System.out.println("\nBelow you will see some actions which you can perform");
            System.out.println("1. Get all lemma noun or verb");
            System.out.println("2. Get all named entities");
            System.out.println("3. Get all DDC3-Tags");
            System.out.println("4. Get all sentiment-values");
            System.out.println("5. Get information regarding the sentiment of comments");
            System.out.println("6.Return to menu");
            int CHOICE = HelperFunctions.integerInputs(1, 6);
            mainMenuTask2(CHOICE, speeches); // starts one of the task from above
        }
    }
    /**
     * Main Menu for Task 2.
     * @param CHOICE
     */
    public void mainMenuTask2(int CHOICE, MongoCollection speeches) throws WrongInputException {
        switch (CHOICE){
            case 1:
                System.out.println("0: Return to selection screen");
                System.out.println("1: Lemma noun");
                System.out.println("2: Lemma verb");
                Integer lemma = HelperFunctions.integerInputs(1, 2);
                if(lemma == 1) {
                    System.out.println("0: Return to selection screen");
                    System.out.println("1: All nouns of all speeches");
                    System.out.println("2: All nouns of a certain speaker");
                    System.out.println("3: All nouns of a certain fraction");
                    System.out.println("4: All nouns of a certain session");
                    System.out.println("5: All nouns between two sessions");
                    Integer decision = HelperFunctions.integerInputs(1, 5);
                    if (decision == 1) {
                        lemmNoun(speeches);
                        break;

                    }
                    if (decision == 2) {
                        String speakerName = HelperFunctions.Input();
                        lemmaNounSpeaker(speeches, speakerName);
                    }
                    if (decision == 3) {
                        String fractionName = HelperFunctions.FractionInput();
                        lemmaNounFraction(speeches, fractionName);
                    }
                    if (decision == 4) {
                        System.out.println("!Information! -> The last session has the nr: 239");
                        System.out.println("If entered a nr = 0 it will be corrected to 1");
                        int sNR = HelperFunctions.integerInputs(1, 239);
                        if (sNR <= 0) {
                            sNR = 1;
                        }
                        lemmaNounSession(speeches, String.valueOf(sNR));
                    }
                    if(decision == 5){
                        System.out.println("!Information! -> The last session has the nr: 239");
                        System.out.println("If entered a nr = 0 it will be corrected to 1");
                        int firstSession = HelperFunctions.integerInputs(1, 239);
                        if(firstSession <= 0){
                            firstSession = 1;
                        }
                        int secondSession = HelperFunctions.integerInputs(1, 239);
                        if(secondSession <= 0){
                            secondSession = 1;
                        }
                        if(firstSession > secondSession){
                            lemmaNounSessions(speeches, secondSession, firstSession);
                        }else{
                            lemmaNounSessions(speeches, firstSession, secondSession);
                        }
                    }
                }
                if(lemma == 2) {
                    System.out.println("0: Return to selection screen");
                    System.out.println("1: All verb of all speeches");
                    System.out.println("2: All verb of a certain speaker");
                    System.out.println("3: All verb of a certain fraction");
                    System.out.println("4: All verb of a certain session");
                    System.out.println("5: All verb between two sessions");
                    Integer decisionVerb = HelperFunctions.integerInputs(1, 4);
                    if(decisionVerb == 1){
                        lemmaVerb(speeches);
                    }
                    if(decisionVerb == 2){
                        String speakerName = HelperFunctions.Input();
                        lemmaVERBSpeaker(speeches, speakerName);

                    }
                    if(decisionVerb == 3){
                        String fractionName = HelperFunctions.FractionInput();
                        lemmaVERBFraction(speeches, fractionName);

                    }
                    if(decisionVerb == 4){
                        System.out.println("!Information! -> The last session has the nr: 239");
                        System.out.println("If entered a nr = 0 it will be corrected to 1");
                        int sNR = HelperFunctions.integerInputs(1, 239);
                        if(sNR <= 0){
                            sNR = 1;
                        }

                        lemmaVERBSession(speeches, String.valueOf(sNR));
                    }
                    if(decisionVerb == 5){
                        System.out.println("!Information! -> The last session has the nr: 239");
                        System.out.println("If entered a nr = 0 it will be corrected to 1");
                        int firstSession = HelperFunctions.integerInputs(1, 239);
                        if(firstSession <= 0){
                            firstSession = 1;
                        }
                        int secondSession = HelperFunctions.integerInputs(1, 239);
                        if(secondSession <= 0){
                            secondSession = 1;
                        }
                        if(firstSession > secondSession){
                            lemmaVERBSessions(speeches, secondSession, firstSession);
                        }else{
                            lemmaVERBSessions(speeches, firstSession, secondSession);
                        }
                    }
                }
                break;
            case 2:
                System.out.println("!Warning!These Options can take some time");
                System.out.println("0: Return to selection screen");
                System.out.println("1: Return all named entities without filter");
                System.out.println("2: Return all named entities for a certain speaker");
                System.out.println("3: Return all named entities for a certain fraction");
                System.out.println("4: Return all named entities for a certain session");
                System.out.println("5: Return all named entities between two sessions");
                Integer namedEntity = HelperFunctions.integerInputs(1,5);
                if(namedEntity == 1){
                    System.out.println("loading.... Please wait a min.....");
                    namedEntityOrganisation(speeches);
                    System.out.println("------------------------------------------------------------------------------");
                    namedEntityLocation(speeches);
                    System.out.println("------------------------------------------------------------------------------");
                    namedEntityPerson(speeches);
                    System.out.println("------------------------------------------------------------------------------");
                    namedEntityMiscellanies(speeches);
                }
                if(namedEntity == 2){
                    String speakerName = HelperFunctions.Input();
                    organisationSpeaker(speeches, speakerName);
                    System.out.println("------------------------------------------------------------------------------");
                    locationSpeaker(speeches, speakerName);
                    System.out.println("------------------------------------------------------------------------------");
                    personSpeaker(speeches, speakerName);
                    System.out.println("------------------------------------------------------------------------------");
                    miscellaniesSpeaker(speeches, speakerName);
                }
                if(namedEntity == 3){
                    String fractionName = HelperFunctions.FractionInput();
                    organisationFraction(speeches, fractionName);
                    System.out.println("------------------------------------------------------------------------------");
                    locationFraction(speeches, fractionName);
                    System.out.println("------------------------------------------------------------------------------");
                    personFraction(speeches, fractionName);
                    System.out.println("------------------------------------------------------------------------------");
                    miscellaniesFraction(speeches, fractionName);
                }
                if(namedEntity == 4){
                    System.out.println("!Information! -> The last session has the nr: 239");
                    System.out.println("If entered a nr = 0 it will be corrected to 1");
                    int sNR = HelperFunctions.integerInputs(1, 239);
                    if (sNR <= 0) {
                        sNR = 1;
                    }
                    organisationSessionnr(speeches, String.valueOf(sNR));
                    System.out.println("------------------------------------------------------------------------------");
                    locationSessionnr(speeches, String.valueOf(sNR));
                    System.out.println("------------------------------------------------------------------------------");
                    personSessionnr(speeches, String.valueOf(sNR));
                    System.out.println("------------------------------------------------------------------------------");
                    miscellaniesSessionnr(speeches, String.valueOf(sNR));
                }
                if(namedEntity == 5){
                    System.out.println("!Information! -> The last session has the nr: 239");
                    System.out.println("If entered a nr = 0 it will be corrected to 1");
                    int firstSession = HelperFunctions.integerInputs(1, 239);
                    if(firstSession <= 0){
                        firstSession = 1;
                    }
                    int secondSession = HelperFunctions.integerInputs(1, 239);
                    if(secondSession <= 0){
                        secondSession = 1;
                    }
                    if(firstSession > secondSession){
                        organisationSessionnrs(speeches, secondSession, firstSession);
                        System.out.println("------------------------------------------------------------------------------");
                        locationSessionnrs(speeches, secondSession, firstSession);
                        System.out.println("------------------------------------------------------------------------------");
                        personSessionnrs(speeches, secondSession, firstSession);
                        System.out.println("------------------------------------------------------------------------------");
                        miscellaniesSessionnrs(speeches, secondSession, firstSession);
                    }else{
                        organisationSessionnrs(speeches, firstSession, secondSession);
                        System.out.println("------------------------------------------------------------------------------");
                        locationSessionnrs(speeches, firstSession, secondSession);
                        System.out.println("------------------------------------------------------------------------------");
                        personSessionnrs(speeches, firstSession, secondSession);
                        System.out.println("------------------------------------------------------------------------------");
                        miscellaniesSessionnrs(speeches, firstSession, secondSession);
                    }
                }
                break;
            case 3:
                System.out.println("DDC3");
                break;
            case 4:
                System.out.println("0: Return to selection screen");
                System.out.println("1: Return sentiment distribution without filter for whole corpus");
                System.out.println("2: Return sentiment distribution for a certain speaker for whole corpus");
                System.out.println("3: Return sentiment distribution for a certain fraction for whole corpus");
                System.out.println("4: Return sentiment distribution for a certain session for whole corpus");
                System.out.println("5: Return sentiment distribution between two sessions for whole corpus");
                Integer sentiment = HelperFunctions.integerInputs(1,5);
                if(sentiment == 1) {
                    sentimentAllSpeeches(speeches);
                }
                if(sentiment == 2) {
                    String speakerName = HelperFunctions.Input();
                    sentimentSpeaker(speeches, speakerName);
                }
                if(sentiment == 3){
                    String fraction = HelperFunctions.FractionInput();
                    sentimentFraction(speeches, fraction);
                }
                if(sentiment == 4){
                    System.out.println("!Information! -> The last session has the nr: 239");
                    System.out.println("If entered a nr = 0 it will be corrected to 1");
                    int sNR = HelperFunctions.integerInputs(1, 239);
                    if(sNR <= 0){
                        sNR = 1;
                    }
                    sentimentSession(speeches, String.valueOf(sNR));
                }
                if(sentiment == 5){
                    System.out.println("!Information! -> The last session has the nr: 239");
                    System.out.println("If entered a nr = 0 it will be corrected to 1");
                    int firstSession = HelperFunctions.integerInputs(1, 239);
                    if(firstSession <= 0){
                        firstSession = 1;
                    }
                    int secondSession = HelperFunctions.integerInputs(1, 239);
                    if(secondSession <= 0){
                        secondSession = 1;
                    }
                    if(firstSession > secondSession){
                        sentimentSessions(speeches, secondSession, firstSession);
                    }else{
                        sentimentSessions(speeches, firstSession, secondSession);
                    }
                }
                break;
            case 5:
                System.out.println("0: Return to selection screen");
                System.out.println("1: Return to positive comments");
                System.out.println("2: Return to negative comments");
                System.out.println("3: Return to neutral comments");
                Integer value = HelperFunctions.integerInputs(1, 3);
                if(value == 1){
                    System.out.println("0: Return to selection screen");
                    System.out.println("1: Return speeches sorted by the amount of positive comments");
                    System.out.println("2: Return speeches sorted by the amount of positive comments for a certain speaker");
                    System.out.println("3: Return speeches sorted by the amount of positive comments for a certain fraction");
                    System.out.println("4: Return speeches sorted by the amount of positive comments for a certain session");
                    System.out.println("5: Return speeches sorted by the amount of positive comments between two session");
                    Integer positive = HelperFunctions.integerInputs(1, 5);
                    if(positive == 1){
                        speechMostPositiv(speeches);
                    }
                    if(positive == 2){
                        String speaker = HelperFunctions.Input();
                        speechMostPositivSpeaker(speeches, speaker);
                    }
                    if(positive == 3){
                        String fraction = HelperFunctions.FractionInput();
                        speechMostPositivFraction(speeches, fraction);
                    }
                    if(positive == 4){
                        int sNR = HelperFunctions.integerInputs(1, 239);
                        if(sNR <= 0){
                            sNR = 1;
                        }
                        speechMostPositivSession(speeches, String.valueOf(sNR));
                    }
                    if(positive == 5) {
                        System.out.println("!Information! -> The last session has the nr: 239");
                        System.out.println("If entered a nr = 0 it will be corrected to 1");
                        int firstSession = HelperFunctions.integerInputs(1, 239);
                        if (firstSession <= 0) {
                            firstSession = 1;
                        }
                        int secondSession = HelperFunctions.integerInputs(1, 239);
                        if (secondSession <= 0) {
                            secondSession = 1;
                        }
                        if (firstSession > secondSession) {
                            speechMostPositivSessions(speeches, secondSession, firstSession);
                        } else {
                            speechMostPositivSessions(speeches, firstSession, secondSession);
                        }
                    }

                }
                if(value == 2){
                    System.out.println("0: Return to selection screen");
                    System.out.println("1: Return speeches sorted by the amount of negative comments");
                    System.out.println("2: Return speeches sorted by the amount of negative comments for a certain speaker");
                    System.out.println("3: Return speeches sorted by the amount of negative comments for a certain fraction");
                    System.out.println("4: Return speeches sorted by the amount of negative comments for a certain session");
                    System.out.println("5: Return speeches sorted by the amount of negative comments between two session");
                    Integer negative = HelperFunctions.integerInputs(1, 5);
                    if(negative == 1){
                        speechMostNegative(speeches);
                    }
                    if(negative == 2){
                        String speaker = HelperFunctions.Input();
                        speechMostNegativeSpeaker(speeches, speaker);
                    }
                    if(negative == 3){
                        String fraction = HelperFunctions.FractionInput();
                        speechMostNegativeFraction(speeches, fraction);
                    }
                    if(negative == 4){
                        int sNR = HelperFunctions.integerInputs(1, 239);
                        if(sNR <= 0){
                            sNR = 1;
                        }
                        speechMostNegativeSession(speeches, String.valueOf(sNR));
                    }
                    if(negative == 5) {
                        System.out.println("!Information! -> The last session has the nr: 239");
                        System.out.println("If entered a nr = 0 it will be corrected to 1");
                        int firstSession = HelperFunctions.integerInputs(1, 239);
                        if (firstSession <= 0) {
                            firstSession = 1;
                        }
                        int secondSession = HelperFunctions.integerInputs(1, 239);
                        if (secondSession <= 0) {
                            secondSession = 1;
                        }
                        if (firstSession > secondSession) {
                            speechMostNegativeSessions(speeches, secondSession, firstSession);
                        } else {
                            speechMostNegativeSessions(speeches, firstSession, secondSession);
                        }
                    }

                }
                if(value == 3){
                    System.out.println("0: Return to selection screen");
                    System.out.println("1: Return speeches sorted by the amount of neutral comments");
                    System.out.println("2: Return speeches sorted by the amount of neutral comments for a certain speaker");
                    System.out.println("3: Return speeches sorted by the amount of neutral comments for a certain fraction");
                    System.out.println("4: Return speeches sorted by the amount of neutral comments for a certain session");
                    System.out.println("5: Return speeches sorted by the amount of neutral comments between two session");
                    Integer neutral = HelperFunctions.integerInputs(1, 5);
                    if(neutral == 1){
                        speechMostNeutral(speeches);
                    }
                    if(neutral == 2){
                        String speaker = HelperFunctions.Input();
                        speechMostNeutralSpeaker(speeches, speaker);
                    }
                    if(neutral == 3){
                        String fraction = HelperFunctions.FractionInput();
                        speechMostNeutralFraction(speeches, fraction);
                    }
                    if(neutral == 4){
                        int sNR = HelperFunctions.integerInputs(1, 239);
                        if(sNR <= 0){
                            sNR = 1;
                        }
                        speechMostNeutralSession(speeches, String.valueOf(sNR));
                    }
                    if(neutral == 5) {
                        System.out.println("!Information! -> The last session has the nr: 239");
                        System.out.println("If entered a nr = 0 it will be corrected to 1");
                        int firstSession = HelperFunctions.integerInputs(1, 239);
                        if (firstSession <= 0) {
                            firstSession = 1;
                        }
                        int secondSession = HelperFunctions.integerInputs(1, 239);
                        if (secondSession <= 0) {
                            secondSession = 1;
                        }
                        if (firstSession > secondSession) {
                            speechMostNeutralSessions(speeches, secondSession, firstSession);
                        } else {
                            speechMostNeutralSessions(speeches, firstSession, secondSession);
                        }
                    }
                }
                break;
            case 6:
                CLOSING = true;
                break;
            default:
                System.out.println("Wrong Input! Please enter a number between 1 and 6");
        }
    }

    /**
     * This Mehtod starts the analysis for the speeches
     * @param txtpath
     * @param ddcPath
     * @param posPath
     * @throws IOException
     * @throws UIMAException
     */
    public static void speechAnalyser(String txtpath, String ddcPath, String posPath) throws IOException, UIMAException {

        Map<String, String> ddcCSV = RedeAnalyzer.getDDC(ddcPath); //get all the DDC3 categories

        MongoDatabase database = MongoDBConnectionHandler.dbConnector(txtpath); // we connect with the MongoDB

        MongoCollection speeches = database.getCollection("speech"); // we get the desired collection

        MongoCursor cursor = speeches.find().cursor(); //iterate through every protocol
        Integer counter = 0; //a counter to check where we are
        while (cursor.hasNext()) { //we analyse as long as we have documents in the collection
            Document document = (Document) cursor.next(); //cast every object we get into an document
            Speech_MongoDB_Impl speech = new Speech_MongoDB_Impl(document); // create with the document a new speechDB object
            RedeAnalyzer.speechAnalyser(document, speeches, ddcCSV, posPath); // start the analysis for the Document
            counter += 1;
            System.out.println("\n");
            System.out.println(counter + "/25164 sind fertig sind bei rede: " + speech.getSpeechID()); // print where we are
        }

    }

    /**
     * This method filters all the VERB and there frequency through all the speeches
     * @param speech
     */
    public static void lemmaVerb(MongoCollection speech){
        List<Document> filterNounAll = Arrays.asList(new Document().append("$unwind", new Document().append("path", "$Token")), //unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.VERB")), //unwind the Token.VERB field
                new Document().append("$addFields", new Document().append("allVERB", new Document().append("$objectToArray", "$Token.VERB"))), // add a new Field where every VERB is in it
                new Document().append("$project", new Document().append("allVERB", 1.0)), //project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allVERB")), // unwind all VERB
                new Document().append("$group", new Document().append("_id", "$allVERB.k").append("result", new Document().append("$sum", "$allVERB.v"))), //group all keys(VERB) and sum the counts(values)
                new Document().append("$sort", new Document().append("result", -1.0))); // sort desc

        MongoCursor<Document> cursor = speech.aggregate(filterNounAll).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("VERB: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));

        }

    }

    /**
     * This method filters all the VERB of a certain Speaker
     * @param speech
     * @param Speaker
     */
    public static void lemmaVERBSpeaker(MongoCollection speech, String Speaker){
        List<Document> filterNounAllSpeaker = Arrays.asList(new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", Speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.VERB")),  //unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allVERB", new Document().append("$objectToArray", "$Token.VERB"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allVERB", 1.0)), //project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allVERB")), // unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allVERB.k").append("result", new Document().append("$sum", "$allVERB.v"))), //group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc

        MongoCursor<Document> cursor = speech.aggregate(filterNounAllSpeaker).allowDiskUse(true).cursor(); //cursor for every document
        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("The Speaker: " + Speaker + " had used the following VERBS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));

        }

    }

    /**
     * This method filters all the VERB of a certain fraction
     * @param speech
     * @param fraction
     */
    public static void lemmaVERBFraction(MongoCollection speech, String fraction){
        List<Document> filterNounAllSpeaker = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.VERB")),//unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allVERB", new Document().append("$objectToArray", "$Token.VERB"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allVERB", 1.0)),//project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allVERB")),// unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allVERB.k").append("result", new Document().append("$sum", "$allVERB.v"))),//group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc

        MongoCursor<Document> cursor = speech.aggregate(filterNounAllSpeaker).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("The fraction: " + fraction + " had used the following VERBS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));
        }

    }

    /**
     * This method gets all the VERB of a certain session
     * @param speech
     * @param Sessionnr
     */
    public static void lemmaVERBSession(MongoCollection speech, String Sessionnr){
        List<Document> filterNounSession = Arrays.asList(new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.VERB")),//unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allVERB", new Document().append("$objectToArray", "$Token.VERB"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allVERB", 1.0)),//project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allVERB")),// unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allVERB.k").append("result", new Document().append("$sum", "$allVERB.v"))),//group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc
        MongoCursor<Document> cursor = speech.aggregate(filterNounSession).allowDiskUse(true).cursor(); //cursor for every document
        String solution = "";
        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("In the Session: " + Sessionnr + " had been used the following VERBS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));
            //solution += "In the Session: " + Sessionnr + " had been used the following VERBS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result") + "\n";

        }
        //HelperFunctions.createTxT("Uebung3_Frage_2a_Verben_Sitzung", solution);

    }

    /**
     * This method gets all the VERB between two sessions
     * @param speech

     */
    public static void lemmaVERBSessions(MongoCollection speech, int session, int secondSession){
        List<Document> filterNounSession = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", session).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.VERB")),//unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allVERB", new Document().append("$objectToArray", "$Token.VERB"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allVERB", 1.0)),//project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allVERB")),// unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allVERB.k").append("result", new Document().append("$sum", "$allVERB.v"))),//group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc
        MongoCursor<Document> cursor = speech.aggregate(filterNounSession).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("In between the Sessions: " + session + " and " + secondSession + " had been used the following VERBS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));

        }


    }


    /**
     * This method filters all the Noun and there frequency through all the speeches
     * @param speech
     */
    public static void lemmNoun(MongoCollection speech){
        List<Document> filterNounAll = Arrays.asList(new Document().append("$unwind", new Document().append("path", "$Token")), //unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.NOUN")), //unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allNoun", new Document().append("$objectToArray", "$Token.NOUN"))), // add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allNoun", 1.0)), //project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allNoun")), // unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allNoun.k").append("result", new Document().append("$sum", "$allNoun.v"))), //group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0))); // sort desc

        MongoCursor<Document> cursor = speech.aggregate(filterNounAll).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("NOUN: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));



        }
    }


    /**
     * This method filters all the Noun of a certain Speaker
     * @param speech
     * @param Speaker
     */
    public static void lemmaNounSpeaker(MongoCollection speech, String Speaker){
        List<Document> filterNounAllSpeaker = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", Speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.NOUN")),  //unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allNoun", new Document().append("$objectToArray", "$Token.NOUN"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allNoun", 1.0)), //project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allNoun")), // unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allNoun.k").append("result", new Document().append("$sum", "$allNoun.v"))), //group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc

        MongoCursor<Document> cursor = speech.aggregate(filterNounAllSpeaker).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("The Speaker: " + Speaker + " had used the following NOUNS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));

        }

    }

    /**
     * This method filters all the Noun of a certain fraction
     * @param speech
     * @param fraction
     */
    public static void lemmaNounFraction(MongoCollection speech, String fraction){
        List<Document> filterNounAllSpeaker = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.NOUN")),//unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allNoun", new Document().append("$objectToArray", "$Token.NOUN"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allNoun", 1.0)),//project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allNoun")),// unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allNoun.k").append("result", new Document().append("$sum", "$allNoun.v"))),//group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc

        MongoCursor<Document> cursor = speech.aggregate(filterNounAllSpeaker).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("The fraction: " + fraction + " had used the following NOUNS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));
        }

    }

    /**
     * This method get all the VERB of a certain session
     * @param speech
     * @param Sessionnr
     */
    public static void lemmaNounSession(MongoCollection speech, String Sessionnr){
        List<Document> filterNounSession = Arrays.asList(new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.NOUN")),//unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allNoun", new Document().append("$objectToArray", "$Token.NOUN"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allNoun", 1.0)),//project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allNoun")),// unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allNoun.k").append("result", new Document().append("$sum", "$allNoun.v"))),//group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc
        MongoCursor<Document> cursor = speech.aggregate(filterNounSession).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("In the Session: " + Sessionnr + " had been used the following NOUNS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));

        }


    }

    /**
     * This method get all the VERB of a certain session
     * @param speech
     */
    public static void lemmaNounSessions(MongoCollection speech, int firstSession, int secondSession){
        List<Document> filterNounSession = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$Token")),//unwind the Token field
                new Document().append("$unwind", new Document().append("path", "$Token.NOUN")),//unwind the Token.Noun field
                new Document().append("$addFields", new Document().append("allNoun", new Document().append("$objectToArray", "$Token.NOUN"))),// add a new Field where every Noun is in it
                new Document().append("$project", new Document().append("allNoun", 1.0)),//project the new Field for less load time
                new Document().append("$unwind", new Document().append("path", "$allNoun")),// unwind all Nouns
                new Document().append("$group", new Document().append("_id", "$allNoun.k").append("result", new Document().append("$sum", "$allNoun.v"))),//group all keys(Nouns) and sum the countss(values)
                new Document().append("$sort", new Document().append("result", -1.0)));// sort desc
        MongoCursor<Document> cursor = speech.aggregate(filterNounSession).allowDiskUse(true).cursor(); //cursor for every document

        while (cursor.hasNext()) {
            Document document = cursor.next(); // cast every cursor.next into a Document
            System.out.println("In between the Sessions: " + firstSession + " and " + secondSession + " had been used the following NOUNS: " + document.get("_id").toString().replace("#", ".") + " | frequency: " + document.get("result"));

        }


    }


    /**
     * This method gets all the namedentities with the tag Organisation from the comments and speeches
     * @param speech
     */
    public static void namedEntityOrganisation(MongoCollection speech){
        List<Document> organisationSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$organisation.k").append("result", new Document().append("$sum", "$organisation.v"))),//group organisations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> organisationCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "organisation.k").append("result", new Document().append("$sum", "$organisation.v"))));//group all organisations keys(name) and sum the value(frequenzy)

        List<Document> commentOrganisationCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy")),//unwind the namedentity collum of comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy.Organisations")),//unwind the organisations collum of comments
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$comments.NamedEntitiy.Organisations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "organisation.k").append("result", new Document().append("$sum", "$organisation.v"))));//group all organisations keys(name) and sum the value(frequenzy)

        MongoCursor<Document> cursorComment = speech.aggregate(commentOrganisationCount).cursor(); //use the pipline to get desired output
        Integer counter = 0;
        while(cursorComment.hasNext()){
            Document docComment = cursorComment.next();
            counter = (Integer) docComment.get("result"); //get the value of how many organisations there are in comments
        }

        MongoCursor<Document> cursor = speech.aggregate(organisationCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many organisations there are in speeches
            System.out.println("The named entity Organisation occured: " + (counterSpeech+counter) + " throughout all the speeches");
        }

        MongoCursor<Document> orgCursor = speech.aggregate(organisationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document organisation = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Organisation: " + organisation.get("_id").toString().replace("#", ".") + " |frequenzy: " + organisation.get("result"));

        }

    }

    /**
     * This method gets all the namedentities with the tag Location from the comments and speeches
     * @param speech
     */
    public static void namedEntityLocation(MongoCollection speech){
        List<Document> locationSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$locations")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$locations.k").append("result", new Document().append("$sum", "$locations.v"))),//group locations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> locationCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("Locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$locations")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "locations.k").append("result", new Document().append("$sum", "$locations.v"))));//group all locations keys(name) and sum the value(frequenzy)

        List<Document> commentlocationCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy")),//unwind the namedentity collum of comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy.Locations")),//unwind the locations collum of comments
                new Document().append("$addFields", new Document().append("locations", new Document().append("$objectToArray", "$comments.NamedEntitiy.Locations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$locations")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "locations.k").append("result", new Document().append("$sum", "$locations.v"))));//group all locations keys(name) and sum the value(frequenzy)

        MongoCursor<Document> cursorComment = speech.aggregate(commentlocationCount).cursor(); //use the pipline to get desired output
        Integer counter = 0;
        while(cursorComment.hasNext()){
            Document docComment = cursorComment.next();
            counter = (Integer) docComment.get("result"); //get the value of how many locations there are in comments
        }

        MongoCursor<Document> cursor = speech.aggregate(locationCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity location occured: " + (counterSpeech+counter) + " throughout all the speeches");
        }

        MongoCursor<Document> orgCursor = speech.aggregate(locationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Location: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method gets all the namedentities with the tag Person from the comments and speeches
     * @param speech
     */
    public static void namedEntityPerson(MongoCollection speech){
        List<Document> personSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$persons.k").append("result", new Document().append("$sum", "$persons.v"))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> personCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", "$persons.v"))));//group all Persons keys(name) and sum the value(frequenzy)

        List<Document> commentPersonCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy")),//unwind the namedentity collum of comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy.Persons")),//unwind the Persons collum of comments
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$comments.NamedEntitiy.Persons"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", "$persons.v"))));//group all Persons keys(name) and sum the value(frequenzy)

        MongoCursor<Document> cursorComment = speech.aggregate(commentPersonCount).cursor(); //use the pipline to get desired output
        Integer counter = 0;
        while(cursorComment.hasNext()){
            Document docComment = cursorComment.next();
            counter = (Integer) docComment.get("result"); //get the value of how many Persons there are in comments
        }

        MongoCursor<Document> cursor = speech.aggregate(personCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many Persons there are in speeches
            System.out.println("The named entity person occured: " + (counterSpeech+counter) + " throughout all the speeches");
        }

        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document person = orgCursor.next(); //for every Persons found
            System.out.println("NamedEntity-Person: " + person.get("_id").toString().replace("#", ".") + " |frequenzy: " + person.get("result"));

        }

    }

    /**
     * This method gets all the namedentities with the tag Miscellanies from the comments and speeches
     * @param speech
     */
    public static void namedEntityMiscellanies(MongoCollection speech){
        List<Document> miscellaniesSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind  all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))),//group Miscellanies keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> miscellaniesCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))));//group all Miscellanies keys(name) and sum the value(frequenzy)

        List<Document> commentMiscellaniesCount = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy")),//unwind the namedentity collum of comments
                new Document().append("$unwind", new Document().append("path", "$comments.NamedEntitiy.Miscellanies")),//unwind the Miscellanies collum of comments
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$comments.NamedEntitiy.Miscellanies"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))));//group all Miscellanies keys(name) and sum the value(frequenzy)

        MongoCursor<Document> cursorComment = speech.aggregate(commentMiscellaniesCount).cursor(); //use the pipline to get desired output
        Integer counter = 0;
        while(cursorComment.hasNext()){
            Document docComment = cursorComment.next();
            counter = (Integer) docComment.get("result"); //get the value of how many Miscellanies there are in comments
        }

        MongoCursor<Document> cursor = speech.aggregate(miscellaniesCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many Miscellanies there are in speeches
            System.out.println("The named entity miscellanies occured: " + (counterSpeech+counter) + " throughout all the speeches");
        }

        MongoCursor<Document> orgCursor = speech.aggregate(miscellaniesSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document miscellanies = orgCursor.next(); //for every Persons found
            System.out.println("NamedEntity-Miscellanies: " + miscellanies.get("_id").toString().replace("#", ".") + " |frequenzy: " + miscellanies.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag organisation for a certain speaker
     * @param speech
     * @param speaker
     */
    public static void organisationSpeaker(MongoCollection speech, String speaker){
        List<Document> organisationSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$organisation.k").append("result", new Document().append("$sum", "$organisation.v"))),//group organisations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> organisationCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "organisation.k").append("result", new Document().append("$sum", "$organisation.v"))));//group all organisations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(organisationCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many organisations there are in speeches
            System.out.println("The named entity Organisation occured: " + counterSpeech + " throughout all the speech of: " + speaker);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(organisationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document organisation = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Organisation: " + organisation.get("_id").toString().replace("#", ".") + " |frequenzy: " + organisation.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag location for a certain speaker
     * @param speech
     */
    public static void locationSpeaker(MongoCollection speech, String speaker){
        List<Document> locationSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$locations")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$locations.k").append("result", new Document().append("$sum", "$locations.v"))),//group locations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> pipeline = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("Locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("Locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$Locations")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "Locations.k").append("result", new Document().append("$sum", "$Locations.v"))));//group all locations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(pipeline).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity location occured: " + counterSpeech + " throughout all the speech of: " + speaker);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(locationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Location: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag person for a certain speaker
     * @param speech
     */
    public static void personSpeaker(MongoCollection speech, String speaker){
        List<Document> personSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$persons.k").append("result", new Document().append("$sum", "$persons.v"))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> personCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", "$persons.v"))));//group all Persons keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(personCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity persons occured: " + counterSpeech + " throughout all the speech of: " + speaker);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Person: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag Miscellanies for a certain speaker
     * @param speech
     */
    public static void miscellaniesSpeaker(MongoCollection speech, String speaker){
        List<Document> miscellaniesSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind  all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))),//group Miscellanies keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> miscellaniesCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))));//group all Miscellanies keys(name) and sum the value(frequenzy)



        MongoCursor<Document> cursor = speech.aggregate(miscellaniesCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity miscellanies occured: " + counterSpeech + " throughout all the speech of: " + speaker);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(miscellaniesSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Miscellanies: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag organisation for a certain fraction
     * @param speech
     * @param fraction
     */
    public static void organisationFraction(MongoCollection speech, String fraction){
        List<Document> organisationSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$organisation.k").append("result", new Document().append("$sum", "$organisation.v"))),//group organisations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> organisationCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "organisation.k").append("result", new Document().append("$sum", "$organisation.v"))));//group all organisations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(organisationCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many organisations there are in speeches
            System.out.println("The named entity Organisation occured: " + counterSpeech + " throughout all the speeches of the speaker from the Fraction: " + fraction);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(organisationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document organisation = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Organisation: " + organisation.get("_id").toString().replace("#", ".") + " |frequenzy: " + organisation.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag location for a certain fraction
     * @param fraction
     */
    public static void locationFraction(MongoCollection speech, String fraction){
        List<Document> locationSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$locations")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$locations.k").append("result", new Document().append("$sum", "$locations.v"))),//group locations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> pipeline = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("Locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("Locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$Locations")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "Locations.k").append("result", new Document().append("$sum", "$Locations.v"))));//group all locations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(pipeline).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity location occured: " + counterSpeech + " throughout all the speeches of the speaker from the Fraction: " + fraction);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(locationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Location: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag person for a certain fraction
     * @param fraction
     */
    public static void personFraction(MongoCollection speech, String fraction){
        List<Document> personSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$persons.k").append("result", new Document().append("$sum", "$persons.v"))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> personCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", "$persons.v"))));//group all Persons keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(personCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity persons occured: " + counterSpeech + " throughout all the speeches of the speaker from the Fraction: " + fraction);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Person: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag Miscellanies for a certain fraction
     * @param speech
     */
    public static void miscellaniesFraction(MongoCollection speech, String fraction){
        List<Document> miscellaniesSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind  all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))),//group Miscellanies keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> miscellaniesCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))));//group all Miscellanies keys(name) and sum the value(frequenzy)



        MongoCursor<Document> cursor = speech.aggregate(miscellaniesCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity miscellanies occured: " + counterSpeech + " throughout all the speeches of the speaker from the Fraction: " + fraction);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(miscellaniesSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Miscellanies: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag organisation for a certain session
     * @param speech
     * @param Sessionnr
     */
    public static void organisationSessionnr(MongoCollection speech, String Sessionnr){
        List<Document> organisationSort = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$organisation.k").append("result", new Document().append("$sum", "$organisation.v"))),//group organisations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> organisationCount = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "organisation.k").append("result", new Document().append("$sum", "$organisation.v"))));//group all organisations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(organisationCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many organisations there are in speeches
            System.out.println("The named entity Organisation occured: " + counterSpeech + " throughout all the speeches in the session: " + Sessionnr);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(organisationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document organisation = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Organisation: " + organisation.get("_id").toString().replace("#", ".") + " |frequenzy: " + organisation.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag location for a certain session
     * @param Sessionnr
     */
    public static void locationSessionnr(MongoCollection speech, String Sessionnr){
        List<Document> locationSort = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$locations")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$locations.k").append("result", new Document().append("$sum", "$locations.v"))),//group locations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> pipeline = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("Locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("Locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$Locations")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "Locations.k").append("result", new Document().append("$sum", "$Locations.v"))));//group all locations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(pipeline).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity location occured: " + counterSpeech + " throughout all the speeches in the session: " + Sessionnr);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(locationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Location: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag person for a certain session
     * @param Sessionnr
     */
    public static void personSessionnr(MongoCollection speech, String Sessionnr){
        List<Document> personSort = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$persons.k").append("result", new Document().append("$sum", "$persons.v"))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> personCount = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", "$persons.v"))));//group all Persons keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(personCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity persons occured: " + counterSpeech + " throughout all the speeches in the session: " + Sessionnr);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Person: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag Miscellanies for a certain session
     * @param Sessionnr
     */
    public static void miscellaniesSessionnr(MongoCollection speech, String Sessionnr){
        List<Document> miscellaniesSort = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind  all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))),//group Miscellanies keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> miscellaniesCount = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))));//group all Miscellanies keys(name) and sum the value(frequenzy)



        MongoCursor<Document> cursor = speech.aggregate(miscellaniesCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity miscellanies occured: " + counterSpeech + " throughout all the speeches in the session: " + Sessionnr);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(miscellaniesSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Miscellanies: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag organisation between two sessions
     * @param speech
     */
    public static void organisationSessionnrs(MongoCollection speech, int firstSession, int secondSession){
        List<Document> organisationSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$organisation.k").append("result", new Document().append("$sum", "$organisation.v"))),//group organisations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> organisationCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind all all organisations
                new Document().append("$addFields", new Document().append("organisation", new Document().append("$objectToArray", "$NamedEntity.Organisations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("organisation", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$organisation")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "organisation.k").append("result", new Document().append("$sum", "$organisation.v"))));//group all organisations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(organisationCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many organisations there are in speeches
            System.out.println("The named entity Organisation occured: " + counterSpeech + " throughout all the speeches in the session between " + firstSession + " and " + secondSession);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(organisationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document organisation = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Organisation: " + organisation.get("_id").toString().replace("#", ".") + " |frequenzy: " + organisation.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag location between two sessions
     */
    public static void locationSessionnrs(MongoCollection speech, int firstSession, int secondSession){
        List<Document> locationSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$locations")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$locations.k").append("result", new Document().append("$sum", "$locations.v"))),//group locations keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count

        List<Document> pipeline = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind all all locations
                new Document().append("$addFields", new Document().append("Locations", new Document().append("$objectToArray", "$NamedEntity.Locations"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("Locations", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$Locations")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "Locations.k").append("result", new Document().append("$sum", "$Locations.v"))));//group all locations keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(pipeline).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity location occured: " + counterSpeech + " throughout all the speeches in the session between " + firstSession + " and " + secondSession);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(locationSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Location: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag person between two sessions
     */
    public static void personSessionnrs(MongoCollection speech, int firstSession, int secondSession){
        List<Document> personSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$persons.k").append("result", new Document().append("$sum", "$persons.v"))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> personCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", "$persons.v"))));//group all Persons keys(name) and sum the value(frequenzy)


        MongoCursor<Document> cursor = speech.aggregate(personCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity persons occured: " + counterSpeech + " throughout all the speeches in the session between " + firstSession + " and " + secondSession);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Person: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * This method returns all the namedentities with the tag Miscellanies between two sessions
     */
    public static void miscellaniesSessionnrs(MongoCollection speech, int firstSession, int secondSession){
        List<Document> miscellaniesSort = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind  all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "$miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))),//group Miscellanies keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count


        List<Document> miscellaniesCount = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind all Miscellanies
                new Document().append("$addFields", new Document().append("miscellanies", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))),//convert them into a new field as an array
                new Document().append("$project", new Document().append("miscellanies", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$miscellanies")),//unwind the new field
                new Document().append("$group", new Document().append("_id", "miscellanies.k").append("result", new Document().append("$sum", "$miscellanies.v"))));//group all Miscellanies keys(name) and sum the value(frequenzy)



        MongoCursor<Document> cursor = speech.aggregate(miscellaniesCount).cursor(); //use the pipline to get desired output
        while(cursor.hasNext()){
            Document doc = cursor.next();
            Integer counterSpeech = (Integer) doc.get("result"); //get the value of how many locations there are in speeches
            System.out.println("The named entity miscellanies occured: " + counterSpeech + " throughout all the speeches in the session between " + firstSession + " and " + secondSession);
        }

        MongoCursor<Document> orgCursor = speech.aggregate(miscellaniesSort).cursor(); //use the pipline to get desired output

        while(orgCursor.hasNext()){
            Document entity = orgCursor.next(); //for every organisations found
            System.out.println("NamedEntity-Miscellanies: " + entity.get("_id").toString().replace("#", ".") + " |frequenzy: " + entity.get("result"));

        }

    }

    /**
     * categorises the sentiment value into positiv% negativ% neutral% for whole corpus
     * @param speech
     */
    public static void sentimentAllSpeeches(MongoCollection speech){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$gt", 0.0))),//get all Sentimentcalue > 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)), //project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0)))); //count the amount of positiv Sentiment values

        List<Document> filterPositivComments = Arrays.asList(
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")), //unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))), // get all Sentiment values > 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)), // project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of positive sentiment values

        List<Document> filterNegativ = Arrays.asList(
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$lt", 0.0))), //get all Sentimentcalue < 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)),//project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0))));//count the amount of negativ Sentiment values

        List<Document> filterNegativComments = Arrays.asList(
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))), // get all Sentiment values < 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)),// project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of negativ sentiment values


        MongoCursor<Document> sentimentPositiv = speech.aggregate(filterPositiv).cursor(); //filter speech collection
        double valuePositiv = 0.0;
        while (sentimentPositiv.hasNext()){
            Document doc = sentimentPositiv.next(); //create document out of the filter result
            valuePositiv = (double) doc.get("result"); //get the amount of of postive speeches
        }


        MongoCursor<Document> commentPositivSentiment = speech.aggregate(filterPositivComments).cursor(); //filter speech collection
        double commentPositiv = 0.0;
        while(commentPositivSentiment.hasNext()){
            Document commentdoc = commentPositivSentiment.next();//create document out of the filter result
            commentPositiv = (double) commentdoc.get("result");//get the amount of of postive comments
        }


        MongoCursor<Document> sentimentNegativ = speech.aggregate(filterNegativ).cursor();//filter speech collection
        double valueNegativ = 0.0;
        while (sentimentNegativ.hasNext()){
            Document doc = sentimentNegativ.next();//create document out of the filter result
            valueNegativ = (double) doc.get("result");//get the amount of negativ speeches
        }


        MongoCursor<Document> commentNegativSentiment = speech.aggregate(filterNegativComments).cursor();//filter speech collection
        double commentNegativ = 0.0;
        while (commentNegativSentiment.hasNext()){
            Document doc = commentNegativSentiment.next();//create document out of the filter result
            commentNegativ = (double) doc.get("result");//get the amount of negativ comments
        }

        //convert all values into % by adding all positiv/negativ values and the remaining of 100% should be neutral
        double positivePercent = (((valuePositiv+commentPositiv)/221901)*100);
        double negativePercent = (((valueNegativ+commentNegativ)/221901)*100);
        double neutralPercent = (100-(positivePercent+negativePercent));

        System.out.println("What is the sentiment distribution across the entire corpus: ");
        System.out.println("Positiv: " + positivePercent + "%");
        System.out.println("Negativ: " + negativePercent + "%");
        System.out.println("Neutral: " + neutralPercent + "%");

    }

    /**
     * categorises the sentiment value into positiv% negativ% neutral% for a certain Speaker
     * @param speech
     */
    public static void sentimentSpeaker(MongoCollection speech, String speaker){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$gt", 0.0))),//get all Sentimentcalue > 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)), //project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0)))); //count the amount of positiv Sentiment values

        List<Document> filterPositivComments = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")), //unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))), // get all Sentiment values > 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)), // project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of positive sentiment values

        List<Document> filterNegativ = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$lt", 0.0))), //get all Sentimentcalue < 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)),//project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0))));//count the amount of negativ Sentiment values

        List<Document> filterNegativComments = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))), // get all Sentiment values < 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)),// project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of negativ sentiment values


        MongoCursor<Document> sentimentPositiv = speech.aggregate(filterPositiv).cursor(); //filter speech collection
        double valuePositiv = 0.0;
        while (sentimentPositiv.hasNext()){
            Document doc = sentimentPositiv.next(); //create document out of the filter result
            if((double) doc.get("result") == 0.0){
                valuePositiv = 0.0;
            }else {
                valuePositiv = (double) doc.get("result"); //get the amount of of postive speeches
            }
        }


        MongoCursor<Document> commentPositivSentiment = speech.aggregate(filterPositivComments).cursor(); //filter speech collection
        double commentPositiv = 0.0;
        while(commentPositivSentiment.hasNext()){
            Document commentdoc = commentPositivSentiment.next();//create document out of the filter result
            if((double) commentdoc.get("result") == 0.0){
                commentPositiv = 0.0;
            }else {
                commentPositiv = (double) commentdoc.get("result");//get the amount of of postive comments
            }
        }


        MongoCursor<Document> sentimentNegativ = speech.aggregate(filterNegativ).cursor();//filter speech collection
        double valueNegativ = 0.0;
        while (sentimentNegativ.hasNext()){
            Document doc = sentimentNegativ.next();//create document out of the filter result
            if((double) doc.get("result") == 0.0){
                valueNegativ = 0.0;
            }else {
                valueNegativ = (double) doc.get("result");//get the amount of negativ speeches
            }
        }


        MongoCursor<Document> commentNegativSentiment = speech.aggregate(filterNegativComments).cursor();//filter speech collection
        double commentNegativ = 0.0;
        while (commentNegativSentiment.hasNext()){
            Document doc = commentNegativSentiment.next();//create document out of the filter result
            if((double) doc.get("result") == 0.0){
                commentNegativ = 0.0;
            }else {
                commentNegativ = (double) doc.get("result");//get the amount of negativ comments
            }
        }

        double speakerSpeechCount = (valueNegativ+valuePositiv);
        double speakerCommentCount = (commentNegativ+commentPositiv);
        double totalCount = (speakerCommentCount+speakerSpeechCount);
        //convert all values into % by adding all positiv/negativ values and the remaining of 100% should be neutral
        double positivePercent = (((valuePositiv+commentPositiv)/totalCount)*100);
        double negativePercent = (((valueNegativ+commentNegativ)/totalCount)*100);
        double neutralPercent = (100-(positivePercent+negativePercent));

        System.out.println("The speaker: " + speaker + " held: " + speakerSpeechCount + " speeches");
        System.out.println("The sentiment distribution across the entire corpus for the speaker: " + speaker + " is: ");
        System.out.println("Positiv: " + positivePercent + "%");
        System.out.println("Negativ: " + negativePercent + "%");
        System.out.println("Neutral: " + neutralPercent + "%");

    }

    /**
     * categorises the sentiment value into positiv% negativ% neutral% for a certain fraction
     * @param speech
     */
    public static void sentimentFraction(MongoCollection speech, String fraction){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$gt", 0.0))),//get all Sentimentcalue > 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)), //project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0)))); //count the amount of positiv Sentiment values

        List<Document> filterPositivComments = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")), //unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))), // get all Sentiment values > 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)), // project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of positive sentiment values

        List<Document> filterNegativ = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$lt", 0.0))), //get all Sentimentcalue < 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)),//project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0))));//count the amount of negativ Sentiment values

        List<Document> filterNegativComments = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))), // get all Sentiment values < 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)),// project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of negativ sentiment values


        MongoCursor<Document> sentimentPositiv = speech.aggregate(filterPositiv).cursor(); //filter speech collection
        double valuePositiv = 0.0;
        while (sentimentPositiv.hasNext()){
            Document doc = sentimentPositiv.next(); //create document out of the filter result
            valuePositiv = (double) doc.get("result"); //get the amount of of postive speeches

        }


        MongoCursor<Document> commentPositivSentiment = speech.aggregate(filterPositivComments).cursor(); //filter speech collection
        double commentPositiv = 0.0;
        while(commentPositivSentiment.hasNext()){
            Document commentdoc = commentPositivSentiment.next();//create document out of the filter result
            commentPositiv = (double) commentdoc.get("result");//get the amount of of postive comments

        }


        MongoCursor<Document> sentimentNegativ = speech.aggregate(filterNegativ).cursor();//filter speech collection
        double valueNegativ = 0.0;
        while (sentimentNegativ.hasNext()){
            Document doc = sentimentNegativ.next();//create document out of the filter result
            valueNegativ = (double) doc.get("result");//get the amount of negativ speeches

        }


        MongoCursor<Document> commentNegativSentiment = speech.aggregate(filterNegativComments).cursor();//filter speech collection
        double commentNegativ = 0.0;
        while (commentNegativSentiment.hasNext()){
            Document doc = commentNegativSentiment.next();//create document out of the filter result
            commentNegativ = (double) doc.get("result");//get the amount of negativ comments
        }

        double speakerSpeechCount = (valueNegativ+valuePositiv);
        double speakerCommentCount = (commentNegativ+commentPositiv);
        double totalCount = (speakerCommentCount+speakerSpeechCount);
        //convert all values into % by adding all positiv/negativ values and the remaining of 100% should be neutral
        double positivePercent = (((valuePositiv+commentPositiv)/totalCount)*100);
        double negativePercent = (((valueNegativ+commentNegativ)/totalCount)*100);
        double neutralPercent = (100-(positivePercent+negativePercent));

        System.out.println("The speakers of the Fraction: " + fraction + " held: " + speakerSpeechCount + " speeches in total");
        System.out.println("The sentiment distribution across the entire corpus for the fraction: " + fraction + " is: ");
        System.out.println("Positiv: " + positivePercent + "%");
        System.out.println("Negativ: " + negativePercent + "%");
        System.out.println("Neutral: " + neutralPercent + "%");

    }

    /**
     * categorises the sentiment value into positiv% negativ% neutral% for a certain Session
     * @param speech
     */
    public static void sentimentSession(MongoCollection speech, String Sessionnr){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$gt", 0.0))),//get all Sentimentcalue > 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)), //project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0)))); //count the amount of positiv Sentiment values

        List<Document> filterPositivComments = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")), //unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))), // get all Sentiment values > 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)), // project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of positive sentiment values

        List<Document> filterNegativ = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$lt", 0.0))), //get all Sentimentcalue < 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)),//project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0))));//count the amount of negativ Sentiment values

        List<Document> filterNegativComments = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))), // get all Sentiment values < 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)),// project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of negativ sentiment values


        MongoCursor<Document> sentimentPositiv = speech.aggregate(filterPositiv).cursor(); //filter speech collection
        double valuePositiv = 0.0;
        while (sentimentPositiv.hasNext()){
            Document doc = sentimentPositiv.next(); //create document out of the filter result
            valuePositiv = (double) doc.get("result"); //get the amount of of postive speeches

        }


        MongoCursor<Document> commentPositivSentiment = speech.aggregate(filterPositivComments).cursor(); //filter speech collection
        double commentPositiv = 0.0;
        while(commentPositivSentiment.hasNext()){
            Document commentdoc = commentPositivSentiment.next();//create document out of the filter result
            commentPositiv = (double) commentdoc.get("result");//get the amount of of postive comments

        }


        MongoCursor<Document> sentimentNegativ = speech.aggregate(filterNegativ).cursor();//filter speech collection
        double valueNegativ = 0.0;
        while (sentimentNegativ.hasNext()){
            Document doc = sentimentNegativ.next();//create document out of the filter result
            valueNegativ = (double) doc.get("result");//get the amount of negativ speeches

        }


        MongoCursor<Document> commentNegativSentiment = speech.aggregate(filterNegativComments).cursor();//filter speech collection
        double commentNegativ = 0.0;
        while (commentNegativSentiment.hasNext()){
            Document doc = commentNegativSentiment.next();//create document out of the filter result
            commentNegativ = (double) doc.get("result");//get the amount of negativ comments
        }

        double speakerSpeechCount = (valueNegativ+valuePositiv);
        double speakerCommentCount = (commentNegativ+commentPositiv);
        double totalCount = (speakerCommentCount+speakerSpeechCount);
        //convert all values into % by adding all positiv/negativ values and the remaining of 100% should be neutral
        double positivePercent = (((valuePositiv+commentPositiv)/totalCount)*100);
        double negativePercent = (((valueNegativ+commentNegativ)/totalCount)*100);
        double neutralPercent = (100-(positivePercent+negativePercent));

        System.out.println("In the Session: " + Sessionnr + " were: " + speakerSpeechCount + " speeches held in total");
        System.out.println("The sentiment distribution across the entire corpus for the Session: " + Sessionnr + " is: ");
        System.out.println("Positiv: " + positivePercent + "%");
        System.out.println("Negativ: " + negativePercent + "%");
        System.out.println("Neutral: " + neutralPercent + "%");

    }

    /**
     * categorises the sentiment value into positiv% negativ% neutral% between two sessions
     * @param speech
     */
    public static void sentimentSessions(MongoCollection speech, int firstSession, int secondSession){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$gt", 0.0))),//get all Sentimentcalue > 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)), //project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0)))); //count the amount of positiv Sentiment values

        List<Document> filterPositivComments = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")), //unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))), // get all Sentiment values > 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)), // project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of positive sentiment values

        List<Document> filterNegativ = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$lt", 0.0))), //get all Sentimentcalue < 0
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)),//project for less loadtime
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0))));//count the amount of negativ Sentiment values

        List<Document> filterNegativComments = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))), // get all Sentiment values < 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)),// project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of negativ sentiment values


        MongoCursor<Document> sentimentPositiv = speech.aggregate(filterPositiv).cursor(); //filter speech collection
        double valuePositiv = 0.0;
        while (sentimentPositiv.hasNext()){
            Document doc = sentimentPositiv.next(); //create document out of the filter result
            valuePositiv = (double) doc.get("result"); //get the amount of of postive speeches

        }


        MongoCursor<Document> commentPositivSentiment = speech.aggregate(filterPositivComments).cursor(); //filter speech collection
        double commentPositiv = 0.0;
        while(commentPositivSentiment.hasNext()){
            Document commentdoc = commentPositivSentiment.next();//create document out of the filter result
            commentPositiv = (double) commentdoc.get("result");//get the amount of of postive comments

        }


        MongoCursor<Document> sentimentNegativ = speech.aggregate(filterNegativ).cursor();//filter speech collection
        double valueNegativ = 0.0;
        while (sentimentNegativ.hasNext()){
            Document doc = sentimentNegativ.next();//create document out of the filter result
            valueNegativ = (double) doc.get("result");//get the amount of negativ speeches

        }


        MongoCursor<Document> commentNegativSentiment = speech.aggregate(filterNegativComments).cursor();//filter speech collection
        double commentNegativ = 0.0;
        while (commentNegativSentiment.hasNext()){
            Document doc = commentNegativSentiment.next();//create document out of the filter result
            commentNegativ = (double) doc.get("result");//get the amount of negativ comments
        }

        double speakerSpeechCount = (valueNegativ+valuePositiv);
        double speakerCommentCount = (commentNegativ+commentPositiv);
        double totalCount = (speakerCommentCount+speakerSpeechCount);
        //convert all values into % by adding all positiv/negativ values and the remaining of 100% should be neutral
        double positivePercent = (((valuePositiv+commentPositiv)/totalCount)*100);
        double negativePercent = (((valueNegativ+commentNegativ)/totalCount)*100);
        double neutralPercent = (100-(positivePercent+negativePercent));

        System.out.println("In between the session: " + firstSession + " and " + secondSession + " were: " + speakerSpeechCount + " speeches held in total");
        System.out.println("The sentiment distribution across the entire corpus between the session: " + firstSession + " and " + secondSession + " is: ");
        System.out.println("Positiv: " + positivePercent + "%");
        System.out.println("Negativ: " + negativePercent + "%");
        System.out.println("Neutral: " + neutralPercent + "%");

    }

    /**
     * This method return the speeches sorted by most positive comments
     * @param speech
     */
    public static void speechMostPositiv(MongoCollection speech){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$comments")), //get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)), //project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))), //get all positive sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))), //calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0))); //sort the amount

        MongoCursor<Document> positiv = speech.aggregate(filterPositiv).cursor(); //filter the collection

        while(positiv.hasNext()){
            Document doc = positiv.next();
            System.out.println("The speech with the ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " had: " + doc.get("result") + " comments");

        }


    }

    /**
     * This method return the speeches sorted by most positive comments for a certain speaker
     * @param speech
     */
    public static void speechMostPositivSpeaker(MongoCollection speech, String speaker){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))),//get all positive sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));

        MongoCursor<Document> positiv = speech.aggregate(filterPositiv).cursor();//filter the collection
        while(positiv.hasNext()){
            Document doc = positiv.next();
            System.out.println("The speaker " + speaker + " had in his speech with the ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " " + doc.get("result") + " positive comments");
        }

    }

    /**
     * This method return the speeches sorted by most positive comments for a certain fraction
     * @param speech
     */
    public static void speechMostPositivFraction(MongoCollection speech, String fraction){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))),//get all positive sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> positiv = speech.aggregate(filterPositiv).cursor();//filter the collection

        while(positiv.hasNext()){
            Document doc = positiv.next();
            System.out.println("The fraction: " + fraction + " member with the speech ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " had " + doc.get("result") + " positive comments");

        }


    }

    /**
     * This method return the speeches sorted by most positive comments for a certain session
     * @param speech
     */
    public static void speechMostPositivSession(MongoCollection speech, String Sessionnr){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))),//get all positive sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> positiv = speech.aggregate(filterPositiv).cursor();//filter the collection
        while(positiv.hasNext()){
            Document doc = positiv.next();
            System.out.println("In the session: " + Sessionnr + " the speaker with the speech ID: " + doc.get("_id")  + " had " + doc.get("result") + " positive comments");
        }

    }

    /**
     * This method return the speeches sorted by most positive comments between two sessions
     * @param speech
     */
    public static void speechMostPositivSessions(MongoCollection speech, int firstSession, int secondSession){
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))),//get all positive sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> positiv = speech.aggregate(filterPositiv).cursor();//filter the collection

        while(positiv.hasNext()){
            Document doc = positiv.next();
            System.out.println("In between the session: " + firstSession + " and " + secondSession + " the speaker with the speech ID: " + doc.get("_id")  + " had " + doc.get("result") + " positive comments");

        }


    }

    /**
     * This method return the speeches sorted by most negative comments
     * @param speech
     */
    public static void speechMostNegative(MongoCollection speech){
        List<Document> filterNegativ = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$comments")), //get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)), //project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))), //get all negative sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))), //calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0))); //sort the amount

        MongoCursor<Document> negative = speech.aggregate(filterNegativ).cursor(); //filter the collection

        while(negative.hasNext()){
            Document doc = negative.next();
            System.out.println("The speech with the ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " had: " + doc.get("result") + " comments");

        }


    }

    /**
     * This method return the speeches sorted by most negative comments for a certain speaker
     * @param speech
     */
    public static void speechMostNegativeSpeaker(MongoCollection speech, String speaker){
        List<Document> filterNegative = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))),//get all negative sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));

        MongoCursor<Document> negative = speech.aggregate(filterNegative).cursor();//filter the collection
        while(negative.hasNext()){
            Document doc = negative.next();
            System.out.println("The speaker " + speaker + " had in his speech with the ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " " + doc.get("result") + " negative comments");
        }

    }

    /**
     * This method return the speeches sorted by most negative comments for a certain fraction
     * @param speech
     */
    public static void speechMostNegativeFraction(MongoCollection speech, String fraction){
        List<Document> filternegative = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))),//get all negative sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> negative = speech.aggregate(filternegative).cursor();//filter the collection

        while(negative.hasNext()){
            Document doc = negative.next();
            System.out.println("The fraction: " + fraction + " member with the speech ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " had " + doc.get("result") + " negative comments");

        }


    }

    /**
     * This method return the speeches sorted by most negative comments for a certain session
     * @param speech
     */
    public static void speechMostNegativeSession(MongoCollection speech, String Sessionnr){
        List<Document> filterNegative = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))),//get all negative sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> negative = speech.aggregate(filterNegative).cursor();//filter the collection
        while(negative.hasNext()){
            Document doc = negative.next();
            System.out.println("In the session: " + Sessionnr + " the speaker with the speech ID: " + doc.get("_id")  + " had " + doc.get("result") + " negative comments");
        }

    }

    /**
     * This method return the speeches sorted by most negative comments between two sessions
     * @param speech
     */
    public static void speechMostNegativeSessions(MongoCollection speech, int firstSession, int secondSession){
        List<Document> filterNegative = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))),//get all negative sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> negative = speech.aggregate(filterNegative).cursor();//filter the collection

        while(negative.hasNext()){
            Document doc = negative.next();
            System.out.println("In between the session: " + firstSession + " and " + secondSession + " the speaker with the speech ID: " + doc.get("_id")  + " had " + doc.get("result") + " negative comments");

        }


    }

    /**
     * This method return the speeches sorted by most Neutral comments
     * @param speech
     */
    public static void speechMostNeutral(MongoCollection speech){
        List<Document> filterNeutral = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$comments")), //get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)), //project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$eq", 0.0))), //get all Neutral sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))), //calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0))); //sort the amount

        MongoCursor<Document> neutral = speech.aggregate(filterNeutral).cursor(); //filter the collection

        while(neutral.hasNext()){
            Document doc = neutral.next();
            System.out.println("The speech with the ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " had: " + doc.get("result") + " comments");

        }


    }

    /**
     * This method return the speeches sorted by most Neutral comments for a certain speaker
     * @param speech
     */
    public static void speechMostNeutralSpeaker(MongoCollection speech, String speaker){
        List<Document> filterNeutral = Arrays.asList(
                new Document().append("$addFields", new Document().append("fullname", new Document().append("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " ", "$speaker.fraction")))),//create a new field for the filter of the speaker
                new Document().append("$match", new Document().append("fullname", new Document().append("$regex", speaker))),//match/filter for a specific speaker
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$eq", 0.0))),//get all neutral sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));

        MongoCursor<Document> neutral = speech.aggregate(filterNeutral).cursor();//filter the collection
        while(neutral.hasNext()){
            Document doc = neutral.next();
            System.out.println("The speaker " + speaker + " had in his speech with the ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " " + doc.get("result") + " neutral comments");
        }

    }

    /**
     * This method return the speeches sorted by most Neutral comments for a certain fraction
     * @param speech
     */
    public static void speechMostNeutralFraction(MongoCollection speech, String fraction){
        List<Document> filterNeutral = Arrays.asList(
                new Document().append("$addFields", new Document().append("fraction", "$speaker.fraction")), //create a new field for the filter of the fraction
                new Document().append("$match", new Document().append("fraction", new Document().append("$regex", fraction))), //match/filter for a specific fraction
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$eq", 0.0))),//get all neutral sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> neutral = speech.aggregate(filterNeutral).cursor();//filter the collection

        while(neutral.hasNext()){
            Document doc = neutral.next();
            System.out.println("The fraction: " + fraction + " member with the speech ID: " + doc.get("_id") + " in the session: " + doc.get("sitzungsnr") + " had " + doc.get("result") + " neutral comments");

        }


    }

    /**
     * This method return the speeches sorted by most Neutral comments for a certain session
     * @param speech
     */
    public static void speechMostNeutralSession(MongoCollection speech, String Sessionnr){
        List<Document> filterNeutral = Arrays.asList(
                new Document().append("$match", new Document().append("sitzungsnr", Sessionnr)),//match and just get the specific session
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))),//get all neutral sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> neutral = speech.aggregate(filterNeutral).cursor();//filter the collection

        while(neutral.hasNext()){
            Document doc = neutral.next();
            System.out.println("In the session: " + Sessionnr + " the speaker with the speech ID: " + doc.get("_id")  + " had " + doc.get("result") + " neutral comments");

        }


    }

    /**
     * This method return the speeches sorted by most Neutral comments between two sessions
     * @param speech
     */
    public static void speechMostNeutralSessions(MongoCollection speech, int firstSession, int secondSession){
        List<Document> filterNeutral = Arrays.asList(
                new Document().append("$addFields", new Document().append("Sitzungsnr", new Document().append("$toInt", "$sitzungsnr"))), //convert all Strings into Integer
                new Document().append("$match", new Document().append("Sitzungsnr", new Document().append("$gte", firstSession).append("$lte", secondSession))), //filter the sessions
                new Document().append("$unwind", new Document().append("path", "$comments")),//get all comments
                new Document().append("$project", new Document().append("_id", 1.0).append("sitzungsnr", 1.0).append("comments.cSentiment", 1.0)),//project for less load time
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))),//get all neutral sentiments
                new Document().append("$group", new Document().append("_id", "$_id").append("sitzungsnr", new Document().append("$first", "$sitzungsnr")).append("result", new Document().append("$sum", 1.0))),//calculate for every speech how many  positive comment they had
                new Document().append("$sort", new Document().append("result", -1.0)));//sort the amount

        MongoCursor<Document> neutral = speech.aggregate(filterNeutral).cursor();//filter the collection

        while(neutral.hasNext()){
            Document doc = neutral.next();
            System.out.println("In between the session: " + firstSession + " and " + secondSession + " the speaker with the speech ID: " + doc.get("_id")  + " had " + doc.get("result") + " neutral comments");

        }


    }

}


