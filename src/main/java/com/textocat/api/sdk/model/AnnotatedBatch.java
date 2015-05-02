package com.textocat.api.sdk.model;

import java.util.Set;

/**
 * @author Nikita Zhiltsov
 */
public class AnnotatedBatch extends RecognitionResult {
    private final Set<String> batchIds;

    public AnnotatedBatch(Set<String> batchIds, AnnotatedDocument[] documents) {
        super(documents);
        this.batchIds = batchIds;
    }

    public Set<String> getBatchIds() {
        return batchIds;
    }

}
