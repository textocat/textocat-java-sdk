package com.textocat.api.sdk.model;

/**
 * @author Nikita Zhiltsov
 */
public class AnnotatedBatch extends BatchMetadata {
    private final AnnotatedDocument[] documents;

    public AnnotatedBatch(String id, BatchStatus status, AnnotatedDocument[] documents) {
        super(id, status);
        this.documents = documents;
    }

    public AnnotatedDocument[] getDocuments() {
        return documents;
    }

}
