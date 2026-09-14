package com.napier.sem;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class App
{
    public static void main(String[] args)
    {
        // Connect to MongoDB
        MongoClient mongoClient =
                MongoClients.create("mongodb://localhost:27000");

        // Get database
        MongoDatabase database = mongoClient.getDatabase("mydb");

        // Get collection
        MongoCollection<Document> collection =
                database.getCollection("test");

        // Create document
        Document doc = new Document("name", "Kevin Sim")
                .append("class", "Software Engineering Methods")
                .append("year", "2026")
                .append("result",
                        new Document("CW", 95)
                                .append("EX", 85));

        // Insert document
        collection.insertOne(doc);

        // Retrieve first document
        Document myDoc = collection.find().first();

        if (myDoc != null)
        {
            System.out.println(myDoc.toJson());
        }

        // Close connection
        mongoClient.close();
    }
}