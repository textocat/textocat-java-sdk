package com.textocat.api.sdk.model;

import java.util.Set;

/**
 * @author Nikita Zhiltsov
 */
public class AnnotatedBatch {
    private final Set<String> batchIds;

    private final AnnotatedDocument[] documents;

    public AnnotatedBatch(Set<String> batchIds, AnnotatedDocument[] documents) {
        this.batchIds = batchIds;
        this.documents = documents;
    }

    public Set<String> getBatchIds() {
        return batchIds;
    }

    public AnnotatedDocument[] getDocuments() {
        return documents;
    }

}
