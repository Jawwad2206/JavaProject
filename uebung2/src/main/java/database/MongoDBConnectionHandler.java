package database;


import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This class contains all the helper Functions to work with a MongoDB
 * @author Jawwad Khan
 */
public class MongoDBConnectionHandler {

    /**
     * This Method connects with the DB with a certain collection
     * @param txtPath
     * @return
     * @throws IOException
     */
    public static MongoDatabase dbConnector(String txtPath) throws IOException {

        FileInputStream path = new FileInputStream(txtPath); //inputstream for the txt with login date

        Properties property = new Properties(); //creating a new property

        property.load(path);

        String remote_host = property.getProperty("remote_host"); // from the txt we get the key remote host
        String remote_database = property.getProperty("remote_database");// from the txt we get the key remote_database
        String remote_user = property.getProperty("remote_user");// from the txt we get the key remote_user
        String remote_password = property.getProperty("remote_password");// from the txt we get the key remote_password
        Integer remote_port = Integer.valueOf(property.getProperty("remote_port"));// from the txt we get the key remote_port
        String remote_collection = property.getProperty("remote_collection");// from the txt we get the key remote_collection

        MongoCredential credential = MongoCredential.createCredential(remote_user, remote_database, //Connecting with the MongoDB (aus den Vorlesungsfolien)
                remote_password.toCharArray());
        MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(remote_host,
                (remote_port)), Arrays.asList(credential));
        MongoDatabase database = mongoClient.getDatabase(remote_database);

        return database; //returning the collection with which we connected
    }

    /**
     * Functions which inserts Documents into the DB into a collection
     * @param txtPath
     * @param doc
     * @param cCollection
     * @throws IOException
     */
    public static void dbInserter(String txtPath, List<Document> doc, String cCollection) throws IOException {
        MongoDatabase database = MongoDBConnectionHandler.dbConnector(txtPath); //connecting with the mongoDB
        MongoCollection<Document> collection = database.getCollection(cCollection); //get the collection

        collection.insertMany(doc); //insert documents into the collection

        System.out.println("There were --> " + collection.countDocuments() + " Documents uploaded");
    }

    /**
     * Method which inserts one documents at a time into the mongodb
     * @param txtPath
     * @param doc
     * @param cCollection
     * @throws IOException
     */
    public static void dbInserterOne(String txtPath, Document doc, String cCollection) throws IOException {
        MongoDatabase database = MongoDBConnectionHandler.dbConnector(txtPath);//connecting with the mongoDB
        MongoCollection<Document> collection = database.getCollection(cCollection);//get the collection

        collection.insertOne(doc);//insert documents into the collection

        System.out.println("There were --> " + collection.countDocuments() + " Documents uploaded");
    }

    /**
     * Method which deletes a document according an id.
     * method
     * @param id
     * @param collection
     */
    public static void delete(Integer id, MongoCollection collection){
        Document delete = new Document(); // we create a new document
        delete.put("_id", id); //we put the id into the document
        DeleteResult success = collection.deleteOne(delete); // we go through the collection and delete the Document with the id
        System.out.println(success.wasAcknowledged()); //return boolean which shows if successful

    }

    /**
     * method which inserts one document into the db
     * @param document
     * @param collection
     */
    public static void put(Document document, MongoCollection collection){
        collection.insertOne(document); //inserts into the desired collection

    }

    /**
     * method which finds a document from a collection and returns the result
     * @param document
     * @param collection
     * @return
     */
    public static FindIterable find(Document document, MongoCollection collection){
        FindIterable result = collection.find(document); // searches through the
        return result;
    }

    /**
     * method which updates a document.
     * @param document
     * @param collection
     * @return
     */
    public static Boolean update(Document document, MongoCollection collection) {
        FindIterable result = (FindIterable) collection.replaceOne((Bson) find(document, collection), document); //search and if found then replace
        if (result == null) {
            return false; // if not sucessful then return false
        } else {
            return true; //else true
        }
    }

    /**
     * method which counts the amount of documents in a collection.
     * @param collection
     * @return
     */
    public static long count(MongoCollection collection){
        long count = collection.countDocuments(); // counts the amount of documents of a specific collection
        return count; //return the number
    }

}


