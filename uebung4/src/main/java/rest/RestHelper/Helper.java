package rest.RestHelper;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import data.Speaker;
import data.Speech;
import data.impl.mongoDB_impl.Comment_MongoDB_Impl;
import data.impl.mongoDB_impl.Speaker_MongoDB_Impl;
import data.impl.mongoDB_impl.Speech_MongoDB_Impl;
import database.MongoDBConnectionHandler;
import org.apache.commons.math3.util.Precision;
import org.bson.Document;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import org.json.JSONArray;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Helper {
    /**
     * This Method gets all the Deputies from the DB
     * @param deputy
     * @return
     */
    public static ArrayList<Speaker> deputy(MongoCollection deputy){

        ArrayList<Speaker> rSet = new ArrayList<>();
        List<Document> sortFirstName = Arrays.asList( //sort all the deputies by firstname
                new Document().append("$sort", new Document().append("firstname", 1.0)));
        MongoCursor<Document> rDocuments = deputy.aggregate(sortFirstName).cursor(); //go through every Document
        rDocuments.forEachRemaining(d->{
            rSet.add(new Speaker_MongoDB_Impl(d)); //cast every document from cursor into a Speaker Object and add to the list.
        });

        return rSet; //contains all the deputies

    }

    /**
     * Get a speech by entering a speech id
     * @param speech
     * @param request
     * @param response
     * @return
     */
    public static Document speeches(MongoCollection speech, Request request, Response response){
        String sID = request.queryParams().contains("id") ? request.queryParams("id") : "ID19100100"; //request param for the localhost
        Document query = new Document(); //query Document
        query.put("_id", sID.replace("?","")); //add the searching id into the document
        Document doc = MongoDBConnectionHandler.findInMongo(speech, query).first(); //search the document by id
        return doc;
    }

    /**
     * Get all the comments of a speech by id
     * @param speech
     * @param request
     * @param response
     * @return
     */
    public static ArrayList<Object> commentsID(MongoCollection speech, Request request, Response response){
        ArrayList<Object> comments = new ArrayList<>();
        String sID = request.queryParams().contains("speakerId") ? request.queryParams("speakerId") : "ID19100100"; //request param for the localhost
        comments.add(sID);
        Document doc;
        Document query = new Document();//query Document
        query.put("_id", sID);//add the searching id into the document
        doc = MongoDBConnectionHandler.findInMongo(speech, query).first();//search the document by id
        ArrayList<Document> comment = (ArrayList<Document>) doc.get("comments"); //cast the document into a arraylist
        for(int x = 0; x < comment.size(); x++){//go through every comment
            comments.add(comment.get(x).get("comment"));//get the content of the comment
        }
        return comments;
    }

    /**
     * count the amount of speeches in the collection speech
     * @param speech
     * @return
     */
    public static Document countSpeech(MongoCollection speech){
        Document doc = new Document();
        List<Document> countSpeeches = Arrays.asList(
                new Document("$project", new Document("content", 1L)),//project less load time
                new Document("$count", "content"));//count the amount of speeches in the collection "speech"
        MongoCursor<Document> cursor = speech.aggregate(countSpeeches).allowDiskUse(true).cursor();//use the filter on the pipeline
        while(cursor.hasNext()){ //as long as we have documents, we create a new doc
            doc = cursor.next();
        }
        return doc;
    }

    /**
     * count the amount of comments in the collection speech
     * @param speech
     * @return
     */
    public static Document countComments(MongoCollection speech){
        Document doc = new Document();
        List<Document> countComments = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),//unwind all the comments
                new Document("$project", new Document("comments", 1L)),//project less load time
                new Document("$count", "comments"));//count the amount of speeches in the collection "speech"
        MongoCursor<Document> cursor = speech.aggregate(countComments).allowDiskUse(true).cursor();//use the filter on the pipeline
        while(cursor.hasNext()){//as long as we have documents, we create a new doc
            doc = cursor.next();
        }
        return doc;
    }

    /**
     * Filter to get the Top 3 speaker
     * @param speech
     * @return
     */
    public static ArrayList<Document> topSpeaker(MongoCollection speech){
        Document doc;
        ArrayList<Document> top = new ArrayList<>();
        List<Document> topSpeeches =  Arrays.asList(
                new Document("$addFields", new Document("fullname", new Document("$concat", Arrays.asList("$speaker.firstname", " ", "$speaker.lastname", " Fraktion ", "$speaker.fraction", " SpeakerID: ", "$speaker._id")))),//create a new field which contains all the information for the speaker
                new Document("$group", new Document("_id", "$fullname").append("count", new Document("$sum", 1L))), //group by fullname and count how many speeches the speaker held
                new Document("$sort", new Document("count", -1L)), new Document("$limit", 3L)); //get only the top 3 speakers
        MongoCursor<Document> cursor = speech.aggregate(topSpeeches).allowDiskUse(true).cursor();//use the aggregate pipeline to get the desired output
        while(cursor.hasNext()){//as long as we have documents, we create a new doc
            doc = cursor.next();
            top.add(doc);//add the document to the arraylist
        }
        return top;
    }

    /**
     * Get the sentiment values of all speeches.
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject sentimentValues(MongoCollection speech) throws JSONException {
        List<Document> filterPositiv = Arrays.asList(
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)), //project for less loadtime
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$gt", 0.0))),//get all Sentimentcalue > 0

                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0)))); //count the amount of positiv Sentiment values


        List<Document> filterNegativ = Arrays.asList(
                new Document().append("$project", new Document().append("Sentimentwert", 1.0)),//project for less loadtime
                new Document().append("$match", new Document().append("Sentimentwert", new Document().append("$lt", 0.0))), //get all Sentimentcalue < 0
                new Document().append("$group", new Document().append("_id", "Sentimentwert").append("result", new Document().append("$sum", 1.0))));//count the amount of negativ Sentiment values


        MongoCursor<Document> sentimentPositiv = speech.aggregate(filterPositiv).cursor(); //filter speech collection
        double valuePositiv = 0.0;
        while (sentimentPositiv.hasNext()){
            Document doc = sentimentPositiv.next(); //create document out of the filter result
            valuePositiv = (double) doc.get("result"); //get the amount of postive speeches
        }

        MongoCursor<Document> sentimentNegativ = speech.aggregate(filterNegativ).cursor();//filter speech collection
        double valueNegativ = 0.0;
        while (sentimentNegativ.hasNext()){
            Document doc = sentimentNegativ.next();//create document out of the filter result
            valueNegativ = (double) doc.get("result");//get the amount of negativ speeches
        }

        //convert all values into % by adding all positiv/negativ values and the remaining of 100% should be neutral
        double positivePercent = ((valuePositiv)/25200)*100;
        double negativePercent = ((valueNegativ)/25200)*100;
        double neutralPercent = (100-(positivePercent+negativePercent));
        ArrayList sentimentValues = new ArrayList();
        sentimentValues.add(positivePercent);
        sentimentValues.add(negativePercent);
        sentimentValues.add(neutralPercent);
        JSONObject output = new JSONObject();
        for(int i = 0; i<sentimentValues.size(); i++){
            if(String.valueOf(i).equals("0")){
                Number pos1 = Precision.round((Double) sentimentValues.get(i), 2);
                String sPositive = pos1.toString();
                output.put("postive", sPositive);
            }
            if(String.valueOf(i).equals("1")){
                Number pos1 = Precision.round((Double) sentimentValues.get(i), 2);
                String sPositive = pos1.toString();
                output.put("negativ", sPositive);
            }
            if(String.valueOf(i).equals("2")){
                Number pos1 = Precision.round((Double) sentimentValues.get(i), 2);
                String sPositive = pos1.toString();
                output.put("neutral", sPositive);
            }
        }
        return output;
    }

    /**
     * Get the sentiment values of all comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject sentimenComment(MongoCollection speech) throws JSONException {
        List<Document> filterPositivComments = Arrays.asList(
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")), //unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$gt", 0.0))), // get all Sentiment values > 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)), // project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of positive sentiment values


        List<Document> filterNegativComments = Arrays.asList(
                new Document().append("$project", new Document().append("comments", 1.0)),//project comments for less load time
                new Document().append("$unwind", new Document().append("path", "$comments")),//unwind all comments
                new Document().append("$match", new Document().append("comments.cSentiment", new Document().append("$lt", 0.0))), // get all Sentiment values < 0
                new Document().append("$project", new Document().append("comments.cSentiment", 1.0)),// project all sentiment values for less load time
                new Document().append("$group", new Document().append("_id", "comments.cSentiment").append("result", new Document().append("$sum", 1.0)))); // count the amount of negativ sentiment values


        MongoCursor<Document> sentimentPositiv = speech.aggregate(filterPositivComments).cursor(); //filter speech collection
        double valuePositiv = 0.0;
        while (sentimentPositiv.hasNext()){
            Document doc = sentimentPositiv.next(); //create document out of the filter result
            valuePositiv = (double) doc.get("result"); //get the amount of postive speeches
        }

        MongoCursor<Document> sentimentNegativ = speech.aggregate(filterNegativComments).cursor();//filter speech collection
        double valueNegativ = 0.0;
        while (sentimentNegativ.hasNext()){
            Document doc = sentimentNegativ.next();//create document out of the filter result
            valueNegativ = (double) doc.get("result");//get the amount of negativ speeches
        }

        //convert all values into % by adding all positiv/negativ values and the remaining of 100% should be neutral
        double positivePercent = ((valuePositiv)/196701)*100;
        double negativePercent = (((valueNegativ)/196701)*100);
        double neutralPercent = (100-(positivePercent+negativePercent));
        ArrayList sentimentValues = new ArrayList();
        sentimentValues.add(positivePercent);
        sentimentValues.add(negativePercent);
        sentimentValues.add(neutralPercent);
        JSONObject output = new JSONObject();
        for(int i = 0; i<sentimentValues.size(); i++){
            if(String.valueOf(i).equals("0")){
                Number pos1 = Precision.round((Double) sentimentValues.get(i), 2);
                String sPositive = pos1.toString();
                output.put("postive", sPositive);
            }
            if(String.valueOf(i).equals("1")){
                Number pos1 = Precision.round((Double) sentimentValues.get(i), 2);
                String sPositive = pos1.toString();
                output.put("negativ", sPositive);
            }
            if(String.valueOf(i).equals("2")){
                Number pos1 = Precision.round((Double) sentimentValues.get(i), 2);
                String sPositive = pos1.toString();
                output.put("neutral", sPositive);
            }

        }
        return output;


    }

    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentNOUN(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.NOUN")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.NOUN"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));



        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }

        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentDET(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.DET")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.DET"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentADV(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.ADV")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.ADV"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentVERB(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.VERB")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.VERB"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentPRON(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.PRON")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.PRON"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentPUNCT(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.PUNCT")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.PUNCT"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */

    public static JSONObject lemmaCommentADJ(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.ADJ")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.ADJ"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentCONJ(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.CONJ")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.CONJ"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentNUM(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.NUM")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.NUM"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentX(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.X")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.X"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }
    /**
     * Get all Lemmata Noun for the comments
     * @param speech
     * @return
     * @throws JSONException
     */
    public static JSONObject lemmaCommentADP(MongoCollection speech) throws JSONException {
        JSONObject allLemma = new JSONObject();
        List<Document> filterNoun = Arrays.asList(
                new Document("$unwind", new Document("path", "$comments")),
                new Document("$unwind", new Document("path", "$comments.Token")),
                new Document("$unwind", new Document("path", "$comments.Token.ADP")),
                new Document("$addFields", new Document("allNOUN", new Document("$objectToArray", "$comments.Token.ADP"))),
                new Document("$project", new Document("allNOUN", 1L)),
                new Document("$unwind", new Document("path", "$allNOUN")),
                new Document("$group", new Document("_id", "allNOUN.k").append("result", new Document("$sum", 1L))));

        MongoCursor<Document> allNOUN = speech.aggregate(filterNoun).cursor(); //filter speech collection
        Document doc = new Document();
        JSONObject json = new JSONObject();
        while (allNOUN.hasNext()) {
            doc = allNOUN.next();
            allLemma.put(doc.getString("_id"), doc.get("result"));
        }
        System.out.println(allLemma);
        return allLemma;
    }

    /**
     * This method gets all the namedentities with the tag Person from the comments and speeches
     * @param speech
     */
    public static JSONObject namedEntityLocations(MongoCollection speech) throws JSONException {
        List<Document> personSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Locations")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Locations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", 1L))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count
        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output
        JSONObject allLemma = new JSONObject();
        while(orgCursor.hasNext()){
            Document person = orgCursor.next(); //for every Persons found
            allLemma.put(person.getString("_id"), person.get("result"));
            System.out.println(allLemma);

        }
        return allLemma;
    }
    /**
     * This method gets all the namedentities with the tag Person from the comments and speeches
     * @param speech
     */
    public static JSONObject namedEntityPerson(MongoCollection speech) throws JSONException {
        List<Document> personSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Persons")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Persons"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", 1L))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count
        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output
        JSONObject allLemma = new JSONObject();
        while(orgCursor.hasNext()){
            Document person = orgCursor.next(); //for every Persons found
            allLemma.put(person.getString("_id"), person.get("result"));
            System.out.println(allLemma);

        }
        return allLemma;
    }
    /**
     * This method gets all the namedentities with the tag Person from the comments and speeches
     * @param speech
     */
    public static JSONObject namedEntityMisc(MongoCollection speech) throws JSONException {
        List<Document> personSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Miscellanies")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Miscellanies"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", 1L))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count
        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output
        JSONObject allLemma = new JSONObject();
        while(orgCursor.hasNext()){
            Document person = orgCursor.next(); //for every Persons found
            allLemma.put(person.getString("_id"), person.get("result"));

        }
        return allLemma;
    }
    /**
     * This method gets all the namedentities with the tag Person from the comments and speeches
     * @param speech
     */
    public static JSONObject namedEntityOrganisation(MongoCollection speech) throws JSONException {
        List<Document> personSort = Arrays.asList(
                new Document().append("$unwind", new Document().append("path", "$NamedEntity")),//unwind the namedentity collum
                new Document().append("$unwind", new Document().append("path", "$NamedEntity.Organisations")),//unwind  all Persons
                new Document().append("$addFields", new Document().append("persons", new Document().append("$objectToArray", "$NamedEntity.Organisations"))), //convert them into a new field as an array
                new Document().append("$project", new Document().append("persons", 1.0)),//project only the new field for less load time
                new Document().append("$unwind", new Document().append("path", "$persons")), //unwind the new field
                new Document().append("$group", new Document().append("_id", "persons.k").append("result", new Document().append("$sum", 1L))),//group Persons keys(name) and sum the value(frequenzy)
                new Document().append("$sort", new Document().append("result", -1.0))); //all the values by there count
        MongoCursor<Document> orgCursor = speech.aggregate(personSort).cursor(); //use the pipline to get desired output
        JSONObject allLemma = new JSONObject();
        while(orgCursor.hasNext()){
            Document person = orgCursor.next(); //for every Persons found
            allLemma.put(person.getString("_id"), person.get("result"));

        }
        return allLemma;
    }

}
