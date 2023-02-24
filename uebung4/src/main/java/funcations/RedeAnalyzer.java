package funcations;


import au.com.bytecode.opencsv.CSVReader;
import com.mongodb.client.MongoCollection;
import data.impl.mongoDB_impl.Comment_MongoDB_Impl;
import data.impl.mongoDB_impl.Speech_MongoDB_Impl;
import database.MongoDBConnectionHandler;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * This Class analyses a Speech and inserts the analysed Document into the MongoDB
 * @author Jawwad Khan
 */
public class RedeAnalyzer {

    private static AnalysisEngine aEngine; // an Emtpy Engine

    /**
     * This method creates an Engine according to the requirements of the Tasksheet
     * @return aEngine
     */
    public static AnalysisEngine nlpPipline(String posMap) {
        AggregateBuilder builder = new AggregateBuilder();
        URL posmap = RedeAnalyzer.class.getClassLoader().getResource(posMap); // to get the Path from the resource file

        try {
            builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                    SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.lehre.texttechnologylab.org/"
            ));

            builder.add(createEngineDescription(GerVaderSentiment.class,
                    GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.lehre.texttechnologylab.org/",
                    GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));

            builder.add(createEngineDescription(LabelAnnotatorDocker.class,
                    LabelAnnotatorDocker.PARAM_FASTTEXT_K, 100,
                    LabelAnnotatorDocker.PARAM_CUTOFF, false,
                    LabelAnnotatorDocker.PARAM_SELECTION, "text",
                    LabelAnnotatorDocker.PARAM_TAGS, "ddc3",
                    LabelAnnotatorDocker.PARAM_USE_LEMMA, true,
                    LabelAnnotatorDocker.PARAM_ADD_POS, true,
                    LabelAnnotatorDocker.PARAM_POSMAP_LOCATION, posmap.getPath(),
                    LabelAnnotatorDocker.PARAM_REMOVE_FUNCTIONWORDS, true,
                    LabelAnnotatorDocker.PARAM_REMOVE_PUNCT, true,
                    LabelAnnotatorDocker.PARAM_REST_ENDPOINT, "http://ddc.lehre.texttechnologylab.org/"
            ));
            aEngine = builder.createAggregate();
        } catch (ResourceInitializationException e) {
            throw new RuntimeException();
        }
        return aEngine;
    }

    /**
     * This methods takes an Document from the DB and analyses this Document through a Pipeline with an Engine and appends
     * the analysed information to the document. In the end we update the Document from the DB
     * Anschließend werden diese in die DB geschoben.
     */
    public static void speechAnalyser (Document doc, MongoCollection sCollection,  Map<String, String> ddc, String posMap) throws UIMAException, IOException {
        try {
            // We convert the Document into a speech object.
            Speech_MongoDB_Impl speech = new Speech_MongoDB_Impl(doc);

            //we create a Cas Object, and give the Cas the text of the speech
            JCas RedeJCas = JCasFactory.createText(speech.getText(), "de");

            //we run the pipeline, with the Cas and Engine.
            SimplePipeline.runPipeline(RedeJCas.getCas(), nlpPipline(posMap));

            //We get the Sentiment value of a speech.
            List<Double> gesamtSentiment = new ArrayList<>(); //empty Array which will contain all sentiments of a speech
            for (Sentence sentence : JCasUtil.select(RedeJCas, Sentence.class)) { //for each sentence of the speech
                for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) { //we get the sentiment value
                    gesamtSentiment.add(sentiment.getSentiment()); // we insert the sentiment value into the arraylist
                }
            }
            double averageSentiment = 0.0; //initiate a double value
            for (double x : gesamtSentiment) { //for each value in the arrayList
                averageSentiment += x; //we sum all the values of in the array List
            }
            averageSentiment = averageSentiment / (double) gesamtSentiment.toArray().length; //we calculate the average of the sentiment of the speech

            List<Token> tokens = new ArrayList<>(); // we get every Token of a speech

            for (Token token : JCasUtil.select(RedeJCas, Token.class)) { //for each token in the speech
                tokens.add(token); //insert into array list
            }
            Document docToken = new Document(); // new Document for tokens
            ArrayList<String> pos = new ArrayList<>(); // arrayList for all pos
            ArrayList<String> lemma = new ArrayList<>();// arrayList for all lemma
            ArrayList<Integer> counter = new ArrayList<>();// arrayList for amount occurence of lemma
            for (int x = 0; x < tokens.size(); x++) { // iterate through every lemma
                Integer Counter = 1; //initiate a counter
                for (int y = x + 1; y < tokens.size(); y++) { // we compare every token with eachother
                    if (tokens.get(x).getLemma().getValue().equals(tokens.get(y).getLemma().getValue())) {
                        //if we find the same lemma value in a speech, we increase the count by one
                        Counter += 1;
                    }

                }
                //if we have not the lemma value in the lemma arraylist we append it into the arraylist
                if (!lemma.contains(tokens.get(x).getLemma().getValue())) {
                    lemma.add(tokens.get(x).getLemma().getValue());//add to arraylist the lemma value
                    pos.add(tokens.get(x).getPos().getCoarseValue()); //add the coarsevalue of the lemma
                    counter.add(Counter);//add the the amount of occurance of the lemma
                }
            }

            ArrayList<String> posName = getPOSName(pos); // we get every pos value in a speech (the category)

            for (int i = 0; i < posName.size(); i++) { // for every Pos category we iterate through the speech
                Document docLema = new Document(); //new document where all the lemma will be appended
                for (int j = 0; j < pos.size(); j++) {
                    if (posName.get(i).equals(pos.get(j))) { //if pos value in speech is the same as the pos category we currently iterate through
                        if (lemma.get(j).contains(".")) { // if a lemma contains a "."
                            String lemmaNoPoint = lemma.get(j).replace(".", "#"); // replace all points with #
                            docLema.append(lemmaNoPoint, counter.get(j)); //append it into the doc with the amount of occurrence
                        } else {
                            docLema.append(lemma.get(j), counter.get(j)); //append it into the doc with the amount of occurrence
                        }
                    }
                }
                docToken.put(posName.get(i), docLema); // append the doc with to the category
            }

            List<String> Person = new ArrayList<>(); //empty ArrayList which will contain all the Person values of namedEntity
            List<Integer> personCounter = new ArrayList<>(); //empty ArrayList which will contain all the Person count values of namedEntity
            List<String> Location = new ArrayList<>(); //empty ArrayList which will contain all the Location values of namedEntity
            List<Integer> locationCounter = new ArrayList<>(); //empty ArrayList which will contain all the Location count values of namedEntity
            List<String> Organisation = new ArrayList<>(); //empty ArrayList which will contain all the Organisation values of namedEntity
            List<Integer> organCounter = new ArrayList<>(); //empty ArrayList which will contain all the Organisation count values of namedEntity
            List<String> Miscellaneous = new ArrayList<>(); //empty ArrayList which will contain all the Miscellaneous values of namedEntity
            List<Integer> miscCounter = new ArrayList<>(); //empty ArrayList which will contain all the Miscellaneous count values of namedEntity

            for (NamedEntity namedEntity : JCasUtil.select(RedeJCas, NamedEntity.class)) { // we get every NamedEntity from the Cas
                Integer count = 1;
                Integer pCount = 1;
                Integer oCount = 1;
                Integer mCount = 1;
                if (namedEntity.getValue().equals("LOC")) { // if the Value is Location
                    if (Location.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                        count += 1;

                    } else {
                        Location.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                    }
                    locationCounter.add(count); // at the end we add the counter into the counter list
                } else if (namedEntity.getValue().equals("PER")) {
                    if (Person.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                        pCount += 1;

                    } else {
                        Person.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                    }
                    personCounter.add(pCount); // at the end we add the counter into the counter list
                } else if (namedEntity.getValue().equals("ORG")) {
                    if (Organisation.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                        oCount += 1;

                    } else {
                        Organisation.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                    }
                    organCounter.add(oCount); // at the end we add the counter into the counter list

                } else if (namedEntity.getValue().equals("MISC")) {
                    if (Miscellaneous.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                        mCount += 1;

                    } else {
                        Miscellaneous.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                    }
                    miscCounter.add(mCount); // at the end we add the counter into the counter list

                }
            }

            Document namedEntites = new Document();
            Document Persons = new Document();
            Document Locations = new Document();
            Document Organisations = new Document();
            Document Miscellanies = new Document();
            for (int i = 0; i < Location.size(); i++) {
                Locations.append(Location.get(i), locationCounter.get(i)); // we append every Location found with his counter to the document
            }
            for (int j = 0; j < Person.size(); j++) {
                Persons.append(Person.get(j), personCounter.get(j)); // we append every Person found with his counter to the document
            }
            for (int x = 0; x < Organisation.size(); x++) {
                Organisations.append(Organisation.get(x), organCounter.get(x)); // we append every Organisation found with his counter to the document
            }
            for (int y = 0; y < Miscellaneous.size(); y++) {
                Miscellanies.append(Miscellaneous.get(y), miscCounter.get(y)); // we append every Miscellaneous found with his counter to the document
            }

            //append all to NamedEntitiy so that we have one big Document
            namedEntites.append("Persons", Persons);
            namedEntites.append("Locations", Locations);
            namedEntites.append("Organisations", Organisations);
            namedEntites.append("Miscellanies", Miscellanies);

            //Document for the ddc Tags
            Document ddc3 = new Document();

            //we get every DDC tag of a speech in a collection
            Collection<CategoryCoveredTagged> categories = JCasUtil.select(RedeJCas, CategoryCoveredTagged.class);
            for(CategoryCoveredTagged category : categories){ //iteratre through every single DDC tag
                Document nameDDC = new Document();
                if(category.getScore() == 1.95313E-8){ //if the value is 1.95313E-8 we set it as 0 for convenience’s sake
                    nameDDC.put(ddc.get(category.getValue().replaceAll("__label_ddc__", "")),0);
                }else{
                    //to get the actual value we have a Map, which contains all the ddc namens and number for the name
                    nameDDC.put(ddc.get(category.getValue().replaceAll("__label_ddc__", "")),category.getScore());
                }
                //at the end we put it in the document
                ddc3.put(category.getValue().replaceAll("__label_ddc__", ""), nameDDC);
            }

            //for every comment we do the same analysis as we did for the speeches
            for(int i = 0; i < speech.getComments().size(); i++){
                // we get all the comments of a speech
                Document docComment = (Document) speech.getComments().get(i);
                //create a new Comments object
                Comment_MongoDB_Impl comment = new Comment_MongoDB_Impl(docComment);

                //create a cas and set the text of of the comment to the cas
                JCas commentCas = JCasFactory.createText(comment.getContent(), "de");

                //run the Pipeline with the Comment cas
                SimplePipeline.runPipeline(commentCas, nlpPipline(posMap));

                //We get the Sentiment value of a speech.
                List<Double> gesamtSentimentComment = new ArrayList<>(); //empty Array which will contain all sentiments of a comment
                for (Sentence sentence : JCasUtil.select(commentCas, Sentence.class)) { //for each sentence of the comment
                    for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)) { //we get the sentiment value
                        gesamtSentimentComment.add(sentiment.getSentiment()); // we insert the sentiment value into the arraylist
                    }
                }
                double averageSentimentComment = 0.0; //initiate a double value
                for (double x : gesamtSentimentComment) { //for each value in the arrayList
                    averageSentimentComment += x; //we sum all the values of in the array List
                }
                averageSentimentComment = averageSentimentComment / (double) gesamtSentimentComment.toArray().length; //we calculate the average of the sentiment of the comment

                List<Token> tokensComments = new ArrayList<>(); // we get every Token of a comment

                for (Token token : JCasUtil.select(commentCas, Token.class)) { //for each token in the comment
                    tokensComments.add(token); //insert into array list
                }
                Document docTokenComments = new Document();
                ArrayList<String> posComment = new ArrayList<>(); // arrayList for all pos
                ArrayList<String> lemmaComment = new ArrayList<>(); // arrayList for all lemma
                ArrayList<Integer> counterComment = new ArrayList<>(); // arrayList for all lemmacount
                for (int x = 0; x < tokensComments.size(); x++) {
                    Integer Counter = 1;
                    for (int y = x + 1; y < tokensComments.size(); y++) {
                        //if the tokens are the same we increase the count
                        if (tokensComments.get(x).getLemma().getValue().equals(tokensComments.get(y).getLemma().getValue())) {
                            Counter += 1;
                        }
                    }
                    //if we do not have the lemma we add all infos of the lemma to the certain list.
                    if (!lemmaComment.contains(tokensComments.get(x).getLemma().getValue())) {
                        lemmaComment.add(tokensComments.get(x).getLemma().getValue());
                        posComment.add(tokensComments.get(x).getPos().getCoarseValue());
                        counterComment.add(Counter);
                    }
                }

                //get all the Pos categories of Pos
                ArrayList<String> posNameComment = getPOSName(posComment);

                for (int c = 0; c < posNameComment.size(); c++) {
                    Document docLema = new Document();
                    for (int j = 0; j < posComment.size(); j++) {
                        //if the pos of the comment is the same as the category we searching
                        if (posNameComment.get(c).equals(posComment.get(j))) {
                            if (lemmaComment.get(j).contains(".")) { //remove all "."
                                String lemmaNoPoint = lemmaComment.get(j).replace(".", "#");
                                docLema.append(lemmaNoPoint, counterComment.get(j)); //put the lemma and the counter to the list
                            } else {
                                //put the lemma and the counter to the list
                                docLema.append(lemmaComment.get(j), counterComment.get(j));
                            }
                        }
                    }
                    //put the category with all the lemma of the category into the list.
                    docTokenComments.put(posNameComment.get(c), docLema);
                }

                List<String> PersonComments = new ArrayList<>();
                List<Integer> personCounterComments = new ArrayList<>();
                List<String> LocationComments = new ArrayList<>();
                List<Integer> locationCounterComments = new ArrayList<>();
                List<String> OrganisationComments = new ArrayList<>();
                List<Integer> organCounterComments = new ArrayList<>();
                List<String> MiscellaneousComments = new ArrayList<>();
                List<Integer> miscCounterComments = new ArrayList<>();


                for (NamedEntity namedEntity : JCasUtil.select(commentCas, NamedEntity.class)) {
                    Integer count = 1;
                    Integer pCount = 1;
                    Integer oCount = 1;
                    Integer mCount = 1;
                    if (namedEntity.getValue().equals("LOC")) {
                        if (LocationComments.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                            count += 1;

                        } else {
                            LocationComments.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                        }
                        locationCounterComments.add(count); // at the end we add the counter into the counter list
                    } else if (namedEntity.getValue().equals("PER")) {
                        if (PersonComments.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                            pCount += 1;

                        } else {
                            PersonComments.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                        }
                        personCounterComments.add(pCount); // at the end we add the counter into the counter list
                    } else if (namedEntity.getValue().equals("ORG")) {
                        if (OrganisationComments.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                            oCount += 1;

                        } else {
                            OrganisationComments.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                        }
                        organCounterComments.add(oCount); // at the end we add the counter into the counter list

                    } else if (namedEntity.getValue().equals("MISC")) {
                        if (MiscellaneousComments.equals(namedEntity.getCoveredText().replace(".", " "))) { //if we already have the Entity we increase the counter by one
                            mCount += 1;

                        } else {
                            MiscellaneousComments.add(namedEntity.getCoveredText().replace(".", " ")); // if not we add it to the list
                        }
                        miscCounterComments.add(mCount); // at the end we add the counter into the counter list

                    }
                }

                Document namedEntitesComments = new Document();
                Document PersonsComments = new Document();
                Document LocationsComments = new Document();
                Document OrganisationsComments = new Document();
                Document MiscellaniesComments = new Document();

                for (int a = 0; a < LocationComments.size(); a++) {
                    LocationsComments.append(LocationComments.get(a), locationCounterComments.get(a)); // we append every Location found with his counter to the document
                }
                for (int j = 0; j < PersonComments.size(); j++) {
                    PersonsComments.append(PersonComments.get(j), personCounterComments.get(j)); // we append every Person found with his counter to the document
                }
                for (int x = 0; x < OrganisationComments.size(); x++) {
                    OrganisationsComments.append(OrganisationComments.get(x), organCounterComments.get(x)); // we append every Organisation found with his counter to the document
                }
                for (int y = 0; y < MiscellaneousComments.size(); y++) {
                    MiscellaniesComments.append(MiscellaneousComments.get(y), miscCounterComments.get(y)); // we append every Miscellaneous found with his counter to the document
                }

                //append all to the entity to one document
                namedEntitesComments.append("Persons", PersonsComments);
                namedEntitesComments.append("Locations", LocationsComments);
                namedEntitesComments.append("Organisations", OrganisationsComments);
                namedEntitesComments.append("Miscellanies", MiscellaniesComments);

                //append all filtered information into the comments
                docComment.append("cSentiment", averageSentimentComment);
                docComment.append("Token", docTokenComments);
                docComment.append("NamedEntitiy", namedEntitesComments);
            }

            // at the end we append all analysed information to the document and update this certain document into the DB

            doc.append("Sentimentwert", averageSentiment);
            doc.append("Token", docToken);
            doc.append("NamedEntity", namedEntites);
            doc.append("DDC3", ddc3);
            doc.append("xmi", HelperFunctions.toXMI(RedeJCas));

            MongoDBConnectionHandler.updateInMongo(sCollection, doc); //Update the document

        } catch (Exception CASError) {
            CASError.printStackTrace();
        }
    }

    /**
     * With this Method we get all the unique Pos values of a speech
     * @param pos
     * @return
     */
    public static ArrayList<String> getPOSName(ArrayList<String> pos){
        Set<String> diffPos = new HashSet<>();
        for(int a = 0; a < pos.size(); a++){
            String namePos = pos.get(a); //for every Pos value of a speech we cast it into a string
            diffPos.add(namePos); //put it into a set, so that we have unique values at the end
        }
        ArrayList<String> singlePos = new ArrayList<>();
        for(Object e : diffPos){ // for all objects in the set,
            singlePos.add(e.toString()); //cast the object to a string and put into a List
        }

        return singlePos; //return the List
    }

    /**
     * This Methiod reads the csv file and return all the values of the csv in form of a Map<S,S>
     * @param ddcPfad
     * @return
     * @throws IOException
     */
    public static Map<String, String> getDDC(String ddcPfad) throws IOException {
        List<List<String>> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(ddcPfad))) { //reads the csv file through a given path
            String[] values = null;
            while ((values = csvReader.readNext()) != null) { //reads all the lines of the csv file
                records.add(Arrays.asList(values)); //puts the values into an arraylist
            }
        }
        Map<String , String> csv = new HashMap<>();
        for(List<String> s : records){ //for each entry we put it into the Map
            if(s.get(0).equals("")){ //we read a space we skip

            }else {
                //String contains the name of the number -> Politikwissenschaftn
                String categ = s.get(0).substring(3).replace(".", "");

                //put all into the values in the form -> 320 = Informatik into the csv
                csv.put(s.get(0).substring(0, 3), categ.replace("\t", ""));
            }
        }
        return csv; // return the csv Map
    }


}






