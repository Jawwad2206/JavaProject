package ui;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import data.Speaker;
import data.impl.mongoDB_impl.Speaker_MongoDB_Impl;
import data.impl.mongoDB_impl.Speech_MongoDB_Impl;
import database.MongoDBConnectionHandler;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.S;
import edu.washington.cs.knowitall.logic.Expression;
import exceptions.WrongInputException;
import funcations.HelperFunctions;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.bson.Document;
import org.json.JSONException;
import org.xml.sax.SAXException;
import rest.RestHelper.Helper;
import rest.Routs;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static spark.Spark.after;
import static spark.Spark.options;

public class UserInterfaceSpark {
    Boolean CLOSE; // Default Value is fault (used) for while Loops
    Boolean CLOSING;// Default Value is fault (used) for while Loops

    public static void main5(String txtpath) throws IOException, ParserConfigurationException, WrongInputException, SAXException, JSONException {
        MongoDatabase database = MongoDBConnectionHandler.dbConnector(txtpath); // we connect with the MongoDB
        MongoCollection speeches = database.getCollection("speech"); // we get the desired collection
        MongoCollection deputy = database.getCollection("stammdatenblatt"); // we get the desired collection
        UserInterfaceSpark menuForSpark = new UserInterfaceSpark();
        menuForSpark.printHeader();
        menuForSpark.MainMenu(speeches, deputy);
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
     * This is the MainMenu for the Rest.
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public void MainMenu(MongoCollection speeches, MongoCollection deputies) throws ParserConfigurationException, IOException, SAXException, WrongInputException, JSONException {
        System.out.println("All XML Files were sucessfully found!");
        System.out.println("|----------Welcome to the Plenarprotocol App----------|");
        CLOSE = false;
        while (!CLOSE) {
            System.out.println("\nBelow you will see some Actions this Programm can perform");
            System.out.println("Please press the one of the following Numbers to perfrom an action: ");
            System.out.println("1. Start Rest\n");
            System.out.println("2. End the Programm\n");
            int CHOICE = HelperFunctions.integerInputs(1, 2);
            doMenu(CHOICE, speeches, deputies); //Starts the Menu with the given parameters
        }
    }
    /**
     * Through the input entered from the getInput one of the following task will be performed.
     * @param CHOICE
     */
    public void doMenu(int CHOICE, MongoCollection speeches, MongoCollection deputies) throws WrongInputException, IOException, JSONException {
        switch (CHOICE) {
            case 1:
                Task2Menu(speeches, deputies); //starts the Menu for the second task
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
     * Main Menu for the Rest.
     */
    public void Task2Menu(MongoCollection speeches, MongoCollection deputies) throws WrongInputException, IOException, JSONException {
        CLOSING = false;
        while(!CLOSING) {
            System.out.println("\nBelow you will see some actions which you can perform");
            System.out.println("1. Start Rest for all deputies,speech,comments");
            System.out.println("2. Start Rest for Task 2a");
            System.out.println("3. Start Rest for Task 2b");
            System.out.println("4.Return to menu");
            int CHOICE = HelperFunctions.integerInputs(1, 4);
            mainMenuTask2(CHOICE, speeches, deputies); // starts one of the task from above
        }
    }
    public static void enableCORS () {

        options("/speech", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "GET");
            // Note: this may or may not be necessary in your particular application
            response.type("application/json;charset=UTF-8");
        });
    }


    /**
     * Main Menu for Rest.
     * @param CHOICE
     */
    public void mainMenuTask2(int CHOICE, MongoCollection speeches, MongoCollection deputies) throws WrongInputException, IOException, JSONException {
        switch (CHOICE){
            case 1:
                ArrayList<Speaker> dep = Helper.deputy(deputies);
                Routs.speakerTemplate(dep);
                enableCORS();
                System.out.println("To see the Deputylist, you have to open the Link shown below");
                System.out.println("http://localhost:4567/deputy");
                System.out.println("\n");
                //Routs.speechIDTemplate(speeches);
                Routs.speechShow(speeches);
                System.out.println("To see a specific speech, you have to open the Link shown below");
                System.out.println("http://localhost:4567/speech");
                System.out.println("!Important!Has to start with 'ID[number]' -> :id = ID19100300 or any oder valid speechID");
                Routs.commentIDTemplate(speeches);
                System.out.println("To see a specific speech, you have to open the Link shown below");
                System.out.println("http://localhost:4567/comment");
                System.out.println("!Important!Has to start with 'ID[number]' -> :id = ID19100300 or any oder valid speechID");
                break;
            case 2:
                Routs.task2a(speeches);
                System.out.println("http://localhost:4567/2a");
                break;
            case 3:
                Routs.sentimentBarChart(speeches);
                Routs.sentimentComments(speeches);
                Routs.NamedEntity(speeches);
                Routs.chartsFTL(speeches);
                Routs.Lemma(speeches);
                Routs.NamedEntity(speeches);
                System.out.println("http://localhost:4567/charts");
                break;
            case 4:
                CLOSING = true;
                break;
            default:
                System.out.println("Wrong Input! Please enter a number between 1 and 6");
        }
    }

}
