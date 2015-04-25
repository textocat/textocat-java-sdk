package com.textocat.api.sdk.model;

import com.google.gson.Gson;

import java.util.Arrays;

/**
 * @author Nikita Zhiltsov
 */
public class Batch extends BatchMetadata {
    /**
     *  documents
     */
    private final Document[] documents;

    public Batch(Document[] documents) {
        this.documents = documents;
    }

    public Document[] getDocuments() {
        return documents;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(documents);
    }
}
