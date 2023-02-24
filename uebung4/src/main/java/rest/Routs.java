package rest;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import data.Speaker;
import data.Speech;
import data.impl.mongoDB_impl.Speaker_MongoDB_Impl;
import data.impl.mongoDB_impl.Speech_MongoDB_Impl;
import database.MongoDBConnectionHandler;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.O;
import freemarker.template.Configuration;
import jdk.nashorn.internal.parser.JSONParser;
import net.arnx.jsonic.JSON;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.cypher.internal.frontend.v3_2.phases.Do;
import rest.RestHelper.Helper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import javax.json.Json;
import javax.print.Doc;
import static com.mongodb.client.model.Filters.eq;
import static java.lang.Math.round;
import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


/**
 * This class creates all the Routes for the localhost
 */
public class Routs {

    public static Configuration cf = Configuration.getDefaultConfiguration();

    /**
     * This method creates the localhost for all the deputies
     * @param deputy
     * @throws IOException
     */
    public static void speakerTemplate(ArrayList<Speaker> deputy) throws IOException {

        cf.setDirectoryForTemplateLoading(new File("src/templates"));

        get("/deputy", "text/html", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();

            attributes.put("title", "DeputyList");
            attributes.put("speakers", deputy);

            return new ModelAndView(attributes, "speaker.ftl");

        }, new FreeMarkerEngine(cf));

    }

    /**
     * This method creates a localhost for a single Deputy
     * @param deputies
     * @throws IOException
     */
    public static void speakerIDTemplate(MongoCollection deputies) throws IOException {
        cf.setDirectoryForTemplateLoading(new File("src/templates"));

        get("/deputy","text/html", (request, response) ->{
            Document doc = Helper.speeches(deputies, request, response);
            Map<String, Object> localhost = new HashMap<>();
            localhost.put("title", "Deputy");
            localhost.put("ID", doc.get("_id"));
            localhost.put("name", doc.get("firstname") + " " + doc.get("lastname"));
            localhost.put("party", doc.get("party"));
            localhost.put("fraction", doc.get("fraction"));
            localhost.put("speakerID", doc.get("_id"));
            return new ModelAndView(localhost, "singleSpeaker.ftl");

        }, new FreeMarkerEngine(cf));

    }

    /**
     * Speech show.
     *
     */
    public static void speechShow(MongoCollection speech){
        get("/speech", (request, response) -> {
            Speech_MongoDB_Impl Text = new Speech_MongoDB_Impl(Helper.speeches(speech, request, response));
            Document doc = Helper.speeches(speech, request, response);
            Document speaker = (Document) doc.get("speaker");
            JSONObject json = new JSONObject();
            json.put("ID", Text.getSpeechID()).put("Speaker", speaker.get("_id")).put("Text", Text.getText());

            return json;

        });
        System.out.println("Link fürs Element im Browser: http://localhost:4567/speech");
    }

    /**
     * creates a localhost for a speech
     * @param speech
     * @throws IOException
     */
    public static void speechIDTemplate (MongoCollection speech) throws IOException {

    cf.setDirectoryForTemplateLoading(new File("src/templates"));

    get("/speech", (request, response) -> {
        Speech_MongoDB_Impl Text = new Speech_MongoDB_Impl(Helper.speeches(speech, request, response));
        Document doc = Helper.speeches(speech, request, response);
        Document speaker = (Document) doc.get("speaker");
        Map<String, Object> localhost = new HashMap<>();
        localhost.put("ID", Text.getSpeechID());
        localhost.put("name", speaker.get("firstname") + " " + speaker.get("lastname") );
        localhost.put("party", speaker.get("party"));
        localhost.put("fraction", speaker.get("fraction"));
        localhost.put("speakerID", speaker.get("_id"));
        localhost.put("speechContent", Text.getText());
        return new ModelAndView(localhost, "speech.ftl");

    }, new FreeMarkerEngine(cf));

    }

    /**
     * creates a localhost for comments
     * @param speech
     * @throws IOException
     */
    public static void commentIDTemplate(MongoCollection speech) throws IOException {

        cf.setDirectoryForTemplateLoading(new File("src/templates"));

        get("/comment", (request, response) -> {
            ArrayList<Object> comment = Helper.commentsID(speech, request, response);
            String sID = (String) comment.get(0);
            ArrayList<Object> allcomments = new ArrayList<>();
            for(int i = 1; i< comment.size(); i++){
                allcomments.add(comment.get(i));
            }
            Map<String, Object> localhost = new HashMap<>();
            localhost.put("ID", sID);
            localhost.put("comments", allcomments);
            return new ModelAndView(localhost, "comment.ftl");

        }, new FreeMarkerEngine(cf));
    }

    /**
     * creates a localhost for Task 2a
     * @param speech
     * @throws IOException
     */
    public static void task2a(MongoCollection speech) throws IOException {
        cf.setDirectoryForTemplateLoading(new File("src/templates"));

        get("/2a", (request, response) -> {
            Document totalSpeeches = Helper.countSpeech(speech);
            Document totalComments = Helper.countComments(speech);
            ArrayList<Document> topSpeakers = Helper.topSpeaker(speech);
            Map<String, Object> localhost = new HashMap<>();
            localhost.put("topSpeaker", topSpeakers.get(0).get("_id") + " " + topSpeakers.get(0).get("count"));
            localhost.put("topSpeaker1", topSpeakers.get(1).get("_id") + " " + topSpeakers.get(0).get("count"));
            localhost.put("topSpeaker2", topSpeakers.get(2).get("_id") + " " + topSpeakers.get(0).get("count"));
            localhost.put("speechCount", totalSpeeches.get("content"));
            localhost.put("commentsCount", totalComments.get("comments"));
            return new ModelAndView(localhost, "2a.ftl");

        }, new FreeMarkerEngine(cf));
    }

    /**
     * creates a localhost for the sentiment Values.
     * @param speech
     */
    public static void sentimentBarChart(MongoCollection speech){
        get("/sentimentSpeech", (request, response) -> {
                JSONObject sentimentValues = Helper.sentimentValues(speech);
                return sentimentValues;
        });
    }

    /**
     * creates a localhost for the sentiment values for the comments
     * @param speech
     */
    public static void sentimentComments(MongoCollection speech){
        get("/sentimentComments", (request, response) -> {
            JSONObject sentimentValues = Helper.sentimenComment(speech);
            return sentimentValues;
        });
    }
    /**
     * creates a localhost for the NamedEntity values for the comments
     * @param speech
     */
    public static void NamedEntity(MongoCollection speech){
        get("/NamedEntity", (request, response) -> {
            JSONObject NamedEntity = new JSONObject();
            NamedEntity.put("Person",Helper.namedEntityPerson(speech));
            NamedEntity.put("Orga",Helper.namedEntityOrganisation(speech));
            NamedEntity.put("Misc",Helper.namedEntityMisc(speech));
            NamedEntity.put("Loca",Helper.namedEntityLocations(speech));
            return NamedEntity;
        });
    }
    /**
     * creates a localhost for the sentiment values for the comments
     * @param speech
     */
    public static void Lemma(MongoCollection speech){
        get("/lemma", (request, response) -> {
            JSONObject lemma = new JSONObject();
            lemma.put("NOUN", Helper.lemmaCommentNOUN(speech));
            lemma.put("DET", Helper.lemmaCommentDET(speech));
            lemma.put("ADV", Helper.lemmaCommentADV(speech));
            lemma.put("VERB", Helper.lemmaCommentVERB(speech));
            lemma.put("PROPN", Helper.lemmaCommentPRON(speech));
            lemma.put("PUNCT", Helper.lemmaCommentPUNCT(speech));
            lemma.put("ADJ", Helper.lemmaCommentADJ(speech));
            lemma.put("CONJ", Helper.lemmaCommentCONJ(speech));
            lemma.put("NUM", Helper.lemmaCommentNUM(speech));
            lemma.put("X", Helper.lemmaCommentX(speech));
            lemma.put("ADP", Helper.lemmaCommentADP(speech));
            return lemma;
        });
    }

    public static void chartsFTL(MongoCollection speech) throws IOException {
        cf.setDirectoryForTemplateLoading(new File("src/templates"));
        get("/charts", (request, response) -> {
            Map<String, Object> localhost = new HashMap<>();
            localhost.put("title", "Charts");
            localhost.put("json", Helper.sentimentValues(speech));
            localhost.put("comments" ,Helper.sentimenComment(speech));
            localhost.put("NOUN", Helper.lemmaCommentNOUN(speech));
            localhost.put("DET", Helper.lemmaCommentDET(speech));
            localhost.put("ADV", Helper.lemmaCommentADV(speech));
            localhost.put("VERB", Helper.lemmaCommentVERB(speech));
            localhost.put("PROPN", Helper.lemmaCommentPRON(speech));
            localhost.put("PUNCT", Helper.lemmaCommentPUNCT(speech));
            localhost.put("ADJ", Helper.lemmaCommentADJ(speech));
            localhost.put("CONJ", Helper.lemmaCommentCONJ(speech));
            localhost.put("NUM", Helper.lemmaCommentNUM(speech));
            localhost.put("X", Helper.lemmaCommentX(speech));
            localhost.put("ADP", Helper.lemmaCommentADP(speech));
            localhost.put("Pers", Helper.namedEntityPerson(speech));
            localhost.put("Org", Helper.namedEntityOrganisation(speech));
            localhost.put("MISC", Helper.namedEntityMisc(speech));
            localhost.put("LOC", Helper.namedEntityLocations(speech));

            return new ModelAndView(localhost, "Charts.ftl");

        }, new FreeMarkerEngine(cf));
    }
}

