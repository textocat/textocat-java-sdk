package com.textocat.api.sdk.model;

/**
 * @author Nikita Zhiltsov
 */
public abstract class RecognitionResult {
    protected final AnnotatedDocument[] documents;

    public RecognitionResult(AnnotatedDocument[] documents) {
        this.documents = documents;
    }

    public AnnotatedDocument[] getDocuments() {
        return documents;
    }
}
